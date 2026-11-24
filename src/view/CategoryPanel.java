package view;

import model.Category;
import repository.CategoryRepository;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class CategoryPanel extends JPanel {

    private JTextField nameField;
    private JComboBox<String> typeBox;
    private JTable categoryTable;
    private DefaultTableModel tableModel;
    private CategoryRepository repository;

    public CategoryPanel() {
        repository = new CategoryRepository();

        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel form = new JPanel(new GridLayout(3, 2, 10, 10));

        form.add(new JLabel("Nama Kategori"));
        nameField = new JTextField();
        form.add(nameField);

        form.add(new JLabel("Jenis Kategori"));
        typeBox = new JComboBox<>(
                new String[]{"Pemasukan", "Pengeluaran"}
        );
        form.add(typeBox);

        JButton saveButton = new JButton("Simpan");
        JButton editButton = new JButton("Edit");
        JButton deleteButton = new JButton("Hapus");
        JButton clearButton = new JButton("Bersihkan");

        form.add(saveButton);
        form.add(editButton);

        JPanel buttons = new JPanel();
        buttons.add(deleteButton);
        buttons.add(clearButton);

        tableModel = new DefaultTableModel(
                new String[]{"ID", "Nama Kategori", "Jenis"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        categoryTable = new JTable(tableModel);

        add(form, BorderLayout.NORTH);
        add(new JScrollPane(categoryTable), BorderLayout.CENTER);
        add(buttons, BorderLayout.SOUTH);

        saveButton.addActionListener(e -> saveCategory());
        editButton.addActionListener(e -> editCategory());
        deleteButton.addActionListener(e -> deleteCategory());
        clearButton.addActionListener(e -> clearForm());

        categoryTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int row = categoryTable.getSelectedRow();

                if (row != -1) {
                    nameField.setText(
                            tableModel.getValueAt(row, 1).toString()
                    );
                    typeBox.setSelectedItem(
                            tableModel.getValueAt(row, 2).toString()
                    );
                }
            }
        });

        loadCategories();
    }

    private void saveCategory() {
        String name = nameField.getText().trim();
        String type = (String) typeBox.getSelectedItem();

        if (name.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this, "Nama kategori harus diisi!"
            );
            return;
        }

        try {
            repository.addCategory(new Category(0, name, type));

            JOptionPane.showMessageDialog(
                    this, "Kategori berhasil disimpan!"
            );

            clearForm();
            loadCategories();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                    this, "Gagal menyimpan kategori: " + e.getMessage()
            );
        }
    }

    private void loadCategories() {
        try {
            tableModel.setRowCount(0);

            List<Category> categories = repository.getAllCategories();

            for (Category category : categories) {
                tableModel.addRow(new Object[]{
                        category.getId(),
                        category.getName(),
                        category.getType()
                });
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                    this, "Gagal mengambil kategori: " + e.getMessage()
            );
        }
    }

    private void editCategory() {
        int row = categoryTable.getSelectedRow();

        if (row == -1) {
            JOptionPane.showMessageDialog(
                    this, "Pilih kategori yang ingin diedit!"
            );
            return;
        }

        String name = nameField.getText().trim();
        String type = (String) typeBox.getSelectedItem();

        if (name.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this, "Nama kategori harus diisi!"
            );
            return;
        }

        try {
            int id = Integer.parseInt(
                    tableModel.getValueAt(row, 0).toString()
            );

            repository.updateCategory(new Category(id, name, type));

            JOptionPane.showMessageDialog(
                    this, "Kategori berhasil diedit!"
            );

            clearForm();
            loadCategories();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                    this, "Gagal mengedit kategori: " + e.getMessage()
            );
        }
    }

    private void deleteCategory() {
        int row = categoryTable.getSelectedRow();

        if (row == -1) {
            JOptionPane.showMessageDialog(
                    this, "Pilih kategori yang ingin dihapus!"
            );
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Yakin ingin menghapus kategori ini?",
                "Konfirmasi",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                int id = Integer.parseInt(
                        tableModel.getValueAt(row, 0).toString()
                );

                repository.deleteCategory(id);

                JOptionPane.showMessageDialog(
                        this, "Kategori berhasil dihapus!"
                );

                clearForm();
                loadCategories();

            } catch (Exception e) {
                JOptionPane.showMessageDialog(
                        this, "Gagal menghapus kategori: " + e.getMessage()
                );
            }
        }
    }

    private void clearForm() {
        nameField.setText("");
        typeBox.setSelectedIndex(0);
        categoryTable.clearSelection();
    }
}