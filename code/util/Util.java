package code.util;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import javax.imageio.ImageIO;

public class Util {
    // Returns the JSON or HTML response from a URL as a String
    public static String getResponse(String url) {
        try{
            // Open the connection and begin reading
            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
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
        catch(MalformedURLException e) {/* SOMETHING WRONG WITH URL */}
        catch(IOException e) {/* PROBLEM READING THE RESPONSE */}

        return null;
    }

    // Returns a BufferedImage from a web URL
    public static BufferedImage getImageFromURL(String url) {
        if (url != null) {
            try {
                return ImageIO.read(new URL(url));
            }
            catch(MalformedURLException e) {/* SOMETHING WRONG WITH URL */}
            catch(IOException e) {/* CAN'T WRITE TO IMAGE */}
        }
        
        // Pleases the compiler (i can put a fallback image here in the future, as well)
        return null;
    }
}
