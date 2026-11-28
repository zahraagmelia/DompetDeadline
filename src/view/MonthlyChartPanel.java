
package view;

import javax.swing.*;
import java.awt.*;

public class MonthlyChartPanel extends JPanel {

    private int pemasukan;
    private int pengeluaran;

    public MonthlyChartPanel() {
        setPreferredSize(new Dimension(500, 250));
        putClientProperty(AppTheme.BACKGROUND_ROLE, AppTheme.CARD_KEY);
        setBackground(AppTheme.bgCard());
    }

    public void setData(int pemasukan, int pengeluaran) {
        this.pemasukan = pemasukan;
        this.pengeluaran = pengeluaran;

        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D grafik = (Graphics2D) g.create();

        grafik.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON
        );

        int lebar = getWidth();
        int tinggi = getHeight();

        grafik.setColor(AppTheme.textPrimary());
        grafik.setFont(new Font("Arial", Font.BOLD, 16));
        grafik.drawString("Grafik Pemasukan dan Pengeluaran", 20, 25);

        int nilaiMaksimum = Math.max(pemasukan, pengeluaran);

        if (nilaiMaksimum == 0) {
            grafik.setFont(new Font("Arial", Font.PLAIN, 14));
            grafik.drawString("Belum ada data transaksi bulan ini", 20, 90);
            grafik.dispose();
            return;
        }

        int tinggiMaksimum = tinggi - 100;
        int tinggiPemasukan =
                (int) ((double) pemasukan / nilaiMaksimum * tinggiMaksimum);
        int tinggiPengeluaran =
                (int) ((double) pengeluaran / nilaiMaksimum * tinggiMaksimum);

        int lebarBatang = 80;
        int posisiPemasukan = lebar / 3 - lebarBatang / 2;
        int posisiPengeluaran = 2 * lebar / 3 - lebarBatang / 2;
        int dasar = tinggi - 45;

        grafik.setColor(AppTheme.chartIncome());
        grafik.fillRoundRect(
                posisiPemasukan,
                dasar - tinggiPemasukan,
                lebarBatang,
                tinggiPemasukan,
                12, 12
        );

        grafik.setColor(AppTheme.chartExpense());
        grafik.fillRoundRect(
                posisiPengeluaran,
                dasar - tinggiPengeluaran,
                lebarBatang,
                tinggiPengeluaran,
                12, 12
        );

        grafik.setColor(AppTheme.textPrimary());
        grafik.setFont(new Font("Arial", Font.PLAIN, 12));

        grafik.drawString("Pemasukan", posisiPemasukan - 5, dasar + 20);
        grafik.drawString("Pengeluaran", posisiPengeluaran - 10, dasar + 20);

        grafik.setFont(new Font("Arial", Font.BOLD, 12));

        grafik.drawString(
                "Rp " + String.format("%,d", pemasukan).replace(',', '.'),
                posisiPemasukan - 15,
                dasar - tinggiPemasukan - 8
        );

        grafik.drawString(
                "Rp " + String.format("%,d", pengeluaran).replace(',', '.'),
                posisiPengeluaran - 15,
                dasar - tinggiPengeluaran - 8
        );

        grafik.dispose();
    }
}
