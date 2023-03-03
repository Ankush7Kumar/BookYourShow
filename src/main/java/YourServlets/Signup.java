package YourServlets;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import javax.xml.transform.Result;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

@WebServlet(name = "Signup", value = "/Signup")
public class Signup extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter out = response.getWriter();
        String username = request.getParameter("username");
        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String address = request.getParameter("address");

        try{
            Class.forName("com.mysql.jdbc.Driver");
            System.out.println("cp1");
            Connection con= DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/bookyourshow?characterEncoding=utf8","root","ucantcme");
            PreparedStatement stmt=con.prepareStatement("insert into user (username, u_name, email, pwd, address, role) values (?,?,?,?,?,'customer')");
            System.out.println("cp2");
            stmt.setString(1,username);
            stmt.setString(2,name);
            stmt.setString(3,email);
            stmt.setString(4,password);
            stmt.setString(5,address);
            System.out.println("cp3");

            stmt.executeUpdate();
            System.out.println("cp4");
            stmt.close();
            con.close();
            response.sendRedirect(request.getContextPath()+"/index.html");
            //out.println("signup successful. Login now");
        }catch(Exception e){
            System.out.println("exception k bhitar");
            System.out.println(e);
            e.printStackTrace();
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
