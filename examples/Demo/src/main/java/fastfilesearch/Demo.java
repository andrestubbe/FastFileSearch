package fastfilesearch;

import fasttheme.FastTheme;
import fastui.Container;
import fastui.component.Image;
import fastui.composable.Button;
import fastui.composable.TextField;
import fastui.composable.Timeline;
import fastui.composable.TextArea;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Date;

/**
 * FastFileSearch Unified Demo - Integrated Timeline and Mocked Search Engine.
 */
public class Demo {

    private static final Color COLOR_BG = new Color(15, 15, 15);
    private static final Color COLOR_ACCENT = new Color(32, 255, 128);
    private static final Color COLOR_PANEL = new Color(22, 22, 22);

    private Timeline timeline;
    private TextField searchField;
    private TextArea resultsArea;
    private Image statusLight;

    private BufferedImage lightRed;
    private BufferedImage lightGreen;
    private boolean isReady = false;

    private fastfileindex.FileIndex index;
    private FastFileSearch searchEngine;
    private boolean isBuilding = false;

    public static void main(String[] args) {
        System.out.println("Demo starting...");
        new Demo().start();
    }

    private BufferedImage createLight(Color color) {
        BufferedImage img = new BufferedImage(20, 20, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = img.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setColor(color);
        g.fillOval(2, 2, 16, 16);
        g.setColor(color.darker());
        g.drawOval(2, 2, 16, 16);
        g.dispose();
        return img;
    }

    public void start() {
        System.setProperty("sun.java2d.uiScale", "1.0f");

        lightRed = createLight(new Color(255, 60, 60));
        lightGreen = createLight(new Color(32, 255, 128));
        BufferedImage lightOff = createLight(new Color(60, 20, 20));

        // 1. Setup UI Components
        final long now = System.currentTimeMillis();
        final long oneYearAgo = now - 365L * 24 * 60 * 60 * 1000;

        timeline = new Timeline(
            oneYearAgo, now,
            200, 20, COLOR_PANEL,
            90, new Color(35, 35, 35), new Color(32, 255, 128, 160),
            new Font("Inter", Font.PLAIN, 22), new Color(60, 60, 60), new Color(140, 140, 140)
        );
        timeline.setBounds(50, 50, 1310, 50, 90, 15);

        statusLight = new Image(lightRed);
        statusLight.setBounds(1370, 65, 20, 20); // Top right, vertically aligned with timeline

        searchField = new TextField(50, 12, new Color(25, 25, 25), new Font("Inter", Font.PLAIN, 22), Color.WHITE, COLOR_ACCENT);
        searchField.setBounds(50, 260, 1340, 50); // Widened to full width
        searchField.setText("*.java");

        final int arc = 20;
        final Font font = new Font("Consolas", Font.PLAIN, 32);
        final Color color1;
        resultsArea = new TextArea(arc, new Color(20, 20, 20), font, new Color(200, 200, 200));
        resultsArea.setBounds(50, 330, 1340, 650);
        resultsArea.setText("FastFileSearch v0.1.0 starting...\nInitializing Native Index Engine...");

        // 2. Assembly
        final Container container = new Container();
        container.setBackground(COLOR_BG);
        container.add(timeline);
//        container.add(statusLight);
        container.add(searchField);
        container.add(resultsArea);

        // 3. Logic Wiring
        timeline.getRangeBehavior().addListener((min, max) -> {
            performSearch();
        });

        searchField.addChangeListener(text -> {
            performSearch();
        });

        // 4. Window Setup
        final JFrame frame = new JFrame("FastFileSearch - Sequencer UI");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setContentPane(container);
        frame.setSize(1440, 1080);
        frame.setLocationRelativeTo(null);

        frame.addNotify();

        final long hwnd = FastTheme.getWindowHandle(frame);
        if (hwnd != 0) {
            FastTheme.setTitleBarDarkMode(hwnd, true);
            FastTheme.setTitleBarColor(hwnd, 15, 15, 15);
            FastTheme.setTitleBarTextColor(hwnd, 220, 220, 220);
        }

        // --- RESPONSIVE LAYOUT LOGIC ---
        frame.addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override
            public void componentResized(java.awt.event.ComponentEvent e) {
                int w = container.getWidth();
                int h = container.getHeight();
                int padding = 50;
                int innerW = w - (padding * 2);

                // 1. Timeline (Fixed height, dynamic width)
                timeline.setBounds(padding, 50, innerW - 40, 50, 90, 15);
                
                // 2. Status Light (Moves with top-right corner)
                statusLight.setBounds(w - padding - 20, 65, 20, 20);

                // 3. Search Field (Fixed Y and height, dynamic width)
                searchField.setBounds(padding, 260, innerW, 50);

                // 4. Results Area (Fills the rest of the height)
                int resultsY = 330;
                int resultsH = h - resultsY - padding;
                if (resultsH < 100) resultsH = 100; // Minimum height
                resultsArea.setBounds(padding, resultsY, innerW, resultsH);
                
                container.repaint();
            }
        });

