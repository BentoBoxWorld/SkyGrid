package world.bentobox.skygrid.generators;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import java.util.NavigableMap;
import java.util.Random;

import org.bukkit.World.Environment;
import org.bukkit.entity.EntityType;
import org.junit.Before;
import org.junit.Test;

import world.bentobox.skygrid.Settings;
import world.bentobox.skygrid.SkyGrid;

/**
 * Tests for configurable spawner mobs (issue #12).
 */
public class WorldStylesTest {

    private Settings settings;
    private SkyGrid addon;

    @Before
    public void setUp() {
        settings = new Settings();
        addon = mock(SkyGrid.class);
        when(addon.getSettings()).thenReturn(settings);
    }

    @Test
    public void testEmptyConfigUsesAndSavesDefaults() {
        WorldStyles ws = new WorldStyles(addon);
        assertEquals(WorldStyles.defaultNormalSpawns(), settings.getSpawners());
        assertEquals(WorldStyles.defaultNetherSpawns(), settings.getNetherSpawners());
        assertEquals(WorldStyles.defaultEndSpawns(), settings.getEndSpawners());
        assertEquals(WorldStyles.defaultNormalSpawns().size(), ws.get(Environment.NORMAL).getSpawns().size());
        assertEquals(13, ws.get(Environment.NETHER).getSpawns().size());
        assertEquals(3, ws.get(Environment.THE_END).getSpawns().size());
    }

    @Test
    public void testConfiguredListIsUsed() {
        Map<EntityType, Integer> m = new EnumMap<>(EntityType.class);
        m.put(EntityType.COW, 10);
        m.put(EntityType.VILLAGER, 0); // disabled
        settings.setSpawners(m);
        WorldStyles ws = new WorldStyles(addon);
        NavigableMap<Integer, EntityType> spawns = ws.get(Environment.NORMAL).getSpawns();
        assertEquals(1, spawns.size());
        assertEquals(EntityType.COW, spawns.firstEntry().getValue());
    }

    @Test
    public void testUnusableMobsAreSkipped() {
        Map<EntityType, Integer> m = new HashMap<>();
        m.put(EntityType.PLAYER, 10);
        m.put(EntityType.ARROW, 10);
        m.put(EntityType.ZOMBIE, 10);
        settings.setSpawners(m);
        WorldStyles ws = new WorldStyles(addon);
        NavigableMap<Integer, EntityType> spawns = ws.get(Environment.NORMAL).getSpawns();
        assertEquals(1, spawns.size());
        assertEquals(EntityType.ZOMBIE, spawns.firstEntry().getValue());
    }

    @Test
    public void testAllInvalidFallsBackToDefaults() {
        Map<EntityType, Integer> m = new HashMap<>();
        m.put(EntityType.ARROW, 10);
        settings.setSpawners(m);
        WorldStyles ws = new WorldStyles(addon);
        assertEquals(WorldStyles.defaultNormalSpawns().size(), ws.get(Environment.NORMAL).getSpawns().size());
        verify(addon).logWarning(org.mockito.ArgumentMatchers.contains("No valid spawner mobs"));
    }

    @Test
    public void testWeightsGiveProportionalPicks() {
        Map<EntityType, Integer> m = new EnumMap<>(EntityType.class);
        m.put(EntityType.COW, 1);
        m.put(EntityType.PIG, 3);
        settings.setSpawners(m);
        NavigableMap<Integer, EntityType> spawns = new WorldStyles(addon).get(Environment.NORMAL).getSpawns();
        // Every value from nextInt maps to an entry, and counts match the weights exactly
        int cows = 0;
        int pigs = 0;
        for (int r = 0; r < spawns.lastKey(); r++) {
            EntityType t = spawns.higherEntry(r).getValue();
            if (t == EntityType.COW) cows++;
            else if (t == EntityType.PIG) pigs++;
        }
        assertEquals(1, cows);
        assertEquals(3, pigs);
        // Random picks never fall off the end
        Random rand = new Random(1);
        for (int i = 0; i < 1000; i++) {
            assertTrue(spawns.higherEntry(rand.nextInt(spawns.lastKey())) != null);
        }
    }

    @Test
    public void testDefaultsAreSpawnable() {
        WorldStyles.defaultNormalSpawns().keySet().forEach(t -> assertTrue(t + " spawnable", t.isSpawnable() && t.isAlive()));
        WorldStyles.defaultNetherSpawns().keySet().forEach(t -> assertTrue(t + " spawnable", t.isSpawnable() && t.isAlive()));
        WorldStyles.defaultEndSpawns().keySet().forEach(t -> assertTrue(t + " spawnable", t.isSpawnable() && t.isAlive()));
        assertFalse(WorldStyles.defaultNormalSpawns().containsKey(EntityType.BAT));
    }
}
