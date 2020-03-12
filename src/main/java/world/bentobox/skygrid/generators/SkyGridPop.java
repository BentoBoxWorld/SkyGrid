package world.bentobox.skygrid.generators;

import java.util.List;
import java.util.Objects;
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
import org.bukkit.entity.EntityType;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import world.bentobox.skygrid.SkyGrid;


public class SkyGridPop extends BlockPopulator {
    private static final RandomSeries slt = new RandomSeries(27);
    private final int size;
    private SkyGrid addon;
    private final List<Material> chestItemsWorld;
    private final List<Material> chestItemsNether;
    private final List<Material> chestItemsEnd;

    private final static Material[] SAPLING_TYPE = {
            Material.ACACIA_SAPLING,
            Material.BIRCH_SAPLING,
            Material.DARK_OAK_SAPLING,
            Material.JUNGLE_SAPLING,
            Material.OAK_SAPLING,
            Material.SPRUCE_SAPLING
    };

    public SkyGridPop(SkyGrid addon) {
        this.addon = addon;
        this.size = addon.getSettings().getIslandHeight();
        // Load the chest items
        chestItemsWorld = addon.getSettings().getChestItemsOverworld().stream().map(Material::matchMaterial).filter(Objects::nonNull).collect(Collectors.toList());
        chestItemsNether = addon.getSettings().getChestItemsNether().stream().map(Material::matchMaterial).filter(Objects::nonNull).collect(Collectors.toList());
        chestItemsEnd = addon.getSettings().getChestItemsEnd().stream().map(Material::matchMaterial).filter(Objects::nonNull).collect(Collectors.toList());
        addon.log("Loaded " + chestItemsWorld.size() + " chest items for world");
        addon.log("Loaded " + chestItemsNether.size() + " chest items for nether world");
        addon.log("Loaded " + chestItemsEnd.size() + " chest items for end world");

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
        slt.reset();
        switch (b.getWorld().getEnvironment()) {
        case NETHER:
            for (int i = 0; chestItemsNether.size() > 0 && i < addon.getSettings().getChestFillNether() && i < 27; i ++) {
                ItemStack item = new ItemStack(this.chestItemsNether.get(random.nextInt(chestItemsNether.size())));
                inv.setItem(slt.next(), item);
            }
            break;
        case THE_END:
            for (int i = 0; chestItemsEnd.size() > 0 && i < addon.getSettings().getChestFillEnd() && i < 27; i ++) {
                ItemStack item = new ItemStack(this.chestItemsEnd.get(random.nextInt(chestItemsEnd.size())));
                inv.setItem(slt.next(), item);
            }
            break;
        default:
            for (int i = 0; chestItemsWorld.size() > 0 && i < addon.getSettings().getChestFill() && i < 27; i ++) {
                ItemStack item = new ItemStack(this.chestItemsWorld.get(random.nextInt(chestItemsWorld.size())));
                inv.setItem(slt.next(), item);
            }
            break;
        }
    }

}