package world.bentobox.skygrid.generators;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;
import java.util.TreeMap;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.TreeType;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Chest;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.block.data.type.EndPortalFrame;
import org.bukkit.block.data.type.Sapling;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import world.bentobox.skygrid.SkyGrid;


public class SkyGridPop extends BlockPopulator {
    private static RandomSeries slt = new RandomSeries(27);
    private final int size;
    private SkyGrid addon;
    private final static boolean DEBUG = false;
    private final static HashMap<String, Double> END_ITEMS;
    private final static HashMap<String, Short> spawnEggData;

    private final static Material[] SAPLING_TYPE = {
            Material.ACACIA_SAPLING,
            Material.BIRCH_SAPLING,
            Material.DARK_OAK_SAPLING,
            Material.JUNGLE_SAPLING,
            Material.OAK_SAPLING,
            Material.SPRUCE_SAPLING
    };
    private final static Material[] DIRT_TYPE = {
            Material.DIRT,
            Material.COARSE_DIRT,
            Material.PODZOL
    };
    private final static Material[] SAND_TYPE = {
            Material.SAND,
            Material.RED_SAND
    };

    static {
        // Hard code these probabilities. TODO: make these config.yml settings.
        END_ITEMS = new HashMap<String, Double>();
        // double format - integer part is the quantity, decimal is the probability
        END_ITEMS.put("FIREWORK",20.2); // for elytra
        END_ITEMS.put("EMERALD", 1.1);
        END_ITEMS.put("CHORUS_FRUIT", 3.2);
        END_ITEMS.put("ELYTRA", 1.1);
        END_ITEMS.put("PURPLE_SHULKER_BOX", 1.2);

        spawnEggData = new HashMap<String, Short>();
        short index = 21; // The magic number
        for (EntityType type : EntityType.values()) {
            if (type.isAlive()) {
                //Bukkit.getLogger().info("DEBUG: " + type.toString() + "=>" + (index));
                spawnEggData.put(type.toString(), index);
            }
            index++;
        }
    }

    /**
     * @param size
     */
    public SkyGridPop(SkyGrid addon) {
        this.addon = addon;
        this.size = addon.getSettings().getIslandHeight();
    }

