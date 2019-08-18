package fr.ipazu.advancedrealm.utils;

import com.boydti.fawe.FaweAPI;
import com.boydti.fawe.util.TaskManager;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.extent.clipboard.ClipboardFormats;
import fr.ipazu.advancedrealm.Main;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

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

    private void pasteLegacy()  {
        TaskManager.IMP.async(() -> {
            Vector vector = new Vector(location.getX(), location.getY(), location.getZ());
            try {
                ClipboardFormats.findByFile(file).load(file).paste(FaweAPI.getWorld(location.getWorld().getName()), vector, true, false, null);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

    }

    private void paste13() throws NoSuchMethodException, IllegalAccessException, InstantiationException, InvocationTargetException {
        Class<?> wrapperclass = Main.wrapperclass;
        wrapperclass.getMethod("paste",File.class,Location.class).invoke(wrapperclass.newInstance(),file,location);
        System.out.println("Pasting 1.13");
    }

    private void paste14() throws Exception {
        Class<?> wrapperclass = Main.wrapperclass;
        wrapperclass.getMethod("paste14",File.class,Location.class).invoke(wrapperclass.newInstance(),file,location);
        System.out.println("Pasting 1.14");
    }
}

