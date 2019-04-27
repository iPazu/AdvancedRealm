package fr.ipazu.advancedrealm.gui.allrealms;


import fr.ipazu.advancedrealm.gui.WholeGUI;
import fr.ipazu.advancedrealm.realm.Realm;
import fr.ipazu.advancedrealm.realm.RealmPlayer;
import fr.ipazu.advancedrealm.realm.RealmRank;
import fr.ipazu.advancedrealm.utils.ItemsUtils;
import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import fr.minuskube.inv.content.Pagination;
import fr.minuskube.inv.content.SlotIterator;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class AllKickProvider implements InventoryProvider {
    private ArrayList<Realm> avaiblerealms = new ArrayList<>();
    private RealmPlayer realmPlayer;
    private RealmPlayer vised;
    private ArrayList<ClickableItem> items = new ArrayList<>();
    private ClickableItem up, down;

    public AllKickProvider(Player player, RealmPlayer vised) {
        this.realmPlayer = RealmPlayer.getPlayer(player.getUniqueId().toString());
        this.vised = vised;
        setAvaibleRealms();
    }

    public void setAvaibleRealms() {
        for (Realm r : realmPlayer.getAllRealm()) {
            if (realmPlayer.getRankByRealm(r) == RealmRank.MANAGER || realmPlayer.getRankByRealm(r) == RealmRank.OWNER) {
                if(r.getRealmMembers().contains(vised))
                    if(realmPlayer.getOwned() == r){
                        avaiblerealms.add(0,r);
                    }else
                        avaiblerealms.add(r);

            }
        }
        Collections.reverse(avaiblerealms);
    }

    @Override
    public void init(Player player, InventoryContents inventoryContents) {
        Pagination pagination = inventoryContents.pagination();
        int i = 1;
        ClickableItem basic = ClickableItem.of(new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte) 15), e -> e.setCancelled(true));
        if (realmPlayer.getAllRealm().size() <= 9)
            inventoryContents.fill(basic);
        for (Realm r : avaiblerealms) {
            ClickableItem kick = ClickableItem.of(ItemsUtils.getHead(r.getOwner().getName(),"§bKick from "+r.getOwner().getName()+"'s Realm.",Arrays.asList("§7# of members §6" + r.getRealmMembers().size() + "§7/§6" + r.getLevel().getMaxplayer(), "§7Privacy: §6" + r.getPrivacyString())), e -> {
                e.setCancelled(true);
                player.closeInventory();
                if(realmPlayer.getRankByRealm(r) == RealmRank.MEMBER || realmPlayer.getRankByRealm(r) == RealmRank.GUARD){
                    player.sendMessage("§cYou don't have the required rank to kick this player of the realm.");
                    return;
                }
                if(player.getName().equalsIgnoreCase(vised.getName())){
                    player.sendMessage("§cYou cannot kick yourself.");
                    return;
                }
                if(vised.getRankByRealm(r) == RealmRank.OWNER){
                    player.sendMessage("§cYou cannot kick the owner of the realm.");
                    return;
                }
                r.kickPlayer(vised);
                player.sendMessage("§aYou have successfully kicked that player from the Realm.");
                for(RealmPlayer s : r.getRealmMembers()){
                    if(Bukkit.getPlayer(s.getName()) != null)
                        Bukkit.getPlayer(s.getName()).sendMessage("§e§l"+vised.getName()+"§c has been kicked from §b§l"+r.getOwner().getName()+"'s §cRealm.");
                }
            });
            if (realmPlayer.getOwned() == r) {
                kick.getItem().setType(Material.BED);
            }
            if (realmPlayer.getAllRealm().size() <= 9)
                placeIfVoid(kick, inventoryContents);
            else {
                up = ClickableItem.of(new ItemsUtils(Material.ARROW, "§bNext page", Arrays.asList("§7Click to go to ", "§7the page " + getNextPage(pagination))).toItemStack(), e -> {
                    e.setCancelled(true);
                    new WholeGUI().getAllKickGUI(player,vised).open(player, pagination.next().getPage());
                });
                down = ClickableItem.of(new ItemsUtils(Material.ARROW, "§bPrevious page", Arrays.asList("§7Click to go to ", "§7the page " + getPreviousPage(pagination))).toItemStack(), e -> {
                    e.setCancelled(true);
                    new WholeGUI().getAllKickGUI(player,vised).open(player, pagination.previous().getPage());
                });
                i++;
                int j = i;
                if (j > 9 && (j - 1) % 9 == 0) {
                    items.add(down);
                } else if (j % 9 == 0) {
                    items.add(up);
                } else
                    items.add(kick);
            }
        }
        if (realmPlayer.getAllRealm().size() > 9) {
            while (items.size() % 9 != 0) {
                items.add(basic);
            }
            pagination.setItems(items.toArray(new ClickableItem[items.size()]));
            pagination.setItemsPerPage(9);
            pagination.addToIterator(inventoryContents.newIterator(SlotIterator.Type.HORIZONTAL, 0, 0));
        }

    }

    public void placeIfVoid(ClickableItem clickableItem, InventoryContents inventoryContents) {
        SlotIterator iterator = inventoryContents.newIterator(SlotIterator.Type.HORIZONTAL, 0, 0);
        while (!iterator.ended()) {
            if (!iterator.get().isPresent() || iterator.get().get().getItem().getType() == Material.STAINED_GLASS_PANE && iterator.get().isPresent()) {
                inventoryContents.set(iterator.row(), iterator.column(), clickableItem);
                return;
            }
            iterator.next();
        }
    }
    private int getPreviousPage(Pagination pagination) {
        int i = pagination.getPage();
        return i;
    }

    private int getNextPage(Pagination pagination) {
        int i = pagination.getPage();
        i++;
        return i;
    }
    @Override
    public void update(Player player, InventoryContents inventoryContents) {

    }
}
