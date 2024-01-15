package world.bentobox.skygrid;

import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.generator.BiomeProvider;
import org.bukkit.generator.ChunkGenerator;
import org.eclipse.jdt.annotation.NonNull;

import world.bentobox.bentobox.api.addons.GameModeAddon;
import world.bentobox.bentobox.api.commands.admin.DefaultAdminCommand;
import world.bentobox.bentobox.api.commands.island.DefaultPlayerCommand;
import world.bentobox.bentobox.api.configuration.Config;
import world.bentobox.bentobox.api.configuration.WorldSettings;
import world.bentobox.bentobox.api.flags.Flag.Type;
import world.bentobox.bentobox.lists.Flags;
import world.bentobox.skygrid.generators.SkyGridBiomeProvider;
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
    private SkyGridGen gen;
    private final Config<Settings> configObject = new Config<>(this, Settings.class);

    @Override
    public void onLoad() {
        // Save the default config from config.yml
        saveDefaultConfig();
        // Load settings from config.yml. This will check if there are any issues with it too.
        loadSettings();
    }

    private void loadSettings() {
        settings = configObject.loadConfigObject();
        if (settings == null) {
            // Disable
            logError("SkyGrid settings could not load! Addon disabled.");
            setState(State.DISABLED);
            return;
        }
        saveWorldSettings();
        worldStyles = new WorldStyles(this);

        // Generator
        gen = new SkyGridGen(this);

        // Register commands
        playerCommand = new DefaultPlayerCommand(this) {};
        adminCommand = new DefaultAdminCommand(this) {};

    }

    @Override
    public void onEnable() {
        // Set default protection flags for world to allow everything
        Flags.values().stream().filter(f -> f.getType().equals(Type.PROTECTION)).forEach(f -> f.setDefaultSetting(getOverWorld(), true));
    }

    @Override
    public void onDisable() {
        // Nothing to do here
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
        String worldName = settings.getWorldName().toLowerCase();
        if (getServer().getWorld(worldName) == null) {
            getLogger().info("Creating SkyGrid world ...");
        }
        BiomeProvider bp = new SkyGridBiomeProvider();
        // Create the world if it does not exist
        islandWorld = WorldCreator.name(worldName).environment(World.Environment.NORMAL).generator(gen).seed(123456L)
                .biomeProvider(bp).createWorld();

        // Make the nether if it does not exist
        if (settings.isNetherGenerate()) {
            if (getServer().getWorld(worldName + NETHER) == null) {
                log("Creating SkyGrid's Nether...");
            }
            netherWorld = WorldCreator.name(worldName + NETHER).generator(gen).environment(World.Environment.NETHER)
                    .biomeProvider(bp).createWorld();
        }
        // Make the end if it does not exist
        if (settings.isEndGenerate()) {
            if (getServer().getWorld(worldName + THE_END) == null) {
                log("Creating SkyGrid's End World...");
            }
            endWorld = WorldCreator.name(worldName + THE_END).generator(gen).environment(World.Environment.THE_END)
                    .biomeProvider(bp).createWorld();
        }
    }

    @Override
    public WorldSettings getWorldSettings() {
        return getSettings();
    }

    public WorldStyles getWorldStyles() {
        return worldStyles;
    }

    @Override
    public @NonNull ChunkGenerator getDefaultWorldGenerator(String worldName, String id) {
        return gen;
    }

    @Override
    public void saveWorldSettings() {
        if (settings != null) {
            configObject.saveConfigObject(settings);
        }

    }

    @Override
    public boolean isUsesNewChunkGeneration() {
        return true;
    }
}
