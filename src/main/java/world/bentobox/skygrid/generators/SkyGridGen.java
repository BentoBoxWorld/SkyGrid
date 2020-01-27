package world.bentobox.skygrid.generators;

import java.util.Collections;
import java.util.List;
import java.util.Random;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.generator.ChunkGenerator;

import world.bentobox.skygrid.SkyGrid;

public class SkyGridGen extends ChunkGenerator {

    private final SkyGrid addon;

    private BiomeGenerator biomeGenerator;

    private BlockPopulator populator;

    private SkyGridChunks preMade;

    /**
     * @param addon - addon
     */
    public SkyGridGen(SkyGrid addon) {
        this.addon = addon;
        this.populator = new SkyGridPop(addon);
        preMade = new SkyGridChunks(addon);
    }
    
    @Override
    public ChunkData generateChunkData(World world, Random random, int chunkX, int chunkZ, ChunkGenerator.BiomeGrid biomeGrid) {
        // The chunk we are making
        ChunkData result = createChunkData(world);
        preMade.getSkyGridChunk(world.getEnvironment()).forEach(b -> result.setBlock(b.getX(), b.getY(), b.getZ(), b.getBd()));
        // Set biome
        if (addon.getSettings().isCreateBiomes() && world.getEnvironment().equals(Environment.NORMAL)) {
            if (biomeGenerator == null) {
                biomeGenerator = new BiomeGenerator(world);
            }
            for (int x = 0; x < 16; x ++) {
                for (int z = 0; z < 16; z ++) {
                    int realX = x + chunkX * 16; //used so that the noise function gives us
                    int realZ = z + chunkZ * 16; //different values each chunk
                    biomeGrid.setBiome(x, z, biomeGenerator.getDominantBiome(realX, realZ));
                }
            }
        }
        return result;
    }


    @Override
    public List<BlockPopulator> getDefaultPopulators(World world) {
        //return Collections.emptyList();
        return Collections.singletonList(populator);
    }

    @Override
    public Location getFixedSpawnLocation(World world, Random random) {
        return new Location(world, 0, addon.getSettings().getIslandHeight() + 2D, 0);
    }



}