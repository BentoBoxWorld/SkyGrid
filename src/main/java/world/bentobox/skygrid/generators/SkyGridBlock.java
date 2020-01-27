package world.bentobox.skygrid.generators;

import org.bukkit.Material;
import org.bukkit.block.Biome;
import org.bukkit.block.data.BlockData;

public class SkyGridBlock {

    private final Biome biome;
    private final BlockData bd;
    private final int x;
    private final int y;
    private final int z;
    
    public SkyGridBlock(int x, int y, int z, BlockData blockData) {
        this.x = x;
        this.y = y;
        this.z = z;
        bd = blockData;
        biome = Biome.BADLANDS;
    }
    public SkyGridBlock(int x, int y, int z, Material m) {
        this(x,y,z,m.createBlockData());
    }
    /**
     * @return the biome
     */
    public Biome getBiome() {
        return biome;
    }
    /**
     * @return the bd
     */
    public BlockData getBd() {
        return bd;
    }
    /**
     * @return the x
     */
    public int getX() {
        return x;
    }
    /**
     * @return the y
     */
    public int getY() {
        return y;
    }
    /**
     * @return the z
     */
    public int getZ() {
        return z;
    }

}
