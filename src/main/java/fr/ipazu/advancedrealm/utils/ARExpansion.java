package fr.ipazu.advancedrealm.utils;

import fr.ipazu.advancedrealm.Main;
import fr.ipazu.advancedrealm.realm.Realm;
import fr.ipazu.advancedrealm.realm.RealmLevel;
import fr.ipazu.advancedrealm.realm.RealmPlayer;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;

import java.util.logging.Level;

public class ARExpansion extends PlaceholderExpansion {

        private Main plugin;

        /**
         * Since we register the expansion inside our own plugin, we
         * can simply use this method here to get an instance of our
         * plugin.
         *
         * @param plugin
         *        The instance of our plugin.
         */
        public ARExpansion(Main plugin){
            this.plugin = plugin;
        }

        /**
         * Because this is an internal class,
         * you must override this method to let PlaceholderAPI know to not unregister your expansion class when
         * PlaceholderAPI is reloaded
         *
         * @return true to persist through reloads
         */
        @Override
        public boolean persist(){
            return true;
        }

        /**
         * Because this is a internal class, this check is not needed
         * and we can simply return {@code true}
         *
         * @return Always true since it's an internal class.
         */
        @Override
        public boolean canRegister(){
            return true;
        }

        /**
         * The name of the person who created this expansion should go here.
         * <br>For convienience do we return the author from the plugin.yml
         *
         * @return The name of the author as a String.
         */
        @Override
        public String getAuthor(){
            return plugin.getDescription().getAuthors().toString();
        }

        /**
         * The placeholder identifier should go here.
         * <br>This is what tells PlaceholderAPI to call our onRequest
         * method to obtain a value if a placeholder starts with our
         * identifier.
         * <br>This must be unique and can not contain % or _
         *
         * @return The identifier in {@code %<identifier>_<value>%} as String.
         */
        @Override
        public String getIdentifier(){
            return "advancedrealm";
        }

        /**
         * This is the version of the expansion.
         * <br>You don't have to use numbers, since it is set as a String.
         *
         * For convienience do we return the version from the plugin.yml
         *
         * @return The version as a String.
         */
        @Override
        public String getVersion(){
            return plugin.getDescription().getVersion();
        }

        /**
         * This is the method called when a placeholder with our identifier
         * is found and needs a value.
         * <br>We specify the value identifier in this method.
         * <br>Since version 2.9.1 can you use OfflinePlayers in your requests.
         *
         * @param  player
         *         A {@link org.bukkit.Player Player}.
         * @param  identifier
         *         A String containing the identifier/value.
         *
         * @return possibly-null String of the requested identifier.
         */
        @Override
        public String onPlaceholderRequest(Player player, String identifier){

            if(player == null){
                return "";
            }
            RealmPlayer realmPlayer = RealmPlayer.getPlayer(player.getUniqueId().toString());
            Realm realm = getRealm(realmPlayer);
            // %advancedrealm_realm_level%
            if(identifier.equals("realm_level")){
                if(realm == null)
                    return "0";
                return ""+ realm.getLevel().getNumber();
            }
            // %advancedrealm_realm_maxplayer%
            if(identifier.equals("realm_maxplayer")){
                if(realm == null)
                    return "0";
                return ""+ realm.getLevel().getMaxplayer();
            }
            // %advancedrealm_realm_bordersize%
            if(identifier.equals("realm_bordersize")){
                if(realm == null)
                    return "0";
                return ""+ realm.getLevel().getBordersize();
            }
            // %advancedrealm_realm_nextlevelprice%
            if(identifier.equals("realm_nextlevelprice")){
                if(realm == null)
                    return "0";
                else if(realm.getLevel() == RealmLevel.getLevel(RealmLevel.realmlevel.size()) )
                    return "max";

                return ""+ RealmLevel.getLevel(realm.getLevel().getNumber()+1).getPrice();
            }
            // %advancedrealm_realm_playernumber%
            if(identifier.equals("realm_playernumber")){
                if(realm == null)
                    return "0";
                return ""+ realm.getRealmMembers().size();
            }
            // %advancedrealm_realm_owner%
            if(identifier.equals("realm_owner")){
                if(realm == null)
                    return "0";
                return ""+ realm.getOwner().getName();
            }
            // %advancedrealm_realm_perk%
            if(identifier.equals("realm_perk")){
                if(realm == null)
                    return "0";
                return ""+ realm.getPerk();
            }
            // %advancedrealm_realm_privacy%
            if(identifier.equals("realm_privacy")){
                if(realm == null)
                    return "0";
                return ""+ realm.getPrivacyString();
            }
            // %advancedrealm_realm_theme%

            if(identifier.equals("realm_theme")){
                if(realm == null)
                    return "0";
                return ""+ realm.getTheme().getThemeType().getName();
            }
            // %advancedrealm_realm_vote%

            if(identifier.equals("realm_vote")){
                if(realm == null)
                    return "0";
                return ""+ realm.getVote();
            }
            // We return null if an invalid placeholder (f.e. %someplugin_placeholder3%)
            // was provided
            return null;
        }
        private Realm getRealm(RealmPlayer realmPlayer){
            if(realmPlayer.getAllRealm().size() == 0)
                return null;
            if(realmPlayer.getOwned() != null)
                return realmPlayer.getOwned();
            else
                return realmPlayer.getAllRealm().get(0);
        }
    }

