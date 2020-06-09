
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAO;

import Model.NhaCungCap;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author quang
 */
public class NhaCungCapDAO {
    private static NhaCungCapDAO _instance;
    private static DataBaseUtils dataBaseUtils;

    public NhaCungCapDAO() throws Exception {
        dataBaseUtils = DataBaseUtils.getInstance();
    }


    /**
     * Design Pattern: Singleton
     *
     * @return
     * @throws Exception
     */
    public static NhaCungCapDAO getInstance() throws Exception {
        if (_instance == null) {
            synchronized (MatHangDAO.class) {
                if (null == _instance) {
                    _instance = new NhaCungCapDAO();
                }
            }
        }
        return _instance;
    }


    /**
     * Đọc danh sách nhà cung cấp từ DB
     *
     * @return
     * @throws Exception
     */
    public ArrayList<NhaCungCap> getNhaCungCaps() throws Exception {
        ArrayList<NhaCungCap> nhaCungCaps = new ArrayList<NhaCungCap>();
        ResultSet resultSet = null;

        final String sql = "SELECT * FROM NHACUNGCAP";

        try {
            resultSet = dataBaseUtils.excuteQueryRead(sql);

            while (resultSet.next()) {
                NhaCungCap nhaCungCap = new NhaCungCap(
                        resultSet.getString("MANCC"),
                        resultSet.getString("TENNCC"),
                        resultSet.getString("DIACHI"),
                        resultSet.getString("DIENTHOAI")
                );

                nhaCungCaps.add(nhaCungCap);
            }

        } catch (Exception e) {
            throw new Exception("Đọc danh sách nhà cung cấp lỗi");
        } finally {
            resultSet.close();
        }

        return nhaCungCaps;
    }
    
    
    public ArrayList<NhaCungCap> getNhaCungCapDeXoa() throws Exception {
        ArrayList<NhaCungCap>  nhaCungCaps= new ArrayList<NhaCungCap>();
        ResultSet resultSet = null;

        final String sql = "SELECT * FROM NHACUNGCAP";

        try {
            resultSet = dataBaseUtils.excuteQueryRead(sql);

            while (resultSet.next()) {
                NhaCungCap nhaCungCap = new NhaCungCap(
                        resultSet.getString("MANCC"),
                        resultSet.getString("TENNCC"),
                        resultSet.getString("DIACHI"),
                        resultSet.getString("DIENTHOAI")
                );

                nhaCungCaps.add(nhaCungCap);
            }

        } catch (Exception e) {
            throw new Exception("Đọc danh sách nhà cung cấp lỗi");
        } finally {
            resultSet.close();
        }

        return nhaCungCaps;
    }
    
    
    


    /**
     * Lấy nhà cung cấp từ DB
     *
     * @param maNhaCungCap
     * @return
     * @throws Exception
     */
    public NhaCungCap getNhaCungCap(String maNhaCungCap) throws Exception {
        NhaCungCap nhaCungCap = null;
        String sql = String.format("SELECT * FROM NHACUNGCAP WHERE MANCC = '%s'", maNhaCungCap);
        try (ResultSet resultSet = dataBaseUtils.excuteQueryRead(sql)) {

            while (resultSet.next()) {
                nhaCungCap = new NhaCungCap(
                        resultSet.getString("MANCC"),
                        resultSet.getString("TENNCC"),
                        resultSet.getString("DIACHI"),
                        resultSet.getString("DIENTHOAI")
                );
            }
        } catch (SQLException e) {
            throw new Exception(String.format("Đọc dữ liệu nhà cung cấp %s lỗi", nhaCungCap.getMaNCC()));
        }

        return nhaCungCap;
    }


