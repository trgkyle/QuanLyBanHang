/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package VTableModel;

import Model.MatHang;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author quang
 */
public class MatHangTableModel extends AbstractTableModel{
    private ArrayList<MatHang> matHangs;

    private final String[] columnNames = new String[]{
            "Mã mặt hàng", "Tên mặt hàng", "Thể loại", "Tình trạng", "Đơn giá", "Số lượng tồn", "Mã Nhà Cung Cấp", "Ghi chú"
    };

    public void setModel(ArrayList<MatHang> matHangs) {
        this.matHangs = matHangs;
    }

    public MatHangTableModel(ArrayList<MatHang> matHangs) {
        this.matHangs = matHangs;
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
        return matHangs.size();
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        MatHang matHang;

        if (rowIndex > getRowCount())
            return null;

        try {
            matHang = matHangs.get(rowIndex);
        } catch (IndexOutOfBoundsException e) {
            matHangs.trimToSize();
            return null;
        }

        switch (columnIndex) {
            case 0:
                return matHang.getMaMatHang();
            case 1:
                return matHang.getTenMatHang();
            case 2:
                return matHang.getTheLoai();
            case 3:
                return matHang.isTinhTrang() ? "Mới" : "Hư hỏng";
            case 4:
                Locale locale = new Locale("vi", "VN");
                NumberFormat numberFormat = NumberFormat.getCurrencyInstance(locale);
                return numberFormat.format(matHang.getDonGia());
            case 5:
                return matHang.getSoLuongTon();
            case 6:
                return matHang.getMaNhaCungCap();
            case 7:
                return matHang.getGhiChu();
        }

        return null;
    }
}
