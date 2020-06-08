/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Utils;

import java.text.SimpleDateFormat;

/**
 *
 * @author quang
 */
public class Formats {
    // Format ngày theo định dạng Ngày - Tháng - Năm (người dùng)
    public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy");

    // Format ngày theo định dạng Năm - Tháng - Ngày (lưu vào SQL)
    public static final SimpleDateFormat DATE_FORMAT_SQL = new SimpleDateFormat("yyyy-MM-dd");

}
