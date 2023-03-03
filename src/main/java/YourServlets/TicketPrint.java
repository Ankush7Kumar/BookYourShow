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
import java.util.Arrays;
import java.util.List;

@WebServlet(name = "TicketPrint", value = "/TicketPrint")
public class TicketPrint extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter out = response.getWriter();
        out.println("TICKET");
        out.println("");
        System.out.println("tp1");
        HttpSession session=request.getSession(false);
        System.out.println("tp2");
        String uname=(String)session.getAttribute("uname");
        System.out.println("tp3");
        String mid=(String)session.getAttribute("movie_id");
        System.out.println("tp4");
        String sid=(String)session.getAttribute("show_id");
        System.out.println("tp5");
        out.println("Username: "+uname);
        out.println("Movie Id: "+mid);
        out.println("Show Id: " + sid);
        out.println("Silver Seats Booked: ");
        List<Integer> booked_silver = new ArrayList<>();
        List<Integer> booked_gold = new ArrayList<>();


        for(int i = 1; i < 51; i++) {
            if(request.getParameter(String.valueOf(i)) != null) {
                out.print("S"+i+" ");
                booked_silver.add(i);
            }
        }

        out.println("");
        out.println("Gold Seats Booked: ");

        for(int i = 51; i < 101; i++) {
            if(request.getParameter(String.valueOf(i)) != null) {
                out.print("G"+(i-50)+" ");
                booked_gold.add(i-50);
            }
        }

        System.out.println("list of booked seats follows: ");
        System.out.println(booked_silver);
        System.out.println(booked_gold);


        try{
            Class.forName("com.mysql.jdbc.Driver");
            Connection con= DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/bookyourshow?characterEncoding=utf8","root","ucantcme");
            PreparedStatement stmt0=con.prepareStatement("select booked_ss from shows where show_id = ?");
            stmt0.setInt(1, Integer.parseInt(sid));
            ResultSet rs0 = stmt0.executeQuery();
            String previous_ss = null;
            while(rs0.next()) {
                previous_ss = rs0.getString("booked_ss");
            }

            String toupdatesilver = "garbagesilver";
            if(previous_ss.length() == 3) {
                 toupdatesilver = booked_silver.toString();
            } else {
                if(booked_silver.size() == 0) {
                    toupdatesilver = previous_ss;
                } else {
                    toupdatesilver = previous_ss.substring(0,previous_ss.length()-1) + ", " + (booked_silver.toString()).substring(1,booked_silver.toString().length()-1) + "]";
                }

            }

            stmt0.close();
            rs0.close();


            PreparedStatement stmt=con.prepareStatement("update shows set booked_ss = ? where show_id = ?");
            stmt.setString(1, toupdatesilver);
            stmt.setInt(2, Integer.parseInt(sid));
            stmt.executeUpdate();
            stmt.close();


            PreparedStatement stmt01=con.prepareStatement("select booked_gs from shows where show_id = ?");
            stmt01.setInt(1, Integer.parseInt(sid));
            ResultSet rs01 = stmt01.executeQuery();
            String previous_gs = null;
            while(rs01.next()) {
                previous_gs = rs01.getString("booked_gs");
            }

            String toupdategold = "garbagegold";
            System.out.println("previous gs lenght is "+ previous_gs.length());

            if(previous_gs.length() == 3) {
                toupdategold = booked_gold.toString();
            } else {
                if(booked_gold.size() == 0) {
                    toupdategold = previous_gs;

                } else {
                    toupdategold = previous_gs.substring(0,previous_gs.length()-1) + ", " + (booked_gold.toString()).substring(1,booked_gold.toString().length()-1) + "]";
                }
            }
            stmt01.close();
            rs01.close();


            PreparedStatement stmt2=con.prepareStatement("update shows set booked_gs = ? where show_id = ?");
            stmt2.setString(1, toupdategold);
            stmt2.setInt(2, Integer.parseInt(sid));
            stmt2.executeUpdate();
            stmt2.close();
            con.close();





        } catch (Exception e) {
            System.out.println("ticket print ke exception k bhitar");
            System.out.println(e);
            e.printStackTrace();
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
