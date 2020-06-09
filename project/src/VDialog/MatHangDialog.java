/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package VDialog;

import DAO.MatHangDAO;
import DAO.NhaCungCapDAO;
import Model.DanhSachNhaCungCap;
import Model.MatHang;
import Model.NhaCungCap;
import Utils.PatternRegexs;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JFrame;
import javax.swing.JRootPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

/**
 *
 * @author quang
 */
public class MatHangDialog extends javax.swing.JDialog {

    /**
     * Creates new form MatHangDialog
     */
    
    private MatHang matHang;
    private boolean isChinhSua;
    private MatHangDAO matHangDAO;
    private DanhSachNhaCungCap danhSachNhaCungCap;
    
    
     private void prepareDialog() {
         
         if (this.isChinhSua) {
             this.txtMaMatHang.setEditable(false);
             this.txtMaMatHang.setText(this.matHang.getMaMatHang());
         }else{
             this.txtMaMatHang.setText(getMaMatHangMoi());
             this.txtMaMatHang.setEditable(false);
         }
        
         
         if (this.isChinhSua) this.txtTenMatHang.setText(this.matHang.getTenMatHang());
         
          
        if (this.isChinhSua) {
            this.txtHangSanXuat.setText(this.matHang.getHangSanXuat());
            
        }
        
        
        
        if (this.isChinhSua){
            this.comTinhTrang.setEnabled(true);
            this.comTinhTrang.setSelectedItem(this.matHang.isTinhTrang() ? "Mới" : "Hỏng");
        }
        
        
        if (this.isChinhSua){  this.txtDonGia.setText(this.matHang.getDonGia()+"");}
             
        if (this.isChinhSua){ this.txtGhiChu.setText(this.matHang.getGhiChu());}
          
        if (this.isChinhSua){  this.txtSoLuongTon.setText( this.matHang.getSoLuongTon()+"");}
        
        if (this.isChinhSua){  this.txtLoaiMathang.setText(this.matHang.getTheLoai()+"");}
       
        
        this.btnThem.addActionListener(btnLuu_Click());
    }
    
