package code;
import java.util.TreeMap;

import code.util.Parser;

public class Config {
    // Singleton instance
    private static Config instance;

    // filePath
    private String filePath = "config.cfg";

    // Variable map
    private TreeMap<String, String> configVars;

    // Private constructor & public getter
    private Config() {}
    public static Config getInstance() {if(instance == null) {instance = new Config();}; return instance;}
    
    // Initializes the variable map
    private void fetchConfigVars() {
        configVars = Parser.parse(filePath);
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
}
