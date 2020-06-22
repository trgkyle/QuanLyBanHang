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
       
    }

    @Override
    public String toString() {
        return "ChiTietHoaDon{" + "matHang=" + matHang + '}';
    }


   
}
