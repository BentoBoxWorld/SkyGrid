package world.bentobox.skygrid;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.bukkit.Difficulty;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.modules.junit4.PowerMockRunner;


/**
 * @author tastybento
 *
 */
@RunWith(PowerMockRunner.class)
public class SettingsTest {

    private Settings s;

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        s = new Settings();
    }

    /**
     * Test method for {@link world.bentobox.skygrid.Settings#getBlocks()}.
     */
    @Test
    public void testGetBlocks() {
        assertTrue(s.getBlocks().isEmpty());
    }

    /**
     * Test method for {@link world.bentobox.skygrid.Settings#setBlocks(java.util.Map)}.
     */
    @Test
    public void testSetBlocks() {
        assertTrue(s.getBlocks().isEmpty());
        s.setBlocks(Map.of(Material.STONE, 5));
        assertFalse(s.getBlocks().isEmpty());
    }

    /**
     * Test method for {@link world.bentobox.skygrid.Settings#getNetherBlocks()}.
     */
    @Test
    public void testGetNetherBlocks() {
        assertTrue(s.getNetherBlocks().isEmpty());
    }

    /**
     * Test method for {@link world.bentobox.skygrid.Settings#setNetherBlocks(java.util.Map)}.
     */
    @Test
    public void testSetNetherBlocks() {
        assertTrue(s.getNetherBlocks().isEmpty());
        s.setNetherBlocks(Map.of(Material.STONE, 5));
        assertFalse(s.getNetherBlocks().isEmpty());
    }

    /**
     * Test method for {@link world.bentobox.skygrid.Settings#getEndBlocks()}.
     */
    @Test
    public void testGetEndBlocks() {
        assertTrue(s.getEndBlocks().isEmpty());
    }

    /**
     * Test method for {@link world.bentobox.skygrid.Settings#setEndBlocks(java.util.Map)}.
     */
    @Test
    public void testSetEndBlocks() {
        assertTrue(s.getEndBlocks().isEmpty());
        s.setEndBlocks(Map.of(Material.STONE, 5));
        assertFalse(s.getEndBlocks().isEmpty());
    }

    /**
     * Test method for {@link world.bentobox.skygrid.Settings#getEndFrameProb()}.
     */
    @Test
    public void testGetEndFrameProb() {
        assertEquals(0.1D, s.getEndFrameProb(), 0D);
    }

    /**
     * Test method for {@link world.bentobox.skygrid.Settings#setEndFrameProb(double)}.
     */
    @Test
    public void testSetEndFrameProb() {
        assertEquals(0.1D, s.getEndFrameProb(), 0D);
        s.setEndFrameProb(20D);
        assertEquals(20D, s.getEndFrameProb(), 0D);
    }

    /**
     * Test method for {@link world.bentobox.skygrid.Settings#getChestFill()}.
     */
    @Test
    public void testGetChestFill() {
        assertEquals(5, s.getChestFill());
    }

    /**
     * Test method for {@link world.bentobox.skygrid.Settings#setChestFill(int)}.
     */
    @Test
    public void testSetChestFill() {
        s.setChestFill(20);
        assertEquals(20, s.getChestFill());
    }

    /**
     * Test method for {@link world.bentobox.skygrid.Settings#getChestFillNether()}.
     */
    @Test
    public void testGetChestFillNether() {
        assertEquals(5, s.getChestFillNether());
    }

    /**
     * Test method for {@link world.bentobox.skygrid.Settings#setChestFillNether(int)}.
     */
    @Test
    public void testSetChestFillNether() {
        s.setChestFillNether(20);
        assertEquals(20, s.getChestFillNether());
    }

    /**
     * Test method for {@link world.bentobox.skygrid.Settings#getChestFillEnd()}.
     */
    @Test
    public void testGetChestFillEnd() {
        assertEquals(5, s.getChestFillEnd());
    }

    /**
     * Test method for {@link world.bentobox.skygrid.Settings#setChestFillEnd(int)}.
     */
    @Test
    public void testSetChestFillEnd() {
        s.setChestFillEnd(20);
        assertEquals(20, s.getChestFillEnd());
    }

    /**
     * Test method for {@link world.bentobox.skygrid.Settings#getChestItemsOverworld()}.
     */
    @Test
    public void testGetChestItemsOverworld() {
        assertTrue(s.getChestItemsOverworld().isEmpty());
    }

    /**
     * Test method for {@link world.bentobox.skygrid.Settings#setChestItemsOverworld(java.util.List)}.
     */
    @Test
    public void testSetChestItemsOverworld() {
        assertTrue(s.getChestItemsOverworld().isEmpty());
        s.setChestItemsOverworld(Map.of(Material.ACACIA_BOAT, 1));
        assertFalse(s.getChestItemsOverworld().isEmpty());
    }

    /**
     * Test method for {@link world.bentobox.skygrid.Settings#getChestItemsNether()}.
     */
    @Test
    public void testGetChestItemsNether() {
        assertTrue(s.getChestItemsNether().isEmpty());
    }

    /**
     * Test method for {@link world.bentobox.skygrid.Settings#setChestItemsNether(java.util.List)}.
     */
    @Test
    public void testSetChestItemsNether() {
        assertTrue(s.getChestItemsNether().isEmpty());
        s.setChestItemsNether(Map.of(Material.ACACIA_BOAT, 1));
        assertFalse(s.getChestItemsNether().isEmpty());
    }

    /**
     * Test method for {@link world.bentobox.skygrid.Settings#getChestItemsEnd()}.
     */
    @Test
    public void testGetChestItemsEnd() {
        assertTrue(s.getChestItemsEnd().isEmpty());
    }

    /**
     * Test method for {@link world.bentobox.skygrid.Settings#setChestItemsEnd(java.util.List)}.
     */
    @Test
    public void testSetChestItemsEnd() {
        assertTrue(s.getChestItemsEnd().isEmpty());
        s.setChestItemsEnd(Map.of(Material.ACACIA_BOAT, 1));
        assertFalse(s.getChestItemsEnd().isEmpty());
    }

    /**
     * Test method for {@link world.bentobox.skygrid.Settings#isCheckForBlocks()}.
     */
    @Test
    public void testIsCheckForBlocks() {
        assertFalse(s.isCheckForBlocks());
    }

    /**
     * Test method for {@link world.bentobox.skygrid.Settings#getFriendlyName()}.
     */
    @Test
    public void testGetFriendlyName() {
        assertEquals("SkyGrid", s.getFriendlyName());
    }

    /**
     * Test method for {@link world.bentobox.skygrid.Settings#getWorldName()}.
     */
    @Test
    public void testGetWorldName() {
        assertEquals("skygrid-world", s.getWorldName());
    }

    /**
     * Test method for {@link world.bentobox.skygrid.Settings#getDifficulty()}.
     */
    @Test
    public void testGetDifficulty() {
        assertEquals(Difficulty.NORMAL, s.getDifficulty());
    }

    /**
     * Test method for {@link world.bentobox.skygrid.Settings#getIslandDistance()}.
     */
    @Test
    public void testGetIslandDistance() {
        assertEquals(1000, s.getIslandDistance());
    }

    /**
     * Test method for {@link world.bentobox.skygrid.Settings#getIslandProtectionRange()}.
     */
    @Test
    public void testGetIslandProtectionRange() {
        assertEquals(50, s.getIslandProtectionRange());
    }

    /**
     * Test method for {@link world.bentobox.skygrid.Settings#getIslandStartX()}.
     */
    @Test
    public void testGetIslandStartX() {
        assertEquals(0, s.getIslandStartX());
    }

    /**
     * Test method for {@link world.bentobox.skygrid.Settings#getIslandStartZ()}.
     */
    @Test
    public void testGetIslandStartZ() {
        assertEquals(0, s.getIslandStartZ());
    }

    /**
     * Test method for {@link world.bentobox.skygrid.Settings#getIslandXOffset()}.
     */
    @Test
    public void testGetIslandXOffset() {
        assertEquals(0, s.getIslandXOffset());
    }

    /**
     * Test method for {@link world.bentobox.skygrid.Settings#getIslandZOffset()}.
     */
    @Test
    public void testGetIslandZOffset() {
        assertEquals(0, s.getIslandZOffset());
    }

    /**
     * Test method for {@link world.bentobox.skygrid.Settings#getIslandHeight()}.
     */
    @Test
    public void testGetIslandHeight() {
        assertEquals(128, s.getIslandHeight());
    }

    /**
     * Test method for {@link world.bentobox.skygrid.Settings#isUseOwnGenerator()}.
     */
    @Test
    public void testIsUseOwnGenerator() {
        assertTrue(s.isUseOwnGenerator());
    }

    /**
     * Test method for {@link world.bentobox.skygrid.Settings#getSeaHeight()}.
     */
    @Test
    public void testGetSeaHeight() {
        assertEquals(0, s.getSeaHeight());
    }

    /**
     * Test method for {@link world.bentobox.skygrid.Settings#getMaxIslands()}.
     */
    @Test
    public void testGetMaxIslands() {
        assertEquals(-1, s.getMaxIslands());
    }

    /**
     * Test method for {@link world.bentobox.skygrid.Settings#getDefaultGameMode()}.
     */
    @Test
    public void testGetDefaultGameMode() {
        assertEquals(GameMode.SURVIVAL, s.getDefaultGameMode());
    }

    /**
     * Test method for {@link world.bentobox.skygrid.Settings#isNetherGenerate()}.
     */
    @Test
    public void testIsNetherGenerate() {
        assertTrue(s.isNetherGenerate());
    }

    /**
     * Test method for {@link world.bentobox.skygrid.Settings#isNetherIslands()}.
     */
    @Test
    public void testIsNetherIslands() {
        assertTrue(s.isNetherIslands());
    }

    /**
     * Test method for {@link world.bentobox.skygrid.Settings#getNetherSpawnRadius()}.
     */
    @Test
    public void testGetNetherSpawnRadius() {
        assertEquals(0, s.getNetherSpawnRadius());
    }

    /**
     * Test method for {@link world.bentobox.skygrid.Settings#isEndGenerate()}.
     */
    @Test
    public void testIsEndGenerate() {
        assertTrue(s.isEndGenerate());
    }

    /**
     * Test method for {@link world.bentobox.skygrid.Settings#isEndIslands()}.
     */
    @Test
    public void testIsEndIslands() {
        assertTrue(s.isEndIslands());
    }

    /**
     * Test method for {@link world.bentobox.skygrid.Settings#isDragonSpawn()}.
     */
    @Test
    public void testIsDragonSpawn() {
        assertFalse(s.isDragonSpawn());
    }

    /**
     * Test method for {@link world.bentobox.skygrid.Settings#getRemoveMobsWhitelist()}.
     */
    @Test
    public void testGetRemoveMobsWhitelist() {
        assertTrue(s.getRemoveMobsWhitelist().isEmpty());

    }

    /**
     * Test method for {@link world.bentobox.skygrid.Settings#getWorldFlags()}.
     */
    @Test
    public void testGetWorldFlags() {
        assertTrue(s.getWorldFlags().isEmpty());
    }

    /**
     * Test method for {@link world.bentobox.skygrid.Settings#getDefaultIslandFlagNames()}.
     */
    @Test
    public void testGetDefaultIslandFlagNames() {
        assertTrue(s.getDefaultIslandFlagNames().isEmpty());
    }

    /**
     * Test method for {@link world.bentobox.skygrid.Settings#getDefaultIslandSettingNames()}.
     */
    @Test
    public void testGetDefaultIslandSettingNames() {
        assertTrue(s.getDefaultIslandSettingNames().isEmpty());
    }

    /**
     * Test method for {@link world.bentobox.skygrid.Settings#getDefaultIslandFlags()}.
     */
    @SuppressWarnings("deprecation")
    @Test
    public void testGetDefaultIslandFlags() {
        assertTrue(s.getDefaultIslandFlags().isEmpty());
    }

    /**
     * Test method for {@link world.bentobox.skygrid.Settings#getDefaultIslandSettings()}.
     */
    @SuppressWarnings("deprecation")
    @Test
    public void testGetDefaultIslandSettings() {
        assertTrue(s.getDefaultIslandSettings().isEmpty());
    }

    /**
     * Test method for {@link world.bentobox.skygrid.Settings#getHiddenFlags()}.
     */
    @Test
    public void testGetHiddenFlags() {
        assertTrue(s.getHiddenFlags().isEmpty());
    }

    /**
     * Test method for {@link world.bentobox.skygrid.Settings#getVisitorBannedCommands()}.
     */
    @Test
    public void testGetVisitorBannedCommands() {
        assertTrue(s.getVisitorBannedCommands().isEmpty());
    }

    /**
     * Test method for {@link world.bentobox.skygrid.Settings#getFallingBannedCommands()}.
     */
    @Test
    public void testGetFallingBannedCommands() {
        assertTrue(s.getFallingBannedCommands().isEmpty());
    }

    /**
     * Test method for {@link world.bentobox.skygrid.Settings#getMaxTeamSize()}.
     */
    @Test
    public void testGetMaxTeamSize() {
        assertEquals(4, s.getMaxTeamSize());
    }

    /**
     * Test method for {@link world.bentobox.skygrid.Settings#getMaxHomes()}.
     */
    @Test
    public void testGetMaxHomes() {
        assertEquals(5, s.getMaxHomes());
    }

    /**
     * Test method for {@link world.bentobox.skygrid.Settings#getResetLimit()}.
     */
    @Test
    public void testGetResetLimit() {
        assertEquals(-1, s.getResetLimit());
    }

    /**
     * Test method for {@link world.bentobox.skygrid.Settings#isLeaversLoseReset()}.
     */
    @Test
    public void testIsLeaversLoseReset() {
        assertFalse(s.isLeaversLoseReset());
    }

    /**
     * Test method for {@link world.bentobox.skygrid.Settings#isKickedKeepInventory()}.
     */
    @Test
    public void testIsKickedKeepInventory() {
        assertFalse(s.isKickedKeepInventory());
    }

    /**
     * Test method for {@link world.bentobox.skygrid.Settings#isCreateIslandOnFirstLoginEnabled()}.
     */
    @Test
    public void testIsCreateIslandOnFirstLoginEnabled() {
        assertFalse(s.isCreateIslandOnFirstLoginEnabled());
    }

    /**
     * Test method for {@link world.bentobox.skygrid.Settings#getCreateIslandOnFirstLoginDelay()}.
     */
    @Test
    public void testGetCreateIslandOnFirstLoginDelay() {
        assertEquals(5, s.getCreateIslandOnFirstLoginDelay());
    }

    /**
     * Test method for {@link world.bentobox.skygrid.Settings#isCreateIslandOnFirstLoginAbortOnLogout()}.
     */
    @Test
    public void testIsCreateIslandOnFirstLoginAbortOnLogout() {
        assertTrue(s.isCreateIslandOnFirstLoginAbortOnLogout());
    }

    /**
     * Test method for {@link world.bentobox.skygrid.Settings#isOnJoinResetMoney()}.
     */
    @Test
    public void testIsOnJoinResetMoney() {
        assertFalse(s.isOnJoinResetMoney());
    }

    /**
     * Test method for {@link world.bentobox.skygrid.Settings#isOnJoinResetInventory()}.
     */
    @Test
    public void testIsOnJoinResetInventory() {
        assertFalse(s.isOnJoinResetInventory());
    }

    /**
     * Test method for {@link world.bentobox.skygrid.Settings#isOnJoinResetEnderChest()}.
     */
    @Test
    public void testIsOnJoinResetEnderChest() {
        assertFalse(s.isOnJoinResetEnderChest());
    }

    /**
     * Test method for {@link world.bentobox.skygrid.Settings#isOnLeaveResetMoney()}.
     */
    @Test
    public void testIsOnLeaveResetMoney() {
        assertFalse(s.isOnLeaveResetMoney());
    }

    /**
     * Test method for {@link world.bentobox.skygrid.Settings#isOnLeaveResetInventory()}.
     */
    @Test
    public void testIsOnLeaveResetInventory() {
        assertFalse(s.isOnLeaveResetInventory());
    }

    /**
     * Test method for {@link world.bentobox.skygrid.Settings#isOnLeaveResetEnderChest()}.
     */
    @Test
    public void testIsOnLeaveResetEnderChest() {
        assertFalse(s.isOnLeaveResetEnderChest());
    }

    /**
     * Test method for {@link world.bentobox.skygrid.Settings#isDeathsCounted()}.
     */
    @Test
    public void testIsDeathsCounted() {
        assertTrue(s.isDeathsCounted());
    }

    /**
     * Test method for {@link world.bentobox.skygrid.Settings#isAllowSetHomeInNether()}.
     */
    @Test
    public void testIsAllowSetHomeInNether() {
        assertTrue(s.isAllowSetHomeInNether());
    }

    /**
     * Test method for {@link world.bentobox.skygrid.Settings#isAllowSetHomeInTheEnd()}.
     */
    @Test
    public void testIsAllowSetHomeInTheEnd() {
        assertTrue(s.isAllowSetHomeInTheEnd());
    }

    /**
     * Test method for {@link world.bentobox.skygrid.Settings#isRequireConfirmationToSetHomeInNether()}.
     */
    @Test
    public void testIsRequireConfirmationToSetHomeInNether() {
        assertTrue(s.isRequireConfirmationToSetHomeInNether());
    }

    /**
     * Test method for {@link world.bentobox.skygrid.Settings#isRequireConfirmationToSetHomeInTheEnd()}.
     */
    @Test
    public void testIsRequireConfirmationToSetHomeInTheEnd() {
        assertTrue(s.isRequireConfirmationToSetHomeInTheEnd());
    }

    /**
     * Test method for {@link world.bentobox.skygrid.Settings#getDeathsMax()}.
     */
    @Test
    public void testGetDeathsMax() {
        assertEquals(10, s.getDeathsMax());
    }

    /**
     * Test method for {@link world.bentobox.skygrid.Settings#isTeamJoinDeathReset()}.
     */
    @Test
    public void testIsTeamJoinDeathReset() {
        assertTrue(s.isTeamJoinDeathReset());
    }

    /**
     * Test method for {@link world.bentobox.skygrid.Settings#getGeoLimitSettings()}.
     */
    @Test
    public void testGetGeoLimitSettings() {
        assertTrue(s.getGeoLimitSettings().isEmpty());
    }

    /**
     * Test method for {@link world.bentobox.skygrid.Settings#getIvSettings()}.
     */
    @Test
    public void testGetIvSettings() {
        assertTrue(s.getIvSettings().isEmpty());
    }

    /**
     * Test method for {@link world.bentobox.skygrid.Settings#getResetEpoch()}.
     */
    @Test
    public void testGetResetEpoch() {
        assertEquals(0L, s.getResetEpoch());
    }

    /**
     * Test method for {@link world.bentobox.skygrid.Settings#setFriendlyName(java.lang.String)}.
     */
    @Test
    public void testSetFriendlyName() {
        s.setFriendlyName("test");
        assertEquals("test", s.getFriendlyName());
    }

    /**
     * Test method for {@link world.bentobox.skygrid.Settings#setWorldName(java.lang.String)}.
     */
    @Test
    public void testSetWorldName() {
        s.setWorldName("test");
        assertEquals("test", s.getWorldName());
    }

    /**
     * Test method for {@link world.bentobox.skygrid.Settings#setDifficulty(org.bukkit.Difficulty)}.
     */
    @Test
    public void testSetDifficulty() {
        s.setDifficulty(Difficulty.HARD);
        assertEquals(Difficulty.HARD, s.getDifficulty());
    }

    /**
     * Test method for {@link world.bentobox.skygrid.Settings#setIslandDistance(int)}.
     */
    @Test
    public void testSetIslandDistance() {
        s.setIslandDistance(12345);
        assertEquals(12345, s.getIslandDistance());
    }

    /**
     * Test method for {@link world.bentobox.skygrid.Settings#setIslandProtectionRange(int)}.
     */
    @Test
    public void testSetIslandProtectionRange() {
        s.setIslandProtectionRange(12345);
        assertEquals(12345, s.getIslandProtectionRange());
    }

    /**
     * Test method for {@link world.bentobox.skygrid.Settings#setIslandStartX(int)}.
     */
    @Test
    public void testSetIslandStartX() {
        s.setIslandStartX(12345);
        assertEquals(12345, s.getIslandStartX());
    }

    /**
     * Test method for {@link world.bentobox.skygrid.Settings#setIslandStartZ(int)}.
     */
    @Test
    public void testSetIslandStartZ() {
        s.setIslandStartZ(12345);
        assertEquals(12345, s.getIslandStartZ());
    }

    /**
     * Test method for {@link world.bentobox.skygrid.Settings#setIslandHeight(int)}.
     */
    @Test
    public void testSetIslandHeight() {
        s.setIslandHeight(12345);
        assertEquals(255, s.getIslandHeight());
    }

    /**
     * Test method for {@link world.bentobox.skygrid.Settings#setDefaultGameMode(org.bukkit.GameMode)}.
     */
    @Test
    public void testSetDefaultGameMode() {
        s.setDefaultGameMode(GameMode.SPECTATOR);
        assertEquals(GameMode.SPECTATOR, s.getDefaultGameMode());
    }

    /**
     * Test method for {@link world.bentobox.skygrid.Settings#setNetherGenerate(boolean)}.
     */
    @Test
    public void testSetNetherGenerate() {
        s.setNetherGenerate(false);
        assertFalse(s.isNetherGenerate());
        s.setNetherGenerate(true);
        assertTrue(s.isNetherGenerate());
    }

    /**
     * Test method for {@link world.bentobox.skygrid.Settings#setEndGenerate(boolean)}.
     */
    @Test
    public void testSetEndGenerate() {
        s.setEndGenerate(false);
        assertFalse(s.isEndGenerate());
        s.setEndGenerate(true);
        assertTrue(s.isEndGenerate());
    }

    /**
     * Test method for {@link world.bentobox.skygrid.Settings#setRemoveMobsWhitelist(java.util.Set)}.
     */
    @Test
    public void testSetRemoveMobsWhitelist() {
        s.setRemoveMobsWhitelist(Collections.singleton(EntityType.AXOLOTL));
        assertTrue(s.getRemoveMobsWhitelist().contains(EntityType.AXOLOTL));
    }

    /**
     * Test method for {@link world.bentobox.skygrid.Settings#setWorldFlags(java.util.Map)}.
     */
    @Test
    public void testSetWorldFlags() {
        s.setWorldFlags(Map.of("trueFlag", true, "falseFlag", false));
        assertTrue(s.getWorldFlags().get("trueFlag"));
        assertFalse(s.getWorldFlags().get("falseFlag"));
    }

    /**
     * Test method for {@link world.bentobox.skygrid.Settings#setDefaultIslandFlagNames(java.util.Map)}.
     */
    @Test
    public void testSetDefaultIslandFlagNames() {
        s.setDefaultIslandFlagNames(Map.of("TEST", 500));
        assertTrue(s.getDefaultIslandFlagNames().get("TEST") == 500);
    }

    /**
     * Test method for {@link world.bentobox.skygrid.Settings#setDefaultIslandSettingNames(java.util.Map)}.
     */
    @Test
    public void testSetDefaultIslandSettingNames() {
        s.setDefaultIslandSettingNames(Map.of("SETTING", 456));
        assertTrue(s.getDefaultIslandSettingNames().get("SETTING") == 456);
    }

    /**
     * Test method for {@link world.bentobox.skygrid.Settings#setHiddenFlags(java.util.List)}.
     */
    @Test
    public void testSetHiddenFlags() {
        s.setHiddenFlags(List.of("FLAG1", "FLAG2"));
        assertTrue(s.getHiddenFlags().contains("FLAG2"));
        assertFalse(s.getHiddenFlags().contains("FLAG3"));
    }

    /**
     * Test method for {@link world.bentobox.skygrid.Settings#setVisitorBannedCommands(java.util.List)}.
     */
    @Test
    public void testSetVisitorBannedCommands() {
        s.setVisitorBannedCommands(List.of("banned"));
        assertTrue(s.getVisitorBannedCommands().contains("banned"));
        assertFalse(s.getVisitorBannedCommands().contains("not-banned"));
    }

    /**
     * Test method for {@link world.bentobox.skygrid.Settings#setFallingBannedCommands(java.util.List)}.
     */
    @Test
    public void testSetFallingBannedCommands() {
        s.setFallingBannedCommands(List.of("banned"));
        assertTrue(s.getFallingBannedCommands().contains("banned"));
        assertFalse(s.getFallingBannedCommands().contains("not-banned"));
    }

    /**
     * Test method for {@link world.bentobox.skygrid.Settings#setMaxTeamSize(int)}.
     */
    @Test
    public void testSetMaxTeamSize() {
        s.setMaxTeamSize(12345);
        assertEquals(12345, s.getMaxTeamSize());
    }

    /**
     * Test method for {@link world.bentobox.skygrid.Settings#setMaxHomes(int)}.
     */
    @Test
    public void testSetMaxHomes() {
        s.setMaxHomes(12345);
        assertEquals(12345, s.getMaxHomes());
    }

    /**
     * Test method for {@link world.bentobox.skygrid.Settings#setResetLimit(int)}.
     */
    @Test
    public void testSetResetLimit() {
        s.setResetLimit(12345);
        assertEquals(12345, s.getResetLimit());
    }

    /**
     * Test method for {@link world.bentobox.skygrid.Settings#setLeaversLoseReset(boolean)}.
     */
    @Test
    public void testSetLeaversLoseReset() {
        s.setLeaversLoseReset(false);
        assertFalse(s.isLeaversLoseReset());
        s.setLeaversLoseReset(true);
        assertTrue(s.isLeaversLoseReset());
    }

    /**
     * Test method for {@link world.bentobox.skygrid.Settings#setKickedKeepInventory(boolean)}.
     */
    @Test
    public void testSetKickedKeepInventory() {
        s.setKickedKeepInventory(false);
        assertFalse(s.isKickedKeepInventory());
        s.setKickedKeepInventory(true);
        assertTrue(s.isKickedKeepInventory());
    }

    /**
     * Test method for {@link world.bentobox.skygrid.Settings#setOnJoinResetMoney(boolean)}.
     */
    @Test
    public void testSetOnJoinResetMoney() {
        s.setOnJoinResetMoney(false);
        assertFalse(s.isOnJoinResetMoney());
        s.setOnJoinResetMoney(true);
        assertTrue(s.isOnJoinResetMoney());
    }

    /**
     * Test method for {@link world.bentobox.skygrid.Settings#setOnJoinResetInventory(boolean)}.
     */
    @Test
    public void testSetOnJoinResetInventory() {
        s.setOnJoinResetInventory(false);
        assertFalse(s.isOnJoinResetInventory());
        s.setOnJoinResetInventory(true);
        assertTrue(s.isOnJoinResetInventory());
    }

    /**
     * Test method for {@link world.bentobox.skygrid.Settings#setOnJoinResetEnderChest(boolean)}.
     */
    @Test
    public void testSetOnJoinResetEnderChest() {
        s.setOnJoinResetEnderChest(false);
        assertFalse(s.isOnJoinResetEnderChest());
        s.setOnJoinResetEnderChest(true);
        assertTrue(s.isOnJoinResetEnderChest());
    }

    /**
     * Test method for {@link world.bentobox.skygrid.Settings#setOnLeaveResetMoney(boolean)}.
     */
    @Test
    public void testSetOnLeaveResetMoney() {
        s.setOnLeaveResetMoney(false);
        assertFalse(s.isOnLeaveResetMoney());
        s.setOnLeaveResetMoney(true);
        assertTrue(s.isOnLeaveResetMoney());
    }

    /**
     * Test method for {@link world.bentobox.skygrid.Settings#setOnLeaveResetInventory(boolean)}.
     */
    @Test
    public void testSetOnLeaveResetInventory() {
        s.setOnLeaveResetInventory(false);
        assertFalse(s.isOnLeaveResetInventory());
        s.setOnLeaveResetInventory(true);
        assertTrue(s.isOnLeaveResetInventory());
    }

    /**
     * Test method for {@link world.bentobox.skygrid.Settings#setOnLeaveResetEnderChest(boolean)}.
     */
    @Test
    public void testSetOnLeaveResetEnderChest() {
        s.setOnLeaveResetEnderChest(false);
        assertFalse(s.isOnLeaveResetEnderChest());
        s.setOnLeaveResetEnderChest(true);
        assertTrue(s.isOnLeaveResetEnderChest());
    }

    /**
     * Test method for {@link world.bentobox.skygrid.Settings#setCreateIslandOnFirstLoginEnabled(boolean)}.
     */
    @Test
    public void testSetCreateIslandOnFirstLoginEnabled() {
        s.setCreateIslandOnFirstLoginEnabled(false);
        assertFalse(s.isCreateIslandOnFirstLoginEnabled());
        s.setCreateIslandOnFirstLoginEnabled(true);
        assertTrue(s.isCreateIslandOnFirstLoginEnabled());
    }

    /**
     * Test method for {@link world.bentobox.skygrid.Settings#setCreateIslandOnFirstLoginDelay(int)}.
     */
    @Test
    public void testSetCreateIslandOnFirstLoginDelay() {
        s.setCreateIslandOnFirstLoginDelay(12345);
        assertEquals(12345, s.getCreateIslandOnFirstLoginDelay());
    }

    /**
     * Test method for {@link world.bentobox.skygrid.Settings#setCreateIslandOnFirstLoginAbortOnLogout(boolean)}.
     */
    @Test
    public void testSetCreateIslandOnFirstLoginAbortOnLogout() {
        s.setCreateIslandOnFirstLoginAbortOnLogout(false);
        assertFalse(s.isCreateIslandOnFirstLoginAbortOnLogout());
        s.setCreateIslandOnFirstLoginAbortOnLogout(true);
        assertTrue(s.isCreateIslandOnFirstLoginAbortOnLogout());
    }

    /**
     * Test method for {@link world.bentobox.skygrid.Settings#setDeathsCounted(boolean)}.
     */
    @Test
    public void testSetDeathsCounted() {
        s.setDeathsCounted(false);
        assertFalse(s.isDeathsCounted());
        s.setDeathsCounted(true);
        assertTrue(s.isDeathsCounted());
    }

    /**
     * Test method for {@link world.bentobox.skygrid.Settings#setDeathsMax(int)}.
     */
    @Test
    public void testSetDeathsMax() {
        s.setDeathsMax(12345);
        assertEquals(12345, s.getDeathsMax());
    }

    /**
     * Test method for {@link world.bentobox.skygrid.Settings#setTeamJoinDeathReset(boolean)}.
     */
    @Test
    public void testSetTeamJoinDeathReset() {
        s.setTeamJoinDeathReset(false);
        assertFalse(s.isTeamJoinDeathReset());
        s.setTeamJoinDeathReset(true);
        assertTrue(s.isTeamJoinDeathReset());
    }

    /**
     * Test method for {@link world.bentobox.skygrid.Settings#setGeoLimitSettings(java.util.List)}.
     */
    @Test
    public void testSetGeoLimitSettings() {
        s.setGeoLimitSettings(List.of("test"));
        assertTrue(s.getGeoLimitSettings().contains("test"));
    }

    /**
     * Test method for {@link world.bentobox.skygrid.Settings#setIvSettings(java.util.List)}.
     */
    @Test
    public void testSetIvSettings() {
        s.setIvSettings(List.of("test"));
        assertTrue(s.getIvSettings().contains("test"));
    }

    /**
     * Test method for {@link world.bentobox.skygrid.Settings#setAllowSetHomeInNether(boolean)}.
     */
    @Test
    public void testSetAllowSetHomeInNether() {
        s.setAllowSetHomeInNether(false);
        assertFalse(s.isAllowSetHomeInNether());
        s.setAllowSetHomeInNether(true);
        assertTrue(s.isAllowSetHomeInNether());
    }

    /**
     * Test method for {@link world.bentobox.skygrid.Settings#setAllowSetHomeInTheEnd(boolean)}.
     */
    @Test
    public void testSetAllowSetHomeInTheEnd() {
        s.setAllowSetHomeInTheEnd(false);
        assertFalse(s.isAllowSetHomeInTheEnd());
        s.setAllowSetHomeInTheEnd(true);
        assertTrue(s.isAllowSetHomeInTheEnd());
    }

    /**
     * Test method for {@link world.bentobox.skygrid.Settings#setRequireConfirmationToSetHomeInNether(boolean)}.
     */
    @Test
    public void testSetRequireConfirmationToSetHomeInNether() {
        s.setRequireConfirmationToSetHomeInNether(false);
        assertFalse(s.isRequireConfirmationToSetHomeInNether());
        s.setRequireConfirmationToSetHomeInNether(true);
        assertTrue(s.isRequireConfirmationToSetHomeInNether());
    }

    /**
     * Test method for {@link world.bentobox.skygrid.Settings#setRequireConfirmationToSetHomeInTheEnd(boolean)}.
     */
    @Test
    public void testSetRequireConfirmationToSetHomeInTheEnd() {
        s.setRequireConfirmationToSetHomeInTheEnd(false);
        assertFalse(s.isRequireConfirmationToSetHomeInTheEnd());
        s.setRequireConfirmationToSetHomeInTheEnd(true);
        assertTrue(s.isRequireConfirmationToSetHomeInTheEnd());
    }

    /**
     * Test method for {@link world.bentobox.skygrid.Settings#setResetEpoch(long)}.
     */
    @Test
    public void testSetResetEpoch() {
        s.setResetEpoch(12345);
        assertEquals(12345, s.getResetEpoch());
    }

    /**
     * Test method for {@link world.bentobox.skygrid.Settings#getPermissionPrefix()}.
     */
    @Test
    public void testGetPermissionPrefix() {
        assertEquals("skygrid", s.getPermissionPrefix());
    }

    /**
     * Test method for {@link world.bentobox.skygrid.Settings#isWaterUnsafe()}.
     */
    @Test
    public void testIsWaterUnsafe() {
        assertFalse(s.isWaterUnsafe());
    }

    /**
     * Test method for {@link world.bentobox.skygrid.Settings#getBanLimit()}.
     */
    @Test
    public void testGetBanLimit() {
        assertEquals(-1, s.getBanLimit());
    }

    /**
     * Test method for {@link world.bentobox.skygrid.Settings#setBanLimit(int)}.
     */
    @Test
    public void testSetBanLimit() {
        assertEquals(-1, s.getBanLimit());
        s.setBanLimit(12345);
        assertEquals(12345, s.getBanLimit());
    }

    /**
     * Test method for {@link world.bentobox.skygrid.Settings#getPlayerCommandAliases()}.
     */
    @Test
    public void testGetPlayerCommandAliases() {
        assertEquals("skygrid sg",s.getPlayerCommandAliases());
    }

    /**
     * Test method for {@link world.bentobox.skygrid.Settings#setPlayerCommandAliases(java.lang.String)}.
     */
    @Test
    public void testSetPlayerCommandAliases() {
        assertEquals("skygrid sg",s.getPlayerCommandAliases());
        s.setPlayerCommandAliases("aliases");
        assertEquals("aliases",s.getPlayerCommandAliases());
    }

    /**
     * Test method for {@link world.bentobox.skygrid.Settings#getAdminCommandAliases()}.
     */
    @Test
    public void testGetAdminCommandAliases() {
        assertEquals("sgadmin sga",s.getAdminCommandAliases());
    }

    /**
     * Test method for {@link world.bentobox.skygrid.Settings#setAdminCommandAliases(java.lang.String)}.
     */
    @Test
    public void testSetAdminCommandAliases() {
        assertEquals("sgadmin sga",s.getAdminCommandAliases());
        s.setAdminCommandAliases("aliases");
        assertEquals("aliases",s.getAdminCommandAliases());
    }

    /**
     * Test method for {@link world.bentobox.skygrid.Settings#isDeathsResetOnNewIsland()}.
     */
    @Test
    public void testIsDeathsResetOnNewIsland() {
        assertTrue(s.isDeathsResetOnNewIsland());
    }

    /**
     * Test method for {@link world.bentobox.skygrid.Settings#setDeathsResetOnNewIsland(boolean)}.
     */
    @Test
    public void testSetDeathsResetOnNewIsland() {
        s.setDeathsResetOnNewIsland(false);
        assertFalse(s.isDeathsResetOnNewIsland());
        s.setDeathsResetOnNewIsland(true);
        assertTrue(s.isDeathsResetOnNewIsland());
    }

    /**
     * Test method for {@link world.bentobox.skygrid.Settings#getOnJoinCommands()}.
     */
    @Test
    public void testGetOnJoinCommands() {
        assertTrue(s.getOnJoinCommands().isEmpty());
    }

    /**
     * Test method for {@link world.bentobox.skygrid.Settings#setOnJoinCommands(java.util.List)}.
     */
    @Test
    public void testSetOnJoinCommands() {
        s.setOnJoinCommands(List.of("command", "do this"));
        assertEquals("do this", s.getOnJoinCommands().get(1));
    }

    /**
     * Test method for {@link world.bentobox.skygrid.Settings#getOnLeaveCommands()}.
     */
    @Test
    public void testGetOnLeaveCommands() {
        assertTrue(s.getOnLeaveCommands().isEmpty());
    }

    /**
     * Test method for {@link world.bentobox.skygrid.Settings#setOnLeaveCommands(java.util.List)}.
     */
    @Test
    public void testSetOnLeaveCommands() {
        s.setOnLeaveCommands(List.of("command", "do this"));
        assertEquals("do this", s.getOnLeaveCommands().get(1));
    }

    /**
     * Test method for {@link world.bentobox.skygrid.Settings#getOnRespawnCommands()}.
     */
    @Test
    public void testGetOnRespawnCommands() {
        assertTrue(s.getOnRespawnCommands().isEmpty());
    }

    /**
     * Test method for {@link world.bentobox.skygrid.Settings#setOnRespawnCommands(java.util.List)}.
     */
    @Test
    public void testSetOnRespawnCommands() {
        s.setOnRespawnCommands(List.of("command", "do this"));
        assertEquals("do this", s.getOnRespawnCommands().get(1));
    }

    /**
     * Test method for {@link world.bentobox.skygrid.Settings#isOnJoinResetHealth()}.
     */
    @Test
    public void testIsOnJoinResetHealth() {
        assertTrue(s.isOnJoinResetHealth());
    }

    /**
     * Test method for {@link world.bentobox.skygrid.Settings#setOnJoinResetHealth(boolean)}.
     */
    @Test
    public void testSetOnJoinResetHealth() {
        s.setOnJoinResetHealth(false);
        assertFalse(s.isOnJoinResetHealth());
        s.setOnJoinResetHealth(true);
        assertTrue(s.isOnJoinResetHealth());
    }

    /**
     * Test method for {@link world.bentobox.skygrid.Settings#isOnJoinResetHunger()}.
     */
    @Test
    public void testIsOnJoinResetHunger() {
        assertTrue(s.isOnJoinResetHunger());
    }

    /**
     * Test method for {@link world.bentobox.skygrid.Settings#setOnJoinResetHunger(boolean)}.
     */
    @Test
    public void testSetOnJoinResetHunger() {
        s.setOnJoinResetHunger(false);
        assertFalse(s.isOnJoinResetHunger());
        s.setOnJoinResetHunger(true);
        assertTrue(s.isOnJoinResetHunger());
    }

    /**
     * Test method for {@link world.bentobox.skygrid.Settings#isOnJoinResetXP()}.
     */
    @Test
    public void testIsOnJoinResetXP() {
        assertFalse(s.isOnJoinResetXP());
    }

    /**
     * Test method for {@link world.bentobox.skygrid.Settings#setOnJoinResetXP(boolean)}.
     */
    @Test
    public void testSetOnJoinResetXP() {
        s.setOnJoinResetXP(false);
        assertFalse(s.isOnJoinResetXP());
        s.setOnJoinResetXP(true);
        assertTrue(s.isOnJoinResetXP());
    }

    /**
     * Test method for {@link world.bentobox.skygrid.Settings#isOnLeaveResetHealth()}.
     */
    @Test
    public void testIsOnLeaveResetHealth() {
        assertFalse(s.isOnLeaveResetHealth());
    }

    /**
     * Test method for {@link world.bentobox.skygrid.Settings#setOnLeaveResetHealth(boolean)}.
     */
    @Test
    public void testSetOnLeaveResetHealth() {
        s.setOnLeaveResetHealth(false);
        assertFalse(s.isOnLeaveResetHealth());
        s.setOnLeaveResetHealth(true);
        assertTrue(s.isOnLeaveResetHealth());
    }

    /**
     * Test method for {@link world.bentobox.skygrid.Settings#isOnLeaveResetHunger()}.
     */
    @Test
    public void testIsOnLeaveResetHunger() {
        assertFalse(s.isOnLeaveResetHunger());
    }

    /**
     * Test method for {@link world.bentobox.skygrid.Settings#setOnLeaveResetHunger(boolean)}.
     */
    @Test
    public void testSetOnLeaveResetHunger() {
        s.setOnLeaveResetHunger(false);
        assertFalse(s.isOnLeaveResetHunger());
        s.setOnLeaveResetHunger(true);
        assertTrue(s.isOnLeaveResetHunger());
    }

    /**
     * Test method for {@link world.bentobox.skygrid.Settings#isOnLeaveResetXP()}.
     */
    @Test
    public void testIsOnLeaveResetXP() {
        assertFalse(s.isOnLeaveResetXP());
    }

    /**
     * Test method for {@link world.bentobox.skygrid.Settings#setOnLeaveResetXP(boolean)}.
     */
    @Test
    public void testSetOnLeaveResetXP() {
        assertFalse(s.isOnLeaveResetXP());
        s.setOnLeaveResetXP(true);
        assertTrue(s.isOnLeaveResetXP());
    }

    /**
     * Test method for {@link world.bentobox.skygrid.Settings#isTeleportPlayerToIslandUponIslandCreation()}.
     */
    @Test
    public void testIsTeleportPlayerToIslandUponIslandCreation() {
        assertTrue(s.isTeleportPlayerToIslandUponIslandCreation());
    }

    /**
     * Test method for {@link world.bentobox.skygrid.Settings#getMaxCoopSize()}.
     */
    @Test
    public void testGetMaxCoopSize() {
        assertEquals(4, s.getMaxCoopSize());
    }

    /**
     * Test method for {@link world.bentobox.skygrid.Settings#setMaxCoopSize(int)}.
     */
    @Test
    public void testSetMaxCoopSize() {
        s.setMaxCoopSize(12345);
        assertEquals(12345, s.getMaxCoopSize());
    }

    /**
     * Test method for {@link world.bentobox.skygrid.Settings#getMaxTrustSize()}.
     */
    @Test
    public void testGetMaxTrustSize() {
        assertEquals(4, s.getMaxTrustSize());
    }

    /**
     * Test method for {@link world.bentobox.skygrid.Settings#setMaxTrustSize(int)}.
     */
    @Test
    public void testSetMaxTrustSize() {
        s.setMaxTrustSize(12345);
        assertEquals(12345, s.getMaxTrustSize());
    }

    /**
     * Test method for {@link world.bentobox.skygrid.Settings#getDefaultNewPlayerAction()}.
     */
    @Test
    public void testGetDefaultNewPlayerAction() {
        assertEquals("create", s.getDefaultNewPlayerAction());
    }

    /**
     * Test method for {@link world.bentobox.skygrid.Settings#setDefaultNewPlayerAction(java.lang.String)}.
     */
    @Test
    public void testSetDefaultNewPlayerAction() {
        s.setDefaultNewPlayerAction("test");
        assertEquals("test", s.getDefaultNewPlayerAction());
    }

    /**
     * Test method for {@link world.bentobox.skygrid.Settings#getDefaultPlayerAction()}.
     */
    @Test
    public void testGetDefaultPlayerAction() {
        assertEquals("go", s.getDefaultPlayerAction());
    }

    /**
     * Test method for {@link world.bentobox.skygrid.Settings#setDefaultPlayerAction(java.lang.String)}.
     */
    @Test
    public void testSetDefaultPlayerAction() {
        s.setDefaultPlayerAction("test");
        assertEquals("test", s.getDefaultPlayerAction());
    }

    /**
     * Test method for {@link world.bentobox.skygrid.Settings#getMobLimitSettings()}.
     */
    @Test
    public void testGetMobLimitSettings() {
        assertTrue(s.getMobLimitSettings().isEmpty());
    }

    /**
     * Test method for {@link world.bentobox.skygrid.Settings#setMobLimitSettings(java.util.List)}.
     */
    @Test
    public void testSetMobLimitSettings() {
        s.setMobLimitSettings(List.of("test"));
        assertEquals("test", s.getMobLimitSettings().get(0));
    }

    /**
     * Test method for {@link world.bentobox.skygrid.Settings#isMakeNetherPortals()}.
     */
    @Test
    public void testIsMakeNetherPortals() {
        assertFalse(s.isMakeNetherPortals());
    }

    /**
     * Test method for {@link world.bentobox.skygrid.Settings#isMakeEndPortals()}.
     */
    @Test
    public void testIsMakeEndPortals() {
        assertTrue(s.isMakeEndPortals());
    }

    /**
     * Test method for {@link world.bentobox.skygrid.Settings#setMakeNetherPortals(boolean)}.
     */
    @Test
    public void testSetMakeNetherPortals() {
        s.setMakeNetherPortals(false);
        assertFalse(s.isMakeNetherPortals());
        s.setMakeNetherPortals(true);
        assertTrue(s.isMakeNetherPortals());
    }

    /**
     * Test method for {@link world.bentobox.skygrid.Settings#setMakeEndPortals(boolean)}.
     */
    @Test
    public void testSetMakeEndPortals() {
        s.setMakeEndPortals(false);
        assertFalse(s.isMakeEndPortals());
        s.setMakeEndPortals(true);
        assertTrue(s.isMakeEndPortals());
    }


}
