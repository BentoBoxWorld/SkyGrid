package world.bentobox.skygrid;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.bukkit.Difficulty;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;

import world.bentobox.bentobox.api.configuration.ConfigComment;
import world.bentobox.bentobox.api.configuration.ConfigEntry;
import world.bentobox.bentobox.api.configuration.StoreAt;
import world.bentobox.bentobox.api.configuration.WorldSettings;
import world.bentobox.bentobox.api.flags.Flag;
import world.bentobox.bentobox.database.objects.DataObject;
import world.bentobox.bentobox.database.objects.adapters.Adapter;
import world.bentobox.bentobox.database.objects.adapters.FlagSerializer;
import world.bentobox.bentobox.database.objects.adapters.FlagSerializer2;

/**
 * All the plugin settings are here
 * @author Tastybento
 */
@StoreAt(filename="config.yml", path="addons/SkyGrid") // Explicitly call out what name this should have.
@ConfigComment("SkyGrid Configuration [version]")
@ConfigComment("This config file is dynamic and saved when the server is shutdown.")
@ConfigComment("You cannot edit it while the server is running because changes will")
@ConfigComment("be lost! Use in-game settings GUI or edit when server is offline.")
@ConfigComment("")
public class Settings implements DataObject, WorldSettings {

    /* Blocks */
    @ConfigComment("World block types. If the material cannot be placed, bedrock will be used.")
    @ConfigComment("Format: Material : Probability")
    @ConfigComment("Block types must be Bukkit Material types.")
    @ConfigComment("Chests have different items in them in different world types.")
    @ConfigComment("Over world blocks. Beware of making too many chests, they can lag a lot.")
    @ConfigEntry(path = "world.blocks")
    private Map<Material, Integer> blocks = new HashMap<>();

    @ConfigComment("Nether block types")
    @ConfigComment("Beware with glowstone and lava - the lighting calcs will lag the")
    @ConfigComment("server badly if there are too many blocks.")
    @ConfigEntry(path = "world.netherblocks")
    private Map<Material, Integer> netherBlocks = new HashMap<>();

    @ConfigComment("The End blocks. END_CRYSTAL is blocked because it causes serious performance issues.")
    @ConfigEntry(path = "world.endblocks")
    private Map<Material, Integer> endBlocks = new HashMap<>();

    /* SkyGrid */
    @ConfigComment("Overworld has biomes - this will affect some block types and tree types.")
    @ConfigEntry(path = "world.create-biomes")
    private boolean createBiomes = true;

    @ConfigComment("The probability of a frame being created in a chunk. Frames are always at y=0.")
    @ConfigEntry(path = "world.end-frame-probability")
    private double endFrameProb = 0.1;

    @ConfigComment("Allow saplings to grow into trees sometimes.")
    @ConfigEntry(path = "world.grow-trees")
    private boolean growTrees = true;

    /*      WORLD       */
    @ConfigComment("Friendly name for this world. Used in admin commands. Must be a single word")
    @ConfigEntry(path = "world.friendly-name")
    private String friendlyName = "SkyGrid";

    @ConfigComment("Name of the world - if it does not exist then it will be generated.")
    @ConfigComment("It acts like a prefix for nether and end")
    @ConfigEntry(path = "world.world-name")
    private String worldName = "SkyGrid-world";

    @ConfigComment("World difficulty setting - PEACEFUL, EASY, NORMAL, HARD")
    @ConfigComment("Other plugins may override this setting")
    @ConfigEntry(path = "world.difficulty")
    private Difficulty difficulty = Difficulty.NORMAL;

    @ConfigComment("Start at these coordinates.")
    @ConfigEntry(path = "world.start-x", needsReset = true)
    private int islandStartX = 0;

    @ConfigEntry(path = "world.start-z", needsReset = true)
    private int islandStartZ = 0;

    @ConfigComment("SkyGrid height")
    @ConfigComment("This is the height of the top sky grid layer")
    @ConfigEntry(path = "world.skygrid-height")
    private int islandHeight = 128;

    @ConfigComment("Spawn height")
    @ConfigComment("Height where players will spawn. Can be less than the grid height")
    @ConfigEntry(path = "world.spawn-height")
    private int spawnHeight = 128;

    @ConfigComment("Space around new players in blocks (will be rounded up to nearest 16 blocks)")
    @ConfigEntry(path = "world.space-around-players")
    private int islandDistance = 1000;

    @ConfigComment("Default protection radius around player's home (so total size is 2x this)")
    @ConfigEntry(path = "world.protection-range")
    private int islandProtectionRange = 50;

