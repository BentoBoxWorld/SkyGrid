package world.bentobox.skygrid;


import world.bentobox.bentobox.api.addons.Addon;
import world.bentobox.bentobox.api.addons.Pladdon;


public class SkyGridPladdon extends Pladdon {

    private SkyGrid addon;

    @Override
    public Addon getAddon() {
        if (addon == null) {
            addon = new SkyGrid();
        }
        return addon;
    }
}
