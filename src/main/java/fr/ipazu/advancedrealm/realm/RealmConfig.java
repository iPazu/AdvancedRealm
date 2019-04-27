package fr.ipazu.advancedrealm.realm;


import com.google.common.collect.Iterables;
import fr.ipazu.advancedrealm.Main;
import fr.ipazu.advancedrealm.realm.themes.ThemeType;
import fr.ipazu.advancedrealm.utils.ConfigFiles;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.ArrayList;

public class RealmConfig {
    private File file = new File(Main.getInstance().getDataFolder(), "realm.yml");
    private FileConfiguration config = YamlConfiguration.loadConfiguration(file);

    public RealmConfig() {

    }

    public void updateRealm(Realm realm) {
        config.set("realms." + realm.getOwner().getUniqueId() + ".owner.uuid", realm.getOwner().getUniqueId());
        config.set("realms." + realm.getOwner().getUniqueId() + ".owner.name", realm.getOwner().getName());
        config.set("realms." + realm.getOwner().getUniqueId() + ".privacy", realm.getPrivacy());
        config.set("realms." + realm.getOwner().getUniqueId() + ".level", realm.getLevel().getNumber());
        updateVote(realm);
        for (RealmPlayer bplayer : realm.getBanned()) {
            config.set("realms." + realm.getOwner().getUniqueId() + ".banned.", bplayer.getUniqueId());
        }
        config.set("realms." + realm.getOwner().getUniqueId() + ".theme.id", realm.getTheme().getThemeType().getName());
        config.set("realms." + realm.getOwner().getUniqueId() + ".theme.spawn.x", realm.getTheme().getSpawn().getBlockX());
        config.set("realms." + realm.getOwner().getUniqueId() + ".theme.spawn.y", realm.getTheme().getSpawn().getBlockY());
        config.set("realms." + realm.getOwner().getUniqueId() + ".theme.spawn.z", realm.getTheme().getSpawn().getBlockZ());
        config.set("realms." + realm.getOwner().getUniqueId() + ".theme.spawn.yaw", realm.getTheme().getSpawn().getYaw());
        config.set("realms." + realm.getOwner().getUniqueId() + ".theme.spawn.pitch", realm.getTheme().getSpawn().getPitch());
        try {
            config.save(file);
        } catch (Exception e) {
            System.out.println("[AdvancedRealm] Error while loading the realm.yml file, check if it is deleted or try to reinstall the plugin. If you don't sucess at solving the problem you can contact iPazu#3982 at discord");
            e.printStackTrace();
        }

    }

    public void addPlayer(RealmPlayer rplayer, Realm realm) {
        config.set("realms." + realm.getOwner().getUniqueId() + ".players." + rplayer.getUniqueId() + ".name", rplayer.getName());
        try {
            config.save(file);
        } catch (Exception e) {
            System.out.println("[AdvancedRealm] Error while loading the realm.yml file, check if it is deleted or try to reinstall the plugin. If you don't sucess at solving the problem you can contact iPazu#3982 at discord");
            e.printStackTrace();
        }
    }

    public void promotePlayer(RealmPlayer rplayer, Realm realm) {
        config.set("realms." + realm.getOwner().getUniqueId() + ".players." + rplayer.getUniqueId() + ".rank", rplayer.getRankByRealm(realm).toString());
        try {
            config.save(file);
        } catch (Exception e) {
            System.out.println("[AdvancedRealm] Error while loading the realm.yml file, check if it is deleted or try to reinstall the plugin. If you don't sucess at solving the problem you can contact iPazu#3982 at discord");
            e.printStackTrace();
        }
    }

    public void removePlayer(RealmPlayer rplayer, Realm realm) {
        config.set("realms." + realm.getOwner().getUniqueId() + ".players." + rplayer.getUniqueId(), null);
        try {
            config.save(file);
        } catch (Exception e) {
            System.out.println("[AdvancedRealm] Error while loading the realm.yml file, check if it is deleted or try to reinstall the plugin. If you don't sucess at solving the problem you can contact iPazu#3982 at discord");
            e.printStackTrace();
        }
    }

