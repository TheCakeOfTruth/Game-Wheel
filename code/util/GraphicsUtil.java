package code.util;

import java.awt.Color;
import java.util.Random;

public class GraphicsUtil {
    // Random object
    private static Random rand = new Random();

    // Returns a random integer between 0 and max
    public static int randInt(int max) {
        return rand.nextInt(max);
    }

    // Converts a random integer to a color
    public static Color randomColor() {
        return new Color(randInt(16777216));
    }

    // Changes a color to have increased saturation and brightness
    public static Color colorFilter(Color in) {
        float[] vals = new float[3]; 
        Color.RGBtoHSB(in.getRed(), in.getGreen(), in.getBlue(), vals);
        
        vals[1] = (float) ((vals[1] % 0.4) + 0.4);
        vals[2] = (float) ((vals[2] % 0.5) + 0.5);

        Color out = new Color(Color.HSBtoRGB(vals[0], vals[1], vals[2]));
        
        return out;
    }

    // Hashes a string into a pseudo-random color
    public static Color strToColor(String in) {
        byte[] bin = in.getBytes();
        int sum = 0;
        for (int i = 0; i < bin.length; i++) {
            sum += (i + 1) * bin[i] * bin[i];
        }
        Color out = new Color((int) (Math.ceil(16777216 * (Math.sin(sum) + 1)) % 16777216));
        return out;
    }
}
