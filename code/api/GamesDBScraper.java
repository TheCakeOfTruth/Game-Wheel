package code.api;

import java.util.regex.Pattern;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.TreeMap;
import java.util.regex.Matcher;

import code.util.WebUtil;

public class GamesDBScraper {
    // Singleton instance
    private static GamesDBScraper instance;
    
    // Base site
    private String siteURL = "https://thegamesdb.net/search.php";
    
    // Parsing patterns
    private Pattern optionPattern = Pattern.compile("<option.*</option>");
    private Pattern valuePattern = Pattern.compile("=\"\\d+\"");
    private Pattern platformPattern = Pattern.compile(">.*<");
    private Pattern imgPattern = Pattern.compile("<img class=[^>]*>");
    private Pattern srcPattern = Pattern.compile("src=\".*\"");

    // Map of platform filter IDs
    private TreeMap<String, Integer> platforms;

    // Constructor
    private GamesDBScraper() {}
    public static GamesDBScraper getInstance() {if (instance == null) {instance = new GamesDBScraper();}; return instance;}

    // Populates the platform map
    public void fetchPlatformMap() {
        // Get base search HTML & initialize map
        String result = WebUtil.getResponse(siteURL);
        platforms = new TreeMap<>();

        // For all options,
        Matcher optionMatcher = optionPattern.matcher(result);
        while (optionMatcher.find()) {
            String platformOption = optionMatcher.group();
            String platform = "";
            String value = "";

            // Get the value
            Matcher valueMatcher = valuePattern.matcher(platformOption);
            if (valueMatcher.find()) {value = valueMatcher.group();}
            value = value.substring(2, value.length() - 1);

            // Get the platform
            Matcher platformMatcher = platformPattern.matcher(platformOption);
            if (platformMatcher.find()) {platform = platformMatcher.group();}
            platform = platform.substring(1, platform.length() - 1);

            // Add to the map
            platforms.put(platform, Integer.valueOf(value));
        }
    }

    public TreeMap<String, Integer> getPlatforms() {if (platforms == null) {fetchPlatformMap();}; return platforms;}

    // Platform filter ID getter w/ default
    private int tryPlatform(String platformName) {
        Integer result = getPlatforms().get(platformName);
        if (platformName == null) {return 0;}
        if (result == null) {return 0;}
        return result;
    }

    // Gets the URL to the cover image of the game
    public String getCoverImageURL(String gameName, String platformName) {
        // Get the search page
        String result = WebUtil.getResponse(siteURL + "?name=" + URLEncoder.encode(gameName, StandardCharsets.UTF_8) + "&platform_id%5B%5D=" + tryPlatform(platformName));

        // Grab the img class of the first result
        String url = null;
        Matcher imgFinder = imgPattern.matcher(result);
        if (imgFinder.find()) {
            // Isolate the image url
            url = imgFinder.group();
            Matcher srcMatcher = srcPattern.matcher(url);
            if (srcMatcher.find()) {
                url = srcMatcher.group();
                url = url.substring(5, url.length() - 1);
            }
        } 

        // Return
        return url;
    }

    public static void main(String[] args) throws Exception {
        System.out.println(getInstance().getCoverImageURL("Super Mario 64 DS", "Nintendo Scrungler"));
    }
}