    public void banPlayer(RealmPlayer rplayer, Realm realm) {
        if (realm.getBanned().size() == 0)
            config.set("realms." + realm.getOwner().getUniqueId() + ".banned", null);
        else
            config.set("realms." + realm.getOwner().getUniqueId() + ".banned." + rplayer.getUniqueId() + ".name", rplayer.getName());
        try {
            config.save(file);
        } catch (Exception e) {
            System.out.println("[AdvancedRealm] Error while loading the realm.yml file, check if it is deleted or try to reinstall the plugin. If you don't sucess at solving the problem you can contact iPazu#3982 at discord");
            e.printStackTrace();
        }
    }

    public void unbanPlayer(RealmPlayer rplayer, Realm realm) {
        config.set("realms." + realm.getOwner().getUniqueId() + ".banned." + rplayer.getUniqueId(), null);
        try {
            config.save(file);
        } catch (Exception e) {
            System.out.println("[AdvancedRealm] Error while loading the realm.yml file, check if it is deleted or try to reinstall the plugin. If you don't sucess at solving the problem you can contact iPazu#3982 at discord");
            e.printStackTrace();
        }
    }

    public void addNewPlayer(Player p) {
        config.set("realmplayers." + p.getUniqueId() + ".uuid", p.getUniqueId().toString());
        config.set("realmplayers." + p.getUniqueId() + ".name", p.getName());
        try {
            config.save(file);
        } catch (Exception ex) {
            System.out.println("[AdvancedRealm] Error while loading the realm.yml file, check if it is deleted or try to reinstall the plugin. If you don't sucess at solving the problem you can contact iPazu#3982 at discord");
            ex.printStackTrace();
        }

        if (RealmPlayer.getPlayer(p.getUniqueId().toString()) == null)
            new RealmPlayer(p.getUniqueId().toString(), p.getName());

    }

    public void setPerk(Realm realm) {
        config.set("realms." + realm.getOwner().getUniqueId() + ".perk", realm.getPerk());
        try {
            config.save(file);
        } catch (Exception ex) {
            System.out.println("[AdvancedRealm] Error while loading the realm.yml file, check if it is deleted or try to reinstall the plugin. If you don't sucess at solving the problem you can contact iPazu#3982 at discord");
            ex.printStackTrace();
        }
    }

    public void setPrivacy(Realm realm) {
        config.set("realms." + realm.getOwner().getUniqueId() + ".privacy", realm.getPrivacy());
        try {
            config.save(file);
        } catch (Exception ex) {
            System.out.println("[AdvancedRealm] Error while loading the realm.yml file, check if it is deleted or try to reinstall the plugin. If you don't sucess at solving the problem you can contact iPazu#3982 at discord");
            ex.printStackTrace();
        }
    }

    public void delete(Realm realm) {
        if (Realm.allrealm.size() == 0) {
            config.set("realms", null);
        } else
            config.set("realms." + realm.getOwner().getUniqueId(), null);
        try {
            config.save(file);
        } catch (Exception ex) {
            System.out.println("[AdvancedRealm] Error while loading the realm.yml file, check if it is deleted or try to reinstall the plugin. If you don't sucess at solving the problem you can contact iPazu#3982 at discord");
            ex.printStackTrace();
        }
    }

