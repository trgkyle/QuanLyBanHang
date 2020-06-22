/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package VTabbed;


import Model.DanhSachKhachHang;
import Model.DanhSachMatHang;
import Model.DanhSachMuaBan;
import Model.HoaDon;
import Model.MatHangHoaDon;
import VDialog.HoaDonBanDialog;


import VTableModel.MuaBanTableModel;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.RowFilter;
import javax.swing.SwingConstants;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

/**
 *
 * @author quang
 */
public class QuanLyMuaBan extends javax.swing.JPanel {

    /**
     * Creates new form DanhSachMuaBan
     */
    private DanhSachMatHang danhSachMatHang;
    private DanhSachKhachHang danhSachKhachHang;
    private DanhSachMuaBan danhSachMuaBan;
    private final Component rootComponent = this;

    private int indexFilter = 0;

    private TableRowSorter<TableModel> sorter;
    private MuaBanTableModel muaBanTableModel;

    /**
     * Tạo GUI
     */
    private void prepareUI() {

        cbFilter.addActionListener(cbFilter_Selected());

        txtTimKiem.getDocument().addDocumentListener(txtTimKiem_DocumentListener());

        cbFilterTimKiem.addActionListener(cbFilterTimKiem_Changed());

        muaBanTableModel = new MuaBanTableModel(danhSachMuaBan.getAll());

        sorter = new TableRowSorter<>(muaBanTableModel);

        tblHoaDon.setModel(muaBanTableModel);
        tblHoaDon.setRowSorter(sorter);

        JTableHeader jtableHeader = tblHoaDon.getTableHeader();
        Font headerFont = new Font("Verdana", Font.PLAIN, 20);
        jtableHeader.setFont(headerFont);
        
        
        refresh(true);
    }

    /**
     * Kiểm tra tình trạng trước khi thêm/sửa
     *
     * @param hoaDon
     * @param soLuongCu
     * @return
     */
    private boolean kiemTraTinhTrang(HoaDon hoaDon, int soLuongCu) {
        if (soLuongCu == -1) {
            return true;
        }
        // kiểm tra số lượng đặt có đủ không
        int soLuongTon = soLuongCu;
        for (MatHangHoaDon matHangHoaDon : hoaDon.getMatHang()) {
            soLuongTon = matHangHoaDon.getSoLuongTon();
            // kiểm tra mặt hàng
            if (matHangHoaDon.isTinhTrang()) {
                JOptionPane.showMessageDialog(rootComponent, "Mặt Hàng không còn sử dụng được");
                return false;
            }
        }
        if (soLuongTon < hoaDon.tinhTongSoLuong()) {
            JOptionPane.showMessageDialog(rootComponent, "Không đủ số lượng");
            return false;
        }

        return true;
    }

    private void validateActionButtonHoaDon() {
        if (getCurrentSelected() != -1) {
            btnThanhToan.setEnabled(true);
            btnTraHang.setEnabled(true);
            HoaDon hoaDon = danhSachMuaBan.getAll().get(getCurrentSelected());
            System.out.println(hoaDon.isTinhTrang());
            if (hoaDon.isTinhTrang() == -1) {
                btnThanhToan.setEnabled(false);
                btnTraHang.setEnabled(false);
            } else if(hoaDon.isTinhTrang() == 1){
                btnThanhToan.setEnabled(false);
            } else {
                btnThanhToan.setEnabled(true);
                btnTraHang.setEnabled(true);
            }
        }
    }

