package world.bentobox.skygrid.generators;

import java.util.Map.Entry;
import java.util.NavigableMap;
import java.util.Random;
import java.util.TreeMap;

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
    private final SkyGrid addon;
    private final NavigableMap<Integer, Material> chestItemsWorld = new TreeMap<>();
    private final NavigableMap<Integer, Material> chestItemsNether = new TreeMap<>();
    private final NavigableMap<Integer, Material> chestItemsEnd = new TreeMap<>();
    private int worldTotal;
    private int netherTotal;
    private int endTotal;
    private Random random;
    private Chunk chunk;

    private static final Material[] SAPLING_TYPE = {
            Material.ACACIA_SAPLING,
            Material.BIRCH_SAPLING,
            Material.DARK_OAK_SAPLING,
            Material.JUNGLE_SAPLING,
            Material.OAK_SAPLING,
            Material.SPRUCE_SAPLING
    };

    private static final String LOADED = "Loaded ";

    public SkyGridPop(SkyGrid addon) {
        this.addon = addon;
        this.size = addon.getSettings().getIslandHeight();
        // Load the chest items
        for (Entry<Material, Integer> en : addon.getSettings().getChestItemsOverworld().entrySet()) {
            worldTotal += en.getValue();
            chestItemsWorld.put(worldTotal, en.getKey());
        }
        for (Entry<Material, Integer> en : addon.getSettings().getChestItemsNether().entrySet()) {
            netherTotal += en.getValue();
            chestItemsNether.put(netherTotal, en.getKey());
        }
        for (Entry<Material, Integer> en : addon.getSettings().getChestItemsEnd().entrySet()) {
            endTotal += en.getValue();
            chestItemsEnd.put(endTotal, en.getKey());
        }
        addon.log(LOADED + chestItemsWorld.size() + " chest items for world");
        addon.log(LOADED + chestItemsNether.size() + " chest items for nether world");
        addon.log(LOADED + chestItemsEnd.size() + " chest items for end world");

    }

    @Override
    @Deprecated
    public void populate(World world, Random random, Chunk chunk) {
        this.random = random;
        this.chunk = chunk;
        for (int x = 1; x < 16; x += 4) {
            for (int z = 1; z < 16; z +=4) {
                for (int y = 0; y <= size; y += 4) {
                    alterBlocks(chunk.getBlock(x, y, z));
                }
            }
        }
        // Do an end portal check
        if (addon.getSettings().isEndGenerate() && world.getEnvironment().equals(Environment.NORMAL)
                && random.nextDouble() < addon.getSettings().getEndFrameProb()) {
            makeEndPortal();
        }
    }

    private void alterBlocks(Block b) {
        // Alter blocks
        switch (b.getType()) {
        case CHEST:
            setChest(b);
            break;
        case SPAWNER:
            setSpawner(b);
            break;
        case DIRT:
            if (b.getRelative(BlockFace.UP).getBlockData() instanceof Sapling) {
                if (b.getBiome().equals(Biome.DESERT)) {
                    b.setType(Material.SAND, false);
                } else {
                    setSaplingType(b.getRelative(BlockFace.UP));
                }
            }
            break;
        default:
            break;
        }

    }

    private void makeEndPortal() {
        for (int xx = 1; xx< 6; xx++) {
            for (int zz = 1; zz < 6; zz++) {
                if (xx == zz || (xx==1 && zz==5) || (xx==5 && zz==1) || (xx>1 && xx<5 && zz>1 && zz<5)) {
                    continue;
                }
                setFrame(xx, zz, chunk.getBlock(xx, 0, zz));
            }
        }
    }

    private void setFrame(int xx, int zz, Block frame) {
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

    private void setSaplingType(Block b) {
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

    private void setSpawner(Block b) {
        CreatureSpawner spawner = (CreatureSpawner) b.getState();
        NavigableMap<Integer,EntityType> spawns = addon.getWorldStyles().get(b.getWorld().getEnvironment()).getSpawns();
        int randKey = random.nextInt(spawns.lastKey());
        EntityType type = spawns.ceilingEntry(randKey).getValue();
        spawner.setDelay(120);
        spawner.setSpawnedType(type);
        spawner.update(true, false);
    }

    private void setChest(Block b) {
        Chest chest = (Chest) b.getState();
        Inventory inv = chest.getBlockInventory();
        slt.reset();
        switch (b.getWorld().getEnvironment()) {
        case NETHER -> fillChest(inv, chestItemsNether, addon.getSettings().getChestFillNether(), netherTotal);
        case THE_END -> fillChest(inv, chestItemsEnd, addon.getSettings().getChestFillEnd(), endTotal);
        default -> fillChest(inv, chestItemsWorld, addon.getSettings().getChestFill(), worldTotal);
        }
    }

    private void fillChest(Inventory inv, NavigableMap<Integer, Material> probMap, int chestFill,
            int total) {
        for (int i = 0; !probMap.isEmpty() && i < chestFill && i < 27; i ++) {
            Material temp = probMap.get(random.nextInt(total));
            if (temp == null) {
                temp = probMap.ceilingEntry(random.nextInt(total)).getValue();
            }
            if (temp == null) {
                temp = probMap.firstEntry().getValue();
            }
            inv.setItem(slt.next(), new ItemStack(temp));
        }

    }

}