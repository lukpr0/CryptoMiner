import java.security.NoSuchAlgorithmException;

public class HashRunner extends Thread {
    private int number=0;
    private String name;
    public void init(int i, String name) {
        number = i;
        this.name = name;
    }
    public void run() {
        System.out.printf("Thread no. %d started..%n", number);
        HashClient hasher= null;
        try {
            hasher = new HashClient("SHA-256",name, number);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        String seed;
        String parent=hasher.getLatestParent();
        while(true)
        {
            seed = hasher.findSeed(parent);
            hasher.sendSeed(parent,seed);
            parent = hasher.toHex(hasher.getHash(parent,seed));
        }
    }
}
