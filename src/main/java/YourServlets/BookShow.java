package YourServlets;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "BookShow", value = "/BookShow")
public class BookShow extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter out = response.getWriter();
        int mid = Integer.parseInt(request.getParameter("mid"));
        //out.println("bookshow page");
        //out.println("mid: "+mid);
        try{


            Class.forName("com.mysql.jdbc.Driver");
            Connection con= DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/bookyourshow?characterEncoding=utf8","root","ucantcme");
            PreparedStatement stmt=con.prepareStatement("select start_date, end_date from movie where mid=?");
            stmt.setInt(1,mid);
            ResultSet rs = stmt.executeQuery();
            Date start_date = null;
            Date end_date = null;
            if(rs.next()){
                start_date = rs.getDate("start_date");
                end_date = rs.getDate("end_date");
            }
            stmt.close();
            rs.close();
            //con.close();

            assert start_date != null;
            LocalDate start = start_date.toLocalDate();
            LocalDate end = end_date.toLocalDate();
            List<LocalDate> totalDates = new ArrayList<>();
            while (!start.isAfter(end)) {
                totalDates.add(start);
                start = start.plusDays(1);
            }




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
                    "<form action=\"FindSeats\" method=\"get\">\n" +
                    "    \n" +
                    "\n" +
                    "  <h3>\n" +
                    "      Select a Date\n" +
                    "  </h3>\n");





            out.println("  <select name=\"datepick\" id=\"datepick\">\n");

            for(int i = 0; i < totalDates.size();i++) {
                out.println("<option value=");
                out.println(totalDates.get(i));
                out.println(">");
                out.println(totalDates.get(i));
                out.println("</option>\n");
            }




            out.println("  </select>\n" +
                    "  <br>\n" +
                    "  \n" +
                    "  <h3>\n" +
                    "      Choose a Slot\n" +
                    "  </h3>\n");


            System.out.println("bs1");
            PreparedStatement stmt2=con.prepareStatement("select slot1, slot2, slot3, slot4 from movie where mid=?");
            stmt2.setInt(1,mid);
            ResultSet rs2 = stmt2.executeQuery();
            System.out.println("bs2");
            int slot1 = -1;
            int slot2 = -1;
            int slot3 = -1;
            int slot4 = -1;
            System.out.println("bs3");
            while(rs2.next()) {
                slot1 = rs2.getInt("slot1");
                slot2 = rs2.getInt("slot2");
                slot3 = rs2.getInt("slot3");
                slot4 = rs2.getInt("slot4");



                if(slot1 == 1) {
                    out.println("  <label for=\"nine\">9am-12pm</label>\n" +
                            "  <input type=\"radio\" id=\"slot1\" name=\"slotpick\" value=\"1\" required><br>\n");
                }
                if (slot2 == 1) {
                    out.println("  <label for=\"twelve\">12pm-3pm</label>\n" +
                            "  <input type=\"radio\" id=\"slot2\" name=\"slotpick\" value=\"2\" required><br>\n");
                }
                if (slot3 == 1) {
                    out.println("  <label for=\"three\">3pm-6pm</label>\n" +
                            "  <input type=\"radio\" id=\"slot3\" name=\"slotpick\" value=\"3\" required><br>\n");
                }
                if (slot4 == 1){
                    out.println("  <label for=\"six\">6pm-9pm</label>\n" +
                            "  <input type=\"radio\" id=\"slot4\" name=\"slotpick\" value=\"4\" required><br><br><br>\n");
                }


            }
            rs2.close();
            stmt2.close();
            con.close();

//            out.println("  <label for=\"nine\">9am-12pm</label>\n" +
//                    "  <input type=\"radio\" id=\"nine\" name=\"slotpick\" value=9><br>\n");
//
//            out.println(
//                    "  <label for=\"twelve\">12pm-3pm</label>\n" +
//                    "  <input type=\"radio\" id=\"twelve\" name=\"slotpick\" value=\"12\"><br>\n" +
//                    "  <label for=\"three\">3pm-6pm</label>\n" +
//                    "  <input type=\"radio\" id=\"three\" name=\"slotpick\" value=\"3\"><br>\n" +
//                    "  <label for=\"six\">6pm-9pm</label>\n" +
//                    "  <input type=\"radio\" id=\"six\" name=\"slotpick\" value=\"6\"><br><br><br>\n");


            out.println("  <br><br><br><input type=\"submit\" value=\"Pick Seats\">\n" +
                    "</form>\n" +
                    "\n" +
                    "</body>\n" +
                    "</html>\n" +
                    "\n");


        } catch(Exception e) {
            System.out.println("bookshow ke exception k bhitar");
            System.out.println(e);
            e.printStackTrace();

        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
