//used by NoIP updater
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class DyDNS {
    
    public static String getCurrentIP() throws IOException {
        URL url = new URL("https://ifconfig.me/");
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream(), StandardCharsets.UTF_8))) {
            return reader.readLine().trim();
        }
    }
    
    public static void NoIpUpdate(String username, String password, String hostname, String domain, String currentIP) throws IOException {
        String updateUrl = String.format("https://dynupdate.no-ip.com/nic/update?hostname=%s.%s", hostname, domain);
        URL url = new URL(updateUrl);

        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setDoOutput(true);
        String credentials = username + ":" + password;
        String base64Credentials = java.util.Base64.getEncoder().encodeToString(credentials.getBytes(StandardCharsets.UTF_8));
        connection.setRequestProperty("Authorization", "Basic " + base64Credentials);

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
            String response = reader.readLine();
            System.out.println("No-IP Update Response: " + response);
        }
    }

}