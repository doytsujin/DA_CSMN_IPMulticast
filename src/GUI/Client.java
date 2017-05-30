package GUI;

import controller.*;
import controller.MultiCast;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.LineUnavailableException;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import model.DatagramCollection;

public class Client extends javax.swing.JFrame {

    int port;
    MulticastReceiverMessage receiverMessage;
    MulticastReceiverVideo receiverVideo;
    MulticastReceiveVoice multicastReceiveVoice;
    HashMap<Long, DatagramCollection> datagramCollection;
    DefaultComboBoxModel model = new DefaultComboBoxModel(new String[]{});
    ArrayList<InetAddress> addressJoin;

    private void init() {
        cBIp.setModel(model);
    }

    public Client() {
        initComponents();
        init();
        addressJoin = new ArrayList<>();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jSplitPane1 = new javax.swing.JSplitPane();
        pnView = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtOutPut = new javax.swing.JTextArea();
        pnTop = new javax.swing.JPanel();
        pnTopLeft = new javax.swing.JPanel();
        txtPort = new javax.swing.JTextField();
        btnSetPort = new javax.swing.JToggleButton();
        txtIp = new javax.swing.JTextField();
        btnJoin = new javax.swing.JButton();
        cBIp = new javax.swing.JComboBox<>();
        btnLeave = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        btnReceiveVideo = new javax.swing.JToggleButton();
        btnReceiveSound = new javax.swing.JToggleButton();
        btnReceiveMessage = new javax.swing.JToggleButton();
        jButton1 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setLayout(new java.awt.BorderLayout());

        jPanel5.setLayout(new java.awt.BorderLayout());
        jPanel1.add(jPanel5, java.awt.BorderLayout.WEST);

        jSplitPane1.setDividerLocation(350);
        jSplitPane1.setResizeWeight(0.8);

        javax.swing.GroupLayout pnViewLayout = new javax.swing.GroupLayout(pnView);
        pnView.setLayout(pnViewLayout);
        pnViewLayout.setHorizontalGroup(
            pnViewLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 349, Short.MAX_VALUE)
        );
        pnViewLayout.setVerticalGroup(
            pnViewLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 354, Short.MAX_VALUE)
        );

        jSplitPane1.setLeftComponent(pnView);

        txtOutPut.setColumns(20);
        txtOutPut.setRows(5);
        txtOutPut.setFocusable(false);
        jScrollPane1.setViewportView(txtOutPut);

        jSplitPane1.setRightComponent(jScrollPane1);

        jPanel1.add(jSplitPane1, java.awt.BorderLayout.CENTER);

        getContentPane().add(jPanel1, java.awt.BorderLayout.CENTER);

        pnTop.setLayout(new java.awt.BorderLayout());

        txtPort.setColumns(5);
        txtPort.setText("12345");
        pnTopLeft.add(txtPort);

        btnSetPort.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/setting.png"))); // NOI18N
        btnSetPort.setText("Set Port");
        btnSetPort.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSetPortActionPerformed(evt);
            }
        });
        pnTopLeft.add(btnSetPort);

        txtIp.setColumns(8);
        txtIp.setText("224.0.0.2");
        txtIp.setEnabled(false);
        pnTopLeft.add(txtIp);

        btnJoin.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/join.png"))); // NOI18N
        btnJoin.setText("Join");
        btnJoin.setEnabled(false);
        btnJoin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnJoinActionPerformed(evt);
            }
        });
        pnTopLeft.add(btnJoin);

        cBIp.setMaximumRowCount(15);
        cBIp.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "224.0.0.1:12345", " " }));
        cBIp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cBIpActionPerformed(evt);
            }
        });
        pnTopLeft.add(cBIp);

        btnLeave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/leave.png"))); // NOI18N
        btnLeave.setText("Leave");
        btnLeave.setEnabled(false);
        btnLeave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLeaveActionPerformed(evt);
            }
        });
        pnTopLeft.add(btnLeave);

        pnTop.add(pnTopLeft, java.awt.BorderLayout.WEST);

        getContentPane().add(pnTop, java.awt.BorderLayout.PAGE_START);

        btnReceiveVideo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/video.png"))); // NOI18N
        btnReceiveVideo.setText("Receive Video");
        btnReceiveVideo.setEnabled(false);
        btnReceiveVideo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnReceiveVideoActionPerformed(evt);
            }
        });
        jPanel3.add(btnReceiveVideo);

        btnReceiveSound.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/soud.png"))); // NOI18N
        btnReceiveSound.setText("Receive Sound");
        btnReceiveSound.setEnabled(false);
        btnReceiveSound.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnReceiveSoundActionPerformed(evt);
            }
        });
        jPanel3.add(btnReceiveSound);

        btnReceiveMessage.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/message.png"))); // NOI18N
        btnReceiveMessage.setText("Receive Messeage");
        btnReceiveMessage.setEnabled(false);
        btnReceiveMessage.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnReceiveMessageActionPerformed(evt);
            }
        });
        jPanel3.add(btnReceiveMessage);

        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/trash.png"))); // NOI18N
        jButton1.setText("Clear");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jPanel3.add(jButton1);

        getContentPane().add(jPanel3, java.awt.BorderLayout.PAGE_END);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void cBIpActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cBIpActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cBIpActionPerformed

    private void btnJoinActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnJoinActionPerformed
        //Add Multicast ip
        String ip = txtIp.getText();
        try {
            if (model.getIndexOf(ip) == -1) {
                model.addElement(ip);
                // kiểm tra nếu không tôn tại
                InetAddress address = InetAddress.getByName(ip);
                addressJoin.add(address);
                if (receiverMessage != null) {
                    receiverMessage.join(address);
                }
            }
        } catch (Exception e) {
            model.removeElement(ip);
            if (model.getSize() <= 0) {
            }
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error, try again Or Please correct template 224.0.0.1:12345");
        }

    }//GEN-LAST:event_btnJoinActionPerformed

    private void btnLeaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLeaveActionPerformed
        try {
            // thoat khỏi ipmulticast
            if (cBIp.getSelectedItem() == null) {
                return;
            }
            String ip = cBIp.getSelectedItem().toString();
            InetAddress add = InetAddress.getByName(ip);
            if (receiverMessage != null) {
                receiverMessage.leave(add);
            }
            addressJoin.remove(add);
            //done
            this.model.removeElement(ip);
            JOptionPane.showMessageDialog(this, "Success exit...");
        } catch (UnknownHostException ex) {
            Logger.getLogger(ServerSend.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ServerSend.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btnLeaveActionPerformed

    private void btnReceiveMessageActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnReceiveMessageActionPerformed
        boolean run = btnReceiveMessage.isSelected();
        if (!run) {
            if (receiverMessage != null) {
                receiverMessage.stop();
            }
        } else {
            try {
                receiverMessage = new MulticastReceiverMessage(txtOutPut, port);
                for (InetAddress inetAddress : addressJoin) {
                    receiverMessage.join(inetAddress);
                    System.out.println("Join...");
                }

                receiverMessage.start();
            } catch (IOException ex) {
                Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
            }
        }


    }//GEN-LAST:event_btnReceiveMessageActionPerformed

    private void btnReceiveVideoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnReceiveVideoActionPerformed
        boolean run = btnReceiveVideo.isSelected();
        if (!run) {
            if (receiverVideo != null) {
                receiverVideo.stop();
            }
        } else {
            try {
                receiverVideo = new MulticastReceiverVideo(pnView, port);
                for (InetAddress inetAddress : addressJoin) {
                    receiverVideo.join(inetAddress);
                    System.out.println("Join...");
                }

                receiverVideo.start();
            } catch (IOException ex) {
                Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_btnReceiveVideoActionPerformed

    private void btnSetPortActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSetPortActionPerformed
        boolean run = btnSetPort.isSelected();
        if (!run) {
            //destroy;
            setEnable(run);
        } else {
            try {
                int port = Integer.parseInt(txtPort.getText());
                this.port = port;
                setEnable(!run);

            } catch (Exception ex) {
                Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                setEnable(run);

            }
        }

    }//GEN-LAST:event_btnSetPortActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        txtOutPut.setText("");
    }//GEN-LAST:event_jButton1ActionPerformed

    private void btnReceiveSoundActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnReceiveSoundActionPerformed
        boolean run = btnReceiveSound.isSelected();
        if (!run) {
            if (multicastReceiveVoice != null) {
                multicastReceiveVoice.stop();
            }
        } else {
            try {
                multicastReceiveVoice = new MulticastReceiveVoice(port);
                for (InetAddress inetAddress : addressJoin) {
                    multicastReceiveVoice.join(inetAddress);
                    System.out.println("Join...");
                }

                multicastReceiveVoice.start();
            } catch (IOException ex) {
                Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
            } catch (LineUnavailableException ex) {
                Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_btnReceiveSoundActionPerformed
    private void setEnable(boolean state) {
        txtPort.setEnabled(!state);
        this.txtIp.setEnabled(state);
        this.btnJoin.setEnabled(state);
        this.btnLeave.setEnabled(state);
        this.btnReceiveMessage.setEnabled(state);
        this.btnReceiveSound.setEnabled(state);
        this.btnReceiveVideo.setEnabled(state);

    }

    public static void main(String args[]) {
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
            java.util.logging.Logger.getLogger(Client.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Client.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Client.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Client.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {

                Client serverSend = new Client();
                serverSend.pack();
                serverSend.setLocationRelativeTo(null);
                serverSend.setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnJoin;
    private javax.swing.JButton btnLeave;
    private javax.swing.JToggleButton btnReceiveMessage;
    private javax.swing.JToggleButton btnReceiveSound;
    private javax.swing.JToggleButton btnReceiveVideo;
    private javax.swing.JToggleButton btnSetPort;
    private javax.swing.JComboBox<String> cBIp;
    private javax.swing.JButton jButton1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JPanel pnTop;
    private javax.swing.JPanel pnTopLeft;
    private javax.swing.JPanel pnView;
    private javax.swing.JTextField txtIp;
    private javax.swing.JTextArea txtOutPut;
    private javax.swing.JTextField txtPort;
    // End of variables declaration//GEN-END:variables
}