    public void updatePlayerName(Player player) {
        RealmPlayer rp = RealmPlayer.getPlayer(player.getUniqueId().toString());
        for (Realm r : rp.getAllRealm()) {
            if (r.getOwner().getUniqueId().equals(player.getUniqueId().toString())) {
                config.set("realms." + r.getOwner().getUniqueId() + ".owner.name", player.getName());
            }
            config.set("realms." + r.getOwner().getUniqueId() + ".players." + rp.getUniqueId() + ".name", player.getName());
            rp.setName(player.getName());

        }
        for (String s : config.getConfigurationSection("realmplayers").getKeys(false)) {
            if (player.getUniqueId().toString().equals(s))
                config.set("realmplayers." + s + ".name", player.getName());
        }
        try {
            config.save(file);
        } catch (Exception ex) {
            System.out.println("[AdvancedRealm] Error while loading the realm.yml file, check if it is deleted or try to reinstall the plugin. If you don't sucess at solving the problem you can contact iPazu#3982 at discord");
            ex.printStackTrace();
        }
        rp.setName(player.getName());
    }

    private void loadNewRealmPlayer() {
        for (String s : config.getConfigurationSection("realmplayers").getKeys(false)) {
            if (RealmPlayer.getPlayer(s) == null)
                new RealmPlayer(config.getString("realmplayers." + s + ".uuid"), config.getString("realmplayers." + s + ".name"));
        }
    }

    public void updateLastVote(RealmPlayer rp) {
        config.set("realmplayers." + rp.getUniqueId() + ".lastvote", System.currentTimeMillis());
        rp.setLastvote(System.currentTimeMillis());
        try {
            config.save(file);
        } catch (Exception ex) {
            System.out.println("[AdvancedRealm] Error while loading the realm.yml file, check if it is deleted or try to reinstall the plugin. If you don't sucess at solving the problem you can contact iPazu#3982 at discord");
            ex.printStackTrace();
        }
    }

    public void setLevel(Realm realm) {
        config.set("realms." + realm.getOwner().getUniqueId() + ".level", realm.getLevel().getNumber());
        try {
            config.save(file);
        } catch (Exception ex) {
            System.out.println("[AdvancedRealm] Error while loading the realm.yml file, check if it is deleted or try to reinstall the plugin. If you don't sucess at solving the problem you can contact iPazu#3982 at discord");
            ex.printStackTrace();
        }
    }

    public void loadRealm(String name) {

        for (String s : config.getConfigurationSection("realms." + name + ".players").getKeys(false)) {
            if (RealmPlayer.getPlayer(s) == null)
                new RealmPlayer(s, config.getString("realms." + name + ".players." + s + ".name"));
        }
        Location spawn = new Location(ConfigFiles.getWorld(), config.getInt("realms." + name + ".theme.spawn.x"), config.getInt("realms." + name + ".theme.spawn.y"), config.getInt("realms." + name + ".theme.spawn.z"), (float) config.getInt("realms." + name + ".theme.spawn.yaw"), (float) config.getInt("realms." + name + ".theme.spawn.pitch"));
        Realm realm = new Realm(RealmPlayer.getPlayer(config.getString("realms." + name + ".owner.uuid")), ThemeType.themeTypes.get(config.getString("realms." + name + ".theme.id")), spawn, config.getInt("realms." + name + ".level"), config.getInt("realms." + name + ".vote"));
        for (String s : config.getConfigurationSection("realms." + name + ".players").getKeys(false)) {
            RealmPlayer rp = RealmPlayer.getPlayer(s);
            if (rp.getOwned() != realm) {
                realm.addPlayer(rp);
                realm.promote(rp, RealmRank.getRankByString(config.getString("realms." + name + ".players." + s + ".rank")));
            }
        }
        realm.setOwner(RealmPlayer.getPlayer(config.getString("realms." + name + ".owner.uuid")));
        realm.setPrivacy(config.getBoolean("realms." + name + ".privacy"));
        if (config.getConfigurationSection("realms." + name + ".banned") != null) {
            for (String s : config.getConfigurationSection("realms." + name + ".banned").getKeys(false)) {
                if (RealmPlayer.getPlayer(s) == null)
                    new RealmPlayer(s, config.getString("realms." + name + ".banned." + s + ".name"));
                realm.banPlayer(RealmPlayer.getPlayer(s));

            }
        }
    }

