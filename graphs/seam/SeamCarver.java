import edu.princeton.cs.algs4.Picture;

public class SeamCarver {
    private Picture picture;

    public SeamCarver(Picture picture) {
        if (picture == null) throw new IllegalArgumentException();
        this.picture = new Picture(picture);
    }

    public Picture picture() {
        return new Picture(picture);
    }

    public int width() {
        return picture.width();
    }

    public int height() {
        return picture.height();
    }

    /**
     * You will use the dual-gradient energy function: The energy of pixel (x,y) is
     * √Δ2x(x,y) + Δ2y(x,y),
     * where the square of the x-gradient
     * Δ2x(x,y) = Rx(x,y)² + Gx(x,y)² + Bx(x,y)²,
     * and where the central differences Rx(x,y), Gx(x,y), and Bx(x,y)
     * are the differences in the red, green, and blue components between pixel
     * (x + 1, y) and pixel (x − 1, y), respectively.
     * The square of the y-gradient Δ2y(x,y) is defined in an analogous manner.
     * We define the energy of a pixel at the border of the image to be 1000,
     * so that it is strictly larger than the energy of any interior pixel.
     */
    // energy of pixel at column x and row y
    public double energy(int x, int y) {
        if (x >= width() || y >= height() || x < 0 || y < 0) throw new IllegalArgumentException();
        if (x == 0 || x == width() - 1 || y == 0 || y == height() - 1) return 1000;

        int leftNeighbor = picture.getRGB(x - 1, y);
        int rightNeighbor = picture.getRGB(x + 1, y);
        int topNeighbor = picture.getRGB(x, y - 1);
        int bottomNeighbor = picture.getRGB(x, y + 1);

        double xGradient = getGradient(leftNeighbor, rightNeighbor);
        double yGradient = getGradient(topNeighbor, bottomNeighbor);

        return Math.sqrt(xGradient + yGradient);
    }

    private double getGradient(int neighborA, int neighborB) {
        double rx = extractRed(neighborA) - extractRed(neighborB);
        double gx = extractGreen(neighborA) - extractGreen(neighborB);
        double bx = extractBlue(neighborA) - extractBlue(neighborB);

        return Math.pow(rx, 2) + Math.pow(gx, 2) + Math.pow(bx, 2);
    }

    private int extractRed(int rgb) {
        return (rgb >> 16) & 0xFF;
    }

    private int extractGreen(int rgb) {
        return (rgb >> 8) & 0xFF;
    }
    private int extractBlue(int rgb) {
        return rgb & 0xFF;
    }

    // sequence of indices for horizontal seam
    public int[] findHorizontalSeam() {
        // Guard against single row picture.
        if (width() == 1) return new int[1];

        // Create matrices to store the path,
        int[][] pathTo = new int[width()][height()];
        // the cumulative energy cost,
        double[][] distTo = new double[width()][height()];
        // and the precomputed edge costs.
        double[][] cost = new double[width()][height()];

        // Compute the edges and initialize the distance to infinity, except in the initial column.
        for (int y = 0; y < cost[0].length; y++) {
            distTo[1][y] = 1000 + energy(1, y);
            pathTo[1][y] = y;
            for (int x = 2; x < cost.length; x++) {
                cost[x][y] = energy(x, y);
                distTo[x][y] = Double.POSITIVE_INFINITY;
            }
        }

        // Guard against index out of bounds.
        int rightEdge = width() - 1;
        int bottomEdge = height() - 1;

        // Relax all edges in topological order (from left to right).
        for (int x = 1; x < rightEdge; x++)
            for (int y = 1; y < bottomEdge; y++) {
                if (distTo[x + 1][y - 1] > distTo[x][y] + cost[x + 1][y - 1]) {
                    distTo[x + 1][y - 1] = distTo[x][y] + cost[x + 1][y - 1];
                    pathTo[x + 1][y - 1] = y;
                }
                if (distTo[x + 1][y] > distTo[x][y] + cost[x + 1][y]) {
                    distTo[x + 1][y] = distTo[x][y] + cost[x + 1][y];
                    pathTo[x + 1][y] = y;
                }
                if (distTo[x + 1][y + 1] > distTo[x][y] + cost[x + 1][y + 1]) {
                    distTo[x + 1][y + 1] = distTo[x][y] + cost[x + 1][y + 1];
                    pathTo[x + 1][y + 1] = y;
                }
            }

        // Scan the last column for the shortest path.
        double minEnergy = Double.POSITIVE_INFINITY;
        int seamEnd = 0;
        for (int y = 0; y < distTo[rightEdge].length; y++)
            if (distTo[rightEdge][y] < minEnergy) {
                minEnergy = distTo[rightEdge][y];
                seamEnd = y;
            }

        // Store the shortest path to return it.
        int[] seam = new int[width()];
        seam[rightEdge] = seamEnd;
        for (int i = rightEdge; i > 0; i--)
            seam[i - 1] = pathTo[i][seam[i]];

        return seam;
    }

