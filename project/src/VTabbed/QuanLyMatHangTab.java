/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package VTabbed;

import Model.DanhSachMatHang;
import Model.MatHang;
import VDialog.KhachHangDialog;
import VDialog.MatHangDialog;

import VDialog.XoaDialog;

import VTableModel.MatHangTableModel;
import java.awt.Component;
import java.awt.Font;
import java.awt.Frame;
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
public class QuanLyMatHangTab extends javax.swing.JPanel {

    /**
     * Creates new form QuanLyMatHangTab
     */
    private int indexFilter = 0;
    private TableRowSorter<TableModel> sorter;
    private final Component rootComponent = this;
    private DanhSachMatHang danhSachMathang;
    private MatHangTableModel matHangTableModel;
   // private MatHang matHang;
    
    public void prepareUI(){
         matHangTableModel = new MatHangTableModel(danhSachMathang.getAll());

        sorter = new TableRowSorter<>(matHangTableModel);

        tblMatHang.setModel(matHangTableModel); //= new JTable(khachHangTableModel);
        tblMatHang.setRowSorter(sorter);
        
        
        txtTimKiem.getDocument().addDocumentListener(txtTimKiem_DocumentListener());
        
        btnHangHong.addActionListener(btnHangHong_Click());
        
        cbFilterTimKiem.addActionListener(cbFilterTimKiem_Changed());
        
        
        JTableHeader jtableHeader = tblMatHang.getTableHeader();
        Font headerFont = new Font("Verdana", Font.PLAIN, 20);
        jtableHeader.setFont(headerFont);
        
        refresh(true);
        
    }
    
    /**
     * Sự kiện button xoá băng mặt hàng
     *
     * @return
     */
    private ActionListener btnHangHong_Click() {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // hiện dialog xác nhận xoá
                int reply = JOptionPane.showConfirmDialog(null, "Xóa mặt hàng", "Xóa", JOptionPane.YES_NO_OPTION);

                // nếu người dùng đồng ý
                if (reply == JOptionPane.YES_OPTION) {
                    try {
                         danhSachMathang.xoaMatHangHong();
                         refresh(true);
                    } catch (Exception e1) {
                        JOptionPane.showMessageDialog(null, e1);
                    }
                }

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
            return tblMatHang.convertRowIndexToModel(tblMatHang.getSelectedRow());
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
        if (oldSelected != -1 && oldSelected <= tblMatHang.getModel().getRowCount()) {
            tblMatHang.setRowSelectionInterval(oldSelected, oldSelected);
        } else if (oldSelected != -1 && oldSelected > tblMatHang.getModel().getRowCount()) {
            tblMatHang.setRowSelectionInterval(oldSelected - 1, oldSelected - 1);
        } else if (oldSelected == -1 && tblMatHang.getModel().getRowCount() > 0) {
            tblMatHang.setRowSelectionInterval(0, 0);
        } else tblMatHang.clearSelection();
    }