    @Override
    public void populate(World world, Random random, Chunk chunk) {
        if (DEBUG)
            Bukkit.getLogger().info("DEBUG: populate chunk");
        boolean chunkHasPortal = false;
        int r = 0;
        for (int x = 0; x < 16; x += 4) {
            for (int y = 0; y < size; y += 4) {
                for (int z = 0; z < 16; z +=4) {
                    Block b = chunk.getBlock(x, y, z);
                    // Do an end portal check
                    if (addon.getSettings().isEndGenerate() && world.getEnvironment().equals(Environment.NORMAL)
                            && x==0 && z==0 && y == 0 && !chunkHasPortal) {
                        if (random.nextDouble() < addon.getSettings().getEndFrameProb()) {
                            chunkHasPortal = true;
                            for (int xx = 0; xx< 5; xx++) {
                                for (int zz = 0; zz < 5; zz++) {
                                    if (xx == zz || (xx==0 && zz==4) || (xx==4 && zz==0))
                                        continue;
                                    if (xx>0 && xx<4 && zz>0 && zz<4) {
                                        continue;
                                    }
                                    Block frame = chunk.getBlock(xx, 0, zz);
                                    frame.setType(Material.END_PORTAL_FRAME);
                                    // Cast to end frame
                                    EndPortalFrame endFrame = (EndPortalFrame)frame.getBlockData();

                                    // Add the odd eye of ender
                                    endFrame.setEye(random.nextDouble() < 0.1);
                                    if (zz == 0) {
                                        // Face South
                                        endFrame.setFacing(BlockFace.SOUTH);
                                    } else if (zz == 4) {
                                        // Face North
                                        endFrame.setFacing(BlockFace.NORTH);
                                    } else if (xx == 0) {
                                        // Face East
                                        endFrame.setFacing(BlockFace.EAST);
                                    } else if (xx == 4) {
                                        // Face West
                                        endFrame.setFacing(BlockFace.WEST);
                                    }
                                }
                            }
                        }
                    }
                    if (b.getType().equals(Material.AIR))
                        continue;
                    // Alter blocks
                    switch (b.getType()) {
                    case CHEST:
                        if (DEBUG)
                            Bukkit.getLogger().info("DEBUG: chest");
                        setChest(b, random);
                        break;
                    case SPAWNER:
                        if (DEBUG)
                            Bukkit.getLogger().info("DEBUG: mob spawner");
                        setSpawner(b, random);
                        break;
                    case DIRT:
                        if (DEBUG)
                            Bukkit.getLogger().info("DIRT");
                        if (b.getRelative(BlockFace.UP).getBlockData() instanceof Sapling) {
                            if (addon.getSettings().isGrowTrees() && random.nextBoolean()) {
                                // Get biome
                                Biome biome = b.getBiome();
                                switch (biome) {

                                case DESERT:
                                    // never used because dirt = sand in desert
                                    b.getRelative(BlockFace.UP).setType(Material.DEAD_BUSH);
                                    break;
                                case BIRCH_FOREST:
                                case BIRCH_FOREST_HILLS:
                                case FOREST:
                                    b.getRelative(BlockFace.UP).setType(Material.AIR);
                                    world.generateTree(b.getRelative(BlockFace.UP).getLocation(),TreeType.BIRCH);
                                    break;
                                case SWAMP:
                                    b.getRelative(BlockFace.UP).setType(Material.AIR);
                                    world.generateTree(b.getRelative(BlockFace.UP).getLocation(),TreeType.SWAMP);
                                    break;
                                case SAVANNA:
                                case SAVANNA_PLATEAU:
                                case SHATTERED_SAVANNA:
                                case SHATTERED_SAVANNA_PLATEAU:
                                    b.getRelative(BlockFace.UP).setType(Material.AIR);
                                    world.generateTree(b.getRelative(BlockFace.UP).getLocation(),TreeType.ACACIA);
                                    break;
                                case JUNGLE_EDGE:
                                case JUNGLE_HILLS:
                                case MODIFIED_JUNGLE:
                                case MODIFIED_JUNGLE_EDGE:
                                case JUNGLE:
                                    b.getRelative(BlockFace.UP).setType(Material.AIR);
                                    //b.getRelative(BlockFace.UP).setData((byte)3);
                                    world.generateTree(b.getRelative(BlockFace.UP).getLocation(),TreeType.JUNGLE);
                                    break;
                                case SNOWY_BEACH:
                                case SNOWY_MOUNTAINS:
                                case SNOWY_TAIGA:
                                case SNOWY_TAIGA_HILLS:
                                case SNOWY_TAIGA_MOUNTAINS:
                                case SNOWY_TUNDRA:
                                    b.getRelative(BlockFace.UP).setType(Material.AIR);
                                    world.generateTree(b.getRelative(BlockFace.UP).getLocation(),TreeType.REDWOOD);
                                    break;
                                case GIANT_SPRUCE_TAIGA:
                                case GIANT_SPRUCE_TAIGA_HILLS:
                                case GIANT_TREE_TAIGA:
                                case GIANT_TREE_TAIGA_HILLS:
                                    b.getRelative(BlockFace.UP).setType(Material.AIR);
                                    world.generateTree(b.getRelative(BlockFace.UP).getLocation(),TreeType.MEGA_REDWOOD);
                                    break;
                                case DARK_FOREST:
                                case DARK_FOREST_HILLS:
                                    b.getRelative(BlockFace.UP).setType(Material.AIR);
                                    world.generateTree(b.getRelative(BlockFace.UP).getLocation(),TreeType.DARK_OAK);
                                    break;
                                default:
                                    b.getRelative(BlockFace.UP).setType(Material.AIR);
                                    world.generateTree(b.getRelative(BlockFace.UP).getLocation(),TreeType.TREE);
                                    break;
                                }
                            } else {
                                setSaplingType(b, random);
                            }
                        } else if (b.getRelative(BlockFace.UP).getType().equals(Material.AIR)){
                            // Randomize the dirt type
                            b.setType(DIRT_TYPE[random.nextInt(3)]);
                        }
                        break;
                    case SAND:
                        if (DEBUG)
                            Bukkit.getLogger().info("DEBUG SAND");
                        b.setType(SAND_TYPE[random.nextInt(2)]);
                    case OAK_LOG:
                        if (DEBUG)
                            Bukkit.getLogger().info("DEBUG: OAK LOG");

                        if (addon.getSettings().isCreateBiomes()) {
                            setSaplingType(b, random);
                        } else {
                            b.setType(SAPLING_TYPE[random.nextInt(6)]);
                        }
                        break;
                        /*case OAK_LEAVES:
                        if (DEBUG)
                            Bukkit.getLogger().info("DEBUG: leaves");
                        int ra = random.nextInt(6);
                        if (ra < 4) {
                            b.setData((byte)ra);
                        } else {
                            b.setType(Material.LEAVES_2);
                            b.setData((byte)random.nextInt(2));
                        }
                        break;
                         */
                    default:
                        break;
                    }
                    // Check blocks above the block
                    /*
                    switch (b.getRelative(BlockFace.UP).getType()) {
                    case LONG_GRASS:
                        if (DEBUG)
                            Bukkit.getLogger().info("DEBUG: LONG grass");
                        if (Settings.createBiomes && b.getBiome().toString().contains("DESERT")) {
                            b.getRelative(BlockFace.UP).setType(Material.DEAD_BUSH);
                        } else {
                            b.getRelative(BlockFace.UP).setData((byte)random.nextInt(3));
                        }
                        break;
                    case RED_ROSE:
                        if (DEBUG)
                            Bukkit.getLogger().info("DEBUG: red rose");
                        if (Settings.createBiomes && b.getBiome().toString().contains("DESERT")) {
                            b.getRelative(BlockFace.UP).setType(Material.DEAD_BUSH);
                        } else {
                            b.getRelative(BlockFace.UP).setData((byte)random.nextInt(9));
                        }
                        break;
                    case DOUBLE_PLANT:
                        if (DEBUG)
                            Bukkit.getLogger().info("DEBUG: Double plant");
                        if (Settings.createBiomes && b.getBiome().toString().contains("DESERT")) {
                            b.getRelative(BlockFace.UP).setType(Material.DEAD_BUSH);
                        } else {
                            b.getRelative(BlockFace.UP).setData((byte)random.nextInt(6));
                            b.getRelative(BlockFace.UP).getRelative(BlockFace.UP).setData((byte)8);
                        }
                        break;
                    default:
                        break;
                    }*/
                    // Nether
                    if (b.getWorld().getEnvironment().equals(Environment.NETHER)) {
                        if (b.getType().equals(Material.STONE)) {
                            b.setType(Material.NETHER_QUARTZ_ORE);
                        }
                    }
                    // End
                    if (b.getWorld().getEnvironment().equals(Environment.THE_END)) {
                        if (DEBUG)
                            Bukkit.getLogger().info("DEBUG the end " + b);
                        if (b.getRelative(BlockFace.UP).getType().toString().equals("CHORUS_PLANT")) {
                            if (DEBUG)
                                Bukkit.getLogger().info("DEBUG Chorus Plant");
                            world.generateTree(b.getRelative(BlockFace.UP).getLocation(), TreeType.CHORUS_PLANT);
                        }
                        // End crystal becomes hay block in the generator - leave lighting calcs crash server
                        /*
			if (b.getRelative(BlockFace.UP).getType().equals(Material.HAY_BLOCK)) {
			    b.getRelative(BlockFace.UP).setType(Material.AIR);
			    b.getWorld().spawn(b.getRelative(BlockFace.UP).getLocation(), EnderCrystal.class);
			}
                         */
                    }
                }
            }
        }
    }

