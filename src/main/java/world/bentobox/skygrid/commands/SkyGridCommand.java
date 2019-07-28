package world.bentobox.skygrid.commands;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import world.bentobox.bentobox.api.addons.GameModeAddon;
import world.bentobox.bentobox.api.commands.CompositeCommand;
import world.bentobox.bentobox.api.commands.island.IslandBanCommand;
import world.bentobox.bentobox.api.commands.island.IslandBanlistCommand;
import world.bentobox.bentobox.api.commands.island.IslandExpelCommand;
import world.bentobox.bentobox.api.commands.island.IslandInfoCommand;
import world.bentobox.bentobox.api.commands.island.IslandLanguageCommand;
import world.bentobox.bentobox.api.commands.island.IslandResetnameCommand;
import world.bentobox.bentobox.api.commands.island.IslandSethomeCommand;
import world.bentobox.bentobox.api.commands.island.IslandSetnameCommand;
import world.bentobox.bentobox.api.commands.island.IslandSettingsCommand;
import world.bentobox.bentobox.api.commands.island.IslandSpawnCommand;
import world.bentobox.bentobox.api.commands.island.IslandUnbanCommand;
import world.bentobox.bentobox.api.commands.island.team.IslandTeamCommand;
import world.bentobox.bentobox.api.events.island.IslandEvent.Reason;
import world.bentobox.bentobox.api.user.User;
import world.bentobox.bentobox.managers.island.NewIsland;
import world.bentobox.skygrid.SkyGrid;

public class SkyGridCommand extends CompositeCommand {

    public SkyGridCommand(SkyGrid addon) {
        super(addon,
                addon.getSettings().getIslandCommand().split(" ")[0],
                addon.getSettings().getIslandCommand().split(" "));
    }

    /* (non-Javadoc)
     * @see us.tastybento.bskyblock.api.commands.CompositeCommand#setup()
     */
    @Override
    public void setup() {
        setDescription("commands.skygrid.help.description");
        setOnlyPlayer(true);
        // Permission
        setPermission("skygrid");
        // Set up subcommands
        new IslandInfoCommand(this);
        new ResetCommand(this);
        new IslandSetnameCommand(this);
        new IslandResetnameCommand(this);
        new IslandSethomeCommand(this);
        new IslandSettingsCommand(this);
        new IslandLanguageCommand(this);
        new IslandBanCommand(this);
        new IslandUnbanCommand(this);
        new IslandBanlistCommand(this);
        new IslandSpawnCommand(this);
        new IslandExpelCommand(this);
        // Team commands
        new IslandTeamCommand(this);
        // SkyGrid sub commands
        new GoCommand(this);
    }

    /* (non-Javadoc)
     * @see us.tastybento.bskyblock.api.commands.CommandArgument#execute(org.bukkit.command.CommandSender, java.lang.String[])
     */
    @Override
    public boolean execute(User user, String label, List<String> args) {
        if (user == null) {
            return false;
        }
        if (args.isEmpty()) {
            // Known player, go
            if (getIslands().getIsland(getWorld(), user.getUniqueId()) != null) {
                return getSubCommand("go").map(goCmd -> goCmd.call(user, goCmd.getLabel(), new ArrayList<>())).orElse(false);
            }
            try {
                NewIsland.builder()
                .player(user)
                .addon((GameModeAddon) getAddon())
                .reason(Reason.CREATE)
                .noPaste()
                .build();
                return true;
            } catch (IOException e) {
                getPlugin().logError("Could not create island for player. " + e.getMessage());
                user.sendMessage("skygrid.errors.unable-to-start");
                return false;
            }
        } else {
            this.showHelp(this, user);
        }
        return false;

    }

}
