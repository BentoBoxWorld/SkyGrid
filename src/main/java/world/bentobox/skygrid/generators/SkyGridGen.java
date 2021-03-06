package world.bentobox.skygrid.generators;

import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.block.Biome;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.generator.ChunkGenerator;

import world.bentobox.skygrid.SkyGrid;

public class SkyGridGen extends ChunkGenerator {

    private final SkyGrid addon;

    private Map<Environment, BiomeGenerator> biomeGenerator;

    private BlockPopulator populator;

    private SkyGridChunks preMade;

    /**
     * @param addon - addon
     */
    public SkyGridGen(SkyGrid addon) {
        this.addon = addon;
        this.populator = new SkyGridPop(addon);
        preMade = new SkyGridChunks(addon);
        biomeGenerator = new EnumMap<>(Environment.class);
    }

    @Override
    public ChunkData generateChunkData(World world, Random random, int chunkX, int chunkZ, ChunkGenerator.BiomeGrid biomeGrid) {
        // The chunk we are making
        ChunkData result = createChunkData(world);
        preMade.getSkyGridChunk(world.getEnvironment()).forEach(b -> result.setBlock(b.getX(), b.getY(), b.getZ(), b.getBd()));
        // Set biome
        if (addon.getSettings().isCreateBiomes()) {
            for (int x = 0; x < 16; x +=4) {
                for (int z = 0; z < 16; z +=4) {
                    int realX = x + chunkX * 16; //used so that the noise function gives us
                    int realZ = z + chunkZ * 16; //different values each chunk
                    Biome b = biomeGenerator.computeIfAbsent(world.getEnvironment(), k -> new BiomeGenerator(world)).getDominantBiome(realX, realZ);
                    for (int y = 0; y < world.getMaxHeight(); y+=4) {
                        biomeGrid.setBiome(x, y, z, b);
                    }
                }
            }
        }
        return result;
    }


    @Override
    public List<BlockPopulator> getDefaultPopulators(World world) {
        return Collections.singletonList(populator);
    }

    @Override
    public Location getFixedSpawnLocation(World world, Random random) {
        return new Location(world, 0, addon.getSettings().getIslandHeight() + 2D, 0);
    }



}