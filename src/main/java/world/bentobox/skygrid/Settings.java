package world.bentobox.skygrid;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import org.bukkit.Difficulty;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;

import world.bentobox.bentobox.BentoBox;
import world.bentobox.bentobox.api.configuration.ConfigComment;
import world.bentobox.bentobox.api.configuration.ConfigEntry;
import world.bentobox.bentobox.api.configuration.StoreAt;
import world.bentobox.bentobox.api.configuration.WorldSettings;
import world.bentobox.bentobox.api.flags.Flag;
import world.bentobox.bentobox.database.objects.adapters.Adapter;
import world.bentobox.bentobox.database.objects.adapters.FlagBooleanSerializer;

/**
 * All the settings are here
 * @author tastybento
 */
@StoreAt(filename="config.yml", path="addons/SkyGrid") // Explicitly call out what name this should have.
@ConfigComment("SkyGrid Configuration [version]")
@ConfigComment("")
public class Settings implements WorldSettings {

    /* Commands */
    @ConfigComment("Island Command. What command users will run to access their island.")
    @ConfigComment("To define alias, just separate commands with white space.")
    @ConfigEntry(path = "skygrid.command.island")
    private String playerCommandAliases = "skygrid sg";

    @ConfigComment("The island admin command.")
    @ConfigComment("To define alias, just separate commands with white space.")
    @ConfigEntry(path = "skygrid.command.admin")
    private String adminCommandAliases = "sgadmin sga";

    @ConfigComment("The default action for new player command call.")
    @ConfigComment("Sub-command of main player command that will be run on first player command call.")
    @ConfigComment("By default it is sub-command 'create'.")
    @ConfigEntry(path = "skygrid.command.new-player-action", since = "1.16.0")
    private String defaultNewPlayerAction = "create";

    @ConfigComment("The default action for player command.")
    @ConfigComment("Sub-command of main player command that will be run on each player command call.")
    @ConfigComment("By default it is sub-command 'go'.")
    @ConfigEntry(path = "skygrid.command.default-action", since = "1.16.0")
    private String defaultPlayerAction = "go";

    /* Chests */
    @ConfigComment("Number of unique items per chest")
    @ConfigEntry(path = "world.chest-fill.overworld")
    private int chestFill = 5;
    @ConfigComment("Nether chest fill setting")
    @ConfigEntry(path = "world.chest-fill.nether")
    private int chestFillNether = 5;
    @ConfigComment("The End chest fill setting")
    @ConfigEntry(path = "world.chest-fill.end")
    private int chestFillEnd = 5;
    @ConfigComment("Chest items will be taken randomly from this list according to the relative probabilities given")
    @ConfigComment("Format: Material : Probability")
    @ConfigComment("Material values can be found at https://hub.spigotmc.org/javadocs/spigot/org/bukkit/Material.html")
    @ConfigEntry(path = "world.chest-items.overworld")
    private Map<Material, Integer> chestItemsOverworld = new EnumMap<>(Material.class);
    @ConfigEntry(path = "world.chest-items.nether")
    private Map<Material, Integer> chestItemsNether = new EnumMap<>(Material.class);
    @ConfigEntry(path = "world.chest-items.end")
    private Map<Material, Integer> chestItemsEnd = new EnumMap<>(Material.class);

    /* Blocks */
    @ConfigComment("World block types. If the material cannot be placed, bedrock will be used.")
    @ConfigComment("Format: Material : Probability")
    @ConfigComment("Block types must be Bukkit Material types.")
    @ConfigComment("Chests have different items in them in different world types.")
    @ConfigComment("Over world blocks. Beware of making too many chests, they can lag a lot.")
    @ConfigEntry(path = "world.blocks")
    private Map<Material, Integer> blocks = new EnumMap<>(Material.class);

    // Nether
    @ConfigComment("Generate SkyGrid Nether - if this is false, the nether world will not be made")
    @ConfigEntry(path = "world.nether.generate")
    private boolean netherGenerate = true;

