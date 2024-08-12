package world.bentobox.skygrid.generators;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NavigableMap;
import java.util.Random;
import java.util.TreeMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.block.Biome;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Chest;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.block.data.type.EndPortalFrame;
import org.bukkit.entity.EntityType;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.generator.LimitedRegion;
import org.bukkit.generator.WorldInfo;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import world.bentobox.skygrid.SkyGrid;


public class SkyGridPop extends BlockPopulator {
    private static final RandomSeries slt = new RandomSeries(27);
    private final int islandHeight;
    private final SkyGrid addon;
    private final NavigableMap<Integer, Material> chestItemsWorld = new TreeMap<>();
    private final NavigableMap<Integer, Material> chestItemsNether = new TreeMap<>();
    private final NavigableMap<Integer, Material> chestItemsEnd = new TreeMap<>();
    private int worldTotal;
    private int netherTotal;
    private int endTotal;
    private Map<UUID, Random> rand = new HashMap<>();
    private Random random = new Random();

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
        this.islandHeight = addon.getSettings().getIslandHeight();
        // Load the chest items
        for (Entry<Material, Integer> en : addon.getSettings().getChestItemsOverworld().entrySet()) {
            if (en.getKey().isItem()) {
                worldTotal += en.getValue();
                chestItemsWorld.put(worldTotal, en.getKey());
            } else {
                addon.logWarning(en.getKey() + " is not an item so cannot go in a chest.");
            }
        }
        for (Entry<Material, Integer> en : addon.getSettings().getChestItemsNether().entrySet()) {
            if (en.getKey().isItem()) {
                netherTotal += en.getValue();
                chestItemsNether.put(netherTotal, en.getKey());
            } else {
                addon.logWarning(en.getKey() + " is not an item so cannot go in a chest.");
            }
        }
        for (Entry<Material, Integer> en : addon.getSettings().getChestItemsEnd().entrySet()) {
            if (en.getKey().isItem()) {
                endTotal += en.getValue();
                chestItemsEnd.put(endTotal, en.getKey());
            } else {
                addon.logWarning(en.getKey() + " is not an item so cannot go in a chest.");
            }
        }
        addon.log(LOADED + chestItemsWorld.size() + " chest items for world");
        addon.log(LOADED + chestItemsNether.size() + " chest items for nether world");
        addon.log(LOADED + chestItemsEnd.size() + " chest items for end world");

    }

    private Location getLoc(World world, int x, int y, int z, int chunkX, int chunkZ) {
        Vector v = new Vector(x, y, z);
        return v.add(new Vector(chunkX << 4, 0, chunkZ << 4)).toLocation(world);
    }

    public void populate(WorldInfo worldInfo, Random random, int chunkX, int chunkZ, LimitedRegion region) {
        random = rand.computeIfAbsent(worldInfo.getUID(), (b) -> new Random(worldInfo.getSeed()));
        World world = Bukkit.getWorld(worldInfo.getUID());
        for (int x = 0; x < 16; x += 4) {
            for (int z = 0; z < 16; z += 4) {
                for (int y = worldInfo.getMinHeight(); y <= islandHeight; y += 4) {
                    Location loc = getLoc(world, x, y, z, chunkX, chunkZ);
                    alterBlocks(region, loc, worldInfo.getEnvironment());
                }
            }
        }
        // Do an end portal check
        if (addon.getSettings().isEndGenerate() && worldInfo.getEnvironment().equals(Environment.NORMAL)
                && random.nextDouble() < addon.getSettings().getEndFrameProb()) {
            makeEndPortal(region, chunkX, chunkZ);
        }

    }

    private void alterBlocks(LimitedRegion region, Location loc, Environment environment) {
        // Alter blocks
        Material m = region.getBlockData(loc).getMaterial();

        switch (m) {
        case CHEST:
            setChest(region, loc, environment);
            break;
        case SPAWNER:
            setSpawner(region, loc, environment);
            break;
        case DIRT:
            if (Tag.SAPLINGS.isTagged(m)) {
                if (region.getBiome(loc).equals(Biome.DESERT)) {
                    region.setType(loc, Material.SAND);
                } else {
                    setSaplingType(region, loc);
                }
            }
            break;
        default:
            break;
        }

    }

    private void makeEndPortal(LimitedRegion region, int chunkX, int chunkZ) {
        for (int xx = 1; xx< 6; xx++) {
            for (int zz = 1; zz < 6; zz++) {
                if (xx == zz || (xx==1 && zz==5) || (xx==5 && zz==1) || (xx>1 && xx<5 && zz>1 && zz<5)) {
                    continue;
                }
                setFrame(region, xx + (chunkX << 4), addon.getSettings().getEndFrameHeight(), zz + (chunkZ << 4));
            }
        }
    }

    private void setFrame(LimitedRegion region, int xx, int yy, int zz) {
        region.setType(xx, yy, zz, Material.END_PORTAL_FRAME);
        // Cast to end frame
        EndPortalFrame endFrame = (EndPortalFrame) region.getBlockData(xx, yy, zz);

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
        region.setBlockData(xx, yy, zz, endFrame);
    }

    private void setSaplingType(LimitedRegion region, Location loc) {
        // Set sapling type if there is one specific to this biome
        Material sapling = switch (region.getBiome(loc)) {
        case JUNGLE -> Material.JUNGLE_SAPLING;
        case PLAINS -> random.nextBoolean() ? Material.BIRCH_SAPLING : Material.OAK_SAPLING;
        case TAIGA -> Material.SPRUCE_SAPLING;
        case SWAMP -> Material.MANGROVE_PROPAGULE;
        case DESERT -> Material.DEAD_BUSH;
        case BADLANDS -> Material.DEAD_BUSH;
        case CHERRY_GROVE -> Material.CHERRY_SAPLING;
        case BAMBOO_JUNGLE -> Material.BAMBOO;
        case SAVANNA -> Material.ACACIA_SAPLING; // Acacia
        case BIRCH_FOREST -> Material.BIRCH_SAPLING;
        case MUSHROOM_FIELDS -> random.nextBoolean() ? Material.RED_MUSHROOM : Material.BROWN_MUSHROOM;
        default -> SAPLING_TYPE[random.nextInt(6)];
        };
        region.setType(loc.add(new Vector(0, 1, 0)), sapling);
    }

    private void setSpawner(LimitedRegion region, Location loc, Environment environment) {
        CreatureSpawner spawner = (CreatureSpawner) region.getBlockState(loc);
        NavigableMap<Integer, EntityType> spawns = addon.getWorldStyles().get(environment).getSpawns();
        int randKey = random.nextInt(spawns.lastKey());
        EntityType type = spawns.ceilingEntry(randKey).getValue();
        spawner.setDelay(120);
        spawner.setSpawnedType(type);
        spawner.update(true, false);
    }

    private void setChest(LimitedRegion region, Location loc, Environment environment) {
        Chest chest = (Chest) region.getBlockState(loc);
        Inventory inv = chest.getBlockInventory();
        slt.reset();
        switch (environment) {
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