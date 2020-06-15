/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package VTabbed;

import Model.DanhSachChoThue;
import Model.DanhSachKhachHang;
import Model.DanhSachMatHang;
import Model.HoaDon;
import VDialog.HoaDonBanDialog;
import VDialog.HoaDonMuaDialog;
import VDialog.ThanhToanDialog;
import VTableModel.MuaBanTableModel;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.RowFilter;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

/**
 *
 * @author quang
 */
public class QuanLyMuaBan extends javax.swing.JPanel {

    /**
     * Creates new form QuanLyChoThueTab
     */
    private DanhSachMatHang danhSachMatHang;
    private DanhSachKhachHang danhSachKhachHang;
    private DanhSachChoThue danhSachChoThue;
    private final Component rootComponent = this;

    private int indexFilter = 0;

    private TableRowSorter<TableModel> sorter;
    private MuaBanTableModel choThueTableModel;

    /**
     * Tạo GUI
     */
    private void prepareUI() {
        if (danhSachKhachHang.getAll().size() <= 0) {
            btnThem.setToolTipText("Không tìm thấy khách hàng");
        } else if (danhSachMatHang.getAll().size() <= 0) {
            btnThem.setToolTipText("Không tìm thấy mặt hàng trong kho");
        } else {
            btnThem.setEnabled(true);
            btnThem.setToolTipText("[Alt + T] Thêm hoá đơn mới");
        }

        cbFilter.addActionListener(cbFilter_Selected());

        txtTimKiem.getDocument().addDocumentListener(txtTimKiem_DocumentListener());

        cbFilterTimKiem.addActionListener(cbFilterTimKiem_Changed());

        choThueTableModel = new MuaBanTableModel(danhSachChoThue.getAll());

        sorter = new TableRowSorter<>(choThueTableModel);

        tblChoThue.setModel(choThueTableModel);
        tblChoThue.setRowSorter(sorter);

        refresh(true);

    }

    /**
     * Kiểm tra tình trạng trước khi thêm/sửa
     *
     * @param hoaDon
     * @param soLuongCu
     * @return
     */
    private boolean kiemTraTinhTrangThue(HoaDon hoaDon, int soLuongCu) {
        // kiểm tra số lượng đặt có đủ không
        if ((hoaDon.getMatHang().getSoLuongTon() + soLuongCu) < hoaDon.getSoLuong()) {
            JOptionPane.showMessageDialog(rootComponent, "Không đủ số lượng");
            return false;
        }

        // kiểm tra mặt hàng
        if (!hoaDon.getMatHang().isTinhTrang()) {
            JOptionPane.showMessageDialog(rootComponent, "Mặt Hàng không còn sử dụng được");

            return false;
        }

        return true;
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
            return tblChoThue.convertRowIndexToModel(tblChoThue.getSelectedRow());
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
                danhSachChoThue.loadData();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(rootComponent, e);
            }

            // load lại table
            choThueTableModel.setModel(danhSachChoThue.getAll());
            tblChoThue.setModel(choThueTableModel);

            sorter.setModel(choThueTableModel);

