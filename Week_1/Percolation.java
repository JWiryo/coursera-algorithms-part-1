/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {

    private int openCount;

    private int height,width;
    private int topVirtualNode;
    private int bottomVirtualNode;
    private int[] grid;
    private WeightedQuickUnionUF weightedQuickUnionUF, weightedQuickUnionUFCheck;

    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int n) {

        if (n <= 0) {
            throw new IllegalArgumentException();
        }

        this.openCount = 0;

        // Height, Width == number of rows,columns in the grid
        this.height = n;
        this.width = n;

        // Initiate top and bottom virtual nodes
        this.topVirtualNode = n*n;
        this.bottomVirtualNode = n*n + 1;

        // Block all spots
        int size = n*n;
        grid = new int[size];
        for (int i = 0; i < size; i++) {
            grid[i] = 0;
        }

        // Insert imaginary top and bottom node to UF data structure
        weightedQuickUnionUF = new WeightedQuickUnionUF(size + 2);
        // 2nd UF to avoid Backwash
        weightedQuickUnionUFCheck = new WeightedQuickUnionUF(size + 2);
    }

    // opens the site (row, col) if it is not open already
    public void open(int row, int col) {
        isWithinBound(row, col);

        // If already open, break
        if(isOpen(row,col)) {
            return;
        }

        // Open current coordinate
        int currentIndex = convert2Dto1D(row,col);
        this.grid[currentIndex] = 1;
        openCount++;

        // Union this node with top virtual node if first row
        if (row == 1 && !weightedQuickUnionUF.connected(currentIndex, topVirtualNode))
        {
            weightedQuickUnionUF.union(currentIndex, topVirtualNode);
            weightedQuickUnionUFCheck.union(currentIndex, topVirtualNode);
        }

        // Union this node with bottom virtual node if last row
        if (row == height && !weightedQuickUnionUF.connected(currentIndex, bottomVirtualNode))
        {
            weightedQuickUnionUFCheck.union(currentIndex, bottomVirtualNode);
        }

        // Union with top cell
        if (row > 1) {
            if (isOpen(row-1, col)) {
                weightedQuickUnionUF.union(currentIndex, convert2Dto1D(row-1, col));
                weightedQuickUnionUFCheck.union(currentIndex, convert2Dto1D(row-1, col));
            }
        }

        // Union with bottom cell
        if (row < height) {
            if (isOpen(row+1, col)) {
                weightedQuickUnionUF.union(currentIndex, convert2Dto1D(row+1, col));
                weightedQuickUnionUFCheck.union(currentIndex, convert2Dto1D(row+1, col));
            }
        }

        // Union with left cell
        if (col > 1) {
            if (isOpen(row, col-1)) {
                weightedQuickUnionUF.union(currentIndex, convert2Dto1D(row, col-1));
                weightedQuickUnionUFCheck.union(currentIndex, convert2Dto1D(row, col-1));
            }
        }

        // Union with right cell
        if (col < width) {
            if (isOpen(row, col+1)) {
                weightedQuickUnionUF.union(currentIndex, convert2Dto1D(row, col+1));
                weightedQuickUnionUFCheck.union(currentIndex, convert2Dto1D(row, col+1));
            }
        }
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {

        isWithinBound(row, col);
        if (grid[convert2Dto1D(row,col)] == 1) {
            return true;
        }
        return false;
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {

        isWithinBound(row, col);
        if(!isOpen(row,col)) {
            return false;
        }

        if(weightedQuickUnionUF.connected(topVirtualNode, convert2Dto1D(row,col))){
            return true;
        }
        return false;
    }

    // returns the number of open sites
    public int numberOfOpenSites() {
        return openCount;
    }

    // does the system percolate?
    public boolean percolates() {
        return weightedQuickUnionUFCheck.connected(topVirtualNode,bottomVirtualNode);
    }

    // Method to convert 2D grid coordinates to 1D
    private int convert2Dto1D(int row, int col) {
        return (row-1) * width + (col-1);
    }

    // Check if input is within index bound
    private Boolean isWithinBound(int row, int col) {
        if (row > 0 && col > 0 && row <= width && col <= width) {
            return true;
        }
        throw new IndexOutOfBoundsException();
    }

    // test client (optional)
    public static void main(String[] args) {

    }
}
