package view;

import javax.swing.JComponent;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import javax.swing.JViewport;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import java.awt.Color;
import java.awt.Component;
import java.awt.Window;
import java.util.prefs.Preferences;

/**
 * Mengelola palet warna aplikasi (light/dark) dan preferensi pengguna.
 * Semua warna diambil dari tabel warna yang diberikan pada gambar.
 */
public final class AppTheme {

    public enum Mode { LIGHT, DARK }

    /** Client property untuk menentukan warna latar khusus sebuah panel. */
    public static final String BACKGROUND_ROLE = "AppTheme.backgroundRole";
    public static final String CARD_KEY = "AppTheme.cardBackground";

    private static final Preferences PREFS =
            Preferences.userNodeForPackage(AppTheme.class);
    private static final String MODE_KEY = "mode";

    // Light mode
    private static final Color LIGHT_BG_PRIMARY = Color.decode("#F5F2EA");
    private static final Color LIGHT_BG_SECONDARY = Color.decode("#FBF7EF");
    private static final Color LIGHT_BG_CARD = Color.decode("#FFFFFF");
    private static final Color LIGHT_BG_SUBTLE = Color.decode("#DCE8DF");
    private static final Color LIGHT_TEXT_PRIMARY = Color.decode("#333F38");
    private static final Color LIGHT_TEXT_SECONDARY = Color.decode("#718F7C");
    private static final Color LIGHT_BORDER = Color.decode("#DCE8DF");
    private static final Color LIGHT_BUTTON = Color.decode("#718F7C");
    private static final Color LIGHT_BUTTON_HOVER = Color.decode("#4A5C44");

    // Dark mode
    private static final Color DARK_BG_PRIMARY = Color.decode("#171F1A");
    private static final Color DARK_BG_SECONDARY = Color.decode("#102821");
    private static final Color DARK_BG_CARD = Color.decode("#222026");
    private static final Color DARK_BG_SUBTLE = Color.decode("#303034");
    private static final Color DARK_TEXT_PRIMARY = Color.decode("#F5F2EA");
    private static final Color DARK_TEXT_SECONDARY = Color.decode("#C3CEC5");
    private static final Color DARK_BORDER = Color.decode("#3B4A40");
    private static final Color DARK_BUTTON = Color.decode("#A8BFAF");
    private static final Color DARK_BUTTON_HOVER = Color.decode("#BFD2A4");

    private static Mode current = Mode.LIGHT;

    private AppTheme() {
    }

    /** Memuat mode yang terakhir dipilih sebelum aplikasi dibuka. */
    public static void load() {
        Mode tersimpan;
        try {
            tersimpan = Mode.valueOf(PREFS.get(MODE_KEY, Mode.LIGHT.name()));
        } catch (IllegalArgumentException | SecurityException e) {
            tersimpan = Mode.LIGHT;
        }
        apply(tersimpan);
    }

    public static Mode current() {
        return current;
    }

    public static boolean isDark() {
        return current == Mode.DARK;
    }

    public static void setMode(Mode mode) {
        apply(mode);
        try {
            PREFS.put(MODE_KEY, mode.name());
        } catch (SecurityException ignored) {
            // Preferensi tidak tersedia: mode tetap berlaku untuk sesi ini.
        }
    }

    public static void toggle() {
        setMode(isDark() ? Mode.LIGHT : Mode.DARK);
    }

    /** Warna tombol teks disesuaikan agar tetap kontras dengan warna tombol. */
    private static Color buttonText(Mode mode) {
        return mode == Mode.DARK ? DARK_BG_PRIMARY : LIGHT_BG_CARD;
    }

    public static Color bgPrimary() {
        return current == Mode.DARK ? DARK_BG_PRIMARY : LIGHT_BG_PRIMARY;
    }

    public static Color bgSecondary() {
        return current == Mode.DARK ? DARK_BG_SECONDARY : LIGHT_BG_SECONDARY;
    }

