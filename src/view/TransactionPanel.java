
package view;

import model.Transaction;
import repository.TransactionRepository;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.time.LocalDate;

public class TransactionPanel extends JPanel {

    private JComboBox<String> typeBox;
    private JTextField amountField;
    private JTextField categoryField;
    private JTextField noteField;

    private TransactionRepository repository;
    private DefaultTableModel tableModel;
    private JTable transactionTable;

    public TransactionPanel() {

        repository = new TransactionRepository();

        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Form transaksi
        JPanel form = new JPanel(new GridLayout(6, 2, 10, 10));

        form.add(new JLabel("Jenis Transaksi"));
        typeBox = new JComboBox<>(
                new String[]{"Pemasukan", "Pengeluaran"});
        form.add(typeBox);

        form.add(new JLabel("Jumlah Uang"));
        amountField = new JTextField();
        form.add(amountField);

        form.add(new JLabel("Kategori"));
        categoryField = new JTextField();
        form.add(categoryField);

        form.add(new JLabel("Catatan"));
        noteField = new JTextField();
        form.add(noteField);

        JButton saveButton = new JButton("Simpan Transaksi");
        JButton clearButton = new JButton("Bersihkan");
        JButton editButton = new JButton("Edit Transaksi");
        JButton deleteButton = new JButton("Hapus Transaksi");

        form.add(saveButton);
        form.add(clearButton);
        form.add(editButton);
        form.add(deleteButton);

        // Tabel transaksi
        String[] columns = {
            "ID", "Jenis", "Tanggal",
            "Jumlah", "Kategori", "Catatan"
        };

        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        transactionTable = new JTable(tableModel);

        JScrollPane scrollPane =
                new JScrollPane(transactionTable);

        // Mengambil data ketika baris tabel dipilih
        transactionTable.getSelectionModel()
                .addListSelectionListener(e -> {

            if (!e.getValueIsAdjusting()) {
                int selectedRow = transactionTable.getSelectedRow();

                if (selectedRow != -1) {
                    typeBox.setSelectedItem(
                            tableModel.getValueAt(selectedRow, 1).toString()
                    );

                    amountField.setText(
                            tableModel.getValueAt(selectedRow, 3).toString()
                    );

                    categoryField.setText(
                            tableModel.getValueAt(selectedRow, 4).toString()
                    );

                    Object note = tableModel.getValueAt(selectedRow, 5);
                    noteField.setText(note == null ? "" : note.toString());
                }
            }
        });

        add(form, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        // Aksi tombol
        saveButton.addActionListener(e -> saveTransaction());
        clearButton.addActionListener(e -> clearForm());
        editButton.addActionListener(e -> editTransaction());
        deleteButton.addActionListener(e -> deleteTransaction());

        loadTransactions();
    }

    // Menambahkan transaksi
    private void saveTransaction() {

        try {
            String type = (String) typeBox.getSelectedItem();
            int amount = Integer.parseInt(amountField.getText());
            String category = categoryField.getText().trim();
            String note = noteField.getText().trim();

            if (amount <= 0 || category.isEmpty()) {
                JOptionPane.showMessageDialog(
                        this, "Jumlah dan kategori harus diisi!");
                return;
            }

            Transaction transaction = new Transaction(
                    0,
                    type,
                    LocalDate.now(),
                    amount,
                    category,
                    note,
                    null
            );

            repository.addTransaction(transaction);

            JOptionPane.showMessageDialog(
                    this, "Transaksi berhasil disimpan!");

            clearForm();
            loadTransactions();

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(
                    this, "Jumlah uang harus berupa angka!");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                    this, "Gagal menyimpan: " + e.getMessage());
        }
    }

    // Menampilkan seluruh transaksi
    private void loadTransactions() {

        try {
            tableModel.setRowCount(0);

            List<Transaction> transactions =
                    repository.getAllTransactions();

            for (Transaction transaction : transactions) {
                tableModel.addRow(new Object[]{
                    transaction.getId(),
                    transaction.getType(),
                    transaction.getDate(),
                    transaction.getAmount(),
                    transaction.getCategory(),
                    transaction.getNote()
                });
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                    this, "Gagal mengambil data: " + e.getMessage());
        }
    }

    // Membersihkan form
    private void clearForm() {
        amountField.setText("");
        categoryField.setText("");
        noteField.setText("");
        transactionTable.clearSelection();
    }

    // Mengedit transaksi
    private void editTransaction() {

        int selectedRow = transactionTable.getSelectedRow();

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(
                    this, "Pilih transaksi yang ingin diedit!");
            return;
        }

        try {
            int id = Integer.parseInt(
                    tableModel.getValueAt(selectedRow, 0).toString()
            );

            String type = (String) typeBox.getSelectedItem();
            int amount = Integer.parseInt(amountField.getText());
            String category = categoryField.getText().trim();
            String note = noteField.getText().trim();

            if (amount <= 0 || category.isEmpty()) {
                JOptionPane.showMessageDialog(
                        this, "Jumlah dan kategori harus diisi!");
                return;
            }

            Transaction transaction = new Transaction(
                    id,
                    type,
                    LocalDate.parse(
                            tableModel.getValueAt(selectedRow, 2).toString()
                    ),
                    amount,
                    category,
                    note,
                    null
            );

            repository.updateTransaction(transaction);

            JOptionPane.showMessageDialog(
                    this, "Transaksi berhasil diedit!");

            clearForm();
            loadTransactions();

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(
                    this, "Jumlah uang harus berupa angka!");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                    this, "Gagal mengedit: " + e.getMessage());
        }
    }

    // Menghapus transaksi
    private void deleteTransaction() {

        int selectedRow = transactionTable.getSelectedRow();

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(
                    this, "Pilih transaksi yang ingin dihapus!");
            return;
        }

        int konfirmasi = JOptionPane.showConfirmDialog(
                this,
                "Yakin ingin menghapus transaksi ini?",
                "Konfirmasi Hapus",
                JOptionPane.YES_NO_OPTION
        );

        if (konfirmasi == JOptionPane.YES_OPTION) {

            try {
                int id = Integer.parseInt(
                        tableModel.getValueAt(selectedRow, 0).toString()
                );

                repository.deleteTransaction(id);

                JOptionPane.showMessageDialog(
                        this, "Transaksi berhasil dihapus!");

                clearForm();
                loadTransactions();

            } catch (Exception e) {
                JOptionPane.showMessageDialog(
                        this, "Gagal menghapus: " + e.getMessage());
            }
        }
    }
}
