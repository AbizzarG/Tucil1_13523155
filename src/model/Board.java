// Class untuk merepresentasikan papan puzzle, termasuk logika penempatan dan pengecekan piece

package model;

import java.util.Arrays;

public class Board {
    private int rows;
    private int cols;
    private char[][] grid;

    public Board(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        this.grid = new char[rows][cols];
        // Inisialisasi grid kosong dengan '.'
        for (int i = 0; i < rows; i++) {
            Arrays.fill(grid[i], '.');
        }
    }

    // Mengcopy board
    public Board(Board other) {
        this.rows = other.rows;
        this.cols = other.cols;
        this.grid = new char[rows][cols];
        for (int i = 0; i < rows; i++) {
            System.arraycopy(other.grid[i], 0, this.grid[i], 0, cols);
        }
    }

    // Cek apakah piece bisa ditempatkan pada posisi tertentu
    public boolean canPlacePiece(Piece piece, Position pos) {
        boolean[][] shape = piece.getShape();
        for (int i = 0; i < piece.getHeight(); i++) {
            for (int j = 0; j < piece.getWidth(); j++) {
                if (shape[i][j]) {
                    int newRow = pos.getRow() + i;
                    int newCol = pos.getCol() + j;

                    // Cek batas batas board
                    if (newRow >= rows || newCol >= cols || newRow < 0 || newCol < 0) {
                        return false;
                    }
                    // Cek tumpang tindih antar piece
                    if (grid[newRow][newCol] != '.') {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    // Menempatkan piece pada posisi yang sesuai
    public void placePiece(Piece piece, Position pos) {
        boolean[][] shape = piece.getShape();
        for (int i = 0; i < piece.getHeight(); i++) {
            for (int j = 0; j < piece.getWidth(); j++) {
                if (shape[i][j]) {
                    grid[pos.getRow() + i][pos.getCol() + j] = piece.getId();
                }
            }
        }
    }

    // Menghapus piece dari posisi tertentu
    public void removePiece(Piece piece, Position pos) {
        boolean[][] shape = piece.getShape();
        for (int i = 0; i < piece.getHeight(); i++) {
            for (int j = 0; j < piece.getWidth(); j++) {
                if (shape[i][j]) {
                    grid[pos.getRow() + i][pos.getCol() + j] = '.';
                }
            }
        }
    }

    // Mencari posisi kosong yang pertama
    public Position findFirstEmpty() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (grid[i][j] == '.') {
                    return new Position(i, j);
                }
            }
        }
        return null;
    }

    // Cek apakah board sudah terisi penuh
    public boolean isFull() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (grid[i][j] == '.') {
                    return false;
                }
            }
        }
        return true;
    }

    // Mengembalikan jumlah baris
    public int getRows() {
        return rows;
    }

    // Mengembalikan jumlah kolom
    public int getCols() {
        return cols;
    }

    // Mengembalikan array 2D representasi papan
    public char[][] getGrid() {
        return grid;
    }
}