    public static Color bgCard() {
        return current == Mode.DARK ? DARK_BG_CARD : LIGHT_BG_CARD;
    }

    public static Color bgSubtle() {
        return current == Mode.DARK ? DARK_BG_SUBTLE : LIGHT_BG_SUBTLE;
    }

    public static Color textPrimary() {
        return current == Mode.DARK ? DARK_TEXT_PRIMARY : LIGHT_TEXT_PRIMARY;
    }

    public static Color textSecondary() {
        return current == Mode.DARK ? DARK_TEXT_SECONDARY : LIGHT_TEXT_SECONDARY;
    }

    public static Color border() {
        return current == Mode.DARK ? DARK_BORDER : LIGHT_BORDER;
    }

    public static Color button() {
        return current == Mode.DARK ? DARK_BUTTON : LIGHT_BUTTON;
    }

    public static Color buttonHover() {
        return current == Mode.DARK ? DARK_BUTTON_HOVER : LIGHT_BUTTON_HOVER;
    }

    public static Color buttonText() {
        return buttonText(current);
    }

    /** Dua warna grafik yang masih berasal dari palet yang diberikan. */
    public static Color chartIncome() {
        return button();
    }

    public static Color chartExpense() {
        return buttonHover();
    }

    /** Format warna untuk dipakai di dalam label HTML. */
    public static String htmlColor(Color color) {
        return String.format("#%06X", color.getRGB() & 0xFFFFFF);
    }

