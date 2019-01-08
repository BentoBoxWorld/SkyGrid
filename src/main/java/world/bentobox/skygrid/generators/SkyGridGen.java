package world.bentobox.skygrid.generators;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.block.Biome;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.generator.ChunkGenerator;

import world.bentobox.skygrid.SkyGrid;

public class SkyGridGen extends ChunkGenerator {

    private SkyGrid addon;

    // Blocks that need to be placed on dirt
    private final static List<Material> needDirt = Arrays.asList(
            Material.ACACIA_SAPLING,
            Material.BIRCH_SAPLING,
            Material.DARK_OAK_SAPLING,
            Material.JUNGLE_SAPLING,
            Material.OAK_SAPLING,
            Material.SPRUCE_SAPLING,
            Material.RED_MUSHROOM,
            Material.BROWN_MUSHROOM,
            Material.SUGAR_CANE,
            Material.GRASS,
            Material.DEAD_BUSH,
            Material.SUNFLOWER,
            Material.ORANGE_TULIP,
            Material.PINK_TULIP,
            Material.RED_TULIP,
            Material.WHITE_TULIP,
            Material.ROSE_RED,
            Material.ALLIUM,
            Material.AZURE_BLUET,
            Material.BEETROOTS
            // TODO add all the other plants that need to go on dirt
            );



    /**
     * @param addon
     */
    public SkyGridGen(SkyGrid addon) {
        this.addon = addon;
    }

