import javax.swing.SwingUtilities;
import view.MainFrame;

public class Main {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainFrame aplikasi = new MainFrame();
            aplikasi.setVisible(true);
        });
    }
}
