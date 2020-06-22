/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package VTabbed;

import DAO.TaiKhoanDAO;
import Model.DanhSachNhanVien;
import Model.NhanVien;
import Model.TaiKhoan;
import VDialog.NhanVienDialog;
import VTableModel.NhanVienTableModel;
import View.DangNhap;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.RowFilter;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

/**
 *
 * @author quang
 */
public class QuanLyNhanVienTab extends javax.swing.JPanel {

    /**
     * Creates new form QuanLyNhanVienTab
     */
    
    private DanhSachNhanVien danhSachNhanVien;
    private TaiKhoanDAO taiKhoanDAO;
   
    private final Component rootComponent = this;
    private static int indexFilter = 0;
    private NhanVienTableModel nhanVienTableModel;
    private TableRowSorter<TableModel> sorter;
    
    
    /**
     * Tạo GUI
     */
    private void prepareUI() {
        btnThem.addActionListener(btnThem_Click());
        btnSua.addActionListener(btnSua_Click());
        btnXoa.addActionListener(btnXoa_Click());
        
        txtTimKiem.getDocument().addDocumentListener(txtTimKiem_DocumentListener());
        cbFilterTimKiem.addActionListener(cbFilterTimKiem_Changed());
        
        
        nhanVienTableModel = new NhanVienTableModel(danhSachNhanVien.getAll());

        sorter = new TableRowSorter<>(nhanVienTableModel);

        tblNhanVien.setModel(nhanVienTableModel);
        tblNhanVien.setRowSorter(sorter);
        
        JTableHeader jtableHeader = tblNhanVien.getTableHeader();
        Font headerFont = new Font("Verdana", Font.PLAIN, 20);
        jtableHeader.setFont(headerFont);
        refresh(true);
    }
    
    
    /**
     * Lấy vị trí đang chọn trong table
     *
     * @return
     */
    private int getCurrentSelected() {
        try {
            return tblNhanVien.convertRowIndexToModel(tblNhanVien.getSelectedRow());
        } catch (Exception e) {
            return -1;
        }
    }


    /**
     * Set row được chọn
     *
     * @param oldSelected
     */
    private void setCurrentSelected(int oldSelected) {
        if (oldSelected != -1 && oldSelected <= tblNhanVien.getModel().getRowCount()) {
            tblNhanVien.setRowSelectionInterval(oldSelected, oldSelected);
        } else if (oldSelected != -1 && oldSelected > tblNhanVien.getModel().getRowCount()) {
            tblNhanVien.setRowSelectionInterval(oldSelected - 1, oldSelected - 1);
        } else if (oldSelected == -1 && tblNhanVien.getModel().getRowCount() > 0) {
            tblNhanVien.setRowSelectionInterval(0, 0);
        } else tblNhanVien.clearSelection();
    }


    /**
     * Filter table theo tên nhân viên
     *
     * @param filter_text
     */
    private void filterTable(String filter_text) {
        if (filter_text.isEmpty())
            sorter.setRowFilter(null);
        else {
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


    /**
     * Cập nhật giao diện khi có sự thay đổi dữ liệu
     */
    public void refresh(boolean reloadData) {
        int oldSelected = getCurrentSelected();

        if (reloadData) {
            // load dữ liệu từ DB
            try {
                danhSachNhanVien.loadData();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(rootComponent, e);
            }

            // load dữ liệu lên table
            nhanVienTableModel.setModel(danhSachNhanVien.getAll());
            tblNhanVien.setModel(nhanVienTableModel);

            sorter.setModel(nhanVienTableModel);

            tblNhanVien.revalidate();
            tblNhanVien.repaint();
            setCurrentSelected(oldSelected);
        }

        // bật tắt nút xoá/sửa
        if (getCurrentSelected() != -1) {
            btnSua.setEnabled(true);
            btnSua.setToolTipText("[Alt + S] Cập nhật thông tin nhân viên");

            btnXoa.setToolTipText("[Alt + X] Xoá nhân viên");
            btnXoa.setEnabled(true);
        } else {
            btnSua.setToolTipText("Vui lòng chọn nhân viên cần cập nhật thông tin");
            btnSua.setEnabled(false);

            btnXoa.setToolTipText("Vui lòng chọn nhân viên cần xoá");
            btnXoa.setEnabled(false);
        }
    }

/**
     * Sự kiện nút thêm
     *
     * @return
     */
    private ActionListener btnThem_Click() {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // hiện dialog thêm
                NhanVienDialog nhanVienDialog = null;
                try {
                    nhanVienDialog = new NhanVienDialog(new JFrame(), null, null);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(rootComponent, ex);
                }

                // lấy thông tin nhân viên + tài khoản vừa nhập
                System.out.println("Dang trong NhanVienTab");
                
                NhanVien nhanVien = nhanVienDialog.getNhanVien();
                System.out.println(nhanVien);
                
                TaiKhoan taiKhoan = nhanVienDialog.getTaiKhoan();
                
                System.out.println(taiKhoan);

                // kiểm tra dữ liệu
                if (nhanVien == null && taiKhoan == null)
                    return;
                
                
                // thêm nhân viên và tài khoản vào DB
                try {
                    
                    danhSachNhanVien.them(nhanVien);
                    taiKhoanDAO.themTaiKhoan(taiKhoan);
                    refresh(true);
                } catch (Exception e1) {
                    JOptionPane.showMessageDialog(rootComponent, e1);
                }
            }
        };
    }


