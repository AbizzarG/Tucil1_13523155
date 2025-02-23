// Class untuk membaca dan memparse file input puzzle

package util;

import model.*;
import java.io.*;
import java.util.*;

public class PuzzleReader {
    // Membaca data puzzle dari file dan mengubahnya menjadi objek PuzzleData
    public static PuzzleData readFromFile(String filename) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            System.out.println("Membaca file: " + filename);

            // Baca dimensi board dan jumlah piece dari baris pertama
            String firstLine = reader.readLine();
            System.out.println("Line pertama: " + firstLine);
            String[] dimensions = firstLine.split(" ");
            int N = Integer.parseInt(dimensions[0]);
            int M = Integer.parseInt(dimensions[1]);
            int P = Integer.parseInt(dimensions[2]);

            // Baca tipe kasus puzzle (DEFAULT/CUSTOM/PYRAMID) *untuk program ini hanya
            // menggunakan Default
            String caseType = reader.readLine();
            System.out.println("Tipe Puzzle: " + caseType);

            // Baca data piece satu per satu
            List<Piece> pieces = new ArrayList<>();
            List<String> currentPieceLines = new ArrayList<>();
            char currentId = 'A';

            String line;
            while ((line = reader.readLine()) != null && pieces.size() < P) {
                if (!line.trim().isEmpty()) {
                    // Jika baris berisi karakter berbeda dengan ID saat ini,
                    // berarti ini piece baru
                    if (!line.contains(String.valueOf(currentId)) && !currentPieceLines.isEmpty()) {
                        System.out.println("Membuat piece " + currentId + ":");
                        for (String l : currentPieceLines) {
                            System.out.println("\t" + l);
                        }
                        pieces.add(createPiece(currentId, currentPieceLines));
                        currentPieceLines = new ArrayList<>();
                        currentId++;
                    }
                    currentPieceLines.add(line);
                }
            }

            // Proses piece terakhir jika masih ada
            if (!currentPieceLines.isEmpty()) {
                System.out.println("Membuat piece " + currentId + ":");
                for (String l : currentPieceLines) {
                    System.out.println("\t" + l);
                }
                pieces.add(createPiece(currentId, currentPieceLines));
            }

            return new PuzzleData(N, M, P, caseType, pieces);
        }
    }

    // Membuat objek Piece dari data teks yang dibaca
    private static Piece createPiece(char id, List<String> lines) {
        if (lines.isEmpty()) {
            throw new IllegalArgumentException("Data piece kosong untuk piece " + id);
        }

        // Menentukan dimensi piece
        int height = lines.size();
        int width = lines.stream().mapToInt(String::length).max().orElse(0);
        boolean[][] shape = new boolean[height][width];

        System.out.println("Membuat piece " + id + " dengan dimensi " + height + "x" + width);
        for (int i = 0; i < height; i++) {
            String line = lines.get(i);
            System.out.println("Memproses line: " + line);
            for (int j = 0; j < line.length(); j++) {
                shape[i][j] = line.charAt(j) == id;
            }
        }

        return new Piece(id, shape);
    }
}