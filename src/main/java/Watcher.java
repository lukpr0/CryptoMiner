package main.java;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Watcher extends Thread {
    private static Pattern pattern;
    public void run () {
        while (true) {
            System.out.println("[Watcher] Checking time..");
            try {
                HashClient.getLatestParent();
                Thread.sleep(2000);
                int time = getSeconds(getTimeFromSite());
                Thread.sleep(1000*time/5);
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    static {
        pattern = Pattern.compile("Hash/s - \\d*[ms]");
    }
    public static int getSeconds(String res) {
        Matcher matcher = pattern.matcher(res);
        matcher.find();
        String time = matcher.group().replace("Hash/s - ", "");
        int factor = time.endsWith("s") ? 1 : 60;
        int seconds = factor * Integer.parseInt(time.substring(0,time.length()-1));
        return seconds;
    }

    public static String getTimeFromSite() throws IOException, InterruptedException {

        BufferedReader in = RequestHandler.sendRequestToBufferedReader("http://hash.h10a.de");

        StringBuilder sb = new StringBuilder();
        while (in.ready()) {
            sb.append(in.readLine());
        }
        return sb.toString();

    }
}
