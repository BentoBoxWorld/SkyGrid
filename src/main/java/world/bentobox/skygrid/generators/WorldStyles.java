package world.bentobox.skygrid.generators;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;

import org.bukkit.Material;
import org.bukkit.World.Environment;
import org.bukkit.entity.EntityType;

import com.google.common.base.Enums;

import world.bentobox.skygrid.SkyGrid;


/**
 * Provides different results for different world types: normal, nether, etc.
 *
 */
public class WorldStyles {
    private static final Map<Environment, WorldStyles> map = new EnumMap<>(Environment.class);

    private static final String LOADED = "Loaded ";

    private SkyGrid addon;

    private BlockProbability prob;
    private NavigableMap<Integer,EntityType> spawns;

    public WorldStyles(SkyGrid addon) {
        this.addon = addon;
        map.put(Environment.NORMAL, new WorldStyles(normalWorldProbabilities(), normalSpawns()));
        map.put(Environment.NETHER, new WorldStyles(netherWorldProbabilities(), netherSpawns()));
        map.put(Environment.THE_END, new WorldStyles(endWorldProbabilities(), endSpawns()));
    }

    public WorldStyles(BlockProbability prob, NavigableMap<Integer, EntityType> spawns) {
        this.prob = prob;
        this.spawns = spawns;
    }

    public WorldStyles get(Environment style) {
        return map.get(style);
    }

    /**
     * @return the block probability
     */
    public BlockProbability getProb() {
        return prob;
    }

    /**
     * @return the spawns
     */
    public NavigableMap<Integer,EntityType> getSpawns() {
        return spawns;
    }

    /**
     * Set up the block probabilities for the normal world
     * @return Block Probabilities
     */
    private BlockProbability normalWorldProbabilities() {
        BlockProbability blockProbability = new BlockProbability();
        addon.getSettings().getBlocks().forEach(blockProbability::addBlock);
        addon.log(LOADED + blockProbability.getSize() + " block types for SkyGrid over world");
        if (blockProbability.isEmpty()) {
            blockProbability.addBlock(Material.STONE, 100);
            addon.logError("Using default stone as only block (fix/update config.yml)");
        }
        return blockProbability;
    }

    /**
     * Nether world probabilities
     * @return Block probability
     */
    private BlockProbability netherWorldProbabilities() {
        BlockProbability blockProbability = new BlockProbability();
        addon.getSettings().getNetherBlocks().forEach(blockProbability::addBlock);
        addon.log(LOADED + blockProbability.getSize() + " block types for SkyGrid nether");
        if (blockProbability.isEmpty()) {
            blockProbability.addBlock(Material.NETHERRACK, 100);
            blockProbability.addBlock(Material.LAVA, 300);
            blockProbability.addBlock(Material.GRAVEL, 30);
            blockProbability.addBlock(Material.SPAWNER, 2);
            blockProbability.addBlock(Material.CHEST, 1);
            blockProbability.addBlock(Material.SOUL_SAND, 100);
            blockProbability.addBlock(Material.GLOWSTONE, 1);
            blockProbability.addBlock(Material.NETHER_BRICK, 30);
            blockProbability.addBlock(Material.NETHER_BRICK_FENCE, 10);
            blockProbability.addBlock(Material.NETHER_BRICK_STAIRS,15);
            blockProbability.addBlock(Material.NETHER_WART_BLOCK, 30);
            blockProbability.addBlock(Material.NETHER_QUARTZ_ORE, 15);
            addon.logWarning("Using default nether blocks (fix/update config.yml)");
        }
        return blockProbability;
    }

    /**
     * End world probabilities
     * @return block probability
     */
    private BlockProbability endWorldProbabilities() {
        BlockProbability blockProbability = new BlockProbability();
        addon.getSettings().getEndBlocks().forEach(blockProbability::addBlock);
        addon.log(LOADED + blockProbability.getSize() + " block types for SkyGrid end world");
        if (blockProbability.isEmpty()) {
            blockProbability.addBlock(Material.END_STONE, 300);
            blockProbability.addBlock(Material.OBSIDIAN, 10);
            blockProbability.addBlock(Material.SPAWNER, 2);
            blockProbability.addBlock(Material.CHEST, 1);
            addon.logWarning("Using default end settings for blocks (fix/update config.yml)");
        }
        return blockProbability;
    }

