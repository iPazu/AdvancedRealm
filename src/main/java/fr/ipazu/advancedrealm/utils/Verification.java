package fr.ipazu.advancedrealm.utils;

import fr.ipazu.advancedrealm.Main;
import org.bukkit.Bukkit;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

public class Verification {
    private static String id = "MineSpira" ;
    private String ressource = "%%__RESOURCE__%%";
    private String nonce = "%%__NONCE__%%";
    private static ArrayList<String> blackid = new ArrayList<>();

    public static void disablePlugin() {
        Bukkit.getServer().getPluginManager().disablePlugin(Main.getInstance());
    }

    public static void check() {
       loadBlackId();
       if(blackid.contains(id)){
           System.out.println("[AdvancedRealm] You are using a leaked version of AdvancedRealm");
           System.out.println("[AdvancedRealm] The plugin turned off and your license will be banned");
           System.out.println("[AdvancedRealm] Leaks destroy the work of developpers, if you want to have the plugin you can buy it on spigot");
           disablePlugin();
       }
    }
    private static void loadBlackId() {
        try {
            URL url = new URL("https://gitlab.com/iPazu/advancedrealm-api/raw/master/blacklicence");
            Scanner in = new Scanner(url.openStream());

            while (in.hasNextLine()) {
               blackid.add(in.nextLine());

            }
            in.close();
        }
        catch (Exception e) {
            System.out.println("[AdvancedRealm] Failed the verification, please report this to iPazu#9120");
        }
    }
    private void useless()
    {
        ArrayList<String> strs = new ArrayList<>();
        strs.forEach(System.out::println);
    }
}
