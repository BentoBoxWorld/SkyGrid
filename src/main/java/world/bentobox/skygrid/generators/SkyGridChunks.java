package world.bentobox.skygrid.generators;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.bukkit.Material;
import org.bukkit.World.Environment;
import org.bukkit.block.data.Bisected;
import org.bukkit.block.data.BlockData;

import world.bentobox.skygrid.SkyGrid;

/**
 * A premade randomized chunk of grid
 * @author tastybento
 *
 */
public class SkyGridChunks {

    // Blocks that need to be placed on dirt
    private static final List<Material> NEEDS_DIRT = Arrays.asList(
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

    private static final int PRE_MADE_CHUNKS_NUMBER = 30;

    private final SkyGrid addon;

    private final Random random = new Random(System.currentTimeMillis());
    private final List<List<SkyGridBlock>> skyGridChunks = new ArrayList<>();
    private final List<List<SkyGridBlock>> skyGridChunksEnd = new ArrayList<>();

    private final List<List<SkyGridBlock>> skyGridChunksNether = new ArrayList<>();

    public SkyGridChunks(SkyGrid addon) {
        super();
        this.addon = addon;
        // Make chunks for world, nether and end
        BlockProbability prob = addon.getWorldStyles().get(Environment.NORMAL).getProb();
        BlockProbability probNether = addon.getWorldStyles().get(Environment.NETHER).getProb();
        BlockProbability probEnd = addon.getWorldStyles().get(Environment.THE_END).getProb();
        // Generate a number of chunks in advance
        addon.log("Making chunks for SkyGrid");
        for (int i = 0; i < PRE_MADE_CHUNKS_NUMBER; i++) {
            skyGridChunks.add(getChunk(prob));
            skyGridChunksNether.add(getChunk(probNether));
            skyGridChunksEnd.add(getChunk(probEnd));
        }
        addon.log("Done making chunks");
    }


    private List<SkyGridBlock> getChunk(BlockProbability prob) {
        List<SkyGridBlock> result = new ArrayList<>();
        for (int x = 1; x < 16; x += 4) {
            for (int z = 1; z < 16; z += 4) {
                for (int y = 0; y <= addon.getSettings().getIslandHeight(); y += 4) {
                    setBlock(prob, x, y, z, result);
                }
            }
        }
        return result;
    }

    /**
     * Get a random skygrid chunk
     * @param env - environment
     * @return list of sky grid blocks
     */
    public List<SkyGridBlock> getSkyGridChunk(Environment env) {
        switch (env) {
        case NETHER:
            return skyGridChunksNether.get(random.nextInt(skyGridChunksNether.size()));
        case THE_END:
            return skyGridChunksEnd.get(random.nextInt(skyGridChunksEnd.size()));
        default:
            return skyGridChunks.get(random.nextInt(skyGridChunks.size()));
        }

    }

    private void setBlock(BlockProbability prob, int x, int y, int z, List<SkyGridBlock> result) {
        // Get a random block and feed in the last block (true if cactus or cane)
        Material blockMat = prob.getBlock(random, y == 0, false);
        // If blockMat is not "a block" then cannot be generated
        if (!blockMat.isBlock()) {
            blockMat = Material.STONE;
        }
        // Check if the block needs dirt
        if (NEEDS_DIRT.contains(blockMat)) {
            // Add dirt
            result.add(new SkyGridBlock(x, y, z, Material.DIRT.createBlockData()));
            BlockData dataBottom = blockMat.createBlockData();
            if (dataBottom instanceof Bisected) {
                ((Bisected) dataBottom).setHalf(Bisected.Half.BOTTOM);
                BlockData dataTop = blockMat.createBlockData();
                ((Bisected) dataTop).setHalf(Bisected.Half.TOP);
                result.add(new SkyGridBlock(x, y + 1, z, dataBottom));
                result.add(new SkyGridBlock(x, y + 2, z, dataTop));
            } else {
                result.add(new SkyGridBlock(x, y+1, z, blockMat.createBlockData()));
            }
            if (blockMat.equals(Material.SUGAR_CANE)) {
                // x will never be more than 12
                result.add(new SkyGridBlock(x+1, y, z, Material.WATER));
            }
        } else {
            switch (blockMat) {
            case CACTUS:
                result.add(new SkyGridBlock(x, y, z, Material.SAND));
                result.add(new SkyGridBlock(x, y-1, z, Material.SANDSTONE));
                result.add(new SkyGridBlock(x, y+1, z, blockMat));
                break;
            case NETHER_WART:
                result.add(new SkyGridBlock(x, y, z, Material.SOUL_SAND));
                result.add(new SkyGridBlock(x, y+1, z, blockMat));
                break;
            case END_ROD:
                result.add(new SkyGridBlock(x, y, z, Material.END_STONE));
                result.add(new SkyGridBlock(x, y+1, z, blockMat));
                break;
            case CHORUS_PLANT:
                result.add(new SkyGridBlock(x, y, z, Material.END_STONE));
                result.add(new SkyGridBlock(x, y+1, z, blockMat));
                break;
            default:
                result.add(new SkyGridBlock(x, y, z, blockMat));
            }
        }

    }
}
