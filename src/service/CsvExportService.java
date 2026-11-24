
package service;

import model.Transaction;
import repository.TransactionRepository;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class CsvExportService {

    private final TransactionRepository repository =
            new TransactionRepository();

    public void exportTransactions() {

        JFileChooser fileChooser = new JFileChooser();

        fileChooser.setDialogTitle("Simpan Data Transaksi");
        fileChooser.setSelectedFile(
                new java.io.File("data_transaksi.csv")
        );

        int pilihan = fileChooser.showSaveDialog(null);

        if (pilihan != JFileChooser.APPROVE_OPTION) {
            return;
        }

        java.io.File file = fileChooser.getSelectedFile();

        if (!file.getName().toLowerCase().endsWith(".csv")) {
            file = new java.io.File(
                    file.getAbsolutePath() + ".csv"
            );
        }

        try {
            List<Transaction> transactions =
                    repository.getAllTransactions();

            try (BufferedWriter writer = new BufferedWriter(
                    new FileWriter(file, StandardCharsets.UTF_8))) {

                // BOM agar karakter UTF-8 terbaca baik di Excel
                writer.write('\uFEFF');

                writer.write(
                        "ID,Jenis,Tanggal,Jumlah,Kategori,Catatan"
                );
                writer.newLine();

                for (Transaction transaction : transactions) {

                    writer.write(
                            escape(String.valueOf(transaction.getId()))
                            + ","
                            + escape(transaction.getType())
                            + ","
                            + escape(transaction.getDate().toString())
                            + ","
                            + escape(String.valueOf(transaction.getAmount()))
                            + ","
                            + escape(transaction.getCategory())
                            + ","
                            + escape(transaction.getNote())
                    );

                    writer.newLine();
                }
            }

            JOptionPane.showMessageDialog(
                    null,
                    "Data transaksi berhasil diekspor!\n"
                            + file.getAbsolutePath()
            );

        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                    null,
                    "Gagal mengekspor data: " + e.getMessage()
            );
        }
    }

    // Mengamankan isi kolom CSV
    private String escape(String value) {

        if (value == null) {
            value = "";
        }

        value = value.replace("\"", "\"\"");

        return "\"" + value + "\"";
    }
}
