/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package VTabbed;


import Model.DanhSachMatHang;
import Model.DanhSachMuaBan;
import Model.HoaDon;
import VTableModel.MuaBanTableModel;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JOptionPane;
import javax.swing.table.JTableHeader;

/**
 *
 * @author quang
 */
public class ThongKeTab extends javax.swing.JPanel {

    /**
     * Creates new form ThongKeTab
     */
    private static DanhSachMuaBan danhSachMuaBan;
    private static DanhSachMatHang danhSachMatHang;
    private static LocalDate currentDate = LocalDate.now();
    private MuaBanTableModel muaBanTableModel;

    /**
     * Tạo GUI
     */
    private void prepareUI() throws Exception {

//        btnDangXuat.addActionListener(btnDangXuat_Click());
        cbThang.addActionListener(cbThang_Selected());
        cbNam.addActionListener(cbNam_Selected());

        muaBanTableModel = new MuaBanTableModel(danhSachMuaBan.getAll());

        tblHoaDon.setModel(muaBanTableModel);
        // generate dữ liệu năm (5 năm gần đây)
        cbNam.addItem("Tất cả");
        for (int i = 0; i < 5; i++) {
            cbNam.addItem(String.valueOf(currentDate.getYear() - i));
        }

        JTableHeader jtableHeader = tblHoaDon.getTableHeader();
        Font headerFont = new Font("Verdana", Font.PLAIN, 20);
        jtableHeader.setFont(headerFont);
        refresh();

    }

