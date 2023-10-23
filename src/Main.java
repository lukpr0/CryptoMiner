import main.java.HashClient;
import main.java.HashRunner;
import main.java.Logger;
import main.java.Watcher;

public class Main {

    static String name = "Your-Name-B4";

    public static void main(String[] args) {
        HashClient.getLatestParent();
        Watcher w = new Watcher();
        w.start();
        Logger.setLevel(Logger.DEBUG);
        Logger.log(String.format("Starting as %s", name), Logger.MINIMAL);
        int n = Runtime.getRuntime().availableProcessors();
        for (int i = 0; i < n-1; i++) {
            HashRunner runner = new HashRunner();
            runner.init(i, name);
            runner.start();
        }
    }
}