    /**
     * Lấy mã  cuối nhà cung cấp trong DB
     * Dùng để generate mã nhà cung cấp mới
     *
     * @return
     * @throws Exception
     */
    public String getMaNhaCungCapCuoi() throws Exception {
        String sql = "SELECT TOP 1 MANCC FROM NHACUNGCAP ORDER BY MANCC DESC";
        ResultSet resultSet = null;
        String ketQua;

        try {
            resultSet = dataBaseUtils.excuteQueryRead(sql);
            resultSet.next();

            ketQua = resultSet.getString("MANCC");
        } catch (SQLException e) {
            throw new Exception("Đọc dữ liệu nhà cung cấp lỗi");
        } finally {
            resultSet.close();
        }

        return ketQua;
    }


    /**
     * Thêm nhà cung cấp mới vào DB
     *
     * @param nhaCungCap
     * @return
     * @throws Exception
     */
    public NhaCungCap themNhaCungCap(NhaCungCap nhaCungCap) throws Exception {
        String sql = "INSERT INTO NHACUNGCAP (MANCC,TENNCC,DIACHI,DIENTHOAI) VALUES (?,?,?,?)";
        PreparedStatement preparedStatement = null;
        

        if (nhaCungCap == null)
            return null;

        try {
            preparedStatement = dataBaseUtils.excuteQueryWrite(sql);

            preparedStatement.setString(1, nhaCungCap.getMaNCC());
            preparedStatement.setString(2, nhaCungCap.getTenNCC());
            preparedStatement.setString(3, nhaCungCap.getDiaChi());
            preparedStatement.setString(4, nhaCungCap.getSoDienThoai());
            

            if (preparedStatement.executeUpdate() > 0) {
                dataBaseUtils.commitQuery();
                return getNhaCungCap(nhaCungCap.getMaNCC());
            }
        } catch (Exception e) {
            dataBaseUtils.rollbackQuery();
            throw new Exception("Thêm nhà cung cấp lỗi");
        } finally {
            preparedStatement.close();
        }

        return null;
    }


    /**
     * Xoá nhà cung cấp trong DB
     *
     * @param maMatHang
     * @return
     * @throws Exception
     */
    public boolean xoaNhaCungCap(String maNhaCungCap) throws Exception {
        String sql = "DELETE FROM NHACUNGCAP WHERE MANCC = ?";
        PreparedStatement preparedStatement = null;

        if (getNhaCungCap(maNhaCungCap) == null)
            return false;

        try {
            preparedStatement = dataBaseUtils.excuteQueryWrite(sql);

            preparedStatement.setString(1, maNhaCungCap);

            if (preparedStatement.executeUpdate() > 0) {
                dataBaseUtils.commitQuery();
                return true;
            }
        } catch (Exception e) {
            dataBaseUtils.rollbackQuery();
            throw new Exception("Lỗi xoá nhà cung cấp");
        } finally {
            preparedStatement.close();
        }

        return false;
    }


    /**
     * Cập nhật thông tin nhà cung cấp trong DB
     *
     * @param matHang
     * @return
     * @throws Exception
     */
    public NhaCungCap suaNhaCungCap(NhaCungCap nhaCungCap) throws Exception {
        String sql = "UPDATE NHACUNGCAP SET " +
                "TENNCC = ?, DIACHI = ?, DIENTHOAI = ?  WHERE MANCC = ?";
        PreparedStatement preparedStatement = null;

        if (nhaCungCap == null)
            return null;

        try {
            preparedStatement = dataBaseUtils.excuteQueryWrite(sql);

            preparedStatement.setString(1, nhaCungCap.getTenNCC());
            preparedStatement.setString(2, nhaCungCap.getDiaChi());
            preparedStatement.setString(3, nhaCungCap.getSoDienThoai());
            preparedStatement.setString(4, nhaCungCap.getMaNCC());
            

            if (preparedStatement.executeUpdate() > 0) {
                dataBaseUtils.commitQuery();
                return getNhaCungCap(nhaCungCap.getMaNCC());
            }
        } catch (Exception e) {
            dataBaseUtils.rollbackQuery();
            throw new Exception("Cập nhật thông tin nhà cung cấp lỗi");
        } finally {
            preparedStatement.close();
        }

        return null;
    }
}