    public void refresh() throws Exception {
        final Pattern pattern = Pattern.compile("^Tháng (\\d.*)");
        try {

            // load data mới
            danhSachMatHang.loadData();
            danhSachMuaBan.loadData();

            // Card doanh thu
            int nam = String.valueOf(cbNam.getSelectedItem()).equalsIgnoreCase("Tất cả") ? 0
                    : Integer.parseInt(String.valueOf(cbNam.getSelectedItem()));
            int thang = 0;
            final Matcher matcher = pattern.matcher(String.valueOf(cbThang.getSelectedItem()));
            if (matcher.find()) {
                thang = Integer.valueOf(matcher.group(1));
            }

            // hiển thị doanh thu theo định dạng tiền việt nam
            Locale locale_vn = new Locale("vi", "VN");
            NumberFormat numberFormat = NumberFormat.getCurrencyInstance(locale_vn);
            txtDoanhThu.setText(numberFormat.format(danhSachMuaBan.tongDoanhThu(thang, nam)));

            // tình trạng kho
            txtTongHang.setText(String.valueOf(danhSachMatHang.tongSoMatHangTon()
                    + danhSachMuaBan.soLuongMatHangMua()));

            txtTongThue.setText(String.valueOf(danhSachMuaBan.soLuongMatHangMua()));

            txtTongHong.setText(String.valueOf(danhSachMatHang.tongSoMatHangHong()));

            // cập nhật bảng mua bán thanh toán
            ArrayList<HoaDon> hoaDons = new ArrayList<>();
            for (HoaDon hoaDon : danhSachMuaBan.getAll()) {
                if (hoaDon.isTinhTrang() == 0) {
                    hoaDons.add(hoaDon);
                }
            }

            muaBanTableModel.setModel(hoaDons);
            tblHoaDon.setModel(muaBanTableModel);

            tblHoaDon.revalidate();
            tblHoaDon.repaint();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * Sự kiện khi chọn item combo box Năm
     *
     * @return
     */
    private ActionListener cbNam_Selected() {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                /**
                 * nếu như người dùng muốn xem tổng doanh thu thì xoá tất cả
                 * item tháng item tháng sẽ phụ thuộc theo năm (nếu chọn năm
                 * hiện tại thì tháng không vươt quá tháng hiện tại)
                 */
                if (String.valueOf(cbNam.getSelectedItem()).equalsIgnoreCase("Tất cả")) {
                    cbThang.removeAllItems();
                    cbThang.addItem("Tất cả");
                } else {
                    // generate dữ liệu tháng theo năm
                    cbThang.removeAllItems();
                    int nam = Integer.parseInt(String.valueOf(cbNam.getSelectedItem()));
                    for (int i = 0; i <= (nam == currentDate.getYear() ? currentDate.getMonthValue() : 12); i++) {
                        cbThang.addItem(i == 0 ? "Tất cả" : String.format("Tháng %d", i));
                    }
                }

                try {
                    refresh();
                } catch (Exception ex) {
                    Logger.getLogger(ThongKeTab.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        };
    }

    /**
     * Sự kiện khi chọn item combo box tháng
     *
     * @return
     */
    private ActionListener cbThang_Selected() {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    refresh();
                } catch (Exception ex) {
                    Logger.getLogger(ThongKeTab.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        };
    }

    /**
     * Sự kiện button đăng xuất
     *
     * @return
     */
    private void btnDangXuat_Click() {

        // dialog xác nhận xoá
        int reply = JOptionPane.showConfirmDialog(null,
                " Bạn có muốn đăng xuất không", "Đăng xuất",
                JOptionPane.YES_NO_OPTION);

        // nếu người dùng đồng ý
        if (reply == JOptionPane.YES_OPTION) {

            try {

                System.exit(0);
            } catch (Exception ex) {
                Logger.getLogger(QuanLyNhanVienTab.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    public ThongKeTab() throws Exception {
        initComponents();

        try {
            danhSachMuaBan = new DanhSachMuaBan();
            danhSachMatHang = new DanhSachMatHang();
        } catch (Exception e) {
        }

        prepareUI();
        setSize(1400, 750);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        tblHoaDon = new javax.swing.JTable();
        jPanel1 = new javax.swing.JPanel();
        txtTongHong = new javax.swing.JTextField();
        txtDoanhThu = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        txtTongHang = new javax.swing.JTextField();
        txtTongThue = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        cbThang = new javax.swing.JComboBox<>();
        cbNam = new javax.swing.JComboBox<>();
        dangXuat = new javax.swing.JButton();
        jLabel7 = new javax.swing.JLabel();

        tblHoaDon.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        tblHoaDon.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        tblHoaDon.setRowHeight(22);
        jScrollPane1.setViewportView(tblHoaDon);

        txtTongHong.setEditable(false);
        txtTongHong.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N

        txtDoanhThu.setEditable(false);
        txtDoanhThu.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        txtDoanhThu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtDoanhThuActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel1.setText("Tổng Số Hàng");

        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel2.setText("Tổng Số Hàng Đã Mua");

        jLabel3.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel3.setText("Tổng Loại Hàng Đã Hỏng");

        jLabel4.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel4.setText("Tổng Doanh Thu");

        txtTongHang.setEditable(false);
        txtTongHang.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N

        txtTongThue.setEditable(false);
        txtTongThue.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        txtTongThue.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTongThueActionPerformed(evt);
            }
        });

        jLabel5.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel5.setText("Thống kê theo tháng");

        jLabel6.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel6.setText("Thống kê theo năm");

        cbThang.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        cbThang.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        cbNam.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N

        dangXuat.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        dangXuat.setText("Đăng xuất");
        dangXuat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                dangXuatActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 195, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 156, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(27, 27, 27)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtTongThue, javax.swing.GroupLayout.DEFAULT_SIZE, 144, Short.MAX_VALUE)
                            .addComponent(txtDoanhThu)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 211, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(11, 11, 11)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtTongHang, javax.swing.GroupLayout.DEFAULT_SIZE, 144, Short.MAX_VALUE)
                            .addComponent(txtTongHong))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 265, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(dangXuat, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 166, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(20, 20, 20)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(cbNam, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(cbThang, javax.swing.GroupLayout.PREFERRED_SIZE, 234, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(txtTongHang, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel1))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel3)
                            .addComponent(txtTongHong, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(28, 28, 28)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2)
                            .addComponent(txtTongThue, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(26, 26, 26)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel4)
                            .addComponent(txtDoanhThu, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(dangXuat)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel5)
                            .addComponent(cbThang, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(26, 26, 26)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel6)
                            .addComponent(cbNam, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabel7.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel7.setText("Mặt hàng chờ thanh toán");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(66, 66, 66)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel7)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 1072, Short.MAX_VALUE)
                            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(73, 73, 73))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(60, 60, 60)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(38, 38, 38)
                .addComponent(jLabel7)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 285, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(60, 60, 60))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void txtDoanhThuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtDoanhThuActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtDoanhThuActionPerformed

    private void dangXuatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_dangXuatActionPerformed
        // TODO add your handling code here:
        this.btnDangXuat_Click();
    }//GEN-LAST:event_dangXuatActionPerformed

    private void txtTongThueActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTongThueActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTongThueActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<String> cbNam;
    private javax.swing.JComboBox<String> cbThang;
    private javax.swing.JButton dangXuat;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tblHoaDon;
    private javax.swing.JTextField txtDoanhThu;
    private javax.swing.JTextField txtTongHang;
    private javax.swing.JTextField txtTongHong;
    private javax.swing.JTextField txtTongThue;
    // End of variables declaration//GEN-END:variables
}
