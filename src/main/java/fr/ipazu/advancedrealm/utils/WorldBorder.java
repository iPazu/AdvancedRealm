package fr.ipazu.advancedrealm.utils;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.lang.reflect.Constructor;
import java.util.ArrayList;

public class WorldBorder {
    public static void sendBorder(Location center, int size, Player player) {
        try {
            Object world = Reflector.getCraftClass("CraftWorld").getMethod("getHandle").invoke(center.getWorld());
            Class<?> worldborder = Reflector.getNmsClass("WorldBorder");
            Object worldborderinstance = Reflector.getNmsClass("WorldBorder").newInstance();

            worldborder.getMethod("setCenter", double.class, double.class).invoke(worldborderinstance, (double) center.getBlockX(), (double) center.getBlockZ());
            worldborder.getMethod("setSize", double.class).invoke(worldborderinstance, (double) size);
            worldborder.getField("world").set(worldborderinstance, world);
            Object enumBorder = Reflector.getNmsClass("PacketPlayOutWorldBorder$EnumWorldBorderAction").getField("INITIALIZE").get(null);
            Constructor<?> worldborderconstructor = Reflector.getNmsClass("PacketPlayOutWorldBorder").getConstructor(Reflector.getNmsClass("WorldBorder"), Reflector.getNmsClass("PacketPlayOutWorldBorder$EnumWorldBorderAction"));
            Object worldborderPacket = worldborderconstructor.newInstance(worldborderinstance, enumBorder);
            Reflector.sendPacket(player, worldborderPacket);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void useless()
    {
        ArrayList<String> strs = new ArrayList<>();
        strs.forEach(System.out::println);
    }
}