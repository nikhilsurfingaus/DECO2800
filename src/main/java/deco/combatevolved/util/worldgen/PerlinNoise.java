package deco.combatevolved.util.worldgen;


/**
 * Perlin noise generation algorithm, modified implementation of Ken Perlin's original algorithm
 * Perlin noise generates smooth random gradients
 */
public class PerlinNoise {
	/**
	 * Perlin Noise constructor
	 */
	private PerlinNoise() {
		/*
		 * This constructor is not called, but added to deal with the: Add a private
		 * constructor to hide the implicit public one. code smell
		 */
	}
	/**
	 * Perlin noise function
	 * @param x The x value to put through the perlin noise function
	 * @param y The y value to put through the perlin noise function
	 * @return A random value
	 */
	public static float perlin(float x, float y) {
		// grid cells
		int x0 = (int)x;
		int x1 = x0 + 1;
		int y0 = (int)y;
		int y1 = y0 + 1;

		// for distance vectors  - FROM CORNERS TO POINT!
		float dx = x - x0;  // distance from x0 to x
		float dy = y - y0;  // distance from y0 to y
		float[] x0y0 = {dx, dy};
		float[] x1y0 = {dx - 1, dy};
		float[] x0y1 = {dx, dy - 1};
		float[] x1y1 = {dx - 1, dy - 1};

		float u = fade(dx);
		float v = fade(dy);

		float a = dotGridGradient(x0y0, x0, y0);
		float b = dotGridGradient(x1y0, x1, y0);
		float c = dotGridGradient(x0y1, x0, y1);
		float d = dotGridGradient(x1y1, x1, y1);

		float ix0 = lerp(a, b, u);
		float ix1 = lerp(c, d, u);

		return (lerp(ix0, ix1, v) + 1) / 2 ;
	}

	/**
	 * Linearly interpolates between a0 to a1 on w position.
	 * @param a0 Range starting value to interpolate on
	 * @param a1 Range ending value to interpolate on
	 * @param w value to interpolate on
	 * @return A linearly interpolated value
	 */
	private static float lerp(float a0, float a1, float w) {
		return (1.0f - w)*a0 + w*a1;
	}

	/**
	 *
	 * @param distance
	 * @param xi
	 * @param yi
	 * @return
	 */
	private static float dotGridGradient(float[] distance, int xi, int yi) {
		return ((distance[0] * NoiseMap.vector[xi][yi][0]) + (distance[1] * NoiseMap.vector[xi][yi][1]));
	}

	/**
	 * Creates a smoother result than linear interpolation
	 * @param num Interpolation weight
	 * @return "Smoothed" interpolation weight
	 */
	private static float fade(float num) {
		return num * num * num * (num * (num * 6 - 15) + 10);  // 6t^5 - 15t^4 + 10t^3
	}
}