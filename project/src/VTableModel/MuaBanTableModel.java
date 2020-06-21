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

public class MuaBanTableModel extends AbstractTableModel {
    private ArrayList<HoaDon> hoaDons;

    private final String[] columnNames = new String[]{
            "Mã hoá đơn", "Tên khách hàng", "Số lượng", "Ngày mua", "Thành tiền", "Tình trạng"
    };

    public void setModel(ArrayList<HoaDon> hoaDons) {
        this.hoaDons = hoaDons;
    }

    public MuaBanTableModel(ArrayList<HoaDon> hoaDons) {
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
                    return hoaDon.tinhTongSoLuong();
                case 3:
                    return Formats.DATE_FORMAT.format(hoaDon.getNgayLap());
                case 4:
                    Locale locale = new Locale("vi", "VN");
                    NumberFormat numberFormat = NumberFormat.getCurrencyInstance(locale);
                    return numberFormat.format(hoaDon.thanhTien());
                case 5:
                    return hoaDon.isTinhTrang() == 1 ? "Đã thanh toán" : hoaDon.isTinhTrang() == 0 ? "Chưa thanh toán": "Trả hàng";
            }
        } catch (Exception e) {

        }

        return null;
    }
}
