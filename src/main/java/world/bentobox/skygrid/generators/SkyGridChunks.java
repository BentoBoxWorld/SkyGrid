package world.bentobox.skygrid.generators;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.Material;
import org.bukkit.World.Environment;
import org.bukkit.block.data.Bisected;
import org.bukkit.block.data.BlockData;

import com.google.common.base.Enums;

import world.bentobox.skygrid.SkyGrid;

/**
 * A premade randomized chunk of grid
 * @author tastybento
 *
 */
public class SkyGridChunks {

    // Blocks that need to be placed on dirt
    private static final List<Material> NEEDS_DIRT = List.of(Material.ACACIA_SAPLING, Material.ALLIUM, Material.AZURE_BLUET,
            Material.BEETROOTS, Material.BIRCH_SAPLING, Material.BLUE_ORCHID, Material.BROWN_MUSHROOM, Material.DANDELION,
            Material.DARK_OAK_SAPLING, Material.DEAD_BUSH, Material.FERN,
            Enums.getIfPresent(Material.class, "GRASS").or(Material.SHORT_GRASS), Material.JUNGLE_SAPLING,
            Material.LARGE_FERN, Material.LILAC, Material.OAK_SAPLING, Material.ORANGE_TULIP, Material.OXEYE_DAISY,
            Material.PEONY, Material.PINK_TULIP, Material.POPPY, Material.RED_MUSHROOM, Material.RED_TULIP,
            Material.ROSE_BUSH, Material.SPRUCE_SAPLING, Material.SUGAR_CANE, Material.SUNFLOWER, Material.TALL_GRASS,
            Material.WHEAT, Material.WHITE_TULIP);

    private static final int PRE_MADE_CHUNKS_NUMBER = 100;

    private final SkyGrid addon;

    private final Random random = new Random(System.currentTimeMillis());
    private final List<List<SkyGridBlock>> chunks = new ArrayList<>();
    private final List<List<SkyGridBlock>> chunksEnd = new ArrayList<>();

    private final List<List<SkyGridBlock>> chunksNether = new ArrayList<>();

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
            chunks.add(getChunk(prob));
            chunksNether.add(getChunk(probNether));
            chunksEnd.add(getChunk(probEnd));
        }
        addon.log("Done making chunks");
    }

    public Material getBlock(Environment env) {
        BlockProbability prob = addon.getWorldStyles().get(env).getProb();
        return prob.getBlock(random, false, false);
        /*
        // Get a random block and feed in the last block (true if cactus or cane)
        Material blockMat = prob.getBlock(random, y == 0, false);
        // If blockMat is not "a block" then cannot be generated
        if (!blockMat.isAir() && !blockMat.isBlock()) {
            blockMat = Material.STONE;
        }
        // Convert to deep
        if (y < 0) {
            if (blockMat == Material.STONE) {
                blockMat = Material.DEEPSLATE;
        
            } else if (blockMat == Material.COBBLESTONE) {
                blockMat = Material.COBBLED_DEEPSLATE;
            } else {
                blockMat = Enums.getIfPresent(Material.class, "DEEPSLATE_" + blockMat.name()).or(blockMat);
            }
        }
        return new SkyGridBlock(x, y, z, blockMat);*/
    }

    private List<SkyGridBlock> getChunk(BlockProbability prob) {
        List<SkyGridBlock> result = new ArrayList<>();
        for (int x = 1; x < 16; x += 4) {
            for (int z = 1; z < 16; z += 4) {
                for (int y = -64; y <= addon.getSettings().getIslandHeight(); y += 4) {
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
        return switch (env) {
        case NETHER -> chunksNether.get(random.nextInt(chunksNether.size()));
        case THE_END -> chunksEnd.get(random.nextInt(chunksEnd.size()));
        default -> chunks.get(random.nextInt(chunks.size()));
        };

    }

    private void setBlock(BlockProbability prob, int x, int y, int z, List<SkyGridBlock> result) {
        // Get a random block and feed in the last block (true if cactus or cane)
        Material blockMat = prob.getBlock(random, y == 0, false);
        // If blockMat is not "a block" then cannot be generated
        if (!blockMat.isAir() && !blockMat.isBlock()) {
            blockMat = Material.STONE;
        }
        // Convert to deep
        if (y < 0) {
            if (blockMat == Material.STONE) {
                blockMat = Material.DEEPSLATE;

            } else if (blockMat == Material.COBBLESTONE) {
                blockMat = Material.COBBLED_DEEPSLATE;
            } else {
                blockMat = Enums.getIfPresent(Material.class, "DEEPSLATE_" + blockMat.name()).or(blockMat);
            }
        }

        // Check if the block needs dirt
        if (NEEDS_DIRT.contains(blockMat)) {
            // Add dirt
            result.add(new SkyGridBlock(x, y, z, Material.DIRT.createBlockData()));
            BlockData dataBottom = blockMat.createBlockData();
            if (dataBottom instanceof Bisected bisected) {
                bisected.setHalf(Bisected.Half.BOTTOM);
                BlockData dataTop = blockMat.createBlockData();
                bisected.setHalf(Bisected.Half.TOP);
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
            case CACTUS, DEAD_BUSH -> {
                result.add(new SkyGridBlock(x, y, z, Material.SAND));
                result.add(new SkyGridBlock(x, y - 1, z, Material.SANDSTONE));
                result.add(new SkyGridBlock(x, y + 1, z, blockMat));
            }
            case NETHER_WART -> {
                result.add(new SkyGridBlock(x, y, z, Material.SOUL_SAND));
                result.add(new SkyGridBlock(x, y + 1, z, blockMat));
            }
            case END_ROD, CHORUS_PLANT -> {
                result.add(new SkyGridBlock(x, y, z, Material.END_STONE));
                result.add(new SkyGridBlock(x, y + 1, z, blockMat));
            }
            default -> result.add(new SkyGridBlock(x, y, z, blockMat));
            }
        }

    }
}
