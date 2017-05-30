package GUI;

import controller.MultiCast;
import controller.MulticastSendVideo;
import controller.MulticastSendVoice;
import java.io.File;
import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.LineUnavailableException;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import org.opencv.core.Core;

public class ServerSend extends javax.swing.JFrame {

    MulticastSendVideo multicastSendVideo;
    MulticastSendVoice multicastSendVoice;

    public ServerSend() {
        initComponents();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        txtIpPortVideo = new javax.swing.JTextField();
        btnStreamVideo = new javax.swing.JToggleButton();
        jPanel7 = new javax.swing.JPanel();
        txtIpPortMessage = new javax.swing.JTextField();
        btnSendMessage = new javax.swing.JButton();
        jPanel8 = new javax.swing.JPanel();
        txtIpPortSound = new javax.swing.JTextField();
        btnStreamSound = new javax.swing.JToggleButton();
        jPanel9 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        txtMessage = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Server Ip Multicast");
        setResizable(false);

        jPanel1.setLayout(new java.awt.BorderLayout());

        jPanel3.setLayout(new java.awt.BorderLayout());

        jPanel6.setLayout(new java.awt.BorderLayout());

        txtIpPortVideo.setColumns(10);
        txtIpPortVideo.setText("224.0.0.2:12345");
        jPanel6.add(txtIpPortVideo, java.awt.BorderLayout.NORTH);

        btnStreamVideo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/video.png"))); // NOI18N
        btnStreamVideo.setText("Stream Video");
        btnStreamVideo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnStreamVideoActionPerformed(evt);
            }
        });
        jPanel6.add(btnStreamVideo, java.awt.BorderLayout.CENTER);

        jPanel4.add(jPanel6);

        jPanel7.setLayout(new java.awt.BorderLayout());

        txtIpPortMessage.setColumns(10);
        txtIpPortMessage.setText("224.0.0.2:12345");
        txtIpPortMessage.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtIpPortMessageActionPerformed(evt);
            }
        });
        jPanel7.add(txtIpPortMessage, java.awt.BorderLayout.CENTER);

        btnSendMessage.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/message.png"))); // NOI18N
        btnSendMessage.setText("Send Message");
        btnSendMessage.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSendMessageActionPerformed(evt);
            }
        });
        jPanel7.add(btnSendMessage, java.awt.BorderLayout.SOUTH);

        jPanel4.add(jPanel7);

        jPanel8.setLayout(new java.awt.BorderLayout());

        txtIpPortSound.setColumns(10);
        txtIpPortSound.setText("224.0.0.2:12345");
        txtIpPortSound.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtIpPortSoundActionPerformed(evt);
            }
        });
        jPanel8.add(txtIpPortSound, java.awt.BorderLayout.CENTER);

        btnStreamSound.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/soud.png"))); // NOI18N
        btnStreamSound.setText("Stream Sound");
        btnStreamSound.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnStreamSoundActionPerformed(evt);
            }
        });
        jPanel8.add(btnStreamSound, java.awt.BorderLayout.SOUTH);

        jPanel4.add(jPanel8);

        jPanel9.setLayout(new java.awt.BorderLayout());
        jPanel4.add(jPanel9);

        jPanel3.add(jPanel4, java.awt.BorderLayout.CENTER);

        txtMessage.setColumns(36);
        txtMessage.setText("test send Message");
        jPanel5.add(txtMessage);

        jPanel3.add(jPanel5, java.awt.BorderLayout.PAGE_START);

        jPanel1.add(jPanel3, java.awt.BorderLayout.SOUTH);

        getContentPane().add(jPanel1, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnSendMessageActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSendMessageActionPerformed
        // send message to multicast
        //this.iPMulticast.send(txtMessage.getText().getBytes());
        String ipPort = txtIpPortMessage.getText();
        try {
            MultiCast multiCast = new MultiCast();
            multiCast.sendMessage(txtMessage.getText().getBytes(), System.currentTimeMillis(), getAddress(ipPort), getPort(ipPort));
        } catch (UnknownHostException ex) {
            Logger.getLogger(ServerSend.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ServerSend.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btnSendMessageActionPerformed

    private void txtIpPortMessageActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtIpPortMessageActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtIpPortMessageActionPerformed

    private void txtIpPortSoundActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtIpPortSoundActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtIpPortSoundActionPerformed

    private void btnStreamVideoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnStreamVideoActionPerformed
        // tiến hành stream camera
        boolean run = this.btnStreamVideo.isSelected();
        if (!run) {
            if (multicastSendVideo != null) {
                multicastSendVideo.destop();
                multicastSendVideo.stop();
                txtIpPortVideo.setEditable(true);
            }
        } else {
            try {
                String ipPort = txtIpPortVideo.getText();
                multicastSendVideo = new MulticastSendVideo(getAddress(ipPort), getPort(ipPort));
                multicastSendVideo.start();
                txtIpPortVideo.setEditable(false);
            } catch (UnknownHostException ex) {
                Logger.getLogger(ServerSend.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(ServerSend.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                txtIpPortVideo.setEditable(true);
            }
        }
    }//GEN-LAST:event_btnStreamVideoActionPerformed

    private void btnStreamSoundActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnStreamSoundActionPerformed
        // tiến hành stream camera
        boolean run = this.btnStreamSound.isSelected();
        if (!run) {
            if (multicastSendVoice != null) {
                multicastSendVoice.destop();
                multicastSendVoice.stop();
                txtIpPortSound.setEditable(true);
            }
        } else {
            try {
                String ipPort = txtIpPortVideo.getText();
                multicastSendVoice = new MulticastSendVoice(getAddress(ipPort), getPort(ipPort));
                multicastSendVoice.start();
                txtIpPortSound.setEditable(false);
            } catch (UnknownHostException ex) {
                Logger.getLogger(ServerSend.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(ServerSend.class.getName()).log(Level.SEVERE, null, ex);
            } catch (LineUnavailableException ex) {
                Logger.getLogger(ServerSend.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                txtIpPortSound.setEditable(true);
            }
        }
    }//GEN-LAST:event_btnStreamSoundActionPerformed

    public static void main(String args[]) {
//        if (args.length == 0) {  
//            try {  
//                // re-launch the app itselft with VM option passed  
//                Runtime.getRuntime().exec(new String[] {"java", "-Djava.library.path=\"lib\\openvc2.4\\x64\"", "-jar", "DeTaiMang_Multicast.jar"});  
//            } catch (IOException ioe) {  
//                ioe.printStackTrace();  
//            }  
//            System.exit(0);  
//        }  
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;

                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(ServerSend.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ServerSend.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ServerSend.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ServerSend.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //System.loadLibrary(Core.NATIVE_LIBRARY_NAME); // load native library of opencv  
        System.loadLibrary("opencv_java2413"); // load native library of opencv  
        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                ServerSend serverSend = new ServerSend();
                serverSend.pack();
                serverSend.setLocationRelativeTo(null);
                serverSend.setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnSendMessage;
    private javax.swing.JToggleButton btnStreamSound;
    private javax.swing.JToggleButton btnStreamVideo;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JTextField txtIpPortMessage;
    private javax.swing.JTextField txtIpPortSound;
    private javax.swing.JTextField txtIpPortVideo;
    private javax.swing.JTextField txtMessage;
    // End of variables declaration//GEN-END:variables

    DefaultComboBoxModel model = new DefaultComboBoxModel(new String[]{});

    private int getPort(String ipPort) {
        return Integer.parseInt(ipPort.split(":")[1]);
    }

    private InetAddress getAddress(String ipPort) throws UnknownHostException {
        return InetAddress.getByName(ipPort.split(":")[0]);
    }
}
