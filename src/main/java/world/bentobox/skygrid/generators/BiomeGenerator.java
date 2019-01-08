package world.bentobox.skygrid.generators;

import java.util.HashMap;

import org.bukkit.World;
import org.bukkit.util.noise.PerlinOctaveGenerator;

public class BiomeGenerator {
	
	private final PerlinOctaveGenerator temperatureGen, rainfallGen;
	
	public BiomeGenerator(World world) {
		// I used a scale of 1/300 instead of something like 1/600 or 1/1000
		// to make the biome edges easier to see
		temperatureGen = new PerlinOctaveGenerator(world.getSeed(), 16);
		temperatureGen.setScale(1.0/100.0);
		
		rainfallGen = new PerlinOctaveGenerator(world.getSeed() + 1, 15);
		rainfallGen.setScale(1.0/100.0);
	}

	public HashMap<Biomes, Double> getBiomes(int realX, int realZ) {
		return Biomes.getBiomes(Math.abs(temperatureGen.noise(realX, realZ, 0.5, 0.5)*100.0), Math.abs(rainfallGen.noise(realX, realZ, 0.5, 0.5)*100.0));
	}

}