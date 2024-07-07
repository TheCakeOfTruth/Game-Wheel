package code;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Config {
    // Singleton instance
    private static Config instance;

    // Patterns for parsing variables
    private final Pattern leftSide = Pattern.compile("^( )*[\\d\\w_]+( )*=");
    private final Pattern rightSide = Pattern.compile("=( )*(\\d+|\".*\")( )*($|#)");

    // filePath
    private String filePath = "config.cfg";

    // Variable map
    private TreeMap<String, String> configVars;

    // Private constructor & public getter
    private Config() {}
    public static Config getInstance() {if(instance == null) {instance = new Config();}; return instance;}
    
    // Initializes the variable map
    private void fetchConfigVars() {
        // Reset the map
        configVars = new TreeMap<>();
        try{
            // File reader
            BufferedReader configReader = new BufferedReader(new FileReader(filePath));
            String line = configReader.readLine();
            while (line != null) {
                // Skip commented-out lines
                if(line.startsWith("#")) {line = configReader.readLine(); continue;}

                // Variable definition
                String varName, varValue;

                // Find the left side of the definition
                Matcher leftMatch = leftSide.matcher(line);
                if(leftMatch.find()) {
                    // Get the match
                    varName = leftMatch.group();
                    // Remove the ending "=" and any heading/trailing spaces
                    varName = varName.substring(0, varName.length() - 1).trim();
                }
                // If there's an error, skip the line
                else {line = configReader.readLine(); continue;}

                // Find the right side of the definition
                Matcher rightMatch = rightSide.matcher(line);
                if(rightMatch.find()) {
                    // Get the match
                    varValue = rightMatch.group();
                    // Remove the starting "="
                    varValue = varValue.substring(1);
                    // If applicable, remove the ending "#"
                    if(varValue.endsWith("#")) {varValue = varValue.substring(0, varValue.length() - 1);}
                    // Remove heading/trailing spaces
                    varValue = varValue.trim();
                    // Remove quotes if present
                    if(varValue.startsWith("\"")) {varValue = varValue.substring(1, varValue.length() - 1);}
                }
                // If there's an error, skip the line
                else {line = configReader.readLine(); continue;}

                // Add to the map
                configVars.put(varName, varValue);

                // Move forward one line
                line = configReader.readLine();
            }

            // Close the parser
            configReader.close();
        }
        catch(FileNotFoundException e) {/* CAN'T FIND CONFIG FILE. HANDLE ERROR ACCORDINGLY. */}
        catch(IOException e) {/* SOMETHING WENT WRONG READING THE CONFIG FILE. IDK WHAT TO DO HERE. */}
    }

    // Variable getters/setters
    public void setFilePath(String filePath) {this.filePath = filePath;}
    public String getFilePath() {return filePath;}

    // Get the value of a config variable
    public String get(String key) {
        // Initialize the map if needed
        if(configVars == null) {fetchConfigVars();}
        // Return the desired value
        return configVars.get(key);
    }

    public static void main(String[] args) {
        System.out.println(getInstance().get("steamUserID"));
    }
}
