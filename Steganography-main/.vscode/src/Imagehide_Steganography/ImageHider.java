package Imagehide_Steganography;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ImageHider {

    private BufferedImage frontImage;
    private BufferedImage hideImage;
    private JPanel resultPanel;

    public void createImageHiderFrame() {
        JFrame frame = new JFrame("Image Hider");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(1200, 400);

        JPanel frontImagePanel = createImagePanel("Front Image");
        JPanel hideImagePanel = createImagePanel("Hide Image");
        resultPanel = createResultPanel();

        frame.setLayout(new GridLayout(1, 3));
        frame.add(frontImagePanel);
        frame.add(hideImagePanel);
        frame.add(resultPanel);

        frame.setVisible(true);
    }

    private JPanel createImagePanel(String title) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder(title));
        JLabel imageLabel = new JLabel();

        // Wrap the image label in a scroll pane
        JScrollPane scrollPane = new JScrollPane(imageLabel);
        panel.add(scrollPane, BorderLayout.CENTER);

        JButton openButton = new JButton("Open " + title);
        openButton.addActionListener(e -> {
            BufferedImage image = loadImage();
            if (image != null) {
                ImageIcon icon = new ImageIcon(image);
                imageLabel.setIcon(icon);
                if (title.equals("Front Image")) {
                    frontImage = image;
                } else if (title.equals("Hide Image")) {
                    hideImage = image;
                }
            }
        });
        panel.add(openButton, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createResultPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Result Image"));
        JLabel resultImageLabel = new JLabel();

        // Wrap the result image label in a scroll pane
        JScrollPane scrollPane = new JScrollPane(resultImageLabel);
        panel.add(scrollPane, BorderLayout.CENTER);

        JButton embedButton = new JButton("Embed");
        embedButton.addActionListener(e -> embedImages(resultImageLabel));

        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(e -> saveImage(resultImageLabel));

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(embedButton);
        buttonPanel.add(saveButton);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        return panel;
    }

    private BufferedImage loadImage() {
        JFileChooser fileChooser = new JFileChooser();
        int option = fileChooser.showOpenDialog(null);
        if (option == JFileChooser.APPROVE_OPTION) {
            try {
                File file = fileChooser.getSelectedFile();
                return ImageIO.read(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private void embedImages(JLabel resultImageLabel) {
        if (frontImage != null && hideImage != null) {
            BufferedImage embeddedImage = embedImage(frontImage, hideImage);
            if (embeddedImage != null) {
                ImageIcon icon = new ImageIcon(embeddedImage);
                resultImageLabel.setIcon(icon);
            }
        } else {
            JOptionPane.showMessageDialog(null, "Please select both front and hide images.");
        }
    }

    private BufferedImage embedImage(BufferedImage frontImage, BufferedImage hideImage) {
        int width = frontImage.getWidth();
        int height = frontImage.getHeight();

        BufferedImage embeddedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

        // Loop through each pixel in the front image
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                // Get the RGB values of the corresponding pixels in both images
                int pixelFront = frontImage.getRGB(x, y);
                int pixelHide = hideImage.getRGB(x % hideImage.getWidth(), y % hideImage.getHeight());

                // Extract the color components (alpha, red, green, blue) from the front image pixel
                int alpha = (pixelFront >> 24) & 0xFF;
                int red = (pixelFront >> 16) & 0xFF;
                int green = (pixelFront >> 8) & 0xFF;
                int blue = pixelFront & 0xFF;

                // Extract the color components (red, green, blue) from the hide image pixel
                int red2 = (pixelHide >> 16) & 0xFF;
                int green2 = (pixelHide >> 8) & 0xFF;
                int blue2 = pixelHide & 0xFF;

                // Modify the color components of the front image pixel to embed information from the hide image
                red = (red & 0xF0) | (red2 >> 4 & 0xF);
                green = (green & 0xF0) | (green2 >> 4 & 0xF);
                blue = (blue & 0xF0) | (blue2 >> 4 & 0xF);

                // Compose the new pixel using the modified color components
                int embeddedPixel = (alpha << 24) | (red << 16) | (green << 8) | blue;

                // Set the embedded pixel in the result image
                embeddedImage.setRGB(x, y, embeddedPixel);
            }
        }

        return embeddedImage;
    }

    private void saveImage(JLabel resultImageLabel) {
        ImageIcon icon = (ImageIcon) resultImageLabel.getIcon();
        if (icon != null) {
            BufferedImage image = new BufferedImage(icon.getIconWidth(), icon.getIconHeight(), BufferedImage.TYPE_INT_ARGB);
            Graphics g = image.createGraphics();
            icon.paintIcon(null, g, 0, 0);
            g.dispose();

            JFileChooser fileChooser = new JFileChooser();
            int option = fileChooser.showSaveDialog(null);
            if (option == JFileChooser.APPROVE_OPTION) {
                try {
                    File file = fileChooser.getSelectedFile();
                    ImageIO.write(image, "png", file);
                    JOptionPane.showMessageDialog(null, "Image saved successfully: " + file.getName());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else {
            JOptionPane.showMessageDialog(null, "Please embed an image first.");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ImageHider imageHider = new ImageHider();
            imageHider.createImageHiderFrame();
        });
    }
}