    private void setSaplingType(Block b, Random random) {
        // Set sapling type
        switch (b.getBiome()) {
        case JUNGLE:
            b.setType(Material.JUNGLE_SAPLING);
            break;
        case PLAINS:
            if (random.nextBoolean()) {
                b.setType(Material.BIRCH_SAPLING); // Birch
            }
            // else Oak
            break;
        case TAIGA:
            b.setType(Material.SPRUCE_SAPLING);
            break;
        case SWAMP:
            break;
        case DESERT:
        case DESERT_HILLS:
            b.setType(Material.DEAD_BUSH);
            break;
        case SAVANNA:
            b.setType(Material.ACACIA_SAPLING); // Acacia
            break;
        case FOREST:
        default:
            b.setType(SAPLING_TYPE[random.nextInt(6)]);
        }

    }

    private void setSpawner(Block b, Random random) {
        CreatureSpawner spawner = (CreatureSpawner) b.getState();
        TreeMap<Integer,EntityType> spawns = addon.getWorldStyles().get(b.getWorld().getEnvironment()).getSpawns();
        int randKey = random.nextInt(spawns.lastKey());
        //Bukkit.getLogger().info("DEBUG: spawner rand key = " + randKey + " out of " + spawns.lastKey());
        EntityType type = spawns.ceilingEntry(randKey).getValue();
        //Bukkit.getLogger().info("DEBUG: spawner type = " + type);
        spawner.setDelay(120);
        spawner.setSpawnedType(type);
        spawner.update(true);
    }

