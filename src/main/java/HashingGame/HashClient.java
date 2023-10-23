package HashingGame;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.io.*;

public class HashClient {

    public final MessageDigest 	digest;
    public final String 		name;
    public static int			difficulty;

    private static String seed;
    private static String parent;
    private String currentParent;
    private static String lastHash="";

    //this hash will be used if no usable parent hash is found in the active hashes
    //this is not needed in the current implementation
    //make sure to update this if you plan on doing modification to the getParent function where it would be possible that it fails to find a parent
    //hashed you have found will be stored in the hashes.txt file
    private static String fallbackHash = "000000006ac97d488922f2328a2f20b97a5a1c36ee28c6738f0b7fa42527694f";

    private int number;

    HashClient(String aHash,String aName, int number) throws NoSuchAlgorithmException
    {
        digest     = MessageDigest.getInstance(aHash);
        name       = aName;
        this.number = number;
        currentParent = "";
    }

    public String getLine(final String parent,final String seed)
    {
        return parent+" "+name+" "+seed;
    }

    public byte [] getHash(final String parent,final String seed)
    {
        return digest.digest(getLine(parent,seed).getBytes());
    }

    private static final char[] HEX_ARRAY = "0123456789abcdef".toCharArray();
    public static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = HEX_ARRAY[v >>> 4];
            hexChars[j * 2 + 1] = HEX_ARRAY[v & 0x0F];
        }
        return new String(hexChars);
    }

    static public String toHex(byte [] hash)
    {
        return bytesToHex(hash);
    }

    public int countZerobits(final byte [] input)
    {
        int bits=0;
        int i;
        for(i=0;i<input.length && input[i]==0;i++)
            bits+=8;

        if(i<input.length)
        {
            byte remain=input[i];
            while(remain!=0 && (remain&0x80)==0)
            {
                bits++;
                remain=(byte) ((remain&0x7f)<<1);
            }
        }
        return bits;
    }

    static long lastGetParent=0;

    public static String getParent(String url)
    {
        // Do not hit the server too often to avoid being locked out.
        if(System.currentTimeMillis()-lastGetParent<2000) // Prevent being locked out
        {
            try {
                Thread.sleep(2100-(System.currentTimeMillis()-lastGetParent));
            }
            catch (Exception e)
            {
            }
        }
        lastGetParent=System.currentTimeMillis();

        Logger.log("================================================================================================",Logger.DEBUG);
        Logger.log(String.format("[Global] %s", url),Logger.DEBUG);
        try {
            int level=0;
            String parent="";

            BufferedReader in = RequestHandler.sendRequestToBufferedReader(url);
            String inputLine=in.readLine();
            Logger.log(String.format("[Global] %s ", url),Logger.DEBUG);
            if(inputLine!=null) {
                int newDifficulty=Integer.parseInt(inputLine);
                if(newDifficulty!=difficulty)
                {
                    Logger.log(String.format("[Global] Difficulty: %d", newDifficulty), Logger.DEBUG);
                    difficulty=newDifficulty;
                }
            }

            if (parent.length()==0) {
                if (lastHash.length()==0) lastHash = fallbackHash;
                parent = lastHash;
            }

            while ((inputLine = in.readLine()) != null) {
                Logger.log(String.format("[Global] %s", inputLine), Logger.DEBUG);
                String[] sarray=inputLine.split("\\t");
                String blockHash = sarray[0];
                int blockLevel = Integer.parseInt(sarray[1]);
                String blockCreator = sarray[2];
                if(blockLevel>=level) {
                    level=blockLevel;
                    parent=blockHash;
                }

                if (blockLevel == level && blockCreator.endsWith("-B4")) {
                    level = blockLevel;
                    parent = blockHash;
                }

            }
            //parent = "000000006fd02494ce295e08343200d1ad8a5eea1bb93c9e4a4abfa69815bfa4";
            in.close();
            Logger.log("================================================================================================",Logger.DEBUG);
            return parent;

        }
        catch (Exception e) {
            Logger.log("[Global] Failed.", Logger.DEBUG);
            Logger.log(String.format("[Global] %s", e.getMessage()), Logger.DEBUG);
            //System.exit(1);
        }
        return "";
    }

    public static String getLatestParent() {
        parent = getParent("http://hash.h10a.de/?raw2");
        return parent;
    }

    public String findSeed() {
        String seed;
        boolean done=false;
        int best=0;
        //long additionalData = Double.doubleToLongBits(Math.random())/2;
        do {

            if (!currentParent.equals(parent)) {
                best = 0;
                currentParent = parent;
            }

            seed=Long.toHexString(Double.doubleToLongBits(Double.doubleToLongBits(Math.random()))); // max 64 bits
            //additionalData++;
            byte [] hash=getHash(parent,seed);

            int count=countZerobits(hash);

            if(count>=difficulty) {
                Logger.log("",Logger.MINIMAL);
                Logger.log(String.format("[Thread %d] Done: %d %s", number, count, toHex(hash)), Logger.MINIMAL);
                done=true;
            }
            else if(count>difficulty-6) {
                best=count;
                String shortened = parent.substring(8,13) + "..." + parent.substring(60);
                Logger.log(String.format("[Thread %d] Best: %d %s (using parent: %s, D=%d)", number, count, toHex(hash), shortened, difficulty), Logger.INFO);
            }

        } while(!done);
        return seed;
    }

    public String sendSeed(String seed) {
        lastHash = getHashString(seed);
        //Thread.sleep(2000);
        return getParent("http://hash.h10a.de/?raw2&Z="+parent+"&P="+name+"&R="+seed);
    }

    public String getHashString(String parent,String seed) {
        return bytesToHex(digest.digest(getLine(parent,seed).getBytes()));
    }

    public String getHashString(String seed) {
        return getHashString(parent, seed);
    }
}