package ImageSteganography;

//DecodeMessage.java
import java.awt.image.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.imageio.*;
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

public class Decryption extends JFrame implements ActionListener {
   JButton open = new JButton("Open"), decode = new JButton("Decode"),
         reset = new JButton("Reset");
   JTextArea message = new JTextArea(10, 3);
   BufferedImage image = null;
   BufferedImage image2 = null;
   JSplitPane sp = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
   JScrollPane imagePane = new JScrollPane();
   JScrollPane imagePane2 = new JScrollPane();
   private static final String SECRET_KEY = "123456789";
   private static final String SALTVALUE = "abcdefg";

   public static String decrypt(String strToDecrypt) {
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
         Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
         cipher.init(Cipher.DECRYPT_MODE, secretKey, ivspec);
         /* Retruns decrypted value. */
         return new String(cipher.doFinal(Base64.getDecoder().decode(strToDecrypt)));
      } catch (InvalidAlgorithmParameterException | InvalidKeyException | NoSuchAlgorithmException
            | InvalidKeySpecException | BadPaddingException | IllegalBlockSizeException | NoSuchPaddingException e) {
         System.out.println("Error occured during decryption: " + e.toString());
      }
      return null;
   }

   public Decryption() {
      super("Decode stegonographic message in image");
      assembleInterface();
      this.setSize(800, 600);
      this.setLocationRelativeTo(null);
      this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
      // this.setBounds(GraphicsEnvironment.getLocalGraphicsEnvironment().
      // getMaximumWindowBounds());
      this.setVisible(true);

      open.setBackground(Color.black);
      open.setForeground(Color.WHITE);
      open.setFont(new Font("Monaco", Font.BOLD, 20));

      decode.setBackground(Color.black);
      decode.setForeground(Color.WHITE);
      decode.setFont(new Font("Monaco", Font.BOLD, 20));

      reset.setBackground(Color.black);
      reset.setForeground(Color.WHITE);
      reset.setFont(new Font("Monaco", Font.BOLD, 20));

   }

   private void assembleInterface() {
      JPanel p = new JPanel(new FlowLayout());
      p.add(open);
      p.add(decode);
      p.add(reset);
      this.getContentPane().add(p, BorderLayout.NORTH);
      open.addActionListener(this);
      decode.addActionListener(this);
      reset.addActionListener(this);
      open.setMnemonic('O');
      decode.setMnemonic('D');
      reset.setMnemonic('R');

      p = new JPanel(new GridLayout(1, 1));
      p.add(new JScrollPane(message));
      message.setFont(new Font("Arial", Font.BOLD, 20));
      p.setBorder(BorderFactory.createTitledBorder("Decoded message"));
      message.setEditable(false);
      this.getContentPane().add(p, BorderLayout.SOUTH);
      sp.setLeftComponent(imagePane);
      sp.setRightComponent(imagePane2);
      imagePane.setMinimumSize(new Dimension(386, 500));
      imagePane2.setMinimumSize(new Dimension(250, 500));
      imagePane.setBorder(BorderFactory.createTitledBorder("Steganographed Image 1"));
      imagePane2.setBorder(BorderFactory.createTitledBorder("Steganographed Image 2"));
      this.getContentPane().add(sp, BorderLayout.CENTER);
      sp.setDividerLocation(0.5);
   }

   public void actionPerformed(ActionEvent ae) {
      Object o = ae.getSource();
      if (o == open) {
         openImage();
         openImage2();
      } else if (o == decode)
         decodeMessage();
      else if (o == reset)
         resetInterface();
   }

   private java.io.File showFileDialog(boolean open) {
      JFileChooser fc = new JFileChooser("Open an image");
      javax.swing.filechooser.FileFilter ff = new javax.swing.filechooser.FileFilter() {
         public boolean accept(java.io.File f) {
            String name = f.getName().toLowerCase();
            return f.isDirectory() || name.endsWith(".png") || name.endsWith(".bmp");
         }

         public String getDescription() {
            return "Image (*.png, *.bmp)";
         }
      };
      fc.setAcceptAllFileFilterUsed(false);
      fc.addChoosableFileFilter(ff);

      java.io.File f = null;
      if (open && fc.showOpenDialog(this) == fc.APPROVE_OPTION)
         f = fc.getSelectedFile();
      else if (!open && fc.showSaveDialog(this) == fc.APPROVE_OPTION)
         f = fc.getSelectedFile();
      return f;
   }

   private void openImage() {
      java.io.File f = showFileDialog(true);
      try {
         image = ImageIO.read(f);
         JLabel l = new JLabel(new ImageIcon(image));
         imagePane.getViewport().add(l);
         this.validate();
      } catch (Exception ex) {
         ex.printStackTrace();
      }
   }

   private void openImage2() {
      java.io.File f2 = showFileDialog(true);
      try {
         image2 = ImageIO.read(f2);
         JLabel l2 = new JLabel(new ImageIcon(image2));
         imagePane2.getViewport().add(l2);
         this.validate();
      } catch (Exception ex) {
         ex.printStackTrace();
      }
   }

   private void decodeMessage() {
      if (image == null || image2 == null) {
         JOptionPane.showMessageDialog(null, "open the pictures");
         return;
      }
      int len = extractInteger(image, 0, 0);
      int len2 = extractInteger(image2, 0, 0);
      byte b[] = new byte[len];
      byte b2[] = new byte[len2];
      for (int i = 0; i < len; i++)
         b[i] = extractByte(image, i * 8 + 32, 0);
      for (int i = 0; i < len2; i++)
         b2[i] = extractByte(image2, i * 8 + 32, 0);
      String decryptedB1 = decrypt(new String(b));
      String decryptedB2 = decrypt(new String(b2));
      //decryptedB = decryptedB.concat(decryptedB2);
      String decryptedB = "";
      int l = Math.max(decryptedB1.length(), decryptedB2.length());
      for (int i = 0; i < l; i++) {
         if(i<decryptedB1.length())
            decryptedB+=decryptedB1.charAt(i);
         if(i<decryptedB2.length())
            decryptedB+=decryptedB2.charAt(i); 
      }
      message.setText(decryptedB);
   }

   private int extractInteger(BufferedImage img, int start, int storageBit) {
      int maxX = img.getWidth(), maxY = img.getHeight(),
            startX = start / maxY, startY = start - startX * maxY, count = 0;
      int length = 0;
      for (int i = startX; i < maxX && count < 32; i++) {
         for (int j = startY; j < maxY && count < 32; j++) {
            int rgb = img.getRGB(i, j), bit = getBitValue(rgb, storageBit);
            length = setBitValue(length, count, bit);
            count++;
         }
      }
      return length;
   }

   private byte extractByte(BufferedImage img, int start, int storageBit) {
      int maxX = img.getWidth(), maxY = img.getHeight(),
            startX = start / maxY, startY = start - startX * maxY, count = 0;
      byte b = 0;
      for (int i = startX; i < maxX && count < 8; i++) {
         for (int j = startY; j < maxY && count < 8; j++) {
            int rgb = img.getRGB(i, j), bit = getBitValue(rgb, storageBit);
            b = (byte) setBitValue(b, count, bit);
            count++;
         }
      }
      return b;
   }

   private void resetInterface() {
      message.setText("");
      imagePane.getViewport().removeAll();
      imagePane2.getViewport().removeAll();
      image = null;
      image2 = null;
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
   // Decryption newClass = new Decryption();
   // }
}