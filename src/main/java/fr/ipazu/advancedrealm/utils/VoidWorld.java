package fr.ipazu.advancedrealm.utils;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.generator.ChunkGenerator;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class VoidWorld {

    public static ChunkGenerator getDefaultWorldGenerator() {
        return new VoidWorldGenerator();
    }

    public static class VoidWorldGenerator extends ChunkGenerator {

        @Override
        public ChunkData generateChunkData(World world, Random random, int x, int z, BiomeGrid biome) {
            return createChunkData(world);
        }
    }
}



