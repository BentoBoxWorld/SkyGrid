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
     * Location of a sugar cane's sandstone block, which needs a water basin next to it
     */
    private record CanePlot(int x, int y, int z) {}

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
        // Cut off anything higher than island height
        result.setRegion(0, Math.min(addon.getSettings().getIslandHeight() + 1, worldInfo.getMaxHeight() - 1), 0, 16,
                worldInfo.getMaxHeight(), 16,
                Material.AIR);
        List<CanePlot> canePlots = new ArrayList<>();
        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                for (int y = addon.getSettings().getIslandHeight(); y >= worldInfo.getMinHeight(); y--) {
                    // Check if the block is not the 4th block
                    if ((Math.floorMod(x, 4) != 0) || (Math.floorMod(z, 4) != 0) || (Math.floorMod(y, 4) != 0)) {
                        result.setBlock(x, y, z, Material.AIR);
                    } else {
                        // Use the per-chunk random from the server so that each chunk is different
                        Material nextBlock = addon.getWorldStyles().get(worldInfo.getEnvironment()).getProb()
                                .getBlock(r, y == worldInfo.getMinHeight(), true);
                        // Check for plants, etc. below this y
                        // There is no room in this chunk for a water basin next to the corner column
                        if (nextBlock == Material.SUGAR_CANE && x == 0 && z == 0) {
                            nextBlock = Material.SANDSTONE;
                        }
                        checkPlants(nextBlock, x, y, z, result, canePlots);
                    }
                }
            }
        }
        // These must go in after the loop above, which clears everything that is not in a grid column
        canePlots.forEach(plot -> placeWaterBasin(plot, result));
    }

    /**
     * Places water next to the sand under a sugar cane and walls it in so that it cannot flow.
     * The basin extends along x, or along z in the first row of a chunk because a wall
     * would otherwise be outside the chunk.
     * @param plot location of the sugar cane's sandstone block
     * @param result chunk data
     */
    private void placeWaterBasin(CanePlot plot, ChunkData result) {
        int dx = plot.z() == 0 ? 0 : 1;
        int dz = 1 - dx;
        int waterX = plot.x() + dx;
        int waterY = plot.y() + 1;
        int waterZ = plot.z() + dz;
        result.setBlock(waterX, waterY, waterZ, Material.WATER);
        // Floor
        result.setBlock(waterX, plot.y(), waterZ, Material.SANDSTONE);
        // Far wall, opposite the sand
        result.setBlock(waterX + dx, waterY, waterZ + dz, Material.SANDSTONE);
        // Side walls
        result.setBlock(waterX + dz, waterY, waterZ + dx, Material.SANDSTONE);
        result.setBlock(waterX - dz, waterY, waterZ - dx, Material.SANDSTONE);
    }


    private boolean checkPlants(Material nextBlock, int x, int y, int z, ChunkData result, List<CanePlot> canePlots) {
        if (Tag.SAPLINGS.isTagged(nextBlock) || Tag.FLOWERS.isTagged(nextBlock)
                || Tag.ITEMS_VILLAGER_PLANTABLE_SEEDS.isTagged(nextBlock)) {
            result.setBlock(x, y, z, Material.DIRT);
            result.setBlock(x, y + 1, z, nextBlock);
        } else {
            switch (nextBlock) {
            case CACTUS, DEAD_BUSH -> {
                result.setBlock(x, y, z, Material.SANDSTONE);
                result.setBlock(x, y + 1, z, Material.SAND);
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
            case SUGAR_CANE -> {
                result.setBlock(x, y, z, Material.SANDSTONE);
                result.setBlock(x, y + 1, z, Material.SAND);
                result.setBlock(x, y + 2, z, nextBlock);
                canePlots.add(new CanePlot(x, y, z));
            }
            case SHORT_GRASS, TALL_GRASS -> {
                result.setBlock(x, y, z, Material.DIRT);
                result.setBlock(x, y + 1, z, nextBlock);
            }
            case RED_MUSHROOM, BROWN_MUSHROOM -> {
                result.setBlock(x, y, z, Material.PODZOL);
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

    /**
     * Vanilla noise must be off. It runs before {@link #generateNoise} and would fill the grid with terrain.
     * @return false
     */
    @Override
    public boolean shouldGenerateNoise() {
        return false;
    }

    /**
     * Vanilla surface must be off. It would turn grid blocks into grass and dirt.
     * @return false
     */
    @Override
    public boolean shouldGenerateSurface() {
        return false;
    }

    /**
     * Vanilla caves must be off. There is no terrain to carve and they would remove grid blocks.
     * @return false
     */
    @Override
    public boolean shouldGenerateCaves() {
        return false;
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