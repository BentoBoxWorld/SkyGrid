package world.bentobox.skygrid.generators;

import java.util.Collections;
import java.util.List;
import java.util.Random;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.generator.WorldInfo;

import world.bentobox.skygrid.SkyGrid;

public class SkyGridGen extends ChunkGenerator {

    private final SkyGrid addon;

    private final BlockPopulator populator;
    private final SkyGridChunks preMade;

    /**
     * @param addon - addon
     */
    public SkyGridGen(SkyGrid addon) {
        this.addon = addon;
        this.populator = new SkyGridPop(addon);
        preMade = new SkyGridChunks(addon);
    }

    @Override
    public void generateNoise(WorldInfo worldInfo, Random r, int x, int z, ChunkData result) {
        result.setRegion(0, worldInfo.getMinHeight(), 0, 16, worldInfo.getMaxHeight(), 16, Material.AIR);
        preMade.getSkyGridChunk(worldInfo.getEnvironment()).forEach(b -> result.setBlock(b.getX(), b.getY(), b.getZ(), b.getBd()));
    }

    @Override
    public List<BlockPopulator> getDefaultPopulators(World world) {
        return Collections.singletonList(populator);
    }

    @Override
    public Location getFixedSpawnLocation(World world, Random random) {
        return new Location(world, 0, addon.getSettings().getIslandHeight() + 2D, 0);
    }

    /**
     * Gets if the server should generate Vanilla surface.
     * @return true if the server should generate Vanilla surface
     */
    @Override
    public boolean shouldGenerateSurface() {
        return true;
    }

    /**
     * Gets if the server should generate Vanilla caves.
     * @return true if the server should generate Vanilla caves
     */
    @Override
    public boolean shouldGenerateCaves() {
        return true;
    }

    /**
     * Gets if the server should generate Vanilla decorations after this
     * ChunkGenerator.
     * @return true if the server should generate Vanilla decorations
     */
    @Override
    public boolean shouldGenerateDecorations() {
        return true;
    }

    /**
     * Gets if the server should generate Vanilla mobs after this
     * ChunkGenerator.
     * @return true if the server should generate Vanilla mobs
     */
    @Override
    public boolean shouldGenerateMobs() {
        return true;
    }

    /**
     * Gets if the server should generate Vanilla structures after this
     * ChunkGenerator.
     * @return true if the server should generate Vanilla structures
     */
    @Override
    public boolean shouldGenerateStructures() {
        return true;
    }

}