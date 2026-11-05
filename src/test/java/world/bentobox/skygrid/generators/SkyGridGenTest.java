package world.bentobox.skygrid.generators;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.Tag;
import org.bukkit.World.Environment;
import org.bukkit.block.data.BlockData;
import org.bukkit.generator.ChunkGenerator.ChunkData;
import org.bukkit.generator.WorldInfo;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import world.bentobox.skygrid.Settings;
import world.bentobox.skygrid.SkyGrid;

/**
 * Tests that the generator builds the grid from the configured blocks only.
 */
public class SkyGridGenTest {

    private static final int HEIGHT = 128;
    private static final List<Material> CONFIGURED = List.of(Material.GLASS, Material.GOLD_BLOCK,
            Material.IRON_BLOCK, Material.DIAMOND_BLOCK, Material.EMERALD_BLOCK);

    private SkyGridGen gen;
    private WorldInfo worldInfo;
    private BlockProbability prob;
    private WorldStyles worldStyles;

    @BeforeClass
    @SuppressWarnings("unchecked")
    public static void setUpServer() {
        // Tag constants are looked up from the server when the Tag class initializes
        if (Bukkit.getServer() == null) {
            Server server = mock(Server.class);
            when(server.getLogger()).thenReturn(Logger.getAnonymousLogger());
            when(server.getTag(any(), any(), any())).thenReturn(mock(Tag.class));
            Bukkit.setServer(server);
        }
    }

    @Before
    public void setUp() {
        Settings settings = new Settings();
        settings.setIslandHeight(HEIGHT);

        prob = new BlockProbability();
        CONFIGURED.forEach(m -> prob.addBlock(m, 10));
        worldStyles = mock(WorldStyles.class);
        when(worldStyles.get(any())).thenReturn(worldStyles);
        when(worldStyles.getProb()).thenReturn(prob);

        SkyGrid addon = mock(SkyGrid.class);
        when(addon.getSettings()).thenReturn(settings);
        when(addon.getWorldStyles()).thenReturn(worldStyles);

        worldInfo = mock(WorldInfo.class);
        when(worldInfo.getSeed()).thenReturn(12345L);
        when(worldInfo.getEnvironment()).thenReturn(Environment.NORMAL);
        when(worldInfo.getMinHeight()).thenReturn(-64);
        when(worldInfo.getMaxHeight()).thenReturn(320);

        gen = new SkyGridGen(addon);
    }

    /**
     * Generates a chunk where every location already contains the given material.
     * @return the grid blocks that were written, in position order
     */
    private Map<String, Material> generate(long chunkSeed, Material existing) {
        Map<String, Material> grid = new TreeMap<>();
        generateAll(chunkSeed, existing).forEach((key, material) -> {
            int[] pos = parse(key);
            if (pos[0] % 4 == 0 && pos[2] % 4 == 0 && Math.floorMod(pos[1], 4) == 0) {
                grid.put(key, material);
            }
        });
        return grid;
    }

    /**
     * @return the final material written to every location, including those outside the grid
     */
    private Map<String, Material> generateAll(long chunkSeed, Material existing) {
        Map<String, Material> written = new TreeMap<>();
        ChunkData data = mock(ChunkData.class);
        BlockData blockData = mock(BlockData.class);
        lenient().when(blockData.getMaterial()).thenReturn(existing);
        lenient().when(data.getBlockData(anyInt(), anyInt(), anyInt())).thenReturn(blockData);
        doAnswer(inv -> {
            written.put(key(inv.getArgument(0), inv.getArgument(1), inv.getArgument(2)), inv.getArgument(3));
            return null;
        }).when(data).setBlock(anyInt(), anyInt(), anyInt(), any(Material.class));
        gen.generateNoise(worldInfo, new Random(chunkSeed), 0, 0, data);
        return written;
    }

    private static String key(int x, int y, int z) {
        return x + "," + y + "," + z;
    }

    private static int[] parse(String key) {
        return Arrays.stream(key.split(",")).mapToInt(Integer::parseInt).toArray();
    }

    /**
     * Makes the given materials the only ones that can generate
     */
    private void onlyGenerate(Material... materials) {
        prob = new BlockProbability();
        Arrays.stream(materials).forEach(m -> prob.addBlock(m, 10));
        when(worldStyles.getProb()).thenReturn(prob);
    }

    @Test
    public void testVanillaTerrainGenerationIsOff() {
        assertFalse(gen.shouldGenerateNoise());
        assertFalse(gen.shouldGenerateSurface());
        assertFalse(gen.shouldGenerateCaves());
    }

