// Class untuk merepresentasikan piece puzzle, termasuk rotasi dan pencerminan bentuk piece

package model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Piece {
    private char id;
    private boolean[][] shape;
    private int height;
    private int width;

    public Piece(char id, boolean[][] shape) {
        this.id = id;
        this.shape = shape;
        this.height = shape.length;
        this.width = shape[0].length;
    }

    // Mengcopy piece yang ada
    public Piece(Piece other) {
        this.id = other.id;
        this.height = other.height;
        this.width = other.width;
        this.shape = new boolean[height][width];
        for (int i = 0; i < height; i++) {
            System.arraycopy(other.shape[i], 0, this.shape[i], 0, width);
        }
    }

    // Rotasi 90 derajat searah jarum jam
    public Piece rotate() {
        boolean[][] newShape = new boolean[width][height];
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                newShape[j][height - 1 - i] = shape[i][j];
            }
        }
        return new Piece(id, newShape);
    }

    // Pencerminan horizontal
    public Piece flipHorizontal() {
        boolean[][] newShape = new boolean[height][width];
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                newShape[i][width - 1 - j] = shape[i][j];
            }
        }
        return new Piece(id, newShape);
    }

    // Mendapatkan semua kemungkinan orientasi piece
    public List<Piece> getAllOrientations() {
        Set<String> uniqueOrientations = new HashSet<>();
        List<Piece> orientations = new ArrayList<>();

        Piece current = this;
        // 4 rotasi
        for (int i = 0; i < 4; i++) {
            // Original dan flip horizontal untuk setiap rotasi
            Piece flipped = current.flipHorizontal();
            addUniqueOrientation(current, uniqueOrientations, orientations);
            addUniqueOrientation(flipped, uniqueOrientations, orientations);
            current = current.rotate();
        }

        return orientations;
    }

    // Menambahkan orientasi piece yang unik ke dalam list
    // Menggunakan Set untuk mengecek duplikat berdasarkan representasi string piece
    private void addUniqueOrientation(Piece piece, Set<String> uniqueOrientations, List<Piece> orientations) {
        String key = piece.toString();
        if (uniqueOrientations.add(key)) {
            orientations.add(piece);
        }
    }

    // Getter untuk ID huruf piece (A-Z)
    public char getId() {
        return id;
    }

    // Getter untuk array 2D yang merepresentasikan bentuk piece
    // true = ada piece, false = kosong
    public boolean[][] getShape() {
        return shape;
    }

    // Getter untuk tinggi piece (jumlah baris)
    public int getHeight() {
        return height;
    }

    // Getter untuk lebar piece (jumlah kolom)
    public int getWidth() {
        return width;
    }
}