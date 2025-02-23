// Class untuk menyimpan data puzzle yang dibaca dari file input (ukuran board dan piece)

package model;

import java.util.List;

public class PuzzleData {
    private final int rows;
    private final int cols;
    private final int pieceCount;
    private final String caseType;
    private final List<Piece> pieces;

    // Constructor untuk membuat objek PuzzleData dengan parameter yang diberikan
    public PuzzleData(int rows, int cols, int pieceCount, String caseType, List<Piece> pieces) {
        this.rows = rows;
        this.cols = cols;
        this.pieceCount = pieceCount;
        this.caseType = caseType;
        this.pieces = pieces;
    }

    // Getter untuk jumlah baris board
    public int getRows() {
        return rows;
    }

    // Getter untuk jumlah kolom board
    public int getCols() {
        return cols;
    }

    // Getter untuk jumlah total piece puzzle
    public int getPieceCount() {
        return pieceCount;
    }

    // Getter untuk tipe kasus puzzle
    public String getCaseType() {
        return caseType;
    }

    // Getter untuk daftar piece puzzle
    public List<Piece> getPieces() {
        return pieces;
    }
}