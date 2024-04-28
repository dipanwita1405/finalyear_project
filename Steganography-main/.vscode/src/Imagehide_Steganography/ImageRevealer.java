package Imagehide_Steganography;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

public class ImageRevealer {
    private static final int ZOOM_MIN = 1;
    private static final int ZOOM_MAX = 5;
    private static final int ZOOM_INITIAL = 5; // Initial zoom level

    public static BufferedImage revealImage(BufferedImage img1) {
        BufferedImage imgResult = new BufferedImage(img1.getWidth(), img1.getHeight(), BufferedImage.TYPE_INT_ARGB);

        for (int y = 0; y < img1.getHeight(); y++) {
            for (int x = 0; x < img1.getWidth(); x++) {
                int p1 = img1.getRGB(x, y);
                int r1 = (p1 >> 16) & 0xff;
                int g1 = (p1 >> 8) & 0xff;
                int b1 = p1 & 0xff;

                int a = 255;
                int r = (r1 << 4) & 0xf0;
                int g = (g1 << 4) & 0xf0;
                int b = (b1 << 4) & 0xf0;

                int p = (a << 24) | (r << 16) | (g << 8) | b;
                imgResult.setRGB(x, y, p);
            }
        }

        displayImage(imgResult);

        return imgResult;
    }

    private static void displayImage(BufferedImage imgResult) {
        JFrame frame = new JFrame("Revealed Image");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        ImageIcon icon = new ImageIcon(imgResult);
        JLabel label = new JLabel(icon);

        JScrollPane scrollPane = new JScrollPane(label);
        frame.getContentPane().add(scrollPane, BorderLayout.CENTER);

        // Create a JPanel for zoom control and save button
        JPanel controlsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));

        // Zoom control
        JSlider zoomSlider = new JSlider(JSlider.HORIZONTAL, ZOOM_MIN, ZOOM_MAX, ZOOM_INITIAL);
        zoomSlider.setMajorTickSpacing(1);
        zoomSlider.setPaintTicks(true);
        zoomSlider.setPaintLabels(true);
        zoomSlider.addChangeListener(e -> {
            int zoomLevel = zoomSlider.getValue();
            double scaleFactor = (double) zoomLevel / ZOOM_INITIAL;
            int scaledWidth = (int) (imgResult.getWidth() * scaleFactor);
            int scaledHeight = (int) (imgResult.getHeight() * scaleFactor);
            ImageIcon scaledIcon = new ImageIcon(imgResult.getScaledInstance(scaledWidth, scaledHeight, Image.SCALE_SMOOTH));
            label.setIcon(scaledIcon);
        });

        JPanel zoomPanel = new JPanel();
        zoomPanel.add(new JLabel("Zoom Level:"));
        zoomPanel.add(zoomSlider);
        controlsPanel.add(zoomPanel);

        // Save button
        JButton saveButton = new JButton("Save Image");
        saveButton.addActionListener(e -> saveImage(imgResult));
        controlsPanel.add(saveButton);

        frame.getContentPane().add(controlsPanel, BorderLayout.NORTH);

        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private static void saveImage(BufferedImage image) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Save Image");
        int userSelection = fileChooser.showSaveDialog(null);
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File outputFile = fileChooser.getSelectedFile();
            if (!outputFile.getName().toLowerCase().endsWith(".png")) {
                outputFile = new File(outputFile.getAbsolutePath() + ".png");
            }
            try {
                ImageIO.write(image, "png", outputFile);
                JOptionPane.showMessageDialog(null, "Image saved successfully...\nFile path: " + outputFile.getAbsolutePath());
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Error saving image: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public static void main(String[] args) {
        BufferedImage sampleImage = new BufferedImage(900, 300, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = sampleImage.createGraphics();
        g2d.setColor(Color.RED);
        g2d.fillRect(0, 0, 400, 300);
        g2d.dispose();

        revealImage(sampleImage);
    }
}
