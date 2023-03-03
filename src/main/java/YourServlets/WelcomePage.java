package YourServlets;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import static java.lang.System.out;

@WebServlet(name = "WelcomePage", value = "/WelcomePage")
public class WelcomePage extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter out = response.getWriter();
        //out.println("welcome page");

        String username = request.getParameter("username");
        String password = request.getParameter("password");


        try{

            Class.forName("com.mysql.jdbc.Driver");
            Connection con= DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/bookyourshow?characterEncoding=utf8","root","ucantcme");
            PreparedStatement stmt=con.prepareStatement("select * from user where username=? and pwd=?");
            stmt.setString(1,username);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();
            if(rs.next()){
                //response.sendRedirect(request.getContextPath()+"/LoginWorked?username="+username);

                //System.out.println("cp1");
                PreparedStatement stmt2 =con.prepareStatement("select u_name, role from user where username=?");
                //System.out.println("cp2");
                stmt2.setString(1,username);
                //System.out.println("cp3");
                ResultSet rs2 = stmt2.executeQuery();
                //System.out.println("cp4");
                if(rs2.next()) {
                    String user_role = rs2.getString("role");
                    String u_name = rs2.getString("u_name");
                    if(user_role.equals("admin")) {
                        //you are admin
                        response.sendRedirect(request.getContextPath()+"/AdminLogin?username="+username);
                        //out.println("You are logged in as "+user_role+" : "+u_name);
                    } else {
                        //you are customer
                        //System.out.println("cp5");
                        out.println("Login Successful");
                        HttpSession session=request.getSession();
                        session.setAttribute("uname",username);
                        response.sendRedirect(request.getContextPath()+"/CustomerLogin?username="+username);
                        out.println("You are logged in as "+user_role+" : "+u_name);
                    }
                }
                rs.close();
                stmt.close();
                rs2.close();
                stmt2.close();
            } else {
                out.println("Login Unsuccessful");
                out.println("Either username or password is incorrect");
            }
            con.close();
        }   catch(Exception e){
                System.out.println("welcome page ke exception k bhitar");
                System.out.println(e);
                e.printStackTrace();
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