    private static void apply(Mode mode) {
        current = mode;

        Color latar = mode == Mode.DARK ? DARK_BG_PRIMARY : LIGHT_BG_PRIMARY;
        Color sekunder = mode == Mode.DARK ? DARK_BG_SECONDARY : LIGHT_BG_SECONDARY;
        Color kartu = mode == Mode.DARK ? DARK_BG_CARD : LIGHT_BG_CARD;
        Color halus = mode == Mode.DARK ? DARK_BG_SUBTLE : LIGHT_BG_SUBTLE;
        Color teks = mode == Mode.DARK ? DARK_TEXT_PRIMARY : LIGHT_TEXT_PRIMARY;
        Color teksSekunder = mode == Mode.DARK ? DARK_TEXT_SECONDARY : LIGHT_TEXT_SECONDARY;
        Color tepi = mode == Mode.DARK ? DARK_BORDER : LIGHT_BORDER;
        Color tombol = mode == Mode.DARK ? DARK_BUTTON : LIGHT_BUTTON;
        Color tombolHover = mode == Mode.DARK ? DARK_BUTTON_HOVER : LIGHT_BUTTON_HOVER;
        Color teksTombol = buttonText(mode);

        UIManager.put(CARD_KEY, kartu);

        UIManager.put("Panel.background", latar);
        UIManager.put("Panel.foreground", teks);
        UIManager.put("Window.background", latar);
        UIManager.put("RootPane.background", latar);
        UIManager.put("Label.background", latar);
        UIManager.put("Label.foreground", teks);

        UIManager.put("Button.background", tombol);
        UIManager.put("Button.foreground", teksTombol);
        UIManager.put("ToggleButton.background", halus);
        UIManager.put("ToggleButton.foreground", teks);
        UIManager.put("CheckBox.background", latar);
        UIManager.put("CheckBox.foreground", teks);
        UIManager.put("RadioButton.background", latar);
        UIManager.put("RadioButton.foreground", teks);

        UIManager.put("TextField.background", kartu);
        UIManager.put("TextField.foreground", teks);
        UIManager.put("TextField.caretForeground", teks);
        UIManager.put("TextField.selectionBackground", tombolHover);
        UIManager.put("TextField.selectionForeground", teksTombol);
        UIManager.put("PasswordField.background", kartu);
        UIManager.put("PasswordField.foreground", teks);
        UIManager.put("FormattedTextField.background", kartu);
        UIManager.put("FormattedTextField.foreground", teks);
        UIManager.put("TextArea.background", kartu);
        UIManager.put("TextArea.foreground", teks);
        UIManager.put("TextPane.background", kartu);
        UIManager.put("TextPane.foreground", teks);
        UIManager.put("EditorPane.background", kartu);
        UIManager.put("EditorPane.foreground", teks);

        UIManager.put("ComboBox.background", kartu);
        UIManager.put("ComboBox.foreground", teks);
        UIManager.put("ComboBox.selectionBackground", tombolHover);
        UIManager.put("ComboBox.selectionForeground", teksTombol);
        UIManager.put("List.background", kartu);
        UIManager.put("List.foreground", teks);
        UIManager.put("List.selectionBackground", tombolHover);
        UIManager.put("List.selectionForeground", teksTombol);

        UIManager.put("Table.background", kartu);
        UIManager.put("Table.foreground", teks);
        UIManager.put("Table.gridColor", tepi);
        UIManager.put("Table.selectionBackground", tombolHover);
        UIManager.put("Table.selectionForeground", teksTombol);
        UIManager.put("TableHeader.background", halus);
        UIManager.put("TableHeader.foreground", teks);

        UIManager.put("ScrollPane.background", latar);
        UIManager.put("Viewport.background", kartu);
        UIManager.put("TabbedPane.background", sekunder);
        UIManager.put("TabbedPane.foreground", teks);
        UIManager.put("TabbedPane.selected", kartu);
        UIManager.put("TabbedPane.unselectedBackground", sekunder);
        UIManager.put("TabbedPane.contentAreaColor", latar);

        UIManager.put("OptionPane.background", latar);
        UIManager.put("OptionPane.foreground", teks);
        UIManager.put("OptionPane.messageForeground", teks);
        UIManager.put("MenuBar.background", sekunder);
        UIManager.put("MenuBar.foreground", teks);
        UIManager.put("Menu.background", sekunder);
        UIManager.put("Menu.foreground", teks);
        UIManager.put("Menu.selectionBackground", tombolHover);
        UIManager.put("Menu.selectionForeground", teksTombol);
        UIManager.put("MenuItem.background", kartu);
        UIManager.put("MenuItem.foreground", teks);
        UIManager.put("MenuItem.selectionBackground", tombolHover);
        UIManager.put("MenuItem.selectionForeground", teksTombol);
        UIManager.put("CheckBoxMenuItem.background", kartu);
        UIManager.put("CheckBoxMenuItem.foreground", teks);
        UIManager.put("CheckBoxMenuItem.selectionBackground", tombolHover);
        UIManager.put("CheckBoxMenuItem.selectionForeground", teksTombol);
        UIManager.put("ToolBar.background", sekunder);
        UIManager.put("Separator.foreground", tepi);
        UIManager.put("Separator.background", tepi);
        UIManager.put("TitledBorder.titleColor", teksSekunder);
    }

    /** Memperbarui seluruh komponen yang sudah tampil setelah mode diganti. */
    public static void applyTo(Window window) {
        SwingUtilities.updateComponentTreeUI(window);
        refreshBackgrounds(window);
        window.repaint();
    }

    private static void refreshBackgrounds(Component component) {
        if (component instanceof JComponent ui) {
            Object role = ui.getClientProperty(BACKGROUND_ROLE);
            if (role instanceof String key) {
                component.setBackground(UIManager.getColor(key));
            } else if (component instanceof JMenuBar) {
                component.setBackground(UIManager.getColor("MenuBar.background"));
            } else if (component instanceof JToolBar) {
                component.setBackground(UIManager.getColor("ToolBar.background"));
            } else if (component instanceof JViewport) {
                component.setBackground(UIManager.getColor("Viewport.background"));
            } else if (component instanceof JPanel) {
                component.setBackground(UIManager.getColor("Panel.background"));
            }
        }

        if (component instanceof java.awt.Container container) {
            for (Component child : container.getComponents()) {
                refreshBackgrounds(child);
            }
        }
    }
}
