
package view;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JTabbedPane;
import javax.swing.KeyStroke;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

public class MainFrame extends JFrame {

    public MainFrame() {

        setTitle("Dompet Deadline");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JTabbedPane menu = new JTabbedPane();

        DashboardPanel dashboard = new DashboardPanel();

        TransactionPanel transaksi = new TransactionPanel(dashboard);
        CategoryPanel kategori = new CategoryPanel();
        TaskPanel tugas = new TaskPanel(dashboard);

        menu.addTab("Dashboard", dashboard);
        menu.addTab("Transaksi", transaksi);
        menu.addTab("Kategori", kategori);
        menu.addTab("Tugas", tugas);

        add(menu);

        JCheckBoxMenuItem modeGelap = new JCheckBoxMenuItem("Mode Gelap");
        modeGelap.setSelected(AppTheme.isDark());
        modeGelap.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_D, InputEvent.CTRL_DOWN_MASK));
        modeGelap.addActionListener(e -> {
            AppTheme.setMode(modeGelap.isSelected()
                    ? AppTheme.Mode.DARK
                    : AppTheme.Mode.LIGHT);
            AppTheme.applyTo(this);
            dashboard.perbaruiTugas();
        });

        JMenu tampilan = new JMenu("Tampilan");
        tampilan.add(modeGelap);

        JMenuBar menuBar = new JMenuBar();
        menuBar.add(tampilan);
        setJMenuBar(menuBar);
    }
}
