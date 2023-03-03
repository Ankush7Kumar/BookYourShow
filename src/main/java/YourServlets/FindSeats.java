package YourServlets;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import java.util.List;


@WebServlet(name = "FindSeats", value = "/FindSeats")
public class FindSeats extends HttpServlet {


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter out = response.getWriter();
        Date date = Date.valueOf(request.getParameter("datepick"));
        //out.println("date is "+ date);

        System.out.println("fs1");
        int slotp = Integer.parseInt(request.getParameter("slotpick"));
        System.out.println("fs2");
        //out.println("slotpick: "+slotp);
        System.out.println("fs3");

        try {
            System.out.println("fs4");
            Class.forName("com.mysql.jdbc.Driver");
            Connection con= DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/bookyourshow?characterEncoding=utf8","root","ucantcme");
            PreparedStatement stmt=con.prepareStatement("select show_id from shows where movie_id=? and show_slot=?");
            System.out.println("fs5");
            HttpSession session=request.getSession(false);
            System.out.println("fs5.1");
            String mid=(String)session.getAttribute("movie_id");
            String uname=(String)session.getAttribute("uname");
            System.out.println("mid is "+mid);
            System.out.println("username is "+uname);
            System.out.println("fs6");
            stmt.setInt(1, Integer.parseInt(mid));
            if(slotp == 1) {stmt.setInt(2,9);} else if (slotp == 2) {stmt.setInt(2,12);} else if (slotp == 3) {stmt.setInt(2,15);} else {stmt.setInt(2,18);}
            System.out.println("fs7");
            ResultSet rs = stmt.executeQuery();
            System.out.println("fs8");
            int sid = -1;
            System.out.println("fs9");
            while(rs.next()){
                System.out.println("fs10");
                sid = rs.getInt("show_id");
                System.out.println("show id is "+sid);
                System.out.println("rs.getInt(\"show_id\") is "+rs.getInt("show_id"));
                //response.sendRedirect(request.getContextPath()+"/LoginWorked?username="+username);

            }
            System.out.println("fs11");

            session.setAttribute("show_id",String.valueOf(sid));
            System.out.println("fs12");
            stmt.close();
            rs.close();
            //con.close();
            System.out.println("fs13");
            //out.println("showidentity is " + sid);
            System.out.println("fs14");

            PreparedStatement stmt2=con.prepareStatement("select silver_price, gold_price from movie where mid=?");
            stmt2.setInt(1, Integer.parseInt(mid));
            ResultSet rs2 = stmt2.executeQuery();
            int silver_price = -1;
            int gold_price = -1;
            while(rs2.next()){
                silver_price = rs2.getInt("silver_price");
                gold_price = rs2.getInt("gold_price");
            }

            stmt2.close();
            rs2.close();

            PreparedStatement stmt3=con.prepareStatement("select booked_gs, booked_ss from shows where show_id=?");
            stmt3.setInt(1, sid);
            ResultSet rs3 = stmt3.executeQuery();
            String booked_ss = null;
            String booked_gs = null;
            while(rs3.next()){
                booked_ss = rs3.getString("booked_ss");
                booked_gs = rs3.getString("booked_gs");
            }
            System.out.println("butterflow booked seats follow");
            System.out.println(booked_ss);
            List<String> silvers = List.of(booked_ss.split(","));
            //System.out.println(silvers);
            //System.out.println(booked_gs);
            List<String> golds = List.of(booked_gs.split(","));
            //System.out.println(golds);

            stmt3.close();
            rs3.close();

            con.close();



            out.println("<!DOCTYPE html>\n" +
                    "<html lang=\"en\">\n" +
                    "<head>\n" +
                    "  <meta charset=\"UTF-8\">\n" +
                    "    <title>SeatFind</title>\n" +
                    "<style>\n" +
                    "    body{text-align: center;}\n" +
                    "  </style>" +
                    " <link rel=\"stylesheet\" href=\"styles.css\">\n" +
                    "  \n" +
                    "</head>\n" +
                    "<body>\n");


            //out.println("<button type=\"button\" class=\"btn btn-primary btn-square-md\">Button</button>\n");
            out.println("<br><div class=\"boxed\">\n" +
                    "<br> SCREEN \n" +
                    "</div><br><br>");


            out.println("<br><br>Silver Seat Price: <b> ");
            out.println(silver_price);

            out.println(" </b><br>\n");
            makeseatsilver(out, booked_ss);



            out.println("<br><br><br>\n");

            out.println("Gold Seats Price: <b>");
            out.println(gold_price);
            out.println(" </b><br>");

            makeseatgold(out, booked_gs);

            out.println("\n" + "</body>\n" +
                    "</html>");



        } catch (Exception e) {
            System.out.println("findseats ke exception k bhitar");
            System.out.println(e);
            e.printStackTrace();

        }
    }

    private void makeseatsilver(PrintWriter out, String booked_ss) {
        String tosplitsilver = booked_ss.substring(1,booked_ss.length()-1);
        List<String> silvers = List.of(tosplitsilver.split(",|\s"));
        System.out.println("inside makesilverseat: silvers: "+silvers);

        int index = 1;
        out.println("<form action=TicketPrint>");
        for(int i = 0; i < 5; i++){
            for(int j = 1; j < 11; j++) {
                out.println("<input type=\"checkbox\" id=");
                out.println(index);
                out.println(" name=");
                out.println(index);
                out.println(" value=\"Yes\" ");
                if(silvers.contains(Integer.toString(index))){
                    out.println("disabled");
                }
                out.println(">");
//                out.println("<button type=\"button\" class=\"btn btn-primary btn-square-md\">");
//                out.print("S"+index);
                index++;
//                out.println("</button>\n");
            }
            out.println("<br>");
        }
    }

    private void makeseatgold(PrintWriter out, String booked_gs) {
        String tosplitgold = booked_gs.substring(1,booked_gs.length()-1);
        List<String> golds = List.of(tosplitgold.split(",|\s"));
        int index = 51;
        for(int i = 0; i < 3; i++){
            for(int j = 0; j < 12; j++) {
//                out.println("<button type=\"button\" class=\"btn btn-primary-gold btn-square-md-gold\" >");
//                out.print("G"+index);
//                index++;
//                out.println("</button>\n");
                out.println("<input type=\"checkbox\" id=");
                out.println(index);
                out.println(" name=");
                out.println(index);
                out.println(" value=\"Yes\" ");
                if(golds.contains(Integer.toString(index-50))){
                    out.println("disabled");
                }
                out.println(">");
                index++;
            }
            out.println("<br>");
        }

        for(int i = 0; i < 14; i++) {
//            out.println("<button type=\"button\" class=\"btn btn-primary-gold btn-square-md-gold\">");
//            out.print("G"+index);
//            index++;
//            out.println("</button>\n");
            out.println("<input type=\"checkbox\" id=");
            out.println(index);
            out.println(" name=");
            out.println(index);
            out.println(" value=\"Yes\" ");
            if(golds.contains(Integer.toString(index-50))){
                out.println("disabled");
            }
            out.println(">");
            index++;
        }
        out.println("<br><br><br><input type=\"submit\" value=\"Click to Book\">\n" +
                "</form>");
        out.println("<br>");
    }




    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
