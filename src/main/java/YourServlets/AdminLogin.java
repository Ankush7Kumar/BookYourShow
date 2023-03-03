package YourServlets;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "AdminLogin", value = "/AdminLogin")
public class AdminLogin extends HttpServlet {

//    public Connection getCon() throws SQLException, ClassNotFoundException {
//        Class.forName("com.mysql.jdbc.Driver");
//        Connection con= DriverManager.getConnection(
//                "jdbc:mysql://localhost:3306/bookyourshow?characterEncoding=utf8","root","ucantcme");
//        return con;
//    }
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter out = response.getWriter();
        List<String> languages = new ArrayList<>();


        try{
            Class.forName("com.mysql.jdbc.Driver");
            Connection con= DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/bookyourshow?characterEncoding=utf8","root","ucantcme");
            PreparedStatement stmt=con.prepareStatement("select lname from languages");
            ResultSet rs = stmt.executeQuery();
            while(rs.next()){
                //response.sendRedirect(request.getContextPath()+"/LoginWorked?username="+username);
                String lang = rs.getString("lname");
                languages.add(lang);
            }
            rs.close();
            stmt.close();
            con.close();
        }   catch(Exception e){
            System.out.println("welcome page ke exception k bhitar");
            System.out.println(e);
            e.printStackTrace();
        }

        out.println("<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <title>AdminLogin</title>\n" +
                "    <style>\n" +
                "      body{text-align: center;}\n" +
                "    </style>\n" +
                "\n" +
                "</head>\n" +
                "<body>\n" +
                "<form action=\"EnterMovie\" method=\"get\">\n" +
                "\n" +
                "  <h1>\n" +
                "    Enter Movie\n" +
                "  </h1>\n" +
                "  \n" +
                "  <h4>\n" +
                "    Enter Movie Details\n" +
                "  </h4>\n" +
                "\n" +
                "  <input type=\"text\" name=\"name\" placeholder=\"enter name\" required=\"\"><br>\n" +
                "  <input type=\"text\" name=\"description\" placeholder=\"enter description\" required=\"\"> <br>\n" +
                "  <label for=\"release_year\">Release year:</label>\n" +
                "  <input type=\"number\" id=\"release_year\" name=\"release_year\" min=\"1900\" max=\"2023\" value=\"2023\" required=\"\"/><br>\n" +
                "  \n" +
                "  <label for=\"rating\">Rating: </label>\n" +
                "  <input type=\"number\" id=\"rating\" name=\"rating\"  min=\"0.0\" max=\"5.0\" step=\"0.1\" value=\"4.0\" required=\"\"/><br>\n" +
                "    \n");

        out.println("<label for=\"original_language\">Choose original language:</label>\n" +
                "\n" +
                "<select name=\"original_language\" id=\"original_language\">\n");

        for (String l : languages) {
            out.println("  <option value=");
            out.println("\"" + l + "\">" + l);
            out.println("</option>\n");
        }

        out.println("</select>\n");

        out.println("<br><label for=\"language\">Choose show language:</label>\n" +
                "\n" +
                "<select name=\"language\" id=\"language\">\n");

        for (String l : languages) {
            out.println("  <option value=");
            out.println("\"" + l + "\">" + l);
            out.println("</option>\n");
        }

        out.println("</select>\n");

        out.println(
                "  <h4>\n" +
                        "    Enter Availability\n" +
                        "  </h4>\n" +
                        "  \n" +
                        "  <label for=\"startdate\">Start Date:</label>\n" +
                        "  <input type=\"date\" id=\"startdate\" name=\"startdate\" required=\"\"><br>\n" +
                        "  <label for=\"enddate\">End Date:</label>\n" +
                        "  <input type=\"date\" id=\"enddate\" name=\"enddate\"><br>\n" +
                        "  \n" +
                        "  \n" +
                        "  <br><text>Check available slots:</text><br>\n" +
                        "  <label for=\"slot1\"> 9am to 12pm</label>\n" +
                        "  <input type=\"checkbox\" id=\"slot1\" name=\"slot1\" value=\"yes\"><br>\n" +
                        "  <label for=\"slot2\"> 12pm to 3pm</label>\n" +
                        "  <input type=\"checkbox\" id=\"slot2\" name=\"slot2\" value=\"yes\"><br>\n" +
                        "  <label for=\"slot3\"> 3pm to 6pm</label>\n" +
                        "  <input type=\"checkbox\" id=\"slot3\" name=\"slot3\" value=\"yes\"><br>\n" +
                        "  <label for=\"slot4\"> 6pm to 9pm</label>\n" +
                        "  <input type=\"checkbox\" id=\"slot4\" name=\"slot4\" value=\"yes\"><br>\n" +
                        "\n" +
                        "  <h4>\n" +
                        "    Enter Pricing\n" +
                        "  </h4>\n" +
                        "  \n" +
                        "  <label for=\"silverprice\">Silver Ticker Price:</label>\n" +
                        "  <input type=\"number\" id=\"silverprice\" name=\"silverprice\" min=\"0\" max=\"100000\" required=\"\"><br>\n" +
                        "  <label for=\"goldprice\">Gold Ticker Price:</label>\n" +
                        "  <input type=\"number\" id=\"goldprice\" name=\"goldprice\" min=\"0\" max=\"100000\" required=\"\"><br><br><br><br>\n" +
                        "  \n" +
                        "  \n" +
                        "  <input type=\"submit\" value=\"Enter Movie\"> <br>\n" +
                        "\n" +
                        "\n" +
                        "</form>\n" +
                        "</body>\n" +
                        "</html>");

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
