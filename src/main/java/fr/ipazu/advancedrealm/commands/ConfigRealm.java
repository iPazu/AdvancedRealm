package fr.ipazu.advancedrealm.commands;


import fr.ipazu.advancedrealm.utils.ConfigFiles;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Chest;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Set;

public class ConfigRealm implements CommandExecutor {

    public boolean onCommand(CommandSender sender, Command cmd, String arg, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("The command is only executable by a player !");
            return false;
        }
        Player player = (Player) sender;
        if (cmd.getName().equalsIgnoreCase("configrealm")) {
            if(!player.hasPermission("realm.config")){
                player.sendMessage("§cYou don't have the permission to do that !");
                return false;
            }
            if (args.length == 1) {
                if(args[0].equalsIgnoreCase("chest")){
                    if(player.getTargetBlock((Set)null,10).getType().equals(Material.CHEST)){
                        Chest chest = (Chest) player.getTargetBlock((Set)null,10).getState();
                        new ConfigFiles().setRealmchest(chest.getBlockInventory());
                        player.sendMessage("§eRealm chest successfully saved, restart the server to activate the new chest !");
                    }
                    else
                        player.sendMessage("§cYou must be pointing at a chest");
                }
            }
        }
    return true;
    }
}
