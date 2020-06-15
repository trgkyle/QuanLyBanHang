/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package VTabbed;

import Model.DanhSachChoThue;
import Model.DanhSachMatHang;
import Model.HoaDon;
import VTableModel.MuaBanTableModel;
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

/**
 *
 * @author quang
 */
public class ThongKeTab extends javax.swing.JPanel {

    /**
     * Creates new form ThongKeTab
     */
    
    private static DanhSachChoThue danhSachChoThue;
    private static DanhSachMatHang danhSachMatHang;
    private static LocalDate currentDate = LocalDate.now();
    private MuaBanTableModel choThueTableModel;
    
    /**
     * Tạo GUI
     */
    private void prepareUI() throws Exception {
        
//        btnDangXuat.addActionListener(btnDangXuat_Click());
        
        cbThang.addActionListener(cbThang_Selected());
        cbNam.addActionListener(cbNam_Selected());
        
        
        choThueTableModel = new MuaBanTableModel(danhSachChoThue.getAll());

        tblChoThue.setModel(choThueTableModel);
        // generate dữ liệu năm (5 năm gần đây)
        cbNam.addItem("Tất cả");
        for (int i = 0; i < 5; i++)
            cbNam.addItem(String.valueOf(currentDate.getYear() - i));

        refresh();
    
    }
    
    public void refresh() throws Exception{
        final Pattern pattern = Pattern.compile("^Tháng (\\d.*)");
        try {
            
             // load data mới
            danhSachMatHang.loadData();
            danhSachChoThue.loadData();

            // Card doanh thu
            int nam = String.valueOf(cbNam.getSelectedItem()).equalsIgnoreCase("Tất cả") ? 0 :
                    Integer.parseInt(String.valueOf(cbNam.getSelectedItem()));
            int thang = 0;
            final Matcher matcher = pattern.matcher(String.valueOf(cbThang.getSelectedItem()));
            if (matcher.find())
                thang = Integer.valueOf(matcher.group(1));

            // hiển thị doanh thu theo định dạng tiền việt nam
            Locale locale_vn = new Locale("vi", "VN");
            NumberFormat numberFormat = NumberFormat.getCurrencyInstance(locale_vn);
            txtDoanhThu.setText(numberFormat.format(danhSachChoThue.tongDoanhThu(thang, nam)));


            
            
            
            
             // tình trạng kho
            txtTongHang.setText(String.valueOf(danhSachMatHang.tongSoMatHangTon() +
                    danhSachChoThue.soLuongMatHangDaThue()));

            txtTongThue.setText(String.valueOf(danhSachChoThue.soLuongMatHangDaThue()));

            txtTongHong.setText(String.valueOf(danhSachMatHang.tongSoMatHangHong()));
            
            
            
            
        // cập nhật bảng thuê quá hạn
            ArrayList<HoaDon> hoaDons = new ArrayList<>();
            for (HoaDon hoaDon : danhSachChoThue.getAll())
                if (!hoaDon.isTinhTrang())
                    hoaDons.add(hoaDon);

            choThueTableModel.setModel(hoaDons);
            tblChoThue.setModel(choThueTableModel);

            tblChoThue.revalidate();
            tblChoThue.repaint();


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
                 * nếu như người dùng muốn xem tổng doanh thu thì xoá tất cả item tháng
                 * item tháng sẽ phụ thuộc theo năm (nếu chọn năm hiện tại thì tháng không vươt quá tháng hiện tại)
                 */
                if (String.valueOf(cbNam.getSelectedItem()).equalsIgnoreCase("Tất cả")) {
                    cbThang.removeAllItems();
                    cbThang.addItem("Tất cả");
                } else {
                    // generate dữ liệu tháng theo năm
                    cbThang.removeAllItems();
                    int nam = Integer.parseInt(String.valueOf(cbNam.getSelectedItem()));
                    for (int i = 0; i <= (nam == currentDate.getYear() ? currentDate.getMonthValue() : 12); i++)
                        cbThang.addItem(i == 0 ? "Tất cả" : String.format("Tháng %d", i));
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
    private ActionListener btnDangXuat_Click() {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                

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
        };
    }
    
    
    
    public ThongKeTab() throws Exception {
        initComponents();
        
        try {
            danhSachChoThue = new DanhSachChoThue();
            danhSachMatHang = new DanhSachMatHang();
        } catch (Exception e) {
        }

        prepareUI();
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
        tblChoThue = new javax.swing.JTable();
        cbThang = new javax.swing.JComboBox<>();
        cbNam = new javax.swing.JComboBox<>();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        txtTongHang = new javax.swing.JTextField();
        txtTongThue = new javax.swing.JTextField();
        txtTongHong = new javax.swing.JTextField();
        txtDoanhThu = new javax.swing.JTextField();

        tblChoThue.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane1.setViewportView(tblChoThue);

        cbThang.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel1.setText("Tổng Số Hàng");

        jLabel2.setText("Tổng Số Hàng Đã Mua");

        jLabel3.setText("Tổng Loại Hàng Đã Hỏng");

        jLabel4.setText("Tổng Doanh Thu");

        txtTongHang.setEditable(false);

        txtTongThue.setEditable(false);

        txtTongHong.setEditable(false);

        txtDoanhThu.setEditable(false);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(66, 66, 66)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane1)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel4)
                            .addComponent(jLabel3)
                            .addComponent(jLabel2)
                            .addComponent(jLabel1))
                        .addGap(40, 40, 40)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(txtTongHang)
                                .addComponent(txtTongThue)
                                .addComponent(txtDoanhThu, javax.swing.GroupLayout.DEFAULT_SIZE, 235, Short.MAX_VALUE))
                            .addComponent(txtTongHong, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 235, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 310, Short.MAX_VALUE)
                        .addComponent(cbThang, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(cbNam, javax.swing.GroupLayout.PREFERRED_SIZE, 166, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(73, 73, 73))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtTongHang, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(cbThang, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(cbNam, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel1))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(13, 13, 13)
                        .addComponent(txtTongThue, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(jLabel2)))
                .addGap(12, 12, 12)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(txtTongHong, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(17, 17, 17)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(txtDoanhThu, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 136, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 285, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(87, 87, 87))
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<String> cbNam;
    private javax.swing.JComboBox<String> cbThang;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tblChoThue;
    private javax.swing.JTextField txtDoanhThu;
    private javax.swing.JTextField txtTongHang;
    private javax.swing.JTextField txtTongHong;
    private javax.swing.JTextField txtTongThue;
    // End of variables declaration//GEN-END:variables
}
