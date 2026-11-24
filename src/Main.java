
import javax.swing.SwingUtilities;
import view.MainFrame;
import repository.DatabaseInitializer;
import service.TaskReminderService;

public class Main {

    public static void main(String[] args) {

        // Membuat database dan tabel
        DatabaseInitializer.initialize();

        // Membuka tampilan aplikasi
        SwingUtilities.invokeLater(() -> {

            MainFrame aplikasi = new MainFrame();
            aplikasi.setVisible(true);

            // Menjalankan pengingat deadline otomatis
            TaskReminderService pengingat =
                    new TaskReminderService();

            pengingat.start();
        });
    }
}
