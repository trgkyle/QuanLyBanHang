/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package VTableModel;

/**
 *
 * @author quang
 */
import Model.HoaDon;
import Utils.Formats;
import javax.swing.table.AbstractTableModel;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class ChoThueTableModel extends AbstractTableModel {
    private ArrayList<HoaDon> hoaDons;

    private final String[] columnNames = new String[]{
            "Mã hoá đơn", "Tên khách hàng", "Tên mặt hàng", "Số lượng", "Ngày mua", "Thành tiền", "Tình trạng"
    };

    public void setModel(ArrayList<HoaDon> hoaDons) {
        this.hoaDons = hoaDons;
    }

    public ChoThueTableModel(ArrayList<HoaDon> hoaDons) {
        this.hoaDons = hoaDons;
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public int getRowCount() {
        return hoaDons.size();
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        try {
            HoaDon hoaDon = hoaDons.get(rowIndex);

            switch (columnIndex) {
                case 0:
                    return hoaDon.getMaHoaDon();
                case 1:
                    return hoaDon.getKhachHang().getHoTen();
                case 2:
                    return hoaDon.getMatHang().getTenMatHang();
                case 3:
                    return hoaDon.getSoLuong();
                case 4:
                    return Formats.DATE_FORMAT.format(hoaDon.getNgayLap());
                case 5:
                    Locale locale = new Locale("vi", "VN");
                    NumberFormat numberFormat = NumberFormat.getCurrencyInstance(locale);
                    return numberFormat.format(hoaDon.thanhTien());
                case 6:
                    return hoaDon.isTinhTrang() ? "Đã thanh toán" : "Chưa thanh toán";
            }
        } catch (Exception e) {

        }

        return null;
    }
}
