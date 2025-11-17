package world.bentobox.skygrid.listeners;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;

import world.bentobox.bentobox.util.teleport.SafeSpotTeleport;
import world.bentobox.skygrid.SkyGrid;

/**
 * Listens to player's teleporting to the End and puts an obsidian platform under them.
 */
public class EndPortalListener implements Listener {
    private SkyGrid addon;

    public EndPortalListener(SkyGrid addon) {
        super();
        this.addon = addon;
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onPlayerTeleport(PlayerPortalEvent e) {
        if (addon.getEndWorld() == null ||
                e.getCause() != TeleportCause.END_PORTAL || !addon.inWorld(e.getFrom())) {
            return;
        }
        Location to = e.getPlayer().getLocation().toVector().toLocation(addon.getEndWorld());
        to.setY(addon.getSettings().getIslandHeight());
        Bukkit.getScheduler().runTask(addon.getPlugin(), () ->
        new SafeSpotTeleport.Builder(addon.getPlugin()).portal().entity(e.getPlayer()).location(to).build());
        e.setCancelled(true);

    }
}
