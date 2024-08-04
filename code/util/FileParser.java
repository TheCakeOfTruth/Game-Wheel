package code.util;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FileParser {
    private static final Pattern leftSide = Pattern.compile("^( )*[\\d\\w_]+( )*=");
    private static final Pattern rightSide = Pattern.compile("=( )*(\\d+|\".*\")( )*($|#)");

    private String filePath;
    private TreeMap<String, String> vars;

    public FileParser(String filePath) {
        this.filePath = filePath;
    }

    // Variable getters/setters
    public void setFilePath(String filePath) {this.filePath = filePath; vars = null;}
    public String getFilePath() {return filePath;}

    // Get the value of a config variable
    public String get(String key) {
        // Initialize the map if needed
        if(vars == null) {parse();}
        // Return the desired value
        return vars.get(key);
    }

    // Initializes the variable map
    public void parse() {
        // Reset the map
        TreeMap<String, String> vars = new TreeMap<>();
        try{
            // File reader
            BufferedReader fileReader = new BufferedReader(new FileReader(filePath));
            String line = fileReader.readLine();
            while (line != null) {
                // Skip commented-out lines
                if(line.startsWith("#")) {line = fileReader.readLine(); continue;}

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
                else {line = fileReader.readLine(); continue;}

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
                else {line = fileReader.readLine(); continue;}

                // Add to the map
                vars.put(varName, varValue);

                // Move forward one line
                line = fileReader.readLine();
            }

            // Close the parser
            fileReader.close();
        }
        catch(FileNotFoundException e) {System.out.println(e.getMessage()); /* CAN'T FIND CONFIG FILE. HANDLE ERROR ACCORDINGLY. */}
        catch(IOException e) {System.out.println(e.getMessage()); /* SOMETHING WENT WRONG READING THE CONFIG FILE. IDK WHAT TO DO HERE. */}
    }
}
