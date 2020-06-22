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
import Model.MatHangHoaDon;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class HoaDonDAO {
    
    private static HoaDonDAO _instance;
    private static DataBaseUtils dataBaseUtils;
    private static MatHangHoaDonDAO matHangHoaDonDAO;
    private static KhachHangDAO khachHangDAO;
    private ResultSet resultSet;
    private PreparedStatement preparedStatement;

    /**
     * Tạo kết nối đến DB
     *
     * @throws Exception
     */
    private HoaDonDAO() throws Exception {
        matHangHoaDonDAO = new MatHangHoaDonDAO();
        khachHangDAO = new KhachHangDAO();
        dataBaseUtils = DataBaseUtils.getInstance();
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
        String sql = "SELECT * FROM HOADON";
        try {
            resultSet = dataBaseUtils.excuteQueryRead(sql);
            
            while (resultSet.next()) {
                System.out.println(resultSet.getString("MAHD"));
                HoaDon hoaDon = new HoaDon(
                        matHangHoaDonDAO.getMatHangs(resultSet.getString("MAHD")),
                        resultSet.getString("MAHD"),
                        khachHangDAO.getKhachHang(resultSet.getString("MAKH")),
                        resultSet.getDate("NGAYLAP"),
                        resultSet.getInt("TINHTRANG")
                );
                System.out.println("Get ma nha cung cap");
                System.out.println(hoaDon.getMatHang().get(0).getMaNhaCungCap());
                System.out.println("Get ten nha cung cap");
                System.out.println(hoaDon.getMatHang().get(0).getTenNhaCungCap());
                System.out.println("Get tinh trang");
                System.out.println(hoaDon.isTinhTrang());
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
        String sql = String.format("SELECT * FROM HOADON WHERE MAHD = '%s'", maHoaDon);
        
        try {
            System.out.println("Bat dau thuc thi");
            resultSet = dataBaseUtils.excuteQueryRead(sql);
            System.out.println("Thuc thi xong");
            if (resultSet.next()) {
                System.out.println("...");
                System.out.println(resultSet.getString("MAHD"));
                System.out.println(resultSet.getString("MAKH"));
                System.out.println(resultSet.getDate("NGAYLAP").toString());
                System.out.println(resultSet.getInt("TINHTRANG"));
                hoaDon = new HoaDon(
                        matHangHoaDonDAO.getMatHangs(resultSet.getString("MAHD")),
                        resultSet.getString("MAHD"),
                        KhachHangDAO.getInstance().getKhachHang(resultSet.getString("MAKH")),
                        resultSet.getDate("NGAYLAP"),
                        resultSet.getInt("TINHTRANG")
                );
            }
            
        } catch (Exception e) {
            System.out.println(e);
            throw new Exception("Lỗi lấy thông tin hoá đơn");
        } finally {
            resultSet.close();
        }
        
        return hoaDon;
    }

    /**
     * Lấy mã hoá đơn cuối dùng để generate mã hoá đơn mới
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
     * Thêm hoá đơn mới vào DB
     *
     * @param hoaDon
     * @return
     * @throws Exception
     */
    public HoaDon themHoaDon(HoaDon hoaDon) throws Exception {
        System.out.println("Them hoa don");
        String sql = "INSERT INTO HOADON (MAHD, NGAYLAP, MAKH, TINHTRANG) VALUES (?,?,?,?)";
        try {
            System.out.println(hoaDon.toString());
            preparedStatement = dataBaseUtils.excuteQueryWrite(sql);
            preparedStatement.setString(1, hoaDon.getMaHoaDon());
            preparedStatement.setDate(2, hoaDon.getNgayLap());
            preparedStatement.setString(3, hoaDon.getKhachHang().getMaKH());
            preparedStatement.setString(4, Integer.toString(hoaDon.isTinhTrang()));
            
            if (preparedStatement.executeUpdate() > 0) {
                dataBaseUtils.commitQuery();
                sql = "INSERT INTO CHITIETHOADON (MAHD, MAMH, TENMH, SOLUONG ) VALUES ( ?, ?, ?, ?)";
                System.out.println(hoaDon.getMatHang().size());
                for (MatHangHoaDon matHangHoaDon : hoaDon.getMatHang()) {
                    System.out.println(matHangHoaDon.toString());
                    preparedStatement = dataBaseUtils.excuteQueryWrite(sql);
                    preparedStatement.setString(1, hoaDon.getMaHoaDon());
                    preparedStatement.setString(2, matHangHoaDon.getMaMatHang());
                    preparedStatement.setString(3, matHangHoaDon.getTenMatHang());
                    preparedStatement.setInt(4, matHangHoaDon.getSoLuong());
                   
                    if (preparedStatement.executeUpdate() <= 0) {
                        System.out.println("Loi thuc thi tao chi tiet hoa don");
                        dataBaseUtils.rollbackQuery();
                    }
                    else {
                        
                        dataBaseUtils.commitQuery();
                    }
                }
                System.out.println("Return");
                return getHoaDon(hoaDon.getMaHoaDon());
            }
        } catch (Exception e) {
            System.out.println(e);
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
    public boolean traHang(String maHoaDon) throws Exception {
        final String sql = String.format("{call TRAHANG_HOADON(%s)}", maHoaDon);
        
        try {
            dataBaseUtils.excuteProcedure(sql);
            return true;
        } catch (Exception e) {
            throw new Exception("Lỗi thanh toán hoá đơn");
        }
    }
}
