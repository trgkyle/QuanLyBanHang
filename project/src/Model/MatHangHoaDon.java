/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import java.util.Objects;

/**
 *
 * @author s2hdp
 */
public class MatHangHoaDon extends MatHang {
    private int soLuong;

    public int getSoLuong() {
        return soLuong;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 41 * hash + Objects.hashCode(this.getTenMatHang());
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final MatHangHoaDon other = (MatHangHoaDon) obj;
        if (!Objects.equals(this.getTenMatHang(), other.getTenMatHang())) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "MatHangHoaDon{" + "soLuong=" + soLuong + '}';
    }



    public MatHangHoaDon(MatHang matHang, int soLuong) {
        super(matHang.getMaMatHang(),matHang.getTenMatHang(),matHang.getTenNhaCungCap(), matHang.getTheLoai(),matHang.isTinhTrang(),matHang.getMaNhaCungCap(), matHang.getGhiChu(), matHang.getDonGia(), matHang.getSoLuongTon());
        this.soLuong = soLuong;
    }
    
    public MatHangHoaDon(String maMatHang, int soLuong) {
        super(maMatHang);
        this.soLuong = soLuong;
    }
    
    
    public void setSoLuong(int soLuong) {
        this.soLuong = soLuong;
    }
}
