package world.bentobox.skygrid.generators;

import java.util.Random;

/**
 * Generates a series of numbers in random order. Used to distribute the
 * items in a chest randomly.
 *
 */
public class RandomSeries {
	private int[] reset;
	private int[] series;
	private int pos;
	
	public RandomSeries(int size) {
		reset = new int[size];
		for (int i = 0; i < size; i++) {
			reset[i] = i;
		}
		reset();
	}
	
	public void reset() {
		pos = 0;
		series = reset.clone();
	}
	
	public int next(Random random) {
		int slot = pos + random.nextInt(series.length - pos);
		int temp = series[pos];
		series[pos] = series[slot];
		series[slot] = temp;
		return series[pos++];
	}
	
}