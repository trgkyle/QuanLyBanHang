/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAO;

import Model.MatHang;
import Model.MatHangHoaDon;
import java.sql.ResultSet;
import java.util.ArrayList;

/**
 *
 * @author s2hdp
 */
public class MatHangHoaDonDAO {

    private static MatHangHoaDonDAO _instance;
    private static DataBaseUtils dataBaseUtils;
    private static MatHangDAO matHangDAO;
    private ResultSet resultSet;
    
    public MatHangHoaDonDAO() throws Exception {
        matHangDAO = new MatHangDAO();
        dataBaseUtils = DataBaseUtils.getInstance();
    }

    /**
     *
     * @return @throws Exception
     */
    public static MatHangHoaDonDAO getInstance() throws Exception {
        if (_instance == null) {
            synchronized (MatHangHoaDonDAO.class) {
                if (null == _instance) {
                    _instance = new MatHangHoaDonDAO();
                }
            }
        }
        return _instance;
    }

    /**
     * Đọc danh sách mặt hàng từ DB
     *
     * @return
     * @throws Exception
     */
    public ArrayList<MatHangHoaDon> getMatHangs(String maHoaDon) throws Exception {
        ArrayList<MatHangHoaDon> matHangs = new ArrayList<MatHangHoaDon>();
        String sql = String.format("SELECT * FROM CHITIETHOADON WHERE MAHD = '%s'", maHoaDon);
        try {
            resultSet = dataBaseUtils.excuteQueryRead(sql);

            while (resultSet.next()) {
                MatHangHoaDon matHangHoaDon = new MatHangHoaDon(matHangDAO.getMatHangsByMMH(resultSet.getString("MAMH")),Integer.parseInt(resultSet.getString("SOLUONG")));
                matHangs.add(matHangHoaDon);
            }

        } catch (Exception e) {
            throw new Exception("Đọc danh sách mặt hàng lỗi");
        } finally {
            resultSet.close();
        }
        
        return matHangs;
    }
}