    @ConfigComment("The default game mode for this world. Players will be set to this mode when they create")
    @ConfigComment("a new island for example. Options are SURVIVAL, CREATIVE, ADVENTURE, SPECTATOR")
    @ConfigEntry(path = "world.default-game-mode")
    private GameMode defaultGameMode = GameMode.SURVIVAL;

    private int banLimit = -1;

    @ConfigComment("Nether trees are made if a player grows a tree in the nether (gravel and glowstone)")
    @ConfigComment("Applies to both vanilla and islands Nether")
    @ConfigEntry(path = "world.nether.trees")
    private boolean netherTrees = true;

    private Set<EntityType> removeMobsWhitelist = new HashSet<>();

    @ConfigComment("World flags. These are boolean settings for various flags for this world")
    @ConfigEntry(path = "world.flags")
    private Map<String, Boolean> worldFlags = new HashMap<>();

    @ConfigComment("These are the default protection settings protected areas.")
    @ConfigComment("The value is the minimum island rank required to do the action")
    @ConfigComment("Ranks are: Visitor = 0, Member = 900, Owner = 1000")
    @ConfigEntry(path = "world.default-protection-flags")
    @Adapter(FlagSerializer.class)
    private Map<Flag, Integer> defaultIslandFlags = new HashMap<>();

    @ConfigComment("These are the default settings for new protected areas")
    @ConfigEntry(path = "world.default-settings")
    @Adapter(FlagSerializer2.class)
    private Map<Flag, Integer> defaultIslandSettings = new HashMap<>();

    @ConfigComment("These are the settings visible to users. (Not implemented yet)")
    @ConfigEntry(path = "world.visible-settings", experimental = true)
    private List<String> visibleSettings = new ArrayList<>();

    private List<String> visitorBannedCommands = new ArrayList<>();

    @ConfigComment("These settings should not be edited")
    @ConfigEntry(path = "do-not-edit-these-settings.reset-epoch")
    private long resetEpoch = 0;

    private String uniqueId = "config";

    /**
     * @return the friendlyName
     */
    @Override
    public String getFriendlyName() {
        return friendlyName;
    }

    /**
     * @return the blocks
     */
    public Map<Material, Integer> getBlocks() {
        return blocks;
    }

    /**
     * @return the netherBlocks
     */
    public Map<Material, Integer> getNetherBlocks() {
        return netherBlocks;
    }

    /**
     * @return the endBlocks
     */
    public Map<Material, Integer> getEndBlocks() {
        return endBlocks;
    }

    /**
     * @param blocks the blocks to set
     */
    public void setBlocks(Map<Material, Integer> blocks) {
        this.blocks = blocks;
    }

    /**
     * @param netherBlocks the netherBlocks to set
     */
    public void setNetherBlocks(Map<Material, Integer> netherBlocks) {
        this.netherBlocks = netherBlocks;
    }

    /**
     * @param endBlocks the endBlocks to set
     */
    public void setEndBlocks(Map<Material, Integer> endBlocks) {
        this.endBlocks = endBlocks;
    }

    /**
     * @return the worldName
     */
    @Override
    public String getWorldName() {
        return worldName;
    }

    /**
     * @return the difficulty
     */
    @Override
    public Difficulty getDifficulty() {
        return difficulty;
    }

    /**
     * @return the islandDistance
     */
    @Override
    public int getIslandDistance() {
        return islandDistance;
    }

    /**
     * @return the islandProtectionRange
     */
    @Override
    public int getIslandProtectionRange() {
        return islandProtectionRange;
    }

    /**
     * @return the islandStartX
     */
    @Override
    public int getIslandStartX() {
        return islandStartX;
    }

    /**
     * @return the islandStartZ
     */
    @Override
    public int getIslandStartZ() {
        return islandStartZ;
    }

    /**
     * @return the islandXOffset
     */
    @Override
    public int getIslandXOffset() {
        return 0;
    }

    /**
     * @return the islandZOffset
     */
    @Override
    public int getIslandZOffset() {
        return 0;
    }

    /**
     * @return the islandHeight
     */
    @Override
    public int getIslandHeight() {
        return islandHeight;
    }

    /**
     * @return the useOwnGenerator
     */
    @Override
    public boolean isUseOwnGenerator() {
        return false;
    }

    /**
     * @return the seaHeight
     */
    @Override
    public int getSeaHeight() {
        return 0;
    }

    /**
     * @return the maxIslands
     */
    @Override
    public int getMaxIslands() {
        return -1;
    }

    /**
     * @return the defaultGameMode
     */
    @Override
    public GameMode getDefaultGameMode() {
        return defaultGameMode;
    }

    /**
     * @return the netherGenerate
     */
    @Override
    public boolean isNetherGenerate() {
        return true;
    }

    /**
     * @return the netherIslands
     */
    @Override
    public boolean isNetherIslands() {
        return true;
    }

