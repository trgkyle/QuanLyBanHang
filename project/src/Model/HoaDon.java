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
    private String MaKhachHang;

    public void setMaKhachHang(String MaKhachHang) {
        this.MaKhachHang = MaKhachHang;
    }

    public String getMaKhachHang() {
        return MaKhachHang;
    }

    public void setTinhTrang(int tinhTrang) {
        this.tinhTrang = tinhTrang;
    }

    public int getTinhTrang() {
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
        DecimalFormat dcf = new DecimalFormat("###,###,###,###.##");
        return Double.parseDouble(dcf.format(tongTien));
    }

    public HoaDon() {
    }

    public HoaDon(ArrayList<MatHangHoaDon> matHang, String maHoaDon, KhachHang khachHang, Date ngayLap) {
        super(matHang, 0);
        this.maHoaDon = maHoaDon;
        this.khachHang = khachHang;
        this.ngayLap = ngayLap;
    }

    public HoaDon(ArrayList<MatHangHoaDon> matHang, String maHoaDon, KhachHang khachHang, Date ngayLap, int tinhTrang) {
        super(matHang, tinhTrang);
        this.maHoaDon = maHoaDon;
        this.khachHang = khachHang;
        this.ngayLap = ngayLap;
    }

    public HoaDon(ArrayList<MatHangHoaDon> matHang, int soLuong, String maHoaDon, KhachHang khachHang) {
        super(matHang, soLuong);
        this.maHoaDon = maHoaDon;
        this.khachHang = khachHang;
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
