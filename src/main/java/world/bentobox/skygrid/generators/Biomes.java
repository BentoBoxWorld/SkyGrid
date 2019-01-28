package world.bentobox.skygrid.generators;

import java.util.HashMap;

import org.bukkit.block.Biome;

/**
 * In order to find the biome of a region, we look up how close it's
 * conditions are to the conditions on this map. We can use it's proximity
 * to a biome to determine how much influence that biome's noise generators,
 * vegetation and features should have on the area of the map. This allows us
 * to have seamless transitions between the biomes (at the price of speed)
 *
 * For example, a place with no precipitation and -30 degree (celcius) weather
 * is a tundra, but it is also much closer to a dessert than a rain forest.
 *
 */
public enum Biomes {

    //We store the biome, the temperature and rainfall for each biome.
    SNOWY_TUNDRA(Biome.SNOWY_TUNDRA, 0, 100),
    SNOWY_TAIGA(Biome.SNOWY_TAIGA, 0, 100),
    FROZEN_RIVER(Biome.FROZEN_RIVER, 0, 10),
    SNOWY_BEACH(Biome.SNOWY_BEACH, 0, 100),
    MOUNTAINS(Biome.MOUNTAINS, 20, 60),
    WOODED_MOUNTAINS(Biome.WOODED_MOUNTAINS, 20, 60),
    DESERT(Biome.DESERT, 60, 4),
    FOREST(Biome.FOREST, 50, 60),
    PLAINS(Biome.PLAINS, 40, 30),
    SWAMP(Biome.SWAMP, 40, 70),
    JUNGLE(Biome.JUNGLE, 60, 50),
    SAVANNA(Biome.SAVANNA, 40, 10),
    DESERT_HILLS(Biome.DESERT_HILLS, 60, 5),
    TAIGA(Biome.TAIGA, 30, 5);

    public final Biome biome;
    public final double optimumTemperature, optimumRainfall;

    Biomes(Biome biome, double temp, double rain) {
        this.biome = biome;
        this.optimumTemperature = temp;
        this.optimumRainfall = rain;
    }

    /**
     * Returns the mapping between the 3 closest biomes and "amount of the biome" in this location.
     * This is just so that we can limit the amount of calculations we have to do.
     * This could probably be cleaned up a bit
     */
    public static HashMap<Biomes, Double> getBiomes(double temp, double rain) {
        //We tell it the capacity we need to avoid expensive dynamic lengthening
        HashMap<Biomes, Double> biomes = new HashMap<>(3);

        Biomes closestBiome = null, secondClosestBiome = null, thirdClosestBiome = null;
        double closestDist = 10000000, secondClosestDist = 10000000, thirdClosestDist = 10000000;

        for (Biomes biome : Biomes.values()) {
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

    private static double getSquaredDistance(Biomes biome, double temp, double rain) {
        return Math.abs((biome.optimumTemperature-temp)*(biome.optimumTemperature-temp) + (biome.optimumRainfall-rain)*(biome.optimumRainfall-rain));
    }

}