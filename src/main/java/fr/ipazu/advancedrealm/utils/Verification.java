package fr.ipazu.advancedrealm.utils;

import fr.ipazu.advancedrealm.Main;
import org.bukkit.Bukkit;

public class Verification {
    String id = "%%__USER__%%";
    String ressource = "%%__RESOURCE__%%";
    String nonce = "%%__NONCE__%%";


    public void disablePlugin(){
        Bukkit.getServer().getPluginManager().disablePlugin(Main.getInstance());
    }
    public void check(){

    }

    private void getBlackID(){

    }
}
