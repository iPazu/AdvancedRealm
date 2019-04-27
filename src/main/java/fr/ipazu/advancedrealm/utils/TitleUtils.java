package fr.ipazu.advancedrealm.utils;


import org.bukkit.entity.Player;

import java.lang.reflect.Constructor;


public class TitleUtils {

    public static void titlePacket(Player player, Integer fadeIn, Integer stay, Integer fadeOut, String title, String subtitle) {
        try {


            Object enumTitle = Reflector.getNmsClass("PacketPlayOutTitle").getDeclaredClasses()[0].getField("TITLE").get(null);
            Object titleChat = Reflector.getNmsClass("IChatBaseComponent").getDeclaredClasses()[0].getMethod("a", String.class).invoke(null, "{\"text\":\"" + title + "\"}");

            Object enumSubtitle = Reflector.getNmsClass("PacketPlayOutTitle").getDeclaredClasses()[0].getField("SUBTITLE").get(null);
            Object subtitleChat = Reflector.getNmsClass("IChatBaseComponent").getDeclaredClasses()[0].getMethod("a", String.class).invoke(null, "{\"text\":\"" + subtitle + "\"}");
            
            Constructor<?> titleConstructor = Reflector.getNmsClass("PacketPlayOutTitle").getConstructor(Reflector.getNmsClass("PacketPlayOutTitle").getDeclaredClasses()[0], Reflector.getNmsClass("IChatBaseComponent"), int.class, int.class, int.class);
            Object titlePacket = titleConstructor.newInstance(enumTitle, titleChat, 30, 50, 30);
            Object subtitlePacket = titleConstructor.newInstance(enumSubtitle, subtitleChat, 30, 40, 30);

            Reflector.sendPacket(player, titlePacket);
            Reflector.sendPacket(player, subtitlePacket);

        } catch (Exception e1) {
            e1.printStackTrace();
        }

    }


}
