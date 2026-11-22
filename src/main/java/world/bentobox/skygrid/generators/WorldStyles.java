package world.bentobox.skygrid.generators;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;

import org.bukkit.Material;
import org.bukkit.World.Environment;
import org.bukkit.entity.EntityType;

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
        map.put(Environment.NORMAL, new WorldStyles(normalWorldProbabilities(),
                spawns(addon.getSettings().getSpawners(), defaultNormalSpawns(), "over world")));
        map.put(Environment.NETHER, new WorldStyles(netherWorldProbabilities(),
                spawns(addon.getSettings().getNetherSpawners(), defaultNetherSpawns(), "nether")));
        map.put(Environment.THE_END, new WorldStyles(endWorldProbabilities(),
                spawns(addon.getSettings().getEndSpawners(), defaultEndSpawns(), "end world")));
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
     * Build the spawner table for an environment from config. If the config list is
     * empty, the built-in defaults are put into the settings so they get saved to config.yml.
     * @param configured spawner weights from config.yml
     * @param defaults built-in default weights
     * @param name name of the world for logging
     * @return cumulative probability map for use with {@link NavigableMap#higherEntry}
     */
    private TreeMap<Integer, EntityType> spawns(Map<EntityType, Integer> configured, Map<EntityType, Integer> defaults, String name) {
        if (configured.isEmpty()) {
            configured.putAll(defaults);
        }
        TreeMap<Integer, EntityType> s = toCumulative(configured, name);
        if (s.isEmpty()) {
            addon.logWarning("No valid spawner mobs for SkyGrid " + name + " - using defaults (fix/update config.yml)");
            s = toCumulative(defaults, name);
        }
        addon.log(LOADED + s.size() + " spawner mob types for SkyGrid " + name);
        return s;
    }

    /**
     * Turn relative weights into a cumulative map. Each key is the running total of
     * weights, so picking {@code higherEntry(random.nextInt(lastKey()))} chooses each
     * mob in proportion to its weight.
     * @param weights mob weights
     * @param name name of the world for logging
     * @return cumulative map, empty if no usable entries
     */
    TreeMap<Integer, EntityType> toCumulative(Map<EntityType, Integer> weights, String name) {
        TreeMap<Integer, EntityType> s = new TreeMap<>();
        int total = 0;
        for (Map.Entry<EntityType, Integer> en : weights.entrySet()) {
            EntityType type = en.getKey();
            Integer weight = en.getValue();
            if (type == null || weight == null || weight <= 0) {
                continue;
            }
            if (!type.isSpawnable() || !type.isAlive()) {
                addon.logWarning(type + " in the SkyGrid " + name + " spawners list cannot be used in a spawner - skipping");
                continue;
            }
            total += weight;
            s.put(total, type);
        }
        return s;
    }

    /**
     * Default over world spawner mobs
     * @return map of mob to weight
     */
    public static Map<EntityType, Integer> defaultNormalSpawns() {
        Map<EntityType, Integer> m = new EnumMap<>(EntityType.class);
        List<EntityType> types = List.of(EntityType.ALLAY, EntityType.ARMADILLO, EntityType.AXOLOTL, EntityType.BEE,
                EntityType.BOGGED, EntityType.CAMEL, EntityType.CAT, EntityType.CAVE_SPIDER, EntityType.CHICKEN,
                EntityType.COD, EntityType.COW, EntityType.CREEPER, EntityType.DOLPHIN, EntityType.DONKEY,
                EntityType.DROWNED, EntityType.ELDER_GUARDIAN, EntityType.ENDERMAN, EntityType.FOX, EntityType.FROG,
                EntityType.GLOW_SQUID, EntityType.GOAT, EntityType.GUARDIAN, EntityType.HORSE, EntityType.HUSK,
                EntityType.ILLUSIONER, EntityType.IRON_GOLEM, EntityType.LLAMA, EntityType.MOOSHROOM, EntityType.MULE,
                EntityType.OCELOT, EntityType.PANDA, EntityType.PARROT, EntityType.PIG, EntityType.PILLAGER,
                EntityType.POLAR_BEAR, EntityType.PUFFERFISH, EntityType.RABBIT, EntityType.RAVAGER, EntityType.SALMON,
                EntityType.SHEEP, EntityType.SILVERFISH, EntityType.SKELETON, EntityType.SLIME, EntityType.SNIFFER,
                EntityType.SPIDER, EntityType.SQUID, EntityType.STRAY, EntityType.TURTLE, EntityType.VEX,
                EntityType.VILLAGER, EntityType.VINDICATOR, EntityType.WITCH, EntityType.WOLF, EntityType.ZOMBIE,
                EntityType.ZOMBIE_HORSE, EntityType.ZOMBIE_VILLAGER);
        types.forEach(t -> m.put(t, 10));
        return m;
    }

    /**
     * Default nether spawner mobs. Weights give about the same odds as before they were configurable.
     * @return map of mob to weight
     */
    public static Map<EntityType, Integer> defaultNetherSpawns() {
        Map<EntityType, Integer> m = new EnumMap<>(EntityType.class);
        m.put(EntityType.BLAZE, 25);
        m.put(EntityType.MAGMA_CUBE, 25);
        m.put(EntityType.SKELETON, 25);
        m.put(EntityType.WITHER_SKELETON, 25);
        m.put(EntityType.SKELETON_HORSE, 50);
        m.put(EntityType.ENDERMAN, 25);
        m.put(EntityType.ZOMBIFIED_PIGLIN, 125);
        m.put(EntityType.STRIDER, 50);
        m.put(EntityType.PIGLIN, 125);
        m.put(EntityType.ZOGLIN, 125);
        m.put(EntityType.HOGLIN, 125);
        m.put(EntityType.GHAST, 75);
        m.put(EntityType.PIGLIN_BRUTE, 50);
        return m;
    }

    /**
     * Default end spawner mobs. Weights give about the same odds as before they were configurable.
     * @return map of mob to weight
     */
    public static Map<EntityType, Integer> defaultEndSpawns() {
        Map<EntityType, Integer> m = new EnumMap<>(EntityType.class);
        m.put(EntityType.SHULKER, 1);
        m.put(EntityType.ENDERMITE, 54);
        m.put(EntityType.ENDERMAN, 45);
        return m;
    }

}
