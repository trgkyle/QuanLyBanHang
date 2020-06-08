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
import DAO.MatHangDAO;


public class DanhSachMatHang {
    private ArrayList<MatHang> matHangs = null;
    private static MatHangDAO matHangDAO;

    public DanhSachMatHang() throws Exception {
        matHangDAO = MatHangDAO.getInstance();
        loadData();
    }

    /**
     * Load dữ liệu từ database
     *
     * @throws Exception
     */
    public void loadData() throws Exception {
        matHangs = matHangDAO.getMatHangs();
    }


    /**
     * Lấy danh sách mặt hàng
     *
     * @return
     */
    public ArrayList<MatHang> getAll() {
        return matHangs;
    }
    
    
    /**
     * Lấy danh sách mặt hàng chưa hỏng
     *
     * @return
     */
    public ArrayList<MatHang> getAllNew() {
        
        matHangs.add(matHangs.get(timMatHangChuaHong()));
        
        return matHangs;
    }

    

    /**
     * Thêm 1 mặt hàng mới (không cho thêm mặt hàng bị trùng mã)
     * Lưu mặt hàng vào DB
     *
     * @param matHang
     * @return
     * @throws Exception
     */
    public boolean them(MatHang matHang) throws Exception {
        if (matHang == null || matHangs.contains(matHang))
            return false;

        return (matHangs.add(matHangDAO.themMatHang(matHang)));
    }


    /**
     * Xoá mặt hàng bằng mã mặt hàng
     * Xoá mặt hàng tương ứng trong DB
     *
     * @param maMatHang
     * @return
     * @throws Exception
     */
    public boolean xoa(String maMatHang) throws Exception {
        MatHang matHang = matHangs.get(tim(maMatHang));

        if (matHang == null)
            return false;

        return (matHangDAO.xoaMatHang(maMatHang) && matHangs.remove(matHang));
    }


    /**
     * Cập nhật thông tin mặt hàng
     * Cập nhật thông tin mặt hàng tương ứng trong DB
     *
     * @param matHang
     * @return
     * @throws Exception
     */
    public boolean sua(MatHang matHang) throws Exception {
        return matHangs.set(tim(matHang.getMaMatHang()), matHangDAO.suaMatHang(matHang)) != null;
    }


    /**
     * Tìm vị trí của mặt hàng
     *
     * @param maMatHang
     * @return
     */
    public int tim(String maMatHang) {
        for (int i = 0; i < matHangs.size(); i++)
            if (matHangs.get(i).getMaMatHang().equals(maMatHang))
                return i;

        return -1;
    }
    
    
    
    /**
     * Tìm vị trí của mặt hàng chưa hỏng
     *
     * @param tinhTrang
     * @return
     */
    public int timMatHangChuaHong() {
        for (int i = 0; i < matHangs.size(); i++)
            if (matHangs.get(i).isTinhTrang() == true)
                return i;

        return -1;
    }
    
    
    


    /**
     * Xoá mặt hàng  hỏng trong danh sách
     * Xoá mặt hàng tương ứng trong DB
     */
    public void xoaMatHangHong() {
        ArrayList<String> dsXoa = new ArrayList<>();

        for (MatHang matHang : matHangs) {
            if (!matHang.isTinhTrang()) {
                dsXoa.add(matHang.getMaMatHang());
            }
        }

        for (String maMatHang : dsXoa) {
            try {
                xoa(maMatHang);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * Lấy tổng số mặt hàng tồn trong kho
     *
     * @return
     */
    public int tongSoMatHangTon() {
        int tong = 0;

        for (MatHang matHang : matHangs)
            tong += matHang.getSoLuongTon();

        return tong;
    }


    /**
     * Lấy tổng số mặt hàng hỏng
     *
     * @return
     */
    public int tongSoMatHangHong() {
        int count = 0;

        for (MatHang matHang : matHangs)
            if (!matHang.isTinhTrang())
                count++;

        return count;
    }
}
