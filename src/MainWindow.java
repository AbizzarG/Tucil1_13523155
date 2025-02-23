// Class untuk implementasi GUI utama yang menampilkan puzzle board dan mengontrol interaksi dengan user

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import model.*;
import solver.*;
import util.*;

public class MainWindow extends JFrame implements SolverVisualizer {
    // Komponen GUI
    private JPanel boardPanel; // Panel untuk papan puzzle
    private JTextField filePathField; // Field untuk path file input
    private JLabel timeLabel; // Label waktu eksekusi
    private JLabel iterationsLabel; // Label jumlah iterasi
    private JButton saveButton; // Tombol simpan solusi
    private JButton solveButton; // Tombol mulai solving
    private JButton stopButton; // Tombol stop solving
    private JButton resetButton; // Tombol reset papan
    private Solution solution; // Solusi yang ditemukan
    private volatile boolean stopRequested = false;
    private static final int CELL_SIZE = 40;
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

    // Konstruktor utama
    public MainWindow() {
        setupWindow();
        createComponents();
        layoutComponents();
    }

    // Setup properti window utama
    private void setupWindow() {
        setTitle("IQ Puzzler Pro Solver");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 500);
        setLocationRelativeTo(null);
    }

    // Membuat semua komponen GUI
    private void createComponents() {
        // Input Panel
        JPanel inputPanel = new JPanel(new BorderLayout(5, 0));
        filePathField = new JTextField();
        filePathField.setEditable(false);
        JButton browseButton = new JButton("Browse");
        browseButton.addActionListener(_ -> handleBrowseButton());
        inputPanel.add(filePathField, BorderLayout.CENTER);
        inputPanel.add(browseButton, BorderLayout.EAST);

        // Control Panel
        JPanel controlPanel = new JPanel();
        solveButton = new JButton("Solve");
        resetButton = new JButton("Reset");
        stopButton = new JButton("Stop");
        saveButton = new JButton("Simpan Solusi");

        solveButton.addActionListener(_ -> handleSolveButton());
        resetButton.addActionListener(_ -> handleResetButton());
        stopButton.addActionListener(_ -> handleStopButton());
        saveButton.addActionListener(_ -> handleSaveButton());

        saveButton.setEnabled(false);
        stopButton.setEnabled(false);

        controlPanel.add(solveButton);
        controlPanel.add(resetButton);
        controlPanel.add(stopButton);
        controlPanel.add(saveButton);

        // Board Panel
        boardPanel = new JPanel();
        boardPanel.setPreferredSize(new Dimension(400, 300));
        boardPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        // Stats Panel
        JPanel statsPanel = new JPanel(new GridLayout(2, 1, 5, 5));
        timeLabel = new JLabel("Waktu: -");
        iterationsLabel = new JLabel("Pengulangan: -");
        statsPanel.add(timeLabel);
        statsPanel.add(iterationsLabel);
        statsPanel.setBorder(BorderFactory.createTitledBorder("Statistics"));

        // Main Layout
        setLayout(new BorderLayout(10, 10));
        JPanel topPanel = new JPanel(new BorderLayout(5, 5));
        topPanel.add(inputPanel, BorderLayout.NORTH);
        topPanel.add(controlPanel, BorderLayout.SOUTH);

        add(topPanel, BorderLayout.NORTH);
        add(boardPanel, BorderLayout.CENTER);
        add(statsPanel, BorderLayout.SOUTH);

        // Add padding
        ((JPanel) getContentPane()).setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    }

    // Mengatur layout komponen
    private void layoutComponents() {
        pack();
    }

    // Handler untuk tombol Browse
    private void handleBrowseButton() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new FileNameExtensionFilter("Text Files", "txt"));

        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            filePathField.setText(fileChooser.getSelectedFile().getAbsolutePath());
            resetBoard();
        }
    }

    // Update tampilan board saat solving
    @Override
    public void updateVisualization(Board board) {
        updateBoard(board);
    }

    // Handler untuk tombol Stop
    private void handleStopButton() {
        stopRequested = true;
        stopButton.setEnabled(false);
    }

    // Handler untuk tombol Solve
    private void handleSolveButton() {
        if (filePathField.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Masukkan file puzzlenya terlebih dahulu.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Reset flag dan update state tombol
        stopRequested = false;
        solveButton.setEnabled(false);
        stopButton.setEnabled(true);
        resetButton.setEnabled(false);
        saveButton.setEnabled(false);

        // Mulai proses solving di thread terpisah
        new Thread(() -> {
            try {
                PuzzleData puzzleData = PuzzleReader.readFromFile(filePathField.getText());
                BruteForceSolver solver = new BruteForceSolver(
                        puzzleData.getRows(),
                        puzzleData.getCols(),
                        puzzleData.getPieces(),
                        this,
                        () -> stopRequested);

                solution = solver.solve();

                SwingUtilities.invokeLater(() -> {
                    stopButton.setEnabled(false);
                    solveButton.setEnabled(true);
                    resetButton.setEnabled(true);

                    if (solution != null && !stopRequested) {
                        updateBoard(solution.getBoard());
                        timeLabel.setText("Waktu: " + solution.getExecutionTime() + " ms");
                        iterationsLabel.setText("Pengulangan: " + solution.getIterationCount());
                        saveButton.setEnabled(true);
                    } else if (stopRequested) {
                        JOptionPane.showMessageDialog(this,
                                "Proses pencarian dihentikan",
                                "Stopped",
                                JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(this,
                                "Tidak ada solusi mas!",
                                "Result",
                                JOptionPane.INFORMATION_MESSAGE);
                    }
                });
            } catch (Exception e) {
                SwingUtilities.invokeLater(() -> {
                    stopButton.setEnabled(false);
                    solveButton.setEnabled(true);
                    resetButton.setEnabled(true);
                    JOptionPane.showMessageDialog(this,
                            "Error saat menyelesaikan puzzle: " + e.getMessage(),
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                });
            }
        }).start();
    }

    // Handler untuk tombol Reset
    private void handleResetButton() {
        resetBoard();
    }

    // Handler untuk tombol Save
    private void handleSaveButton() {
        if (solution == null)
            return;

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("Text Files", "txt"));
        fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("PNG Images", "png"));
        fileChooser.setAcceptAllFileFilterUsed(false);

        // Set default nama file
        fileChooser.setSelectedFile(new File("solusi.txt"));

        if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            try {
                File file = fileChooser.getSelectedFile();
                String path = file.getPath();

                // Pastikan ekstensi file benar
                if (!path.endsWith(".txt") && !path.endsWith(".png")) {
                    FileNameExtensionFilter filter = (FileNameExtensionFilter) fileChooser.getFileFilter();
                    String ext = filter.getExtensions()[0];
                    path = path + "." + ext;
                    file = new File(path);
                }

                System.out.println("Menyimpan ke: " + path); // Debug print

                // Save sesuai format
                if (path.endsWith(".txt")) {
                    saveToTxt(file);
                } else if (path.endsWith(".png")) {
                    saveToImage(file);
                }

                JOptionPane.showMessageDialog(this,
                        "Solusi berhasil disimpan ke: " + path,
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception e) {
                e.printStackTrace(); // Print stack trace untuk debug
                JOptionPane.showMessageDialog(this,
                        "Error saat menyimpan solusi: " + e.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // Menyimpan solusi ke file txt
    private void saveToTxt(File file) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            // Write board configuration
            char[][] grid = solution.getBoard().getGrid();
            for (int i = 0; i < solution.getBoard().getRows(); i++) {
                for (int j = 0; j < solution.getBoard().getCols(); j++) {
                    writer.write(grid[i][j] + " ");
                }
                writer.newLine();
            }

            // Write statistics
            writer.newLine();
            writer.write("Execution time: " + solution.getExecutionTime() + " ms");
            writer.newLine();
            writer.write("Iterations: " + solution.getIterationCount());
        }
    }

    // Menyimpan solusi ke file gambar
    private void saveToImage(File file) throws IOException {
        ImageGenerator.saveToImage(solution, file.getPath());
    }

    // Reset tampilan board ke kondisi awal
    private void resetBoard() {
        boardPanel.removeAll();
        timeLabel.setText("Waktu: -");
        iterationsLabel.setText("Pengulangan: -");
        saveButton.setEnabled(false);
        solution = null;
        boardPanel.revalidate();
        boardPanel.repaint();
    }

    // Update tampilan board dengan konfigurasi baru
    private void updateBoard(Board board) {
        boardPanel.removeAll();
        boardPanel.setLayout(new GridLayout(board.getRows(), board.getCols(), 1, 1));

        // Tambahkan animasi fade in/out
        for (int i = 0; i < board.getRows(); i++) {
            for (int j = 0; j < board.getCols(); j++) {
                JPanel cell = new JPanel();
                cell.setPreferredSize(new Dimension(CELL_SIZE, CELL_SIZE));
                char piece = board.getGrid()[i][j];

                if (piece != '.') {
                    cell.setBackground(getPieceColor(piece));
                    JLabel label = new JLabel(String.valueOf(piece));
                    label.setForeground(Color.WHITE);
                    label.setFont(label.getFont().deriveFont(Font.BOLD));
                    cell.add(label);
                } else {
                    cell.setBackground(new Color(240, 240, 240));
                }

                cell.setBorder(BorderFactory.createLineBorder(Color.GRAY));
                boardPanel.add(cell);
            }
        }

        boardPanel.revalidate();
        boardPanel.repaint();
    }

    // Mendapatkan warna untuk piece berdasarkan huruf (A-Z)
    private Color getPieceColor(char piece) {
        int index = piece - 'A';
        if (index >= 0 && index < PIECE_COLORS.length) {
            return PIECE_COLORS[index];
        }
        return Color.GRAY;
    }

}