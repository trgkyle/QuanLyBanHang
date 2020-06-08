/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package VTabbed;

import Model.DanhSachKhachHang;
import Model.KhachHang;
import VDialog.KhachHangDialog;
import VTableModel.KhachHangTableModel;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.RowFilter;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;


/**
 *
 * @author quang
 */
public class QuanLyKhachHangTab extends javax.swing.JPanel {

     private DanhSachKhachHang danhSachKhachHang;
     private int indexFilter = 0;
     private KhachHangTableModel khachHangTableModel;
     private TableRowSorter<TableModel> sorter;
     private final Component rootComponent = this;
//    private JTable tblKhachHang;
    
    public void prepareUI(){
         khachHangTableModel = new KhachHangTableModel(danhSachKhachHang.getAll());

        sorter = new TableRowSorter<>(khachHangTableModel);

        tblKhachHang.setModel(khachHangTableModel); //= new JTable(khachHangTableModel);
        tblKhachHang.setRowSorter(sorter);
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
            return tblKhachHang.convertRowIndexToModel(tblKhachHang.getSelectedRow());
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
        if (oldSelected != -1 && oldSelected <= tblKhachHang.getModel().getRowCount()) {
            tblKhachHang.setRowSelectionInterval(oldSelected, oldSelected);
        } else if (oldSelected != -1 && oldSelected > tblKhachHang.getModel().getRowCount()) {
            tblKhachHang.setRowSelectionInterval(oldSelected - 1, oldSelected - 1);
        } else if (oldSelected == -1 && tblKhachHang.getModel().getRowCount() > 0) {
            tblKhachHang.setRowSelectionInterval(0, 0);
        } else tblKhachHang.clearSelection();
    }


    /**
     * Refresh giao diện khi có cập nhật
     */
    public void refresh(boolean reloadData) {
        int oldSelected = getCurrentSelected();

        if (reloadData) {
            // load lai dữ liệu
            try {
                danhSachKhachHang.loadData();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Lỗi không load được dữ liệu");
            }

            // cập nhật table
            khachHangTableModel.setModel(danhSachKhachHang.getAll());
            tblKhachHang.setModel(khachHangTableModel);

            sorter.setModel(khachHangTableModel);

            tblKhachHang.revalidate();
            tblKhachHang.repaint();
            setCurrentSelected(oldSelected);
        }

        // bật tắt chức năng sữa, xoá
        if (getCurrentSelected() != -1) {
            btnSua.setEnabled(true);
            btnSua.setToolTipText("[Alt + S] Cập nhật thông tin khách hàng");

            btnXoa.setToolTipText("[Alt + X] Xoá khách hàng");
            btnXoa.setEnabled(true);
        } else {
            btnSua.setToolTipText("Vui lòng chọn khách hàng cần cập nhật thông tin");
            btnSua.setEnabled(false);

            btnXoa.setToolTipText("Vui lòng chọn khách hàng cần xoá");
            btnXoa.setEnabled(false);
        }
    }


