package ss.util;

import net.sf.doodleproject.numerics4j.random.NormalRandomVariable;
import net.sf.doodleproject.numerics4j.random.RNG;
import net.sf.doodleproject.numerics4j.random.RandomRNG;
import net.sf.doodleproject.numerics4j.random.UniformRandomVariable;

public class RandomGenerator {

	private static RNG rng = new RandomRNG(System.currentTimeMillis());

	public static long triangular(double a, double b, double c) {
		double U = UniformRandomVariable.nextRandomVariable(0, 1, rng);
		double F = (c - a) / (b - a);
		if (U <= F)
			return Math.round(a + Math.sqrt(U * (b - a) * (c - a)));
		else
			return Math.round(b - Math.sqrt((1 - U) * (b - a) * (b - c)));
	}
	
	public static double gaussian(double mean, double standardDeviation) {
		return NormalRandomVariable.nextRandomVariable(mean, standardDeviation,
				rng);
	}

}
