package world.bentobox.skygrid.generators;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.generator.BiomeProvider;
import org.bukkit.generator.WorldInfo;
import org.bukkit.util.noise.PerlinOctaveGenerator;

/**
 * @author tastybento
 */
public class SkyGridBiomeProvider extends BiomeProvider {

    private final Map<World.Environment, PerlinOctaveGenerator> temperatureGenMap = new ConcurrentHashMap<>();
    private final Map<World.Environment, PerlinOctaveGenerator> rainfallGenMap = new ConcurrentHashMap<>();


    @Override
    public Biome getBiome(WorldInfo worldInfo, int realX, int y, int realZ) {

        // Make and cache the PerlinOctaveGenerator for the environment
        PerlinOctaveGenerator temperatureGen = temperatureGenMap.computeIfAbsent(worldInfo.getEnvironment(), wf -> {
            PerlinOctaveGenerator tg = new PerlinOctaveGenerator(worldInfo.getSeed(), 16);
            tg.setScale(1.0 / 100.0);
            return tg;
        });
        PerlinOctaveGenerator rainfallGen = rainfallGenMap.computeIfAbsent(worldInfo.getEnvironment(), wf -> {
            PerlinOctaveGenerator rg = new PerlinOctaveGenerator(worldInfo.getSeed() + 1, 15);
            rg.setScale(1.0 / 100.0);
            return rg;
        });

        //We get the 3 closest biome's to the temperature and rainfall at this block
        Map<SkyGridBiomes, Double> biomes = SkyGridBiomes.getBiomes(worldInfo.getEnvironment(),
                Math.abs(temperatureGen.noise(realX, realZ, 0.5, 0.5) * 100.0),
                Math.abs(rainfallGen.noise(realX, realZ, 0.5, 0.5) * 100.0));

        double maxNoiz = 0.0;
        SkyGridBiomes maxBiome = null;

        for (Entry<SkyGridBiomes, Double> biome : biomes.entrySet()) {
            if (biome.getValue() >= maxNoiz) {
                maxNoiz = biome.getValue();
                maxBiome = biome.getKey();
            }
        }
        return Objects.requireNonNull(maxBiome).biome;
    }

    @Override
    public List<Biome> getBiomes(WorldInfo worldInfo) {
        return Arrays.stream(SkyGridBiomes.values()).map(SkyGridBiomes::getBiome).toList();
    }

}
