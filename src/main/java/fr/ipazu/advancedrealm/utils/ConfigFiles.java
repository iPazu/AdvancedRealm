package fr.ipazu.advancedrealm.utils;

import fr.ipazu.advancedrealm.Main;
import fr.ipazu.advancedrealm.realm.RealmConfig;
import fr.ipazu.advancedrealm.realm.RealmLevel;
import fr.ipazu.advancedrealm.realm.themes.ThemeConfig;
import org.bukkit.*;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;

public class ConfigFiles {
    private static Location spawn;
    private static Inventory realmchest;
    private static World realmworld;
    private static long cooldown;

    private void checkFolder() {
        if (!Main.getInstance().getDataFolder().exists())
            Main.getInstance().getDataFolder().mkdir();
        File themedir = new File(Main.getInstance().getDataFolder().getPath() + "/theme");
        if (!themedir.exists())
            themedir.mkdir();
    }


    public void init() {
        checkFolder();
        initConfigs();
        loadConfig();
        loadUpgrades();
        System.out.println("[AdvancedRealm] Starting loading themes ...");
        new ThemeConfig().loadAllThemes();
        System.out.println("[AdvancedRealm] Starting loading realms ...");
        new RealmConfig().loadAllRealm();
    }

    public void loadConfig() {
        YamlConfiguration config = Config.CONFIG.getConfig();
        if (Bukkit.getWorld(config.getString("config.spawn.world")) == null) {
            Bukkit.createWorld(WorldCreator.name(config.getString("config.spawn.world")));

        }
        spawn = new Location(Bukkit.getWorld(config.getString("config.spawn.world")), config.getInt("config.spawn.x"), config.getInt("config.spawn.y"), config.getInt("config.spawn.z"), (float) config.getInt("config.spawn.yaw"), (float) config.getInt("config.spawn.pitch"));
        if (Bukkit.getWorld(config.getString("config.world")) == null) {
            Bukkit.createWorld(WorldCreator.name(config.getString("config.world")));
        }
        realmworld = Bukkit.getWorld(config.getString("config.world"));
        if (config.getString("config.chest") != null) {
            try {
                realmchest = InventorySerialization.fromBase64(config.getString("config.chest"));
            } catch (Exception ex) {
                System.out.println("[AdvancedRealm] Error while loading the config.yml file in the chest section, check if it is deleted or try to reinstall the plugin. If you don't sucess at solving the problem you can contact iPazu#3982 at discord");

            }
        }
        cooldown = getCooldown();

    }

    private void initConfigs() {
        Arrays.stream(Config.values()).forEach(config -> {
            try {
                if (!config.getFile().exists())
                    config.getFile().createNewFile();

                if (config.getCopyDefault())
                    copyDefault(config.getFileName(), config.getFile());

                config.setConfig(YamlConfiguration.loadConfiguration(config.getFile()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public void copyDefault(String configname, File file) throws IOException {
        YamlConfiguration config = YamlConfiguration.loadConfiguration(new File(Main.getInstance().getDataFolder(), configname));
        YamlConfiguration sourceconfig = YamlConfiguration.loadConfiguration(new InputStreamReader(Main.getInstance().getResource(configname), StandardCharsets.UTF_8));
        for (String s : sourceconfig.getKeys(true)) {
            if (config.get(s) == null) {
                config.set(s, sourceconfig.get(s));
            }

        }
        config.save(file);
    }

    public void sendToSpawn(Player player) {
        player.teleport(spawn);
        WorldBorder.sendBorder(new Location(realmworld, 0, 50, 0), 30000000, player);
    }

    public static int getRealmspacing() {
        return Config.CONFIG.getConfig().getInt("config.spacing");
    }

    public void setRealmchest(Inventory inv) {
        Config.CONFIG.getConfig().set("realmchest", InventorySerialization.toBase64(inv));
        try {
            Config.CONFIG.getConfig().save(Config.CONFIG.getFile());
        } catch (Exception ex) {
            System.out.println("[AdvancedRealm] Error while editing the config.yml file in the chest section, check if it is deleted or try to reinstall the plugin. If you don't sucess at solving the problem you can contact iPazu#3982 at discord");

        }
    }

    public static Inventory getRealmchest() {
        return realmchest;
    }

    public static Location getSpawn() {
        return spawn;
    }


    public static World getWorld() {
        return realmworld;
    }

    public static long getCooldownValue() {
        return cooldown;
    }

    private long getCooldown() {
        long cooldown = 0;
        for (String s : Config.CONFIG.getConfig().getString("vote.cooldown").split("/")) {
            if (s.contains("d")) {
                cooldown += 86400000 * Integer.parseInt(stripNonDigits(s));
            }
            if (s.contains("h")) {
                cooldown += 3600000 * Integer.parseInt(stripNonDigits(s));
            }
            if (s.contains("m")) {
                cooldown += 60000 * Integer.parseInt(stripNonDigits(s));
            }
            if (s.contains("s")) {
                cooldown += 1000 * Integer.parseInt(stripNonDigits(s));
            }

        }
        return cooldown;
    }

    public static String stripNonDigits(
            final CharSequence input /* inspired by seh's comment */) {
        final StringBuilder sb = new StringBuilder(
                input.length() /* also inspired by seh's comment */);
        for (int i = 0; i < input.length(); i++) {
            final char c = input.charAt(i);
            if (c > 47 && c < 58) {
                sb.append(c);
            }
        }
        return sb.toString();
    }
    private void loadUpgrades(){
        YamlConfiguration config = Config.UPGRADES.getConfig();
        ConfigurationSection levelsection = config.getConfigurationSection("levels");
        for(String s : levelsection.getKeys(false)){
            new RealmLevel(Integer.parseInt(s),levelsection.getInt(s+".cost"),levelsection.getInt(s+".bordersize"),levelsection.getInt(s+".maxplayer"));
        }
    }
}
