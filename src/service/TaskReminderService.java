
package service;

import model.Task;
import repository.TaskRepository;
import repository.TaskReminderRepository;

import javax.swing.JOptionPane;
import javax.swing.Timer;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class TaskReminderService {

    private final TaskRepository taskRepository =
            new TaskRepository();

    private final TaskReminderRepository reminderRepository =
            new TaskReminderRepository();

    private Timer timer;
    private boolean sudahBerjalan = false;

    // Memulai pemeriksaan deadline otomatis
    public void start() {

        if (sudahBerjalan) {
            return;
        }

        sudahBerjalan = true;

        // Periksa langsung saat aplikasi dibuka
        periksaDeadline();

        // Periksa kembali setiap 1 menit
        timer = new Timer(60_000, e -> periksaDeadline());
        timer.start();
    }

    // Memeriksa seluruh deadline tugas
    private void periksaDeadline() {

        try {
            List<Task> daftarTugas =
                    taskRepository.getAllTasks();

            LocalDate hariIni = LocalDate.now();

            for (Task tugas : daftarTugas) {

                if ("Selesai".equalsIgnoreCase(tugas.getStatus())) {
                    continue;
                }

                long selisihHari = ChronoUnit.DAYS.between(
                        hariIni,
                        tugas.getDeadline()
                );

                String jenisPengingat;
                String pesan;

                if (selisihHari == 1) {
                    jenisPengingat = "BESOK";
                    pesan = "Deadline tugas besok!";
                } else if (selisihHari == 0) {
                    jenisPengingat = "HARI_INI";
                    pesan = "Deadline tugas hari ini!";
                } else if (selisihHari < 0) {
                    jenisPengingat = "TERLAMBAT";
                    pesan = "Deadline tugas sudah terlewat!";
                } else {
                    continue;
                }

                String tanggalDeadline =
                        tugas.getDeadline().toString();

                boolean sudahDiingatkan =
                        reminderRepository.sudahDiingatkan(
                                tugas.getId(),
                                tanggalDeadline,
                                jenisPengingat
                        );

                if (!sudahDiingatkan) {

                    JOptionPane.showMessageDialog(
                            null,
                            pesan
                                    + "\n\nJudul: " + tugas.getTitle()
                                    + "\nMata Kuliah: " + tugas.getCourse()
                                    + "\nDeadline: " + tanggalDeadline
                                    + "\nPrioritas: " + tugas.getPriority(),
                            "Pengingat Deadline",
                            JOptionPane.WARNING_MESSAGE
                    );

                    reminderRepository.simpanPengingat(
                            tugas.getId(),
                            tanggalDeadline,
                            jenisPengingat
                    );
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
