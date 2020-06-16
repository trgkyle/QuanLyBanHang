/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAO;

/**
 *
 * @author quang
 */
import Model.HoaDon;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class HoaDonDAO {
    private static HoaDonDAO _instance;
    private static DataBaseUtils dataBaseUtils;
    private static MatHangDAO matHangDAO;
    private ResultSet resultSet;
    private PreparedStatement preparedStatement;


    /**
     * Tạo kết nối đến DB
     *
     * @throws Exception
     */
    private HoaDonDAO() throws Exception {
        dataBaseUtils = DataBaseUtils.getInstance();
         matHangDAO = MatHangDAO.getInstance();
    }


    /**
     * Design Pattern: Singleton
     *
     * @return
     * @throws Exception
     */
    public static HoaDonDAO getInstance() throws Exception {
        if (_instance == null) {
            synchronized (HoaDonDAO.class) {
                if (null == _instance) {
                    _instance = new HoaDonDAO();
                }
            }
        }
        return _instance;
    }


    /**
     * đọc danh sách hoá đơn từ DB
     *
     * @return
     * @throws Exception
     */
    public ArrayList<HoaDon> getHoaDons() throws Exception {
        ArrayList<HoaDon> hoaDons = new ArrayList<HoaDon>();
        String sql = "SELECT * FROM VIEW_HOADON";

        try {
            resultSet = dataBaseUtils.excuteQueryRead(sql);

            while (resultSet.next()) {
                HoaDon hoaDon = new HoaDon(
                        matHangDAO.getMatHang(resultSet.getString("MAMH")),
                        resultSet.getInt("SOLUONG"),
                        resultSet.getString("MAHD"),
                        KhachHangDAO.getInstance().getKhachHang(resultSet.getString("MAKH")),
                        resultSet.getDate("NGAYLAP"),
                        resultSet.getInt("TINHTRANG")
                );

                hoaDons.add(hoaDon);
            }
        } catch (Exception e) {
            throw new Exception("Lỗi lấy danh sách hoá đơn");
        } finally {
            resultSet.close();
        }

        return hoaDons;
    }


    /**
     * Đọc hoá đơn từ DB
     *
     * @param maHoaDon
     * @return
     * @throws Exception
     */
    public HoaDon getHoaDon(String maHoaDon) throws Exception {
        HoaDon hoaDon = null;
        String sql = String.format("SELECT * FROM VIEW_HOADON WHERE MAHD = '%s'", maHoaDon);

        try {
            resultSet = dataBaseUtils.excuteQueryRead(sql);
            resultSet.next();

            hoaDon = new HoaDon(
                    matHangDAO.getMatHang(resultSet.getString("MAMH")),
                    resultSet.getInt("SOLUONG"),
                    resultSet.getString("MAHD"),
                    KhachHangDAO.getInstance().getKhachHang(resultSet.getString("MAKH")),
                    resultSet.getDate("NGAYLAP"),
                    resultSet.getInt("TINHTRANG")
            );
        } catch (Exception e) {
            throw new Exception("Lỗi lấy thông tin hoá đơn");
        } finally {
            resultSet.close();
        }

        return hoaDon;
    }


    /**
     * Lấy mã hoá đơn cuối
     * dùng để generate mã hoá đơn mới
     *
     * @return
     * @throws Exception
     */
    public String getMaHoaDonCuoi() throws Exception {
        String sql = "SELECT TOP 1 MAHD FROM HOADON ORDER BY MAHD DESC";
        String ketQua;

        try {
            resultSet = dataBaseUtils.excuteQueryRead(sql);
            resultSet.next();

            ketQua = resultSet.getString("MAHD");
        } catch (SQLException e) {
            throw new Exception("Đọc dữ liệu hoá đơn lỗi");
        } finally {
            resultSet.close();
        }

        return ketQua;
    }


    /**
     * Cập nhật thông tin hoá đơn vào DB
     *
     * @param hoaDon
     * @return
     * @throws Exception
     */
    public HoaDon suaHoaDon(HoaDon hoaDon) throws Exception {
        String sql = "UPDATE CHITIETHOADON SET " +
                "MAMH = ?, SOLUONG = ?, TINHTRANG = ? WHERE MAHD = ?";

        try {
            preparedStatement = dataBaseUtils.excuteQueryWrite(sql);

            preparedStatement.setString(1, hoaDon.getMatHang().getMaMatHang());
            preparedStatement.setInt(2, hoaDon.getSoLuong());
            preparedStatement.setInt(3, hoaDon.isTinhTrang());
            preparedStatement.setString(4, hoaDon.getMaHoaDon());

            if (preparedStatement.executeUpdate() > 0) {
                sql = "UPDATE HOADON SET NGAYLAP = ? WHERE MAHD = ?";
                preparedStatement = dataBaseUtils.excuteQueryWrite(sql);

                preparedStatement.setDate(1, hoaDon.getNgayLap());
                preparedStatement.setString(2, hoaDon.getMaHoaDon());

                if (preparedStatement.executeUpdate() > 0) {
                    dataBaseUtils.commitQuery();
                    return getHoaDon(hoaDon.getMaHoaDon());
                }
            }
        } catch (Exception e) {
            dataBaseUtils.rollbackQuery();
            throw new Exception("Lỗi cập nhật thông tin hoá đơn");
        } finally {
            preparedStatement.close();
        }

        return null;
    }


    /**
     * Thêm hoá đơn mới vào DB
     *
     * @param hoaDon
     * @return
     * @throws Exception
     */
    public HoaDon themHoaDon(HoaDon hoaDon) throws Exception {
        String sql = "INSERT INTO HOADON (MAHD, NGAYLAP, MAKH) VALUES (?,?,?)";
        try {
            preparedStatement = dataBaseUtils.excuteQueryWrite(sql);

            preparedStatement.setString(1, hoaDon.getMaHoaDon());
            preparedStatement.setDate(2, hoaDon.getNgayLap());
            preparedStatement.setString(3, hoaDon.getKhachHang().getMaKH());

            if (preparedStatement.executeUpdate() > 0) {
                dataBaseUtils.commitQuery();
                sql = "INSERT INTO CHITIETHOADON (MAHD, MAMH, SOLUONG, TINHTRANG) VALUES ( ?, ?, ?, ?)";

                preparedStatement = dataBaseUtils.excuteQueryWrite(sql);

                preparedStatement.setString(1, hoaDon.getMaHoaDon());
                preparedStatement.setString(2, hoaDon.getMatHang().getMaMatHang());
                preparedStatement.setInt(3, hoaDon.getSoLuong());
                preparedStatement.setInt(4, hoaDon.isTinhTrang());

                if (preparedStatement.executeUpdate() > 0) {
                    dataBaseUtils.commitQuery();
                    return getHoaDon(hoaDon.getMaHoaDon());
                }
            }
        } catch (Exception e) {
            dataBaseUtils.rollbackQuery();
            throw new Exception("Lỗi thêm hoá đơn");
        } finally {
            preparedStatement.close();
        }

        return null;
    }


    /**
     * Cập nhật thanh toán hoá đơn vào DB
     *
     * @param maHoaDon
     * @return
     * @throws Exception
     */
    public boolean thanhToanHoaDon(String maHoaDon) throws Exception {
        final String sql = String.format("{call THANHTOAN_HOADON(%s)}", maHoaDon);

        try {
            dataBaseUtils.excuteProcedure(sql);
            return true;
        } catch (Exception e) {
            throw new Exception("Lỗi thanh toán hoá đơn");
        }
    }


    /**
     * Xoá hoá đơn trong DB
     *
     * @param maHoaDon
     * @return
     * @throws Exception
     */
    public boolean xoaHoaDon(String maHoaDon) throws Exception {
        String sql = "DELETE FROM CHITIETHOADON WHERE MAHD = ?";

        try {
            preparedStatement = dataBaseUtils.excuteQueryWrite(sql);

            preparedStatement.setString(1, maHoaDon);

            if (preparedStatement.executeUpdate() > 0) {
                sql = "DELETE FROM HOADON WHERE MAHD = ?";

                preparedStatement = DataBaseUtils.getInstance().excuteQueryWrite(sql);

                preparedStatement.setString(1, maHoaDon);

                if (preparedStatement.executeUpdate() > 0) {
                    dataBaseUtils.commitQuery();
                    return true;
                }
            }
        } catch (Exception e) {
            dataBaseUtils.rollbackQuery();
            throw new Exception("Lỗi xoá hoá đơn");
        } finally {
            preparedStatement.close();
        }

        return false;
    }
}