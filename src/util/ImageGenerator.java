// Class untuk menghasilkan gambar PNG dari solusi puzzle

package util;

import model.*;
import java.awt.*;
import java.awt.image.*;
import javax.imageio.*;
import java.io.*;

public class ImageGenerator {
    // Konstanta untuk ukuran gambar
    private static final int CELL_SIZE = 50; // Ukuran setiap sel dalam pixel
    private static final int BORDER = 2; // Ukuran border antar sel
    private static final int PADDING = 20; // Padding di sekitar board

    // Warna untuk setiap piece (A-Z)
    private static final Color[] PIECE_COLORS = {
            new Color(231, 76, 60), // A - Merah terang
            new Color(46, 204, 113), // B - Hijau muda fresh
            new Color(52, 152, 219), // C - Biru laut
            new Color(155, 89, 182), // D - Ungu anggur
            new Color(241, 196, 15), // E - Kuning cerah
            new Color(230, 126, 34), // F - Orange segar
            new Color(26, 188, 156), // G - Turquoise
            new Color(52, 73, 94), // H - Abu gelap
            new Color(192, 57, 43), // I - Merah tua
            new Color(39, 174, 96), // J - Hijau daun
            new Color(41, 128, 185), // K - Biru medium
            new Color(142, 68, 173), // L - Ungu royal
            new Color(243, 156, 18), // M - Orange muda
            new Color(211, 84, 0), // N - Orange tua
            new Color(22, 160, 133), // O - Tosca gelap
            new Color(44, 62, 80), // P - Hitam keabu
            new Color(255, 99, 71), // Q - Salmon
            new Color(50, 205, 50), // R - Lime green
            new Color(30, 144, 255), // S - Dodger blue
            new Color(138, 43, 226), // T - Blue violet
            new Color(255, 215, 0), // U - Golden
            new Color(255, 140, 0), // V - Dark orange
            new Color(64, 224, 208), // W - Turquoise light
            new Color(119, 136, 153), // X - Slate gray
            new Color(220, 20, 60), // Y - Crimson
            new Color(34, 139, 34) // Z - Forest green
    };

    // Menyimpan solusi puzzle sebagai gambar
    public static void saveToImage(Solution solution, String filename) throws IOException {
        Board board = solution.getBoard();
        int width = (board.getCols() * (CELL_SIZE + BORDER)) + (2 * PADDING);
        int height = (board.getRows() * (CELL_SIZE + BORDER)) + (2 * PADDING);

        // Buat gambar baru dengan kualitas render yang bagus
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = image.createGraphics();

        try {
            // Set background putih
            g2d.setColor(Color.WHITE);
            g2d.fillRect(0, 0, width, height);

            // Set pengaturan rendering untuk hasil yang lebih baik
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

            // Gambar board
            char[][] grid = board.getGrid();
            for (int row = 0; row < board.getRows(); row++) {
                for (int col = 0; col < board.getCols(); col++) {
                    int x = PADDING + (col * (CELL_SIZE + BORDER));
                    int y = PADDING + (row * (CELL_SIZE + BORDER));

                    char piece = grid[row][col];
                    if (piece != '.') {
                        // Gambar piece
                        g2d.setColor(getPieceColor(piece));
                        g2d.fillRect(x, y, CELL_SIZE, CELL_SIZE);

                        // Tambah label
                        g2d.setColor(Color.WHITE);
                        g2d.setFont(new Font("Arial", Font.BOLD, CELL_SIZE / 2));
                        FontMetrics metrics = g2d.getFontMetrics();
                        String text = String.valueOf(piece);
                        int textX = x + (CELL_SIZE - metrics.stringWidth(text)) / 2;
                        int textY = y + ((CELL_SIZE + metrics.getAscent()) / 2);
                        g2d.drawString(text, textX, textY);
                    } else {
                        // Gambar cell kosong
                        g2d.setColor(Color.LIGHT_GRAY);
                        g2d.fillRect(x, y, CELL_SIZE, CELL_SIZE);
                    }

                    // Gambar border
                    g2d.setColor(Color.BLACK);
                    g2d.drawRect(x, y, CELL_SIZE, CELL_SIZE);
                }
            }

            // Tambah informasi statistik
            g2d.setColor(Color.BLACK);
            g2d.setFont(new Font("Arial", Font.PLAIN, 12));
            String stats = String.format("Waktu: %d ms | Pengulangan: %d",
                    solution.getExecutionTime(), solution.getIterationCount());
            g2d.drawString(stats, PADDING, height - PADDING / 2);

            // Simpan gambar
            File outputFile = new File(filename);
            boolean success = ImageIO.write(image, "PNG", outputFile);

            if (!success) {
                throw new IOException("Tidak bisa menyimpan gambar ke " + filename);
            }

            System.out.println("Gambar berhasil disimpan ke: " + outputFile.getAbsolutePath());

        } finally {
            g2d.dispose();
        }
    }

    private static Color getPieceColor(char piece) {
        int index = piece - 'A';
        if (index >= 0 && index < PIECE_COLORS.length) {
            return PIECE_COLORS[index];
        }
        return Color.GRAY;
    }
}