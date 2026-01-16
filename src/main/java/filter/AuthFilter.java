///*
// * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
// * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
// */
//package filter;
//
//import jakarta.servlet.Filter;
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.ServletRequest;
//import jakarta.servlet.ServletResponse;
//import jakarta.servlet.annotation.WebFilter;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import jakarta.servlet.http.HttpSession;
//import java.io.IOException;
//
///**
// *
// * @author LENOVO
// */
//
//@WebFilter(urlPatterns = {
//    // Chặn Servlet
//    "/booking/*",      // Trang đặt vé
//    "/my-bookings/*",  // Lịch sử đặt vé
//    "/profile/*",      // Thông tin cá nhân
//    "/payment/*",       // Trang thanh toán
//        
//    // Chặn trang
//    "/views/user/booking.jsp",
//    "/views/user/my-bookings.jsp",
//    "/views/user/profile.jsp",
//    "/views/user/payment.jsp"
//})
//public class AuthFilter implements Filter{
//
//    @Override
//    public void doFilter(ServletRequest sr, ServletResponse sr1, FilterChain fc) throws IOException, ServletException {
//        HttpServletRequest request = (HttpServletRequest)sr;
//        HttpServletResponse response = (HttpServletResponse)sr1;
//      
//        HttpSession session = request.getSession(false);
//        
//        boolean isLoggedIn = (session != null && session.getAttribute("user") != null);
//        
//        if(isLoggedIn){
//            fc.doFilter(sr, sr1);
//        }
//        else{
//            String requestURI = request.getRequestURI();
//            response.sendRedirect(request.getContextPath() + "/views/auth/login.jsp");
//        }
//    }
//    
//}
