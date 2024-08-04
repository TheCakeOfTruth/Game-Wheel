package code.api;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import code.Main;
import code.games.Game;
import code.games.SteamGame;
import code.util.WebUtil;

public class SteamAPI {
    // Singleton instance
    private static SteamAPI instance;

    // Default variables
    private String apiKey = Main.env.get("STEAM_API_KEY");
    private String steamID = Main.config.get("steamUserID");
    private ArrayList<Game> games; 

    // Private constructor & public getter
    private SteamAPI() {}
    public static SteamAPI getInstance() {if(instance == null) {instance = new SteamAPI();}; return instance;}

    private void fetchSteamGames() {
        // Get data from API
        String str = WebUtil.getResponse("http://api.steampowered.com/IPlayerService/GetOwnedGames/v0001/?key=" + apiKey + "&steamid=" + steamID + "&include_appinfo=true&format=json");
        if (str == null) {return;}

        // Parse the JSON API response into SteamGame objects
        games = new ArrayList<>();
        JSONArray data = new JSONObject(str).getJSONObject("response").getJSONArray("games");
        for (int i = 0; i < data.length(); i++) {
            int appid = data.getJSONObject(i).getInt("appid");
            String name = data.getJSONObject(i).getString("name");

            games.add(new SteamGame(appid, name));
        }
    }

    // Variable getters/setters
    public void setApiKey(String apiKey) {this.apiKey = apiKey;}
    public String getApiKey() {return apiKey;}
    public void setSteamID(String steamID) {this.steamID = steamID;}
    public String getSteamID() {return steamID;}
    public ArrayList<Game> getGames() {if(games == null) {fetchSteamGames();}; return games;}
}
