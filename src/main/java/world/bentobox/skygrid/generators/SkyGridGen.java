package world.bentobox.skygrid.generators;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.World;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.generator.WorldInfo;

import world.bentobox.skygrid.SkyGrid;

public class SkyGridGen extends ChunkGenerator {

    private final SkyGrid addon;

    private final BlockPopulator populator;

    /**
     * @param addon - addon
     */
    public SkyGridGen(SkyGrid addon) {
        super();
        this.addon = addon;
        this.populator = new SkyGridPop(addon);
    }

    @Override
    public void generateNoise(WorldInfo worldInfo, Random r, int chunkX, int chunkZ, ChunkData result) {
        Random rand = new Random(worldInfo.getSeed());
        // Cut off anything higher than island height
        result.setRegion(0, Math.min(addon.getSettings().getIslandHeight() + 1, worldInfo.getMaxHeight() - 1), 0, 16,
                worldInfo.getMaxHeight(), 16,
                Material.AIR);
        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                for (int y = addon.getSettings().getIslandHeight(); y >= worldInfo.getMinHeight(); y--) {
                    // Check if the block is not the 4th block
                    if ((Math.floorMod(x, 4) != 0) || (Math.floorMod(z, 4) != 0) || (Math.floorMod(y, 4) != 0)) {
                        result.setBlock(x, y, z, Material.AIR);
                    } else {
                        Material m = result.getBlockData(x, y, z).getMaterial();
                        if (m == Material.AIR || m == Material.WATER) {
                            Material nextBlock = addon.getWorldStyles().get(worldInfo.getEnvironment()).getProb()
                                    .getBlock(rand, y == worldInfo.getMinHeight(), true);
                            // Check for plants, etc. below this y 
                            checkPlants(nextBlock, x, y, z, result);
                        }

                    }
                }
            }
        }
    }


    private boolean checkPlants(Material nextBlock, int x, int y, int z, ChunkData result) {
        if (Tag.SAPLINGS.isTagged(nextBlock) || Tag.FLOWERS.isTagged(nextBlock)
                || Tag.ITEMS_VILLAGER_PLANTABLE_SEEDS.isTagged(nextBlock)) {
            result.setBlock(x, y, z, Material.DIRT);
            result.setBlock(x, y + 1, z, nextBlock);
        } else {
            switch (nextBlock) {
            case CACTUS, DEAD_BUSH -> {
                result.setBlock(x, y, z, Material.SANDSTONE);
                result.setBlock(x, y, z, Material.SAND);
                result.setBlock(x, y + 2, z, nextBlock);
            }
            case NETHER_WART -> {
                result.setBlock(x, y, z, Material.SOUL_SAND);
                result.setBlock(x, y + 1, z, nextBlock);
            }
            case END_ROD, CHORUS_PLANT -> {
                result.setBlock(x, y, z, Material.END_STONE);
                result.setBlock(x, y + 1, z, nextBlock);
            }

            default -> {
                result.setBlock(x, y, z, nextBlock);
                return false;
            }
            }
        }
        return true;
    }


    @Override
    public List<BlockPopulator> getDefaultPopulators(World world) {
        List<BlockPopulator> list = new ArrayList<>();
        list.addAll(super.getDefaultPopulators(world));
        list.add(populator);
        return list;
    }

    @Override
    public Location getFixedSpawnLocation(World world, Random random) {
        return new Location(world, 0, addon.getSettings().getIslandHeight() + 2D, 0);
    }

    @Override
    public boolean shouldGenerateNoise() {
        return true;
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