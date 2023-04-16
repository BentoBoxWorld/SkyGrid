package world.bentobox.skygrid;


import world.bentobox.bentobox.api.addons.Addon;
import world.bentobox.bentobox.api.addons.Pladdon;


public class SkyGridPladdon extends Pladdon {

    @Override
    public Addon getAddon() {
        return new SkyGrid();
    }
}