    /**
     * Refresh giao diện khi có cập nhật
     */
    public void refresh(boolean reloadData) {
        int oldSelected = getCurrentSelected();

        if (reloadData) {
            // load lại dữ liệu từ DB
            try {
                danhSachMathang.loadData();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(rootComponent, e);
            }

            // load lại table mặt hàng
            matHangTableModel.setModel(danhSachMathang.getAll());
            tblMatHang.setModel(matHangTableModel);

            sorter.setModel(matHangTableModel);

            tblMatHang.revalidate();
            tblMatHang.repaint();
            setCurrentSelected(oldSelected);
        }

        // Nếu chưa có mặt hàng nào hư hỏng thì tắt nút xoá mặt hàng hỏng
        btnHangHong.setEnabled(false);
        for (MatHang matHang : danhSachMathang.getAll()) {
            if (!matHang.isTinhTrang()) {
                btnHangHong.setEnabled(true);
                break;
            }
        }

        /**
         * Kiểm tra xem người dùng có chọn dòng nào không
         * Nếu người dùng có chọn thì bật nút xoá và sửa
         */
        if (tblMatHang.getSelectedRow() == -1) {
            btnSua.setToolTipText("Vui lòng chọn mặt hàng cần cập nhật thông tin");
            btnSua.setEnabled(false);

            btnXoa.setToolTipText("Vui lòng chọn mặt hàng cần xoá");
            btnXoa.setEnabled(false);
        } else {
            btnSua.setEnabled(true);
            btnSua.setToolTipText("[Alt + S] Cập nhật thông tin mặt hàng");

            btnXoa.setEnabled(true);
            btnXoa.setToolTipText("[Alt + X] Xoá mặt hàng");
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

    
    
    
    
    
    
    
    
    
    public QuanLyMatHangTab() {
        initComponents();
         try {
             danhSachMathang = new DanhSachMatHang();
              prepareUI();
                //System.out.println("Load Bang Quan Ly Khach Hang");
         } catch (Exception ex) {
             Logger.getLogger(QuanLyKhachHangTab.class.getName()).log(Level.SEVERE, null, ex);
         }
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
        txtTimKiem = new javax.swing.JTextField();
        cbFilterTimKiem = new javax.swing.JComboBox<>();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblMatHang = new javax.swing.JTable();
        btnHangHong = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();

        setPreferredSize(new java.awt.Dimension(1127, 639));

        btnThem.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        btnThem.setText("Thêm");
        btnThem.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnThemMouseClicked(evt);
            }
        });

