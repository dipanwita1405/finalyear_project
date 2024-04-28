
package Audio_Steganography;

// import Coding.SendEmail;
import java.awt.Cursor;
import java.awt.Font;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.Mixer;
import javax.sound.sampled.TargetDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;

/**
 *
 * @author Hadi
 */
public class Encode_Audio extends javax.swing.JFrame {

    // private static final String algorithm = "PBEWithMD5AndDES";
    byte[] audioBytes;
    byte[] audioBytes2;
    String Message;
    // char pwd[];

    // boolean stopCapture = false;
    ByteArrayOutputStream byteArrayOutputStream;
    ByteArrayOutputStream byteArrayOutputStream2;
    AudioFormat audioFormat;
    AudioFormat audioFormat2;
    TargetDataLine targetDataLine;
    AudioInputStream audioInputStreamForEncode;
    AudioInputStream audioInputStreamForEncode2;
    AudioInputStream audioInputStream;
    AudioInputStream audioInputStream2;

    String inputAudioFilePath;
    String inputAudioFilePath2;

    private final AudioPlayer player = new AudioPlayer();
    private Thread playbackThread;
    private RecordTimer timer;
    private boolean isRecording = false;
    private boolean isPlaying = false;
    private String saveFilePath;
    private String SaveEncodeAudio;
    String EncodeAudioPath;
    private String SaveEncodeAudio2;
    String EncodeAudioPath2;

    // Icons used for buttons
    private final ImageIcon iconRecord = new ImageIcon(getClass().getResource(
            "/images/Record.gif"));
    private final ImageIcon iconStop = new ImageIcon(getClass().getResource(
            "/images/Stop.gif"));
    private final ImageIcon iconPlay = new ImageIcon(getClass().getResource(
            "/images/Play.gif"));

