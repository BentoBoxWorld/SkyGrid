package world.bentobox.skygrid;

import org.bukkit.plugin.java.annotation.plugin.ApiVersion;
import org.bukkit.plugin.java.annotation.plugin.Plugin;

import world.bentobox.bentobox.api.addons.Addon;
import world.bentobox.bentobox.api.addons.Pladdon;

@Plugin(name="SkyGrid", version="1.0")
@ApiVersion(ApiVersion.Target.v1_16)
public class SkyGridPladdon extends Pladdon {

    @Override
    public Addon getAddon() {
        return new SkyGrid();
    }
}
