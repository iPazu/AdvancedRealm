package fr.ipazu.advancedrealm.gui;

import fr.ipazu.advancedrealm.Main;
import fr.ipazu.advancedrealm.realm.Realm;
import fr.ipazu.advancedrealm.realm.RealmLevel;
import fr.ipazu.advancedrealm.realm.RealmPlayer;
import fr.ipazu.advancedrealm.realm.RealmRank;
import fr.ipazu.advancedrealm.utils.Config;
import fr.ipazu.advancedrealm.utils.ItemsUtils;
import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class RealmProvider implements InventoryProvider {
    private Player player;
    private Realm realm;
    private ClickableItem banned, teleport, theme, upgrade, members, privacy, basic, back;
    private boolean from;
    private YamlConfiguration config;

    public RealmProvider(Player player, Realm realms, boolean from) {
        this.player = player;
        this.realm = realms;
        this.from = from;
        this.config = Config.ASPECT.getConfig();
        setUpItems();
    }

    public void setUpItems() {
        teleport = ClickableItem.of(new ItemsUtils(Config.getMaterial(config.getString("gui.realmgui.home.item")), Config.getStringWithReplacementRealm(config.getString("gui.realmgui.home.name"),realm), (byte) config.getInt("gui.realmgui.home.data"),Config.getListWithReplacementRealm(config.getStringList("gui.realmgui.home.lore"),realm)).toItemStack(), e -> {
            e.setCancelled(true);
            realm.teleportToSpawn(player);
            player.sendMessage(Config.getStringWithReplacementRealm(config.getString("gui.realmgui.home.clickmessage"),realm));
            e.getWhoClicked().closeInventory();
        });
        banned = ClickableItem.of(new ItemsUtils(Config.getMaterial(config.getString("gui.realmgui.banned.item")), Config.getStringWithReplacementRealm(config.getString("gui.realmgui.banned.name"),realm), (byte) config.getInt("gui.realmgui.banned.data"), Config.getListWithReplacementRealm(config.getStringList("gui.realmgui.banned.lore"),realm)).toItemStack(), e -> {
            e.setCancelled(true);
            e.getWhoClicked().closeInventory();
            new WholeGUI().openBanned(player, realm);
        });

        privacy = ClickableItem.of(new ItemsUtils(Config.getMaterial(config.getString("gui.realmgui.privacy.item")), Config.getStringWithReplacementRealm(config.getString("gui.realmgui.privacy.name"),realm), (byte) config.getInt("gui.realmgui.privacy.data") ,Config.getListWithReplacementRealm(config.getStringList("gui.realmgui.privacy.lore"),realm)).toItemStack(), e -> {
            e.setCancelled(true);
            if (RealmPlayer.getPlayer(player.getUniqueId().toString()).getRankByRealm(realm) == RealmRank.MANAGER || RealmPlayer.getPlayer(player.getUniqueId().toString()).getRankByRealm(realm) == RealmRank.OWNER) {
                if (realm.getPrivacy())
                    realm.setPrivacy(false);
                else
                realm.setPrivacy(true);
                player.sendMessage(Config.getStringWithReplacementRealm(config.getString("gui.realmgui.privacy.clickmessage"),realm));
                e.getWhoClicked().closeInventory();
            } else
                player.sendMessage("§cOnly the manager and the owner of the Realm can do this !");
        });

        members = ClickableItem.of(new ItemsUtils(Config.getMaterial(config.getString("gui.realmgui.members.item")), Config.getStringWithReplacementRealm(config.getString("gui.realmgui.members.name"),realm), (byte) config.getInt("gui.realmgui.members.data"),Config.getListWithReplacementRealm(config.getStringList("gui.realmgui.members.lore"),realm)).toItemStack(), e -> {
            e.setCancelled(true);
            e.getWhoClicked().closeInventory();
            new WholeGUI().openMembersGui(player, realm);
        });

        theme = ClickableItem.of(new ItemsUtils(Config.getMaterial(config.getString("gui.realmgui.theme.item")), Config.getStringWithReplacementRealm(config.getString("gui.realmgui.theme.name"),realm), (byte) config.getInt("gui.realmgui.theme.data"),Config.getListWithReplacementRealm(config.getStringList("gui.realmgui.theme.lore"),realm)).toItemStack(), e -> {
            e.setCancelled(true);
            e.getWhoClicked().closeInventory();
            new WholeGUI().openThemeGui(player, realm);
        });
        if (realm.getLevel().getNumber() >= 20) {
            upgrade = ClickableItem.of(new ItemsUtils(Config.getMaterial(config.getString("gui.realmgui.upgrade.item")), Config.getStringWithReplacementRealm(config.getString("gui.realmgui.upgrade.name"),realm), (byte) config.getInt("gui.realmgui.upgrade.data"), Config.getListWithReplacementRealm(config.getStringList("gui.realmgui.upgrade.maxlevellore"),realm)).toItemStack(), e -> {
                player.sendMessage("§cThis realm is already at max level.");
                e.setCancelled(true);
                player.closeInventory();
            });

    }
        else {
            RealmLevel nextlevel = RealmLevel.getLevel(realm.getLevel().getNumber() + 1);
            upgrade = ClickableItem.of(new ItemsUtils(Config.getMaterial(config.getString("gui.realmgui.upgrade.item")), Config.getStringWithReplacementRealm(config.getString("gui.realmgui.upgrade.name"),realm), (byte) config.getInt("gui.realmgui.upgrade.data"), Config.getListWithReplacementRealm(config.getStringList("gui.realmgui.upgrade.lore"),realm)).toItemStack(), e -> {
                e.setCancelled(true);
                e.getWhoClicked().closeInventory();
                if (Main.getInstance().setupEconomy()) {
                    if (RealmPlayer.getPlayer(player.getUniqueId().toString()).getRankByRealm(realm) == RealmRank.MEMBER || RealmPlayer.getPlayer(player.getUniqueId().toString()).getRankByRealm(realm) == RealmRank.GUARD) {
                        player.sendMessage("§cOnly the manager and the owner of the Realm can do this !");
                        return;
                    }
                    if (Main.getInstance().economy.getBalance(player) < nextlevel.getPrice()) {
                        player.sendMessage("§cYou don't have enough money to do upgrade your Realm.");
                        player.sendMessage("§cYou have §6" + Main.getInstance().economy.getBalance(player) + " §cand you need §6" + nextlevel.getPrice());
                        return;
                    }

                    Main.getInstance().economy.withdrawPlayer(player, nextlevel.getPrice());
                    realm.upgrade(realm.getLevel().getNumber() + 1);
                    for (RealmPlayer s : realm.getRealmMembers()) {
                        if (Bukkit.getPlayer(s.getName()) != null)
                            Bukkit.getPlayer(s.getName()).sendMessage("§e§l" + player.getName() + " §aupgraded §b§l" + realm.getOwner().getName() + "'s §aRealm to the level §e" + realm.getLevel().getNumber());
                    }
                }
            });
            back = ClickableItem.of(new ItemsUtils(Config.getMaterial(config.getString("gui.back.item")), Config.getStringWithReplacementRealm(config.getString("gui.back.name"),realm), (byte) config.getInt("gui.back.data"), Config.getListWithReplacementRealm(config.getStringList("gui.back.lore"),realm)).toItemStack(), e -> {
                e.setCancelled(true);
                player.closeInventory();
                new WholeGUI().openAllRealmGUI(player);
            });
            basic = ClickableItem.of(new ItemStack(Config.getMaterial(config.getString("gui.default.item")), 1, (byte) config.getInt("gui.default.data")), e -> e.setCancelled(true));
        }
    }

    @Override
    public void init(Player player, InventoryContents inventoryContents) {
        inventoryContents.fill(basic);
        inventoryContents.set(Config.getRowFromInt(config.getInt("gui.realmgui.home.slot")), Config.getCollumFromInt(config.getInt("gui.realmgui.home.slot")), teleport);
        inventoryContents.set(Config.getRowFromInt(config.getInt("gui.realmgui.privacy.slot")), Config.getCollumFromInt(config.getInt("gui.realmgui.privacy.slot")), privacy);
        inventoryContents.set(Config.getRowFromInt(config.getInt("gui.realmgui.upgrade.slot")), Config.getCollumFromInt(config.getInt("gui.realmgui.upgrade.slot")), upgrade);
        inventoryContents.set(Config.getRowFromInt(config.getInt("gui.realmgui.theme.slot")), Config.getCollumFromInt(config.getInt("gui.realmgui.theme.slot")), theme);
        inventoryContents.set(Config.getRowFromInt(config.getInt("gui.realmgui.members.slot")), Config.getCollumFromInt(config.getInt("gui.realmgui.members.slot")), members);
        inventoryContents.set(Config.getRowFromInt(config.getInt("gui.realmgui.banned.slot")), Config.getCollumFromInt(config.getInt("gui.realmgui.banned.slot")), banned);
        if (from) {
            inventoryContents.set(Config.getRowFromInt(config.getInt("gui.back.slot")), Config.getCollumFromInt(config.getInt("gui.back.slot")), back);
        }
    }

    @Override
    public void update(Player player, InventoryContents inventoryContents) {

    }

}
