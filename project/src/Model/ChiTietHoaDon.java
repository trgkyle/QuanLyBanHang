/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

/**
 *
 * @author quang
 */
public class ChiTietHoaDon {
    private MatHang matHang;
   // private int soNgayDuocMuon;
    private int soLuong;
    private boolean tinhTrang;  // true: đã trả, false: đang thuê

    public boolean isTinhTrang() {
        return tinhTrang;
    }

    public void setTinhTrang(boolean tinhTrang) {
        this.tinhTrang = tinhTrang;
    }

    public MatHang getMatHang() {
        return matHang;
    }

    public void setMatHang(MatHang matHang) {
        this.matHang = matHang;
    }



    public int getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(int soLuong) {
        this.soLuong = soLuong;
    }

    public ChiTietHoaDon() {
    }

    public ChiTietHoaDon(MatHang matHang, int soLuong) {
        this.matHang = matHang;
       
        this.soLuong = soLuong;
        this.tinhTrang = false;
    }

    public ChiTietHoaDon(MatHang matHang, int soLuong, boolean tinhTrang) {
        this.matHang = matHang;
       
        this.soLuong = soLuong;
        this.tinhTrang = tinhTrang;
    }

    @Override
    public String toString() {
        return "ChiTietHoaDon{" +
                "MatHang=" + matHang +
               
                ", soLuong=" + soLuong +
                ", tinhTrang=" + tinhTrang +
                '}';
    }
}
