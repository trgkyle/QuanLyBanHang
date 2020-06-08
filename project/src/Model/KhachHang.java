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

public class KhachHang  extends ThongTinCaNhan{
    private String maKH;
    private Date ngayThamGia;

    public String getMaKH() {
        return maKH;
    }

    public void setMaKH(String maKH) {
        this.maKH = maKH;
    }

    public Date getNgayThamGia() {
        return ngayThamGia;
    }

    public void setNgayThamGia(Date ngayThamGia) {
        this.ngayThamGia = ngayThamGia;
    }

    

    public KhachHang() {
    }

    public KhachHang(String cMND, String hoTen, boolean gioiTinh, String soDienThoai, String diaChi, Date ngaySinh) {
        super(cMND, hoTen, gioiTinh, soDienThoai, diaChi, ngaySinh);
    }

    public KhachHang(String cMND, String hoTen, boolean gioiTinh, String soDienThoai, String diaChi, Date ngaySinh, String maKH, Date ngayThamGia) {
        super(cMND, hoTen, gioiTinh, soDienThoai, diaChi, ngaySinh);
        this.maKH = maKH;
        this.ngayThamGia = ngayThamGia;
    }

    public KhachHang(String cMND, String hoTen, boolean gioiTinh, String soDienThoai, String diaChi, Date ngaySinh, String maKH) {
        super(cMND, hoTen, gioiTinh, soDienThoai, diaChi, ngaySinh);
        this.maKH = maKH;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        KhachHang khachHang = (KhachHang) o;
        return maKH.equals(khachHang.maKH);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), maKH);
    }

    @Override
    public String toString() {
        return "KhachHang{" +
                "maKH=" + maKH +
                ", ngayThamGia=" + ngayThamGia +
                "} " + super.toString();
    }
}
