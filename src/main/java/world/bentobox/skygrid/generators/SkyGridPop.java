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
    private static final RandomSeries slt = new RandomSeries(27);
    private final int size;
    private SkyGrid addon;
    private final static HashMap<Material, Double> END_ITEMS;
    private final static List<Material> FOOD = Arrays.stream(Material.values()).filter(Material::isEdible).filter(m -> !m.equals(Material.POISONOUS_POTATO)).collect(Collectors.toList());
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
        END_ITEMS = new HashMap<>();
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
        // Do an end portal check
        if (addon.getSettings().isEndGenerate() && world.getEnvironment().equals(Environment.NORMAL)) {
            checkEndPortal(chunk, random);
        }
        for (int x = 1; x < 16; x += 4) {
            for (int z = 1; z < 16; z +=4) {
                for (int y = 0; y <= size; y += 4) {
                    Block b = chunk.getBlock(x, y, z);
                    if (b.getType().equals(Material.AIR))
                        continue;
                    // Alter blocks
                    switch (b.getType()) {
                    case CHEST:
                        setChest(b, random, world);
                        break;
                    case SPAWNER:
                        setSpawner(b, random, world);
                        break;
                    case DIRT:
                        if (b.getRelative(BlockFace.UP).getBlockData() instanceof Sapling) {
                            if (b.getBiome().equals(Biome.DESERT)) {
                                b.setType(Material.SAND, false);
                            } else {
                                setSaplingType(b.getRelative(BlockFace.UP), random, world);
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

    private void checkEndPortal(Chunk chunk, Random random) {
        if (random.nextDouble() < addon.getSettings().getEndFrameProb()) {
            for (int xx = 1; xx< 6; xx++) {
                for (int zz = 1; zz < 6; zz++) {
                    if (xx == zz || (xx==1 && zz==5) || (xx==5 && zz==1))
                        continue;
                    if (xx>1 && xx<5 && zz>1 && zz<5) {
                        continue;
                    }
                    Block frame = chunk.getBlock(xx, 0, zz);
                    frame.setType(Material.END_PORTAL_FRAME, false);
                    // Cast to end frame
                    EndPortalFrame endFrame = (EndPortalFrame)frame.getBlockData();

                    // Add the odd eye of ender
                    endFrame.setEye(random.nextDouble() < 0.8);
                    if (zz == 1) {
                        // Face South
                        endFrame.setFacing(BlockFace.SOUTH);
                    } else if (zz == 5) {
                        // Face North
                        endFrame.setFacing(BlockFace.NORTH);
                    } else if (xx == 1) {
                        // Face East
                        endFrame.setFacing(BlockFace.EAST);
                    } else {
                        // Face West
                        endFrame.setFacing(BlockFace.WEST);
                    }
                    frame.setBlockData(endFrame, false);
                }
            }
        }
    }

    private void setSaplingType(Block b, Random random, World world) {
        // Set sapling type
        switch (b.getBiome()) {
        case JUNGLE:
            b.setType(Material.JUNGLE_SAPLING, false);
            break;
        case PLAINS:
            if (random.nextBoolean()) {
                b.setType(Material.BIRCH_SAPLING, false); // Birch
            }
            // else Oak
            break;
        case TAIGA:
            b.setType(Material.SPRUCE_SAPLING, false);
            break;
        case SWAMP:
            break;
        case DESERT:
        case DESERT_HILLS:
            b.setType(Material.DEAD_BUSH, false);
            break;
        case SAVANNA:
            b.setType(Material.ACACIA_SAPLING, false); // Acacia
            break;
        case FOREST:
        default:
            b.setType(SAPLING_TYPE[random.nextInt(6)], false);
        }

    }

    private void setSpawner(Block b, Random random, World world) {
        CreatureSpawner spawner = (CreatureSpawner) b.getState();
        TreeMap<Integer,EntityType> spawns = addon.getWorldStyles().get(b.getWorld().getEnvironment()).getSpawns();
        int randKey = random.nextInt(spawns.lastKey());
        EntityType type = spawns.ceilingEntry(randKey).getValue();
        spawner.setDelay(120);
        spawner.setSpawnedType(type);
        spawner.update(true, false);
    }

    private void setChest(Block b, Random random, World world) {
        Chest chest = (Chest) b.getState();
        Inventory inv = chest.getBlockInventory();
        HashSet<ItemStack> set = new HashSet<>();
        switch (b.getWorld().getEnvironment()) {
        case NETHER:
            for (int i = 0; i < addon.getSettings().getChestFillNether(); i ++) {
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

                    double rand1 = random.nextDouble();
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
            }
            break;
        case NORMAL:
            for (int i = 0; i < addon.getSettings().getChestFill(); i ++) {
                set.add(new ItemStack(FOOD.get(random.nextInt(FOOD.size())), random.nextInt(3))); //food/tools
                if (random.nextDouble() < 0.7) {
                    set.add(new ItemStack(FOOD.get(random.nextInt(FOOD.size())), random.nextInt(3))); //food/tools
                    set.add(new ItemStack(FOOD.get(random.nextInt(FOOD.size())), random.nextInt(3))); //food/tools
                    set.add(new ItemStack(FOOD.get(random.nextInt(FOOD.size())), random.nextInt(3))); //food/tools
                }
                if (random.nextDouble() < 0.2) {
                    set.add(new ItemStack(FOOD.get(random.nextInt(FOOD.size())), random.nextInt(3))); //food/tools
                    set.add(new ItemStack(FOOD.get(random.nextInt(FOOD.size())), random.nextInt(3))); //food/tools
                }
                if (random.nextDouble() < 0.1) {
                    set.add(new ItemStack(SPAWNEGGS.get(random.nextInt(SPAWNEGGS.size()))));
                }
                set.add(new ItemStack(BLOCKS.get(random.nextInt(BLOCKS.size())), random.nextInt(3)));
                if (random.nextDouble() < 0.7) {
                    set.add(new ItemStack(BLOCKS.get(random.nextInt(BLOCKS.size())), random.nextInt(3)));
                }
                if (random.nextDouble() < 0.7) {
                    set.add(new ItemStack(BLOCKS.get(random.nextInt(BLOCKS.size())), random.nextInt(3)));
                }
                if (random.nextDouble() < 0.7) {
                    set.add(new ItemStack(BLOCKS.get(random.nextInt(BLOCKS.size())), random.nextInt(3)));
                }
                if (random.nextDouble() < 0.5) {
                    set.add(new ItemStack(ITEMS.get(random.nextInt(ITEMS.size())), random.nextInt(3)));
                }
                if (random.nextDouble() < 0.4) {
                    set.add(new ItemStack(ITEMS.get(random.nextInt(ITEMS.size())), random.nextInt(3)));
                }
                if (random.nextDouble() < 0.3) {
                    set.add(new ItemStack(ITEMS.get(random.nextInt(ITEMS.size())), random.nextInt(3)));
                }
                if (random.nextDouble() < 0.1) {
                    set.add(new ItemStack(Material.WATER_BUCKET));
                }
            }
            break;
        case THE_END:
            for (int i = 0; i < addon.getSettings().getChestFillEnd(); i ++) {
                set.add(itemInRange(318, 350, random)); //food/tools
                set.add(itemInRange(318, 350, random)); //food/tools
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
            }
            break;
        default:
            break;

        }
        // Put the first 27 items into the chest
        set.stream().limit(27).forEach(i -> inv.setItem(slt.next(random), i));
        slt.reset();
    }

    private ItemStack pickEgg(Random random, EntityType... type) {
        EntityType choice = type[random.nextInt(type.length)];
        if (choice == EntityType.PIG_ZOMBIE) {
            return new ItemStack(Material.ZOMBIE_PIGMAN_SPAWN_EGG);
        } else {
            return new ItemStack(Material.valueOf(choice.name() + "_SPAWN_EGG"));
        }
    }
    private ItemStack itemInRange(int min, int max, Random random) {
        return new ItemStack(Arrays.asList(Material.values()).get(random.nextInt(max - min + 1) + min), 1);
    }

}