// Class untuk menyimpan solusi puzzle yang ditemukan beserta statistik penyelesaiannya

package model;

import java.util.ArrayList;
import java.util.List;

public class Solution {
    private Board board;
    private List<Piece> usedPieces;
    private long executionTime;
    private long iterationCount;

    // Setter untuk mengupdate board dengan deep copy
    public void setBoard(Board board) {
        this.board = new Board(board);
    }

    // Constructor untuk membuat objek Solution dengan board awal
    public Solution(Board board) {
        this.board = new Board(board);
        this.usedPieces = new ArrayList<>();
        this.executionTime = 0;
        this.iterationCount = 0;
    }

    // Menambahkan piece yang digunakan ke dalam solusi
    public void addPiece(Piece piece) {
        usedPieces.add(new Piece(piece));
    }

    // Setter untuk waktu eksekusi
    public void setExecutionTime(long time) {
        this.executionTime = time;
    }

    // Setter untuk jumlah iterasi
    public void setIterationCount(long count) {
        this.iterationCount = count;
    }

    // Getter untuk board solusi
    public Board getBoard() {
        return board;
    }

    // Getter untuk daftar piece yang digunakan
    public List<Piece> getUsedPieces() {
        return usedPieces;
    }

    // Getter untuk waktu eksekusi
    public long getExecutionTime() {
        return executionTime;
    }

    // Getter untuk jumlah iterasi
    public long getIterationCount() {
        return iterationCount;
    }

}