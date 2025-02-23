// Class implementasi algoritma brute force untuk mencari solusi puzzle

package solver;

import model.*;
import java.util.*;
import java.util.function.Supplier;

import javax.swing.SwingUtilities;

public class BruteForceSolver {
    private Board board;
    private List<Piece> pieces;
    private long startTime;
    private long iterationCount;
    private Solution solution;
    private SolverVisualizer visualizer;
    private static final int VISUALIZATION_DELAY = 100; // dalam ms
    private Supplier<Boolean> stopCheck;

    // Konstruktor untuk inisialisasi solver
    public BruteForceSolver(int rows, int cols, List<Piece> pieces,
            SolverVisualizer visualizer,
            Supplier<Boolean> stopCheck) {
        this.board = new Board(rows, cols);
        this.pieces = new ArrayList<>(pieces);
        this.iterationCount = 0;
        this.visualizer = visualizer;
        this.stopCheck = stopCheck;
    }

    // Fungsi utama untuk mencari solusi puzzle
    public Solution solve() {
        startTime = System.currentTimeMillis();
        solution = new Solution(board);

        // Menampilkan piece yang akan digunakan (untuk debugging)
        System.out.println("\nPiece yang akan digunakan:");
        for (Piece p : pieces) {
            System.out.println("Piece " + p.getId() + " Dimensi: " + p.getHeight() + "x" + p.getWidth());
        }

        boolean found = solveRecursive(new ArrayList<>(pieces));

        if (found) {
            solution.setExecutionTime(System.currentTimeMillis() - startTime);
            solution.setIterationCount(iterationCount);
            return solution;
        }
        return null;
    }

    // Fungsi rekursif yang mencoba setiap kemungkinan penempatan piece
    private boolean solveRecursive(List<Piece> remainingPieces) {
        if (stopCheck.get()) {
            return false;
        }

        iterationCount++;

        // Update visualisasi setiap 10 iterasi
        if (iterationCount % 10 == 0 && visualizer != null) {
            SwingUtilities.invokeLater(() -> visualizer.updateVisualization(new Board(board)));
            try {
                Thread.sleep(VISUALIZATION_DELAY);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        // Menampilkan status setiap 100 iterasi (untuk debugging)
        if (iterationCount % 100 == 0) {
            System.out.println("Pengukangan: " + iterationCount);
            System.out.println("Piece yang tersisa: " + remainingPieces.size());
            System.out.println("Status papan sekarang:");
            printBoard();
        }

        // Basis Rekursif: cek apakah semua piece sudah digunakan dan board terisi penuh
        if (remainingPieces.isEmpty()) {
            boolean isFull = board.isFull();
            System.out.println("Mengecek apakah papannya penuh: " + isFull);
            return isFull;
        }

        // Cari posisi kosong pertama
        Position emptyPos = board.findFirstEmpty();
        if (emptyPos == null) {
            System.out.println("Tidak ada posisi kosong");
            return false;
        }

        // Debug print
        System.out.println("Mencoba posisi: " + emptyPos.getRow() + "," + emptyPos.getCol());

        // Mencooba setiap piece yang tersisa
        for (int i = 0; i < remainingPieces.size(); i++) {
            Piece currentPiece = remainingPieces.get(i);

            // Mencoba setiap orientasi piece
            List<Piece> orientations = currentPiece.getAllOrientations();
            System.out
                    .println("Mencoba piece " + currentPiece.getId() + " dengan " + orientations.size() + " orientasi");

            for (Piece orientation : orientations) {
                if (board.canPlacePiece(orientation, emptyPos)) {
                    // Letakkan piece di board
                    board.placePiece(orientation, emptyPos);
                    if (visualizer != null) {
                        SwingUtilities.invokeLater(() -> visualizer.updateVisualization(new Board(board)));
                    }
                    // Hapus piece yang sudah digunakan dalam daftar
                    List<Piece> newRemaining = new ArrayList<>(remainingPieces);
                    newRemaining.remove(i);

                    // Rekursif untuk piece selanjutnya
                    if (solveRecursive(newRemaining)) {
                        solution.addPiece(orientation);
                        solution.setBoard(new Board(board)); // Simpan state board saat ini
                        return true;
                    }

                    // Backtracking ke posisi sebelumnya jika solusi belum ditemukan
                    board.removePiece(orientation, emptyPos);
                    if (visualizer != null) {
                        SwingUtilities.invokeLater(() -> visualizer.updateVisualization(new Board(board)));
                    }
                }
            }
        }

        return false;
    }

    private void printBoard() {
        char[][] grid = board.getGrid();
        for (int i = 0; i < board.getRows(); i++) {
            for (int j = 0; j < board.getCols(); j++) {
                System.out.print(grid[i][j] + " ");
            }
            System.out.println();
        }
    }

}