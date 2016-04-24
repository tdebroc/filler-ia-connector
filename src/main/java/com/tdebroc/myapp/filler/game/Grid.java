package com.tdebroc.myapp.filler.game;

import java.io.Serializable;

public class Grid implements Serializable {

	private Cell[][] grid;

	public void randomInit(int size) {
		grid = new Cell[size][size];
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				grid[i][j] = new Cell(Colors.getRandomColor());
			}
		}
		grid[0][0].setColor(Colors.getColors()[0]);
		grid[grid.length - 1][grid[0].length - 1].setColor(Colors.getColors()[1]);
        grid[0][grid[0].length - 1].setColor(Colors.getColors()[2]);
        grid[grid.length - 1][0].setColor(Colors.getColors()[3]);
	}

	public Cell[][] getGrid() {
		return grid;
	}

	public void setGrid(Cell[][] grid) {
		this.grid = grid;
	}

	public void displayGrid() {
		for (int i = 0; i < grid.length; i++) {
			for (int j = 0; j < grid[i].length; j++) {
				System.out.print(grid[i][j].getColor());
			}
			System.out.println("");
		}
		System.out.println("");
	}

	public Cell getCell(Position p) {
		return grid[p.getX()][p.getY()];
	}

	public Cell getCell(int x, int y) {
		return grid[x][y];
	}

    public int getNumberOfCells() {
        return grid.length * grid.length;
    }

    private int numberOfCells;
    public void setNumberOfCells() {

    }
}
