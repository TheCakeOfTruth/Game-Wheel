package code.util;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;

import javax.imageio.ImageIO;

public class WebUtil {
    // Returns the JSON or HTML response from a URL as a String
    public static String getResponse(String url) {
        try{
            // Open the connection and begin reading
            HttpURLConnection connection = (HttpURLConnection) new URI(url).toURL().openConnection();
            BufferedReader rd = new BufferedReader(new InputStreamReader(connection.getInputStream()));

            // Build the response into the String
            String response = "";
            String line = rd.readLine();
            while (line != null) {response += line + "\n"; line = rd.readLine();}

            // Disconnect/close
            connection.disconnect();
            rd.close();

            // Return
            return response;
        }
        catch (URISyntaxException e) {/* SOMETHING WRONG WITH URL */}
        catch (IOException e) {/* PROBLEM READING THE RESPONSE */}

        return null;
    }

    // Returns a BufferedImage from a web URL
    public static BufferedImage getImageFromURL(String url) {
        if (url != null) {
            try {
                return ImageIO.read(new URI(url).toURL());
            }
            catch(URISyntaxException e) {/* SOMETHING WRONG WITH URL */}
            catch(IOException e) {/* CAN'T WRITE TO IMAGE */}
        }
        
        // Pleases the compiler (i can put a fallback image here in the future, as well)
        return null;
    }
}
