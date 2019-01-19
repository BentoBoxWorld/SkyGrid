package world.bentobox.skygrid.generators;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.TreeMap;
import java.util.stream.Collectors;

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
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.Sapling;

import world.bentobox.skygrid.SkyGrid;


public class SkyGridPop extends BlockPopulator {
    private static RandomSeries slt = new RandomSeries(27);
    private final int size;
    private SkyGrid addon;
    private final static HashMap<Material, Double> END_ITEMS;
    private final static List<Material> FOOD = Arrays.stream(Material.values()).filter(Material::isEdible).collect(Collectors.toList());
    private final static List<Material> SPAWNEGGS = Arrays.stream(Material.values()).filter(m -> m.name().endsWith("SPAWN_EGG")).collect(Collectors.toList());
    private final static List<Material> BLOCKS = Arrays.stream(Material.values()).filter(Material::isBlock).collect(Collectors.toList());
    private final static List<Material> ITEMS = Arrays.stream(Material.values()).filter(Material::isItem).collect(Collectors.toList());

    private final static Material[] SAPLING_TYPE = {
            Material.ACACIA_SAPLING,
            Material.BIRCH_SAPLING,
            Material.DARK_OAK_SAPLING,
            Material.JUNGLE_SAPLING,
            Material.OAK_SAPLING,
            Material.SPRUCE_SAPLING
    };

    static {
        // Hard code these probabilities. TODO: make these config.yml settings.
        END_ITEMS = new HashMap<Material, Double>();
        // double format - integer part is the quantity, decimal is the probability
        END_ITEMS.put(Material.FIREWORK_ROCKET, 20.2); // for elytra
        END_ITEMS.put(Material.EMERALD, 1.1);
        END_ITEMS.put(Material.CHORUS_FRUIT, 3.2);
        END_ITEMS.put(Material.ELYTRA, 1.1);
        END_ITEMS.put(Material.SHULKER_BOX, 1.2);
    }

    public SkyGridPop(SkyGrid addon) {
        this.addon = addon;
        this.size = addon.getSettings().getIslandHeight();
    }

    @Override
    public void populate(World world, Random random, Chunk chunk) {
        boolean chunkHasPortal = false;
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
                                    endFrame.setEye(random.nextDouble() < 0.8);
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
                                    frame.setBlockData(endFrame);
                                }
                            }
                        }
                    }
                    if (b.getType().equals(Material.AIR))
                        continue;
                    // Alter blocks
                    switch (b.getType()) {
                    case CHEST:
                        setChest(b, random);
                        break;
                    case SPAWNER:
                        setSpawner(b, random);
                        break;
                    case DIRT:
                        if (b.getRelative(BlockFace.UP).getBlockData() instanceof Sapling) {
                            if (addon.getSettings().isGrowTrees() && random.nextBoolean()) {
                                // Get biome
                                Biome biome = b.getBiome();
                                switch (biome) {

                                case DESERT:
                                    b.setType(Material.SAND);
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
                        }
                        break;
                    default:
                        break;
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
        EntityType type = spawns.ceilingEntry(randKey).getValue();
        spawner.setDelay(120);
        spawner.setSpawnedType(type);
        spawner.update(true);
    }

    private void setChest(Block b, Random random) {
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
                set.add(pickEgg(random, EntityType.GHAST, EntityType.PIG_ZOMBIE, EntityType.ENDERMAN)); //spawn eggs
            } else if (random.nextDouble() < 0.9) {
                // Blaze, Magma Cube
                set.add(pickEgg(random, EntityType.BLAZE, EntityType.MAGMA_CUBE)); //spawn eggs
            }
            if (random.nextDouble() < 0.3) {

                Double rand1 = random.nextDouble();
                if (rand1 < 0.1)
                    set.add(new ItemStack(Material.CLOCK)); // clock
                else if (rand1 < 0.5) {
                    set.add(new ItemStack(Material.BLAZE_ROD));
                } else if (rand1 < 0.6) {
                    set.add(new ItemStack(Material.SADDLE));
                } else if (rand1 < 0.7) {
                    set.add(new ItemStack(Material.IRON_HORSE_ARMOR));
                } else if (rand1 < 0.8) {
                    set.add(new ItemStack(Material.GOLDEN_HORSE_ARMOR));
                } else if (rand1 < 0.9) {
                    set.add(new ItemStack(Material.DIAMOND_HORSE_ARMOR));
                } else {
                    set.add(new ItemStack(Material.GHAST_TEAR));
                }
            }
            break;
        case NORMAL:
            if (random.nextDouble() < 0.7) {
                set.add(new ItemStack(FOOD.get(random.nextInt(FOOD.size())), random.nextInt(3))); //food/tools
            }
            if (random.nextDouble() < 0.3) {
                set.add(new ItemStack(SPAWNEGGS.get(random.nextInt(SPAWNEGGS.size()))));
            }
            if (random.nextDouble() < 0.7) {
                set.add(new ItemStack(BLOCKS.get(random.nextInt(BLOCKS.size())), random.nextInt(3)));
            }

            if (random.nextDouble() < 0.1) {
                set.add(new ItemStack(BLOCKS.get(random.nextInt(ITEMS.size())), random.nextInt(3)));
            }


            break;
        case THE_END:
            set.add(itemInRange(318, 350, random)); //food/tools
            if (random.nextDouble() < 0.2)
                set.add(new ItemStack(Material.ENDERMAN_SPAWN_EGG)); //enderman spawn egg
            if (random.nextDouble() < 0.4)
                set.add(itemInRange(256, 294, random)); //weapon/random
            for (Material mat : Material.values()) {
                if (END_ITEMS.containsKey(mat)) {
                    int qty = (int)((double)END_ITEMS.get(mat));
                    double probability = END_ITEMS.get(mat) - qty;
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

    private ItemStack pickEgg(Random random, EntityType... type) {
        EntityType choice = type[random.nextInt(type.length)];
        return new ItemStack(Material.valueOf(choice.name() + "_SPAWN_EGG"));
    }
    private ItemStack itemInRange(int min, int max, Random random) {
        return new ItemStack(Arrays.asList(Material.values()).get(random.nextInt(max - min + 1) + min), 1);
    }

}