    /**
     * Sự kiện khi nhập text tìm kiếm Tìm kiếm realtime
     *
     * @return
     */
    private DocumentListener txtTimKiem_DocumentListener() {
        return new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                filterTable(txtTimKiem.getText().trim());
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                filterTable(txtTimKiem.getText().trim());
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                filterTable(txtTimKiem.getText().trim());
            }
        };
    }

    /**
     * Lấy vị trí đang chọn trong table
     *
     * @return
     */
    private int getCurrentSelected() {
        try {
            return tblHoaDon.convertRowIndexToModel(tblHoaDon.getSelectedRow());
        } catch (Exception e) {
            return -1;
        }
    }

    /**
     * Refresh giao diện khi có cập nhật dữ liệu
     */
    public void refresh(boolean reloadData) {
        int oldSelected = getCurrentSelected();

        if (reloadData) {
            // load lại dữ liệu từ DB
            try {
                danhSachMatHang.loadData();
                danhSachKhachHang.loadData();
                danhSachMuaBan.loadData();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(rootComponent, e);
            }

            // load lại table
            muaBanTableModel.setModel(danhSachMuaBan.getAll());
            tblHoaDon.setModel(muaBanTableModel);

            sorter.setModel(muaBanTableModel);

            tblHoaDon.revalidate();
            tblHoaDon.repaint();
            setCurrentSelected(oldSelected);
            
            this.validateActionButtonHoaDon();
        }
        /**
         * Bật tắt nút thêm hoá đơn Khi chưa có người dùng và mặt hàng thì k
         * được thêm hoá đơn
         */
        int rowSelected = -1;
        try {
            rowSelected = tblHoaDon.convertRowIndexToModel(tblHoaDon.getSelectedRow());
        } catch (Exception e) {
        }

        if (rowSelected == -1) {

            btnTraHang.setToolTipText("Vui lòng chọn hoá đơn cần xoá");
            btnTraHang.setEnabled(false);

            btnThanhToan.setToolTipText("Vui lòng chọn hoá đơn cần thanh toán");
            btnThanhToan.setEnabled(false);
        } else {

            btnTraHang.setToolTipText("[Alt + X] Xoá hoá đơn");
            btnTraHang.setEnabled(true);

            if (String.valueOf(tblHoaDon.getModel().getValueAt(rowSelected, 6)).equalsIgnoreCase("Chưa thanh toán")) {
                btnThanhToan.setToolTipText("Thanh toán hoá đơn");
                btnThanhToan.setEnabled(true);
            } else {
                btnThanhToan.setToolTipText("Hoá đơn đã được thanh toán");

            }
        }
        this.validateActionButtonHoaDon();
    }

    /**
     * Set row được chọn
     *
     * @param oldSelected
     */
    private void setCurrentSelected(int oldSelected) {
        if (oldSelected != -1 && oldSelected <= tblHoaDon.getModel().getRowCount()) {
            tblHoaDon.setRowSelectionInterval(oldSelected, oldSelected);
        } else if (oldSelected != -1 && oldSelected > tblHoaDon.getModel().getRowCount()) {
            tblHoaDon.setRowSelectionInterval(oldSelected - 1, oldSelected - 1);
        } else if (oldSelected == -1 && tblHoaDon.getModel().getRowCount() > 0) {
            tblHoaDon.setRowSelectionInterval(0, 0);
        } else {
            tblHoaDon.clearSelection();
        }
    }

    

    /**
     * Sự kiện button thêm hóa đơn bán
     *
     * @return
     */
    private void btnThemHoaDonBan() {
        // hiện dialog thêm hoá đơn
        HoaDonBanDialog hoaDonBanDialog = null;
        try {
            hoaDonBanDialog = new HoaDonBanDialog(new JFrame(), null);
        } catch (Exception ex) {
            System.out.println("Error 1");
            JOptionPane.showMessageDialog(rootComponent, ex);
        }
        // lấy hoá đơn nhập trong dialog
        HoaDon hoaDon = hoaDonBanDialog.getHoaDon();
        // kiểm tra tình trạng mua và thêm vào DB
        try {
            if (hoaDon != null && kiemTraTinhTrang(hoaDon, -1)) {
                danhSachMuaBan.them(hoaDon);
                refresh(true);
            }
        } catch (Exception e1) {
            System.out.println("Error 2");
            JOptionPane.showMessageDialog(rootComponent, e1);
        }
    }
    
    private void thongTinHoaDon(){
        
        int selected = tblHoaDon.getSelectedRow();
        if(selected == -1) return;
        HoaDon detailHoaDon = danhSachMuaBan.getAll().get(selected);
        // hiện dialog thêm hoá đơn
        HoaDonBanDialog hoaDonBanDialog = null;
        try {
            hoaDonBanDialog = new HoaDonBanDialog(new JFrame(), detailHoaDon);
        } catch (Exception ex) {
            System.out.println("Error 1");
            JOptionPane.showMessageDialog(rootComponent, ex);
        }
        // lấy hoá đơn nhập trong dialog
        HoaDon hoaDon = hoaDonBanDialog.getHoaDon();
        // kiểm tra tình trạng mua và thêm vào DB
        try {
            if (hoaDon != null && kiemTraTinhTrang(hoaDon, -1)) {
                danhSachMuaBan.them(hoaDon);
                refresh(true);
            }
        } catch (Exception e1) {
            System.out.println("Error 2");
            JOptionPane.showMessageDialog(rootComponent, e1);
        }
    }
    
    

    /**
     * Sự kiện button trả hàng
     *
     * @return
     */
    private void btnTraHang_Click() {
        // nếu người dùng chưa chọn dòng nào thì thông báo
        if (getCurrentSelected() == -1) {
            JOptionPane.showMessageDialog(rootComponent, "Vui lòng chọn hoá đơn cần trả");
            return;
        }

        // lấy thông tin hoá đơn cần trả
        String maHoaDon = muaBanTableModel.getValueAt(getCurrentSelected(), 0).toString();
        String tenKhachHang = muaBanTableModel.getValueAt(getCurrentSelected(), 1).toString();
        String tenMatHang = muaBanTableModel.getValueAt(getCurrentSelected(), 2).toString();

        // hiện dialog xác nhận
        int reply = JOptionPane.showConfirmDialog(null,
                "Bạn có muốn trả mặt hàng thuộc hoá đơn này không?\nHóa đơn " + maHoaDon + "\nTên khách hàng: " + tenKhachHang + "\nTên mặt hàng: " + tenMatHang,
                "Trả hàng", JOptionPane.YES_NO_OPTION);

        // nếu người dùng đồng ý
        if (reply == JOptionPane.YES_OPTION) {
            try {
                // update trang thai hoa don
                HoaDon hoaDon = danhSachMuaBan.getAll().get(getCurrentSelected());
                danhSachMuaBan.traHang(hoaDon.getMaHoaDon());
                refresh(true);
            } catch (Exception e1) {
                JOptionPane.showMessageDialog(rootComponent, e1);
            }
        }
    }

    /**
     * Sự kiện button thanh toán
     *
     * @return
     */
    private void btnThanhToan_Click() {

        // nếu người dùng chưa chọn dòng nào thì thông báo
        if (getCurrentSelected() == -1) {
            JOptionPane.showMessageDialog(rootComponent, "Vui lòng chọn hoá đơn cần trả");
            return;
        }

        // lấy thông tin hoá đơn cần trả
        String maHoaDon = muaBanTableModel.getValueAt(getCurrentSelected(), 0).toString();
        String tenKhachHang = muaBanTableModel.getValueAt(getCurrentSelected(), 1).toString();
        String tenMatHang = muaBanTableModel.getValueAt(getCurrentSelected(), 2).toString();

        // hiện dialog xác nhận
        int reply = JOptionPane.showConfirmDialog(null,
                "Thanh toán \nHóa đơn " + maHoaDon + "\nTên khách hàng: " + tenKhachHang + "\nTên mặt hàng: " + tenMatHang,
                "Thanh toán", JOptionPane.YES_NO_OPTION);

        // nếu người dùng đồng ý
        if (reply == JOptionPane.YES_OPTION) {
            try {
                // update trang thai hoa don
                HoaDon hoaDon = danhSachMuaBan.getAll().get(getCurrentSelected());
                // return to stock
                hoaDon.setTinhTrang(1);
                danhSachMuaBan.thanhToanHoaDon(hoaDon.getMaHoaDon());
                tblHoaDon.clearSelection();
                refresh(true);
            } catch (Exception e1) {
                JOptionPane.showMessageDialog(rootComponent, e1);
            }
        }
    }

    /**
     * Sự kiện khi chọn tìm kiếm theo gì
     *
     * @return
     */
    private ActionListener cbFilterTimKiem_Changed() {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                switch (cbFilterTimKiem.getSelectedIndex()) {
                    case 0:
                        indexFilter = 0;
                        break;
                    case 1:
                        indexFilter = 1;
                        break;
                    case 2:
                        indexFilter = 2;
                        break;
                }

                filterTable(txtTimKiem.getText().trim());
            }
        };
    }

    /**
     * Sự kiện ComboBox filter table theo Tình trạng hoá đơn
     *
     * @return
     */
    private ActionListener cbFilter_Selected() {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String filter_text = String.valueOf(cbFilter.getSelectedItem());

                if (filter_text.equalsIgnoreCase("Tất cả")) //Tất cả
                {
                    sorter.setRowFilter(null);
                } else {
                    try {
                        RowFilter<Object, Object> filter = new RowFilter<Object, Object>() {
                            @Override
                            public boolean include(Entry<?, ?> entry) {
                                return (entry.getStringValue(5).contains(filter_text));
                            }
                        };
                        sorter.setRowFilter(filter);
                    } catch (NumberFormatException e1) {
                        cbFilter.setSelectedIndex(0);
                    }
                }
            }
        };
    }

    /**
     * Tìm kiếm Sử dụng dối tượng filter table
     *
     * @param filter_text
     */
    private void filterTable(String filter_text) {
        if (filter_text.isEmpty()) {
            sorter.setRowFilter(null);
        } else {
            try {
                RowFilter<Object, Object> filter = new RowFilter<Object, Object>() {
                    @Override
                    public boolean include(Entry<?, ?> entry) {
                        return (entry.getStringValue(indexFilter).contains(filter_text));
                    }
                };
                sorter.setRowFilter(filter);
            } catch (NumberFormatException e) {
                txtTimKiem.selectAll();
            }
        }
    }

    public QuanLyMuaBan() {
        initComponents();

        try {
            danhSachMuaBan = new DanhSachMuaBan();
            danhSachKhachHang = new DanhSachKhachHang();
            danhSachMatHang = new DanhSachMatHang();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(rootComponent, e);
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

        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblHoaDon = new javax.swing.JTable();
        jButton1 = new javax.swing.JButton();
        btnTraHang = new javax.swing.JButton();
        btnThanhToan = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        cbFilter = new javax.swing.JComboBox<>();
        txtTimKiem = new javax.swing.JTextField();
        cbFilterTimKiem = new javax.swing.JComboBox<>();

        tblHoaDon.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        tblHoaDon.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "Mã hóa đơn", "Tên khách hàng", "Số lượng", "Ngày mua", "Thành tiền", "Tình trạng"
            }
        ));
        tblHoaDon.setRowHeight(22);
        tblHoaDon.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblHoaDonMouseClicked(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                tblHoaDonMouseReleased(evt);
            }
        });
        jScrollPane1.setViewportView(tblHoaDon);

        jButton1.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jButton1.setText("Tạo hóa đơn xuất kho");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        btnTraHang.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        btnTraHang.setText("Trả hàng");
        btnTraHang.setEnabled(false);
        btnTraHang.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTraHangActionPerformed(evt);
            }
        });

        btnThanhToan.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        btnThanhToan.setText("Thanh Toán");
        btnThanhToan.setEnabled(false);
        btnThanhToan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnThanhToanActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel1.setText("Tìm kiếm");

        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel2.setText("Tình trạng");

        cbFilter.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        cbFilter.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Tất cả", "Đã thanh toán", "Chưa thanh toán" }));

        txtTimKiem.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N

        cbFilterTimKiem.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        cbFilterTimKiem.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Mã Hóa Đơn", "Tên Khách Hàng", "Tên Mặt Hàng" }));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(btnTraHang, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(btnThanhToan, javax.swing.GroupLayout.PREFERRED_SIZE, 141, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 117, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(32, 32, 32)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(cbFilter, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtTimKiem))
                        .addGap(21, 21, 21)
                        .addComponent(cbFilterTimKiem, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtTimKiem, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1)
                    .addComponent(cbFilterTimKiem, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton1))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel2)
                        .addComponent(cbFilter, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btnTraHang)
                        .addComponent(btnThanhToan)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 17, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 282, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(60, 60, 60)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(60, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btnTraHangActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTraHangActionPerformed
        // TODO add your handling code here:
        this.btnTraHang_Click();
    }//GEN-LAST:event_btnTraHangActionPerformed

    private void btnThanhToanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnThanhToanActionPerformed
        // TODO add your handling code here:
        this.btnThanhToan_Click();
    }//GEN-LAST:event_btnThanhToanActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        this.btnThemHoaDonBan();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void tblHoaDonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblHoaDonMouseClicked
        // TODO add your handling code here:
        this.validateActionButtonHoaDon();

    }//GEN-LAST:event_tblHoaDonMouseClicked

    private void tblHoaDonMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblHoaDonMouseReleased
        // TODO add your handling code here:
        if (evt.getClickCount() == 2) {
            // your valueChanged overridden method 
            this.thongTinHoaDon();
        }
        this.validateActionButtonHoaDon();
    }//GEN-LAST:event_tblHoaDonMouseReleased


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnThanhToan;
    private javax.swing.JButton btnTraHang;
    private javax.swing.JComboBox<String> cbFilter;
    private javax.swing.JComboBox<String> cbFilterTimKiem;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tblHoaDon;
    private javax.swing.JTextField txtTimKiem;
    // End of variables declaration//GEN-END:variables
}
