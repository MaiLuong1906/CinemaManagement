/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Other/File.java to edit this template
 */
package controller.handle.movie;
import controller.handle.ControllerHandle;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
/**
 *
 * @author nguye
 */
public class AddMovieHandle implements ControllerHandle{

    @Override
    public void excute(HttpServletRequest request, HttpServletResponse respone) throws Exception {
        String method = request.getMethod();
        
        if ("GET".equals(method)) {
            // GET: Hiển thị form (chỉ cần movieId hoặc không cần)
            displayForm(request, respone);
        } else {
            // POST: Xử lý dữ liệu (cần tất cả fields)
            processForm(request, respone);
        }
    }
    // cac ham rieng biet
    // ham cho method get
    private void displayForm(HttpServletRequest request, HttpServletResponse respone) throws ServletException, IOException{
        request.getRequestDispatcher("/views/admin/movies/add-form.jsp")
            .forward(request, respone);
    }
    //ham cho method post
    private void processForm(HttpServletRequest request, HttpServletResponse respone){
        
        
        
    }

}
