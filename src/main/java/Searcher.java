package main.java;

import javax.imageio.IIOException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Searcher {
    private static Pattern namePattern;
    private static Pattern hashPattern;
    public static String checkHash(String hash) throws IOException, InterruptedException {
        BufferedReader in = RequestHandler.sendRequestToBufferedReader("http://hash.h10a.de/?check="+hash);

        StringBuilder sb = new StringBuilder();
        while (in.ready()) {
            sb.append(in.readLine());
        }
        Logger.log(sb.toString(), Logger.DEBUG);
        in.close();
        return sb.toString();
    }

    static {
        namePattern = Pattern.compile("by .*? at length");
        hashPattern = Pattern.compile("<tt>.*</tt> by ");
    }

    public static String getParticipant(String res) {
        Matcher matcher = namePattern.matcher(res);
        matcher.find();
        String name = matcher.group().replace("by ", "").replace(" at length","");
        Logger.log(name, Logger.DEBUG);
        return name;
    }

    public static String getHash(String res) {
        Matcher matcher = hashPattern.matcher(res);
        matcher.find();
        String hash = matcher.group().replace("<tt>", "").replace("</tt> by","");
        Logger.log(hash, Logger.DEBUG);
        return hash;
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        String s = getParticipant(checkHash("000000007d881b956aa8ec86d6957d01df3397db57c437fe61bd4d5699251e89"));
    }
}
