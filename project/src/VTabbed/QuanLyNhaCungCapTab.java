/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package VTabbed;

import Model.DanhSachNhaCungCap;
import Model.NhaCungCap;
import VDialog.NhaCungCapDialog;
import VTableModel.NhaCungCapTableModel;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Level;
import java.util.logging.Logger;
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
public class QuanLyNhaCungCapTab extends javax.swing.JPanel {

    /**
     * Creates new form QuanLyNhaCungCap
     */
    
     private DanhSachNhaCungCap danhSachNhaCungCap;
     private int indexFilter = 0;
     private NhaCungCapTableModel nhaCungCapTableModel;
     private TableRowSorter<TableModel> sorter;
     private final Component rootComponent = this;
//    private JTable tblKhachHang;
    
    public void prepareUI(){
         nhaCungCapTableModel = new NhaCungCapTableModel(danhSachNhaCungCap.getAll());

        sorter = new TableRowSorter<>(nhaCungCapTableModel);

        tblNhaCungCap.setModel(nhaCungCapTableModel); 
        tblNhaCungCap.setRowSorter(sorter);
        txtTimKiem.getDocument().addDocumentListener(txtTimKiem_DocumentListerner());
        cbFilterTimKiem.addActionListener(cbFilterTimKiem_Changed());
        refresh(true);
    }
    
