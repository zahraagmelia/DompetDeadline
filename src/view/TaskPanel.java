
package view;

import model.Task;
import repository.TaskRepository;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;

public class TaskPanel extends JPanel {

    private final TaskRepository repository = new TaskRepository();

    private JTextField txtJudul;
    private JTextField txtMataKuliah;
    private JTextField txtDeadline;
    private JTextField txtBiaya;

    private JComboBox<String> cmbPrioritas;
    private JComboBox<String> cmbStatus;

    private JTable tabel;
    private DefaultTableModel model;

    public TaskPanel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JLabel judul = new JLabel("MANAJEMEN TUGAS KULIAH");
        judul.setFont(new Font("Arial", Font.BOLD, 20));

        add(judul, BorderLayout.NORTH);

        // Form input tugas
        JPanel form = new JPanel(new GridLayout(6, 2, 8, 8));

        txtJudul = new JTextField();
        txtMataKuliah = new JTextField();
        txtDeadline = new JTextField();
        txtBiaya = new JTextField();

        cmbPrioritas = new JComboBox<>(
            new String[]{"Rendah", "Sedang", "Tinggi"}
        );

        cmbStatus = new JComboBox<>(
            new String[]{
                "Belum Dikerjakan",
                "Sedang Dikerjakan",
                "Selesai"
            }
        );

        form.add(new JLabel("Judul Tugas:"));
        form.add(txtJudul);

        form.add(new JLabel("Mata Kuliah:"));
        form.add(txtMataKuliah);

        form.add(new JLabel("Deadline (YYYY-MM-DD):"));
        form.add(txtDeadline);

        form.add(new JLabel("Prioritas:"));
        form.add(cmbPrioritas);

        form.add(new JLabel("Status:"));
        form.add(cmbStatus);

        form.add(new JLabel("Estimasi Biaya (Rp):"));
        form.add(txtBiaya);

        // Tombol
        JButton btnTambah = new JButton("Tambah");
        JButton btnEdit = new JButton("Edit");
        JButton btnHapus = new JButton("Hapus");
        JButton btnBersihkan = new JButton("Bersihkan");

        JPanel tombol = new JPanel(
            new FlowLayout(FlowLayout.CENTER)
        );

        tombol.add(btnTambah);
        tombol.add(btnEdit);
        tombol.add(btnHapus);
        tombol.add(btnBersihkan);

        // Bagian form dan tombol
        JPanel bagianAtas = new JPanel(new BorderLayout(10, 10));
        bagianAtas.add(form, BorderLayout.CENTER);
        bagianAtas.add(tombol, BorderLayout.SOUTH);