    @ConfigComment("Nether block types")
    @ConfigComment("Beware with glowstone and lava - the lighting calcs will lag the")
    @ConfigComment("server badly if there are too many blocks.")
    @ConfigEntry(path = "world.nether.blocks")
    private Map<Material, Integer> netherBlocks = new EnumMap<>(Material.class);

    @ConfigComment("This option indicates if nether portals should be linked via dimensions.")
    @ConfigComment("Option will simulate vanilla portal mechanics that links portals together")
    @ConfigComment("or creates a new portal, if there is not a portal in that dimension.")
    @ConfigEntry(path = "world.nether.create-and-link-portals", since = "1.16")
    private boolean makeNetherPortals = false;

    // End
    @ConfigComment("Generate SkyGrid End - if this is false, the end world will not be made")
    @ConfigEntry(path = "world.end.generate")
    private boolean endGenerate = true;

    @ConfigComment("The End blocks.")
    @ConfigEntry(path = "world.end.blocks")
    private Map<Material, Integer> endBlocks = new EnumMap<>(Material.class);

    @ConfigComment("This option indicates if obsidian platform in the end should be generated")
    @ConfigComment("when player enters the end world.")
    @ConfigEntry(path = "world.end.create-obsidian-platform", since = "1.16")
    private boolean makeEndPortals = true;

    /* SkyGrid */
    @ConfigComment("The probability of a frame being created in a chunk. Frames are always at y=0.")
    @ConfigEntry(path = "world.end-frame-probability")
    private double endFrameProb = 0.1;

    /*      WORLD       */
    @ConfigComment("Friendly name for this world. Used in admin commands. Must be a single word")
    @ConfigEntry(path = "world.friendly-name")
    private String friendlyName = "SkyGrid";

    @ConfigComment("Name of the world - if it does not exist then it will be generated.")
    @ConfigComment("It acts like a prefix for nether and end")
    @ConfigEntry(path = "world.world-name")
    private String worldName = "skygrid-world";

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
    @ConfigComment("This is the height of the top sky grid layer. 255 max.")
    @ConfigEntry(path = "world.skygrid-height")
    private int islandHeight = 128;

    @ConfigComment("The number of concurrent areas a player can have")
    @ConfigComment("A value of 0 will use the BentoBox config.yml default")
    @ConfigEntry(path = "world.concurrent-area")
    private int concurrentIslands = 0;

    @ConfigComment("Disallow team members from having their own area.")
    @ConfigEntry(path = "world.disallow-team-member-areas")
    private boolean disallowTeamMemberIslands = true;

    @ConfigComment("End Frame height")
    @ConfigComment("This is the height where end frames will generate.")
    @ConfigEntry(path = "world.end-frame-height", since = "1.20.0")
    private int endFrameHeight = 3;

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
    private Map<String, Integer> defaultIslandFlagNames = new HashMap<>();

    @ConfigComment("These are the default settings for new protected areas")
    @ConfigEntry(path = "world.default-settings")
    @Adapter(FlagBooleanSerializer.class)
    private Map<String, Integer> defaultIslandSettingNames = new HashMap<>();

    @ConfigComment("These settings/flags are hidden from users")
    @ConfigComment("Ops can toggle hiding in-game using SHIFT-LEFT-CLICK on flags in settings")
    @ConfigEntry(path = "world.hidden-flags")
    private List<String> hiddenFlags = new ArrayList<>();

    @ConfigComment("Visitor banned commands - Visitors to islands cannot use these commands in this world")
    @ConfigEntry(path = "world.visitor-banned-commands")
    private List<String> visitorBannedCommands = new ArrayList<>();

    @ConfigComment("Falling banned commands - players cannot use these commands when falling")
    @ConfigComment("if the PREVENT_TELEPORT_WHEN_FALLING world setting flag is active")
    @ConfigEntry(path = "world.falling-banned-commands")
    private List<String> fallingBannedCommands = new ArrayList<>();

    // ---------------------------------------------

    /*      PROTECTED AREA      */
    @ConfigComment("Default max team size")
    @ConfigComment("Permission size cannot be less than the default below. ")
    @ConfigEntry(path = "area.max-team-size")
    private int maxTeamSize = 4;

