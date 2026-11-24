
package view;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {

    public MainFrame() {

        setTitle("Dompet Deadline");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JTabbedPane menu = new JTabbedPane();

        // Halaman Dashboard
        JPanel dashboard = new JPanel(new BorderLayout());
        dashboard.setBackground(new Color(245, 247, 250));

        JLabel judul = new JLabel(
                "SELAMAT DATANG DI DOMPET DEADLINE",
                SwingConstants.CENTER
        );
        judul.setFont(new Font("Arial", Font.BOLD, 24));

        JLabel deskripsi = new JLabel(
                "Kelola keuangan dan tugas kuliahmu di sini.",
                SwingConstants.CENTER
        );

        JPanel isiDashboard = new JPanel(new GridLayout(2, 1));
        isiDashboard.add(judul);
        isiDashboard.add(deskripsi);

        dashboard.add(isiDashboard, BorderLayout.CENTER);

        // Halaman Transaksi
        TransactionPanel transaksi = new TransactionPanel();
        CategoryPanel kategori = new CategoryPanel();
        TaskPanel tugas = new TaskPanel();
        
        menu.addTab("Dashboard", dashboard);
        menu.addTab("Transaksi", transaksi);
        menu.addTab("Kategori", kategori);
        menu.addTab("Tugas", tugas);
        add(menu);
    }
}
