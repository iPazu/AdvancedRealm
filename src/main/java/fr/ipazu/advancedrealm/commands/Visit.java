package fr.ipazu.advancedrealm.commands;

import fr.ipazu.advancedrealm.realm.RealmPlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Visit implements CommandExecutor{
    public boolean onCommand(CommandSender sender, Command cmd, String arg, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("The command is only executable by a player !");
            return false;
        }
        if(cmd.getName().equalsIgnoreCase("visit")){
            Player player = (Player) sender;
            if(!player.hasPermission("realm.visit")){
                player.sendMessage("§cYou don't have the permission to do this.");
                return false;
            }
            RealmPlayer rp = RealmPlayer.getPlayer(player.getUniqueId().toString());
            if(args.length == 1){
                if(RealmPlayer.getPlayerFromName(args[0].toLowerCase()) != null){
                    RealmPlayer vised = RealmPlayer.getPlayerFromName(args[0].toLowerCase());
                    if(vised.getOwned() == null){
                        player.sendMessage("§cThis realm doesn't exist");
                        return false;
                    }
                    if(vised.getOwned().getBanned().contains(rp) && !player.isOp()){
                        player.sendMessage("§cYou are banned from this realm");
                        return false;
                    }
                    if(vised.getOwned().getPrivacy() && !vised.getOwned().getRealmMembers().contains(rp) && !player.isOp()){
                        player.sendMessage("§cThis realm is private, just the members of the realm can visit it.");
                        return false;
                    }
                    vised.getOwned().teleportToSpawn(player);
                }
                else
                    player.sendMessage("§cThis realm doesn't exist");
                    return false;
            }
                else
                    player.sendMessage("§c/visit <name>");
            }
        return true;
    }
}