    private void setChest(Block b, Random random) {
        //Bukkit.getLogger().info("DEBUG: setChest");
        Chest chest = (Chest) b.getState();
        Inventory inv = chest.getBlockInventory();
        HashSet<ItemStack> set = new HashSet<ItemStack>();
        // Overworld
        switch (b.getWorld().getEnvironment()) {
        case NETHER:
            if (random.nextDouble() < 0.7)
                set.add(itemInRange(256, 294, random)); //weapon/random
            if (random.nextDouble() < 0.7) {
                ItemStack armor = itemInRange(298, 317, random); //armor
                if (armor.getType().toString().endsWith("BOOTS")) {
                    if (random.nextDouble() < 0.5) {
                        armor.addEnchantment(Enchantment.PROTECTION_FALL, random.nextInt(4) + 1);
                    }
                }
                set.add(armor); //armor
            }

            if (random.nextDouble() < 0.9) {
                // ghast, pigman, enderman
                set.add(damageInRange(383, 56, 58, random)); //spawn eggs
            } else if (random.nextDouble() < 0.9) {
                // Blaze, Magma Cube
                set.add(damageInRange(383, 61, 62, random)); //spawn eggs
            }
            if (random.nextDouble() < 0.3) {
                /*
                Double rand1 = random.nextDouble();
                if (rand1 < 0.1)
                    set.add(new ItemStack(Material.WATCH)); // clock
                else if (rand1 < 0.5) {
                    set.add(new ItemStack(Material.BLAZE_ROD));
                } else if (rand1 < 0.6) {
                    set.add(new ItemStack(Material.SADDLE));
                } else if (rand1 < 0.7) {
                    set.add(new ItemStack(Material.IRON_BARDING));
                } else if (rand1 < 0.8) {
                    set.add(new ItemStack(Material.GOLD_BARDING));
                } else if (rand1 < 0.9) {
                    set.add(new ItemStack(Material.DIAMOND_BARDING));
                } else {
                    set.add(new ItemStack(Material.GHAST_TEAR));
                }*/
            }
            break;
        case NORMAL:
            if (random.nextDouble() < 0.7)
                set.add(itemInRange(256, 294, random)); //weapon/random

            if (random.nextDouble() < 0.7)
                set.add(itemInRange(298, 317, random)); //armor

            if (random.nextDouble() < 0.7)
                set.add(itemInRange(318, 350, random)); //food/tools
            if (random.nextDouble() < 0.3) {
                // Creeper, skeleton, spider
                set.add(damageInRange(383, 50, 52, random)); //spawn eggs
            } else if (random.nextDouble() < 0.9) {
                // Zombie, slime
                set.add(damageInRange(383, 54, 55, random)); //spawn eggs
            } else if (random.nextDouble() < 0.9) {
                // Enderman, cave spider, silverfish
                set.add(damageInRange(383, 58, 60, random)); //spawn eggs
            }
            if (random.nextDouble() < 0.4) {
                // Sheep, Cow, chicken, squid, wolf, mooshroom
                set.add(damageInRange(383, 91, 96, random)); //spawn eggs
            }
            if (random.nextDouble() < 0.1) {
                // Ocelot
                set.add(new ItemStack(Material.OCELOT_SPAWN_EGG)); //ocelot spawn egg
            }
            if (random.nextDouble() < 0.1)
                set.add(new ItemStack(Material.VILLAGER_SPAWN_EGG)); //villager spawn egg

            if (random.nextDouble() < 0.1) {
                Double rand = random.nextDouble();
                if (rand < 0.25) {
                    set.add(new ItemStack(Material.HORSE_SPAWN_EGG)); //horse spawn egg
                } else if (rand < 0.5) {
                    set.add(new ItemStack(Material.RABBIT_SPAWN_EGG)); //rabbit spawn egg
                } else if (rand < 0.75) {
                    set.add(new ItemStack(Material.POLAR_BEAR_SPAWN_EGG)); //polar bear spawn egg
                } else
                    set.add(new ItemStack(Material.GUARDIAN_SPAWN_EGG)); //guardian spawn egg
            }
            if (random.nextDouble() < 0.7)
                // Stone, Grass, Dirt, Cobblestone, Planks
                set.add(itemMas(1, 5, 10, 64, random)); //materials

            set.add(damageInRange(6, 0, 5, random)); //sapling

            if (random.nextDouble() < 0.1)
                // Prismarine
                set.add(itemInRange(409, 410, random));

            //for dyes
            if (random.nextDouble() < 0.3)
                set.add(damageInRange(351, 0, 15, random));

            break;
        case THE_END:
            set.add(itemInRange(318, 350, random)); //food/tools
            if (random.nextDouble() < 0.2)
                set.add(new ItemStack(Material.ENDERMAN_SPAWN_EGG)); //enderman spawn egg
            if (random.nextDouble() < 0.4)
                set.add(itemInRange(256, 294, random)); //weapon/random
            for (Material mat : Material.values()) {
                if (END_ITEMS.containsKey(mat.toString())) {
                    int qty = (int)((double)END_ITEMS.get(mat.toString()));
                    double probability = END_ITEMS.get(mat.toString()) - qty;
                    if (random.nextDouble() < probability) {
                        set.add(new ItemStack(mat, qty));
                    }
                }
            }
            if (random.nextDouble() < 0.2)
                set.add(new ItemStack(Material.SHULKER_SPAWN_EGG)); //shulker spawn egg
            break;
        default:
            break;

        }

        for (ItemStack i : set) {
            inv.setItem(slt.next(random), i);
        }
        slt.reset();
    }

    private ItemStack itemInRange(int min, int max, Random random) {
        return new ItemStack(Material.GOLD_INGOT, 1);
        //return new ItemStack(random.nextInt(max - min + 1) + min, 1);
    }

    private ItemStack damageInRange(int type, int min, int max, Random random) {
        return new ItemStack(Material.IRON_BARS, 1);
        //return new ItemStack(type, 1, (short) (random.nextInt(max - min + 1) + min));
    }

    private ItemStack itemMas(int min, int max, int sm, int lg, Random random) {
        return new ItemStack(Material.IRON_INGOT, 1);
        //return new ItemStack(random.nextInt(max - min + 1) + min,
        //        random.nextInt(lg - sm + 1) + sm);
    }


}