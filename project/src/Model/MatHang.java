/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import java.util.Objects;

/**
 *
 * @author quang
 */
public class MatHang {
    private String maMatHang;
    private String tenMatHang;
    private String theLoai;
    private boolean tinhTrang;
    private String hangSanXuat;
    private String ghiChu;
    private Double donGia;
    private int soLuongTon;

    public int getSoLuongTon() {
        return soLuongTon;
    }

    public void setSoLuongTon(int soLuongTon) {
        this.soLuongTon = soLuongTon;
    }

    public String getMaMatHang() {
        return maMatHang;
    }

    public void setMaMatHang(String maMatHang) {
        this.maMatHang = maMatHang;
    }

    public String getTenMatHang() {
        return tenMatHang;
    }

    public void setTenMatHang(String tenMatHang) {
        this.tenMatHang = tenMatHang;
    }

    public String getTheLoai() {
        return theLoai;
    }

    public void setTheLoai(String theLoai) {
        this.theLoai = theLoai;
    }

    public boolean isTinhTrang() {
        return tinhTrang;
    }

    public void setTinhTrang(boolean tinhTrang) {
        this.tinhTrang = tinhTrang;
    }

    public String getHangSanXuat() {
        return hangSanXuat;
    }

    public void setHangSanXuat(String hangSanXuat) {
        this.hangSanXuat = hangSanXuat;
    }

    public String getGhiChu() {
        return ghiChu;
    }

    public void setGhiChu(String ghiChu) {
        this.ghiChu = ghiChu;
    }

    public Double getDonGia() {
        return donGia;
    }

    public void setDonGia(Double donGia) {
        this.donGia = donGia;
    }

    public MatHang() {
    }

    
    public MatHang(String maMatHang, String tenMatHang, int soLuongTon) {
        this.maMatHang = maMatHang;
        this.tenMatHang = tenMatHang;
        this.soLuongTon = soLuongTon;
    }
    
    public MatHang(String maMatHang, String tenMatHang, int soLuongTon, double donGia) {
        this.maMatHang = maMatHang;
        this.tenMatHang = tenMatHang;
        this.soLuongTon = soLuongTon;
        this.donGia = donGia;
    }

    public MatHang(String maMatHang, String tenMatHang, String theLoai, boolean tinhTrang, String hangSanXuat, String ghiChu, Double donGia, int soLuongTon) {
        this.maMatHang = maMatHang;
        this.tenMatHang = tenMatHang;
        this.theLoai = theLoai;
        this.tinhTrang = tinhTrang;
        this.hangSanXuat = hangSanXuat;
        this.ghiChu = ghiChu;
        this.donGia = donGia;
        this.soLuongTon = soLuongTon;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MatHang matHang = (MatHang) o;
        return maMatHang == matHang.maMatHang;
    }

    @Override
    public int hashCode() {
        return Objects.hash(maMatHang);
    }

    @Override
    public String toString() {
        return "MatHang{" +
                "maMatHang='" + maMatHang + '\'' +
                ", tenMatHang='" + tenMatHang + '\'' +
                ", theLoai='" + theLoai + '\'' +
                ", tinhTrang=" + tinhTrang +
                ", hangSanXuat='" + hangSanXuat + '\'' +
                ", ghiChu='" + ghiChu + '\'' +
                ", donGia=" + donGia +
                ", soLuongTon=" + soLuongTon +
                '}';
    }
}