// File utama yang berisi main method untuk menjalankan aplikasi GUI IQ Puzzler Pro Solver

import javax.swing.*;

public class App {
    public static void main(String[] args) {
        // Mengatur tampilan GUI agar sesuai dengan sistem operasi yang digunakan
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Membuat dan menampilkan GUI 
        SwingUtilities.invokeLater(() -> {
            MainWindow mainWindow = new MainWindow();
            mainWindow.setVisible(true);
        });
    }
}