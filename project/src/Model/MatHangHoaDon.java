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
    private KhachHang khachHang;
    public int getSoLuong() {
        return soLuong;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 41 * hash + Objects.hashCode(this.khachHang);
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
        if (!Objects.equals(this.khachHang, other.khachHang)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "MatHangHoaDon{" + "soLuong=" + soLuong + ", khachHang=" + khachHang + '}';
    }

    public MatHangHoaDon(MatHang matHang, int soLuong, KhachHang khachHang) {
        super(matHang.getMaMatHang(), matHang.getTenMatHang(), matHang.getSoLuongTon(),matHang.getDonGia());
        this.soLuong = soLuong;
        this.khachHang = khachHang;
    }

    public KhachHang getKhachHang() {
        return khachHang;
    }
    
    public void setSoLuong(int soLuong) {
        this.soLuong = soLuong;
    }
}
