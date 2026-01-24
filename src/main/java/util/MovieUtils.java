/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package util;

import jakarta.servlet.http.HttpServletRequest;

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
}
