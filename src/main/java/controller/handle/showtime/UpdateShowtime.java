/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Other/File.java to edit this template
 */
package controller.handle.showtime;
import controller.handle.ControllerHandle;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
/**
 *
 * @author nguye
 */
public class UpdateShowtime implements ControllerHandle{

    @Override
    public void excute(HttpServletRequest request, HttpServletResponse respone) 
            throws Exception {
        if ("GET".equalsIgnoreCase(request.getMethod())) {
            handleUpdateShowtimeGet(request, respone);
        } else {
            handleUpdateShowtimePost(request, respone);
        }
    }
    // add danh cho showtime
    private void handleUpdateShowtimeGet(HttpServletRequest request, HttpServletResponse respone)
            throws Exception {
        
    }
    
    private void handleUpdateShowtimePost(HttpServletRequest request, HttpServletResponse respone)
            throws Exception {
        
    }
    

} 
