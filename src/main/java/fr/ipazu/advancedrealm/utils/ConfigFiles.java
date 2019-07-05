package fr.ipazu.advancedrealm.utils;

import fr.ipazu.advancedrealm.Main;
import fr.ipazu.advancedrealm.realm.RealmConfig;
import fr.ipazu.advancedrealm.realm.RealmLevel;
import fr.ipazu.advancedrealm.realm.themes.ThemeConfig;
import org.apache.commons.io.FileUtils;
import org.bukkit.*;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.logging.Level;

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
        pasteFiles();
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
        Verification.check();

    }


    public void loadConfig() {
        YamlConfiguration config = Config.CONFIG.getConfig();
        loadWorlds(config);
        spawn = new Location(Bukkit.getWorld(config.getString("config.spawn.world")), config.getInt("config.spawn.x"), config.getInt("config.spawn.y"), config.getInt("config.spawn.z"), (float) config.getInt("config.spawn.yaw"), (float) config.getInt("config.spawn.pitch"));
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

    private void loadUpgrades() {
        YamlConfiguration config = Config.UPGRADES.getConfig();
        ConfigurationSection levelsection = config.getConfigurationSection("levels");
        for (String s : levelsection.getKeys(false)) {
            new RealmLevel(Integer.parseInt(s), levelsection.getInt(s + ".cost"), levelsection.getInt(s + ".bordersize"), levelsection.getInt(s + ".maxplayer"));
        }
    }

    private void loadWorlds(YamlConfiguration config) {
        if (!new File(Main.getInstance().getDataFolder().getPath() + "/../../" + config.getString("config.world")).exists() && !Bukkit.getVersion().contains("1.14")) {
           System.out.println("[AdvancedRealm] Creating void world called " + config.getString("config.world"));
            WorldCreator wc = new WorldCreator(config.getString("config.world"));
            wc.type(WorldType.FLAT);
            wc.generator(VoidWorld.getDefaultWorldGenerator());
            wc.createWorld();
        }
        if (Bukkit.getWorld(config.getString("config.spawn.world")) == null) {
            Bukkit.createWorld(WorldCreator.name(config.getString("config.spawn.world")));
            System.out.println("[AdvancedRealm] Spawn world loaded: " + config.getString("config.spawn.world"));

        }
        if (Bukkit.getWorld(config.getString("config.world")) == null) {
            Bukkit.createWorld(WorldCreator.name(config.getString("config.world")));
            System.out.println("[AdvancedRealm] Realm world loaded: " + config.getString("config.world"));
        }


    }

    private void pasteFiles() {
        if (!new File(Main.getInstance().getDataFolder()+"/island.schematic").exists()) {
            System.out.println("[AdvancedRealm] Creating island schematic");
            copy(getClass().getResourceAsStream("/schematics/island.schematic"),Main.getInstance().getDataFolder().getPath() + "/island.schematic");
        }
        if (!new File(Main.getInstance().getDataFolder() + "/theme/basictheme.schematic").exists()) {
            System.out.println("[AdvancedRealm] Creating basic theme schematic");
            copy(getClass().getResourceAsStream("/schematics/theme/basictheme.schematic"), Main.getInstance().getDataFolder().getPath() + "/theme/basictheme.schematic");
        }
        if (Bukkit.getVersion().contains("1.13") || Bukkit.getVersion().contains("1.14") && !new File(Main.getInstance().getDataFolder().getPath() + "/../ARWrapper.jar").exists()) {
            System.out.println("[AdvancedRealm] Creating ARwrapper");
            copy(getClass().getResourceAsStream("/ARWrapper-1.0.jar"), Main.getInstance().getDataFolder().getPath() + "/../ARWrapper.jar");
        }
    }
    public static boolean copy(InputStream source , String destination) {

        boolean succeess = true;

        System.out.println("Copying ->" + source + "\n\tto ->" + destination);

        try {
            Files.copy(source, Paths.get(destination), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException ex) {
            System.out.println("[AdvancedRealm] Error while creating files");
            ex.printStackTrace();
        }

        return succeess;

    }
}