    /**
     * Lấy vị trí đang chọn trong table
     *
     * @return
     */
    private int getCurrentSelected() {
        try {
            return tblNhaCungCap.convertRowIndexToModel(tblNhaCungCap.getSelectedRow());
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
        if (oldSelected != -1 && oldSelected <= tblNhaCungCap.getModel().getRowCount()) {
            tblNhaCungCap.setRowSelectionInterval(oldSelected, oldSelected);
        } else if (oldSelected != -1 && oldSelected > tblNhaCungCap.getModel().getRowCount()) {
            tblNhaCungCap.setRowSelectionInterval(oldSelected - 1, oldSelected - 1);
        } else if (oldSelected == -1 && tblNhaCungCap.getModel().getRowCount() > 0) {
            tblNhaCungCap.setRowSelectionInterval(0, 0);
        } else tblNhaCungCap.clearSelection();
    }


    /**
     * Refresh giao diện khi có cập nhật
     */
    public void refresh(boolean reloadData) {
        int oldSelected = getCurrentSelected();

        if (reloadData) {
            // load lai dữ liệu
            try {
                danhSachNhaCungCap.loadData();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Lỗi không load được dữ liệu");
            }

            // cập nhật table
            nhaCungCapTableModel.setModel(danhSachNhaCungCap.getAll());
            tblNhaCungCap.setModel(nhaCungCapTableModel);

            sorter.setModel(nhaCungCapTableModel);

            tblNhaCungCap.revalidate();
            tblNhaCungCap.repaint();
            setCurrentSelected(oldSelected);
        }

        // bật tắt chức năng sữa, xoá
        if (getCurrentSelected() != -1) {
            btnSua.setEnabled(true);
            btnSua.setToolTipText("[Alt + S] Cập nhật thông tin nhà cung cấp");

            btnXoa.setToolTipText("[Alt + X] Xoá nhà cung cấp");
            btnXoa.setEnabled(true);
        } else {
            btnSua.setToolTipText("Vui lòng chọn nhà cung cấp cần cập nhật thông tin");
            btnSua.setEnabled(false);

            btnXoa.setToolTipText("Vui lòng chọn nhà cung cấp cần xoá");
            btnXoa.setEnabled(false);
        }
    }


    /**
     * Tìm kiếm record theo tên nhà cung cấp
     * Dùng đối tượng filter table
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

    
    
    
    public QuanLyNhaCungCapTab() {
        initComponents();
         try {
             danhSachNhaCungCap = new DanhSachNhaCungCap();
              prepareUI();
                
         } catch (Exception ex) {
             Logger.getLogger(QuanLyNhaCungCapTab.class.getName()).log(Level.SEVERE, null, ex);
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
                    case 3:
                        indexFilter = 3;
                        break;
                }

                filterTable(txtTimKiem.getText().trim());
            }

           
        };
    }


    /**
     * Sự kiện khi nhập text tìm kiếm
     * D2ung để tìm kiếm realtime
     *
     * @return
     */
    private DocumentListener txtTimKiem_DocumentListerner() {
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
        tblNhaCungCap = new javax.swing.JTable();
        cbFilterTimKiem = new javax.swing.JComboBox<>();
        txtTimKiem = new javax.swing.JTextField();

        btnThem.setText("Thêm");
        btnThem.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnThemMouseClicked(evt);
            }
        });

        btnSua.setText("Sửa");
        btnSua.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnSuaMouseClicked(evt);
            }
        });

        btnXoa.setText("Xóa");
        btnXoa.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnXoaMouseClicked(evt);
            }
        });

        tblNhaCungCap.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane1.setViewportView(tblNhaCungCap);

        cbFilterTimKiem.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Mã NCC ", "Tên NCC ", "Địa chỉ ", "Số điện thoại" }));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(37, 37, 37)
                .addComponent(btnThem, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(38, 38, 38)
                .addComponent(btnSua, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(40, 40, 40)
                .addComponent(btnXoa, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 356, Short.MAX_VALUE)
                .addComponent(txtTimKiem, javax.swing.GroupLayout.PREFERRED_SIZE, 191, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(cbFilterTimKiem, javax.swing.GroupLayout.PREFERRED_SIZE, 212, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnThem)
                    .addComponent(btnSua)
                    .addComponent(btnXoa)
                    .addComponent(cbFilterTimKiem, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtTimKiem, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(74, 74, 74)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 246, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(312, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btnThemMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnThemMouseClicked
                NhaCungCapDialog nhaCungCapDialog = null;
                try {
                    nhaCungCapDialog = new NhaCungCapDialog(new JFrame(),null); // 
                    nhaCungCapDialog.setVisible(true);
                    
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, ex);
                }

                // lấy nhà cung cấp vừa nhập
                NhaCungCap nhaCungCap = nhaCungCapDialog.getNhaCungCap();

                // nếu người dùng không muốn thêm nhà cung cấp
                if (nhaCungCap == null)
                    return;

                // thêm vào DB
                try {
                    danhSachNhaCungCap.them(nhaCungCap);
                    refresh(true);
                } catch (Exception e1) {
                    JOptionPane.showMessageDialog(this, e1);
                }
    }//GEN-LAST:event_btnThemMouseClicked

    private void btnSuaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnSuaMouseClicked
        //        // nếu người dùng chưa chọn dòng nào thì thông báo
                if (getCurrentSelected() == -1) {
                    JOptionPane.showMessageDialog(this, "Vui lòng chọn nhà cung cấp cần sửa");
                    return;
                }
                
               NhaCungCap a =  danhSachNhaCungCap.getAll().get(getCurrentSelected());

                // hiện dialog sửa thông tin nhà cung cấp
                NhaCungCapDialog nhaCungCapDialog  = null;
                try {
                    nhaCungCapDialog = new NhaCungCapDialog(new JFrame(),a);


                    // lấy thông tin nhà cung cấp
                    NhaCungCap nhaCungCap = nhaCungCapDialog.getNhaCungCap();
                    // kiểm tra nhà cung cấp có null không
                if (nhaCungCap == null)
                    return;

                // lưu thông tin thay đổi vào DB
                try {
                    danhSachNhaCungCap.sua(nhaCungCap);
                    refresh(true);
                } catch (Exception e1) {
                    JOptionPane.showMessageDialog(this,e1);
                }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this,ex);
                }
    }//GEN-LAST:event_btnSuaMouseClicked

    private void btnXoaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnXoaMouseClicked
        //        // nếu người dùng chưa chọn dòn gnào
                if (getCurrentSelected() == -1) {
                    JOptionPane.showMessageDialog(this,"Chọn ô cần xóa");
                    return;
                }

                // lấy thông tin khách hàng ở dòng vừa chọn
                String maKhachHang = tblNhaCungCap.getModel().getValueAt(getCurrentSelected(), 0).toString();
                String tenKhachHang = tblNhaCungCap.getModel().getValueAt(getCurrentSelected(), 1).toString();

                // dialog cảnh báo người dùng
                int reply = JOptionPane.showConfirmDialog(null, "Xóa nhà cung cấp", "Xóa", JOptionPane.YES_NO_OPTION);

                // nếu người dùng đồng ý
                if (reply == JOptionPane.YES_OPTION) {
                    try {
                        danhSachNhaCungCap.xoa(maKhachHang);
                        tblNhaCungCap.clearSelection();
                        refresh(true);
                    } catch (Exception e1) {
                        JOptionPane.showMessageDialog(this, e1);
                    }
                }
    }//GEN-LAST:event_btnXoaMouseClicked


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnSua;
    private javax.swing.JButton btnThem;
    private javax.swing.JButton btnXoa;
    private javax.swing.JComboBox<String> cbFilterTimKiem;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tblNhaCungCap;
    private javax.swing.JTextField txtTimKiem;
    // End of variables declaration//GEN-END:variables
}