    @Override
    public ChunkData generateChunkData(World world, Random random, int chunkX, int chunkZ, ChunkGenerator.BiomeGrid biomeGrid) {

        BiomeGenerator biomeGenerator = new BiomeGenerator(world);
        // Default block
        Material blockMat = Material.AIR;
        // This gets all the blocks that can be picked and their probabilities
        BlockProbability prob = addon.getWorldStyles().get(world.getEnvironment()).getProb();
        // Biomes
        // Settings.createBiomes
        //int noise = (int)Math.floor(voronoi.noise(chunkx, chunkz, 1) * Biome.values().length);
        //Bukkit.getLogger().info("DEBUG: " + noise + " biome = " + Biome.values()[noise]);


        // The chunk we are making
        ChunkData result = createChunkData(world);
        if (addon.getSettings().isCreateBiomes() && world.getEnvironment().equals(Environment.NORMAL)) {
            for (int x=0; x<16; x++) {
                for (int z=0; z<16; z++) {
                    int realX = x + chunkX * 16; //used so that the noise function gives us
                    int realZ = z + chunkZ * 16; //different values each chunk
                    //We get the 3 closest biome's to the temperature and rainfall at this block
                    HashMap<Biomes, Double> biomes = biomeGenerator.getBiomes(realX, realZ);
                    //And tell bukkit (who tells the client) what the biggest biome here is
                    biomeGrid.setBiome(x, z, getDominantBiome(biomes));
                }
            }

        }
        for (int x = 0; x < 16; x += 4) {
            for (int z = 0; z < 16; z += 4) {
                for (int y = 0; y < addon.getSettings().getIslandHeight(); y += 4) {
                    // Get a random block and feed in the last block (true if cactus or cane)
                    blockMat = prob.getBlock(random, y == 0, blockMat == Material.CACTUS || blockMat == Material.SUGAR_CANE);
                    // Check if the block needs dirt
                    if (needDirt.contains(blockMat)) {
                        // Check biome
                        if (biomeGrid.getBiome(x, z).equals(Biome.DESERT)) {
                            // No plants in desert except for cactus
                            if (y == 0) {
                                blockMat = Material.SAND;
                            } else {
                                blockMat = Material.CACTUS;
                            }
                        } else {
                            // Add dirt
                            result.setBlock( x, y, z, Material.DIRT);
                            result.setBlock( x, y+1, z, blockMat);
                            if (blockMat.equals(Material.SUGAR_CANE)) {
                                //Bukkit.getLogger().info("DEBUG: sugar cane - result of random selection = " + blockMat + " " + (chunkx*16+x) + " " + y + " " + (chunkz*16+z));
                                result.setBlock( x+1, y, z, Material.WATER);
                            }
                            /*
                            else if (blockMat.equals(Material.DOUBLE_PLANT)) {
                                result.setBlock( x, y+2, z, blockMat);
                            }*/
                        }
                    } else {
                        switch (blockMat) {
                        case LAVA:
                            // Don't allow stationary lava in this biome because the swamp tree vines can cause it to drip and lag
                            if (biomeGrid.getBiome(x, z).equals(Biome.SWAMP) || biomeGrid.getBiome(x, z).equals(Biome.SWAMP_HILLS)) {
                                result.setBlock( x, y, z, Material.GRASS);
                            } else {
                                result.setBlock( x, y, z, Material.LAVA);
                            }
                            break;
                        case CACTUS:
                            //Bukkit.getLogger().info("DEBUG: cactus - result of random selection = " + blockMat + " " + (chunkx*16+x) + " " + y + " " + (chunkz*16+z));
                            result.setBlock( x, y, z, Material.SAND);
                            result.setBlock( x, y-1, z, Material.VINE);
                            result.setBlock( x, y+1, z, blockMat);
                            break;
                        case DIRT:
                            if (biomeGrid.getBiome(x, z).equals(Biome.DESERT)) {
                                // Desert
                                result.setBlock( x, y, z, Material.SAND);
                            } else {
                                result.setBlock( x, y, z, Material.DIRT);
                            }
                        case WATER:
                            // TODO: handle other dry biomes
                            if (biomeGrid.getBiome(x, z).equals(Biome.DESERT)) {
                                // Desert
                                result.setBlock( x, y, z, Material.SAND);
                            } else {
                                result.setBlock( x, y, z, Material.WATER);
                            }
                            break;
                        case NETHER_WART:
                            //Bukkit.getLogger().info("DEBUG: nether warts - result of random selection = " + blockMat + " " + (chunkx*16+x) + " " + y + " " + (chunkz*16+z));
                            result.setBlock( x, y, z, Material.SOUL_SAND);
                            result.setBlock( x, y+1, z, blockMat);
                            break;
                        default:
                            // Check strings for backwards compatibility
                            if (blockMat.toString().equals("END_ROD")) {
                                //Bukkit.getLogger().info("DEBUG: end rod - result of random selection = " + blockMat + " " + (chunkx*16+x) + " " + y + " " + (chunkz*16+z));
                                result.setBlock( x, y, z, Material.END_STONE);
                                result.setBlock( x, y+1, z, blockMat);
                            } else if (blockMat.toString().equals("CHORUS_PLANT")) {
                                //Bukkit.getLogger().info("DEBUG: Chorus plant - result of random selection = " + blockMat + " " + (chunkx*16+x) + " " + y + " " + (chunkz*16+z));
                                result.setBlock( x, y, z, Material.END_STONE);
                                result.setBlock( x, y+1, z, blockMat);
                            } else {
                                /*
                                 * debug
			if (blockMat.equals(Material.CHEST)) {
			    int xLoc = (chunkx*16+x);
			    int zLoc = (chunkz*16+z);
			    Bukkit.getLogger().info("DEBUG: setting chest at (" + xLoc + " " + y + " " + zLoc + ")");
			}
                                 */
                                result.setBlock( x, y, z, blockMat);
                            }
                        }
                    }
                }
            }
        }
        return result;
    }

    @Override
    public List<BlockPopulator> getDefaultPopulators(World world) {
        //return Arrays.asList(new BlockPopulator[0]);
        List<BlockPopulator> list = new ArrayList<BlockPopulator>(1);
        list.add(new SkyGridPop(addon));
        return list;
    }



    @Override
    public Location getFixedSpawnLocation(World world, Random random) {
        //Bukkit.getLogger().info("DEBUG: fixed spawn loc requested");
        return new Location(world, 0, addon.getSettings().getIslandHeight() + 2, 0);
    }

    //We get the closest biome to send to the client (using the biomegrid)
    private Biome getDominantBiome(HashMap<Biomes, Double> biomes) {
        double maxNoiz = 0.0;
        Biomes maxBiome = null;

        for (Biomes biome : biomes.keySet()) {
            if (biomes.get(biome) >= maxNoiz) {
                maxNoiz = biomes.get(biome);
                maxBiome = biome;
            }
        }
        return maxBiome.biome;
    }


}