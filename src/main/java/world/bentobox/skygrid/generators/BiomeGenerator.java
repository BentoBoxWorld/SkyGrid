package world.bentobox.skygrid.generators;

import java.util.HashMap;
import java.util.Objects;

import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.block.Biome;
import org.bukkit.util.noise.PerlinOctaveGenerator;

public class BiomeGenerator {

    private final PerlinOctaveGenerator temperatureGen;
    private final PerlinOctaveGenerator rainfallGen;
    private final Environment env;

    public BiomeGenerator(World world) {
        temperatureGen = new PerlinOctaveGenerator(world.getSeed(), 16);
        temperatureGen.setScale(1.0/100.0);

        rainfallGen = new PerlinOctaveGenerator(world.getSeed() + 1, 15);
        rainfallGen.setScale(1.0/100.0);
        
        env = world.getEnvironment();
    }

    /**
     * Get the biome for this coordinate
     * @param realX - x
     * @param realZ - z
     * @return Biome
     */
    public Biome getDominantBiome(int realX, int realZ) {
        //We get the 3 closest biome's to the temperature and rainfall at this block
        HashMap<Biomes, Double> biomes = Biomes.getBiomes(env, Math.abs(temperatureGen.noise(realX, realZ, 0.5, 0.5)*100.0), 
                Math.abs(rainfallGen.noise(realX, realZ, 0.5, 0.5)*100.0));
        //And tell bukkit (who tells the client) what the biggest biome here is
        double maxNoiz = 0.0;
        Biomes maxBiome = null;

        for (Biomes biome : biomes.keySet()) {
            if (biomes.get(biome) >= maxNoiz) {
                maxNoiz = biomes.get(biome);
                maxBiome = biome;
            }
        }
        return Objects.requireNonNull(maxBiome).biome;
    }

}