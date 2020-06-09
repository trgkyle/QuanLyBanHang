/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import DAO.NhaCungCapDAO;
import java.util.ArrayList;

/**
 *
 * @author quang
 */
public class  DanhSachNhaCungCap{
    private ArrayList<NhaCungCap> nhaCungCaps;
    private static NhaCungCapDAO nhaCungCapDAO;

    public DanhSachNhaCungCap() throws Exception {
        nhaCungCapDAO = NhaCungCapDAO.getInstance();
        loadData();
    }


    /**
     * Lấy danh sách nhà cung cấp
     *
     * @return
     */
    public ArrayList<NhaCungCap> getAll() {
        return nhaCungCaps;
    }


    /**
     * Load danh sách nhà cung cấp từ DB lên
     *
     * @throws Exception
     */
    public void loadData() throws Exception {
        nhaCungCaps = nhaCungCapDAO.getNhaCungCaps();
    }


    /**
     * Thêm nhà cung cấp mới (không cho thêm trùng mã nhà cung cấp)
     * Lưu nhà cung cấp vào DB
     *
     * @param khachHang
     * @return
     * @throws Exception
     */
    public void them(NhaCungCap nhaCungCap) throws Exception {
        // kiểm tra trùng mã khách hàng
        if (nhaCungCap == null && nhaCungCaps.contains(nhaCungCap))
            throw new Exception("Đã có nhà cung cấp này trong hệ thống");

        nhaCungCaps.add(nhaCungCapDAO.themNhaCungCap(nhaCungCap));
    }


    /**
     * Xoá nhà cung cấp
     * Xoá nhà cung cấp tương ứng trong DB
     *
     * @param maNhaCungCap
     * @return
     * @throws Exception
     */
    public boolean xoa(String maNhaCungCap) throws Exception {
        NhaCungCap nhaCungCap = nhaCungCaps.get(tim(maNhaCungCap));

        if (nhaCungCap == null)
            return false;

        return nhaCungCapDAO.xoaNhaCungCap(maNhaCungCap) && nhaCungCaps.remove(nhaCungCap);
    }


    /**
     * Cập nhật thông tin nhà cung cấp
     * Cập nhật thông tin nhà cung cấp tương ứng trong DB
     *
     * @param nhaCungCap
     * @return
     * @throws Exception
     */
    public boolean sua(NhaCungCap nhaCungCap) throws Exception {
        return nhaCungCaps.set(tim(nhaCungCap.getMaNCC()), nhaCungCapDAO.suaNhaCungCap(nhaCungCap)) != null;
    }


    /**
     * Tìm vị trí của nhà cung cấp trong danh sách
     *
     * @param maNhaCungCap
     * @return
     */
    public int tim(String maNhaCungCap) {
        for (int i = 0; i < nhaCungCaps.size(); i++)
            if (nhaCungCaps.get(i).getMaNCC().equals(maNhaCungCap))
                return i;

        return -1;
    }
}
