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
import DAO.HoaDonDAO;
import java.sql.Date;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class HoaDon extends ChiTietHoaDon {

    private String maHoaDon;
    private KhachHang khachHang;
    private Date ngayLap;
    private int tinhTrang;
    private HoaDonDAO hoaDonDAO;


    public void setTinhTrang(int tinhTrang) {
        this.tinhTrang = tinhTrang;
    }

    public int isTinhTrang() {
        return tinhTrang;
    }

    public String getMaHoaDon() {
        return maHoaDon;
    }

    public void setMaHoaDon(String maHoaDon) {
        this.maHoaDon = maHoaDon;
    }

    public KhachHang getKhachHang() {
        return khachHang;
    }

    public void setKhachHang(KhachHang khachHang) {
        this.khachHang = khachHang;
    }

    public Date getNgayLap() {
        return ngayLap;
    }

    public void setNgayLap(Date ngayLap) {
        this.ngayLap = ngayLap;
    }

    public int tinhTongSoLuong() {
        int tongSoLuong = 0;
        for (MatHangHoaDon matHang : this.getMatHang()) {
            tongSoLuong += matHang.getSoLuong();
        }
        return tongSoLuong;
    }

    public double thanhTien() {
        double tongTien = 0;
        for (MatHangHoaDon matHang : this.getMatHang()) {
            System.out.println((double) matHang.getSoLuong());
            System.out.println(matHang.getDonGia());
            tongTien += (double) matHang.getSoLuong() * matHang.getDonGia();
        }
        return tongTien;
    }

    public HoaDon() throws Exception {
        hoaDonDAO = HoaDonDAO.getInstance();
    }
    
    public HoaDon getHoaDonByMaHoaDon(String maHoaDon) throws Exception{
        HoaDon hd = hoaDonDAO.getHoaDon(maHoaDon);
        return hd;
    }

    public HoaDon(ArrayList<MatHangHoaDon> matHang, String maHoaDon, KhachHang khachHang, Date ngayLap) {
        super(matHang);
        this.maHoaDon = maHoaDon;
        this.khachHang = khachHang;
        this.ngayLap = ngayLap;
    }

    public HoaDon(ArrayList<MatHangHoaDon> matHang, String maHoaDon, KhachHang khachHang, Date ngayLap, int tinhTrang) {
        super(matHang);
        this.maHoaDon = maHoaDon;
        this.khachHang = khachHang;
        this.ngayLap = ngayLap;
        this.tinhTrang = tinhTrang;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        HoaDon hoaDon = (HoaDon) o;
        return maHoaDon == hoaDon.maHoaDon;
    }

    @Override
    public int hashCode() {
        return Objects.hash(maHoaDon);
    }

    @Override
    public String toString() {
        return "HoaDon{" + "maHoaDon=" + maHoaDon + ", khachHang=" + khachHang + ", ngayLap=" + ngayLap + '}';
    }
}
