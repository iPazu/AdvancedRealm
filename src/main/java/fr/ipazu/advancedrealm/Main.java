package fr.ipazu.advancedrealm;

import fr.ipazu.advancedrealm.commands.*;
import fr.ipazu.advancedrealm.events.EventManager;
import fr.ipazu.advancedrealm.realm.Realm;
import fr.ipazu.advancedrealm.utils.ARExpansion;
import fr.ipazu.advancedrealm.utils.ConfigFiles;
import fr.ipazu.advancedrealm.utils.Metrics;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.concurrent.Callable;

public class Main extends JavaPlugin {
    private static Main instance;
    public Economy economy = null;
    public static Metrics metrics;
    public static Class<?> wrapperclass;

    public static Main getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        instance = this;
        if(Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null){
            new ARExpansion(this).register();
        }
        new ConfigFiles().init();
        new EventManager(this);
        loadWrapperClass();
        getCommand("unclaim").setExecutor(new Unclaim());
        getCommand("claim").setExecutor(new Claim());
        getCommand("realm").setExecutor(new RealmCommand());
        getCommand("home").setExecutor(new Home());
        getCommand("visit").setExecutor(new Visit());
        getCommand("configrealm").setExecutor(new ConfigRealm());
        pushMetrics();
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

    public static Metrics getMetrics() {
        return metrics;
    }

    private void pushMetrics() {
        metrics = new Metrics(this);
        metrics.addCustomChart(new Metrics.SingleLineChart("realms_created", new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                return Realm.allrealm.size();
            }
        }));
        System.out.println("[AdvancedRealm] Metrics successfully pushed (" + Realm.allrealm.size() + " realms)");
    }

    public void loadWrapperClass() {
        URLClassLoader child = null;
        try {
            child = new URLClassLoader(new URL[] {new URL("file:///"+Main.getInstance().getDataFolder().getAbsolutePath()+"/../ARWrapper.jar")}, Main.class.getClassLoader());
            wrapperclass = child.loadClass("fr.ipazu.arwrapper.SchematicWrapper");

        } catch (MalformedURLException | ClassNotFoundException e) {
            e.printStackTrace();
        }

    }
}