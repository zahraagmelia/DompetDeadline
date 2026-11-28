
import javax.swing.SwingUtilities;
import view.AppTheme;
import view.MainFrame;
import repository.DatabaseInitializer;
import service.TaskReminderService;

public class Main {

    public static void main(String[] args) {

        // Memakai tema (light/dark) yang terakhir dipilih
        AppTheme.load();

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
