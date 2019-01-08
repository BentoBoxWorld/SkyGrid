package world.bentobox.skygrid;

import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.WorldType;

import world.bentobox.bentobox.api.addons.GameModeAddon;
import world.bentobox.bentobox.api.configuration.Config;
import world.bentobox.bentobox.api.configuration.WorldSettings;
import world.bentobox.skygrid.commands.AdminCommand;
import world.bentobox.skygrid.commands.SkyGridCommand;
import world.bentobox.skygrid.generators.SkyGridGen;
import world.bentobox.skygrid.generators.WorldStyles;

/**
 * Main SkyGrid class - provides skygrid
 * @author tastybento
 * @author Poslovitch
 */
public class SkyGrid extends GameModeAddon {

    private static final String NETHER = "_nether";
    private static final String THE_END = "_the_end";

    // Settings
    private Settings settings;
    private WorldStyles worldStyles;

    @Override
    public void onLoad() {
        // Save the default config from config.yml
        saveDefaultConfig();
        // Load settings from config.yml. This will check if there are any issues with it too.
        loadSettings();
    }

    private void loadSettings() {
        settings = new Config<>(this, Settings.class).loadConfigObject();
        if (settings == null) {
            // Disable
            logError("SkyGrid settings could not load! Addon disabled.");
            setState(State.DISABLED);
        }
        worldStyles = new WorldStyles(this);
    }

    @Override
    public void onEnable(){
        // Register commands
        new SkyGridCommand(this);
        new AdminCommand(this);
    }

    @Override
    public void onDisable() {
        // Save settings
        if (settings != null) {
            new Config<>(this, Settings.class).saveConfigObject(settings);
        }
    }

    @Override
    public void onReload() {
        loadSettings();
    }

    /**
     * @return the settings
     */
    public Settings getSettings() {
        return settings;
    }

    @Override
    public void createWorlds() {
        String worldName = settings.getWorldName();
        if (getServer().getWorld(worldName) == null) {
            getLogger().info("Creating SkyGrid world ...");
        }
        // Create the world if it does not exist
        islandWorld = WorldCreator.name(worldName).type(WorldType.FLAT).environment(World.Environment.NORMAL).generator(new SkyGridGen(this))
                .createWorld();

        // Make the nether if it does not exist
        if (settings.isNetherGenerate()) {
            if (getServer().getWorld(worldName + NETHER) == null) {
                log("Creating SkyGrid's Nether...");
            }
            if (!settings.isNetherIslands()) {
                netherWorld = WorldCreator.name(worldName + NETHER).type(WorldType.NORMAL).environment(World.Environment.NETHER).createWorld();
            } else {
                netherWorld = WorldCreator.name(worldName + NETHER).type(WorldType.FLAT).generator(new SkyGridGen(this))
                        .environment(World.Environment.NETHER).createWorld();
            }
        }
        // Make the end if it does not exist
        if (settings.isEndGenerate()) {
            if (getServer().getWorld(worldName + THE_END) == null) {
                log("Creating SkyGrid's End World...");
            }
            if (!settings.isEndIslands()) {
                endWorld = WorldCreator.name(worldName + THE_END).type(WorldType.NORMAL).environment(World.Environment.THE_END).createWorld();
            } else {
                endWorld = WorldCreator.name(worldName + THE_END).type(WorldType.FLAT).generator(new SkyGridGen(this))
                        .environment(World.Environment.THE_END).createWorld();
            }
        }
    }

    @Override
    public WorldSettings getWorldSettings() {
        return settings;
    }

    public WorldStyles getWorldStyles() {
        return worldStyles;
    }
}
