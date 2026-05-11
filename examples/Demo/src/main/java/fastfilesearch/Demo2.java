package fastfilesearch;

import fasttheme.FastTheme;
import fastui.Container;
import fastui.component.Image;
import fastui.composable.TextField;
import fastui.composable.TextArea;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * FastFileSearch Demo 2 - The "Premium Overlay" Showcase.
 * Uses FastTheme.setBorderlessShadow for a Raycast/Spotlight look.
 */
public class Demo2 {
    private static final Color COLOR_BG = new Color(20, 20, 20);
    private static final Color COLOR_ACCENT = new Color(32, 255, 128);
    
    private TextField searchField;
    private TextArea resultsArea;
    private fastfileindex.FileIndex index;
    private boolean isReady = false;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Demo2().start());
    }

    public void start() {
        System.setProperty("sun.java2d.uiScale", "1.0f");

        // 1. Setup UI Components
        searchField = new TextField(25, 12, new Color(30, 30, 30), new Font("Inter", Font.PLAIN, 28), Color.WHITE, COLOR_ACCENT);
        searchField.setBounds(20, 20, 760, 60);
        searchField.setText("");

        resultsArea = new TextArea(15, new Color(25, 25, 25), new Font("Consolas", Font.PLAIN, 18), new Color(180, 180, 180));
        resultsArea.setBounds(20, 100, 760, 400);
        resultsArea.setText("Initializing Native Index...");

        // 2. Assembly
        final Container container = new Container();
        container.setBackground(COLOR_BG);
        container.add(searchField);
        container.add(resultsArea);

        // 3. Window Setup
        final JFrame frame = new JFrame("FastFileSearch Overlay");
        frame.setUndecorated(false); // MUST be false for native shadow to exist
        frame.setContentPane(container);
        frame.setSize(800, 520);
        frame.setLocationRelativeTo(null);
        frame.setAlwaysOnTop(true);

        // 4. Apply Premium Styling
        frame.addNotify(); // Ensure HWND is created
        long hwnd = FastTheme.getWindowHandle(frame);
        System.out.println("[Debug] HWND: " + hwnd);
        if (hwnd != 0) {
            System.out.println("[Debug] Applying Borderless Shadow...");
            boolean success = FastTheme.setBorderlessShadow(hwnd, true);
            System.out.println("[Debug] Success: " + success);
            
            FastTheme.setTitleBarDarkMode(hwnd, true);
            FastTheme.setCornerStyle(hwnd, 2); 
        }

        // 5. Logic Wiring
        searchField.addChangeListener(text -> performSearch());

        frame.setVisible(true);

        // 6. Build Native Index Asynchronously
        new Thread(() -> {
            try {
                String targetPath = "C:\\Users\\andre\\Documents\\2026-04-28-Work-FastJava";
                fastfileindex.FastFileIndex.build(new String[]{targetPath});
                fastfileindex.FastFileIndex.save("files.idx");
                
                index = fastfileindex.FileIndex.open("files.idx");
                isReady = true;
                
                SwingUtilities.invokeLater(() -> {
                    resultsArea.setText("");
                    performSearch();
                });
            } catch (Exception e) {
                SwingUtilities.invokeLater(() -> resultsArea.setText("Error: " + e.getMessage()));
            }
        }).start();
    }

    private void performSearch() {
        if (!isReady) return;
        String query = searchField.getText().toLowerCase().trim();
        StringBuilder sb = new StringBuilder();
        int count = 0;
        
        try {
            long total = fastfileindex.FastFileIndex.getEntryCount();
            for (long i = 0; i < total && count < 20; i++) {
                String path = fastfileindex.FastFileIndex.getEntryPath(i);
                if (query.isEmpty() || path.toLowerCase().contains(query)) {
                    int lastSlash = path.lastIndexOf('\\');
                    String name = (lastSlash >= 0) ? path.substring(lastSlash + 1) : path;
                    sb.append(" ").append(name).append("\n");
                    count++;
                }
            }
        } catch (Exception ignored) {}
        
        resultsArea.setText(sb.toString());
    }
}
