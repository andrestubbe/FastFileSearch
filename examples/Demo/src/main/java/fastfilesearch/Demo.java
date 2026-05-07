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
    private Button searchButton;
    private TextArea resultsArea;
    private Image statusLight;

    private BufferedImage lightRed;
    private BufferedImage lightGreen;
    private boolean isReady = false;

    private fastfileindex.FileIndex index;
    private boolean isBuilding = false;

    public static void main(String[] args) {
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
        searchField.setBounds(50, 260, 1000, 50);
        searchField.setText(".java");

        searchButton = new Button(50, 12, COLOR_ACCENT, "SEARCH", new Font("Inter", Font.BOLD, 18), Color.BLACK);
        searchButton.setBounds(1060, 260, 330, 50);

        resultsArea = new TextArea(20, new Color(20, 20, 20), new Font("Consolas", Font.PLAIN, 22), new Color(200, 200, 200));
        resultsArea.setBounds(50, 330, 1340, 650);
        resultsArea.setText("FastFileSearch v0.1.0 starting...\nInitializing Native Index Engine...");

        // 2. Assembly
        final Container container = new Container();
        container.setBackground(COLOR_BG);
        container.add(timeline);
        container.add(statusLight);
        container.add(searchField);
        container.add(searchButton);
        container.add(resultsArea);

        // 3. Logic Wiring
        searchButton.addBehavior(new fastui.behaviour.MouseBehavior() {
            @Override
            public void onMousePressed(fastui.component.Component target, int mx, int my) {
                performSearch();
            }
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
                
                isBuilding = false;
                SwingUtilities.invokeLater(() -> resultsArea.append("\n[+] Native FileIndex built! " + fastfileindex.FastFileIndex.getEntryCount() + " items indexed."));
                
                isReady = true;
                SwingUtilities.invokeLater(() -> {
                    statusLight.setImage(lightGreen);
                    resultsArea.append("\n[+] Native Index is READY! Enter a query and hit SEARCH.\n");
                });
            } catch (Throwable e) {
                isBuilding = false;
                SwingUtilities.invokeLater(() -> resultsArea.append("\n[!] Error building native index: " + e.toString() + "\n"));
            }
        }).start();
    }

    private void performSearch() {
        if (!isReady) {
            resultsArea.append("\n[!] Wait for Index to finish building...\n");
            return;
        }
        
        String queryStr = searchField.getText().toLowerCase().replace("*", "");
        long minTime = (long) timeline.getRangeBehavior().getMinValue();
        long maxTime = (long) timeline.getRangeBehavior().getMaxValue();

        resultsArea.setText("Query: '" + queryStr + "'\n");
        resultsArea.append("Time Filter: " + new Date(minTime) + " to " + new Date(maxTime) + "\n\n");
        
        long startSearch = System.nanoTime();
        int matchCount = 0;
        
        try {
            long totalFiles = fastfileindex.FastFileIndex.getEntryCount();
            if (totalFiles == 0) {
                resultsArea.append("Index is empty!\n");
                return;
            }
            
            for (long i = 0; i < totalFiles; i++) {
                // Get timestamp first to avoid unnecessary JNI String allocations
                long fileTime = fastfileindex.FastFileIndex.getEntryModified(i) * 1000L; 
                
                if (fileTime >= minTime && fileTime <= maxTime) {
                    String path = fastfileindex.FastFileIndex.getEntryPath(i);
                    if (path != null && path.toLowerCase().contains(queryStr)) {
                        if (matchCount < 50) { // Limit results to prevent UI lag
                            resultsArea.append(String.format("- [Score: 0.99] %s\n  (Modified: %s)\n\n", path, new Date(fileTime)));
                        }
                        matchCount++;
                    }
                }
            }
        } catch (Exception e) {
            resultsArea.append("Error querying native index: " + e.getMessage() + "\n");
        }
        
        long endSearch = System.nanoTime();
        resultsArea.append(String.format("Native Index Search took: %.2f ms\n", (endSearch - startSearch) / 1000000.0));
        
        if (matchCount > 50) {
            resultsArea.append("Found " + matchCount + " results within the timeline range (showing top 50).\n");
        } else {
            resultsArea.append("Found " + matchCount + " results within the selected timeline range.\n");
        }
    }
}