    private static final String SECRET_KEY = "123456789";
    private static final String SALTVALUE = "abcdefg";

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
                | InvalidKeySpecException | BadPaddingException | IllegalBlockSizeException
                | NoSuchPaddingException e) {
            System.out.println("Error occured during encryption: " + e.toString());
        }
        return null;
    }

    public Encode_Audio() {
        initComponents();
        jLabelEmbedded.setVisible(false);
        jLabelEmbedded2.setVisible(false);
        jButtonPlayEncodedAudio1.setVisible(false);
        jButtonPlayEncodedAudio2.setVisible(false);
        // captureBtn.setFont(new Font("Sans", Font.BOLD, 14));
        // captureBtn.setIcon(iconRecord);
        // playBtn.setFont(new Font("Sans", Font.BOLD, 14));
        // playBtn.setIcon(iconPlay);
        // playBtn.setEnabled(false);
        jLabel2.setVisible(false);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated
    // Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        // captureBtn = new javax.swing.JButton();
        // playBtn = new javax.swing.JButton();
        // labelRecordTime = new javax.swing.JLabel();
        jButtonSelectAudio = new javax.swing.JButton();
        jButtonSelectAudio2 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextAreaMessage = new javax.swing.JTextArea();
        jTextFieldSelectAudio = new javax.swing.JTextField();
        // jLabel1 = new javax.swing.JLabel();
        jButtonEncode = new javax.swing.JButton();
        jButtonSave = new javax.swing.JButton();
        jLabelEmbedded = new javax.swing.JLabel();
        jLabelEmbedded2 = new javax.swing.JLabel();
        // jButtonSend = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jButtonOpenDecoder = new javax.swing.JButton();
        jTextFieldSelectAudio2 = new javax.swing.JTextField();
        jButtonPlayEncodedAudio1 = new javax.swing.JButton();
        jButtonPlayEncodedAudio2 = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setResizable(false);

        jButtonSelectAudio.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jButtonSelectAudio.setText("Select First Audio");
        jButtonSelectAudio.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButtonSelectAudio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSelectAudioActionPerformed(evt);
            }
        });

        jButtonSelectAudio2.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jButtonSelectAudio2.setText("Select Second Audio");
        jButtonSelectAudio2.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButtonSelectAudio2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSelectAudioActionPerformed2(evt);
            }
        });

        jTextAreaMessage.setColumns(20);
        jTextAreaMessage.setFont(new java.awt.Font("Monospaced", 0, 14)); // NOI18N
        jTextAreaMessage.setRows(5);
        jTextAreaMessage.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Enter Message :",
                javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION,
                new java.awt.Font("Tahoma", 0, 14))); // NOI18N
        jScrollPane1.setViewportView(jTextAreaMessage);

        jTextFieldSelectAudio.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jTextFieldSelectAudio.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N

        // jLabel1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        // jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        // jLabel1.setText("Password : ");

        jButtonEncode.setFont(new java.awt.Font("Aharoni", 1, 24)); // NOI18N
        jButtonEncode.setText("Encode");
        jButtonEncode.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButtonEncode.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonEncodeActionPerformed(evt);
            }
        });

        jButtonSave.setFont(new java.awt.Font("Aharoni", 1, 24)); // NOI18N
        jButtonSave.setText("Save");
        jButtonSave.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButtonSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSaveActionPerformed(evt);
                jButtonSaveActionPerformed2(evt);
            }
        });

        jLabelEmbedded.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabelEmbedded.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabelEmbedded.setText("hh:mm:ss");

        jLabelEmbedded2.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabelEmbedded2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabelEmbedded2.setText("hh:mm:ss");

        // jButtonSend.setFont(new java.awt.Font("Aharoni", 1, 24)); // NOI18N
        // jButtonSend.setText("Send");
        // jButtonSend.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        // jButtonSend.addActionListener(new java.awt.event.ActionListener() {
        // public void actionPerformed(java.awt.event.ActionEvent evt) {
        // jButtonSendActionPerformed(evt);
        // }
        // });

        jPanel1.setBackground(new java.awt.Color(0, 0, 0));

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 36)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setText("Encode Audio");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE,
                                javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE));
        jPanel1Layout.setVerticalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, 82, Short.MAX_VALUE));

        jButtonOpenDecoder.setFont(new java.awt.Font("Aharoni", 1, 24)); // NOI18N
        jButtonOpenDecoder.setText("Open Decoder");
        jButtonOpenDecoder.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButtonOpenDecoder.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonOpenDecoderActionPerformed(evt);
            }
        });

        jTextFieldSelectAudio2.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N

        jButtonPlayEncodedAudio1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jButtonPlayEncodedAudio1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/Play.gif"))); // NOI18N
        jButtonPlayEncodedAudio1.setText("Play first audio");
        jButtonPlayEncodedAudio1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButtonPlayEncodedAudio1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonPlayEncodedAudioActionPerformed(evt);
            }
        });

        jButtonPlayEncodedAudio2.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jButtonPlayEncodedAudio2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/Play.gif"))); // NOI18N
        jButtonPlayEncodedAudio2.setText("Play second audio");
        jButtonPlayEncodedAudio2.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButtonPlayEncodedAudio2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonPlayEncodedAudioActionPerformed2(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(0, 153, 0));
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("Encoded Succesfully !");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING,
                                javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE,
                                Short.MAX_VALUE)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jScrollPane1)
                                        // .addGroup(layout.createSequentialGroup()
                                        // .addComponent(captureBtn, javax.swing.GroupLayout.DEFAULT_SIZE, 200,
                                        // Short.MAX_VALUE)
                                        // .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        // .addComponent(labelRecordTime, javax.swing.GroupLayout.PREFERRED_SIZE, 195,
                                        // javax.swing.GroupLayout.PREFERRED_SIZE)
                                        // .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        // .addComponent(playBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 281,
                                        // javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(layout.createSequentialGroup()
                                                .addGroup(layout
                                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                                        .addComponent(jButtonEncode,
                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                        .addComponent(jButtonSelectAudio,
                                                                javax.swing.GroupLayout.Alignment.LEADING,
                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                        .addComponent(jButtonSelectAudio2,
                                                                javax.swing.GroupLayout.Alignment.LEADING,
                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                        .addComponent(jButtonPlayEncodedAudio2,
                                                                javax.swing.GroupLayout.Alignment.LEADING,
                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                        .addComponent(jButtonPlayEncodedAudio1,
                                                                javax.swing.GroupLayout.Alignment.LEADING,
                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                        
                                        
                                                )
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addGroup(layout
                                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(jTextFieldSelectAudio)
                                                        .addComponent(jTextFieldSelectAudio2)
                                                        .addGroup(layout.createSequentialGroup()
                                                                .addGroup(layout.createParallelGroup(
                                                                        javax.swing.GroupLayout.Alignment.LEADING,
                                                                        false)
                                                                        .addComponent(jButtonSave,
                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                155, Short.MAX_VALUE)
                                                                        .addComponent(jLabelEmbedded,
                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                Short.MAX_VALUE)
                                                                        .addComponent(jLabelEmbedded2,
                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                Short.MAX_VALUE)
                                                                                )
                                                                .addPreferredGap(
                                                                        javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                .addGroup(layout.createParallelGroup(
                                                                        javax.swing.GroupLayout.Alignment.LEADING,
                                                                        false)
                                                                        .addGroup(layout.createSequentialGroup()
                                                                                // .addComponent(jButtonSend,
                                                                                // javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                // 185,
                                                                                // javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                // .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                                .addComponent(jButtonOpenDecoder,
                                                                                        javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                        197,
                                                                                        javax.swing.GroupLayout.PREFERRED_SIZE))
                                                                        .addComponent(jLabel2,
                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                Short.MAX_VALUE))))))
                                .addContainerGap()));
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE,
                                        javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                // .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                // .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                // .addComponent(playBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 39,
                                // javax.swing.GroupLayout.PREFERRED_SIZE)
                                // .addComponent(captureBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 39,
                                // javax.swing.GroupLayout.PREFERRED_SIZE)
                                // .addComponent(labelRecordTime, javax.swing.GroupLayout.PREFERRED_SIZE, 34,
                                // javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 230, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(jTextFieldSelectAudio)
                                        .addComponent(jButtonSelectAudio, javax.swing.GroupLayout.PREFERRED_SIZE, 35,
                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(jTextFieldSelectAudio2)
                                        .addComponent(jButtonSelectAudio2, javax.swing.GroupLayout.PREFERRED_SIZE, 35,
                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(jButtonPlayEncodedAudio1,
                                                        javax.swing.GroupLayout.PREFERRED_SIZE, 31,
                                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(0, 1, Short.MAX_VALUE))
                                        .addComponent(jLabelEmbedded, javax.swing.GroupLayout.DEFAULT_SIZE,
                                                javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE,
                                                javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(layout.createSequentialGroup()
                                .addComponent(jButtonPlayEncodedAudio2,
                                javax.swing.GroupLayout.PREFERRED_SIZE, 31,
                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 1, Short.MAX_VALUE))
                                .addComponent(jLabelEmbedded2, javax.swing.GroupLayout.DEFAULT_SIZE,
                                javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)

                                )
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                .addComponent(jButtonSave, javax.swing.GroupLayout.PREFERRED_SIZE, 56,
                                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                        // .addComponent(jButtonSend, javax.swing.GroupLayout.PREFERRED_SIZE, 56,
                                        // javax.swing.GroupLayout.PREFERRED_SIZE)
                                        )
                                        .addComponent(jButtonEncode, javax.swing.GroupLayout.DEFAULT_SIZE,
                                                javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jButtonOpenDecoder, javax.swing.GroupLayout.DEFAULT_SIZE,
                                                javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addContainerGap()));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    // private void captureBtnActionPerformed(java.awt.event.ActionEvent evt)
    // {//GEN-FIRST:event_captureBtnActionPerformed
    // if (isRecording == false)
    // startRecording();
    // else
    // stopRecording();

    // }//GEN-LAST:event_captureBtnActionPerformed

    // private void playBtnActionPerformed(java.awt.event.ActionEvent evt)
    // {//GEN-FIRST:event_playBtnActionPerformed
    // if (!isPlaying)
    // playBack();
    // else
    // stopPlaying();
    // }//GEN-LAST:event_playBtnActionPerformed

    private void jButtonSaveActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButtonSaveActionPerformed
        if (jButtonPlayEncodedAudio1.isShowing())
            showFileDialog(false);
        else
            JOptionPane.showMessageDialog(this, "Error! Audio is Not Encoded ");
    }// GEN-LAST:event_jButtonSaveActionPerformed

    private void jButtonSaveActionPerformed2(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButtonSaveActionPerformed
        if (jButtonPlayEncodedAudio2.isShowing())
            showFileDialog2(false);
        else
            JOptionPane.showMessageDialog(this, "Error! Audio is Not Encoded ");
    }// GEN-LAST:event_jButtonSaveActionPerformed

    private void jButtonSelectAudioActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButtonSelectAudioActionPerformed

        showFileDialog(true);
    }// GEN-LAST:event_jButtonSelectAudioActionPerformed

    private void jButtonSelectAudioActionPerformed2(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButtonSelectAudioActionPerformed

        showFileDialog2(true);
    }// GEN-LAST:event_jButtonSelectAudioActionPerformed

    private void jButtonOpenDecoderActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButtonOpenDecoderActionPerformed
        Decode_Audio audio = new Decode_Audio();
        audio.setVisible(true);
        audio.setLocationRelativeTo(null);
        this.dispose();

    }// GEN-LAST:event_jButtonOpenDecoderActionPerformed

    private void jButtonEncodeActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButtonEncodeActionPerformed
        if (jTextFieldSelectAudio.getText().equals("")
                || jTextFieldSelectAudio2.getText().equals("")
                || jTextAreaMessage.getText().equals(""))
            JOptionPane.showMessageDialog(this, "Please Fill all the Field !");
        else {
            boolean isEm=embed();
            if (isEm) {
                jButtonPlayEncodedAudio1.setVisible(true);
                jButtonPlayEncodedAudio2.setVisible(true);
                jLabelEmbedded.setVisible(true);
                jLabelEmbedded2.setVisible(true);
                jLabel2.setVisible(true);
            } else
                JOptionPane.showMessageDialog(this, "Embed Error");
        }
    }// GEN-LAST:event_jButtonEncodeActionPerformed

    boolean isPlayingEncodedAudio = false;
    boolean isPlayingEncodedAudio2 = false;

    private void jButtonPlayEncodedAudioActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButtonPlayEncodedAudioActionPerformed
        if (isPlayingEncodedAudio == false)
            playEncodedAudio();
        else
            stopPlayingEncoded();
    }// GEN-LAST:event_jButtonPlayEncodedAudioActionPerformed
    private void jButtonPlayEncodedAudioActionPerformed2(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButtonPlayEncodedAudioActionPerformed
        if (isPlayingEncodedAudio2 == false)
            playEncodedAudio2();
        else
            stopPlayingEncoded2();
    }// GEN-LAST:event_jButtonPlayEncodedAudioActionPerformed

    // GEN-LAST:event_jButtonSendActionPerformed

    /************* Start recording sound, the time will count up. *************/
    // private void startRecording() {
    // captureBtn.setText("Stop");
    // captureBtn.setIcon(iconStop);
    // playBtn.setEnabled(false);
    // isRecording = true;
    // captureAudio();

    // timer = new RecordTimer(labelRecordTime);
    // timer.start();
    // }

    /**************
     * Stop recording and save the sound into a WAV file.
     ***************/
    // private void stopRecording() {
    // timer.cancel();
    // captureBtn.setText("Record");
    // captureBtn.setIcon(iconRecord);
    // playBtn.setEnabled(true);
    // isRecording = false;
    // stopCapture = true;
    // setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
    // saveAudioFile();
    // }

    /****************** Start playing back the sound. *****************************/
    // private void playBack() {
    // timer = new RecordTimer(labelRecordTime);
    // timer.start();
    // isPlaying = true;
    // playbackThread = new Thread(new Runnable() {
    // @Override
    // public void run() {
    // try {
    // playBtn.setText("Stop");
    // playBtn.setIcon(iconStop);
    // captureBtn.setEnabled(false);

    // player.play(saveFilePath);
    // timer.reset();

    // playBtn.setText("Play");
    // captureBtn.setEnabled(true);
    // playBtn.setIcon(iconPlay);
    // isPlaying = false;
    // } catch (UnsupportedAudioFileException | LineUnavailableException |
    // IOException ex) {
    // }
    // }
    // });
    // playbackThread.start();
    // }

    /****************** Stop playing back. ********************************** */
    // private void stopPlaying() {
    //     timer.reset();
    //     timer.interrupt();
    //     player.stop();
    //     playbackThread.interrupt();
    // }

    private void stopPlayingEncoded() {
        timer.reset();
        timer.interrupt();
        jButtonPlayEncodedAudio1.setText("Play first audio");
        jButtonPlayEncodedAudio1.setIcon(iconPlay);
        isPlayingEncodedAudio = false;
        player.stop();
        playbackThread.interrupt();

    }
    private void stopPlayingEncoded2() {
        timer.reset();
        timer.interrupt();
        jButtonPlayEncodedAudio2.setText("Play second audio");
        jButtonPlayEncodedAudio2.setIcon(iconPlay);
        isPlayingEncodedAudio2 = false;
        player.stop();
        playbackThread.interrupt();

    }
    /*
     * =============================================================================
     * =====
     */
    /*
     * =============================================================================
     * =====
     */
    /******************
     * Start Save the recorded sound into a WAV file.
     *******************/

    // private void saveAudioFile() {
    // JFileChooser fileChooser = new JFileChooser();
    // FileFilter wavFilter = new FileFilter() {
    // @Override
    // public String getDescription() {
    // return "Sound file (*.WAV)";
    // }
    // @Override
    // public boolean accept(File file) {
    // if (file.isDirectory()) {
    // return true;
    // } else {
    // return file.getName().toLowerCase().endsWith(".wav");
    // }
    // }
    // };
    // fileChooser.setFileFilter(wavFilter);
    // fileChooser.setAcceptAllFileFilterUsed(false);

    // int userChoice = fileChooser.showSaveDialog(this);
    // if (userChoice == JFileChooser.APPROVE_OPTION) {
    // saveFilePath = fileChooser.getSelectedFile().getAbsolutePath();
    // if (!saveFilePath.toLowerCase().endsWith(".wav")) {
    // saveFilePath += ".wav";
    // }
    // File wavFile = new File(saveFilePath);
    // try {
    // save(wavFile);
    // JOptionPane.showMessageDialog(null,
    // "Saved recorded sound to:\n" + saveFilePath);
    // playBtn.setEnabled(true);

    // } catch (IOException ex) {
    // JOptionPane.showMessageDialog(null, "Error",
    // "Error saving to sound file!",
    // JOptionPane.ERROR_MESSAGE);
    // }
    // }
    // }
    /**
     * save Method for saving Audio After Recording
     * 
     * @param wavFile
     * @throws java.io.IOException
     */
    // public void save(File wavFile) throws IOException {
    //     byte[] audioData = byteArrayOutputStream.toByteArray();
    //     ByteArrayInputStream bais = new ByteArrayInputStream(audioData);
    //     AudioFormat audioFormat = getAudioFormat();
    //     AudioInputStream audioInputStream = new AudioInputStream(bais, audioFormat,
    //             audioData.length / audioFormat.getFrameSize());
    //     AudioSystem.write(audioInputStream, AudioFileFormat.Type.WAVE, wavFile);

    //     audioInputStream.close();
    //     byteArrayOutputStream.close();
    // }

    // public void save2(File wavFile) throws IOException {
    //     byte[] audioData2 = byteArrayOutputStream2.toByteArray();
    //     ByteArrayInputStream bais2 = new ByteArrayInputStream(audioData2);
    //     AudioFormat audioFormat2 = getAudioFormat();
    //     AudioInputStream audioInputStream2 = new AudioInputStream(bais2, audioFormat,
    //             audioData2.length / audioFormat2.getFrameSize());
    //     AudioSystem.write(audioInputStream2, AudioFileFormat.Type.WAVE, wavFile);

    //     audioInputStream2.close();
    //     byteArrayOutputStream2.close();
    // }
    /***************
     * End of save Method for saving Audio After Recording.
     *************/

    /**********************************************************************************/
    /*
     * =============================================================================
     * ===
     */
    /******************************
     * Start of Capture Audio.
     ***************************/
    // This method captures audio input
    // from a microphone and saves it in a
    // ByteArrayOutputStream object.

    // private void captureAudio(){
    // try{
    // //Get and display a list of
    // // available mixers.
    // Mixer.Info[] mixerInfo = AudioSystem.getMixerInfo();
    // System.out.println("Available mixers:");
    // for(int cnt = 0; cnt < mixerInfo.length; cnt++){
    // System.out.println(mixerInfo[cnt].getName());
    // }//end for loop
    // //Get everything set up for capture
    // audioFormat = getAudioFormat();
    // DataLine.Info dataLineInfo = new DataLine.Info(TargetDataLine.class,
    // audioFormat);
    // //Select one of the available
    // // mixers.
    // Mixer mixer = AudioSystem.getMixer(mixerInfo[3]);
    // //Get a TargetDataLine on the selected
    // // mixer.
    // targetDataLine = (TargetDataLine)
    // mixer.getLine(dataLineInfo);
    // //Prepare the line for use.
    // targetDataLine.open(audioFormat);
    // targetDataLine.start();
    // //Create a thread to capture the microphone
    // // data and start it running. It will run
    // // until the Stop button is clicked.
    // Thread captureThread = new CaptureThread();
    // captureThread.start();
    // } catch (Exception e) {
    // System.out.println(e);
    // }//end catch
    // }
    // This method creates and returns an
    // AudioFormat object for a given set of format
    // parameters. If these parameters don't work
    // well for you, try some of the other
    // allowable parameter values, which are shown
    // in comments following the declartions.
    private AudioFormat getAudioFormat() {
        float sampleRate = 8000.0F;
        // 8000,11025,16000,22050,44100
        int sampleSizeInBits = 16;
        // 8,16
        int channels = 1;
        // 1,2
        boolean signed = false;
        // true,false
        boolean bigEndian = false;
        // true,false
        return new AudioFormat(sampleRate, sampleSizeInBits, channels, signed, bigEndian);
    }// end getAudioFormat
     // =================================================================================//
     // Inner class to capture data from microphone
     // class CaptureThread extends Thread{
     // //An arbitrary-size temporary holding buffer
     // byte tempBuffer[] = new byte[10000];
     // public void run(){
     // byteArrayOutputStream = new ByteArrayOutputStream();
     // stopCapture = false;
     // try{//Loop until stopCapture is set by
     // // another thread that services the Stop
     // // button.
     // while(!stopCapture){
     // //Read data from the internal buffer of
     // // the data line.
     // int cnt = targetDataLine.read(tempBuffer, 0, tempBuffer.length);
     // if(cnt > 0){
     // //Save data in output stream object.
     // byteArrayOutputStream.write(tempBuffer, 0, cnt);
     // }//end if
     // }//end while
     // byteArrayOutputStream.close();
     // }catch (Exception e) {
     // System.out.println(e);
     // System.exit(0);
     // }//end catch
     // }//end run
     // }//end inner class CaptureThread
    /****************************
     * End of Capture Audio Methods.
     ************************/

    // ================================================================================//
    // ================================================================================//
    /*
     * =============================================================================
     * ===
     */
    /**************
     * Open File for select Audio or Save Encoded Audio .
     ****************/
    /*
     * =============================================================================
     * ===
     */
    private java.io.File showFileDialog(final boolean open) {
        JFileChooser fc = new JFileChooser("Open a File");
        javax.swing.filechooser.FileFilter ff = new javax.swing.filechooser.FileFilter() {
            @Override
            public boolean accept(File f) {
                String name = f.getName().toLowerCase();
                if (open)
                    return f.isDirectory() ||
                            name.endsWith(".wav");
                return f.isDirectory() || name.endsWith(".wav");
            }

            @Override
            public String getDescription() {
                if (open)
                    return "Audio (*.wav)";
                return "Audio (*.wav)";
            }
        };
        fc.setAcceptAllFileFilterUsed(false);
        fc.addChoosableFileFilter(ff);

        java.io.File f = null;
        if (open && fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {

            EncodeAudioPath = fc.getSelectedFile().getAbsolutePath();
            jTextFieldSelectAudio.setText(fc.getSelectedFile().getAbsolutePath());
        } else if (!open && fc.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {

            SaveEncodeAudio = fc.getSelectedFile().getAbsolutePath();

            /**************
             * now write the byte array to an audio file.
             ************************/
            File fileOut = new File(SaveEncodeAudio);
            ByteArrayInputStream byteIS = new ByteArrayInputStream(audioBytes);
            AudioInputStream audioIS = new AudioInputStream(byteIS,
                    audioInputStreamForEncode.getFormat(), audioInputStreamForEncode.getFrameLength());
            if (AudioSystem.isFileTypeSupported(AudioFileFormat.Type.WAVE, audioIS)) {
                try {
                    AudioSystem.write(audioIS, AudioFileFormat.Type.WAVE, fileOut);
                    // System.out.println("Steganographed AU file is written as "
                    //         + SaveEncodeAudio + "...");
                    // System.out.println();
                } catch (Exception e) {
                    System.err.println("Sound File write error");
                }
            }
        }
        return f;
    }

    private java.io.File showFileDialog2(final boolean open) {
        JFileChooser fc2 = new JFileChooser("Open a File");
        javax.swing.filechooser.FileFilter ff2 = new javax.swing.filechooser.FileFilter() {
            @Override
            public boolean accept(File f2) {
                String name2 = f2.getName().toLowerCase();
                if (open)
                    return f2.isDirectory() ||
                            name2.endsWith(".wav");
                return f2.isDirectory() || name2.endsWith(".wav");
            }

            @Override
            public String getDescription() {
                if (open)
                    return "Audio (*.wav)";
                return "Audio (*.wav)";
            }
        };
        fc2.setAcceptAllFileFilterUsed(false);
        fc2.addChoosableFileFilter(ff2);

        java.io.File f2 = null;
        if (open && fc2.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {

            EncodeAudioPath2 = fc2.getSelectedFile().getAbsolutePath();
            jTextFieldSelectAudio2.setText(fc2.getSelectedFile().getAbsolutePath());
        } else if (!open && fc2.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {

            SaveEncodeAudio2 = fc2.getSelectedFile().getAbsolutePath();
            /**************
             * now write the byte array to an audio file.
             ************************/
            File fileOut2 = new File(SaveEncodeAudio2);
            ByteArrayInputStream byteIS2 = new ByteArrayInputStream(audioBytes2);
            AudioInputStream audioIS2 = new AudioInputStream(byteIS2,
                    audioInputStreamForEncode2.getFormat(), audioInputStreamForEncode2.getFrameLength());
            if (AudioSystem.isFileTypeSupported(AudioFileFormat.Type.WAVE, audioIS2)) {
                try {
                    AudioSystem.write(audioIS2, AudioFileFormat.Type.WAVE, fileOut2);
                    // System.out.println("Steganographed AU file2 is written as "
                    //         + SaveEncodeAudio2 + "...");
                    // System.out.println();
                } catch (Exception e) {
                    System.err.println("Sound File write error");
                }
            }
        }
        return f2;
    }
    /****************************
     * End Of File Dialog Window.
     *************************/
    /*
     * =============================================================================
     * ===
     */
    /*
     * =============================================================================
     * ===
     */
    /****************************
     * Start Of Encoding Functions.
     *************************/

    /*
     * =============================================================================
     * ===
     */
    /**
     * End Of File Dialog Window.
     * 
     * @return
     */
    public boolean embed() {

        // pwd = jTextPassword.getText().toCharArray();
        Message = jTextAreaMessage.getText();
        int messLen = Message.length();
        inputAudioFilePath = jTextFieldSelectAudio.getText();
        inputAudioFilePath2 = jTextFieldSelectAudio2.getText();
        // System.out.println(inputAudioFilePath);
        // System.out.println(inputAudioFilePath2);
        audioBytes = readAudio(inputAudioFilePath);
        audioBytes2 = readAudio(inputAudioFilePath2);
        String first = "";
        String second = "";
        for (int i = 0; i < Message.length(); i++) {
            if (i % 2 == 0)
                first += Message.charAt(i);
            else
                second += Message.charAt(i);
        }
        String newFirst = encrypt(first);
        String newSecond = encrypt(second);
        try {
            audioInputStreamForEncode = AudioSystem.getAudioInputStream(new File(inputAudioFilePath));
            // System.out.println(audioInputStreamForEncode);
            audioInputStreamForEncode2 = AudioSystem.getAudioInputStream(new File(inputAudioFilePath2));
            // System.out.println(audioInputStreamForEncode2);
        } catch (UnsupportedAudioFileException | IOException e) {
        }

        if (audioBytes == null || audioBytes2 == null)
            return false;
        else {

            // System.out.println("Embedding the text message in the audio file ...");
            // System.out.println("The plain text data length: " + Message.length());

            // Encrypt the text data.
            byte[] encryptedTextBytes1 = newFirst.getBytes();
            byte[] encryptedTextBytes2 = newSecond.getBytes();
            try {
                // encryptedTextBytes = encrypt(Message);
                if (newFirst.length() * 8 + 32 > audioBytes.length || newSecond.length() * 8 + 32 > audioBytes2.length)
                    return false;

            } catch (Exception e) {
                System.out.println("Error while encrypting the plain text data: "
                        + e);
            }
            // System.out.println("The encrypted first message : "
            //         + newFirst);
            // System.out.println("The encrypted second message : "
            //         + newSecond);

            // First encode the length of the encrypted text
            int messageLength1 = encryptedTextBytes1.length;
            int messageLength2 = encryptedTextBytes2.length;
            byte[] len1 = bit_conversion(messageLength1);
            byte[] len2 = bit_conversion(messageLength2);

            // System.out
            //         .println("The audio bytes before embedding the first message length: "
            //                 + audioBytes.length);
            // System.out
            //         .println("The audio bytes before embedding the second message length: "
            //                 + audioBytes2.length);

            audioBytes = encode_text(audioBytes, len1, 0);
            audioBytes2 = encode_text(audioBytes2, len2, 0);

            // System.out.println("The audio bytes after embedding the message length: "
            //         + audioBytes.length);
            // System.out.println("The audio bytes after embedding the second message length: "
            //         + audioBytes2.length);

            audioBytes = encode_text(audioBytes, encryptedTextBytes1, 32);
            audioBytes2 = encode_text(audioBytes2, encryptedTextBytes2, 32);

            // System.out.println("The audio bytes after embedding the first text message: "
            //         + audioBytes.length);
            // System.out.println("The audio bytes after embedding the second text message: "
            //         + audioBytes2.length);

            // System.out.println();
            return true;
        }
    }

    private byte[] encode_text(byte[] data, byte[] addition, int offset) {
        // check that the data + offset will fit in the image

        if (addition.length + offset > data.length) {
            throw new IllegalArgumentException("File not long enough!");
        }

        // loop through each addition byte
        for (int i = 0; i < addition.length; ++i) {
            // loop through the 8 bits of each byte
            int add = addition[i];
            for (int bit = 7; bit >= 0; --bit, ++offset) // ensure the new // through both
                                                         // loops
            {
                // assign an integer to b, shifted by bit spaces AND 1
                // a single bit of the current byte
                int b = (add >>> bit) & 1;

                // assign the bit by taking: [(previous byte value) AND 0xfe] OR
                // bit to add

                // changes the last bit of the byte in the image to be the bit
                // of addition

                data[offset] = (byte) ((data[offset] & 0xFE) | b);
            }
        }
        return data;
    }

    private byte[] bit_conversion(int i) {
        return (new byte[] { 0, 0, 0, (byte) (i & 0x000000FF) });
    }

    public static byte[] readAudio(String audioFilePath) {

        // System.out.println("Reading the audio file......");

        AudioInputStream audioInputStream = null;
        byte[] audioBytes = null;
        File audioFile = new File(audioFilePath);

        try {
            audioInputStream = AudioSystem.getAudioInputStream(audioFile);
            int bytesPerFrame = audioInputStream.getFormat().getFrameSize();
            // Set an arbitrary buffer size of 1024 frames.
            int numBytes = 154600 * bytesPerFrame;
            audioBytes = new byte[numBytes];

            try {
                audioInputStream.read(audioBytes);

            } catch (Exception ex) {
                // System.out.println("Audio file error:" + ex);
                JOptionPane.showMessageDialog(null, "Audio Error Please Check Your Audio File!");
                return null;
            }
        } catch (UnsupportedAudioFileException | IOException e) {
            // System.out.println("Audio file error:" + e);
            JOptionPane.showMessageDialog(null, "Audio Error Check The Audio File");
            return null;
        }
        return audioBytes;
    }

    // public byte[] encrypt(byte[] plainText, char password[]) {
    // System.out.println("Encrypting the plaintext message: "
    // + new String(plainText));
    // System.out.println("The plain message size is: " + plainText.length);
    // System.out.println("The encryption password length: " + password.length);
    // PBEKeySpec pbeKeySpec;
    // PBEParameterSpec pbeParamSpec;
    // SecretKeyFactory keyFac;
    // byte[] cipherbuff = null;
    // // Salt
    // byte[] salt = { (byte) 0xc7, (byte) 0x73, (byte) 0x21, (byte) 0x8c,
    // (byte) 0x7e, (byte) 0xc8, (byte) 0xee, (byte) 0x99 };

    // // Iteration count
    // int count = 20;

    // // Create PBE parameter set
    // pbeParamSpec = new PBEParameterSpec(salt, count);

    // try {
    // pbeKeySpec = new PBEKeySpec(password);

    // keyFac = SecretKeyFactory.getInstance(algorithm);
    // SecretKey pbeKey = keyFac.generateSecret(pbeKeySpec);

    // // Create PBE Cipher
    // Cipher pbeCipher = Cipher.getInstance(algorithm);

    // // Initialize PBE Cipher with key and parameters
    // pbeCipher.init(Cipher.ENCRYPT_MODE, pbeKey, pbeParamSpec);

    // cipherbuff = pbeCipher.doFinal(plainText);
    // } catch (NoSuchAlgorithmException | InvalidKeySpecException |
    // NoSuchPaddingException | InvalidKeyException
    // | InvalidAlgorithmParameterException | IllegalBlockSizeException |
    // BadPaddingException ex) {
    // // Handle the error...
    // System.out.println("Caught Exception while encryption: " + ex);
    // }

    // System.out.println("The encrypted message: " + new String(cipherbuff));
    // System.out
    // .println("The encrypted message length: " + cipherbuff.length);
    // return cipherbuff;
    // }

    private void playEncodedAudio() {
        timer = new RecordTimer(jLabelEmbedded);
        timer.start();

        isPlayingEncodedAudio = true;
        playbackThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    jButtonPlayEncodedAudio1.setText("Stop");
                    jButtonPlayEncodedAudio1.setIcon(iconStop);

                    player.play(EncodeAudioPath);
                    
                    timer.reset();
                    isPlaying = false;

                } catch (UnsupportedAudioFileException | LineUnavailableException | IOException ex) {
                }
            }
        });
        playbackThread.start();
    }

    private void playEncodedAudio2() {
        timer = new RecordTimer(jLabelEmbedded2);
        timer.start();

        isPlayingEncodedAudio2 = true;
        playbackThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    jButtonPlayEncodedAudio2.setText("Stop");
                    jButtonPlayEncodedAudio2.setIcon(iconStop);

                    player.play(EncodeAudioPath2);
                    timer.reset();
                    isPlaying = false;

                } catch (UnsupportedAudioFileException | LineUnavailableException | IOException ex) {
                }
            }
        });
        playbackThread.start();
    }

    /***************************
     * End Of Encoding Functions.
     ****************************/
    /*
     * =============================================================================
     * ====
     */
    /*
     * =============================================================================
     * ====
     */
    /*
     * =============================================================================
     * ====
     */
    /*
     * =============================================================================
     * ====
     */
    /*
     * =============================================================================
     * ====
     */
    /**
     * End Of Encoding Functions.
     * 
     * @param args
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        // <editor-fold defaultstate="collapsed" desc=" Look and feel setting code
        // (optional) ">
        /*
         * If Nimbus (introduced in Java SE 6) is not available, stay with the default
         * look and feel.
         * For details see
         * http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Encode_Audio.class.getName()).log(java.util.logging.Level.SEVERE, null,
                    ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Encode_Audio.class.getName()).log(java.util.logging.Level.SEVERE, null,
                    ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Encode_Audio.class.getName()).log(java.util.logging.Level.SEVERE, null,
                    ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Encode_Audio.class.getName()).log(java.util.logging.Level.SEVERE, null,
                    ex);
        }
        // </editor-fold>
        // </editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Encode_Audio().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // private javax.swing.JButton captureBtn;
    private javax.swing.JButton jButtonEncode;
    private javax.swing.JButton jButtonOpenDecoder;
    private javax.swing.JButton jButtonPlayEncodedAudio1;
    private javax.swing.JButton jButtonPlayEncodedAudio2;
    private javax.swing.JButton jButtonSave;
    private javax.swing.JButton jButtonSelectAudio;
    private javax.swing.JButton jButtonSelectAudio2;
    // private javax.swing.JButton jButtonSend;
    // private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabelEmbedded;
    private javax.swing.JLabel jLabelEmbedded2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextArea jTextAreaMessage;
    private javax.swing.JTextField jTextFieldSelectAudio;
    private javax.swing.JTextField jTextFieldSelectAudio2;
    // private javax.swing.JLabel labelRecordTime;
    // private javax.swing.JButton playBtn;
    // End of variables declaration//GEN-END:variables
}