        frame.setVisible(true);

        // 5. Build Native Index Asynchronously
        new Thread(() -> {
            try {
                isBuilding = true;
                
                // Blinker Thread
                new Thread(() -> {
                    boolean red = true;
                    while (isBuilding) {
                        final boolean isRed = red;
                        SwingUtilities.invokeLater(() -> statusLight.setImage(isRed ? lightRed : lightOff));
                        try { Thread.sleep(400); } catch (Exception ignored) {}
                        red = !red;
                    }
                }).start();

                String targetPath = "C:\\Users\\andre\\Documents\\2026-04-28-Work-FastJava";
                fastfileindex.FastFileIndex.build(new String[]{targetPath});
                fastfileindex.FastFileIndex.save("files.idx");
                
                index = fastfileindex.FileIndex.open("files.idx");
                searchEngine = FastFileSearch.fromIndex(index, SearchBuildOptions.defaults());
                
                isBuilding = false;
                SwingUtilities.invokeLater(() -> resultsArea.append("\n[+] Native FileIndex built! " + fastfileindex.FastFileIndex.getEntryCount() + " items indexed."));
                
                isReady = true;
                SwingUtilities.invokeLater(() -> {
                    statusLight.setImage(lightGreen);
                    resultsArea.append("\n[+] Native Index is READY! Type to filter or drag the timeline.\n");
                    performSearch();
                });
            } catch (Throwable e) {
                isBuilding = false;
                SwingUtilities.invokeLater(() -> resultsArea.append("\n[!] Error building native index: " + e.toString() + "\n"));
            }
        }).start();
    }

    private void performSearch() {
        if (!isReady) return;
        
        String rawQuery = searchField.getText().toLowerCase().trim();
        String queryStr = rawQuery.replace("*", "");
        
        if (rawQuery.equals("*.*") || rawQuery.isEmpty()) {
            queryStr = ""; // Empty string will match everything via .contains("")
        }


        StringBuilder sb = new StringBuilder();

        int matchCount = 0;
        
        try {
            long totalFiles = fastfileindex.FastFileIndex.getEntryCount();
            if (totalFiles == 0) {
                resultsArea.setText("Index is empty!\n");
                return;
            }
            
            for (long i = 0; i < totalFiles; i++) {
                long fileTime = fastfileindex.FastFileIndex.getEntryModified(i) * 1000L;

                long minTime = timeline.getMinTime();
                long maxTime = timeline.getMaxTime();
                if (fileTime >= minTime && fileTime <= maxTime) {
                    String path = fastfileindex.FastFileIndex.getEntryPath(i);
                    if (path != null && (queryStr.isEmpty() || path.toLowerCase().contains(queryStr))) {
                        if (matchCount < 50) {
                            String name = path;
                            int lastSlash = path.lastIndexOf('\\');
                            if (lastSlash >= 0) {
                                name = path.substring(lastSlash + 1);
                            }
                            sb.append(String.format("%s\n", name));
                        }
                        matchCount++;
                    }
                }
            }
        } catch (Exception e) {
            sb.append("Error querying native index: ").append(e.getMessage()).append("\n");
        }
        
        resultsArea.setText(sb.toString());
    }
}