    private ActionListener btnLuu_Click() {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // kiểm tra dữ liệu nhập
                

                // lấy thông tin mat hàng
                matHang = new MatHang(
                        txtMaMatHang.getText().trim(),
                        txtTenMatHang.getText().trim(),
                        txtLoaiMathang.getText().trim(),
                        String.valueOf(comTinhTrang.getSelectedItem()).equalsIgnoreCase("Mới"),
                        txtHangSanXuat.getText().trim(),
                        txtGhiChu.getText().trim(),
                        Double.parseDouble(txtDonGia.getText().trim()),
                        Integer.parseInt(txtSoLuongTon.getText().trim())
                );

                // đóng dialog
                dispose();
            }
        };
    }
    
    
    
    
    /**
     * Generate mã mặt hàng  mới
     *
     * @return
     */
    private String getMaMatHangMoi() {
        String lastID = "";
        String newID = "";

        // lấy mã mat hang cuối trong DB
        try {
            lastID = matHangDAO.getMaMatHangCuoi();
        } catch (Exception e) {
        }

        // nếu chưa có mat hang nào trong DB thì trả về mã mặc định đầu tiên
        if (lastID.isEmpty()) {
            return "MH00001";
        }

        // generate mã
        Pattern pattern = Pattern.compile(PatternRegexs.REGEX_MAMATHANG);
        Matcher matcher = pattern.matcher(lastID);
        if (matcher.find()) {
            int number = Integer.parseInt(matcher.group(1));
            number++;

            newID = String.format("MH%05d", number);
        }

        return newID;
    }
    
    
    public MatHang getMatHang(){
        return matHang;
    }
    
  
    
    public MatHangDialog(JFrame frame, MatHang matHang) throws Exception {
        super(frame, true);
        initComponents();
        
        this.matHang = matHang;
        // lấy instance kết nối với db (table MATHANG)
        try {
            matHangDAO = MatHangDAO.getInstance();
            danhSachNhaCungCap = new DanhSachNhaCungCap();
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }

       
        
        if (matHang == null) {
            
            isChinhSua = false;
        } else {
           
            isChinhSua = true;
        }

        // tạo GUI
        prepareDialog();

        // đặt button mặc định khi bấm Enter
        JRootPane rootPane = SwingUtilities.getRootPane(this);
        rootPane.setDefaultButton(btnThem);

        // cấu hình cho dialog
        setResizable(false);
       
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
        jLabel8 = new javax.swing.JLabel();
        btnThem = new javax.swing.JButton();
        btnThoat = new javax.swing.JButton();
        txtMaMatHang = new javax.swing.JTextField();
        txtTenMatHang = new javax.swing.JTextField();
        txtLoaiMathang = new javax.swing.JTextField();
        txtHangSanXuat = new javax.swing.JTextField();
        txtSoLuongTon = new javax.swing.JTextField();
        comTinhTrang = new javax.swing.JComboBox<>();
        txtDonGia = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtGhiChu = new javax.swing.JTextArea();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jLabel1.setText("Mã Mặt Hàng");

        jLabel2.setText("Tên Mặt Hàng");

        jLabel3.setText("Loại Hàng");

        jLabel4.setText("Mã NCC");

        jLabel5.setText("Số Lượng Tồn");

        jLabel6.setText("Tình Trạng");

        jLabel7.setText("Đơn Giá");

        jLabel8.setText("Ghi Chú");

        btnThem.setText("Lưu");

        btnThoat.setText("Thoát");
        btnThoat.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnThoatMouseClicked(evt);
            }
        });

        comTinhTrang.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Mới", "Hỏng" }));

        txtGhiChu.setColumns(20);
        txtGhiChu.setRows(5);
        jScrollPane1.setViewportView(txtGhiChu);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(0, 106, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel8)
                    .addComponent(jLabel7)
                    .addComponent(jLabel6)
                    .addComponent(jLabel5)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addGroup(layout.createSequentialGroup()
                            .addComponent(btnThem, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(119, 119, 119)
                            .addComponent(btnThoat, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jLabel3)
                                        .addComponent(jLabel4)
                                        .addComponent(jLabel2))
                                    .addGap(59, 59, 59))
                                .addGroup(layout.createSequentialGroup()
                                    .addComponent(jLabel1)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(txtHangSanXuat, javax.swing.GroupLayout.PREFERRED_SIZE, 222, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(txtMaMatHang)
                                        .addComponent(txtTenMatHang, javax.swing.GroupLayout.PREFERRED_SIZE, 222, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(txtLoaiMathang, javax.swing.GroupLayout.PREFERRED_SIZE, 222, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addComponent(txtSoLuongTon, javax.swing.GroupLayout.PREFERRED_SIZE, 222, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(comTinhTrang, javax.swing.GroupLayout.PREFERRED_SIZE, 222, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(txtDonGia, javax.swing.GroupLayout.PREFERRED_SIZE, 222, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 222, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGap(149, 149, 149)))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(50, 50, 50)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtMaMatHang, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel1))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2)
                            .addComponent(txtTenMatHang, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtLoaiMathang, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel3))
                        .addGap(18, 18, 18)
                        .addComponent(txtHangSanXuat, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 26, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(txtSoLuongTon, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(20, 20, 20)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(comTinhTrang, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(17, 17, 17)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel7)
                    .addComponent(txtDonGia, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(46, 46, 46)
                        .addComponent(jLabel8))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(36, 36, 36)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(44, 44, 44)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnThem)
                    .addComponent(btnThoat))
                .addGap(54, 54, 54))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnThoatMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnThoatMouseClicked
       matHang = null;
       MatHangDialog.this.dispose();
    }//GEN-LAST:event_btnThoatMouseClicked


   

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnThem;
    private javax.swing.JButton btnThoat;
    private javax.swing.JComboBox<String> comTinhTrang;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField txtDonGia;
    private javax.swing.JTextArea txtGhiChu;
    private javax.swing.JTextField txtHangSanXuat;
    private javax.swing.JTextField txtLoaiMathang;
    private javax.swing.JTextField txtMaMatHang;
    private javax.swing.JTextField txtSoLuongTon;
    private javax.swing.JTextField txtTenMatHang;
    // End of variables declaration//GEN-END:variables
}
