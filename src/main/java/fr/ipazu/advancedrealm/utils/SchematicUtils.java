package fr.ipazu.advancedrealm.utils;

import com.boydti.fawe.FaweAPI;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.extent.clipboard.ClipboardFormats;
import fr.ipazu.arwrapper.SchematicWrapper;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import java.io.File;
import java.io.IOException;
public class SchematicUtils {
    private Location location;
    private File file;

    public SchematicUtils(Location location, File file) {
        this.location = location;
        this.file = file;
    }

    public void paste() throws Exception {
        if (Bukkit.getVersion().contains("1.14")  ) {
            paste14();
        } else if (Bukkit.getVersion().contains("1.13")) {
            paste13();
        } else
            pasteLegacy();
    }

    private void pasteLegacy() throws IOException {
        Vector vector = new Vector(location.getX(), location.getY(), location.getZ());
       ClipboardFormats.findByFile(file).load(file).paste(FaweAPI.getWorld(location.getWorld().getName()), vector, true, false, null);
    }

    private void paste13() throws IOException {
        System.out.println("Pasting 1.13");
        new SchematicWrapper(file, location).paste();
    }

    private void paste14() throws Exception {
        System.out.println("Pasting 1.14");
        new SchematicWrapper(file,location).paste14();
    }
}

