// Class untuk menyimpan koordinat posisi (row,col) pada papan puzzle

package model;

public class Position {
    private int row;
    private int col;

    public Position(int row, int col) {
        this.row = row;
        this.col = col;
    }

    // Mengembalikan nilai baris
    public int getRow() {
        return row;
    }

    // Mengembalikan nilai kolom
    public int getCol() {
        return col;
    }

    // Override metode toString untuk membuat format print yang sesuai
    @Override
    public String toString() {
        return "Position(" + row + ", " + col + ")";
    }
}