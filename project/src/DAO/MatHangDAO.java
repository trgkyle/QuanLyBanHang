/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAO;

import Model.MatHang;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author quang
 */
public class MatHangDAO {

    private static MatHangDAO _instance;
    private static DataBaseUtils dataBaseUtils;

    public MatHangDAO() throws Exception {
        dataBaseUtils = DataBaseUtils.getInstance();
    }

    /**
     * Design Pattern: Singleton
     *
     * @return
     * @throws Exception
     */
    public static MatHangDAO getInstance() throws Exception {
        if (_instance == null) {
            synchronized (MatHangDAO.class) {
                if (null == _instance) {
                    _instance = new MatHangDAO();
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
    public ArrayList<MatHang> getMatHangs() throws Exception {
        ArrayList<MatHang> matHangs = new ArrayList<MatHang>();
        ResultSet resultSet = null;
        final String sql = "SELECT * FROM MATHANG";

        try {
            resultSet = dataBaseUtils.excuteQueryRead(sql);
            while (resultSet.next()) {
                System.out.println(resultSet.getString("MAMH"));
                MatHang matHang = new MatHang(
                        resultSet.getString("MAMH"),
                        resultSet.getString("TENMH"),
                        resultSet.getString("THELOAI"),
                        resultSet.getBoolean("TINHTRANG"),
                        resultSet.getString("MANCC"),
                        resultSet.getString("GHICHU"),
                        resultSet.getDouble("DONGIA"),
                        resultSet.getInt("SOLUONGTON")
                );

                matHangs.add(matHang);
            }

        } catch (Exception e) {
            throw new Exception("Đọc danh sách mặt hàng lỗi");
        } finally {
            resultSet.close();
        }
        System.out.println("Return mat hang");

        return matHangs;
    }

    public MatHang getMatHangsByMMH(String maMatHang) throws Exception {
        MatHang matHang = null;
        ResultSet resultSet = null;

//        final String sql = "SELECT * FROM MATHANG";
        String sql = String.format("SELECT * FROM VIEW_THONGTINMATHANG WHERE MAMH = '%s'", maMatHang);

        try {
            resultSet = dataBaseUtils.excuteQueryRead(sql);

            while (resultSet.next()) {
                System.out.println("Lay duoc ma nha cung cap");
                System.out.println(resultSet.getString("MANCC"));
                matHang = new MatHang(
                        resultSet.getString("MAMH"),
                        resultSet.getString("TENMH"),
                        resultSet.getString("TENNCC"),
                        resultSet.getString("THELOAI"),
                        resultSet.getBoolean("TINHTRANG"),
                        resultSet.getString("MANCC"),
                        resultSet.getString("GHICHU"),
                        resultSet.getDouble("DONGIA"),
                        resultSet.getInt("SOLUONGTON")
                );

            }

        } catch (Exception e) {
            throw new Exception("Đọc danh sách mặt hàng lỗi");
        } finally {
            resultSet.close();
        }
        System.out.println("xxxxxxx");
        System.out.println(matHang.getTenNhaCungCap());
        return matHang;
    }

    public ArrayList<MatHang> getMatHangDeXoa() throws Exception {
        ArrayList<MatHang> matHangs = new ArrayList<MatHang>();
        ResultSet resultSet = null;

        final String sql = "SELECT * FROM MATHANG";

        try {
            resultSet = dataBaseUtils.excuteQueryRead(sql);

            while (resultSet.next()) {
                MatHang matHang = new MatHang(
                        resultSet.getString("MAMH"),
                        resultSet.getString("TENMH"),
                        resultSet.getInt("SOLUONGTON")
                );

                matHangs.add(matHang);
            }

        } catch (Exception e) {
            throw new Exception("Đọc danh sách mặt hàng lỗi");
        } finally {
            resultSet.close();
        }

        return matHangs;
    }

    /**
     * Lấy mặt hàng từ DB
     *
     * @param maMatHang
     * @return
     * @throws Exception
     */
    public MatHang getMatHang(String maMatHang) throws Exception {
        MatHang matHang = null;
        String sql = String.format("SELECT * FROM MATHANG WHERE MAMH = '%s'", maMatHang);
        try (ResultSet resultSet = dataBaseUtils.excuteQueryRead(sql)) {

            while (resultSet.next()) {
                matHang = new MatHang(
                        resultSet.getString("MAMH"),
                        resultSet.getString("TENMH"),
                        resultSet.getString("THELOAI"),
                        resultSet.getBoolean("TINHTRANG"),
                        resultSet.getString("MANCC"),
                        resultSet.getString("GHICHU"),
                        resultSet.getDouble("DONGIA"),
                        resultSet.getInt("SOLUONGTON")
                );
            }
        } catch (SQLException e) {
            throw new Exception(String.format("Đọc dữ liệu mặt hàng %s lỗi", matHang.getMaMatHang()));
        }

        return matHang;
    }

    /**
     * Lấy mã mặt hàng cuối trong DB Dùng để generate mã mặt hàng mới
     *
     * @return
     * @throws Exception
     */
    public String getMaMatHangCuoi() throws Exception {
        String sql = "SELECT TOP 1 MAMH FROM MATHANG ORDER BY MAMH DESC";
        ResultSet resultSet = null;
        String ketQua;

        try {
            resultSet = dataBaseUtils.excuteQueryRead(sql);
            resultSet.next();

            ketQua = resultSet.getString("MAMH");
        } catch (SQLException e) {
            throw new Exception("Đọc dữ liệu mặt hàng lỗi");
        } finally {
            resultSet.close();
        }

        return ketQua;
    }

    /**
     * Thêm mặt hàng mới vào DB
     *
     * @param matHang
     * @return
     * @throws Exception
     */
    public MatHang themMatHang(MatHang matHang) throws Exception {
        String sql = "INSERT INTO MATHANG (MAMH, TENMH, MANCC, GHICHU, DONGIA, TINHTRANG, THELOAI, SOLUONGTON) VALUES (?,?,?,?,?,?,?,?)";
        PreparedStatement preparedStatement = null;

        if (matHang == null) {
            return null;
        }

        try {
            preparedStatement = dataBaseUtils.excuteQueryWrite(sql);

            preparedStatement.setString(1, matHang.getMaMatHang());
            preparedStatement.setString(2, matHang.getTenMatHang());
            preparedStatement.setString(3, matHang.getMaNhaCungCap());
            preparedStatement.setString(4, matHang.getGhiChu());
            preparedStatement.setDouble(5, matHang.getDonGia());
            preparedStatement.setBoolean(6, matHang.isTinhTrang());
            preparedStatement.setString(7, matHang.getTheLoai());
            preparedStatement.setInt(8, matHang.getSoLuongTon());

            if (preparedStatement.executeUpdate() > 0) {
                dataBaseUtils.commitQuery();
                return getMatHang(matHang.getMaMatHang());
            }
        } catch (Exception e) {
            dataBaseUtils.rollbackQuery();
            throw new Exception("Thêm mặt hàng lỗi");
        } finally {
            preparedStatement.close();
        }

        return null;
    }

    /**
     * Xoá mặt hàng trong DB
     *
     * @param maMatHang
     * @return
     * @throws Exception
     */
    public boolean xoaMatHang(String maMatHang) throws Exception {
        String sql = "DELETE FROM MATHANG WHERE MAMH = ?";
        PreparedStatement preparedStatement = null;

        if (getMatHang(maMatHang) == null) {
            return false;
        }

        try {
            preparedStatement = dataBaseUtils.excuteQueryWrite(sql);

            preparedStatement.setString(1, maMatHang);

            if (preparedStatement.executeUpdate() > 0) {
                dataBaseUtils.commitQuery();
                return true;
            }
        } catch (Exception e) {
            dataBaseUtils.rollbackQuery();
            throw new Exception("Lỗi xoá mặt hàng");
        } finally {
            preparedStatement.close();
        }

        return false;
    }

    /**
     * Cập nhật thông tin mặt hàng trong DB
     *
     * @param matHang
     * @return
     * @throws Exception
     */
    public MatHang suaMatHang(MatHang matHang) throws Exception {
        String sql = "UPDATE MATHANG SET "
                + "TENMH = ?, MANCC = ?, GHICHU = ?, DONGIA = ?, "
                + "TINHTRANG = ?, THELOAI = ?, SOLUONGTON = ? WHERE MAMH = ?";
        PreparedStatement preparedStatement = null;

        if (matHang == null) {
            return null;
        }

        try {
            preparedStatement = dataBaseUtils.excuteQueryWrite(sql);

            preparedStatement.setString(1, matHang.getTenMatHang());
            preparedStatement.setString(2, matHang.getMaNhaCungCap());
            preparedStatement.setString(3, matHang.getGhiChu());
            preparedStatement.setDouble(4, matHang.getDonGia());
            preparedStatement.setBoolean(5, matHang.isTinhTrang());
            preparedStatement.setString(6, matHang.getTheLoai());
            preparedStatement.setInt(7, matHang.getSoLuongTon());
            preparedStatement.setString(8, matHang.getMaMatHang());

            if (preparedStatement.executeUpdate() > 0) {
                dataBaseUtils.commitQuery();
                return getMatHang(matHang.getMaMatHang());
            }
        } catch (Exception e) {
            dataBaseUtils.rollbackQuery();
            throw new Exception("Cập nhật thông tin mặt hàng lỗi");
        } finally {
            preparedStatement.close();
        }

        return null;
    }
}
