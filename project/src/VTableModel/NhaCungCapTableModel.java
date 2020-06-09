/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package VTableModel;

import Model.NhaCungCap;
import Utils.Formats;
import java.util.ArrayList;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author quang
 */


public class NhaCungCapTableModel extends AbstractTableModel {
    private ArrayList<NhaCungCap> nhaCungCaps;

    private final String[] columnNames = new String[]{
            "Mã NCC", "Tên NCC", "Địa chỉ", "Số điện thoại"};

    public void setModel(ArrayList<NhaCungCap> nhaCungCaps) {
        this.nhaCungCaps = nhaCungCaps;
    }

    public NhaCungCapTableModel(ArrayList<NhaCungCap> nhaCungCaps) {
        this.nhaCungCaps = nhaCungCaps;
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
        return nhaCungCaps.size();
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        try {
            NhaCungCap row = nhaCungCaps.get(rowIndex);

            switch (columnIndex) {
                case 0:
                    return row.getMaNCC();
                case 1:
                    return row.getTenNCC();
                case 2:
                    return row.getDiaChi();
                case 3:
                    return row.getSoDienThoai();
            }
        } catch (Exception e) {
        }


        return null;
    }
}
