/**
 *
 */
package world.bentobox.skygrid.commands;

import java.util.List;

import org.apache.commons.lang.math.NumberUtils;

import world.bentobox.bentobox.api.commands.CompositeCommand;
import world.bentobox.bentobox.api.commands.island.CustomIslandMultiHomeHelp;
import world.bentobox.bentobox.api.localization.TextVariables;
import world.bentobox.bentobox.api.user.User;
import world.bentobox.bentobox.lists.Flags;

/**
 * @author tastybento
 */
public class GoCommand extends CompositeCommand {

    public GoCommand(CompositeCommand islandCommand) {
        super(islandCommand, "go");
    }

    @Override
    public void setup() {
        setPermission("skygrid.go");
        setOnlyPlayer(true);
        setDescription("commands.skygrid.go.description");
        new CustomIslandMultiHomeHelp(this);
    }

    @Override
    public boolean execute(User user, String label, List<String> args) {
        if ((getIWM().inWorld(user.getWorld()) && Flags.PREVENT_TELEPORT_WHEN_FALLING.isSetForWorld(user.getWorld()))
                && user.getPlayer().getFallDistance() > 0) {
            // We're sending the "hint" to the player to tell them they cannot teleport while falling.
            user.sendMessage(Flags.PREVENT_TELEPORT_WHEN_FALLING.getHintReference());
            return true;
        }
        if (!args.isEmpty() && NumberUtils.isDigits(args.get(0))) {
            int homeValue = Integer.parseInt(args.get(0));
            int maxHomes = user.getPermissionValue(getPermissionPrefix() + "skygrid.maxhomes", getIWM().getMaxHomes(getWorld()));
            if (homeValue > 1 && homeValue <= maxHomes) {
                getIslands().homeTeleport(getWorld(), user.getPlayer(), homeValue);
                user.sendMessage("commands.skygrid.go.tip", TextVariables.LABEL, getTopLabel());
                return true;
            }
        }
        getIslands().homeTeleport(getWorld(), user.getPlayer());
        return true;
    }

}