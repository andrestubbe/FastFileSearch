package fastfilesearch;

import fastfileindex.FastFileIndex;
import fastfileindex.FileIndex;
import fastcore.FastCore;
import fasttheme.FastTheme;
import fastui.Container;
import fastui.component.Image;
import fastui.composable.TextField;
import fastui.composable.TextArea;
import fastui.composable.Timeline;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.awt.image.BufferedImage;

/**
 * FastFileSearch Demo 2 - The "Premium Overlay" Showcase (1440x1080
 * Borderless).
 */
public class Demo2 {

    private static final Color COLOR_BG = new Color(15, 15, 15);
    private static final Color COLOR_ACCENT = new Color(32, 255, 128);
    private static final Color COLOR_PANEL = new Color(22, 22, 22);
    private static final Color COLOR_TEXT = new Color(200, 200, 200);

    private static final int HEIGHT_COLLAPSED = 320;
    private static final int HEIGHT_EXPANDED = 1080;
    private static final int WINDOW_WIDTH = 1440;

    private static final int DRAG_ZONE_HEIGHT = 6;
    private static final int WINDOW_ALPHA = 230;
    private static final int CORNER_STYLE = 2;

    private static final int MARGIN_X = 10;
    private static final int MARGIN_BOTTOM = 50;

    private static final int TIMELINE_Y = 50;
    private static final int TIMELINE_H = 50;
    private static final int TIMELINE_ARC = 20;

    private static final int SEARCH_Y = 260;
    private static final int SEARCH_H = 50;
    private static final int SEARCH_ARC = 12;

    private static final int RESULTS_Y = 330;
    private static final int RESULTS_ARC = 20;

    private static final int STATUS_LIGHT_Y = 65;
    private static final int STATUS_LIGHT_SIZE = 20;
    private static final int STATUS_LIGHT_OFFSET_X = 70;

    private static final int TIMELINE_RANGE_H = 90;
    private static final int TIMELINE_RANGE_OFFSET = 15;

    private static final int BLINK_RATE = 400;
    private static final int MAX_RESULTS = 50;

    private static final Color COLOR_TIMELINE_TRACK = new Color(35, 35, 35);
    private static final Color COLOR_TIMELINE_SPAN = new Color(32, 255, 128, 160);
    private static final Color COLOR_TICK = new Color(60, 60, 60);
    private static final Color COLOR_LABEL = new Color(140, 140, 140);
    private static final Color COLOR_SEARCH_BG = new Color(25, 25, 25);
    private static final Color COLOR_RESULTS_BG = new Color(20, 20, 20);
    private static final Color COLOR_LIGHT_RED = new Color(255, 60, 60);
    private static final Color COLOR_LIGHT_GREEN = new Color(32, 255, 128);
    private static final Color COLOR_LIGHT_OFF = new Color(60, 20, 20);

