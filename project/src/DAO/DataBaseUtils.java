/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAO;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author quang
 */
public class DataBaseUtils {
    private static Connection _connection;
    private static DataBaseUtils _instance;
    
    
    private DataBaseUtils() throws Exception {
        _connection = getConnection();
        _connection.setAutoCommit(false);
    }
    
    
    
    public static DataBaseUtils getInstance() throws Exception {
        if (_instance == null) {
            synchronized (DataBaseUtils.class) {
                if (null == _instance) {
                    _instance = new DataBaseUtils();
                }
            }
        }
        return _instance;
    }
    public static Connection getConnection(){
        Connection connection = null;
        String url = "jdbc:sqlserver://localhost:1433;databasename=QUANLYBANHANGVERSION1";
        String user = "sa";
        String password = "Quang1999.";
        try {
            connection = DriverManager.getConnection(url, user, password);
        } catch (Exception e) {e.printStackTrace();}
        return connection;
    }
    
    public PreparedStatement excuteQueryWrite(String sql) {
        try {
            return _connection.prepareStatement(sql);
        } catch (Exception e) {

        }
        return null;
    }
    
    
    /**
     * Thực thi script sql select
     *
     * @param sql
     * @return
     */
    public ResultSet excuteQueryRead(String sql) {
        try {
            return _connection.createStatement().executeQuery(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }


    /**
     * commit sql
     *
     * @throws Exception
     */
    public void commitQuery() throws Exception {
        try {
            _connection.commit();
        } catch (SQLException e) {
            throw new Exception("Lỗi commit query");
        }
    }
    
    
    
    /**
     * rollback sql
     *
     * @throws Exception
     */
    public void rollbackQuery() throws Exception {
        try {
            _connection.rollback();
        } catch (SQLException e) {
            throw new Exception("Lỗi rollback query");
        }
    }


    /**
     * reset DB về trạng thái ban đầu
     *
     * @return
     * @throws Exception
     */
    public boolean resetDatabase() throws Exception {
        final String sql = "{call RESET_DATABASE}";

        try (CallableStatement stmt = _connection.prepareCall(sql)) {
            stmt.execute();
            commitQuery();
            return true;
        } catch (SQLException e) {
            rollbackQuery();
            throw new Exception("Lỗi reset database");
        }
    }


    /**
     * thực thi hàm, thủ tục trong sql
     *
     * @param sql
     * @return
     * @throws Exception
     */
    public boolean excuteProcedure(String sql) throws Exception {
        try (CallableStatement stmt = _connection.prepareCall(sql)) {
            stmt.execute();
            commitQuery();
            return true;
        } catch (SQLException e) {
            rollbackQuery();
            throw new Exception("Lỗi reset database");
        }
    }
    
    
}
