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
    
    private String ghiChu;
    private Double donGia;
    private int soLuongTon;
    private String tenNhaCungCap;
    private String maNhaCungCap;

    public int getSoLuongTon() {
        return soLuongTon;
    }

    public String getTenNhaCungCap() {
        return tenNhaCungCap;
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

    

    public String getGhiChu() {
        return ghiChu;
    }

    public void setGhiChu(String ghiChu) {
        this.ghiChu = ghiChu;
    }

    public Double getDonGia() {
        return donGia;
    }

    public String getMaNhaCungCap() {
        return maNhaCungCap;
    }

    public void setDonGia(Double donGia) {
        this.donGia = donGia;
    }

    public MatHang() {
    }
    
    public MatHang(String maMatHang) {
        this.maMatHang = maMatHang;
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

    public MatHang(String maMatHang, String tenMatHang, String theLoai, boolean tinhTrang, String maNhaCungCap, String ghiChu, Double donGia, int soLuongTon) {
        this.maMatHang = maMatHang;
        this.tenMatHang = tenMatHang;
        this.theLoai = theLoai;
        this.tinhTrang = tinhTrang;
        this.maNhaCungCap = maNhaCungCap;
        this.ghiChu = ghiChu;
        this.donGia = donGia;
        this.soLuongTon = soLuongTon;
    }
    
    public MatHang(String maMatHang, String tenMatHang,String tenNhacc, String theLoai, boolean tinhTrang, String maNhaCungCap, String ghiChu, Double donGia, int soLuongTon) {
        this.maMatHang = maMatHang;
        this.tenMatHang = tenMatHang;
        this.theLoai = theLoai;
        this.tinhTrang = tinhTrang;
        this.maNhaCungCap = maNhaCungCap;
        this.ghiChu = ghiChu;
        this.donGia = donGia;
        this.soLuongTon = soLuongTon;
        this.tenNhaCungCap = tenNhacc;
    }
    
    public MatHang(String maMatHang,String maNhaCungCap, String tenMatHang,String tenNhacc, String theLoai, boolean tinhTrang,  String ghiChu, Double donGia, int soLuongTon) {
        this.maNhaCungCap = maNhaCungCap;
        this.maMatHang = maMatHang;
        this.tenMatHang = tenMatHang;
        this.theLoai = theLoai;
        this.tinhTrang = tinhTrang;
        //this.hangSanXuat = hangSanXuat;
        this.ghiChu = ghiChu;
        this.donGia = donGia;
        this.soLuongTon = soLuongTon;
        this.tenNhaCungCap = tenNhacc;
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
                ", hangSanXuat='" + maNhaCungCap + '\'' +
                ", ghiChu='" + ghiChu + '\'' +
                ", donGia=" + donGia +
                ", soLuongTon=" + soLuongTon +
                '}';
    }
}