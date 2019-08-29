package world.bentobox.skygrid.commands;

import java.util.List;

import org.apache.commons.lang.math.NumberUtils;

import world.bentobox.bentobox.api.commands.CompositeCommand;
import world.bentobox.bentobox.api.commands.DelayedTeleportCommand;
import world.bentobox.bentobox.api.commands.island.CustomIslandMultiHomeHelp;
import world.bentobox.bentobox.api.user.User;
import world.bentobox.bentobox.database.objects.Island;
import world.bentobox.bentobox.lists.Flags;

/**
 * @author tastybento
 */
public class GoCommand extends DelayedTeleportCommand {

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
    public boolean canExecute(User user, String label, List<String> args) {
        // Check if the island is reserved
        Island island = getIslands().getIsland(getWorld(), user.getUniqueId());
        if (island == null) {
            user.sendMessage("general.errors.no-island");
            return false;
        }
        if ((getIWM().inWorld(user.getWorld()) && Flags.PREVENT_TELEPORT_WHEN_FALLING.isSetForWorld(user.getWorld()))
                && user.getPlayer().getFallDistance() > 0) {
            // We're sending the "hint" to the player to tell them they cannot teleport while falling.
            user.sendMessage(Flags.PREVENT_TELEPORT_WHEN_FALLING.getHintReference());
            return false;
        }
        return true;
    }

    @Override
    public boolean execute(User user, String label, List<String> args) {
        if (!args.isEmpty() && NumberUtils.isDigits(args.get(0))) {
            int homeValue = Integer.parseInt(args.get(0));
            int maxHomes = user.getPermissionValue(getPermissionPrefix() + "skygrid.maxhomes", getIWM().getMaxHomes(getWorld()));
            if (homeValue > 1 && homeValue <= maxHomes) {
                this.delayCommand(user, () -> getIslands().homeTeleport(getWorld(), user.getPlayer(), homeValue));
                return true;
            }
        }
        this.delayCommand(user, () -> getIslands().homeTeleport(getWorld(), user.getPlayer()));
        return true;
    }

}