/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package VDialog;

import DAO.HoaDonDAO;
import Model.DanhSachKhachHang;
import Model.DanhSachMatHang;
import Model.DanhSachNhaCungCap;
import Model.HoaDon;
import Model.KhachHang;
import Model.MatHang;
import Model.MatHangHoaDon;
import Model.NhaCungCap;
import Utils.Formats;
import Utils.PatternRegexs;
import com.toedter.calendar.JDateChooser;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JFrame;
import javax.swing.JRootPane;
import javax.swing.SwingUtilities;

/**
 *
 * @author s2hdp
 */
public class HoaDonMuaDialog extends javax.swing.JDialog {

    /**
     * Creates new form ChoThueDialog
     */
    private HoaDon hoaDon;

    private boolean isChinhSua;
    private DanhSachNhaCungCap danhSachNhaCungCap;
    private DanhSachMatHang danhSachMatHang;
    private HoaDonDAO hoaDonDAO;

    private void prepareDialog() {
        // set the txtMaHoaDon
        txtMaHoaDon.setText(getMaHoaDonMoi());
        txtMaHoaDon.setEditable(false);
        if (isChinhSua) {
            txtMaHoaDon.setText(hoaDon.getMaHoaDon());
        }
        // set the comboBox nha cung cap
        for (NhaCungCap nhacc : danhSachNhaCungCap.getAll()) {

            cbMaNhaCungCap.addItem(String.format("[%s] %s", nhacc.getMaNCC(), nhacc.getTenNCC()));
        }

        if (isChinhSua) {
            cbMaNhaCungCap.setSelectedItem(String.format("[%s] %s",
                    hoaDon.getKhachHang().getMaKH(),
                    hoaDon.getKhachHang().getHoTen()));
        }

        // set the comboBox matHang
        for (MatHang matHang : danhSachMatHang.getAll()) {
            if (matHang.getSoLuongTon() > 0) {
                cbMaMatHang.addItem(String.format("[%s] %s", matHang.getMaMatHang(), matHang.getTenMatHang()));
            }
        }

        if (isChinhSua) {
//            cbMaMatHang.addItem(String.format("[%s] %s",
//                    hoaDon.getMatHang().getMaMatHang(), hoaDon.getMatHang().getTenMatHang()));
//
//            cbMaMatHang.setSelectedItem(String.format("[%s] %s",
//                    hoaDon.getMatHang().getMaMatHang(), hoaDon.getMatHang().getTenMatHang()));
        }

        if (isChinhSua) {
            dateChooser.setDate(hoaDon.getNgayLap());
        } else {
            dateChooser.setDate(new java.util.Date());
        }

        //set the soLuong
        if (isChinhSua) {
//            txtSoLuong.setText(String.valueOf(hoaDon.getSoLuong()));
        }

    }

    /**
     * Generate mã hoá đơn mới
     *
     * @return
     */
    private String getMaHoaDonMoi() {
        String lastID = "";
        String newID = "";

        // lấy mã hoá đơn cuối trong DB
        try {
            lastID = hoaDonDAO.getMaHoaDonCuoi();
        } catch (Exception e) {
        }

        // Nếu chưa có hoá đơn nào trong DB thì trả về mã mặc định
        if (lastID.isEmpty()) {
            return "HDM00001";
        }

        // generate mã
        Pattern pattern = Pattern.compile(PatternRegexs.REGEX_MAHOADON);
        Matcher matcher = pattern.matcher(lastID);
        if (matcher.find()) {
            int number = Integer.parseInt(matcher.group(2));
            number++;

            newID = String.format("HDM%05d", number);
        }

        return newID;
    }

    /**
     * Sự kiện nút Thoát
     *
     * @return
     */
    private void btnThoat_Click() {
        hoaDon = null;
        HoaDonMuaDialog.this.dispose();
    }

    /**
     * Sự kiện nút Lưu
     *
     * @return
     */
    private void btnLuu_Click() {
        // Kiểm tra dữ liệu nhập

        MatHang matHang = null;
        NhaCungCap nhacungcap = null;
        KhachHang khachHang = null;
        Pattern pattern = null;
        Matcher matcher = null;

        // lấy dữ liệu mh
        pattern = Pattern.compile("(MH\\d.*)]", Pattern.MULTILINE);
        matcher = pattern.matcher(String.valueOf(cbMaMatHang.getSelectedItem()));

        if (matcher.find()) {
            matHang = danhSachMatHang.getAll().get(danhSachMatHang.tim(matcher.group(1)));
        }
        System.out.println(matHang.toString());
        // lấy dữ liệu khách hàng
        pattern = Pattern.compile("(KH\\d.*)]", Pattern.MULTILINE);
        matcher = pattern.matcher(cbMaNhaCungCap.getSelectedItem().toString());

//                if (matcher.find()) { ???
//                    khachHang = danhSachKhachHang.getAll().get(danhSachKhachHang.tim(matcher.group(1)));
//                } ????
        // tạo thông tin hoá đơn sau khi đóng tab sẽ móc dữ liệu
        hoaDon = new HoaDon(
                null,
                txtMaHoaDon.getText().trim(),
                khachHang,
                Date.valueOf(Formats.DATE_FORMAT_SQL.format(dateChooser.getDate()))
        );

        System.out.println(hoaDon);

        // đóng dialog
        dispose();
    }

