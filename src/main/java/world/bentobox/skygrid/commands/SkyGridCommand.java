package world.bentobox.skygrid.commands;

import java.util.List;

import world.bentobox.bentobox.api.commands.CompositeCommand;
import world.bentobox.bentobox.api.commands.island.IslandLanguageCommand;
import world.bentobox.bentobox.api.commands.island.IslandSettingsCommand;
import world.bentobox.bentobox.api.localization.TextVariables;
import world.bentobox.bentobox.api.user.User;
import world.bentobox.skygrid.SkyGrid;

public class SkyGridCommand extends CompositeCommand {

    public SkyGridCommand(SkyGrid addon) {
        super(addon, "skygrid", "sg");
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
        new IslandSettingsCommand(this);
        new IslandLanguageCommand(this);
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
            // Send player to their home or a start point
            // TODO send player to a starting position
            return true;
        }
        user.sendMessage("general.errors.unknown-command", TextVariables.LABEL, getTopLabel());
        return false;

    }

}