    /**
     * @return the netherTrees
     */
    @Override
    public boolean isNetherTrees() {
        return netherTrees;
    }

    /**
     * @return the netherSpawnRadius
     */
    @Override
    public int getNetherSpawnRadius() {
        return 0;
    }

    /**
     * @return the endGenerate
     */
    @Override
    public boolean isEndGenerate() {
        return true;
    }

    /**
     * @return the endIslands
     */
    @Override
    public boolean isEndIslands() {
        return true;
    }

    /**
     * @return the dragonSpawn
     */
    @Override
    public boolean isDragonSpawn() {
        return false;
    }

    /**
     * @return the removeMobsWhitelist
     */
    @Override
    public Set<EntityType> getRemoveMobsWhitelist() {
        return removeMobsWhitelist;
    }

    /**
     * @return the worldFlags
     */
    @Override
    public Map<String, Boolean> getWorldFlags() {
        return worldFlags;
    }

    /**
     * @return the defaultIslandFlags
     */
    @Override
    public Map<Flag, Integer> getDefaultIslandFlags() {
        return defaultIslandFlags;
    }

    /**
     * @return the defaultIslandSettings
     */
    @Override
    public Map<Flag, Integer> getDefaultIslandSettings() {
        return defaultIslandSettings;
    }

    /**
     * @return the visibleSettings
     */
    @Override
    public List<String> getVisibleSettings() {
        return visibleSettings;
    }

    /**
     * @return the visitorBannedCommands
     */
    @Override
    public List<String> getVisitorBannedCommands() {
        return visitorBannedCommands;
    }

    /**
     * @return the maxTeamSize
     */
    @Override
    public int getMaxTeamSize() {
        return 0;
    }

    /**
     * @return the maxHomes
     */
    @Override
    public int getMaxHomes() {
        return 1;
    }

    /**
     * @return the resetLimit
     */
    @Override
    public int getResetLimit() {
        return 0;
    }

    /**
     * @return the onJoinResetMoney
     */
    @Override
    public boolean isOnJoinResetMoney() {
        return false;
    }

    /**
     * @return the onJoinResetInventory
     */
    @Override
    public boolean isOnJoinResetInventory() {
        return false;
    }

    /**
     * @return the onJoinResetEnderChest
     */
    @Override
    public boolean isOnJoinResetEnderChest() {
        return false;
    }

    /**
     * @return the onLeaveResetMoney
     */
    @Override
    public boolean isOnLeaveResetMoney() {
        return false;
    }

    /**
     * @return the onLeaveResetInventory
     */
    @Override
    public boolean isOnLeaveResetInventory() {
        return false;
    }

    /**
     * @return the onLeaveResetEnderChest
     */
    @Override
    public boolean isOnLeaveResetEnderChest() {
        return false;
    }

    /**
     * @return the isDeathsCounted
     */
    @Override
    public boolean isDeathsCounted() {
        return false;
    }

    /**
     * @return the allowSetHomeInNether
     */
    @Override
    public boolean isAllowSetHomeInNether() {
        return true;
    }

    /**
     * @return the allowSetHomeInTheEnd
     */
    @Override
    public boolean isAllowSetHomeInTheEnd() {
        return true;
    }

    /**
     * @return the requireConfirmationToSetHomeInNether
     */
    @Override
    public boolean isRequireConfirmationToSetHomeInNether() {
        return false;
    }

    /**
     * @return the requireConfirmationToSetHomeInTheEnd
     */
    @Override
    public boolean isRequireConfirmationToSetHomeInTheEnd() {
        return false;
    }

    /**
     * @return the deathsMax
     */
    @Override
    public int getDeathsMax() {
        return 0;
    }

    /**
     * @return the teamJoinDeathReset
     */
    @Override
    public boolean isTeamJoinDeathReset() {
        return false;
    }

    /**
     * @return the geoLimitSettings
     */
    @Override
    public List<String> getGeoLimitSettings() {
        return Collections.emptyList();
    }

    /**
     * @return the ivSettings
     */
    @Override
    public List<String> getIvSettings() {
        return Collections.emptyList();
    }

    /**
     * @return the closePanelOnClickOutside
     */
    public boolean isClosePanelOnClickOutside() {
        return true;
    }

    /**
     * @return the resetEpoch
     */
    @Override
    public long getResetEpoch() {
        return resetEpoch;
    }

    /**
     * @return the uniqueId
     */
    @Override
    public String getUniqueId() {
        return uniqueId;
    }

    /**
     * @param friendlyName the friendlyName to set
     */
    public void setFriendlyName(String friendlyName) {
        this.friendlyName = friendlyName;
    }

