package ImageSteganography;

//EmbedMessage.java
// import Coding.SendEmail;
import java.awt.event.*;
import javax.imageio.*;
import java.awt.image.*;
import javax.swing.*;
import java.awt.*;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Base64;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

/**
 *
 * @author Hadi
 */
public class Encryption extends JFrame implements ActionListener {
   JButton open = new JButton("Open"), embed = new JButton("Embed"), send = new JButton("Send"),
         save = new JButton("Save"), reset = new JButton("Reset");

   JTextArea message = new JTextArea(8, 3);
   BufferedImage sourceImage = null, embeddedImage = null;
   BufferedImage sourceImage2 = null, embeddedImage2 = null;
   JSplitPane sp = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
   JScrollPane originalPane = new JScrollPane(),
         embeddedPane = new JScrollPane();
   JScrollPane originalPane2 = new JScrollPane(),
         embeddedPane2 = new JScrollPane();
   private static final String SECRET_KEY = "123456789";
   private static final String SALTVALUE = "abcdefg";

   /* Encryption Method */
   public static String encrypt(String strToEncrypt) {
      try {
         /* Declare a byte array. */
         byte[] iv = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
         IvParameterSpec ivspec = new IvParameterSpec(iv);
         /* Create factory for secret keys. */
         SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
         /* PBEKeySpec class implements KeySpec interface. */
         KeySpec spec = new PBEKeySpec(SECRET_KEY.toCharArray(), SALTVALUE.getBytes(), 65536, 256);
         SecretKey tmp = factory.generateSecret(spec);
         SecretKeySpec secretKey = new SecretKeySpec(tmp.getEncoded(), "AES");
         Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
         cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivspec);
         /* Retruns encrypted value. */
         return Base64.getEncoder()
               .encodeToString(cipher.doFinal(strToEncrypt.getBytes(StandardCharsets.UTF_8)));
      } catch (InvalidAlgorithmParameterException | InvalidKeyException | NoSuchAlgorithmException
            | InvalidKeySpecException | BadPaddingException | IllegalBlockSizeException | NoSuchPaddingException e) {
         System.out.println("Error occured during encryption: " + e.toString());
      }
      return null;
   }

   public Encryption() {
      super("Embed stegonographic message in image");
      assembleInterface();
      open.setBackground(Color.black);
      open.setForeground(Color.WHITE);
      open.setFont(new Font("Monaco", Font.BOLD, 20));
      open.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

      embed.setBackground(Color.black);
      embed.setForeground(Color.WHITE);
      embed.setFont(new Font("Monaco", Font.BOLD, 20));
      embed.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

      send.setBackground(Color.black);
      send.setForeground(Color.white);
      send.setFont(new Font("Monaco", Font.BOLD, 20));
      send.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

      save.setBackground(Color.black);
      save.setForeground(Color.WHITE);
      save.setFont(new Font("Monaco", Font.BOLD, 20));
      save.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

      reset.setBackground(Color.black);
      reset.setForeground(Color.WHITE);
      reset.setFont(new Font("Monaco", Font.BOLD, 20));
      reset.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

      // this.setBounds(GraphicsEnvironment.getLocalGraphicsEnvironment().
      // getMaximumWindowBounds());
      this.setSize(1000, 700);
      this.setLocationRelativeTo(null);
      this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
      this.setVisible(true);
      sp.setDividerLocation(0.5);
      this.validate();
   }

   private void assembleInterface() {
      // JPanel p = new JPanel(new FlowLayout());
      // p.add(open);
      // p.add(embed);
      // p.add(send);
      // p.add(save);
      // p.add(reset);

      // this.getContentPane().add(p, BorderLayout.SOUTH);
      // open.addActionListener(this);
      // embed.addActionListener(this);
      // send.addActionListener(this);
      // save.addActionListener(this);
      // reset.addActionListener(this);
      // open.setMnemonic('O');
      // embed.setMnemonic('E');
      // send.setMnemonic('G');
      // save.setMnemonic('S');
      // reset.setMnemonic('R');

      // p = new JPanel(new GridLayout(1,1));
      // p.add(new JScrollPane(message));
      // message.setFont(new Font("Arial",Font.BOLD,20));
      // p.setBorder(BorderFactory.createTitledBorder("Message to be embedded"));
      // this.getContentPane().add(p, BorderLayout.NORTH);

      // sp.setLeftComponent(originalPane);
      // sp.setRightComponent(embeddedPane);
      // originalPane.setBorder(BorderFactory.createTitledBorder("Original Image"));
      // embeddedPane.setBorder(BorderFactory.createTitledBorder("Steganographed
      // Image"));
      // this.getContentPane().add(sp, BorderLayout.CENTER);

      JPanel p = new JPanel(new FlowLayout());
      p.add(open);
      p.add(embed);
      p.add(save);
      p.add(reset);
      this.getContentPane().add(p, BorderLayout.SOUTH);
      open.addActionListener(this);
      embed.addActionListener(this);
      save.addActionListener(this);
      reset.addActionListener(this);
      open.setMnemonic('O');
      embed.setMnemonic('E');
      save.setMnemonic('S');
      reset.setMnemonic('R');

      p = new JPanel(new GridLayout(1, 1));
      p.add(new JScrollPane(message));
      message.setFont(new Font("Arial", Font.BOLD, 20));
      p.setBorder(BorderFactory.createTitledBorder("Message to be embedded"));
      this.getContentPane().add(p, BorderLayout.NORTH);

      JSplitPane centerSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);

      JSplitPane leftSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);

      // JPanel originalPane = new JPanel();
      originalPane.setPreferredSize(new Dimension(450, 170)); // Set size for Original Image panel
      originalPane.setBorder(BorderFactory.createTitledBorder("Original Image 1"));
      leftSplitPane.setTopComponent(originalPane);

      // JPanel originalPane2 = new JPanel();
      originalPane2.setPreferredSize(new Dimension(450, 150)); // Set size for Subpanel 1
      originalPane2.setBorder(BorderFactory.createTitledBorder("Original Image 2"));
      leftSplitPane.setBottomComponent(originalPane2);

      JSplitPane rightSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);

      // JPanel embeddedPane = new JPanel();
      embeddedPane.setPreferredSize(new Dimension(300, 170)); // Set size for Steganographed Image panel
      embeddedPane.setBorder(BorderFactory.createTitledBorder("Steganographed Image 1"));
      rightSplitPane.setTopComponent(embeddedPane);

      // JPanel subPanel2 = new JPanel();
      embeddedPane2.setPreferredSize(new Dimension(300, 150)); // Set size for Subpanel 2
      embeddedPane2.setBorder(BorderFactory.createTitledBorder("Steganographed Image 2"));
      rightSplitPane.setBottomComponent(embeddedPane2);

      centerSplitPane.setLeftComponent(leftSplitPane);
      centerSplitPane.setRightComponent(rightSplitPane);

      // Set divider location for the left and right split panes
      leftSplitPane.setDividerLocation(0.5);
      rightSplitPane.setDividerLocation(0.5);

      getContentPane().add(centerSplitPane, BorderLayout.CENTER);

      setVisible(true);
   }

   @Override
   public void actionPerformed(ActionEvent ae) {
      Object o = ae.getSource();
      if (o == open) {
         openImage();
         openImage2();
      } else if (o == embed)
         embedMessage();
      // else if (o == send) {
      //    SendEmail Email = new SendEmail();
      //    Email.setVisible(true);
      //    Email.setLocationRelativeTo(null);
      //    Email.pack();
      // }
       else if (o == save) {
         saveImage();
         saveImage2();
      } else if (o == reset)
         resetInterface();
   }

   private java.io.File showFileDialog(final boolean open) {
      JFileChooser fc = new JFileChooser("Open an image");
      javax.swing.filechooser.FileFilter ff = new javax.swing.filechooser.FileFilter() {
         @Override
         public boolean accept(java.io.File f) {
            String name = f.getName().toLowerCase();
            if (open)
               return f.isDirectory() || name.endsWith(".jpg") || name.endsWith(".jpeg") ||
                     name.endsWith(".png") || name.endsWith(".gif") || name.endsWith(".tiff") ||
                     name.endsWith(".bmp") || name.endsWith(".dib");
            return f.isDirectory() || name.endsWith(".png") || name.endsWith(".bmp");
         }

         @Override
         public String getDescription() {
            if (open)
               return "Image (*.jpg, *.jpeg, *.png, *.gif, *.tiff, *.bmp, *.dib)";
            return "Image (*.png, *.bmp)";
         }
      };
      fc.setAcceptAllFileFilterUsed(false);
      fc.addChoosableFileFilter(ff);

      java.io.File f = null;
      if (open && fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION)
         f = fc.getSelectedFile();
      else if (!open && fc.showSaveDialog(this) == JFileChooser.APPROVE_OPTION)
         f = fc.getSelectedFile();
      return f;
   }

   private void openImage() {
      java.io.File f = showFileDialog(true);
      if (f == null)
         return;
      try {
         sourceImage = ImageIO.read(f);
         JLabel l = new JLabel(new ImageIcon(sourceImage));
         originalPane.getViewport().add(l);
         this.validate();
      } catch (Exception ex) {
      }
   }

   private void openImage2() {
      java.io.File f = showFileDialog(true);
      if (f == null)
         return;
      try {
         sourceImage2 = ImageIO.read(f);
         JLabel l = new JLabel(new ImageIcon(sourceImage2));
         originalPane2.getViewport().add(l);
         this.validate();
      } catch (Exception ex) {
      }
   }

   private void embedMessage() {
      String mess1 = message.getText();
      String first = "";
      String second = "";
      for (int i = 0; i < mess1.length(); i++) {
         if (i % 2 == 0)
            first += mess1.charAt(i);
         else
            second += mess1.charAt(i);
      }
      /*
       * int half = mess1.length() % 2 == 0 ? mess1.length() / 2 : mess1.length() / 2
       * + 1;
       * String first = mess1.substring(0, half);
       * String second = mess1.substring(half);
       */
      embeddedImage = sourceImage.getSubimage(0, 0,
            sourceImage.getWidth(), sourceImage.getHeight());
      embeddedImage2 = sourceImage2.getSubimage(0, 0,
            sourceImage2.getWidth(), sourceImage2.getHeight());
      embedMessage(embeddedImage, encrypt(first));
      embedMessage(embeddedImage2, encrypt(second));
      JLabel l = new JLabel(new ImageIcon(embeddedImage));
      embeddedPane.getViewport().add(l);
      JLabel l2 = new JLabel(new ImageIcon(embeddedImage2));
      embeddedPane2.getViewport().add(l2);
      this.validate();
   }

   private void embedMessage(BufferedImage img, String mess) {
      int messageLength = mess.length();

      int imageWidth = img.getWidth(), imageHeight = img.getHeight(),
            imageSize = imageWidth * imageHeight;
      if (messageLength * 8 + 32 > imageSize) {
         JOptionPane.showMessageDialog(this, "Message is too long for the chosen image",
               "Message too long!", JOptionPane.ERROR_MESSAGE);
         return;
      }
      embedInteger(img, messageLength, 0, 0);

      byte b[] = mess.getBytes();
      for (int i = 0; i < b.length; i++)
         embedByte(img, b[i], i * 8 + 32, 0);
   }

   private void embedInteger(BufferedImage img, int n, int start, int storageBit) {
      int maxX = img.getWidth(), maxY = img.getHeight(),
            startX = start / maxY, startY = start - startX * maxY, count = 0;
      for (int i = startX; i < maxX && count < 32; i++) {
         for (int j = startY; j < maxY && count < 32; j++) {
            int rgb = img.getRGB(i, j), bit = getBitValue(n, count);
            rgb = setBitValue(rgb, storageBit, bit);
            img.setRGB(i, j, rgb);
            count++;
         }
      }
   }

   private void embedByte(BufferedImage img, byte b, int start, int storageBit) {
      int maxX = img.getWidth(), maxY = img.getHeight(),
            startX = start / maxY, startY = start - startX * maxY, count = 0;
      for (int i = startX; i < maxX && count < 8; i++) {
         for (int j = startY; j < maxY && count < 8; j++) {
            int rgb = img.getRGB(i, j), bit = getBitValue(b, count);
            rgb = setBitValue(rgb, storageBit, bit);
            img.setRGB(i, j, rgb);
            count++;
         }
      }
   }

   private void saveImage() {
      if (embeddedImage == null) {
         JOptionPane.showMessageDialog(this, "No message has been embedded!",
               "Nothing to save", JOptionPane.ERROR_MESSAGE);
         return;
      }
      java.io.File f = showFileDialog(false);
      if (f == null)
         return;

      String name = f.getName();
      String ext = name.substring(name.lastIndexOf(".") + 1).toLowerCase();
      if (!ext.equals("png") && !ext.equals("bmp") && !ext.equals("dib")) {
         ext = "png";
         f = new java.io.File(f.getAbsolutePath() + ".png");
      }
      try {
         if (f.exists())
            f.delete();
         ImageIO.write(embeddedImage, ext.toUpperCase(), f);
      } catch (Exception ex) {
      }
   }

   private void saveImage2() {
      java.io.File f = showFileDialog(false);
      String name = f.getName();
      String ext = name.substring(name.lastIndexOf(".") + 1).toLowerCase();
      if (!ext.equals("png") && !ext.equals("bmp") && !ext.equals("dib")) {
         ext = "png";
         f = new java.io.File(f.getAbsolutePath() + ".png");
      }
      try {
         if (f.exists())
            f.delete();
         ImageIO.write(sourceImage2, ext.toUpperCase(), f);
      } catch (Exception ex) {
         ex.printStackTrace();
      }
   }

   private void resetInterface() {
      message.setText("");
      originalPane.getViewport().removeAll();
      embeddedPane.getViewport().removeAll();
      sourceImage = null;
      embeddedImage = null;
      originalPane2.getViewport().removeAll();
      embeddedPane2.getViewport().removeAll();
      sourceImage2 = null;
      embeddedImage2 = null;
      sp.setDividerLocation(0.5);
      this.validate();
   }

   private int getBitValue(int n, int location) {
      int v = n & (int) Math.round(Math.pow(2, location));
      return v == 0 ? 0 : 1;
   }

   private int setBitValue(int n, int location, int bit) {
      int toggle = (int) Math.pow(2, location), bv = getBitValue(n, location);
      if (bv == bit)
         return n;
      if (bv == 0 && bit == 1)
         n |= toggle;
      else if (bv == 1 && bit == 0)
         n ^= toggle;
      return n;
   }

   // public static void main(String arg[]) {
   //// Encryption embedMessage = new Encryption();
   // }
}