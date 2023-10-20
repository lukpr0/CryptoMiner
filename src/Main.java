import java.security.NoSuchAlgorithmException;

public class Main {
    public static void main(String[] args) throws NoSuchAlgorithmException, InterruptedException {
        int n = args.length > 0 ? Integer.parseInt(args[0]) : 1;
        HashClient.getLatestParent();
        Watcher w = new Watcher();
        w.start();
        n = Runtime.getRuntime().availableProcessors();
        for (int i = 0; i < 16; i++) {
            HashRunner runner = new HashRunner();
            runner.init(i, "orpkul-B4");
            runner.start();
        }
        //System.out.println(new HashClient("SHA-256","orpkul-B4", 1).getLatestParent());

    }
}