    /**
     * @param worldName the worldName to set
     */
    public void setWorldName(String worldName) {
        this.worldName = worldName;
    }

    /**
     * @param difficulty the difficulty to set
     */
    @Override
    public void setDifficulty(Difficulty difficulty) {
        this.difficulty = difficulty;
    }

    /**
     * @param islandStartX the islandStartX to set
     */
    public void setIslandStartX(int islandStartX) {
        this.islandStartX = islandStartX;
    }

    /**
     * @param islandStartZ the islandStartZ to set
     */
    public void setIslandStartZ(int islandStartZ) {
        this.islandStartZ = islandStartZ;
    }

    /**
     * @param islandHeight the islandHeight to set
     */
    public void setIslandHeight(int islandHeight) {
        this.islandHeight = islandHeight;
    }

    /**
     * @param defaultGameMode the defaultGameMode to set
     */
    public void setDefaultGameMode(GameMode defaultGameMode) {
        this.defaultGameMode = defaultGameMode;
    }

    /**
     * @param netherTrees the netherTrees to set
     */
    public void setNetherTrees(boolean netherTrees) {
        this.netherTrees = netherTrees;
    }

    /**
     * @param removeMobsWhitelist the removeMobsWhitelist to set
     */
    public void setRemoveMobsWhitelist(Set<EntityType> removeMobsWhitelist) {
        this.removeMobsWhitelist = removeMobsWhitelist;
    }

    /**
     * @param worldFlags the worldFlags to set
     */
    public void setWorldFlags(Map<String, Boolean> worldFlags) {
        this.worldFlags = worldFlags;
    }

    /**
     * @param defaultIslandFlags the defaultIslandFlags to set
     */
    public void setDefaultIslandFlags(Map<Flag, Integer> defaultIslandFlags) {
        this.defaultIslandFlags = defaultIslandFlags;
    }

    /**
     * @param defaultIslandSettings the defaultIslandSettings to set
     */
    public void setDefaultIslandSettings(Map<Flag, Integer> defaultIslandSettings) {
        this.defaultIslandSettings = defaultIslandSettings;
    }

    /**
     * @param visibleSettings the visibleSettings to set
     */
    public void setVisibleSettings(List<String> visibleSettings) {
        this.visibleSettings = visibleSettings;
    }

    /**
     * @param visitorBannedCommands the visitorBannedCommands to set
     */
    public void setVisitorBannedCommands(List<String> visitorBannedCommands) {
        this.visitorBannedCommands = visitorBannedCommands;
    }

    /**
     * @param resetEpoch the resetEpoch to set
     */
    @Override
    public void setResetEpoch(long resetEpoch) {
        this.resetEpoch = resetEpoch;
    }

    /**
     * @param uniqueId the uniqueId to set
     */
    @Override
    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }

    @Override
    public String getPermissionPrefix() {
        return "bskyblock";
    }

    @Override
    public boolean isWaterUnsafe() {
        return false;
    }

    /**
     * @return the banLimit
     */
    @Override
    public int getBanLimit() {
        return banLimit;
    }

    /**
     * @param banLimit the banLimit to set
     */
    public void setBanLimit(int banLimit) {
        this.banLimit = banLimit;
    }

    /**
     * @return the createBiomes
     */
    public boolean isCreateBiomes() {
        return createBiomes;
    }

    /**
     * @param createBiomes the createBiomes to set
     */
    public void setCreateBiomes(boolean createBiomes) {
        this.createBiomes = createBiomes;
    }

    /**
     * @return the spawnHeight
     */
    public int getSpawnHeight() {
        return spawnHeight + 5;
    }

    /**
     * @param spawnHeight the spawnHeight to set
     */
    public void setSpawnHeight(int spawnHeight) {
        this.spawnHeight = spawnHeight - 5;
    }

    /**
     * @return the endFrameProb
     */
    public double getEndFrameProb() {
        return endFrameProb;
    }

    /**
     * @param endFrameProb the endFrameProb to set
     */
    public void setEndFrameProb(double endFrameProb) {
        this.endFrameProb = endFrameProb;
    }

    /**
     * @return the growTrees
     */
    public boolean isGrowTrees() {
        return growTrees;
    }

    /**
     * @param growTrees the growTrees to set
     */
    public void setGrowTrees(boolean growTrees) {
        this.growTrees = growTrees;
    }

    /**
     * @param islandProtectionRange the islandProtectionRange to set
     */
    public void setIslandProtectionRange(int islandProtectionRange) {
        this.islandProtectionRange = islandProtectionRange;
    }

    /**
     * @param islandDistance the islandDistance to set
     */
    public void setIslandDistance(int islandDistance) {
        this.islandDistance = islandDistance;
    }

}
