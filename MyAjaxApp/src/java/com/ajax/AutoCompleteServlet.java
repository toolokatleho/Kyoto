/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package com.ajax;


import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;


/**
 *
 * @author TOOLO
 */
@WebServlet(name = "AutoCompleteServlet" , urlPatterns = { "/autocomplete" } )
public class AutoCompleteServlet extends HttpServlet {
    private final ComposerData compData = new ComposerData();
    private final HashMap<String,Composer> composers = compData.getComposers();
    
  
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
        throws IOException, ServletException {

    String action = request.getParameter("action");
    String targetId = request.getParameter("id");
    //StringBuffer sb = new StringBuffer();

    if (targetId == null || action == null ) {
        request.getRequestDispatcher("/error.jsp").forward(request, response);
        return; // Stop further execution if targetId is null
    }
    targetId = targetId.trim().toLowerCase();

    boolean namesAdded = false;
    StringBuilder sb = new StringBuilder();
    
    if ("complete".equals(action)) { // Use equals() for string comparison

        // check if user sent empty string
        if (!targetId.isEmpty()) {

            Iterator<String> it = composers.keySet().iterator();

            while (it.hasNext()) {
                String id = (String) it.next();
                Composer composer = (Composer) composers.get(id);

                if (composer.getFirstName().toLowerCase().startsWith(targetId) ||
                    composer.getLastName().toLowerCase().startsWith(targetId) ||
                    (composer.getFirstName() + " " + composer.getLastName()).toLowerCase().startsWith(targetId)) {

                    sb.append("<composer>");
                    sb.append("<id>").append(composer.getId()).append("</id>");
                    sb.append("<firstName>").append(composer.getFirstName()).append("</firstName>");
                    sb.append("<lastName>").append(composer.getLastName()).append("</lastName>");
                    sb.append("</composer>");
                    namesAdded = true;
                }
            }
        }

        if (namesAdded) {
            response.setContentType("text/xml");
            response.setHeader("Cache-Control", "no-cache");
            response.getWriter().write("<composers>" + sb.toString() + "</composers>");
        } else {
            //nothing to show
            response.setStatus(HttpServletResponse.SC_NO_CONTENT);
        }
    } else if ("lookup".equals(action)) { // Use equals() for string comparison

        // put the target composer in the request scope to display
        if (composers.containsKey(targetId)) {
            request.setAttribute("composer", composers.get(targetId));
            request.getRequestDispatcher("/composer.jsp").forward(request, response);
        }else{
            response.setStatus(HttpServletResponse.SC_NO_CONTENT);
        }
    }
}
}
