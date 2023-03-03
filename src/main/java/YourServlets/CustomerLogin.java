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
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "CustomerLogin", value = "/CustomerLogin")
public class CustomerLogin extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter out = response.getWriter();
        List<Integer> movies = new ArrayList<>();
        try{
            Class.forName("com.mysql.jdbc.Driver");
            Connection con= DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/bookyourshow?characterEncoding=utf8","root","ucantcme");
            PreparedStatement stmt=con.prepareStatement("select mid from movie order by mid");
            ResultSet rs = stmt.executeQuery();
            while(rs.next()){
                //response.sendRedirect(request.getContextPath()+"/LoginWorked?username="+username);
                int m_id = rs.getInt("mid");
                movies.add(m_id);
            }
            rs.close();
            stmt.close();
            //con.close();
            System.out.println("cl1");


            out.println("<!DOCTYPE html>\n" +
                    "<html>\n" +
                    "<head>\n" +
                    "  <meta charset=\"UTF-8\">\n" +
                    "  <title>customerlogin</title>\n" +
                    "  <style>\n" +
                    //"    body{text-align: center;}\n" +
                    "  </style>\n" +
                    "</head>"
                    +
                    "<body>\n" +
                    "\n" +
                    "<h1>Movies for you</h1>\n" +
                    "\n");

            //System.out.println("cl1.1");
            System.out.println("movies.size: "+movies.size());

            for(int i = 0; i < movies.size();i++) {
                //System.out.println("cl1.2");
                PreparedStatement stmt2 =con.prepareStatement("select mname, mdesc from movie where mid=?");
                stmt2.setInt(1,movies.get(i));

                ResultSet rs2 = stmt2.executeQuery();
                //System.out.println("cl2");
                while(rs2.next()){
                    int mid_togo = movies.get(i);
                    //System.out.println("cl2.1");
                    //response.sendRedirect(request.getContextPath()+"/LoginWorked?username="+username);
                    String m_name = rs2.getString("mname");
                    //System.out.println("cl2.2");
                    String m_desc = rs2.getString("mdesc");
                    //System.out.println("cl2.3");
                    out.println("<form action=\"MovieDescription?11\" method=\"get\">\n");

                    out.println("<label for=\"movie_name\">Name: "+
                            m_name +
                            "</label><br>\n" +
                            "<label for=\"movie_description\">Description: "
                            + m_desc
                            +"</label> <br>\n" +
                            "<input type=\"hidden\" name=\"mid\" value=" +
                            mid_togo +
                            " />"
                            +
                            "<button type=\"submit\" formaction=\"MovieDescription\">Book</button>");

                    out.print("<br><br>");
                    out.println("</form>\n" + "\n" +
                                    "</body>\n" +
                                    "</html>");
                    //System.out.println("cl2.4");

                }
                rs2.close();
                stmt2.close();
                //System.out.println("cl2.7");
            }


            //System.out.println("cl3");

            con.close();


        }   catch(Exception e){
            System.out.println("customerlogin ke exception k bhitar");
            System.out.println(e);
            e.printStackTrace();
        }




    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