    @ConfigComment("Default maximum number of coop rank members per area")
    @ConfigComment("Players can have the skygrid.coop.maxsize.<number> permission to be bigger but")
    @ConfigComment("permission size cannot be less than the default below. ")
    @ConfigEntry(path = "area.max-coop-size", since = "1.13.0")
    private int maxCoopSize = 4;

    @ConfigComment("Default maximum number of trusted rank members per area")
    @ConfigComment("Players can have the skygrid.trust.maxsize.<number> permission to be bigger but")
    @ConfigComment("permission size cannot be less than the default below. ")
    @ConfigEntry(path = "area.max-trusted-size", since = "1.13.0")
    private int maxTrustSize = 4;

    @ConfigComment("Default maximum number of homes a player can have. Min = 1")
    @ConfigComment("Accessed via /sg sethome <number> or /sg go <number>")
    @ConfigEntry(path = "area.max-homes")
    private int maxHomes = 5;

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
    @ConfigComment("Overrides the on-leave inventory reset for kicked players.")
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

    @ConfigComment("Reset health - if true, the player's health will be reset.")
    @ConfigEntry(path = "area.reset.on-join.health")
    private boolean onJoinResetHealth = true;

    @ConfigComment("Reset hunger - if true, the player's hunger will be reset.")
    @ConfigEntry(path = "area.reset.on-join.hunger")
    private boolean onJoinResetHunger = true;

    @ConfigComment("Reset experience points - if true, the player's experience will be reset.")
    @ConfigEntry(path = "area.reset.on-join.exp")
    private boolean onJoinResetXP = false;

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

    @ConfigComment("Reset health - if true, the player's health will be reset.")
    @ConfigEntry(path = "area.reset.on-leave.health")
    private boolean onLeaveResetHealth = false;

    @ConfigComment("Reset hunger - if true, the player's hunger will be reset.")
    @ConfigEntry(path = "area.reset.on-leave.hunger")
    private boolean onLeaveResetHunger = false;

    @ConfigComment("Reset experience - if true, the player's experience will be reset.")
    @ConfigEntry(path = "area.reset.on-leave.exp")
    private boolean onLeaveResetXP = false;

    @ConfigComment("Reset Ender Chest - if true, the player's Ender Chest will be cleared.")
    @ConfigEntry(path = "area.reset.on-leave.ender-chest")
    private boolean onLeaveResetEnderChest = false;

    @ConfigComment("Toggles the automatic area creation upon the player's first login on your server.")
    @ConfigComment("If set to true,")
    @ConfigComment("   * Upon connecting to your server for the first time, the player will be told that")
    @ConfigComment("    an area will be created for him.")
    @ConfigComment("  * An area will be created for the player without needing him to run the create command.")
    @ConfigComment("If set to false, this will disable this feature entirely.")
    @ConfigComment("Warning:")
    @ConfigComment("  * If you are running multiple gamemodes on your server, and all of them have")
    @ConfigComment("    this feature enabled, an area in all the gamemodes will be created simultaneously.")
    @ConfigComment("    However, it is impossible to know on which area the player will be teleported to afterwards.")
    @ConfigComment("  * Area creation can be resource-intensive, please consider the options below to help mitigate")
    @ConfigComment("    the potential issues, especially if you expect a lot of players to connect to your server")
    @ConfigComment("    in a limited period of time.")
    @ConfigEntry(path = "area.create-area-on-first-login.enable")
    private boolean createIslandOnFirstLoginEnabled;

    @ConfigComment("Time in seconds after the player logged in, before his area gets created.")
    @ConfigComment("If set to 0 or less, the island will be created directly upon the player's login.")
    @ConfigComment("It is recommended to keep this value under a minute's time.")
    @ConfigEntry(path = "area.create-area-on-first-login.delay")
    private int createIslandOnFirstLoginDelay = 5;

