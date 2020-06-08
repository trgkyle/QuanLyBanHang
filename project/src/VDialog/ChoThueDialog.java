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
import com.toedter.calendar.JDateChooser;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JFrame;
import javax.swing.JRootPane;
import javax.swing.SwingUtilities;

/**
 *
 * @author quang
 */
public class ChoThueDialog extends javax.swing.JDialog {

    /**
     * Creates new form ChoThueDialog
     */
    
    private HoaDon hoaDon;
    
    private boolean isChinhSua;
    private DanhSachKhachHang danhSachKhachHang;
    private DanhSachMatHang danhSachMatHang;
    private HoaDonDAO hoaDonDAO;
    
    
    private void prepareDialog() {
        
        // set the txtMaHoaDon
        txtMaHoaDon.setText(getMaHoaDonMoi());
        txtMaHoaDon.setEditable(false);
        if (isChinhSua) txtMaHoaDon.setText(hoaDon.getMaHoaDon());
        
        // set the comboBox khachHang
        for (KhachHang khachHang : danhSachKhachHang.getAll())
            cbMaKhachHang.addItem(String.format("[%s] %s", khachHang.getMaKH(), khachHang.getHoTen()));
        if (isChinhSua)
            cbMaKhachHang.setSelectedItem(String.format("[%s] %s",
                    hoaDon.getKhachHang().getMaKH(),
                    hoaDon.getKhachHang().getHoTen()));
        
        // set the comboBox matHang
        for (MatHang matHang : danhSachMatHang.getAll())
            if (matHang.getSoLuongTon() > 0)
                cbMaMatHang.addItem(String.format("[%s] %s", matHang.getMaMatHang(), matHang.getTenMatHang()));

        if (isChinhSua) {
            cbMaMatHang.addItem(String.format("[%s] %s",
                    hoaDon.getMatHang().getMaMatHang(), hoaDon.getMatHang().getTenMatHang()));

            cbMaMatHang.setSelectedItem(String.format("[%s] %s",
                    hoaDon.getMatHang().getMaMatHang(), hoaDon.getMatHang().getTenMatHang()));
        }
        
        

        if (isChinhSua) {
            dateChooser.setDate(hoaDon.getNgayLap());
        }
        else {
            dateChooser.setDate(new java.util.Date());
        }
        
        
        //set the soLuong
        if (isChinhSua) txtSoLuong.setText(String.valueOf(hoaDon.getSoLuong()));
        
        
         
        
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
            return "HD00001";
        }

        // generate mã
        Pattern pattern = Pattern.compile(PatternRegexs.REGEX_MAHOADON);
        Matcher matcher = pattern.matcher(lastID);
        if (matcher.find()) {
            int number = Integer.parseInt(matcher.group(1));
            number++;

            newID = String.format("HD%05d", number);
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
                ChoThueDialog.this.dispose();
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

                // lấy dữ liệu băng đĩa
                pattern = Pattern.compile("(MH\\d.*)]", Pattern.MULTILINE);
                matcher = pattern.matcher(String.valueOf(cbMaMatHang.getSelectedItem()));

                if (matcher.find())
                    matHang = danhSachMatHang.getAll().get(danhSachMatHang.tim(matcher.group(1)));

                // lấy dữ liệu khách hàng
                pattern = Pattern.compile("(KH\\d.*)]", Pattern.MULTILINE);
                matcher = pattern.matcher(cbMaKhachHang.getSelectedItem().toString());

                if (matcher.find())
                    khachHang = danhSachKhachHang.getAll().get(danhSachKhachHang.tim(matcher.group(1)));

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

    
    
    
    public ChoThueDialog(JFrame frame, HoaDon hoaDon) throws Exception {
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

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
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
        btnThoat = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jLabel1.setText("Mã Hoá Đơn");

        jLabel2.setText("Mã Khách Hàng");

        jLabel3.setText("Mã Mặt Hàng");

        jLabel4.setText("Ngày Thuê");

        jLabel5.setText("Số Lượng Thuê");

        btnLuu.setText("Lưu");

        btnThoat.setText("Thoát");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(140, 140, 140)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btnLuu, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnThoat, javax.swing.GroupLayout.PREFERRED_SIZE, 141, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addComponent(jLabel1)
                            .addComponent(jLabel3)
                            .addComponent(jLabel4)
                            .addComponent(jLabel5))
                        .addGap(52, 52, 52)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtSoLuong)
                            .addComponent(txtMaHoaDon)
                            .addComponent(cbMaKhachHang, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(cbMaMatHang, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(dateChooser, javax.swing.GroupLayout.DEFAULT_SIZE, 174, Short.MAX_VALUE))))
                .addContainerGap(127, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(51, 51, 51)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(txtMaHoaDon, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(24, 24, 24)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(cbMaKhachHang, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(15, 15, 15)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel3)
                    .addComponent(cbMaMatHang, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(32, 32, 32)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel4)
                    .addComponent(dateChooser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(35, 35, 35)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(txtSoLuong, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(51, 51, 51)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnLuu)
                    .addComponent(btnThoat))
                .addContainerGap(66, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnLuu;
    private javax.swing.JButton btnThoat;
    private javax.swing.JComboBox<String> cbMaKhachHang;
    private javax.swing.JComboBox<String> cbMaMatHang;
    private com.toedter.calendar.JDateChooser dateChooser;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JTextField txtMaHoaDon;
    private javax.swing.JTextField txtSoLuong;
    // End of variables declaration//GEN-END:variables
}