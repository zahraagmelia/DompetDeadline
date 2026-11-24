
package view;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

import model.Task;
import repository.TransactionRepository;
import repository.TaskRepository;

public class DashboardPanel extends JPanel {

    private JLabel saldo;
    private JLabel deadline;
    private JLabel biaya;

    private TransactionRepository transactionRepository;
    private TaskRepository taskRepository;

    private MonthlyChartPanel grafik;

    public DashboardPanel() {

        transactionRepository = new TransactionRepository();
        taskRepository = new TaskRepository();

        setLayout(new BorderLayout(10, 10));
        setBackground(new Color(245, 247, 250));

        JLabel judul = new JLabel(
                "DASHBOARD DOMPET DEADLINE",
                SwingConstants.CENTER
        );
        judul.setFont(new Font("Arial", Font.BOLD, 24));
        judul.setBorder(
                BorderFactory.createEmptyBorder(15, 10, 15, 10)
        );

        saldo = new JLabel("", SwingConstants.CENTER);
        deadline = new JLabel("", SwingConstants.CENTER);
        biaya = new JLabel("", SwingConstants.CENTER);

        Font fontInfo = new Font("Arial", Font.BOLD, 16);
        saldo.setFont(fontInfo);
        deadline.setFont(fontInfo);
        biaya.setFont(fontInfo);

        JPanel informasi = new JPanel(new GridLayout(3, 1, 5, 5));
        informasi.setBackground(new Color(245, 247, 250));
        informasi.add(saldo);
        informasi.add(deadline);
        informasi.add(biaya);

        grafik = new MonthlyChartPanel();

        JPanel isi = new JPanel(new BorderLayout(10, 10));
        isi.setBackground(new Color(245, 247, 250));
        isi.setBorder(
                BorderFactory.createEmptyBorder(10, 30, 15, 30)
        );

        isi.add(informasi, BorderLayout.NORTH);
        isi.add(grafik, BorderLayout.CENTER);

        add(judul, BorderLayout.NORTH);
        add(isi, BorderLayout.CENTER);

        perbaruiDashboard();
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

    public void perbaruiTugas() {
        try {
            List<Task> daftarTugas =
                    taskRepository.getUpcomingTasks();

            if (daftarTugas.isEmpty()) {
                deadline.setText(
                        "Tiga Deadline Terdekat: Belum ada tugas"
                );
            } else {
                StringBuilder teks = new StringBuilder(
                        "<html><center>Deadline Terdekat:<br>"
                );

                for (Task tugas : daftarTugas) {
                    teks.append(tugas.getTitle())
                        .append(" - ")
                        .append(tugas.getDeadline())
                        .append("<br>");
                }

                teks.append("</center></html>");
                deadline.setText(teks.toString());
            }

            int totalBiaya =
                    taskRepository.getTotalActiveTaskCost();

            biaya.setText(
                    "Total Estimasi Biaya Tugas Aktif: Rp "
                    + String.format("%,d", totalBiaya)
                            .replace(',', '.')
            );

        } catch (SQLException e) {
            deadline.setText("Deadline: Gagal dimuat");
            biaya.setText("Biaya tugas: Gagal dimuat");
            e.printStackTrace();
        }
    }

    public void perbaruiGrafik() {
        try {
            int pemasukan =
                    transactionRepository.getMonthlyIncome();

            int pengeluaran =
                    transactionRepository.getMonthlyExpense();

            grafik.setData(pemasukan, pengeluaran);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void perbaruiDashboard() {
        perbaruiSaldo();
        perbaruiTugas();
        perbaruiGrafik();
    }
}
