package fr.ipazu.advancedrealm.utils;


import org.bukkit.*;
import org.bukkit.entity.*;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class Reflector
{
    public static Class<?> getNmsClass(final String ClassName) {
        final String name = Bukkit.getServer().getClass().getPackage().getName();
        final String version = name.substring(name.lastIndexOf(46) + 1) + ".";
        final String className = "net.minecraft.server." + version + ClassName;
        Class<?> c = null;
        try {
            c = Class.forName(className);
        }
        catch (Exception e) {
            Bukkit.getLogger().severe("Reflection failed for getCraftClass > " + className);
            Bukkit.getServer().shutdown();
            e.printStackTrace();
        }
        return c;
    }

    public static Class<?> getCraftClass(final String ClassName) {
        final String name = Bukkit.getServer().getClass().getPackage().getName();
        final String version = name.substring(name.lastIndexOf(46) + 1) + ".";
        final String className = "org.bukkit.craftbukkit." + version + ClassName;
        Class<?> c = null;
        try {
            c = Class.forName(className);
        }
        catch (Exception e) {
            Bukkit.getLogger().severe("Reflection failed for getCraftClass > " + className);
            Bukkit.getServer().shutdown();
            e.printStackTrace();
        }
        return c;
    }
    public static Field getField(final Class<?> cl, final String fieldName) {
        try {
            return cl.getDeclaredField(fieldName);
        }
        catch (Exception e) {
            e.printStackTrace();
            Bukkit.getLogger().severe("Reflection failed for getField classe > " + cl.getClass().getName() + " field > " + fieldName);
            Bukkit.getServer().shutdown();
            return null;
        }
    }
    
    public static void setField(final Object obj, final String field, final Object value) {
        try {
            final Field maxUsesField = obj.getClass().getDeclaredField(field);
            maxUsesField.setAccessible(true);
            maxUsesField.set(obj, value);
            maxUsesField.setAccessible(!maxUsesField.isAccessible());
        }
        catch (Exception e) {
            e.printStackTrace();
            Bukkit.getLogger().severe("Reflection failed for changeField " + obj.getClass().getName() + " field > " + field + " value > " + value);
            Bukkit.getServer().shutdown();
        }
    }
    
    public static Method getMethod(final Class<?> cl, final String method, final Class<?>... args) {
        for (final Method m : cl.getMethods()) {
            if (m.getName().equals(method) && ClassListEqual(args, m.getParameterTypes())) {
                return m;
            }
        }
        return null;
    }
    
    public static boolean ClassListEqual(final Class<?>[] l1, final Class<?>[] l2) {
        boolean equal = true;
        if (l1.length != l2.length) {
            return false;
        }
        for (int i = 0; i < l1.length; ++i) {
            if (l1[i] != l2[i]) {
                equal = false;
                break;
            }
        }
        return equal;
    }
    
    public static Method getMethod(final Class<?> cl, final String method) {
        for (final Method m : cl.getMethods()) {
            if (m.getName().equals(method)) {
                return m;
            }
        }
        return null;
    }
    
    public static Object getHandle(final Entity entity) {
        try {
            return getMethod(entity.getClass(), "getHandle").invoke(entity, new Object[0]);
        }
        catch (Exception e) {
            e.printStackTrace();
            Bukkit.getLogger().severe("Reflection failed for getHandle Entity > " + entity.getType().toString());
            Bukkit.getServer().shutdown();
            return null;
        }
    }
    
    public static Object getHandle(final World world) {
        try {
            return getMethod(world.getClass(), "getHandle").invoke(world, new Object[0]);
        }
        catch (Exception e) {
            e.printStackTrace();
            Bukkit.getLogger().severe("Reflection failed for getHandle World > " + world.getName());
            Bukkit.getServer().shutdown();
            return null;
        }
    }
    public static void sendPacket(Player player, Object packet) {
        try {
            Object handle = player.getClass().getMethod("getHandle").invoke(player);
            Object playerConnection = handle.getClass().getField("playerConnection").get(handle);
            playerConnection.getClass().getMethod("sendPacket", Reflector.getNmsClass("Packet")).invoke(playerConnection, packet);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void setFieldFinalModifiable(final Field nameField) throws Exception {
        nameField.setAccessible(true);
        int modifiers = nameField.getModifiers();
        final Field modifierField = nameField.getClass().getDeclaredField("modifiers");
        modifiers &= 0xFFFFFFEF;
        modifierField.setAccessible(true);
        modifierField.setInt(nameField, modifiers);
    }
}
