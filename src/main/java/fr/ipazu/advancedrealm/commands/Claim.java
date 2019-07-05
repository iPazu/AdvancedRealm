package fr.ipazu.advancedrealm.commands;


import fr.ipazu.advancedrealm.gui.WholeGUI;
import fr.ipazu.advancedrealm.realm.RealmPlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Claim implements CommandExecutor {
    public boolean onCommand(CommandSender sender, Command cmd, String arg, String[] args)
    {
        if (!(sender instanceof Player)) {
            sender.sendMessage("The command is only executable by a player !");

            return false;
        }

        if(cmd.getName().equalsIgnoreCase("claim")){
            Player player = (Player) sender;
            if(!player.hasPermission("realm.claim")){
                player.sendMessage("§cYou don't have the permission to do this.");
                return false;
            }
            RealmPlayer rp = RealmPlayer.getPlayer(player.getUniqueId().toString());
            if(rp.getOwned() == null){
                new WholeGUI().openPerkGUI(player,rp);
            }
            else
                player.sendMessage("§cYou already have claimed a realm");
        }

        return true;
    }
}