        // Tabel tugas
        model = new DefaultTableModel(
            new String[]{
                "ID", "Judul", "Mata Kuliah",
                "Deadline", "Prioritas", "Status", "Biaya"
            }, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tabel = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(tabel);

        // Menggabungkan form dan tabel
        JPanel isi = new JPanel(new BorderLayout(10, 10));
        isi.add(bagianAtas, BorderLayout.NORTH);
        isi.add(scrollPane, BorderLayout.CENTER);

        add(isi, BorderLayout.CENTER);

        // Aksi tombol
        btnTambah.addActionListener(e -> tambahTugas());
        btnEdit.addActionListener(e -> editTugas());
        btnHapus.addActionListener(e -> hapusTugas());
        btnBersihkan.addActionListener(e -> bersihkanForm());

        tabel.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                tampilkanDataTerpilih();
            }
        });

        refreshTabel();
    }

    private void tambahTugas() {
        try {
            Task task = ambilDataForm(0);

            repository.addTask(task);

            JOptionPane.showMessageDialog(
                this, "Tugas berhasil ditambahkan!"
            );

            bersihkanForm();
            refreshTabel();

        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, e.getMessage());

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(
                this, "Gagal menambahkan tugas: " + e.getMessage()
            );
        }
    }

    private void editTugas() {
        int baris = tabel.getSelectedRow();

        if (baris == -1) {
            JOptionPane.showMessageDialog(
                this, "Pilih tugas yang ingin diedit!"
            );
            return;
        }

        try {
            int id = (int) model.getValueAt(baris, 0);
            Task task = ambilDataForm(id);

            repository.updateTask(task);

            JOptionPane.showMessageDialog(
                this, "Tugas berhasil diperbarui!"
            );

            bersihkanForm();
            refreshTabel();

        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, e.getMessage());

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(
                this, "Gagal mengedit tugas: " + e.getMessage()
            );
        }
    }

    private void hapusTugas() {
        int baris = tabel.getSelectedRow();

        if (baris == -1) {
            JOptionPane.showMessageDialog(
                this, "Pilih tugas yang ingin dihapus!"
            );
            return;
        }

        int konfirmasi = JOptionPane.showConfirmDialog(
            this,
            "Yakin ingin menghapus tugas ini?",
            "Konfirmasi",
            JOptionPane.YES_NO_OPTION
        );

        if (konfirmasi == JOptionPane.YES_OPTION) {
            try {
                int id = (int) model.getValueAt(baris, 0);

                repository.deleteTask(id);

                JOptionPane.showMessageDialog(
                    this, "Tugas berhasil dihapus!"
                );

                bersihkanForm();
                refreshTabel();

            } catch (SQLException e) {
                JOptionPane.showMessageDialog(
                    this, "Gagal menghapus tugas: " + e.getMessage()
                );
            }
        }
    }

    private Task ambilDataForm(int id) {
        String judul = txtJudul.getText().trim();
        String mataKuliah = txtMataKuliah.getText().trim();
        String deadline = txtDeadline.getText().trim();
        String biaya = txtBiaya.getText().trim();

        if (judul.isEmpty() || mataKuliah.isEmpty()
                || deadline.isEmpty() || biaya.isEmpty()) {
            throw new IllegalArgumentException(
                "Semua kolom harus diisi!"
            );
        }

        try {
            LocalDate tanggal = LocalDate.parse(deadline);
            int jumlahBiaya = Integer.parseInt(biaya);

            if (jumlahBiaya < 0) {
                throw new IllegalArgumentException(
                    "Biaya tidak boleh negatif!"
                );
            }

            return new Task(
                id,
                judul,
                mataKuliah,
                tanggal,
                (String) cmbPrioritas.getSelectedItem(),
                (String) cmbStatus.getSelectedItem(),
                jumlahBiaya
            );

        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException(
                "Format deadline harus YYYY-MM-DD!"
            );

        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(
                "Estimasi biaya harus berupa angka!"
            );
        }
    }

    private void refreshTabel() {
        model.setRowCount(0);

        try {
            List<Task> tasks = repository.getAllTasks();

            for (Task task : tasks) {
                model.addRow(new Object[]{
                    task.getId(),
                    task.getTitle(),
                    task.getCourse(),
                    task.getDeadline(),
                    task.getPriority(),
                    task.getStatus(),
                    task.getEstimatedCost()
                });
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(
                this, "Gagal memuat tugas: " + e.getMessage()
            );
        }
    }

    private void tampilkanDataTerpilih() {
        int baris = tabel.getSelectedRow();

        if (baris == -1) {
            return;
        }

        txtJudul.setText(model.getValueAt(baris, 1).toString());
        txtMataKuliah.setText(model.getValueAt(baris, 2).toString());
        txtDeadline.setText(model.getValueAt(baris, 3).toString());
        cmbPrioritas.setSelectedItem(model.getValueAt(baris, 4));
        cmbStatus.setSelectedItem(model.getValueAt(baris, 5));
        txtBiaya.setText(model.getValueAt(baris, 6).toString());
    }

    private void bersihkanForm() {
        txtJudul.setText("");
        txtMataKuliah.setText("");
        txtDeadline.setText("");
        txtBiaya.setText("");

        cmbPrioritas.setSelectedIndex(0);
        cmbStatus.setSelectedIndex(0);

        tabel.clearSelection();
    }
}
