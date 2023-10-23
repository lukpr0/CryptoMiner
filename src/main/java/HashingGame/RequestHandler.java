package HashingGame;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class RequestHandler {
    public static BufferedReader sendRequestToBufferedReader(String url) throws IOException, InterruptedException {
        URL server = new URL(url);
        HttpURLConnection yc = (HttpURLConnection) server.openConnection();

        if (yc.getResponseCode()>=400) {
            Logger.log(String.format("Error %d occurred, retrying in 2s", yc.getResponseCode()), Logger.DEBUG);
            Thread.sleep(2000);
            return sendRequestToBufferedReader(url);
        }

        BufferedReader in = new BufferedReader(new InputStreamReader(
                yc.getInputStream()));
        return in;
    }
}
