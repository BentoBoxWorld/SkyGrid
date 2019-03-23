package world.bentobox.skygrid;

import java.util.ArrayList;
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
 * All the settings are here
 * @author tastybento
 */
@StoreAt(filename="config.yml", path="addons/SkyGrid") // Explicitly call out what name this should have.
@ConfigComment("SkyGrid Configuration [version]")
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

    // Nether
    @ConfigComment("Generate SkyGrid Nether - if this is false, the nether world will not be made")
    @ConfigEntry(path = "world.nether.generate")
    private boolean netherGenerate = true;

    @ConfigComment("Nether trees are made if a player grows a tree in the nether (gravel and glowstone)")
    @ConfigEntry(path = "world.nether.trees")
    private boolean netherTrees = true;

    @ConfigComment("Nether block types")
    @ConfigComment("Beware with glowstone and lava - the lighting calcs will lag the")
    @ConfigComment("server badly if there are too many blocks.")
    @ConfigEntry(path = "world.nether.blocks")
    private Map<Material, Integer> netherBlocks = new HashMap<>();

    // End
    @ConfigComment("Generate SkyGrid End - if this is false, the end world will not be made")
    @ConfigEntry(path = "world.end.generate")
    private boolean endGenerate = true;

    @ConfigComment("The End blocks. END_CRYSTAL is blocked because it causes serious performance issues.")
    @ConfigEntry(path = "world.end.blocks")
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

    @ConfigComment("The maximum number of players a player can ban at any one time in this game mode.")
    @ConfigComment("The permission acidarea.ban.maxlimit.X where X is a number can also be used per player")
    @ConfigComment("-1 = unlimited")
    @ConfigEntry(path = "world.ban-limit")
    private int banLimit = -1;

    @ConfigComment("Mob white list - these mobs will NOT be removed when logging in or doing /island")
    @ConfigEntry(path = "world.remove-mobs-whitelist")
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

    @ConfigComment("These settings/flags are hidden from users")
    @ConfigComment("Ops can toggle hiding in-game using SHIFT-LEFT-CLICK on flags in settings")
    @ConfigEntry(path = "world.hidden-flags")
    private List<String> hiddenFlags = new ArrayList<>();

    @ConfigComment("Visitor banned commands - Visitors to islands cannot use these commands in this world")
    @ConfigEntry(path = "world.visitor-banned-commands")
    private List<String> visitorBannedCommands = new ArrayList<>();
    // ---------------------------------------------

    /*      PROTECTED AREA      */
    @ConfigComment("Default max team size")
    @ConfigComment("Permission size cannot be less than the default below. ")
    @ConfigEntry(path = "area.max-team-size")
    private int maxTeamSize = 4;

    // Reset
    @ConfigComment("How many resets a player is allowed (override with /sgadmin clearresets <player>)")
    @ConfigComment("Value of -1 means unlimited, 0 means hardcore - no resets.")
    @ConfigComment("Example, 2 resets means they get 2 resets or 3 islands lifetime")
    @ConfigEntry(path = "area.reset.reset-limit")
    private int resetLimit = -1;

    @ConfigComment("Kicked or leaving players lose resets")
    @ConfigComment("Players who leave a team will lose a reset chance")
    @ConfigComment("If a player has zero resets left and leaves a team, they cannot make a new")
    @ConfigComment("island by themselves and can only join a team.")
    @ConfigComment("Leave this true to avoid players exploiting free islands")
    @ConfigEntry(path = "area.reset.leavers-lose-reset")
    private boolean leaversLoseReset = false;

    @ConfigComment("Allow kicked players to keep their inventory.")
    @ConfigComment("If false, kicked player's inventory will be thrown at the leader if the")
    @ConfigComment("kicked player is online and in the world.")
    @ConfigEntry(path = "area.reset.kicked-keep-inventory")
    private boolean kickedKeepInventory = false;

    @ConfigComment("What should reset when the player joins or starts new")
    @ConfigComment("Reset Money - if this is true, will reset the player's money to the starting money")
    @ConfigComment("Recommendation is that this is set to true, but if you run multi-worlds")
    @ConfigComment("make sure your economy handles multi-worlds too.")
    @ConfigEntry(path = "area.reset.on-join.money")
    private boolean onJoinResetMoney = false;

    @ConfigComment("Reset inventory - if true, the player's inventory will be cleared.")
    @ConfigComment("Note: if you have MultiInv running or a similar inventory control plugin, that")
    @ConfigComment("plugin may still reset the inventory when the world changes.")
    @ConfigEntry(path = "area.reset.on-join.inventory")
    private boolean onJoinResetInventory = false;

    @ConfigComment("Reset Ender Chest - if true, the player's Ender Chest will be cleared.")
    @ConfigEntry(path = "area.reset.on-join.ender-chest")
    private boolean onJoinResetEnderChest = false;

    @ConfigComment("What should reset when the player leaves or is kicked")
    @ConfigComment("Reset Money - if this is true, will reset the player's money to the starting money")
    @ConfigComment("Recommendation is that this is set to true, but if you run multi-worlds")
    @ConfigComment("make sure your economy handles multi-worlds too.")
    @ConfigEntry(path = "area.reset.on-leave.money")
    private boolean onLeaveResetMoney = false;

    @ConfigComment("Reset inventory - if true, the player's inventory will be cleared.")
    @ConfigComment("Note: if you have MultiInv running or a similar inventory control plugin, that")
    @ConfigComment("plugin may still reset the inventory when the world changes.")
    @ConfigEntry(path = "area.reset.on-leave.inventory")
    private boolean onLeaveResetInventory = false;

    @ConfigComment("Reset Ender Chest - if true, the player's Ender Chest will be cleared.")
    @ConfigEntry(path = "area.reset.on-leave.ender-chest")
    private boolean onLeaveResetEnderChest = false;

    // Sethome
    @ConfigEntry(path = "area.sethome.nether.allow")
    private boolean allowSetHomeInNether = true;

    @ConfigEntry(path = "area.sethome.nether.require-confirmation")
    private boolean requireConfirmationToSetHomeInNether = true;

    @ConfigEntry(path = "area.sethome.the-end.allow")
    private boolean allowSetHomeInTheEnd = true;

    @ConfigEntry(path = "area.sethome.the-end.require-confirmation")
    private boolean requireConfirmationToSetHomeInTheEnd = true;

    // Deaths
    @ConfigComment("Whether deaths are counted or not.")
    @ConfigEntry(path = "area.deaths.counted")
    private boolean deathsCounted = true;

    @ConfigComment("Maximum number of deaths to count. The death count can be used by add-ons.")
    @ConfigEntry(path = "area.deaths.max")
    private int deathsMax = 10;

    @ConfigEntry(path = "area.deaths.sum-team")
    private boolean deathsSumTeam = false;

    @ConfigComment("When a player joins a team, reset their death count")
    @ConfigEntry(path = "area.deaths.team-join-reset")
    private boolean teamJoinDeathReset = true;

    // ---------------------------------------------
    /*      PROTECTION      */

    @ConfigComment("Geo restrict mobs.")
    @ConfigComment("Mobs that exit the protected space where they were spawned will be removed.")
    @ConfigEntry(path = "protection.geo-limit-settings")
    private List<String> geoLimitSettings = new ArrayList<>();

    // Invincible visitor settings
    @ConfigComment("Invincible visitors. List of damages that will not affect visitors.")
    @ConfigComment("Make list blank if visitors should receive all damages")
    @ConfigEntry(path = "protection.invincible-visitors")
    private List<String> ivSettings = new ArrayList<>();

    //---------------------------------------------------------------------------------------/


    @ConfigComment("These settings should not be edited")
    @ConfigEntry(path = "do-not-edit-these-settings.reset-epoch")
    private long resetEpoch = 0;

    private String uniqueId = "config";

    /**
     * @return the blocks
     */
    public Map<Material, Integer> getBlocks() {
        return blocks;
    }

    /**
     * @param blocks the blocks to set
     */
    public void setBlocks(Map<Material, Integer> blocks) {
        this.blocks = blocks;
    }

    /**
     * @return the netherBlocks
     */
    public Map<Material, Integer> getNetherBlocks() {
        return netherBlocks;
    }

    /**
     * @param netherBlocks the netherBlocks to set
     */
    public void setNetherBlocks(Map<Material, Integer> netherBlocks) {
        this.netherBlocks = netherBlocks;
    }

    /**
     * @return the endBlocks
     */
    public Map<Material, Integer> getEndBlocks() {
        return endBlocks;
    }

    /**
     * @param endBlocks the endBlocks to set
     */
    public void setEndBlocks(Map<Material, Integer> endBlocks) {
        this.endBlocks = endBlocks;
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
     * @return the friendlyName
     */
    @Override
    public String getFriendlyName() {
        return friendlyName;
    }

    /**
     * @param friendlyName the friendlyName to set
     */
    public void setFriendlyName(String friendlyName) {
        this.friendlyName = friendlyName;
    }

    /**
     * @return the worldName
     */
    @Override
    public String getWorldName() {
        return worldName;
    }

    /**
     * @param worldName the worldName to set
     */
    public void setWorldName(String worldName) {
        this.worldName = worldName;
    }

    /**
     * @return the difficulty
     */
    @Override
    public Difficulty getDifficulty() {
        return difficulty;
    }

    /**
     * @param difficulty the difficulty to set
     */
    @Override
    public void setDifficulty(Difficulty difficulty) {
        this.difficulty = difficulty;
    }

    /**
     * @return the islandStartX
     */
    @Override
    public int getIslandStartX() {
        return islandStartX;
    }

    /**
     * @param islandStartX the islandStartX to set
     */
    public void setIslandStartX(int islandStartX) {
        this.islandStartX = islandStartX;
    }

    /**
     * @return the islandStartZ
     */
    @Override
    public int getIslandStartZ() {
        return islandStartZ;
    }

    /**
     * @param islandStartZ the islandStartZ to set
     */
    public void setIslandStartZ(int islandStartZ) {
        this.islandStartZ = islandStartZ;
    }

    /**
     * @return the islandHeight
     */
    @Override
    public int getIslandHeight() {
        return islandHeight;
    }

    /**
     * @param islandHeight the islandHeight to set
     */
    public void setIslandHeight(int islandHeight) {
        this.islandHeight = islandHeight;
    }

    /**
     * @return the spawnHeight
     */
    public int getSpawnHeight() {
        return spawnHeight;
    }

    /**
     * @param spawnHeight the spawnHeight to set
     */
    public void setSpawnHeight(int spawnHeight) {
        this.spawnHeight = spawnHeight;
    }

    /**
     * @return the islandDistance
     */
    @Override
    public int getIslandDistance() {
        return islandDistance;
    }

    /**
     * @param islandDistance the islandDistance to set
     */
    public void setIslandDistance(int islandDistance) {
        this.islandDistance = islandDistance;
    }

    /**
     * @return the islandProtectionRange
     */
    @Override
    public int getIslandProtectionRange() {
        return islandProtectionRange;
    }

    /**
     * @param islandProtectionRange the islandProtectionRange to set
     */
    public void setIslandProtectionRange(int islandProtectionRange) {
        this.islandProtectionRange = islandProtectionRange;
    }

    /**
     * @return the defaultGameMode
     */
    @Override
    public GameMode getDefaultGameMode() {
        return defaultGameMode;
    }

    /**
     * @param defaultGameMode the defaultGameMode to set
     */
    public void setDefaultGameMode(GameMode defaultGameMode) {
        this.defaultGameMode = defaultGameMode;
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
     * @return the netherGenerate
     */
    @Override
    public boolean isNetherGenerate() {
        return netherGenerate;
    }

    /**
     * @param netherGenerate the netherGenerate to set
     */
    public void setNetherGenerate(boolean netherGenerate) {
        this.netherGenerate = netherGenerate;
    }

    /**
     * @return the netherTrees
     */
    @Override
    public boolean isNetherTrees() {
        return netherTrees;
    }

    /**
     * @param netherTrees the netherTrees to set
     */
    public void setNetherTrees(boolean netherTrees) {
        this.netherTrees = netherTrees;
    }

    /**
     * @return the endGenerate
     */
    @Override
    public boolean isEndGenerate() {
        return endGenerate;
    }

    /**
     * @param endGenerate the endGenerate to set
     */
    public void setEndGenerate(boolean endGenerate) {
        this.endGenerate = endGenerate;
    }

    /**
     * @return the removeMobsWhitelist
     */
    @Override
    public Set<EntityType> getRemoveMobsWhitelist() {
        return removeMobsWhitelist;
    }

    /**
     * @param removeMobsWhitelist the removeMobsWhitelist to set
     */
    public void setRemoveMobsWhitelist(Set<EntityType> removeMobsWhitelist) {
        this.removeMobsWhitelist = removeMobsWhitelist;
    }

    /**
     * @return the worldFlags
     */
    @Override
    public Map<String, Boolean> getWorldFlags() {
        return worldFlags;
    }

    /**
     * @param worldFlags the worldFlags to set
     */
    public void setWorldFlags(Map<String, Boolean> worldFlags) {
        this.worldFlags = worldFlags;
    }

    /**
     * @return the defaultIslandFlags
     */
    @Override
    public Map<Flag, Integer> getDefaultIslandFlags() {
        return defaultIslandFlags;
    }

    /**
     * @param defaultIslandFlags the defaultIslandFlags to set
     */
    public void setDefaultIslandFlags(Map<Flag, Integer> defaultIslandFlags) {
        this.defaultIslandFlags = defaultIslandFlags;
    }

    /**
     * @return the defaultIslandSettings
     */
    @Override
    public Map<Flag, Integer> getDefaultIslandSettings() {
        return defaultIslandSettings;
    }

    /**
     * @param defaultIslandSettings the defaultIslandSettings to set
     */
    public void setDefaultIslandSettings(Map<Flag, Integer> defaultIslandSettings) {
        this.defaultIslandSettings = defaultIslandSettings;
    }

    /**
     * @return the hidden flags
     */
    @Override
    public List<String> getHiddenFlags() {
        return hiddenFlags;
    }

    /**
     * @param hiddenFlags the hidden flags to set
     */
    public void setVisibleSettings(List<String> hiddenFlags) {
        this.hiddenFlags = hiddenFlags;
    }

    /**
     * @return the visitorBannedCommands
     */
    @Override
    public List<String> getVisitorBannedCommands() {
        return visitorBannedCommands;
    }

    /**
     * @param visitorBannedCommands the visitorBannedCommands to set
     */
    public void setVisitorBannedCommands(List<String> visitorBannedCommands) {
        this.visitorBannedCommands = visitorBannedCommands;
    }

    /**
     * @return the maxTeamSize
     */
    @Override
    public int getMaxTeamSize() {
        return maxTeamSize;
    }

    /**
     * @param maxTeamSize the maxTeamSize to set
     */
    public void setMaxTeamSize(int maxTeamSize) {
        this.maxTeamSize = maxTeamSize;
    }

    /**
     * @return the resetLimit
     */
    @Override
    public int getResetLimit() {
        return resetLimit;
    }

    /**
     * @param resetLimit the resetLimit to set
     */
    public void setResetLimit(int resetLimit) {
        this.resetLimit = resetLimit;
    }

    /**
     * @return the leaversLoseReset
     */
    public boolean isLeaversLoseReset() {
        return leaversLoseReset;
    }

    /**
     * @param leaversLoseReset the leaversLoseReset to set
     */
    public void setLeaversLoseReset(boolean leaversLoseReset) {
        this.leaversLoseReset = leaversLoseReset;
    }

    /**
     * @return the kickedKeepInventory
     */
    public boolean isKickedKeepInventory() {
        return kickedKeepInventory;
    }

    /**
     * @param kickedKeepInventory the kickedKeepInventory to set
     */
    public void setKickedKeepInventory(boolean kickedKeepInventory) {
        this.kickedKeepInventory = kickedKeepInventory;
    }

    /**
     * @return the onJoinResetMoney
     */
    @Override
    public boolean isOnJoinResetMoney() {
        return onJoinResetMoney;
    }

    /**
     * @param onJoinResetMoney the onJoinResetMoney to set
     */
    public void setOnJoinResetMoney(boolean onJoinResetMoney) {
        this.onJoinResetMoney = onJoinResetMoney;
    }

    /**
     * @return the onJoinResetInventory
     */
    @Override
    public boolean isOnJoinResetInventory() {
        return onJoinResetInventory;
    }

    /**
     * @param onJoinResetInventory the onJoinResetInventory to set
     */
    public void setOnJoinResetInventory(boolean onJoinResetInventory) {
        this.onJoinResetInventory = onJoinResetInventory;
    }

    /**
     * @return the onJoinResetEnderChest
     */
    @Override
    public boolean isOnJoinResetEnderChest() {
        return onJoinResetEnderChest;
    }

    /**
     * @param onJoinResetEnderChest the onJoinResetEnderChest to set
     */
    public void setOnJoinResetEnderChest(boolean onJoinResetEnderChest) {
        this.onJoinResetEnderChest = onJoinResetEnderChest;
    }

    /**
     * @return the onLeaveResetMoney
     */
    @Override
    public boolean isOnLeaveResetMoney() {
        return onLeaveResetMoney;
    }

    /**
     * @param onLeaveResetMoney the onLeaveResetMoney to set
     */
    public void setOnLeaveResetMoney(boolean onLeaveResetMoney) {
        this.onLeaveResetMoney = onLeaveResetMoney;
    }

    /**
     * @return the onLeaveResetInventory
     */
    @Override
    public boolean isOnLeaveResetInventory() {
        return onLeaveResetInventory;
    }

    /**
     * @param onLeaveResetInventory the onLeaveResetInventory to set
     */
    public void setOnLeaveResetInventory(boolean onLeaveResetInventory) {
        this.onLeaveResetInventory = onLeaveResetInventory;
    }

    /**
     * @return the onLeaveResetEnderChest
     */
    @Override
    public boolean isOnLeaveResetEnderChest() {
        return onLeaveResetEnderChest;
    }

    /**
     * @param onLeaveResetEnderChest the onLeaveResetEnderChest to set
     */
    public void setOnLeaveResetEnderChest(boolean onLeaveResetEnderChest) {
        this.onLeaveResetEnderChest = onLeaveResetEnderChest;
    }

    /**
     * @return the allowSetHomeInNether
     */
    @Override
    public boolean isAllowSetHomeInNether() {
        return allowSetHomeInNether;
    }

    /**
     * @param allowSetHomeInNether the allowSetHomeInNether to set
     */
    public void setAllowSetHomeInNether(boolean allowSetHomeInNether) {
        this.allowSetHomeInNether = allowSetHomeInNether;
    }

    /**
     * @return the requireConfirmationToSetHomeInNether
     */
    @Override
    public boolean isRequireConfirmationToSetHomeInNether() {
        return requireConfirmationToSetHomeInNether;
    }

    /**
     * @param requireConfirmationToSetHomeInNether the requireConfirmationToSetHomeInNether to set
     */
    public void setRequireConfirmationToSetHomeInNether(boolean requireConfirmationToSetHomeInNether) {
        this.requireConfirmationToSetHomeInNether = requireConfirmationToSetHomeInNether;
    }

    /**
     * @return the allowSetHomeInTheEnd
     */
    @Override
    public boolean isAllowSetHomeInTheEnd() {
        return allowSetHomeInTheEnd;
    }

    /**
     * @param allowSetHomeInTheEnd the allowSetHomeInTheEnd to set
     */
    public void setAllowSetHomeInTheEnd(boolean allowSetHomeInTheEnd) {
        this.allowSetHomeInTheEnd = allowSetHomeInTheEnd;
    }

    /**
     * @return the requireConfirmationToSetHomeInTheEnd
     */
    @Override
    public boolean isRequireConfirmationToSetHomeInTheEnd() {
        return requireConfirmationToSetHomeInTheEnd;
    }

    /**
     * @param requireConfirmationToSetHomeInTheEnd the requireConfirmationToSetHomeInTheEnd to set
     */
    public void setRequireConfirmationToSetHomeInTheEnd(boolean requireConfirmationToSetHomeInTheEnd) {
        this.requireConfirmationToSetHomeInTheEnd = requireConfirmationToSetHomeInTheEnd;
    }

    /**
     * @return the deathsCounted
     */
    @Override
    public boolean isDeathsCounted() {
        return deathsCounted;
    }

    /**
     * @param deathsCounted the deathsCounted to set
     */
    public void setDeathsCounted(boolean deathsCounted) {
        this.deathsCounted = deathsCounted;
    }

    /**
     * @return the deathsMax
     */
    @Override
    public int getDeathsMax() {
        return deathsMax;
    }

    /**
     * @param deathsMax the deathsMax to set
     */
    public void setDeathsMax(int deathsMax) {
        this.deathsMax = deathsMax;
    }

    /**
     * @return the deathsSumTeam
     */
    public boolean isDeathsSumTeam() {
        return deathsSumTeam;
    }

    /**
     * @param deathsSumTeam the deathsSumTeam to set
     */
    public void setDeathsSumTeam(boolean deathsSumTeam) {
        this.deathsSumTeam = deathsSumTeam;
    }

    /**
     * @return the teamJoinDeathReset
     */
    @Override
    public boolean isTeamJoinDeathReset() {
        return teamJoinDeathReset;
    }

    /**
     * @param teamJoinDeathReset the teamJoinDeathReset to set
     */
    public void setTeamJoinDeathReset(boolean teamJoinDeathReset) {
        this.teamJoinDeathReset = teamJoinDeathReset;
    }

    /**
     * @return the geoLimitSettings
     */
    @Override
    public List<String> getGeoLimitSettings() {
        return geoLimitSettings;
    }

    /**
     * @param geoLimitSettings the geoLimitSettings to set
     */
    public void setGeoLimitSettings(List<String> geoLimitSettings) {
        this.geoLimitSettings = geoLimitSettings;
    }

    /**
     * @return the ivSettings
     */
    @Override
    public List<String> getIvSettings() {
        return ivSettings;
    }

    /**
     * @param ivSettings the ivSettings to set
     */
    public void setIvSettings(List<String> ivSettings) {
        this.ivSettings = ivSettings;
    }

    /**
     * @return the resetEpoch
     */
    @Override
    public long getResetEpoch() {
        return resetEpoch;
    }

    /**
     * @param resetEpoch the resetEpoch to set
     */
    @Override
    public void setResetEpoch(long resetEpoch) {
        this.resetEpoch = resetEpoch;
    }

    /**
     * @return the uniqueId
     */
    @Override
    public String getUniqueId() {
        return uniqueId;
    }

    /**
     * @param uniqueId the uniqueId to set
     */
    @Override
    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }

    @Override
    public int getIslandXOffset() {
        return 0;
    }

    @Override
    public int getIslandZOffset() {
        return 0;
    }

    @Override
    public int getMaxHomes() {
        return 0;
    }

    @Override
    public int getMaxIslands() {
        return -1;
    }

    @Override
    public int getNetherSpawnRadius() {
        return 0;
    }

    @Override
    public String getPermissionPrefix() {
        return "skygrid";
    }

    @Override
    public int getSeaHeight() {
        return 0;
    }

    @Override
    public boolean isDragonSpawn() {
        return false;
    }

    @Override
    public boolean isEndIslands() {
        return true;
    }

    @Override
    public boolean isNetherIslands() {
        return true;
    }

    @Override
    public boolean isUseOwnGenerator() {
        return true;
    }

    @Override
    public boolean isWaterUnsafe() {
        return false;
    }


}