    /**
     * What will come out of spawners
     * @return map of overworld spawners
     */
    private TreeMap<Integer,EntityType> normalSpawns() {
        TreeMap<Integer,EntityType> s = new TreeMap<>();
        List<EntityType> types = new ArrayList<>();
        types.add(EntityType.CREEPER);
        types.add(EntityType.SKELETON);
        types.add(EntityType.SPIDER);
        types.add(EntityType.CAVE_SPIDER);
        types.add(EntityType.ZOMBIE);
        types.add(EntityType.SLIME);
        types.add(EntityType.PIG);
        types.add(EntityType.SHEEP);
        types.add(EntityType.COW);
        types.add(EntityType.CHICKEN);
        types.add(EntityType.SQUID);
        types.add(EntityType.WOLF);
        types.add(EntityType.ENDERMAN);
        types.add(EntityType.SILVERFISH);
        types.add(EntityType.VILLAGER);
        types.add(EntityType.RABBIT);
        types.add(EntityType.GUARDIAN);
        types.add(EntityType.HORSE);
        types.add(EntityType.WITCH);
        types.add(EntityType.LLAMA);
        types.add(EntityType.POLAR_BEAR);
        types.add(EntityType.HUSK);
        types.add(EntityType.MULE);
        types.add(EntityType.MUSHROOM_COW);
        types.add(EntityType.OCELOT);
        types.add(EntityType.VINDICATOR);
        types.add(EntityType.ZOMBIE_HORSE);
        types.add(EntityType.ZOMBIE_VILLAGER);
        types.add(EntityType.BEE);
        types.add(EntityType.CAT);
        types.add(EntityType.COD);
        types.add(EntityType.DOLPHIN);
        types.add(EntityType.DONKEY);
        types.add(EntityType.DROWNED);
        types.add(EntityType.ELDER_GUARDIAN);
        types.add(EntityType.FOX);
        types.add(EntityType.HUSK);
        types.add(EntityType.PANDA);
        types.add(EntityType.PILLAGER);
        types.add(EntityType.RAVAGER);
        types.add(EntityType.TURTLE);
        types.add(EntityType.VEX);

        int step = 10000 / types.size();
        int i = step;
        for (EntityType type: types) {
            s.put(i, type);
            i += step;
        }
        return s;
    }

    /**
     * What will come out of spawners in the nether
     * @return map of nether spawners
     */
    private TreeMap<Integer,EntityType> netherSpawns() {
        TreeMap<Integer,EntityType> s = new TreeMap<>();
        // Keys should increment and cannot be the same!
        s.put(25,  EntityType.BLAZE);
        s.put(50,  EntityType.MAGMA_CUBE);
        s.put(75,  EntityType.SKELETON);
        s.put(100,  EntityType.WITHER_SKELETON);
        s.put(150,  EntityType.SKELETON_HORSE);
        s.put(175,  EntityType.ENDERMAN);
        if (Enums.getIfPresent(EntityType.class, "ZOMBIFIED_PIGLIN").isPresent()) {
            s.put(300,  EntityType.ZOMBIFIED_PIGLIN);
            s.put(350,  EntityType.STRIDER);
            s.put(475, EntityType.PIGLIN);
            s.put(600,  EntityType.ZOGLIN);
            s.put(725,  EntityType.HOGLIN);
        }
        return s;
    }

    /**
     * What will come out of spawners in the end
     * @return map of end spawners
     */
    private TreeMap<Integer,EntityType> endSpawns() {
        TreeMap<Integer,EntityType> s = new TreeMap<>();
        s.put(50, EntityType.ENDERMAN);
        s.put(55, EntityType.ENDERMITE);
        s.put(65, EntityType.SHULKER);
        return s;
    }

}