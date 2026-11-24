
package view;

import javax.swing.*;

public class MainFrame extends JFrame {

    public MainFrame() {

        setTitle("Dompet Deadline");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JTabbedPane menu = new JTabbedPane();

        // Halaman Dashboard
        DashboardPanel dashboard = new DashboardPanel();

        // Halaman lainnya
        TransactionPanel transaksi = new TransactionPanel(dashboard);
        CategoryPanel kategori = new CategoryPanel();
        TaskPanel tugas = new TaskPanel();

        menu.addTab("Dashboard", dashboard);
        menu.addTab("Transaksi", transaksi);
        menu.addTab("Kategori", kategori);
        menu.addTab("Tugas", tugas);

        add(menu);
    }
}
