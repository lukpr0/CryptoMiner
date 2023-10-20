import java.security.NoSuchAlgorithmException;

public class HashRunner extends Thread {
    private int number=0;
    private String name;
    private String parent;
    public void init(int i, String name) {
        number = i;
        this.name = name;
        this.parent = parent;
    }
    public void run() {
        System.out.printf("[Thread %d] started..%n", number);
        HashClient hasher= null;
        try {
            hasher = new HashClient("SHA-256",name, number);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        String seed;

        while(true)
        {
            seed = hasher.findSeed();
            hasher.sendSeed(seed);
            HashClient.getLatestParent();
        }
    }
}
