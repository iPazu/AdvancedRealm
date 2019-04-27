package fr.ipazu.advancedrealm;

import fr.ipazu.advancedrealm.commands.*;
import fr.ipazu.advancedrealm.events.EventManager;
import fr.ipazu.advancedrealm.utils.ConfigFiles;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
    private static Main instance;
    public Economy economy = null;

    public static Main getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        instance = this;
        new ConfigFiles().init();
        new EventManager(this);
        getCommand("unclaim").setExecutor(new Unclaim());
        getCommand("claim").setExecutor(new Claim());
        getCommand("cell").setExecutor(new RealmCommand());
        getCommand("home").setExecutor(new Home());
        getCommand("visit").setExecutor(new Visit());
        getCommand("configrealm").setExecutor(new ConfigRealm());

    }

    @Override
    public void onDisable() {
    }

    public boolean setupEconomy() {
        RegisteredServiceProvider<Economy> economyProvider = Main.getInstance().getServer().getServicesManager().getRegistration(Economy.class);
        if (economyProvider != null) {
            economy = economyProvider.getProvider();
        }
        return (economy != null);
    }

}