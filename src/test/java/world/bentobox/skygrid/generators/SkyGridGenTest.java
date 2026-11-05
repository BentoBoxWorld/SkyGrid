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

        BlockProbability prob = new BlockProbability();
        CONFIGURED.forEach(m -> prob.addBlock(m, 10));
        WorldStyles styles = mock(WorldStyles.class);
        when(styles.get(any())).thenReturn(styles);
        when(styles.getProb()).thenReturn(prob);

        SkyGrid addon = mock(SkyGrid.class);
        when(addon.getSettings()).thenReturn(settings);
        when(addon.getWorldStyles()).thenReturn(styles);

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
        Map<String, Material> written = new TreeMap<>();
        ChunkData data = mock(ChunkData.class);
        BlockData blockData = mock(BlockData.class);
        lenient().when(blockData.getMaterial()).thenReturn(existing);
        lenient().when(data.getBlockData(anyInt(), anyInt(), anyInt())).thenReturn(blockData);
        doAnswer(inv -> {
            int x = inv.getArgument(0);
            int y = inv.getArgument(1);
            int z = inv.getArgument(2);
            if (x % 4 == 0 && z % 4 == 0 && Math.floorMod(y, 4) == 0) {
                written.put(x + "," + y + "," + z, inv.getArgument(3));
            }
            return null;
        }).when(data).setBlock(anyInt(), anyInt(), anyInt(), any(Material.class));
        gen.generateNoise(worldInfo, new Random(chunkSeed), 0, 0, data);
        return written;
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
}
