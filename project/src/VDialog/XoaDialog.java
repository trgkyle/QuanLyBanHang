/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package VDialog;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 *
 * @author quang
 */
public class XoaDialog extends javax.swing.JDialog {

    /**
     * Creates new form XoaDialog
     */
    
    
    private int soLuong;
    private String ma;
    private String ten;
    
    private void prepareDialog() {
        
        
        this.txtMa.setText(ma);
        this.txtTen.setText(ten);
        this.txtSL.setText(soLuong+"");
        btnHuy.addActionListener(btnHuy_Click());
        btnXoa.addActionListener(btnXoa_Click());
        btnXoaHet.addActionListener(btnXoaHet_Click());
        
        txtXoa.addKeyListener(txtSoLuongMuonXoa_KeyListener());
        
        txtSoLuongConLai.setText(soLuong+"");
        txtSoLuongConLai.setEditable(false);
    }
    
    
    private ActionListener btnHuy_Click() {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                soLuong = -1;
                dispose();
            }
        };
    }


    /**
     * Sự kiện button Đồng ý
     *
     * @return
     */
    private ActionListener btnXoa_Click() {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                
               int reply = JOptionPane.showConfirmDialog(null, "Xoa Hang", "Xoa", JOptionPane.YES_NO_OPTION);

                // nếu người dùng đồng ý
                if (reply == JOptionPane.YES_OPTION) {
                    try {
                        soLuong = soLuong - Integer.parseInt(txtXoa.getText().trim());
                        dispose();
                    } catch (Exception e1) {
                        JOptionPane.showMessageDialog(rootPane, e);
                    }
                }
               
                
                
            }
        };
    }


    /**
     * Sự kiện button Đồng ý
     *
     * @return
     */
    private ActionListener btnXoaHet_Click() {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int reply = JOptionPane.showConfirmDialog(null, "Xoa Het Hang", "Xoa", JOptionPane.YES_NO_OPTION);

                // nếu người dùng đồng ý
                if (reply == JOptionPane.YES_OPTION) {
                    try {
                       soLuong = 0;
                        dispose();
                    } catch (Exception e1) {
                        JOptionPane.showMessageDialog(rootPane, e);
                    }
                }

               
            }
        };
    }
    
    
    /**
     * Sự kiện khi nhập text số lượng
     *
     * @return
     */
    private KeyListener txtSoLuongMuonXoa_KeyListener() {
        return new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
               
            }

            @Override
            public void keyReleased(KeyEvent e) {
                try {
                    int soLuongMuonXoa = Integer.parseInt(txtXoa.getText().trim());

                   

                    txtSoLuongConLai.setText(String.valueOf(soLuong - soLuongMuonXoa));
                } catch (Exception ex) {
                    txtSoLuongConLai.setText(String.valueOf(soLuong));
                }
            }
        };
    }

    
    
    
    
    
    public int getKetQua() {
        return soLuong;
    }
    
    
    public XoaDialog(JFrame frame, String ma, String ten, int soLuong) {
       super(frame, true);
       initComponents();
        this.ma = ma;
        this.ten = ten;
        this.soLuong = soLuong;
        
         prepareDialog();

        setResizable(false);
        setSize(550, 548);
        setAlwaysOnTop(true);
        setLocationRelativeTo(null);
        //setUndecorated(true);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setVisible(true);
        
        
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        txtMa = new javax.swing.JTextField();
        txtTen = new javax.swing.JTextField();
        txtSL = new javax.swing.JTextField();
        txtXoa = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        btnHuy = new javax.swing.JButton();
        btnXoa = new javax.swing.JButton();
        btnXoaHet = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        txtSoLuongConLai = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jLabel1.setText("Mã Hàng");

        jLabel2.setText("Tên Hàng");

        jLabel3.setText("Số Lượng Có");

        jLabel4.setText("Số Lượng Xoá");

        btnHuy.setText("Thoát");

        btnXoa.setText("Xoá");

        btnXoaHet.setText("Xoá Hết");

        jLabel5.setText("Số Lượng Còn Lại");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(97, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addComponent(jLabel3)
                    .addComponent(jLabel2)
                    .addComponent(jLabel4)
                    .addComponent(jLabel5)
                    .addComponent(btnHuy, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(25, 25, 25)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(txtMa, javax.swing.GroupLayout.PREFERRED_SIZE, 215, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btnXoaHet, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(40, 40, 40)
                        .addComponent(btnXoa, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(txtTen, javax.swing.GroupLayout.PREFERRED_SIZE, 215, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtSL, javax.swing.GroupLayout.PREFERRED_SIZE, 215, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtXoa, javax.swing.GroupLayout.PREFERRED_SIZE, 215, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtSoLuongConLai, javax.swing.GroupLayout.PREFERRED_SIZE, 215, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(42, 42, 42))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(34, 34, 34)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtMa, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1))
                .addGap(29, 29, 29)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtTen, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addGap(33, 33, 33)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtSL, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addGap(36, 36, 36)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtXoa, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4))
                .addGap(27, 27, 27)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(txtSoLuongConLai, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(28, 28, 28)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnXoaHet)
                    .addComponent(btnXoa)
                    .addComponent(btnHuy))
                .addContainerGap(100, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
//    public static void main(String args[]) {
//        /* Set the Nimbus look and feel */
//        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
//        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
//         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
//         */
//        try {
//            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
//                if ("Nimbus".equals(info.getName())) {
//                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
//                    break;
//                }
//            }
//        } catch (ClassNotFoundException ex) {
//            java.util.logging.Logger.getLogger(XoaDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (InstantiationException ex) {
//            java.util.logging.Logger.getLogger(XoaDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (IllegalAccessException ex) {
//            java.util.logging.Logger.getLogger(XoaDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
//            java.util.logging.Logger.getLogger(XoaDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        }
//        //</editor-fold>
//
//        /* Create and display the dialog */
//        java.awt.EventQueue.invokeLater(new Runnable() {
//            public void run() {
//                XoaDialog dialog = new XoaDialog(new javax.swing.JFrame(), true);
//                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
//                    @Override
//                    public void windowClosing(java.awt.event.WindowEvent e) {
//                        System.exit(0);
//                    }
//                });
//                dialog.setVisible(true);
//            }
//        });
//    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnHuy;
    private javax.swing.JButton btnXoa;
    private javax.swing.JButton btnXoaHet;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JTextField txtMa;
    private javax.swing.JTextField txtSL;
    private javax.swing.JTextField txtSoLuongConLai;
    private javax.swing.JTextField txtTen;
    private javax.swing.JTextField txtXoa;
    // End of variables declaration//GEN-END:variables
}
