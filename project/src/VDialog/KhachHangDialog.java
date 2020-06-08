/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package VDialog;

import DAO.KhachHangDAO;
import Model.KhachHang;
import Utils.Formats;
import Utils.PatternRegexs;
import View.Admin;
import com.toedter.calendar.JDateChooser;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JRootPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

/**
 *
 * @author quang
 */
public class KhachHangDialog extends javax.swing.JDialog {

    
    
    
   
    private KhachHang khachHang;
    private boolean isChinhSua;
    private KhachHangDAO khachHangDAO;


    
    private void prepareDialog() {
        
         if (this.isChinhSua) {
             this.txtMaKH.setEditable(false);
             this.txtMaKH.setText(this.khachHang.getMaKH());
         }else{
             this.txtMaKH.setText(getMaKhachHangMoi());
             this.txtMaKH.setEditable(false);
         }
        
         
         if (this.isChinhSua) this.txtHoTen.setText(this.khachHang.getHoTen());
         
          
        if (this.isChinhSua) {
            this.txtCMND.setText(this.khachHang.getcMND());
            this.txtCMND.setEditable(false);
        }
        
        
        
        if (this.isChinhSua) this.comGioiTinh.setSelectedItem(this.khachHang.isGioiTinh() ? "Nam" : "Nữ");
        
       
        java.util.Date currentDate = new java.util.Date();
       
        if (isChinhSua) {
            dateChooser.setDate(khachHang.getNgaySinh());
        }
        else{
            dateChooser.setDate(currentDate);
        } 
        
        
        
        
        
        
        
      
        if (isChinhSua) txtSoDienThoai.setText(khachHang.getSoDienThoai());
        
        
        
        if (isChinhSua) txtDiaChi.setText(khachHang.getDiaChi());
        
        
        
        this.btnThem.addActionListener(btnLuu_Click());
    }
    
    private ActionListener btnLuu_Click() {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                
                

                // lấy thông tin khách hàng
                khachHang = new KhachHang(
                        txtCMND.getText().trim(),
                        txtHoTen.getText().trim(),
                        comGioiTinh.getSelectedItem().equals("Nam"),
                        txtSoDienThoai.getText().trim(),
                        txtDiaChi.getText().trim(),
                        Date.valueOf(Formats.DATE_FORMAT_SQL.format(dateChooser.getDate())),
                        txtMaKH.getText().trim()
                );

                // đóng dialog
                dispose();
            }
        };
    }
    
    
    
    
    
    private String getMaKhachHangMoi() {
        String lastID = "";
        String newID = "";

        // lấy mã khách hàng cuối trong DB
        try {
            lastID = khachHangDAO.getMaKhachHangCuoi();
        } catch (Exception e) {
        }

        // Nếu chưa có khách hàng trong DB thì trả về mã mặc định
        if (lastID.isEmpty()) {
            return "KH00001";
        }

        // generate mã khách hàng mới
        Pattern pattern = Pattern.compile(PatternRegexs.REGEX_MAKHACHHANG);
        Matcher matcher = pattern.matcher(lastID);
        if (matcher.find()) {
            int number = Integer.parseInt(matcher.group(1));
            number++;

            newID = String.format("KH%05d", number);
        }

        return newID;
    }
    
    public KhachHang getKhachHang() {
        return khachHang;
    }
    
    
    
    
    
    /**
     * Creates new form KhachHangDialog
     */
    public KhachHangDialog(JFrame frame, KhachHang khachHang) throws Exception {//
        super(frame, true);
        initComponents();
        
        
        this.khachHang = khachHang;

        // tạo kết nối db
        try {
            khachHangDAO = KhachHangDAO.getInstance();
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }

        
        if (khachHang == null) {
            
            isChinhSua = false;
        } else {
            
            isChinhSua = true;
        }

        // Tạo GUI
        prepareDialog();

        // set button mặc định khi nhấn Enter
        JRootPane rootPane = SwingUtilities.getRootPane(this);
        rootPane.setDefaultButton(btnThem);
        
        // cấu hình cho dialog
        setResizable(false);
        setSize(600, 500);
        setAlwaysOnTop(true);
        setLocationRelativeTo(null);
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

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        txtMaKH = new javax.swing.JTextField();
        txtHoTen = new javax.swing.JTextField();
        txtCMND = new javax.swing.JTextField();
        txtSoDienThoai = new javax.swing.JTextField();
        txtDiaChi = new javax.swing.JTextField();
        comGioiTinh = new javax.swing.JComboBox<>();
        dateChooser = new com.toedter.calendar.JDateChooser();
        btnThem = new javax.swing.JButton();
        btnLamMoi = new javax.swing.JButton();
        btnTroVe = new javax.swing.JButton();

        jLabel1.setText("Mã Khách Hàng");

        jLabel2.setText("Họ và Tên");

        jLabel3.setText("Số CMND");

        jLabel4.setText("Giới Tính");

        jLabel5.setText("Ngày Sinh");

        jLabel6.setText("Số Điện Thoại");

        jLabel7.setText("Địa chỉ");

        comGioiTinh.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Nam", "Nu", " " }));

        btnThem.setText("Lưu");

        btnLamMoi.setText("Làm Mới");

        btnTroVe.setText("Thoát");
        btnTroVe.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnTroVeMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(85, 85, 85)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btnThem, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(43, 43, 43)
                        .addComponent(btnLamMoi, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnTroVe, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel1)
                            .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel5, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel6, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel7, javax.swing.GroupLayout.Alignment.LEADING))
                        .addGap(43, 43, 43)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtCMND)
                            .addComponent(txtHoTen)
                            .addComponent(txtMaKH, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(txtSoDienThoai, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(txtDiaChi, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(comGioiTinh, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(dateChooser, javax.swing.GroupLayout.DEFAULT_SIZE, 161, Short.MAX_VALUE))))
                .addContainerGap(110, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(29, 29, 29)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel1)
                            .addComponent(txtMaKH, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(17, 17, 17)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2)
                            .addComponent(txtHoTen, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(28, 28, 28)
                        .addComponent(jLabel3))
                    .addComponent(txtCMND, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(20, 20, 20)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel4)
                    .addComponent(comGioiTinh, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(30, 30, 30)
                        .addComponent(jLabel5)
                        .addGap(24, 24, 24))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(dateChooser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(txtSoDienThoai, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(24, 24, 24)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(txtDiaChi, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(49, 49, 49)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnThem)
                    .addComponent(btnLamMoi)
                    .addComponent(btnTroVe))
                .addContainerGap(72, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btnTroVeMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnTroVeMouseClicked
       khachHang = null;
       KhachHangDialog.this.dispose();
    }//GEN-LAST:event_btnTroVeMouseClicked

    /**
     *
     * @param args
     */
   

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnLamMoi;
    private javax.swing.JButton btnThem;
    private javax.swing.JButton btnTroVe;
    private javax.swing.JComboBox<String> comGioiTinh;
    private com.toedter.calendar.JDateChooser dateChooser;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JTextField txtCMND;
    private javax.swing.JTextField txtDiaChi;
    private javax.swing.JTextField txtHoTen;
    private javax.swing.JTextField txtMaKH;
    private javax.swing.JTextField txtSoDienThoai;
    // End of variables declaration//GEN-END:variables
}