        btnSua.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        btnSua.setText("Sửa");
        btnSua.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnSuaMouseClicked(evt);
            }
        });

        btnXoa.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        btnXoa.setText("Xoá");
        btnXoa.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnXoaMouseClicked(evt);
            }
        });

        txtTimKiem.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N

        cbFilterTimKiem.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        cbFilterTimKiem.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Mã Mặt Hàng", "Tên Mặt Hàng", "Thể Loại", "Hãng Sản Xuất" }));
        cbFilterTimKiem.setMaximumSize(new java.awt.Dimension(152, 34));
        cbFilterTimKiem.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                cbFilterTimKiemMouseClicked(evt);
            }
        });

        tblMatHang.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        tblMatHang.setModel(new javax.swing.table.DefaultTableModel(
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
        tblMatHang.setRowHeight(22);
        jScrollPane1.setViewportView(tblMatHang);

        btnHangHong.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        btnHangHong.setText("Xoá Hàng Hỏng");
        btnHangHong.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnHangHongMouseClicked(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel1.setText("Tìm Kiếm");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btnThem, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnSua, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnXoa, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(36, 36, 36)
                        .addComponent(btnHangHong)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 95, Short.MAX_VALUE)
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtTimKiem, javax.swing.GroupLayout.PREFERRED_SIZE, 216, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(cbFilterTimKiem, 0, 111, Short.MAX_VALUE)
                        .addGap(15, 15, 15))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane1)
                        .addContainerGap())))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(60, 60, 60)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnThem)
                    .addComponent(btnSua)
                    .addComponent(btnXoa)
                    .addComponent(btnHangHong)
                    .addComponent(txtTimKiem, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cbFilterTimKiem, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1))
                .addGap(138, 138, 138)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 249, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(161, Short.MAX_VALUE))
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
                        indexFilter = 2;
                        break;
                    case 3:
                        indexFilter = 6;
                        break;
                }

                filterTable(txtTimKiem.getText().trim());
            }
        };
    }
    
    /**
     * Sự kiện khi nhập text vào ô tìm kiếm
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
    
    
    private void cbFilterTimKiemMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cbFilterTimKiemMouseClicked
//        switch (cbFilterTimKiem.getSelectedIndex()) {
//                    case 0:
//                        indexFilter = 0;
//                        break;
//                    case 1:
//                        indexFilter = 1;
//                        break;
//                    case 2:
//                        indexFilter = 2;
//                        break;
//                    case 3:
//                        indexFilter = 6;
//                        break;
//                }
//
//                filterTable(txtTimKiem.getText().trim());
    }//GEN-LAST:event_cbFilterTimKiemMouseClicked

    
    
    
    private void btnXoaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnXoaMouseClicked
        // nếu người dùng chưa chọn dòng nào thì thông báo
                if (getCurrentSelected() == -1) {
                    JOptionPane.showMessageDialog(this,"Chọn ô cần Xóa");
                    return;
                }

                // lấy thông tin băng đĩa đã chọn
                String maMatHang = tblMatHang.getModel().getValueAt(getCurrentSelected(), 0).toString();
                String tenMatHang = tblMatHang.getModel().getValueAt(getCurrentSelected(), 1).toString();
                int soLuong = Integer.parseInt(tblMatHang.getModel().getValueAt(getCurrentSelected(), 5).toString());

                XoaDialog xoaMatHangDialog = new XoaDialog(new JFrame(), maMatHang, tenMatHang, soLuong);


                int ketQua = xoaMatHangDialog.getKetQua();
                System.out.println(ketQua);
                if (ketQua == 0) {
                    try {
                        danhSachMathang.xoa(maMatHang);
                        tblMatHang.clearSelection();
                        refresh(true);
                    } catch (Exception e1) {
                        JOptionPane.showMessageDialog(this,e1);
                    }
                } else if (ketQua > 0) {
                    try {
                        MatHang matHang = danhSachMathang.getAll().get(danhSachMathang.tim(maMatHang));
                        matHang.setSoLuongTon(ketQua);

                        danhSachMathang.sua(matHang);
                        refresh(true);
                    } catch (Exception e1) {
                        JOptionPane.showMessageDialog(this,e1);
                    }
                }
        
    }//GEN-LAST:event_btnXoaMouseClicked

    private void btnSuaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnSuaMouseClicked
       // nếu người dùng chưa chọn thì thông báo
                if (getCurrentSelected() == -1) {
                    JOptionPane.showMessageDialog(this,"Vui lòng chọn mặt hàng cần sửa");
                    return;
                }

                // hiện dialog sửa mat hang
                MatHangDialog matHangDialog = null;
                try {
                    matHangDialog = new MatHangDialog(new JFrame(),
                            danhSachMathang.getAll().get(getCurrentSelected()));
                                    // lấy thông tin mat hang vừa sửa
                    MatHang matHang = matHangDialog.getMatHang();

                    // nếu người dùng không sửa
                    if (matHang == null)
                        return;

                    // sửa băng đĩa trong danh sách và DB
                    try {
                        danhSachMathang.sua(matHang);
                        refresh(true);
                    } catch (Exception e1) {
                        JOptionPane.showMessageDialog(this,e1);
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this,ex);
                }


    }//GEN-LAST:event_btnSuaMouseClicked

    private void btnThemMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnThemMouseClicked
               // dialog nhập mat hang mới
                MatHangDialog matHangDialog = null;
                
                try {
                    matHangDialog = new MatHangDialog(new JFrame(),null); // 
                    
                    matHangDialog.setVisible(true);
                    
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, ex);
                }

                // lấy mat hàng vừa nhập
                MatHang matHang = matHangDialog.getMatHang();

                // nếu người dùng không muốn thêm mat hàng
                if ( matHang== null)
                    return;

                // thêm vào DB
                try {
                    danhSachMathang.them(matHang);
                    refresh(true);
                } catch (Exception e1) {
                    JOptionPane.showMessageDialog(this, e1);
                }


                    
                    


    }//GEN-LAST:event_btnThemMouseClicked

    private void btnHangHongMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnHangHongMouseClicked
        
    }//GEN-LAST:event_btnHangHongMouseClicked


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnHangHong;
    private javax.swing.JButton btnSua;
    private javax.swing.JButton btnThem;
    private javax.swing.JButton btnXoa;
    private javax.swing.JComboBox<String> cbFilterTimKiem;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tblMatHang;
    private javax.swing.JTextField txtTimKiem;
    // End of variables declaration//GEN-END:variables
}
