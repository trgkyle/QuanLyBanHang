/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import java.util.ArrayList;

/**
 *
 * @author quang
 */
public class ChiTietHoaDon {
    private ArrayList<MatHangHoaDon> matHang;
   // private int soNgayDuocMuon;
    private int tinhTrang;  // true: đã trả, false: đang thuê
    
    public int isTinhTrang() {
        return tinhTrang;
    }

    public void setTinhTrang(int tinhTrang) {
        this.tinhTrang = tinhTrang;
    }

    public ArrayList<MatHangHoaDon> getMatHang() {
        return matHang;
    }

    public void setMatHang(ArrayList<MatHangHoaDon> matHang) {
        this.matHang = matHang;
    }

    public ChiTietHoaDon() {
    }

    public ChiTietHoaDon(ArrayList<MatHangHoaDon> matHang) {
        this.matHang = matHang;
       
        this.tinhTrang = 0;
    }

    public ChiTietHoaDon(ArrayList<MatHangHoaDon> matHang, int tinhTrang) {
        this.matHang = matHang;
       
        this.tinhTrang = tinhTrang;
    }

    @Override
    public String toString() {
        return "ChiTietHoaDon{" + "matHang=" + matHang + ", tinhTrang=" + tinhTrang + '}';
    }


   
}
