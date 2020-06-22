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
import Model.MatHangHoaDon;
import Utils.Formats;
import Utils.PatternRegexs;
import VTableModel.MatHangHoaDonTableModel;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Date;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JRootPane;
import javax.swing.SwingUtilities;
import static javax.swing.WindowConstants.DISPOSE_ON_CLOSE;
import javax.swing.table.JTableHeader;

/**
 *
 * @author s2hdp
 */
public class HoaDonBanDialog extends javax.swing.JDialog {

    private HoaDon hoaDon;
    private ArrayList<MatHangHoaDon> gioHang = new ArrayList<MatHangHoaDon>();
    private boolean isChinhSua;
    private DanhSachKhachHang danhSachKhachHang;
    private DanhSachMatHang danhSachMatHang;
    private HoaDonDAO hoaDonDAO;
    private MatHangHoaDonTableModel muaHangHoaDonTableModel;

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
        setSize(801, 790);
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
            this.jtitle.setText("Thông tin hóa đơn");
            this.btnLuu.setVisible(false);
            this.jlabelMMH.setVisible(false);
            this.cbMaMatHang.setVisible(false);
            this.jlabelSLM.setVisible(false);
            this.txtSoLuong.setVisible(false);
            this.btnThemGioHang.setVisible(false);
            txtMaHoaDon.setText(hoaDon.getMaHoaDon());
        }

        // set the comboBox khachHang
        if (isChinhSua) {
            cbMaKhachHang.addItem(String.format("[%s] %s",
                    hoaDon.getKhachHang().getMaKH(),
                    hoaDon.getKhachHang().getHoTen()));
        } else {
            for (KhachHang khachHang : danhSachKhachHang.getAll()) {
                cbMaKhachHang.addItem(String.format("[%s] %s", khachHang.getMaKH(), khachHang.getHoTen()));
            }
        }

        // set the comboBox matHang
        if (isChinhSua) {

        } else {
            for (MatHang matHang : danhSachMatHang.getAll()) {
                if (matHang.getSoLuongTon() > 0) {
                    cbMaMatHang.addItem(String.format("[%s] %s", matHang.getMaMatHang(), matHang.getTenMatHang()));
                }
            }
        }

        if (isChinhSua) {
            this.gioHang = this.hoaDon.getMatHang();
            refresh(true);
        }

        // set date
        if (isChinhSua) {
            dateChooser.setDate(hoaDon.getNgayLap());
            dateChooser.setEnabled(false);
        } else {
            dateChooser.setDate(new java.util.Date());
        }

        //set thanh tien
        if (isChinhSua) {
            DecimalFormat dcf = new DecimalFormat("###,###,###,###.##");
            inputThanhTien.setText(dcf.format(hoaDon.thanhTien()));
        }
        
        
        
         JTableHeader jtableHeader = tableGioHang.getTableHeader();
        Font headerFont = new Font("Verdana", Font.PLAIN, 20);
        jtableHeader.setFont(headerFont);

    }

    public void refresh(boolean reloadData) {

        if (reloadData) {
            System.out.println("Reload1");
            muaHangHoaDonTableModel = new MatHangHoaDonTableModel(this.gioHang);
            System.out.println("Reload2");
            this.tableGioHang.setModel(muaHangHoaDonTableModel);
            System.out.println("Reload3");
            
            if(this.gioHang.size() > 0) this.btnLuu.setEnabled(true);
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
    private void btnThoat_Click() {
        this.hoaDon = null;
        HoaDonBanDialog.this.dispose();
    }

    /**
     * Sự kiện nút Lưu
     *
     * @return
     */
    private void btnLuu_Click() {
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
                this.gioHang,
                txtMaHoaDon.getText().trim(),
                khachHang,
                Date.valueOf(Formats.DATE_FORMAT_SQL.format(dateChooser.getDate()))
        );
        System.out.println("Gio hang");
        System.out.println(this.gioHang.size());
        this.hoaDon = hoaDon;


        // đóng dialog
        HoaDonBanDialog.this.dispose();
    }

    private void btnThemGioHang_Click() {
        MatHang matHang = null;
        Pattern pattern = null;
        Matcher matcher = null;

        // lấy dữ liệu mặt hàng
        pattern = Pattern.compile("(MH\\d.*)]", Pattern.MULTILINE);
        matcher = pattern.matcher(String.valueOf(cbMaMatHang.getSelectedItem()));

        if (matcher.find()) {
            matHang = danhSachMatHang.getAll().get(danhSachMatHang.tim(matcher.group(1)));
        }

        MatHangHoaDon matHangHoaDon = new MatHangHoaDon(matHang, Integer.parseInt(this.txtSoLuong.getText()));
        System.out.println("san pham truoc khi them vao gio hang");
        System.out.println(matHangHoaDon.getMaNhaCungCap());
        System.out.println(matHangHoaDon.toString());
        boolean replace = false;
        for (int i = 0; i < this.gioHang.size(); i++) {
            if (this.gioHang.get(i).getMaMatHang() == null ? matHangHoaDon.getMaMatHang() == null : this.gioHang.get(i).getMaMatHang().equals(matHangHoaDon.getMaMatHang())) {
                replace = true;
                if (matHangHoaDon.getSoLuong() + this.gioHang.get(i).getSoLuong() <= danhSachMatHang.getAll().get(danhSachMatHang.tim(matHangHoaDon.getMaMatHang())).getSoLuongTon()) {
                    matHangHoaDon.setSoLuong(matHangHoaDon.getSoLuong() + this.gioHang.get(i).getSoLuong());
                    this.gioHang.set(i, matHangHoaDon);
                } else {
                    JOptionPane.showMessageDialog(this, "Lượng mặt hàng không đủ");
                    return;
                }
            }
        }
        if (!replace) {
            if (matHangHoaDon.getSoLuong() <= danhSachMatHang.getAll().get(danhSachMatHang.tim(matHangHoaDon.getMaMatHang())).getSoLuongTon()) {
                this.gioHang.add(matHangHoaDon);
            } else {
                JOptionPane.showMessageDialog(this, "Lượng mặt hàng không đủ");
                return;
            }
        }

        this.refresh(true);
    }

    private void tinhTongTien() {
        double tongTien = 0;
        for (MatHangHoaDon matHang : this.gioHang) {
            System.out.println((double) matHang.getSoLuong());
            System.out.println(matHang.getDonGia());
            tongTien += (double) matHang.getSoLuong() * matHang.getDonGia();
        }
        DecimalFormat dcf = new DecimalFormat("###,###,###,###.##");
        System.out.println(tongTien);
        this.inputThanhTien.setText(dcf.format(tongTien));

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
        jlabelMMH = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jlabelSLM = new javax.swing.JLabel();
        txtMaHoaDon = new javax.swing.JTextField();
        dateChooser = new com.toedter.calendar.JDateChooser();
        cbMaKhachHang = new javax.swing.JComboBox<>();
        cbMaMatHang = new javax.swing.JComboBox<>();
        txtSoLuong = new javax.swing.JTextField();
        btnLuu = new javax.swing.JButton();
        jLabel7 = new javax.swing.JLabel();
        inputThanhTien = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        tableGioHang = new javax.swing.JTable();
        jLabel8 = new javax.swing.JLabel();
        btnThemGioHang = new javax.swing.JButton();
        jtitle = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Hóa đơn bán hàng");
        setResizable(false);

        jPanel1.setToolTipText("Hóa đơn");
        jPanel1.setName("Hi"); // NOI18N

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel1.setText("Mã Hoá Đơn");

        btnThoat.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        btnThoat.setText("Thoát");
        btnThoat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnThoatActionPerformed(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel2.setText("Mã Khách Hàng");

        jlabelMMH.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jlabelMMH.setText("Mã Mặt Hàng");

        jLabel4.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel4.setText("Ngày Mua");

        jlabelSLM.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jlabelSLM.setText("Số Lượng Mua");

        txtMaHoaDon.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N

        dateChooser.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N

        cbMaKhachHang.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N

        cbMaMatHang.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N

        txtSoLuong.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        txtSoLuong.setText("0");
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

        btnLuu.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        btnLuu.setText("Tạo hóa đơn");
        btnLuu.setEnabled(false);
        btnLuu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLuuActionPerformed(evt);
            }
        });

        jLabel7.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel7.setText("Thành tiền");

        inputThanhTien.setEditable(false);
        inputThanhTien.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N

        tableGioHang.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        tableGioHang.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Tên mặt hàng", "Mã nhà cung cấp", "Số lượng"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.Object.class, java.lang.String.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        tableGioHang.setRowHeight(22);
        jScrollPane1.setViewportView(tableGioHang);

        jLabel8.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel8.setText("Giỏ hàng");

        btnThemGioHang.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        btnThemGioHang.setText("Thêm vào giỏ hàng");
        btnThemGioHang.setEnabled(false);
        btnThemGioHang.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnThemGioHangActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel7)
                            .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(inputThanhTien, javax.swing.GroupLayout.DEFAULT_SIZE, 294, Short.MAX_VALUE)
                            .addComponent(dateChooser, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(btnLuu, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnThoat, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jlabelMMH)
                            .addComponent(jLabel1)
                            .addComponent(jlabelSLM, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(cbMaMatHang, javax.swing.GroupLayout.Alignment.TRAILING, 0, 299, Short.MAX_VALUE)
                            .addComponent(cbMaKhachHang, javax.swing.GroupLayout.Alignment.TRAILING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(txtMaHoaDon, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(txtSoLuong)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 545, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(173, 173, 173)
                        .addComponent(btnThemGioHang))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(232, 232, 232)
                        .addComponent(jLabel8)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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
                .addGap(27, 27, 27)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jlabelMMH)
                    .addComponent(cbMaMatHang, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jlabelSLM)
                    .addComponent(txtSoLuong, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnThemGioHang)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel8)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 176, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, 28, Short.MAX_VALUE)
                    .addComponent(dateChooser, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(inputThanhTien, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(49, 49, 49)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnLuu)
                    .addComponent(btnThoat))
                .addGap(35, 35, 35))
        );

        jtitle.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jtitle.setText("HÓA ĐƠN BÁN HÀNG");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(122, Short.MAX_VALUE)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap(122, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jtitle)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(40, 40, 40)
                .addComponent(jtitle, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(55, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtSoLuongActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtSoLuongActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtSoLuongActionPerformed

    private void txtSoLuongKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSoLuongKeyPressed

    }//GEN-LAST:event_txtSoLuongKeyPressed

    private void txtSoLuongInputMethodTextChanged(java.awt.event.InputMethodEvent evt) {//GEN-FIRST:event_txtSoLuongInputMethodTextChanged
        System.out.println(this.txtSoLuong.getText());
    }//GEN-LAST:event_txtSoLuongInputMethodTextChanged

    private void txtSoLuongKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSoLuongKeyReleased
        // TODO add your handling code here:
        if (this.txtSoLuong.getText().length() > 0 && Integer.parseInt(this.txtSoLuong.getText()) > 0) {
            this.btnThemGioHang.setEnabled(true);
        } else {
            this.btnThemGioHang.setEnabled(false);
        }
    }//GEN-LAST:event_txtSoLuongKeyReleased

    private void btnLuuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLuuActionPerformed
        // TODO add your handling code here:
        this.btnLuu_Click();
    }//GEN-LAST:event_btnLuuActionPerformed

    private void btnThoatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnThoatActionPerformed
        // TODO add your handling code here:
        this.btnThoat_Click();
    }//GEN-LAST:event_btnThoatActionPerformed

    private void btnThemGioHangActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnThemGioHangActionPerformed
        // TODO add your handling code here:
        this.btnThemGioHang_Click();
        this.tinhTongTien();
    }//GEN-LAST:event_btnThemGioHangActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnLuu;
    private javax.swing.JButton btnThemGioHang;
    private javax.swing.JButton btnThoat;
    private javax.swing.JComboBox<String> cbMaKhachHang;
    private javax.swing.JComboBox<String> cbMaMatHang;
    private com.toedter.calendar.JDateChooser dateChooser;
    private javax.swing.JTextField inputThanhTien;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel jlabelMMH;
    private javax.swing.JLabel jlabelSLM;
    private javax.swing.JLabel jtitle;
    private javax.swing.JTable tableGioHang;
    private javax.swing.JTextField txtMaHoaDon;
    private javax.swing.JTextField txtSoLuong;
    // End of variables declaration//GEN-END:variables
}
