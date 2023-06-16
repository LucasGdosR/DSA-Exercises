package union_find.percolation;

import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

/**
Monte Carlo simulation. To estimate the percolation threshold, consider the following computational experiment:

Initialize all sites to be blocked.
Repeat the following until the system percolates:
Choose a site uniformly at random among all blocked sites.
Open the site.
The fraction of sites that are opened when the system percolates provides an estimate of the percolation threshold.

By repeating this computation experiment T times and averaging the results, we obtain a more accurate estimate of the
percolation threshold.

Throw an IllegalArgumentException in the constructor if either n ≤ 0 or trials ≤ 0.
Also, include a main() method that takes two command-line arguments n and T, performs T independent computational
experiments (discussed above) on an n-by-n grid, and prints the sample mean, sample standard deviation, and the 95%
confidence interval for the percolation threshold. Use StdRandom to generate random numbers; use StdStats to compute
the sample mean and sample standard deviation.
 */
public class PercolationStats {
    private final double[] results;
    private static final double CONFIDENCE_95 = 1.96d;

    public PercolationStats(int n, int trials) {
        if (n <= 0 || trials <= 0)
            throw new IllegalArgumentException();

        results = new double[trials];
        int numberOfElementsInGrid = n * n;

        for (int i = 0; i < trials; i++) {
            Percolation percolation = new Percolation(n);
            while (!percolation.percolates())
                percolation.open(
                        StdRandom.uniformInt(1, n + 1),
                        StdRandom.uniformInt(1, n + 1)
                );
            results[i] = ((double) percolation.numberOfOpenSites()) / numberOfElementsInGrid;
        }
    }

    public double mean() {
        return StdStats.mean(results);
    }

    public double stddev() {
        return StdStats.stddev(results);
    }

    public double confidenceLo() {
        return mean() - stddev() * CONFIDENCE_95 / Math.sqrt(results.length);
    }

    public double confidenceHi() {
        return mean() + stddev() * CONFIDENCE_95 / Math.sqrt(results.length);
    }

    public static void main(String[] args) {
        PercolationStats stats = new PercolationStats(Integer.parseInt(args[0]), Integer.parseInt(args[1]));
        System.out.println("mean                    = "+stats.mean()+"\n" +
                "stddev                  = "+stats.stddev()+"\n" +
                "95% confidence interval = ["+stats.confidenceLo()+", "+stats.confidenceHi()+"]");
    }
}
