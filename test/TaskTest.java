
import model.Task;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class TaskTest {

    @Test
    void testDataTugas() {
        Task task = new Task(
                1,
                "Membuat Laporan",
                "Pemrograman",
                LocalDate.of(2026, 12, 1),
                "Tinggi",
                "Belum Selesai",
                50000
        );

        assertEquals("Membuat Laporan", task.getTitle());
        assertEquals("Pemrograman", task.getCourse());
    }

    @Test
    void testUbahStatusTugas() {
        Task task = new Task(
                1,
                "Membuat Laporan",
                "Pemrograman",
                LocalDate.of(2026, 12, 1),
                "Tinggi",
                "Belum Selesai",
                50000
        );

        task.setStatus("Selesai");

        assertEquals("Selesai", task.getStatus());
    }

    @Test
    void testBiayaTugas() {
        Task task = new Task(
                1,
                "Membuat Laporan",
                "Pemrograman",
                LocalDate.of(2026, 12, 1),
                "Tinggi",
                "Belum Selesai",
                50000
        );

        assertEquals(50000, task.getEstimatedCost());
    }
}
