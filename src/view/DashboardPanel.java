
package view;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import repository.TransactionRepository;

public class DashboardPanel extends JPanel {

    private JLabel saldo;
    private TransactionRepository transactionRepository;

    public DashboardPanel() {

        transactionRepository = new TransactionRepository();

        setLayout(new BorderLayout(10, 10));
        setBackground(new Color(245, 247, 250));

        JLabel judul = new JLabel(
                "DASHBOARD DOMPET DEADLINE",
                SwingConstants.CENTER
        );
        judul.setFont(new Font("Arial", Font.BOLD, 24));
        judul.setBorder(
                BorderFactory.createEmptyBorder(20, 10, 20, 10)
        );

        saldo = new JLabel("", SwingConstants.CENTER);
        saldo.setFont(new Font("Arial", Font.BOLD, 18));

        JLabel deadline = new JLabel(
                "Tiga Deadline Terdekat: Belum ada data",
                SwingConstants.CENTER
        );

        JLabel biaya = new JLabel(
                "Total Estimasi Biaya Tugas Aktif: Rp 0",
                SwingConstants.CENTER
        );

        Font fontInfo = new Font("Arial", Font.BOLD, 16);
        deadline.setFont(fontInfo);
        biaya.setFont(fontInfo);

        JPanel isi = new JPanel(new GridLayout(3, 1, 10, 15));
        isi.setBackground(new Color(245, 247, 250));
        isi.setBorder(
                BorderFactory.createEmptyBorder(20, 40, 20, 40)
        );

        isi.add(saldo);
        isi.add(deadline);
        isi.add(biaya);

        add(judul, BorderLayout.NORTH);
        add(isi, BorderLayout.CENTER);

        perbaruiSaldo();
    }

    public void perbaruiSaldo() {
        try {
            int jumlahSaldo =
                    transactionRepository.getMonthlyBalance();

            saldo.setText(
                    "Saldo Bulan Ini: Rp "
                    + String.format("%,d", jumlahSaldo)
                            .replace(',', '.')
            );

        } catch (SQLException e) {
            saldo.setText("Saldo Bulan Ini: Gagal dimuat");
            e.printStackTrace();
        }
    }
}
