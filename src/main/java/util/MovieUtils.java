/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package util;

import jakarta.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

/**
 *
 * @author nguye
 */
public class MovieUtils {
    // lay ra cac tham so kieu int tren web
    public static int getIntParameter(HttpServletRequest request, String paramName)
            throws IllegalArgumentException {
        String value = request.getParameter(paramName);
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(paramName + " is required");
        }
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(paramName + " must be a number");
        }
    }
    // lay ra kieu du lieu String 
    public static String getStringParameter(HttpServletRequest request, String paramName)
        throws IllegalArgumentException {
    String value = request.getParameter(paramName);
    if (value == null || value.isBlank()) {
        throw new IllegalArgumentException(paramName + " is required");
    }
    return value.trim();
    }
    // lay ra localdate
    public static LocalDate getLocalDateParameter(HttpServletRequest request, String paramName)
            throws IllegalArgumentException {
        String value = request.getParameter(paramName);
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Thuộc tính không được để trống !");
        }
        try {
            LocalDate date = LocalDate.parse(value); // yyyy-MM-dd
            // Ngay chieu trong qua khu la khong hop le
            if (date.isBefore(LocalDate.now())) {
                throw new IllegalArgumentException(" Ngày chiếu phải là hôm nay hoặc tương lai !");
            }
            return date;
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Ngày chiếu không đúng định dạng !");
        }
    }
}
