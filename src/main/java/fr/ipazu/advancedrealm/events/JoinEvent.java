package fr.ipazu.advancedrealm.events;


import fr.ipazu.advancedrealm.realm.RealmConfig;
import fr.ipazu.advancedrealm.realm.RealmPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class JoinEvent implements Listener{
    @EventHandler
    public void onJoin(PlayerJoinEvent e){
        Player player = e.getPlayer();
        if(RealmPlayer.getPlayer(player.getUniqueId().toString()) == null){
            new RealmConfig().addNewPlayer(player);
        }
        RealmPlayer rp = RealmPlayer.getPlayer(player.getUniqueId().toString());
        if(!rp.getName().toLowerCase().equals(player.getName().toLowerCase())){
            new RealmConfig().updatePlayerName(player);
        }

    }
}