    @ConfigComment("Toggles whether the area creation should be aborted if the player logged off while the")
    @ConfigComment("delay (see the option above) has not worn off yet.")
    @ConfigComment("If set to true,")
    @ConfigComment("  * If the player has logged off the server while the delay (see the option above) has not")
    @ConfigComment("    worn off yet, this will cancel the area creation.")
    @ConfigComment("  * If the player relogs afterward, since he will not be recognized as a new player, no area")
    @ConfigComment("    would be created for him.")
    @ConfigComment("  * If the area creation started before the player logged off, it will continue.")
    @ConfigComment("If set to false, the player's area will be created even if he went offline in the meantime.")
    @ConfigComment("Note this option has no effect if the delay (see the option above) is set to 0 or less.")
    @ConfigEntry(path = "area.create-area-on-first-login.abort-on-logout")
    private boolean createIslandOnFirstLoginAbortOnLogout = true;

    // Commands
    @ConfigComment("List of commands to run when a player joins.")
    @ConfigComment("These commands are run by the console, unless otherwise stated using the [SUDO] prefix,")
    @ConfigComment("in which case they are executed by the player.")
    @ConfigComment("")
    @ConfigComment("Available placeholders for the commands are the following:")
    @ConfigComment("   * [name]: name of the player")
    @ConfigComment("")
    @ConfigComment("Here are some examples of valid commands to execute:")
    @ConfigComment("   * '[SUDO] bbox version'")
    @ConfigComment("   * 'bsbadmin deaths set [player] 0'")
    @ConfigComment("")
    @ConfigComment("Note that player-executed commands might not work, as these commands can be run with said player being offline.")
    @ConfigEntry(path = "area.commands.on-join")
    private List<String> onJoinCommands = new ArrayList<>();

    @ConfigComment("list of commands to run when a player leaves.")
    @ConfigComment("These commands are run by the console, unless otherwise stated using the [SUDO] prefix,")
    @ConfigComment("in which case they are executed by the player.")
    @ConfigComment("")
    @ConfigComment("Available placeholders for the commands are the following:")
    @ConfigComment("   * [name]: name of the player")
    @ConfigComment("")
    @ConfigComment("Here are some examples of valid commands to execute:")
    @ConfigComment("   * '[SUDO] bbox version'")
    @ConfigComment("   * 'bsbadmin deaths set [player] 0'")
    @ConfigComment("")
    @ConfigComment("Note that player-executed commands might not work, as these commands can be run with said player being offline.")
    @ConfigEntry(path = "area.commands.on-leave")
    private List<String> onLeaveCommands = new ArrayList<>();

    @ConfigComment("List of commands that should be executed when the player respawns after death if Flags.ISLAND_RESPAWN is true.")
    @ConfigComment("These commands are run by the console, unless otherwise stated using the [SUDO] prefix,")
    @ConfigComment("in which case they are executed by the player.")
    @ConfigComment("")
    @ConfigComment("Available placeholders for the commands are the following:")
    @ConfigComment("   * [name]: name of the player")
    @ConfigComment("")
    @ConfigComment("Here are some examples of valid commands to execute:")
    @ConfigComment("   * '[SUDO] bbox version'")
    @ConfigComment("   * 'bsbadmin deaths set [player] 0'")
    @ConfigComment("")
    @ConfigComment("Note that player-executed commands might not work, as these commands can be run with said player being offline.")
    @ConfigEntry(path = "area.commands.on-respawn", since = "1.14.0")
    private List<String> onRespawnCommands = new ArrayList<>();

    // Sethome
    @ConfigComment("Allow setting home in the nether. Only available on nether islands, not vanilla nether.")
    @ConfigEntry(path = "area.sethome.nether.allow")
    private boolean allowSetHomeInNether = true;

    @ConfigEntry(path = "area.sethome.nether.require-confirmation")
    private boolean requireConfirmationToSetHomeInNether = true;

    @ConfigComment("Allow setting home in the end. Only available on end islands, not vanilla end.")
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

    @ConfigComment("When a player joins a team, reset their death count")
    @ConfigEntry(path = "area.deaths.team-join-reset")
    private boolean teamJoinDeathReset = true;

    @ConfigComment("Reset player death count when they start reset")
    @ConfigEntry(path = "area.deaths.reset-on-new-island")
    private boolean deathsResetOnNewIsland = true;

