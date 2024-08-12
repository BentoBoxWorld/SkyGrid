package world.bentobox.skygrid.generators;

import java.util.EnumMap;
import java.util.Map;

import org.bukkit.World.Environment;
import org.bukkit.block.Biome;

/**
 * In order to find the biome of a region, we look up how close its
 * conditions are to the conditions on this map. We can use its proximity
 * to a biome to determine how much influence that biome's noise generators,
 * vegetation and features should have on the area of the map. This allows us
 * to have seamless transitions between the biomes (at the price of speed)
 *
 * For example, a place with no precipitation and -30 degree (Celcius) weather
 * is a tundra, but it is also much closer to a desert than a rain forest.
 *
 */
public enum SkyGridBiomes {

    //We store the biome, the temperature and rainfall for each biome.
    PLAINS(Environment.NORMAL, Biome.PLAINS, 40, 30),
    DESERT(Environment.NORMAL, Biome.DESERT, 60, 4),
    BADLANDS(Environment.NORMAL, Biome.BADLANDS, 90, 1),
    FOREST(Environment.NORMAL, Biome.FOREST, 50, 60),
    FLOWER_FOREST(Environment.NORMAL, Biome.FLOWER_FOREST, 50, 60),
    BIRCH_FOREST(Environment.NORMAL, Biome.BIRCH_FOREST, 50, 59),
    CHERRY_GROVE(Environment.NORMAL, Biome.CHERRY_GROVE, 45, 35),
    SWAMP(Environment.NORMAL, Biome.SWAMP, 40, 70),
    JUNGLE(Environment.NORMAL, Biome.JUNGLE, 60, 50),
    BAMBOO_JUNGLE(Environment.NORMAL, Biome.BAMBOO_JUNGLE, 60, 49),
    SAVANNA(Environment.NORMAL, Biome.SAVANNA, 40, 10),
    TAIGA(Environment.NORMAL, Biome.TAIGA, 30, 5),
    MUSHROOM_FIELDS(Environment.NORMAL, Biome.MUSHROOM_FIELDS, 50, 50),
    SNOWY_PLAINS(Environment.NORMAL, Biome.SNOWY_PLAINS, 0, 100),
    SNOWY_TAIGA(Environment.NORMAL, Biome.SNOWY_TAIGA, 0, 15),
    FROZEN_RIVER(Environment.NORMAL, Biome.FROZEN_RIVER, 10, 10),
    SNOWY_BEACH(Environment.NORMAL, Biome.SNOWY_BEACH, 10, 100),
    STONY_PEAKS(Environment.NORMAL, Biome.STONY_PEAKS, 20, 60),

    // Nether
    NETHER_WASTES(Environment.NETHER, Biome.NETHER_WASTES, 40, 30),
    SOUL_SAND_VALLEY(Environment.NETHER, Biome.SOUL_SAND_VALLEY, 40, 70),
    CRIMSON_FOREST(Environment.NETHER, Biome.CRIMSON_FOREST, 50, 60),
    WARPED_FOREST(Environment.NETHER, Biome.WARPED_FOREST, 20, 60),
    BASALT_DELTAS(Environment.NETHER, Biome.BASALT_DELTAS, 20, 50),
    // The End
    THE_END(Environment.THE_END, Biome.THE_END, 40, 30),
    SMALL_END_ISLANDS(Environment.THE_END, Biome.SMALL_END_ISLANDS, 0, 100),
    END_MIDLANDS(Environment.THE_END, Biome.END_MIDLANDS, 50, 60),
    END_HIGHLANDS(Environment.THE_END, Biome.END_HIGHLANDS, 20, 60),
    END_BARRENS(Environment.THE_END, Biome.END_BARRENS, 60, 4);

    public final Environment env;
    public final Biome biome;
    public final double optimumTemperature;
    public final double optimumRainfall;

    SkyGridBiomes(Environment env, Biome biome, double temp, double rain) {
        this.env = env;
        this.biome = biome;
        this.optimumTemperature = temp;
        this.optimumRainfall = rain;
    }

    /**
     * Returns the mapping between the 3 closest biomes and "amount of the biome" in this location.
     * This is just so that we can limit the amount of calculations we have to do.
     * @param env - environment
     * @param temp - temperature
     * @param rain - rain
     * @return Map of 3 biomes
     */
    public static Map<SkyGridBiomes, Double> getBiomes(Environment env, double temp, double rain) {
        // We tell it the capacity we need to avoid expensive dynamic lengthening
        Map<SkyGridBiomes, Double> biomes = new EnumMap<>(SkyGridBiomes.class);

        SkyGridBiomes closestBiome = null;
        SkyGridBiomes secondClosestBiome = null;
        SkyGridBiomes thirdClosestBiome = null;
        double closestDist = 10000000;
        double secondClosestDist = 10000000;
        double thirdClosestDist = 10000000;

        for (SkyGridBiomes biome : SkyGridBiomes.values()) {
            // Only cover biomes for this environment, e.g., Nether
            if (!env.equals(biome.env)) continue;
            // To avoid having to do an expensive square root per biome per block,
            // we just compare the square distances, and take the square root at the
            // end.
            double dist = getSquaredDistance(biome, temp, rain);

            if (dist <= closestDist) {
                thirdClosestDist = secondClosestDist; thirdClosestBiome = secondClosestBiome;
                secondClosestDist = closestDist; secondClosestBiome = closestBiome;
                closestDist = dist; closestBiome = biome;
            }

            else if (dist <= secondClosestDist) {
                thirdClosestDist = secondClosestDist; thirdClosestBiome = secondClosestBiome;
                secondClosestDist = dist; secondClosestBiome = biome;
            }

            else if (dist <= thirdClosestDist) {
                thirdClosestDist = dist; thirdClosestBiome = biome;
            }
        }

        // The 10 is just so that farther distances have less influence
        biomes.put(closestBiome, 10.0/Math.sqrt(closestDist));
        biomes.put(secondClosestBiome, 10.0/Math.sqrt(secondClosestDist));
        biomes.put(thirdClosestBiome, 10.0/Math.sqrt(thirdClosestDist));

        return biomes;
    }

    private static double getSquaredDistance(SkyGridBiomes biome, double temp, double rain) {
        return Math.abs((biome.optimumTemperature-temp)*(biome.optimumTemperature-temp) + (biome.optimumRainfall-rain)*(biome.optimumRainfall-rain));
    }

    /**
     * @return the biome
     */
    public Biome getBiome() {
        return biome;
    }

}