    @Test
    public void testChunksWithDifferentRandomsAreDifferent() {
        assertNotEquals(generate(1L, Material.AIR), generate(2L, Material.AIR));
    }

    @Test
    public void testSameRandomGivesSameChunk() {
        assertEquals(generate(1L, Material.AIR), generate(1L, Material.AIR));
    }

    @Test
    public void testEveryGridLocationGetsAConfiguredBlock() {
        Map<String, Material> written = generate(1L, Material.STONE);
        // 4 x 4 columns, with a block every 4 from -64 to 128 inclusive
        assertEquals(4 * 4 * 49, written.size());
        assertTrue(CONFIGURED.containsAll(written.values()));
    }

    @Test
    public void testSugarCaneStandsOnSandAndSandstone() {
        onlyGenerate(Material.SUGAR_CANE);
        Map<String, Material> all = generateAll(1L, Material.AIR);
        assertEquals(Material.SANDSTONE, all.get(key(4, 0, 4)));
        assertEquals(Material.SAND, all.get(key(4, 1, 4)));
        assertEquals(Material.SUGAR_CANE, all.get(key(4, 2, 4)));
    }

    @Test
    public void testSugarCaneHasWaterNextToItsSand() {
        onlyGenerate(Material.SUGAR_CANE);
        Map<String, Material> all = generateAll(1L, Material.AIR);
        int[][] sides = { { 1, 0 }, { -1, 0 }, { 0, 1 }, { 0, -1 } };
        all.forEach((key, material) -> {
            if (material == Material.SUGAR_CANE) {
                int[] pos = parse(key);
                boolean water = Arrays.stream(sides).anyMatch(side -> all
                        .get(key(pos[0] + side[0], pos[1] - 1, pos[2] + side[1])) == Material.WATER);
                assertTrue("No water next to sugar cane at " + key, water);
            }
        });
    }

    @Test
    public void testWaterCannotFlow() {
        onlyGenerate(Material.SUGAR_CANE);
        Map<String, Material> all = generateAll(1L, Material.AIR);
        int[][] neighbours = { { 1, 0, 0 }, { -1, 0, 0 }, { 0, 0, 1 }, { 0, 0, -1 }, { 0, -1, 0 } };
        long water = all.entrySet().stream().filter(e -> e.getValue() == Material.WATER).peek(e -> {
            int[] pos = parse(e.getKey());
            for (int[] n : neighbours) {
                String neighbour = key(pos[0] + n[0], pos[1] + n[1], pos[2] + n[2]);
                Material m = all.get(neighbour);
                assertTrue("Water at " + e.getKey() + " can flow to " + neighbour + " (" + m + ")",
                        m == Material.SAND || m == Material.SANDSTONE);
            }
        }).count();
        // Every column has a basin in each of the 49 layers, except for the corner column
        assertEquals(15 * 49, water);
    }

    @Test
    public void testNothingIsWrittenOutsideTheChunk() {
        onlyGenerate(Material.SUGAR_CANE);
        generateAll(1L, Material.AIR).keySet().forEach(key -> {
            int[] pos = parse(key);
            assertTrue(key, pos[0] >= 0 && pos[0] < 16 && pos[2] >= 0 && pos[2] < 16);
        });
    }

    @Test
    public void testCornerColumnHasNoSugarCane() {
        onlyGenerate(Material.SUGAR_CANE);
        Map<String, Material> all = generateAll(1L, Material.AIR);
        assertEquals(Material.SANDSTONE, all.get(key(0, 0, 0)));
        assertEquals(Material.AIR, all.get(key(0, 2, 0)));
    }

    @Test
    public void testCactusAndDeadBushStandOnSandAndSandstone() {
        onlyGenerate(Material.CACTUS, Material.DEAD_BUSH, Material.GLASS);
        Map<String, Material> all = generateAll(1L, Material.AIR);
        List<Material> plants = List.of(Material.CACTUS, Material.DEAD_BUSH);
        assertTrue(all.values().stream().anyMatch(plants::contains));
        all.forEach((key, material) -> {
            if (plants.contains(material)) {
                int[] pos = parse(key);
                assertEquals(key, Material.SAND, all.get(key(pos[0], pos[1] - 1, pos[2])));
                assertEquals(key, Material.SANDSTONE, all.get(key(pos[0], pos[1] - 2, pos[2])));
            }
        });
    }
}
