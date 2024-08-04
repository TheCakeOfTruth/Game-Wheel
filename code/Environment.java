package code;
import java.util.TreeMap;

import code.util.Parser;

public class Environment {
    // Singleton instance
    private static Environment instance;

    // filePath
    private String filePath = ".env";

    // Variable map
    private TreeMap<String, String> environmentVars;

    // Private constructor & public getter
    private Environment() {}
    public static Environment getInstance() {if(instance == null) {instance = new Environment();}; return instance;}
    
    // Initializes the variable map
    private void fetchEnvironmentVars() {
        environmentVars = Parser.parse(filePath);
    }

    // Variable getters/setters
    public void setFilePath(String filePath) {this.filePath = filePath;}
    public String getFilePath() {return filePath;}

    // Get the value of a config variable
    public String get(String key) {
        // Initialize the map if needed
        if(environmentVars == null) {fetchEnvironmentVars();}
        // Return the desired value
        return environmentVars.get(key);
    }
}