    private static final Font FONT_UI = new Font("Inter", Font.PLAIN, 22);
    private static final Font FONT_RESULTS = new Font("Consolas", Font.PLAIN, 24);

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Demo2().start());
    }

    private Container container;
    private Timeline timeline;
    private TextField search;
    private TextArea result;
    private Image statusLight;
    private BufferedImage lightRed;
    private BufferedImage lightGreen;
    private boolean isBuilding = false;
    private boolean isReady = false;
    private FileIndex index;

    public void start() {
        System.setProperty("sun.java2d.uiScale", "1.0f");

        lightRed = createLight(COLOR_LIGHT_RED);
        lightGreen = createLight(COLOR_LIGHT_GREEN);
        BufferedImage lightOff = createLight(COLOR_LIGHT_OFF);

        // 1. Setup UI Components (Full Size)
        final long now = System.currentTimeMillis();
        final long oneYearAgo = now - 365L * 24 * 60 * 60 * 1000;

        final long start = oneYearAgo;
        final long end = now;
        timeline = new Timeline(
            (float)MARGIN_X, (float)TIMELINE_Y, (float)(WINDOW_WIDTH - (MARGIN_X * 2)), (float)TIMELINE_H, (float)TIMELINE_RANGE_H, (float)TIMELINE_RANGE_OFFSET,
            start, end,
            TIMELINE_ARC, COLOR_PANEL,
            COLOR_TIMELINE_TRACK, COLOR_TIMELINE_SPAN,
            FONT_UI, COLOR_TICK, COLOR_LABEL
        );

        statusLight = new Image(lightRed);
        statusLight.setBounds(WINDOW_WIDTH - STATUS_LIGHT_OFFSET_X, STATUS_LIGHT_Y, STATUS_LIGHT_SIZE,
                STATUS_LIGHT_SIZE);

        search = new TextField(
                SEARCH_H,
                SEARCH_ARC,
                COLOR_SEARCH_BG, FONT_UI, Color.WHITE,
                COLOR_ACCENT);
        search.setBounds(MARGIN_X, SEARCH_Y, WINDOW_WIDTH - (MARGIN_X * 2), SEARCH_H);
        search.setText("");

        result = new TextArea(RESULTS_ARC, COLOR_RESULTS_BG, FONT_RESULTS, COLOR_TEXT);
        result.setBounds(MARGIN_X, RESULTS_Y, WINDOW_WIDTH - (MARGIN_X * 2),
                HEIGHT_EXPANDED - RESULTS_Y - MARGIN_BOTTOM);
        result.setText("Initializing Native Index Engine...");

        container = new Container();
        container.setOpaque(true);
        container.setBackground(COLOR_BG);
        // container.add(timeline);
        // container.add(statusLight);
        container.add(search);
        // container.add(result);

        // 2. Window Setup
        final JFrame frame = new JFrame("FastFileSearch Premium");
        frame.setUndecorated(true);
        frame.setBackground(COLOR_BG);
        frame.getContentPane().setBackground(COLOR_BG);
        frame.getRootPane().setBackground(COLOR_BG);

        frame.setContentPane(container);
        frame.setSize(WINDOW_WIDTH, HEIGHT_COLLAPSED);
        frame.setLocationRelativeTo(null);
        frame.setAlwaysOnTop(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // 3. Apply Premium Styling (v0.3.0)
        frame.addNotify();
        long hwnd = FastTheme.getWindowHandle(frame);

        if (hwnd != 0) {
            System.out.println("[+] FastTheme handle obtained: " + hwnd);
            // Try FastCore first, fall back to absolute path if needed
            try {
                FastCore.loadLibrary("fasttheme");
                System.out.println("[+] FastTheme loaded via FastCore");
            } catch (UnsatisfiedLinkError e) {
                System.out.println("[!] FastCore load failed, trying absolute path: " + e.getMessage());
                // Fallback to absolute path for development
                String dllPath = "C:\\Users\\andre\\Documents\\2026-05-17-Work-FastJava\\FastTheme\\build\\fasttheme.dll";
                System.load(dllPath);
                System.out.println("[+] FastTheme loaded via absolute path");
            }

            FastTheme.setBorderlessShadow(hwnd, true);
            FastTheme.setOverlayDragHeight(hwnd, DRAG_ZONE_HEIGHT);
            FastTheme.setTitleBarDarkMode(hwnd, true);
            FastTheme.setTitleBarColor(hwnd, COLOR_BG.getRed(), COLOR_BG.getGreen(), COLOR_BG.getBlue());
            FastTheme.setWindowTransparency(hwnd, WINDOW_ALPHA);
            FastTheme.setCornerStyle(hwnd, CORNER_STYLE);
            System.out.println("[+] FastTheme styling applied");
        } else {
            System.out.println("[!] Failed to get window handle");
        }

        // 4. Logic
        timeline.getRangeBehavior().addListener((min, max) -> {
            performSearch();
        });

        final boolean[] isExpanded = { false };
        search.addChangeListener(text -> {
            performSearch();

            boolean hasContent = text != null && !text.trim().isEmpty();
            if (hasContent != isExpanded[0]) {
                isExpanded[0] = hasContent;
                int targetH = isExpanded[0] ? HEIGHT_EXPANDED : HEIGHT_COLLAPSED;

                frame.setSize(frame.getWidth(), targetH);
                container.revalidate();
                container.repaint();
            }
        });

        frame.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                int w = container.getWidth();
                int h = container.getHeight();
                if (w == 0 || h == 0)
                    return;

                timeline.setBounds(MARGIN_X, TIMELINE_Y, w - (MARGIN_X * 2), TIMELINE_H, TIMELINE_RANGE_H,
                        TIMELINE_RANGE_OFFSET);
                statusLight.setBounds(w - STATUS_LIGHT_OFFSET_X, STATUS_LIGHT_Y, STATUS_LIGHT_SIZE, STATUS_LIGHT_SIZE);
                search.setBounds(MARGIN_X, SEARCH_Y, w - (MARGIN_X * 2), SEARCH_H);

                int resultsH = h - RESULTS_Y - MARGIN_BOTTOM;
                if (resultsH > 0) {
                    result.setBounds(MARGIN_X, RESULTS_Y, w - (MARGIN_X * 2), resultsH);
                }
                container.repaint();
            }
        });

        frame.setVisible(true);

        container.revalidate();
        container.repaint();
        frame.revalidate();
        frame.repaint();

        frame.addWindowFocusListener(new WindowFocusListener() {
            @Override
            public void windowGainedFocus(WindowEvent e) {
            }

            @Override
            public void windowLostFocus(WindowEvent e) {
                System.exit(0);
            }
        });

        // 5. Indexing Logic
        new Thread(() -> {
            try {
                isBuilding = true;

                // Blinker Thread
                new Thread(() -> {
                    boolean red = true;
                    while (isBuilding) {
                        final boolean isRed = red;
                        SwingUtilities.invokeLater(() -> statusLight.setImage(isRed ? lightRed : lightOff));
                        try {
                            Thread.sleep(BLINK_RATE);
                        } catch (Exception ignored) {
                        }
                        red = !red;
                    }
                }).start();

                String targetPath = "C:\\Users\\andre\\Documents\\2026-05-17-Work-FastJava";
                FastFileIndex.build(new String[] { targetPath });
                FastFileIndex.save("files.idx");
                index = FileIndex.open("files.idx");

                isBuilding = false;
                isReady = true;
                SwingUtilities.invokeLater(() -> {
                    statusLight.setImage(lightGreen);
                    result.setText("");
                    performSearch();
                });
            } catch (Exception e) {
                isBuilding = false;
                SwingUtilities.invokeLater(() -> result.setText("Error: " + e.getMessage()));
            }
        }).start();
    }

    private void performSearch() {
        if (!isReady) {
            System.out.println("[Debug] Search skipped: Index not ready.");
            return;
        }
        String rawQuery = search.getText().toLowerCase().trim();
        String query = rawQuery.replace("*", "");

        if (rawQuery.equals("*.*") || rawQuery.isEmpty()) {
            query = "";
        }

        StringBuilder sb = new StringBuilder();
        int count = 0;

        long minTime = timeline.getViewport().getSelectionStartTime();
        long maxTime = timeline.getViewport().getSelectionEndTime();

        try {
            long total = FastFileIndex.getEntryCount();
            System.out.println("[Debug] Searching " + total + " entries. Query: '" + query + "'");

            for (long i = 0; i < total && count < MAX_RESULTS; i++) {
                long fileTime = FastFileIndex.getEntryModified(i) * 1000L;

                if (fileTime >= minTime && fileTime <= maxTime) {
                    String path = FastFileIndex.getEntryPath(i);
                    if (path != null && (query.isEmpty() || path.toLowerCase().contains(query))) {
                        int lastSlash = path.lastIndexOf('\\');
                        String name = (lastSlash >= 0) ? path.substring(lastSlash + 1) : path;
                        sb.append(" ").append(name).append("\n");
                        count++;
                    }
                }
            }
            System.out.println("[Debug] Found " + count + " matches.");
        } catch (Exception e) {
            System.out.println("[Debug] Error: " + e.getMessage());
        }

        result.setText(sb.toString());
        container.repaint();
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
}
