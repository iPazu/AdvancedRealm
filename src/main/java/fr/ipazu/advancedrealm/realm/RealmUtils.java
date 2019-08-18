package fr.ipazu.advancedrealm.realm;

import org.bukkit.Location;

public class RealmUtils {
    public Realm getRealmFromLocation(Location location){
        return Realm.getRealmFromLocation(location);
    }
    public RealmPlayer getRealmPlayerFromName(String name){
        return RealmPlayer.getPlayerFromName(name);
    }
    public RealmPlayer getRealmPlayerFromUUID(String uuid){
      return RealmPlayer.getPlayer(uuid);
    }
    public Realm getRealmFromName(String name){
        return RealmPlayer.getPlayerFromName(name).getOwned();
    }
    public Realm getRealmFromUUID(String uuid){
        return RealmPlayer.getPlayer(uuid).getOwned();
    }
}