    /**
     * Tìm kiếm record theo tên khách hàng
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

    
    
    
    /**
     * Creates new form QuanLyKhachHang
     */
    public QuanLyKhachHangTab() {
        initComponents();
         try {
             danhSachKhachHang = new DanhSachKhachHang();
              prepareUI();
                //System.out.println("Load Bang Quan Ly Khach Hang");
         } catch (Exception ex) {
             Logger.getLogger(QuanLyKhachHangTab.class.getName()).log(Level.SEVERE, null, ex);
         }
       
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
        txtTimKiem = new javax.swing.JTextField();
        cbFilterTimKiem = new javax.swing.JComboBox<>();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblKhachHang = new javax.swing.JTable();

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

        btnXoa.setText("Xoá");
        btnXoa.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnXoaMouseClicked(evt);
            }
        });

        cbFilterTimKiem.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Mã Khách Hàng", "Tên Khách Hàng", "CMND", "Số Điên Thoại", "Địa Chỉ" }));

        tblKhachHang.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane1.setViewportView(tblKhachHang);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(44, 44, 44)
                .addComponent(btnThem)
                .addGap(56, 56, 56)
                .addComponent(btnSua)
                .addGap(68, 68, 68)
                .addComponent(btnXoa)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 345, Short.MAX_VALUE)
                .addComponent(txtTimKiem, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cbFilterTimKiem, javax.swing.GroupLayout.PREFERRED_SIZE, 226, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(35, 35, 35)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnThem)
                    .addComponent(btnSua)
                    .addComponent(btnXoa)
                    .addComponent(txtTimKiem, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cbFilterTimKiem, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(66, 66, 66)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(98, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

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

    
    
    
    
    
    private void btnThemMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnThemMouseClicked
        // dialog nhập khách hàng mới
                KhachHangDialog khachHangDialog = null;
                try {
                    khachHangDialog = new KhachHangDialog(new JFrame(),null); // 
                    khachHangDialog.setVisible(true);
                    
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, ex);
                }

                // lấy khách hàng vừa nhập
                KhachHang khachHang = khachHangDialog.getKhachHang();

                // nếu người dùng không muốn thêm khách hàng
                if (khachHang == null)
                    return;

                // thêm vào DB
                try {
                    danhSachKhachHang.them(khachHang);
                    refresh(true);
                } catch (Exception e1) {
                    JOptionPane.showMessageDialog(this, e1);
                }
    }//GEN-LAST:event_btnThemMouseClicked

    private void btnSuaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnSuaMouseClicked
        // nếu người dùng chưa chọn dòng nào thì thông báo
                if (getCurrentSelected() == -1) {
                    JOptionPane.showMessageDialog(this, "Vui lòng chọn khách hàng cần sửa");
                    return;
                }
                
               KhachHang a =  danhSachKhachHang.getAll().get(getCurrentSelected());

                // hiện dialog sửa thông tin khách hàng
                KhachHangDialog khachHangDialog = null;
                try {
                    khachHangDialog = new KhachHangDialog(new JFrame(),a);//
//                            danhSachKhachHang.getAll().get(getCurrentSelected()));
//                    khachHangDialog.setVisible(true);
//                    System.out.println(danhSachKhachHang.getAll().get(getCurrentSelected()));

                    // lấy thông tin khách hàng
                    KhachHang khachHang = khachHangDialog.getKhachHang();
                    // kiểm tra khách hàng có null không
                if (khachHang == null)
                    return;

                // lưu thông tin thay đổi vào DB
                try {
                    danhSachKhachHang.sua(khachHang);
                    refresh(true);
                } catch (Exception e1) {
                    JOptionPane.showMessageDialog(this,e1);
                }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this,ex);
                }

                

                
    }//GEN-LAST:event_btnSuaMouseClicked

    private void btnXoaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnXoaMouseClicked
        // nếu người dùng chưa chọn dòn gnào
                if (getCurrentSelected() == -1) {
                    JOptionPane.showMessageDialog(this,"Chọn ô cần xóa");
                    return;
                }

                // lấy thông tin khách hàng ở dòng vừa chọn
                String maKhachHang = tblKhachHang.getModel().getValueAt(getCurrentSelected(), 0).toString();
                String tenKhachHang = tblKhachHang.getModel().getValueAt(getCurrentSelected(), 1).toString();

                // dialog cảnh báo người dùng
                int reply = JOptionPane.showConfirmDialog(null, "Xóa khách hàng", "Xóa", JOptionPane.YES_NO_OPTION);

                // nếu người dùng đồng ý
                if (reply == JOptionPane.YES_OPTION) {
                    try {
                        danhSachKhachHang.xoa(maKhachHang);
                        tblKhachHang.clearSelection();
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
    private javax.swing.JTable tblKhachHang;
    private javax.swing.JTextField txtTimKiem;
    // End of variables declaration//GEN-END:variables
}
