package tests;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import ss.util.RandomGenerator;

import com.google.common.collect.Lists;

public class RandomGeneratorTest {

	@Test
	public void testTriangularDistribution() {
		double a = 2.0;
		double b = 5.0;
		double c = 12.0;
		List<Long> randoms = Lists.newArrayList();
		for(int i = 0; i < 100000; i++) {
			randoms.add(RandomGenerator.triangular(a, b, c));
		}
		long sum = 0;
		long max = 0;
		long min = 15;
		for (int i = 0; i < randoms.size(); i++) {
			long num = randoms.get(i);
			if(num > max) {
				max = num;
			}
			if(num < min) {
				min = num;
			}
			sum += num;
		}
		Assert.assertTrue(max <= 12);
		Assert.assertTrue(min >= 2);
	}
}