    public void loadAllRealm() {

        if (config.getConfigurationSection("realms") != null) {
            try {
                for (String s : config.getConfigurationSection("realms").getKeys(false)) {
                    loadRealm(s);
                }
                loadNewRealmPlayer();
                for (String s : config.getConfigurationSection("realmplayers").getKeys(false)) {
                    RealmPlayer.getPlayer(s).setLastvote(config.getLong("realmplayers." + s + ".lastvote"));
                    for (String sr : config.getStringList("realmplayers." + s + ".voted")) {
                        if (sr != null) {
                            RealmPlayer.getPlayer(s).addRealmVoted(RealmPlayer.getPlayer(sr).getOwned());
                        }
                    }

                }
                System.out.println("[AdvancedRealm] Succefully loaded all realms !");
            } catch (Exception ex) {
                System.out.println("[AdvancedRealm] failed to load all realms, an error occured , please try to reinstall the plugin or call @iPazu#9120 on discord \n cause: " + ex.getCause() + "\n trace:");
                ex.printStackTrace();
            }
        }
    }

    public void removeVotes(Realm deletedrealm) {

        String deletedName = deletedrealm.getOwner().getUniqueId();
        for (String s : config.getConfigurationSection("realmplayers").getKeys(false)) {
            ArrayList<String> newvotelist = new ArrayList<>();
            for (String sr : config.getStringList("realmplayers." + s + ".voted")) {
                if (sr != null && !sr.equalsIgnoreCase(deletedName)) {
                    newvotelist.add(sr);
                }
            }
            config.set("realmplayers." + s + ".voted",newvotelist);
        }
        try {
            config.save(file);
        } catch (Exception ex) {
            System.out.println("[AdvancedRealm] Error while loading the realm.yml file, check if it is deleted or try to reinstall the plugin. If you don't sucess at solving the problem you can contact iPazu#3982 at discord");
            ex.printStackTrace();
        }
    }

    private Location getLastLocation() {
        if (config.getConfigurationSection("realms") == null) {
            return new Location(ConfigFiles.getWorld(), ConfigFiles.getRealmspacing(), 66, 0);
        }
        String laststring = Iterables.getLast(config.getConfigurationSection("realms").getKeys(false));
        ConfigurationSection section = config.getConfigurationSection("realms." + laststring + ".theme.spawn");
        return new Location(ConfigFiles.getWorld(), section.getDouble("x"), 66, section.getDouble("z"));
    }

    public Location getNewLocation() {
        int i = Realm.allrealm.size() % 20;
        if (i == 0 && Realm.allrealm.size() != 0)
            return getLastLocation().clone().add(-getLastLocation().getX(), 0, ConfigFiles.getRealmspacing());
        else
            return getLastLocation().clone().add(ConfigFiles.getRealmspacing(), 0, 0);
    }

    public void updateVote(Realm realm) {
        config.set("realms." + realm.getOwner().getUniqueId() + ".vote", realm.getVote());
        try {
            config.save(file);
        } catch (Exception e) {
            System.out.println("[AdvancedRealm] Error while loading the realm.yml file, check if it is deleted or try to reinstall the plugin. If you don't sucess at solving the problem you can contact iPazu#3982 at discord");
            e.printStackTrace();
        }
    }

    public void updateMultipleVote(RealmPlayer rp) {
        config.set("realmplayers." + rp.getUniqueId() + ".voted", rp.voteduuid);
        for (String s : rp.voteduuid)
            System.out.println(s);
        try {
            config.save(file);
        } catch (Exception e) {
            System.out.println("[AdvancedRealm] Error while loading the realm.yml file, check if it is deleted or try to reinstall the plugin. If you don't sucess at solving the problem you can contact iPazu#3982 at discord");
            e.printStackTrace();
        }
    }
    private void useless()
    {
        ArrayList<String> strs = new ArrayList<>();
        strs.forEach(System.out::println);
    }
}

