/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package VDialog;

import DAO.HoaDonDAO;
import Model.DanhSachKhachHang;
import Model.DanhSachMatHang;
import Model.HoaDon;
import Model.KhachHang;
import Model.MatHang;
import Utils.Formats;
import Utils.PatternRegexs;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Date;
import java.text.DecimalFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JFrame;
import javax.swing.JRootPane;
import javax.swing.SwingUtilities;
import static javax.swing.WindowConstants.DISPOSE_ON_CLOSE;

/**
 *
 * @author s2hdp
 */
public class HoaDonBanDialog extends javax.swing.JDialog {

    private HoaDon hoaDon;

    private boolean isChinhSua;
    private DanhSachKhachHang danhSachKhachHang;
    private DanhSachMatHang danhSachMatHang;
    private HoaDonDAO hoaDonDAO;

    /**
     * Creates new form HoaDonBanDialog
     */
    public HoaDonBanDialog(JFrame frame, HoaDon hoaDon) throws Exception {
        super(frame, true);
        initComponents();

        this.hoaDon = hoaDon;

        // Tạo kết nối đến db
        try {
            hoaDonDAO = HoaDonDAO.getInstance();
            danhSachKhachHang = new DanhSachKhachHang();
            danhSachMatHang = new DanhSachMatHang();
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }

        if (hoaDon == null) {

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

    private void prepareDialog() {

        // set the txtMaHoaDon
        txtMaHoaDon.setText(getMaHoaDonMoi());
        txtMaHoaDon.setEditable(false);
        if (isChinhSua) {
            txtMaHoaDon.setText(hoaDon.getMaHoaDon());
        }

        // set the comboBox khachHang
        for (KhachHang khachHang : danhSachKhachHang.getAll()) {
            cbMaKhachHang.addItem(String.format("[%s] %s", khachHang.getMaKH(), khachHang.getHoTen()));
        }
        if (isChinhSua) {
            cbMaKhachHang.setSelectedItem(String.format("[%s] %s",
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
            cbMaMatHang.addItem(String.format("[%s] %s",
                    hoaDon.getMatHang().getMaMatHang(), hoaDon.getMatHang().getTenMatHang()));

            cbMaMatHang.setSelectedItem(String.format("[%s] %s",
                    hoaDon.getMatHang().getMaMatHang(), hoaDon.getMatHang().getTenMatHang()));
        }

        if (isChinhSua) {
            dateChooser.setDate(hoaDon.getNgayLap());
        } else {
            dateChooser.setDate(new java.util.Date());
        }

        //set the soLuong
        if (isChinhSua) {
            txtSoLuong.setText(String.valueOf(hoaDon.getSoLuong()));
        }

        //set the button Thoat
        btnThoat.addActionListener(btnThoat_Click());

        //set the button Luu
        btnLuu.addActionListener(btnLuu_Click());

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
            return "HDB00001";
        }

        // generate mã
        Pattern pattern = Pattern.compile(PatternRegexs.REGEX_MAHOADON);
        Matcher matcher = pattern.matcher(lastID);
        if (matcher.find()) {
            int number = Integer.parseInt(matcher.group(2));
            number++;

            newID = String.format("HDB%05d", number);
        }

        return newID;
    }

    /**
     * Sự kiện nút Thoát
     *
     * @return
     */
    private ActionListener btnThoat_Click() {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                hoaDon = null;
                HoaDonBanDialog.this.dispose();
            }
        };
    }

    /**
     * Sự kiện nút Lưu
     *
     * @return
     */
    private ActionListener btnLuu_Click() {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Kiểm tra dữ liệu nhập

                MatHang matHang = null;
                KhachHang khachHang = null;
                Pattern pattern = null;
                Matcher matcher = null;

                // lấy dữ liệu mặt hàng
                pattern = Pattern.compile("(MH\\d.*)]", Pattern.MULTILINE);
                matcher = pattern.matcher(String.valueOf(cbMaMatHang.getSelectedItem()));

                if (matcher.find()) {
                    matHang = danhSachMatHang.getAll().get(danhSachMatHang.tim(matcher.group(1)));
                }

                // lấy dữ liệu khách hàng
                pattern = Pattern.compile("(KH\\d.*)]", Pattern.MULTILINE);
                matcher = pattern.matcher(cbMaKhachHang.getSelectedItem().toString());

                if (matcher.find()) {
                    khachHang = danhSachKhachHang.getAll().get(danhSachKhachHang.tim(matcher.group(1)));
                }

                // tạo thông tin hoá đơn
                hoaDon = new HoaDon(
                        matHang,
                        Integer.parseInt(txtSoLuong.getText().trim()),
                        txtMaHoaDon.getText().trim(),
                        khachHang,
                        Date.valueOf(Formats.DATE_FORMAT_SQL.format(dateChooser.getDate()))
                );

                System.out.println(hoaDon);

                // đóng dialog
                dispose();
            }
        };
    }

    /**
     * Trả về hoá đơn đã được thêm/chỉnh sửa
     *
     * @return
     */
    public HoaDon getHoaDon() {
        return hoaDon;
    }
    // End of variables declaration                   

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
        cbMaKhachHang = new javax.swing.JComboBox<>();
        cbMaMatHang = new javax.swing.JComboBox<>();
        txtSoLuong = new javax.swing.JTextField();
        btnLuu = new javax.swing.JButton();
        jLabel7 = new javax.swing.JLabel();
        inputThanhTien = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jPanel1.setToolTipText("Hóa đơn");
        jPanel1.setName("Hi"); // NOI18N

        jLabel1.setText("Mã Hoá Đơn");

        btnThoat.setText("Thoát");

        jLabel2.setText("Mã Khách Hàng");

        jLabel3.setText("Mã Mặt Hàng");

        jLabel4.setText("Ngày Mua");

        jLabel5.setText("Số Lượng Mua");

        txtSoLuong.addInputMethodListener(new java.awt.event.InputMethodListener() {
            public void caretPositionChanged(java.awt.event.InputMethodEvent evt) {
            }
            public void inputMethodTextChanged(java.awt.event.InputMethodEvent evt) {
                txtSoLuongInputMethodTextChanged(evt);
            }
        });
        txtSoLuong.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtSoLuongActionPerformed(evt);
            }
        });
        txtSoLuong.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtSoLuongKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtSoLuongKeyReleased(evt);
            }
        });

        btnLuu.setText("Tạo hóa đơn");

        jLabel7.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel7.setText("Thành tiền");

        inputThanhTien.setEditable(false);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(btnLuu, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnThoat, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2)
                    .addComponent(jLabel1)
                    .addComponent(jLabel3)
                    .addComponent(jLabel4)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(jLabel7, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel5, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addGap(52, 52, 52)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(cbMaMatHang, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(cbMaKhachHang, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(dateChooser, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txtSoLuong)
                    .addComponent(inputThanhTien)
                    .addComponent(txtMaHoaDon)))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(txtMaHoaDon, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(cbMaKhachHang, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(24, 24, 24)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel3)
                    .addComponent(cbMaMatHang, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(dateChooser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(txtSoLuong, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(inputThanhTien, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(22, 22, 22)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnLuu)
                    .addComponent(btnThoat))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel6.setText("HÓA ĐƠN BÁN HÀNG");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(249, 249, 249)
                .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(228, 228, 228))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(175, 175, 175)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(153, 153, 153))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(34, 34, 34)
                .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtSoLuongActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtSoLuongActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtSoLuongActionPerformed

    private void txtSoLuongKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSoLuongKeyPressed
        
    }//GEN-LAST:event_txtSoLuongKeyPressed

    private void txtSoLuongInputMethodTextChanged(java.awt.event.InputMethodEvent evt) {//GEN-FIRST:event_txtSoLuongInputMethodTextChanged

    }//GEN-LAST:event_txtSoLuongInputMethodTextChanged

    private void txtSoLuongKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSoLuongKeyReleased
        // TODO add your handling code here:
        // TODO add your handling code here:
        MatHang matHang = null;
        Pattern pattern = null;
        Matcher matcher = null;

        // lấy dữ liệu mặt hàng
        pattern = Pattern.compile("(MH\\d.*)]", Pattern.MULTILINE);
        matcher = pattern.matcher(String.valueOf(cbMaMatHang.getSelectedItem()));
        if (matcher.find()) {
            matHang = danhSachMatHang.getAll().get(danhSachMatHang.tim(matcher.group(1)));
            System.out.println(matHang.getDonGia());
            DecimalFormat dcf = new DecimalFormat("###,###,###,###.##");
            this.inputThanhTien.setText(dcf.format(Double.parseDouble(this.txtSoLuong.getText()) * matHang.getDonGia()));
        }
    }//GEN-LAST:event_txtSoLuongKeyReleased


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnLuu;
    private javax.swing.JButton btnThoat;
    private javax.swing.JComboBox<String> cbMaKhachHang;
    private javax.swing.JComboBox<String> cbMaMatHang;
    private com.toedter.calendar.JDateChooser dateChooser;
    private javax.swing.JTextField inputThanhTien;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JTextField txtMaHoaDon;
    private javax.swing.JTextField txtSoLuong;
    // End of variables declaration//GEN-END:variables
}
