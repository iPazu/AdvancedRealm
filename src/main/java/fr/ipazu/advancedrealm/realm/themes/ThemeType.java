package fr.ipazu.advancedrealm.realm.themes;


import com.boydti.fawe.FaweAPI;
import com.boydti.fawe.util.EditSessionBuilder;
import com.sk89q.worldedit.CuboidClipboard;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.extent.clipboard.ClipboardFormats;
import fr.ipazu.advancedrealm.Main;
import fr.ipazu.advancedrealm.utils.ItemsUtils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import javax.xml.crypto.dsig.Transform;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ThemeType {
        private String schematic;
        private String name;
        List<String> lore;
        private String id;
        private byte durability;
        private String itemname;
        private int nblock;
        private String permission;
        public static HashMap<String,ThemeType> themeTypes = new HashMap<>();
        public static ArrayList<ThemeType> allthemeTypes = new ArrayList<>();

        public ThemeType(String name, String path, String permission, int nblock, String itemname, String id, byte durability, List<String> lore) {
            this.schematic = "theme/"+path;
            this.nblock = nblock;
            this.name = name;
            this.itemname = itemname.replace("&","§");
            ArrayList<String > newlorelist = new ArrayList<>();
            for(String newlore : lore){
                newlorelist.add(newlore.replace("&","§"));
            }
            this.lore = newlorelist;
            this.durability = durability;
            this.permission = permission;
            this.id = id;
            themeTypes.put(name,this);
            allthemeTypes.add(this);
        }

        public void pasteTheme(Location spawn) {
            try {
                File file = new File(Main.getInstance().getDataFolder(), schematic);
                Vector v = new Vector(spawn.getBlockX(), spawn.getBlockY(), spawn.getBlockZ());
                EditSession es = new EditSessionBuilder(FaweAPI.getWorld(spawn.getWorld().getName())).fastmode(true).build();
                CuboidClipboard cc = CuboidClipboard.loadSchematic(file);
                cc.paste(es, v, true);

            } catch (Exception e) {
                System.out.println("§c[AdvancedRealm] failed to load schematic, an error occured , please try to reinstall the plugin or call @iPazu#3982 on discord \n cause: " + e.getCause() + "\n trace:");
                e.printStackTrace();
            }

        }

    public int getNblock() {
        return nblock;
    }
    public ItemStack getItem(){
        return new ItemsUtils(Material.getMaterial(id),itemname,durability,lore).toItemStack();
    }

    public String getName() {
        return name;
    }

    public String getPermission() {
        return permission;
    }
    private void useless()
    {
        ArrayList<String> strs = new ArrayList<>();
        strs.forEach(System.out::println);
    }
}
