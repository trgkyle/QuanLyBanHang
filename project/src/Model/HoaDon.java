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
import java.sql.Date;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class HoaDon extends ChiTietHoaDon {
    private String maHoaDon;
    private KhachHang khachHang;
    private Date ngayLap;
    

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



    public double thanhTien() {
        double tongTien = getSoLuong() * getMatHang().getDonGia();

       

        return tongTien;
    }

    public HoaDon() {
    }

    public HoaDon(MatHang matHang, int soLuong) {
        super(matHang, soLuong);
    }

    public HoaDon(MatHang matHang, int soLuong, String maHoaDon, KhachHang khachHang, Date ngayLap) {
        super(matHang, soLuong);
        this.maHoaDon = maHoaDon;
        this.khachHang = khachHang;
        this.ngayLap = ngayLap;
    }

    public HoaDon(MatHang matHang, int soLuong, String maHoaDon, KhachHang khachHang, Date ngayLap, boolean tinhTrang) {
        super(matHang, soLuong, tinhTrang);
        this.maHoaDon = maHoaDon;
        this.khachHang = khachHang;
        this.ngayLap = ngayLap;
    }

    public HoaDon(MatHang matHang, int soLuong, String maHoaDon, KhachHang khachHang) {
        super(matHang, soLuong);
        this.maHoaDon = maHoaDon;
        this.khachHang = khachHang;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HoaDon hoaDon = (HoaDon) o;
        return maHoaDon == hoaDon.maHoaDon;
    }

    @Override
    public int hashCode() {
        return Objects.hash(maHoaDon);
    }

    @Override
    public String toString() {
        return "HoaDon{" +
                "maHoaDon='" + maHoaDon + '\'' +
                ", khachHang=" + khachHang +
                ", ngayLap=" + ngayLap +
                "} " + super.toString();
    }
}