    /**
     * Trả về hoá đơn đã được thêm/chỉnh sửa
     *
     * @return
     */
    public HoaDon getHoaDon() {
        return hoaDon;
    }

    public HoaDonMuaDialog(JFrame frame, HoaDon hoaDon) throws Exception {
        super(frame, true);
        initComponents();

        this.hoaDon = hoaDon;

        // Tạo kết nối đến db
        try {
            hoaDonDAO = HoaDonDAO.getInstance();
            danhSachMatHang = new DanhSachMatHang();
            danhSachNhaCungCap = new DanhSachNhaCungCap();
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }

        if (hoaDon == null) {
            System.out.println("Hoa don null");
            isChinhSua = false;
        } else {

            isChinhSua = true;
        }

        // Tạo GUI
        prepareDialog();
        // Button mặc định khi bấm Enter
        JRootPane rootPane = SwingUtilities.getRootPane(this);
        rootPane.setDefaultButton(btnLuu);

        // cấu hình cho dialog
        setResizable(false);
        setSize(600, 585);
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

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        btnThoat = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        txtMaHoaDon = new javax.swing.JTextField();
        dateChooser = new com.toedter.calendar.JDateChooser();
        cbMaNhaCungCap = new javax.swing.JComboBox<>();
        cbMaMatHang = new javax.swing.JComboBox<>();
        txtSoLuong = new javax.swing.JTextField();
        btnLuu = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Hóa đơn mua hàng");

        jPanel1.setToolTipText("Hóa đơn");
        jPanel1.setName("Hi"); // NOI18N

        jLabel1.setText("Mã Hoá Đơn");

        btnThoat.setText("Thoát");
        btnThoat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnThoatActionPerformed(evt);
            }
        });

        jLabel2.setText("Mã nhà cung cấp");

        jLabel3.setText("Mã Mặt Hàng");

        jLabel4.setText("Ngày Mua");

        jLabel5.setText("Số Lượng Mua");

        btnLuu.setText("Tạo hóa đơn");
        btnLuu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLuuActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(btnLuu, javax.swing.GroupLayout.PREFERRED_SIZE, 206, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnThoat, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addComponent(jLabel1)
                            .addComponent(jLabel3)
                            .addComponent(jLabel4)
                            .addComponent(jLabel5))
                        .addGap(52, 52, 52)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtMaHoaDon)
                            .addComponent(cbMaMatHang, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(cbMaNhaCungCap, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(dateChooser, javax.swing.GroupLayout.DEFAULT_SIZE, 174, Short.MAX_VALUE)
                            .addComponent(txtSoLuong)))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(txtMaHoaDon, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2)
                    .addComponent(cbMaNhaCungCap, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cbMaMatHang, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(dateChooser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4))
                .addGap(27, 27, 27)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(txtSoLuong, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(28, 28, 28)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnLuu)
                    .addComponent(btnThoat))
                .addContainerGap(87, Short.MAX_VALUE))
        );

        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel6.setText("HÓA ĐƠN MUA HÀNG");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(249, 249, 249)
                .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(228, 228, 228))
            .addGroup(layout.createSequentialGroup()
                .addGap(156, 156, 156)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(156, 156, 156))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(34, 34, 34)
                .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(48, 48, 48))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnLuuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLuuActionPerformed
        // TODO add your handling code here:
        this.btnLuu_Click();
    }//GEN-LAST:event_btnLuuActionPerformed

    private void btnThoatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnThoatActionPerformed
        // TODO add your handling code here:
        this.btnThoat_Click();
    }//GEN-LAST:event_btnThoatActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnLuu;
    private javax.swing.JButton btnThoat;
    private javax.swing.JComboBox<String> cbMaMatHang;
    private javax.swing.JComboBox<String> cbMaNhaCungCap;
    private com.toedter.calendar.JDateChooser dateChooser;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JTextField txtMaHoaDon;
    private javax.swing.JTextField txtSoLuong;
    // End of variables declaration//GEN-END:variables
}