    /**
     * Sự kiện sửa thông tin nhân viên
     *
     * @return
     */
    private ActionListener btnSua_Click() {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // nếu người dùng chưa chọn dòng nào
                if (getCurrentSelected() == -1) {
                    JOptionPane.showMessageDialog(rootComponent,"Vui lòng chọn nhân viên cần sửa");
                    return;
                }

                // lấy thông tin nhân viên + tài khoản
                NhanVien nhanVien = danhSachNhanVien.getAll().get(getCurrentSelected());
                TaiKhoan taiKhoan = null;
                try {
                    taiKhoan = taiKhoanDAO.getTaiKhoanByMaNhanVien(nhanVien.getMaNhanVien());
                } catch (Exception e1) {
                    JOptionPane.showMessageDialog(rootComponent, e1);
                    return;
                }

                // hiện dialog chỉnh sửa
                NhanVienDialog nhanVienDialog = null;
                try {
                    nhanVienDialog = new NhanVienDialog(new JFrame(), nhanVien, taiKhoan);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(rootComponent, ex);
                }

                // lấy thông tin nhân viên và tài khoản sau khi chỉnh sửa
                nhanVien = nhanVienDialog.getNhanVien();
                taiKhoan = nhanVienDialog.getTaiKhoan();

                // nếu người dùng không muốn chỉnh sửa
                if (nhanVien == null)
                    return;

                // lưu dữ liệu vào DB
                try {
                    danhSachNhanVien.sua(nhanVien);

                    if (taiKhoan != null)
                        taiKhoanDAO.suaTaiKhoan(taiKhoan);

                    refresh(true);
                } catch (Exception e1) {
                    JOptionPane.showMessageDialog(rootComponent, e1);
                }
            }
        };
    }


    /**
     * Sự kiện button xoá
     *
     * @return
     */
    private ActionListener btnXoa_Click() {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // nếu người dùng chưa chọn dòng nào thì thông báo
                if (getCurrentSelected() == -1) {
                   JOptionPane.showMessageDialog(rootComponent, "Vui lòng chọn nhân viên cần xoá");
                    return;
                }

                // lấy thông tin nhân viên ở dòng đã chọn
                String maNhanVien = tblNhanVien.getModel().getValueAt(getCurrentSelected(), 0).toString();
                String tenNhanVien = tblNhanVien.getModel().getValueAt(getCurrentSelected(), 1).toString();

                // nếu người dùng chọn tài khoản mặc định thì thông báo không cho xoá
                if (maNhanVien.equals("NV00001")) {
                    JOptionPane.showMessageDialog(rootComponent, "Không thể xoá admin mặc định");
                    return;
                }

                // dialog xác nhận xoá
                int reply = JOptionPane.showConfirmDialog(null, 
                      " Bạn có muốn xoá nhân viên này không?\nTên nhân viên: "+tenNhanVien, "Xóa",
                        JOptionPane.YES_NO_OPTION);

                // nếu người dùng đồng ý
                if (reply == JOptionPane.YES_OPTION) {
                    try {
                        danhSachNhanVien.xoa(maNhanVien);
                        tblNhanVien.clearSelection();
                        refresh(true);
                    } catch (Exception e1) {
                        JOptionPane.showMessageDialog(rootComponent, e1);
                    }
                }
            }
        };
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
                        indexFilter = 4;
                        break;
                    case 3:
                        indexFilter = 5;
                        break;
                    case 4:
                        indexFilter = 6;
                        break;
                }

                filterTable(txtTimKiem.getText().trim());
            }
        };
    }


    /**
     * Sự kiện khi nhập text tìm kiếm
     * Dùng để tìm kiếm realtime
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

    
    
    
    
    
    public QuanLyNhanVienTab() {
        initComponents();
        
        // kết nối DB
        try {
            taiKhoanDAO = TaiKhoanDAO.getInstance();
            danhSachNhanVien = new DanhSachNhanVien();
        } catch (Exception e) {
             JOptionPane.showMessageDialog(rootComponent, e);
        }

        // Tạo GUI
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

        btnThem = new javax.swing.JButton();
        btnSua = new javax.swing.JButton();
        btnXoa = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblNhanVien = new javax.swing.JTable();
        cbFilterTimKiem = new javax.swing.JComboBox<>();
        txtTimKiem = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();

        btnThem.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        btnThem.setText("Thêm");

        btnSua.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        btnSua.setText("Sửa");

        btnXoa.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        btnXoa.setText("Xoá");

        tblNhanVien.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        tblNhanVien.setModel(new javax.swing.table.DefaultTableModel(
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
        tblNhanVien.setRowHeight(22);
        jScrollPane1.setViewportView(tblNhanVien);

        cbFilterTimKiem.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        cbFilterTimKiem.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Mã nhân viên", "Tên nhân viên", "CMND", "Số điện thoại", "Địa chỉ" }));

        txtTimKiem.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel1.setText("Tìm kiếm");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(33, 33, 33)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btnThem, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(28, 28, 28)
                        .addComponent(btnSua, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(28, 28, 28)
                        .addComponent(btnXoa, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 228, Short.MAX_VALUE)
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(txtTimKiem, javax.swing.GroupLayout.PREFERRED_SIZE, 205, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(cbFilterTimKiem, javax.swing.GroupLayout.PREFERRED_SIZE, 228, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(60, 60, 60)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnThem)
                            .addComponent(btnSua)
                            .addComponent(btnXoa)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(60, 60, 60)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(cbFilterTimKiem, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtTimKiem, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel1))))
                .addGap(135, 135, 135)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 331, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(60, 60, 60))
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnSua;
    private javax.swing.JButton btnThem;
    private javax.swing.JButton btnXoa;
    private javax.swing.JComboBox<String> cbFilterTimKiem;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tblNhanVien;
    private javax.swing.JTextField txtTimKiem;
    // End of variables declaration//GEN-END:variables
}
