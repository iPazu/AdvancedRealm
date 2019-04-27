package fr.ipazu.advancedrealm.events;


import fr.ipazu.advancedrealm.Main;
import fr.ipazu.advancedrealm.realm.Realm;
import fr.ipazu.advancedrealm.utils.Config;
import fr.ipazu.advancedrealm.utils.ConfigFiles;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockFromToEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.Random;

public class CobbleGenerating implements Listener {
    private final BlockFace[] faces = {BlockFace.SELF, BlockFace.DOWN, BlockFace.UP, BlockFace.NORTH, BlockFace.EAST, BlockFace.SOUTH, BlockFace.WEST};


    @EventHandler
    public void onGenerate(BlockFromToEvent event) {
        if (!ConfigFiles.getOregenActivated()) {
            return;
        }
        int id = this.getID(event.getBlock());
        if (Realm.getRealmFromLocation(event.getToBlock().getLocation()) != null) {
            if ((id >= 8) && (id <= 11) && event.getFace() != BlockFace.DOWN) {
                Block b = event.getToBlock();
                int toid = this.getID(b);
                Location fromLoc = b.getLocation();
                // fix for (lava -> water)
                if (id == 10 || id == 11) {
                    if (!isSurroundedByWater(fromLoc)) {
                        return;
                    }
                }
                if ((toid == 0 || toid == 9 || toid == 8 || toid == 10 || toid == 11) && (generatesCobble(id, b))) {
                    Random r = new Random();
                    double randomnumber = 101 * r.nextDouble();
                    double currentpercentage = 0;
                    for (Map.Entry<Material, Double> entry : ConfigFiles.getOregenvalues().entrySet()) {
                        double previouspercentage = currentpercentage;
                        Material material = entry.getKey();
                        Double percentage = entry.getValue();
                        currentpercentage += percentage;

                        if (randomnumber > previouspercentage && randomnumber <= currentpercentage) {
                            if (Arrays.stream(event.getBlock().getClass().getMethods()).anyMatch(method -> method.getName() == "getTypeId")) {
                                b.setType(material);
                                b.getWorld().playSound(b.getLocation(),Sound.BLOCK_FIRE_EXTINGUISH,5,5);
                            } else {
                                Bukkit.getScheduler().runTask(Main.getInstance(), () -> {
                                    b.setType(material);
                                    b.getState().update(true);
                                    b.getWorld().playSound(b.getLocation(),Sound.BLOCK_FIRE_EXTINGUISH,5,5);
                                });
                            }
                            return;
                        }
                    }
                }

            }
        }
    }


    public boolean isSurroundedByWater(Location fromLoc) {
        Block[] blocks = {
                fromLoc.getWorld().getBlockAt(fromLoc.getBlockX() + 1, fromLoc.getBlockY(), fromLoc.getBlockZ()),
                fromLoc.getWorld().getBlockAt(fromLoc.getBlockX() - 1, fromLoc.getBlockY(), fromLoc.getBlockZ()),
                fromLoc.getWorld().getBlockAt(fromLoc.getBlockX(), fromLoc.getBlockY(), fromLoc.getBlockZ() + 1),
                fromLoc.getWorld().getBlockAt(fromLoc.getBlockX(), fromLoc.getBlockY(), fromLoc.getBlockZ() - 1)};

        for (Block b : blocks) {
            if (b.getType().toString().contains("WATER")) {
                return true;
            }
        }
        return false;

    }


    public boolean generatesCobble(int id, Block b) {
        int mirrorID1 = (id == 8) || (id == 9) ? 10 : 8;
        int mirrorID2 = (id == 8) || (id == 9) ? 11 : 9;
        for (BlockFace face : this.faces) {
            Block r = b.getRelative(face, 1);
            if ((this.getID(r) == mirrorID1) || (this.getID(r) == mirrorID2)) {
                return true;
            }
        }
        return false;
    }


    public int getID(Block b) {
        if (Arrays.stream(b.getClass().getMethods()).anyMatch(method -> method.getName() == "getTypeId")) {
            return b.getTypeId();
        } else {
            try {
                return Material113.valueOf(Material113.class, b.getType().name()).getID();
            } catch (IllegalArgumentException e) {
                return 2;
            }
        }
    }

    public enum Material113 {
        STATIONARY_LAVA(10),
        STATIONARY_WATER(9),
        WATER(8),
        LAVA(11),
        AIR(0);

        int id;

        Material113(int id) {
            this.id = id;
        }

        int getID() {
            return this.id;
        }
    }
    private void useless()
    {
        ArrayList<String> strs = new ArrayList<>();
        strs.forEach(System.out::println);
    }
}
