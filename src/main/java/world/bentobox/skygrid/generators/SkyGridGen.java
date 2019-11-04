package world.bentobox.skygrid.generators;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.block.Biome;
import org.bukkit.block.data.Bisected;
import org.bukkit.block.data.BlockData;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.generator.ChunkGenerator;

import world.bentobox.skygrid.SkyGrid;

public class SkyGridGen extends ChunkGenerator {

    private final SkyGrid addon;

    // Blocks that need to be placed on dirt
    private final static List<Material> NEEDS_DIRT = Arrays.asList(
            Material.ACACIA_SAPLING,
            Material.ALLIUM,
            Material.AZURE_BLUET,
            Material.BEETROOTS,
            Material.BEETROOTS,
            Material.BIRCH_SAPLING,
            Material.BLUE_ORCHID,
            Material.BROWN_MUSHROOM,
            Material.DANDELION,
            Material.DARK_OAK_SAPLING,
            Material.DEAD_BUSH,
            Material.FERN,
            Material.GRASS,
            Material.JUNGLE_SAPLING,
            Material.LARGE_FERN,
            Material.LILAC,
            Material.OAK_SAPLING,
            Material.ORANGE_TULIP,
            Material.OXEYE_DAISY,
            Material.PEONY,
            Material.PINK_TULIP,
            Material.POPPY,
            Material.RED_MUSHROOM,
            Material.RED_TULIP,
            Material.ROSE_BUSH,
            Material.SPRUCE_SAPLING,
            Material.SUGAR_CANE,
            Material.SUNFLOWER,
            Material.SUNFLOWER,
            Material.TALL_GRASS,
            Material.WHEAT,
            Material.WHITE_TULIP
            // TODO add all the other plants that need to go on dirt
            );

    private BiomeGenerator biomeGenerator;

    private BlockPopulator populator;

    /**
     * @param addon - addon
     */
    public SkyGridGen(SkyGrid addon) {
        this.addon = addon;
        this.populator = new SkyGridPop(addon);
    }

    @Override
    public ChunkData generateChunkData(World world, Random random, int chunkX, int chunkZ, ChunkGenerator.BiomeGrid biomeGrid) {
        // This gets all the blocks that can be picked and their probabilities
        BlockProbability prob = addon.getWorldStyles().get(world.getEnvironment()).getProb();

        // The chunk we are making
        ChunkData result = createChunkData(world);
        for (int x = 1; x < 16; x += 4) {
            for (int z = 1; z < 16; z += 4) {
                for (int y = 0; y <= addon.getSettings().getIslandHeight(); y += 4) {
                    setBlock(prob, x, y, z, biomeGrid, random, result);
                }
            }
        }
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

    private void setBlock(BlockProbability prob, int x, int y, int z, BiomeGrid biomeGrid, Random random, ChunkData result) {
        // Get a random block and feed in the last block (true if cactus or cane)
        Material blockMat = prob.getBlock(random, y == 0, false);
        // If blockMat is not "a block" then cannot be generated
        if (!blockMat.isBlock()) {
            return;
        }
        // Check if the block needs dirt
        if (NEEDS_DIRT.contains(blockMat)) {
            // Check biome
            if (biomeGrid.getBiome(x, z).equals(Biome.DESERT)) {
                // No plants in desert except for cactus
                if (y == 0) {
                    blockMat = Material.SAND;
                } else {
                    blockMat = Material.CACTUS;
                }
                result.setBlock( x, y, z, blockMat);
            } else {
                // Add dirt
                result.setBlock( x, y, z, Material.DIRT);
                BlockData dataBottom = blockMat.createBlockData();
                if (dataBottom instanceof Bisected) {
                    ((Bisected) dataBottom).setHalf(Bisected.Half.BOTTOM);
                    BlockData dataTop = blockMat.createBlockData();
                    ((Bisected) dataTop).setHalf(Bisected.Half.TOP);
                    result.setBlock(x, y + 1, z, dataBottom);
                    result.setBlock(x, y + 2, z, dataTop);
                } else {
                    result.setBlock( x, y+1, z, blockMat);
                }
                if (blockMat.equals(Material.SUGAR_CANE)) {
                    // x will never be more than 12
                    result.setBlock(x+1, y, z, Material.WATER);
                }
            }
        } else {
            switch (blockMat) {
            case LAVA:
                // Don't allow lava in this biome because the swamp tree vines can cause it to drip and lag
                if (biomeGrid.getBiome(x, z).name().contains("SWAMP")) {
                    result.setBlock( x, y, z, Material.GRASS);
                } else {
                    result.setBlock( x, y, z, Material.LAVA);
                }
                break;
            case CACTUS:
                if (biomeGrid.getBiome(x, z).equals(Biome.DESERT)) {
                    result.setBlock( x, y, z, Material.SAND);
                    result.setBlock( x, y-1, z, Material.SANDSTONE);
                    result.setBlock( x, y+1, z, blockMat);
                } else {
                    // Cactus only in desert
                    result.setBlock( x, y, z, Material.SAND);
                }
                break;
            case DIRT:
                if (biomeGrid.getBiome(x, z).equals(Biome.DESERT)) {
                    // Desert
                    result.setBlock( x, y, z, Material.SAND);
                } else {
                    result.setBlock( x, y, z, Material.DIRT);
                }
            case WATER:
                if (biomeGrid.getBiome(x, z).name().contains("DESERT")) {
                    // Desert
                    result.setBlock( x, y, z, Material.SAND);
                } else {
                    result.setBlock( x, y, z, Material.WATER);
                }
                break;
            case NETHER_WART:
                result.setBlock( x, y, z, Material.SOUL_SAND);
                result.setBlock( x, y+1, z, blockMat);
                break;
            case END_ROD:
                result.setBlock( x, y, z, Material.END_STONE);
                result.setBlock( x, y+1, z, blockMat);
                break;
            case CHORUS_PLANT:
                result.setBlock( x, y, z, Material.END_STONE);
                result.setBlock( x, y+1, z, blockMat);
                break;
            default:
                result.setBlock( x, y, z, blockMat);
            }
        }

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