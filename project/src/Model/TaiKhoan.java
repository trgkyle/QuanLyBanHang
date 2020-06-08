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
public class TaiKhoan {
     private String tenTaiKhoan;
    private String matKhau;
    private int loaiTaiKhoan;
    private String maNhanVien;

    public String getTenTaiKhoan() {
        return tenTaiKhoan;
    }

    public void setTenTaiKhoan(String tenTaiKhoan) {
        this.tenTaiKhoan = tenTaiKhoan;
    }

    public String getMatKhau() {
        return matKhau;
    }

    public void setMatKhau(String matKhau) {
        this.matKhau = matKhau;
    }

    public int getLoaiTaiKhoan() {
        return loaiTaiKhoan;
    }

    public void setLoaiTaiKhoan(int loaiTaiKhoan) {
        this.loaiTaiKhoan = loaiTaiKhoan;
    }

    public String getMaNhanVien() {
        return maNhanVien;
    }

    public void setMaNhanVien(String maNhanVien) {
        this.maNhanVien = maNhanVien;
    }

    public TaiKhoan() {
    }

    public TaiKhoan(String tenTaiKhoan, String matKhau, int loaiTaiKhoan, String maNhanVien) {//, String maNhanVien
        this.tenTaiKhoan = tenTaiKhoan;
        this.matKhau = matKhau;
        this.loaiTaiKhoan = loaiTaiKhoan;
        this.maNhanVien = maNhanVien;
    }

    @Override
    public boolean equals(Object o) {
       if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TaiKhoan taiKhoan = (TaiKhoan) o;
        return tenTaiKhoan.equals(taiKhoan.tenTaiKhoan);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tenTaiKhoan);
    }

    @Override
    public String toString() {
        return "TaiKhoan{" +
                "tenTaiKhoan='" + tenTaiKhoan + '\'' +
                ", matKhau='" + matKhau + '\'' +
                ", loaiTaiKhoan=" + loaiTaiKhoan +
                ", maNhanVien=" + maNhanVien +
                '}';
    }
}