    // sequence of indices for vertical seam
    public int[] findVerticalSeam() {
        if (height() == 1) return new int[1];
        int[][] pathTo = new int[width()][height()];
        double[][] distTo = new double[width()][height()];
        double[][] cost = new double[width()][height()];
        // Compute the edges and initialize the distance to infinity, except in the initial row.
        for (int x = 0; x < cost.length; x++) {
            distTo[x][1] = 1000 + energy(x, 1);
            pathTo[x][1] = x;
            for (int y = 2; y < cost[0].length; y++) {
                cost[x][y] = energy(x, y);
                distTo[x][y] = Double.POSITIVE_INFINITY;
            }
        }
        int rightEdge = width() - 1;
        int bottomEdge = height() - 1;
        // Relax all edges in topological order (from top to bottom).
        for (int y = 1; y < bottomEdge; y++)
            for (int x = 1; x < rightEdge; x++) {
                if (distTo[x - 1][y + 1] > distTo[x][y] + cost[x - 1][y + 1]) {
                    distTo[x - 1][y + 1] = distTo[x][y] + cost[x - 1][y + 1];
                    pathTo[x - 1][y + 1] = x;
                }
                if (distTo[x][y + 1] > distTo[x][y] + cost[x][y + 1]) {
                    distTo[x][y + 1] = distTo[x][y] + cost[x][y + 1];
                    pathTo[x][y + 1] = x;
                }
                if (distTo[x + 1][y + 1] > distTo[x][y] + cost[x + 1][y + 1]) {
                    distTo[x + 1][y + 1] = distTo[x][y] + cost[x + 1][y + 1];
                    pathTo[x + 1][y + 1] = x;
                }
            }
        // Scan the last row for the shortest path.
        double minEnergy = Double.POSITIVE_INFINITY;
        int seamEnd = 0;
        for (int x = 0; x < distTo.length; x++)
            if (distTo[x][bottomEdge] < minEnergy) {
                minEnergy = distTo[x][bottomEdge];
                seamEnd = x;
            }
        // Store the shortest path to return it.
        int[] seam = new int[height()];
        seam[bottomEdge] = seamEnd;
        for (int i = bottomEdge; i > 0; i--)
            seam[i - 1] = pathTo[seam[i]][i];
        return seam;
    }

    // remove horizontal seam from current picture
    public void removeHorizontalSeam(int[] seam) {
        if (seam == null)                       throw new IllegalArgumentException();
        if (seam.length != width())             throw new IllegalArgumentException();
        if (height() <= 1)                      throw new IllegalArgumentException();
        if (seam[0] < 0 || seam[0] >= height()) throw new IllegalArgumentException();
        for (int i = 1; i < seam.length; i++)
            if (Math.abs(seam[i - 1] - seam[i]) > 1 || seam[i] < 0 || seam[i] >= height())
                throw new IllegalArgumentException();

        int newHeight = height() - 1;
        Picture newPic = new Picture(width(), newHeight);

        for (int x = 0; x < width(); x++)
            for (int y = 0; y < newHeight; y++) {
                if (y < seam[x]) newPic.setRGB(x, y, picture.getRGB(x, y));
                else             newPic.setRGB(x, y, picture.getRGB(x, y + 1));
            }

        picture = new Picture(newPic);
    }

    // remove vertical seam from current picture
    public void removeVerticalSeam(int[] seam) {
        if (seam == null)                      throw new IllegalArgumentException();
        if (seam.length != height())           throw new IllegalArgumentException();
        if (width() <= 1)                      throw new IllegalArgumentException();
        if (seam[0] < 0 || seam[0] >= width()) throw new IllegalArgumentException();
        for (int i = 1; i < seam.length; i++)
            if (Math.abs(seam[i - 1] - seam[i]) > 1 || seam[i] < 0 || seam[i] >= width())
                throw new IllegalArgumentException();

        int newWidth = width() - 1;
        Picture newPic = new Picture(newWidth, height());

        for (int x = 0; x < newWidth; x++)
            for (int y = 0; y < height(); y++) {
                if (x < seam[y]) newPic.setRGB(x, y, picture.getRGB(x, y));
                else             newPic.setRGB(x, y, picture.getRGB(x + 1, y));
            }

        picture = new Picture(newPic);
    }
}