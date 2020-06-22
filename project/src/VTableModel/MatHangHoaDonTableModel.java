/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package VTableModel;

import Model.MatHangHoaDon;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author s2hdp
 */
public class MatHangHoaDonTableModel extends AbstractTableModel {

    private ArrayList<MatHangHoaDon> matHangs;

    private final String[] columnNames = new String[]{
        "Tên mặt hàng", "Mã nhà cung cấp", "Số lượng"
    };

    public void setModel(ArrayList<MatHangHoaDon> matHangs) {
        this.matHangs = matHangs;
    }

    public MatHangHoaDonTableModel(ArrayList<MatHangHoaDon> matHangs) {
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
        MatHangHoaDon matHang;

        if (rowIndex > getRowCount()) {
            return null;
        }

        matHang = matHangs.get(rowIndex);

        switch (columnIndex) {
            case 0:
                return matHang.getTenMatHang();
            case 1:
                return matHang.getMaNhaCungCap();
            case 2:
                return matHang.getSoLuong();
        }

        return null;
    }
}
