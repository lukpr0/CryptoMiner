import java.security.NoSuchAlgorithmException;

public class Main {
    public static void main(String[] args) throws NoSuchAlgorithmException {
        for (int i = 0; i < 15; i++) {
            HashRunner runner = new HashRunner();
            runner.init(i);
            runner.start();
        }
        //System.out.println(new HashClient("SHA-256","orpkul-B4", 1).getLatestParent());
    }
}