            tblChoThue.revalidate();
            tblChoThue.repaint();
            setCurrentSelected(oldSelected);
        }
        /**
         * Bật tắt nút thêm hoá đơn Khi chưa có người dùng và mặt hàng thì k
         * được thêm hoá đơn
         */
        if (danhSachKhachHang.getAll().size() > 0 && danhSachMatHang.getAll().size() > 0) {
            btnThem.setEnabled(true);
            btnThem.setToolTipText("[Alt + T] Thêm hoá đơn");
        } else if (danhSachKhachHang.getAll().size() <= 0) {
            btnThem.setEnabled(false);
            btnThem.setToolTipText("Vui lòng thêm khách hàng");
        } else if (danhSachMatHang.getAll().size() <= 0) {
            btnThem.setEnabled(false);
            btnThem.setToolTipText("Vui lòng thêm mặt hàng");
        }

        /**
         * Bật tắt nút xoá, sửa, thanh toán Khi người dùng chưa chọn hoá đơn nào
         * thì disable nút xoá, sửa, thanh toán Nếu hoá đơn đã thanh toán thì
         * disable nút thanh toán
         */
        int rowSelected = -1;
        try {
            rowSelected = tblChoThue.convertRowIndexToModel(tblChoThue.getSelectedRow());
        } catch (Exception e) {
        }

        if (rowSelected == -1) {
            btnSua.setToolTipText("Vui lòng chọn hoá đơn cần cập nhật thông tin");
            btnSua.setEnabled(false);

            btnXoa.setToolTipText("Vui lòng chọn hoá đơn cần xoá");
            btnXoa.setEnabled(false);

            btnThanhToan.setToolTipText("Vui lòng chọn hoá đơn cần thanh toán");
            btnThanhToan.setEnabled(false);
        } else {
            btnSua.setEnabled(true);
            btnSua.setToolTipText("[Alt + S] Cập nhật thông tin hoá đơn");

            btnXoa.setToolTipText("[Alt + X] Xoá hoá đơn");
            btnXoa.setEnabled(true);

            if (String.valueOf(tblChoThue.getModel().getValueAt(rowSelected, 6)).equalsIgnoreCase("Chưa thanh toán")) {
                btnThanhToan.setToolTipText("Thanh toán hoá đơn");
                btnThanhToan.setEnabled(true);
            } else {
                btnThanhToan.setToolTipText("Hoá đơn đã được thanh toán");

                btnSua.setToolTipText("Không thể cập nhật hoá đơn đã thanh toán");

            }
        }
    }

    /**
     * Set row được chọn
     *
     * @param oldSelected
     */
    private void setCurrentSelected(int oldSelected) {
        if (oldSelected != -1 && oldSelected <= tblChoThue.getModel().getRowCount()) {
            tblChoThue.setRowSelectionInterval(oldSelected, oldSelected);
        } else if (oldSelected != -1 && oldSelected > tblChoThue.getModel().getRowCount()) {
            tblChoThue.setRowSelectionInterval(oldSelected - 1, oldSelected - 1);
        } else if (oldSelected == -1 && tblChoThue.getModel().getRowCount() > 0) {
            tblChoThue.setRowSelectionInterval(0, 0);
        } else {
            tblChoThue.clearSelection();
        }
    }

    /**
     * Sự kiện button thêm hóa đơn mua
     *
     * @return
     */
    private void btnThemHoaDonMua() {
        // hiện dialog thêm hoá đơn
        HoaDonMuaDialog hoaDonMuaDialog = null;
        try {
            hoaDonMuaDialog = new HoaDonMuaDialog(new JFrame(), null);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(rootComponent, ex);
        }

        // lấy hoá đơn nhập trong dialog
        HoaDon hoaDon = hoaDonMuaDialog.getHoaDon();

        // kiểm tra tình trạng mua và thêm vào DB
        try {
            if (hoaDon != null && kiemTraTinhTrangThue(hoaDon, 0)) {
                danhSachChoThue.them(hoaDon);
                refresh(true);
            }
        } catch (Exception e1) {
            JOptionPane.showMessageDialog(rootComponent, e1);
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
            JOptionPane.showMessageDialog(rootComponent, ex);
        }

        // lấy hoá đơn nhập trong dialog
        HoaDon hoaDon = hoaDonBanDialog.getHoaDon();

        // kiểm tra tình trạng mua và thêm vào DB
        try {
            if (hoaDon != null && kiemTraTinhTrangThue(hoaDon, 0)) {
                danhSachChoThue.them(hoaDon);
                refresh(true);
            }
        } catch (Exception e1) {
            JOptionPane.showMessageDialog(rootComponent, e1);
        }
    }

    /**
     * Sự kiện button sửa
     *
     * @return
     */
    private void btnSua_Click() {
        // nếu người dùng chưa chọn dòng nào thì thông báo
        if (getCurrentSelected() == -1) {
            JOptionPane.showMessageDialog(rootComponent, "Vui lòng chọn hoá đơn cần sửa");

            return;
        } else if (String.valueOf(tblChoThue.getModel().getValueAt(getCurrentSelected(), 6)).equalsIgnoreCase("Đã thanh toán")) {
            JOptionPane.showMessageDialog(rootComponent, "Không thể sửa hoá đơn đã thanh toán");
            return;
        }

        // lấy thông tin hoá đơn
        HoaDon hoaDon = danhSachChoThue.getAll().get(getCurrentSelected());
        int soLuongCu = hoaDon.getSoLuong();

        // hiện dialog sửa và thông tin sản phẩm
        HoaDonMuaDialog choThueDialog = null;
        try {
            choThueDialog = new HoaDonMuaDialog(new JFrame(), hoaDon);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(rootComponent, ex);
        }

        // lấy thông tin hoá đơn đã sửa
        hoaDon = choThueDialog.getHoaDon();

        // kiểm tra hoá đơn có rỗng không và tình trạng thuê
        try {
            if (hoaDon != null && kiemTraTinhTrangThue(hoaDon, soLuongCu)) {
                danhSachChoThue.sua(hoaDon);
                refresh(true);
            }
        } catch (Exception e1) {
            JOptionPane.showMessageDialog(rootComponent, e1);
        }
    }

    /**
     * Sự kiện button xoá
     *
     * @return
     */
    private void btnXoa_Click() {
        // nếu người dùng chưa chọn dòng nào thì thông báo
        // nếu hoá đơn đó đã thanh toán thì không cho xoá
        if (getCurrentSelected() == -1) {
            JOptionPane.showMessageDialog(rootComponent, "Vui lòng chọn hoá đơn cần xoá");
            return;
        }
//                } else if (String.valueOf(tblChoThue.getModel().getValueAt(getCurrentSelected(), 6)).equalsIgnoreCase("Đã thanh toán")) {
//                    JOptionPane.showMessageDialog(rootComponent,"Không thể xoá hoá đơn đã thanh toán");
//                    return;
//                }

        // lấy thông tin hoá đơn cần xoá
        String maHoaDon = choThueTableModel.getValueAt(getCurrentSelected(), 0).toString();
        String tenKhachHang = choThueTableModel.getValueAt(getCurrentSelected(), 1).toString();
        String tenMatHang = choThueTableModel.getValueAt(getCurrentSelected(), 2).toString();

        // hiện dialog xác nhận
        int reply = JOptionPane.showConfirmDialog(null,
                "Bạn có muốn xoá hoá đơn này không?\nTên khách hàng: " + tenKhachHang + "\nTên mặt hàng: " + tenMatHang,
                "Xóa", JOptionPane.YES_NO_OPTION);

        // nếu người dùng đồng ý
        if (reply == JOptionPane.YES_OPTION) {
            try {
                danhSachChoThue.xoa(maHoaDon);
                tblChoThue.clearSelection();
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
            JOptionPane.showMessageDialog(rootComponent, "Vui lòng chọn hoá đơn cần thanh toán");
            return;
        } else if (String.valueOf(tblChoThue.getModel().getValueAt(getCurrentSelected(), 6)).equalsIgnoreCase("Đã thanh toán")) {
            JOptionPane.showMessageDialog(rootComponent, "Hoá đơn đã thanh toán");
            return;
        }

        // lấy thông tin hoá đơn cần thanh toán
        String maHoaDon = choThueTableModel.getValueAt(getCurrentSelected(), 0).toString();
        String tenKhachHang = choThueTableModel.getValueAt(getCurrentSelected(), 1).toString();
        String tenMatHang = choThueTableModel.getValueAt(getCurrentSelected(), 2).toString();
        int soLuong = Integer.parseInt(choThueTableModel.getValueAt(getCurrentSelected(), 3).toString());

        ThanhToanDialog thanhToanDialog = new ThanhToanDialog(
                new JFrame(),
                tenKhachHang,
                tenMatHang,
                soLuong
        );

        if (thanhToanDialog.getKetQua() == 0) {
            try {
                danhSachChoThue.thanhToanHoaDon(maHoaDon);
                refresh(true);
            } catch (Exception e1) {
                JOptionPane.showMessageDialog(rootComponent, e1);
            }
        } else if (thanhToanDialog.getKetQua() > 0) {
            try {
                HoaDon hoaDon = danhSachChoThue.getAll().get(danhSachChoThue.tim(maHoaDon));
                hoaDon.setSoLuong(thanhToanDialog.getKetQua());

                danhSachChoThue.sua(hoaDon);
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

                /**
                 * Nếu người dùng chọn tất cả thì hiện tất cả hoá đơn trong bảng
                 * Nếu người dùng chọn Đang thuê thì chỉ hiện hoá đơn đang thuê
                 * Nếu người dùng chọn Đã thanh toán thì chỉ hiện hoá đơn đã
                 * thanh toán
                 */
                if (filter_text.equalsIgnoreCase("Tất cả")) //Tất cả
                {
                    sorter.setRowFilter(null);
                } else {
                    try {
                        RowFilter<Object, Object> filter = new RowFilter<Object, Object>() {
                            @Override
                            public boolean include(Entry<?, ?> entry) {
                                return (entry.getStringValue(6).contains(filter_text));
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
            danhSachChoThue = new DanhSachChoThue();
            danhSachKhachHang = new DanhSachKhachHang();
            danhSachMatHang = new DanhSachMatHang();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(rootComponent, e);
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

        btnThem = new javax.swing.JButton();
        btnSua = new javax.swing.JButton();
        btnXoa = new javax.swing.JButton();
        btnThanhToan = new javax.swing.JButton();
        cbFilter = new javax.swing.JComboBox<>();
        txtTimKiem = new javax.swing.JTextField();
        cbFilterTimKiem = new javax.swing.JComboBox<>();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblChoThue = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();

        btnThem.setText("Tạo hóa đơn mua");
        btnThem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnThemActionPerformed(evt);
            }
        });

        btnSua.setText("Sửa");
        btnSua.setEnabled(false);
        btnSua.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSuaActionPerformed(evt);
            }
        });

        btnXoa.setText("Xoá");
        btnXoa.setEnabled(false);
        btnXoa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnXoaActionPerformed(evt);
            }
        });

        btnThanhToan.setText("Thanh Toán");
        btnThanhToan.setEnabled(false);
        btnThanhToan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnThanhToanActionPerformed(evt);
            }
        });

        cbFilter.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Tất cả", "Đã thanh toán", "Chưa thanh toán" }));

        cbFilterTimKiem.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Mã Hóa Đơn", "Tên Khách Hàng", "Tên Mặt Hàng" }));

        tblChoThue.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "Mã hóa đơn", "Tên khách hàng", "Tên mặt hàng", "Số lượng", "Ngày mua", "Thành tiền", "Tình trạng"
            }
        ));
        jScrollPane1.setViewportView(tblChoThue);

        jLabel1.setText("Tìm kiếm");

        jButton1.setText("Tạo hóa đơn bán");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jLabel2.setText("Tình trạng");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 1159, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(btnSua, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(btnXoa, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(btnThanhToan))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(btnThem, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jButton1)))
                        .addGap(177, 177, 177)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(cbFilter, javax.swing.GroupLayout.PREFERRED_SIZE, 181, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(txtTimKiem, javax.swing.GroupLayout.PREFERRED_SIZE, 181, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(18, 18, 18)
                        .addComponent(cbFilterTimKiem, javax.swing.GroupLayout.PREFERRED_SIZE, 193, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(42, 42, 42)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jButton1)
                        .addComponent(btnThem))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtTimKiem, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel1)
                            .addComponent(cbFilterTimKiem, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2)
                            .addComponent(cbFilter, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(13, 13, 13)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnSua)
                    .addComponent(btnXoa)
                    .addComponent(btnThanhToan))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 270, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(83, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btnThemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnThemActionPerformed
        // TODO add your handling code here:
        this.btnThemHoaDonMua();
    }//GEN-LAST:event_btnThemActionPerformed

    private void btnSuaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSuaActionPerformed
        // TODO add your handling code here:
        this.btnSua_Click();
    }//GEN-LAST:event_btnSuaActionPerformed

    private void btnXoaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnXoaActionPerformed
        // TODO add your handling code here:
        this.btnXoa_Click();
    }//GEN-LAST:event_btnXoaActionPerformed

    private void btnThanhToanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnThanhToanActionPerformed
        // TODO add your handling code here:
        this.btnThanhToan_Click();
    }//GEN-LAST:event_btnThanhToanActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        this.btnThemHoaDonBan();
    }//GEN-LAST:event_jButton1ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnSua;
    private javax.swing.JButton btnThanhToan;
    private javax.swing.JButton btnThem;
    private javax.swing.JButton btnXoa;
    private javax.swing.JComboBox<String> cbFilter;
    private javax.swing.JComboBox<String> cbFilterTimKiem;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tblChoThue;
    private javax.swing.JTextField txtTimKiem;
    // End of variables declaration//GEN-END:variables
}