    // ---------------------------------------------
    /*      PROTECTION      */

    @ConfigComment("Geo restrict mobs.")
    @ConfigComment("Mobs that exit the protected space where they were spawned will be removed.")
    @ConfigEntry(path = "protection.geo-limit-settings")
    private List<String> geoLimitSettings = new ArrayList<>();

    @ConfigComment("SkyGrid blocked mobs.")
    @ConfigComment("List of mobs that should not spawn in SkyGrid.")
    @ConfigEntry(path = "protection.block-mobs")
    private List<String> mobLimitSettings = new ArrayList<>();

    // Invincible visitor settings
    @ConfigComment("Invincible visitors. List of damages that will not affect visitors.")
    @ConfigComment("Make list blank if visitors should receive all damages")
    @ConfigEntry(path = "protection.invincible-visitors")
    private List<String> ivSettings = new ArrayList<>();

    //---------------------------------------------------------------------------------------/


    @ConfigComment("These settings should not be edited")
    @ConfigEntry(path = "do-not-edit-these-settings.reset-epoch")
    private long resetEpoch = 0;

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
        if (islandHeight > 255) islandHeight = 255;
        else if (islandHeight < -64) islandHeight = -64;
        return islandHeight;
    }

    /**
     * @param islandHeight the islandHeight to set
     */
    public void setIslandHeight(int islandHeight) {
        if (islandHeight > 255) islandHeight = 255;
        else if (islandHeight < -64) islandHeight = -64;
        this.islandHeight = islandHeight;
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
     * @deprecated since 1.21
     */
    @Deprecated
    @Override
    public Map<Flag, Integer> getDefaultIslandFlags() {
        return Collections.emptyMap();
    }


    /**
     * @return defaultIslandFlagNames
     */
    @Override
    public Map<String, Integer> getDefaultIslandFlagNames()
    {
        return defaultIslandFlagNames;
    }


    /**
     * @param defaultIslandFlags the defaultIslandFlags to set
     */
    public void setDefaultIslandFlagNames(Map<String, Integer> defaultIslandFlags) {
        this.defaultIslandFlagNames = defaultIslandFlags;
    }

    /**
     * @return the defaultIslandSettings
     * @deprecated since 1.21
     */
    @Deprecated
    @Override
    public Map<Flag, Integer> getDefaultIslandSettings() {
        return Collections.emptyMap();
    }


    /**
     * @return defaultIslandSettingNames
     */
    @Override
    public Map<String, Integer> getDefaultIslandSettingNames()
    {
        return defaultIslandSettingNames;
    }


    /**
     * @param defaultIslandSettings the defaultIslandSettings to set
     */
    public void setDefaultIslandSettingNames(Map<String, Integer> defaultIslandSettings) {
        this.defaultIslandSettingNames = defaultIslandSettings;
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
    public void setHiddenFlags(List<String> hiddenFlags) {
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
     * Optional list of commands that are banned when falling. Not applicable to all game modes so defaults to empty.
     *
     * @return the fallingBannedCommands
     * @since 1.8.0
     */
    @Override
    public List<String> getFallingBannedCommands() {
        return fallingBannedCommands;
    }

    /**
     * @param fallingBannedCommands the fallingBannedCommands to set
     */
    public void setFallingBannedCommands(List<String> fallingBannedCommands) {
        this.fallingBannedCommands = fallingBannedCommands;
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
    @Override
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
    @Override
    public boolean isKickedKeepInventory() {
        return kickedKeepInventory;
    }

    /**
     * This method returns the createIslandOnFirstLoginEnabled boolean value.
     * @return the createIslandOnFirstLoginEnabled value
     * @since 1.9.0
     */
    @Override
    public boolean isCreateIslandOnFirstLoginEnabled()
    {
        return createIslandOnFirstLoginEnabled;
    }

    /**
     * This method returns the createIslandOnFirstLoginDelay int value.
     * @return the createIslandOnFirstLoginDelay value
     * @since 1.9.0
     */
    @Override
    public int getCreateIslandOnFirstLoginDelay()
    {
        return createIslandOnFirstLoginDelay;
    }

    /**
     * This method returns the createIslandOnFirstLoginAbortOnLogout boolean value.
     * @return the createIslandOnFirstLoginAbortOnLogout value
     * @since 1.9.0
     */
    @Override
    public boolean isCreateIslandOnFirstLoginAbortOnLogout()
    {
        return createIslandOnFirstLoginAbortOnLogout;
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
        return maxHomes;
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
        return false;
    }

    @Override
    public boolean isWaterUnsafe() {
        return false;
    }

    /**
     * @return the playerCommandAliases
     */
    @Override
    public String getPlayerCommandAliases() {
        return playerCommandAliases;
    }

    /**
     * @param playerCommandAliases the playerCommandAliases to set
     */
    public void setPlayerCommandAliases(String playerCommandAliases) {
        this.playerCommandAliases = playerCommandAliases;
    }

    /**
     * @return the adminCommandAliases
     */
    @Override
    public String getAdminCommandAliases() {
        return adminCommandAliases;
    }

    /**
     * @param adminCommandAliases the adminCommandAliases to set
     */
    public void setAdminCommandAliases(String adminCommandAliases) {
        this.adminCommandAliases = adminCommandAliases;
    }

    /**
     * @return the chestFill
     */
    public int getChestFill() {
        return chestFill;
    }

    /**
     * @param chestFill the chestFill to set
     */
    public void setChestFill(int chestFill) {
        this.chestFill = chestFill;
    }

    /**
     * @return the chestFillNether
     */
    public int getChestFillNether() {
        return chestFillNether;
    }

    /**
     * @param chestFillNether the chestFillNether to set
     */
    public void setChestFillNether(int chestFillNether) {
        this.chestFillNether = chestFillNether;
    }

    /**
     * @return the chestFillEnd
     */
    public int getChestFillEnd() {
        return chestFillEnd;
    }

    /**
     * @param chestFillEnd the chestFillEnd to set
     */
    public void setChestFillEnd(int chestFillEnd) {
        this.chestFillEnd = chestFillEnd;
    }

    /**
     * @return the chestItemsOverworld
     */
    public Map<Material, Integer> getChestItemsOverworld() {
        return chestItemsOverworld;
    }

    /**
     * @param chestItemsOverworld the chestItemsOverworld to set
     */
    public void setChestItemsOverworld(Map<Material, Integer> chestItemsOverworld) {
        this.chestItemsOverworld = chestItemsOverworld;
    }

    /**
     * @return the chestItemsNether
     */
    public Map<Material, Integer> getChestItemsNether() {
        return chestItemsNether;
    }

    /**
     * @param chestItemsNether the chestItemsNether to set
     */
    public void setChestItemsNether(Map<Material, Integer> chestItemsNether) {
        this.chestItemsNether = chestItemsNether;
    }

    /**
     * @return the chestItemsEnd
     */
    public Map<Material, Integer> getChestItemsEnd() {
        return chestItemsEnd;
    }

    /**
     * @param chestItemsEnd the chestItemsEnd to set
     */
    public void setChestItemsEnd(Map<Material, Integer> chestItemsEnd) {
        this.chestItemsEnd = chestItemsEnd;
    }

    /**
     * @return the deathsResetOnNewIsland
     */
    @Override
    public boolean isDeathsResetOnNewIsland() {
        return deathsResetOnNewIsland;
    }

    /**
     * @param deathsResetOnNewIsland the deathsResetOnNewIsland to set
     */
    public void setDeathsResetOnNewIsland(boolean deathsResetOnNewIsland) {
        this.deathsResetOnNewIsland = deathsResetOnNewIsland;
    }

    /**
     * @return the onJoinCommands
     */
    @Override
    public List<String> getOnJoinCommands() {
        return Objects.requireNonNullElseGet(onJoinCommands, ArrayList::new);
    }

    /**
     * @param onJoinCommands the onJoinCommands to set
     */
    public void setOnJoinCommands(List<String> onJoinCommands) {
        this.onJoinCommands = onJoinCommands;
    }

    /**
     * @return the onLeaveCommands
     */
    @Override
    public List<String> getOnLeaveCommands() {
        return Objects.requireNonNullElseGet(onLeaveCommands, ArrayList::new);
    }

    /**
     * @param onLeaveCommands the onLeaveCommands to set
     */
    public void setOnLeaveCommands(List<String> onLeaveCommands) {
        this.onLeaveCommands = onLeaveCommands;
    }

    /**
     * @return the onRespawnCommands
     */
    @Override
    public List<String> getOnRespawnCommands() {
        return Objects.requireNonNullElseGet(onRespawnCommands, ArrayList::new);
    }

    /**
     * Sets on respawn commands.
     *
     * @param onRespawnCommands the on respawn commands
     */
    public void setOnRespawnCommands(List<String> onRespawnCommands) {
        this.onRespawnCommands = onRespawnCommands;
    }

    /**
     * @return the onJoinResetHealth
     */
    @Override
    public boolean isOnJoinResetHealth() {
        return onJoinResetHealth;
    }

    /**
     * @param onJoinResetHealth the onJoinResetHealth to set
     */
    public void setOnJoinResetHealth(boolean onJoinResetHealth) {
        this.onJoinResetHealth = onJoinResetHealth;
    }

    /**
     * @return the onJoinResetHunger
     */
    @Override
    public boolean isOnJoinResetHunger() {
        return onJoinResetHunger;
    }

    /**
     * @param onJoinResetHunger the onJoinResetHunger to set
     */
    public void setOnJoinResetHunger(boolean onJoinResetHunger) {
        this.onJoinResetHunger = onJoinResetHunger;
    }

    /**
     * @return the onJoinResetXP
     */
    @Override
    public boolean isOnJoinResetXP() {
        return onJoinResetXP;
    }

    /**
     * @param onJoinResetXP the onJoinResetXP to set
     */
    public void setOnJoinResetXP(boolean onJoinResetXP) {
        this.onJoinResetXP = onJoinResetXP;
    }

    /**
     * @return the onLeaveResetHealth
     */
    @Override
    public boolean isOnLeaveResetHealth() {
        return onLeaveResetHealth;
    }

    /**
     * @param onLeaveResetHealth the onLeaveResetHealth to set
     */
    public void setOnLeaveResetHealth(boolean onLeaveResetHealth) {
        this.onLeaveResetHealth = onLeaveResetHealth;
    }

    /**
     * @return the onLeaveResetHunger
     */
    @Override
    public boolean isOnLeaveResetHunger() {
        return onLeaveResetHunger;
    }

    /**
     * @param onLeaveResetHunger the onLeaveResetHunger to set
     */
    public void setOnLeaveResetHunger(boolean onLeaveResetHunger) {
        this.onLeaveResetHunger = onLeaveResetHunger;
    }

    /**
     * @return the onLeaveResetXP
     */
    @Override
    public boolean isOnLeaveResetXP() {
        return onLeaveResetXP;
    }

    /**
     * @param onLeaveResetXP the onLeaveResetXP to set
     */
    public void setOnLeaveResetXP(boolean onLeaveResetXP) {
        this.onLeaveResetXP = onLeaveResetXP;
    }

    /**
     * @param createIslandOnFirstLoginEnabled the createIslandOnFirstLoginEnabled to set
     */
    public void setCreateIslandOnFirstLoginEnabled(boolean createIslandOnFirstLoginEnabled)
    {
        this.createIslandOnFirstLoginEnabled = createIslandOnFirstLoginEnabled;
    }

    /**
     * @param createIslandOnFirstLoginDelay the createIslandOnFirstLoginDelay to set
     */
    public void setCreateIslandOnFirstLoginDelay(int createIslandOnFirstLoginDelay)
    {
        this.createIslandOnFirstLoginDelay = createIslandOnFirstLoginDelay;
    }

    /**
     * @param createIslandOnFirstLoginAbortOnLogout the createIslandOnFirstLoginAbortOnLogout to set
     */
    public void setCreateIslandOnFirstLoginAbortOnLogout(boolean createIslandOnFirstLoginAbortOnLogout)
    {
        this.createIslandOnFirstLoginAbortOnLogout = createIslandOnFirstLoginAbortOnLogout;
    }

    /**
     * @return the maxCoopSize
     */
    @Override
    public int getMaxCoopSize() {
        return maxCoopSize;
    }

    /**
     * @param maxCoopSize the maxCoopSize to set
     */
    public void setMaxCoopSize(int maxCoopSize) {
        this.maxCoopSize = maxCoopSize;
    }

    /**
     * @return the maxTrustSize
     */
    @Override
    public int getMaxTrustSize() {
        return maxTrustSize;
    }

    /**
     * @param maxTrustSize the maxTrustSize to set
     */
    public void setMaxTrustSize(int maxTrustSize) {
        this.maxTrustSize = maxTrustSize;
    }

    /**
     * @return the defaultNewPlayerAction
     */
    @Override
    public String getDefaultNewPlayerAction() {
        return defaultNewPlayerAction;
    }

    /**
     * @param defaultNewPlayerAction the defaultNewPlayerAction to set
     */
    public void setDefaultNewPlayerAction(String defaultNewPlayerAction) {
        this.defaultNewPlayerAction = defaultNewPlayerAction;
    }

    /**
     * @return the defaultPlayerAction
     */
    @Override
    public String getDefaultPlayerAction() {
        return defaultPlayerAction;
    }

    /**
     * @param defaultPlayerAction the defaultPlayerAction to set
     */
    public void setDefaultPlayerAction(String defaultPlayerAction) {
        this.defaultPlayerAction = defaultPlayerAction;
    }

    /**
     * @return the mobLimitSettings
     */
    @Override
    public List<String> getMobLimitSettings() {
        return mobLimitSettings;
    }

    /**
     * @param mobLimitSettings the mobLimitSettings to set
     */
    public void setMobLimitSettings(List<String> mobLimitSettings) {
        this.mobLimitSettings = mobLimitSettings;
    }

    /**
     * @return the makeNetherPortals
     */
    @Override
    public boolean isMakeNetherPortals() {
        return makeNetherPortals;
    }

    /**
     * @return the makeEndPortals
     */
    @Override
    public boolean isMakeEndPortals() {
        return makeEndPortals;
    }

    /**
     * Sets make nether portals.
     * @param makeNetherPortals the make nether portals
     */
    public void setMakeNetherPortals(boolean makeNetherPortals) {
        this.makeNetherPortals = makeNetherPortals;
    }

    /**
     * Sets make end portals.
     * @param makeEndPortals the make end portals
     */
    public void setMakeEndPortals(boolean makeEndPortals) {
        this.makeEndPortals = makeEndPortals;
    }

    /**
     * SkyGrid should not check for blocks.
     * @return false
     */
    @Override
    public boolean isCheckForBlocks()
    {
        return false;
    }

    /**
     * @param maxHomes the maxHomes to set
     */
    public void setMaxHomes(int maxHomes) {
        this.maxHomes = maxHomes;
    }

    /**
     * @return the endFrameHeight
     */
    public int getEndFrameHeight() {
        return endFrameHeight;
    }

    /**
     * @param endFrameHeight the endFrameHeight to set
     */
    public void setEndFrameHeight(int endFrameHeight) {
        this.endFrameHeight = endFrameHeight;
    }

    /**
     * @return the concurrentIslands
     */
    public int getConcurrentIslands() {
        if (concurrentIslands <= 0) {
            return BentoBox.getInstance().getSettings().getIslandNumber();
        }
        return concurrentIslands;
    }

    /**
     * @param concurrentIslands the concurrentIslands to set
     */
    public void setConcurrentIslands(int concurrentIslands) {
        this.concurrentIslands = concurrentIslands;
    }

    /**
     * @return the disallowTeamMemberIslands
     */
    public boolean isDisallowTeamMemberIslands() {
        return disallowTeamMemberIslands;
    }

    /**
     * @param disallowTeamMemberIslands the disallowTeamMemberIslands to set
     */
    public void setDisallowTeamMemberIslands(boolean disallowTeamMemberIslands) {
        this.disallowTeamMemberIslands = disallowTeamMemberIslands;
    }
}
