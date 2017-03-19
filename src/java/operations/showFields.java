/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package operations;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import tools.Controller;
import tools.Field;

/**
 *
 * @author Ceyhun
 */
@WebServlet(name = "showFields", urlPatterns = {"/showFields"})
public class showFields extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            String search = request.getParameter("search");
            Controller cntrl=new Controller();
            List<Field> fields = cntrl.showFields(search);
            int i=0;
            for (Field field : fields) {
                i++;
                out.println("  <tr>\n"
                        + "                <td data=" + field.getId() + ">" + i + "</td>\n"
                        + "                <td>" + field.getFirstName() + "</td>\n"
                        + "                <td>" + field.getLastName() + "</td>\n"
                        + "                <td>" + field.getFatherName() + "</td>\n"
                        + "                <td>" + field.getPhoneNumber() + "</td>\n"
                        + "                <td>" + field.getBornDate() + "</td>\n"
                        + "                <td><span class=\"glyphicon glyphicon-pencil control\" name=\"edit\" data-toggle=\"modal\" data-target=\"#editPlace\"></span></td>\n"
                        + "                <td><span class=\"glyphicon glyphicon-remove control\" name=\"delete\"></span></td>\n"
                        + "            </tr>");
            }
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
