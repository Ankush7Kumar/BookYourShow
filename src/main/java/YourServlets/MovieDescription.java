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

@WebServlet(name = "MovieDescription", value = "/MovieDescription")
public class MovieDescription extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        PrintWriter out = response.getWriter();
        int mid = Integer.parseInt(request.getParameter("mid"));
        HttpSession session=request.getSession(false);
        session.setAttribute("movie_id",Integer.toString(mid));
        System.out.println("md1");

        try{
            Class.forName("com.mysql.jdbc.Driver");
            Connection con= DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/bookyourshow?characterEncoding=utf8","root","ucantcme");
            PreparedStatement stmt=con.prepareStatement("select mname, mdesc,release_year, rating,original_language_id, lang_id from movie where mid=?");
            stmt.setInt(1,mid);
            ResultSet rs = stmt.executeQuery();
            String m_name = "";
            String m_desc = "";
            int m_release_year = -1;
            float rating = -1;
            int orig_lang_id = -1;
            int lang_id = -1;
            while(rs.next()) {
                m_name = rs.getString("mname");
                m_desc = rs.getString("mdesc");
                m_release_year = rs.getInt("release_year");
                rating = rs.getFloat("rating");
                orig_lang_id = rs.getInt("original_language_id");
                lang_id = rs.getInt("lang_id");
            }
            rs.close();
            stmt.close();


            out.println("<!DOCTYPE html>\n" +
                    "<html lang=\"en\">\n" +
                    "<head>\n" +
                    "  <meta charset=\"UTF-8\">\n" +
                    "  <title>movieDescriptionhere</title>\n" +
                    "  <style>\n" +
                    "    body{text-align: center;}\n" +
                    "  </style>\n" +
                    "</head>\n" +
                    "\n" +
                    "<body>\n" +
                    "\n" +
                    "  <h1>\n" +
                    m_name

                    +"\n" +
                    "  </h1><br>\n" +
                    m_desc + "<br><br><br>"
                    + "Release Year: " + m_release_year + "<br>"
                    + "Rating: " + rating + "<br>");
            System.out.println("md2");
            PreparedStatement stmt2=con.prepareStatement("select lname from languages where lid=?");
            stmt2.setInt(1,orig_lang_id);
            ResultSet rs2 = stmt2.executeQuery();
            String orig_lang_name = "";
            System.out.println("md3");
            while(rs2.next()){
                orig_lang_name = rs2.getString("lname");
            }
            System.out.println("md4");
            out.println("Original Language: "+orig_lang_name+"<br>");
            System.out.println("md5");
            stmt2.close();
            rs2.close();

            PreparedStatement stmt3=con.prepareStatement("select lname from languages where lid=?");
            stmt3.setInt(1,lang_id);
            ResultSet rs3 = stmt3.executeQuery();
            String lang_name = "";
            while(rs3.next()){
                lang_name = rs3.getString("lname");
            }
            out.println("Language: "+lang_name+"<br><br><br>");
            stmt3.close();
            rs3.close();


            out.println("\n" +
                    "\n" +
                    "</body>\n" +
                    "</html>\n" +
                    "\n");

            con.close();

            out.println("<!DOCTYPE html>\n" +
                    "<html lang=\"en\">\n" +
                    "<head>\n" +
                    "  <meta charset=\"UTF-8\">\n" +
                    "  <title>bookyourshow</title>\n" +
                    "  <style>\n" +
                    "    body{text-align: center;}\n" +
                    "  </style>\n" +
                    "</head>\n" +
                    "\n" +
                    "<body>\n" +
                    "<form action=\"BookShow\">\n" +
                    "  <input type=\"hidden\" id=\"mid\" name=\"mid\" value="+mid+">\n" +
                    "  <input type=\"submit\" value=\"Find Show\">\n" +
                    "</form>\n" +
                    "</body>\n" +
                    "</html>\n" +
                    "\n");


        } catch (Exception e) {
            System.out.println("movieDescription ke exception k bhitar");
            System.out.println(e);
            e.printStackTrace();
        }


    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
