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
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.List;

@WebServlet(name = "EnterMovie", value = "/EnterMovie")
public class EnterMovie extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        try{
            PrintWriter out = response.getWriter();
            String start_date = request.getParameter("startdate");
            String end_date = request.getParameter("enddate");
            java.sql.Date sd = java.sql.Date.valueOf(request.getParameter("startdate"));
            java.sql.Date ed = java.sql.Date.valueOf(request.getParameter("enddate"));

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date startdate = sdf.parse(start_date);
            Date enddate = sdf.parse(end_date);
            if(startdate.compareTo(enddate) > 0) {
                out.println("Movie not entered");
                out.println("Start date comes after EndDate");
                out.println("Enter dates properly");
                return;
            }

            String name = request.getParameter("name");
            String description = request.getParameter("description");
            String release_year = request.getParameter("release_year"); //null
            String rating = request.getParameter("rating"); //null
            float rate = Float.parseFloat(request.getParameter("rating"));
            String original_language = request.getParameter("original_language");
            String language = request.getParameter("language");
            String slot1 = request.getParameter("slot1");
            String slot2 = request.getParameter("slot2");
            String slot3 = request.getParameter("slot3");
            String slot4 = request.getParameter("slot4");
            String silver_price = request.getParameter("silverprice");
            String gold_price = request.getParameter("goldprice");


            out.println("uuuuuu");
            out.println("name: "+ name);
            out.println("description : "+ description);
            //out.println(" : "+ );
            out.println("release year : "+ release_year);
            out.println("rating : "+ rating);
            out.println("original language : "+ original_language);
            out.println("language : "+ language);
            out.println("start date : "+ start_date);
            out.println("end date : "+ end_date);
            out.println("slot1 : "+ slot1);
            out.println("slot2 : "+ slot2);
            out.println("slot3 : "+ slot3);
            out.println("slot4 : "+ slot4);
            out.println("silver price : "+ silver_price);
            out.println("gold_price : "+ gold_price);

            try{
                Class.forName("com.mysql.jdbc.Driver");
                Connection con= DriverManager.getConnection(
                        "jdbc:mysql://localhost:3306/bookyourshow?characterEncoding=utf8","root","ucantcme");
                PreparedStatement stmt=con.prepareStatement("insert into movie (mname, mdesc, release_year, rating, original_language_id, lang_id, start_date, end_date, slot1, slot2, slot3, slot4, gold_price, silver_price) values \n" +
                        "(?,?,?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);\n");
                stmt.setString(1,name);
                stmt.setString(2,description);
                stmt.setInt(3, Integer.parseInt(release_year));
                stmt.setFloat(4, rate);
                System.out.println("cm1");
                PreparedStatement stmt2 =con.prepareStatement("select lid from languages where lname=?");
                System.out.println("cm2");
                stmt2.setString(1,original_language);
                System.out.println("cm2.1");
                ResultSet rs2 = stmt2.executeQuery();
                System.out.println("cm2.2");
                int ol_id = -1;
                if(rs2.next()){
                    ol_id = rs2.getInt("lid");
                }
                out.println("ol_id: "+ol_id);

                System.out.println("cm3");
                stmt2.close();
                rs2.close();
                stmt.setInt(5,ol_id);
                System.out.println("cm4");
                PreparedStatement stmt3 =con.prepareStatement("select lid from languages where lname=?");
                System.out.println("cm5");
                stmt3.setString(1,original_language);
                ResultSet rs3 = stmt3.executeQuery();
                System.out.println("cm6");
                int l_id = -1;
                if(rs3.next()){
                    l_id = rs3.getInt("lid");
                }
                out.println("l_id: "+l_id);
                System.out.println("cm7");
                stmt3.close();
                System.out.println("cm8");
                rs3.close();
                System.out.println("cm9");
                stmt.setInt(6, l_id);
                System.out.println("cm10");

                stmt.setDate(7,sd);
                stmt.setDate(8,ed);
                //stmt.setDate(7, (java.sql.Date) startdate);
                //stmt.setDate(8, (java.sql.Date) enddate);
                System.out.println("cm11");
                if(slot1 != null){stmt.setInt(9,1);} else {stmt.setInt(9,0);}
                if(slot2 != null){stmt.setInt(10,1);} else {stmt.setInt(10,0);}
                if(slot3 != null){stmt.setInt(11,1);} else {stmt.setInt(11,0);}
                if(slot4 != null){stmt.setInt(12,1);} else {stmt.setInt(12,0);}
                System.out.println("cm12");
                stmt.setInt(13, Integer.parseInt(gold_price));
                stmt.setInt(14, Integer.parseInt(silver_price));
                System.out.println("cm13");
                stmt.executeUpdate();
                out.println("DATA INSERTED");
                stmt.close();
                //con.close();

                //inserting into shows table now
                int total_days = 1;
                int total_slots = 0;
                System.out.println("cm14");
                PreparedStatement stmt4 =con.prepareStatement("SELECT DATEDIFF(?,?) as dd");
                System.out.println("cm15");
                stmt4.setDate(1,ed);
                stmt4.setDate(2,sd);
                System.out.println("cm16");
                ResultSet rs4 = stmt4.executeQuery();
                System.out.println("cm17");
                if(rs4.next()) {
                    Integer date_difference = rs4.getInt("dd");
                    System.out.println("DATE DIFF: "+date_difference);
                    total_days = date_difference + 1;
                }
                out.println("TOTAL DAYS: "+ total_days);
                System.out.println("cm18");
                stmt4.close();
                rs4.close();

                System.out.println("cm18.1");
                LocalDate start = LocalDate.parse(start_date);
                System.out.println("cm18.2");
                LocalDate end = LocalDate.parse(end_date);
                System.out.println("cm18.3");
                List<LocalDate> totalDates = new ArrayList<>();
                System.out.println("cm18.4");
                while (!start.isAfter(end)) {
                    totalDates.add(start);
                    start = start.plusDays(1);
                }


                System.out.println("VVVVVVVVVVVVVV");
                System.out.println(Arrays.deepToString(totalDates.toArray()));
                System.out.println("AAAAAAAAAAAAAAA");

                System.out.println("cm18.5");


                PreparedStatement stmt5 =con.prepareStatement("insert into shows (movie_id, show_date, show_slot, total_gs, total_ss, booked_gs, booked_ss) values (?,?,?,?,?,?,?)");
                System.out.println("cm19");
                PreparedStatement stmt6 = con.prepareStatement("select max(mid) from movie");
                System.out.println("cm20");
                ResultSet rs6 = stmt6.executeQuery();
                System.out.println("cm21");
                int mid = -1;
                if(rs6.next()){
                    mid = rs6.getInt("max(mid)");
                }
                stmt6.close();
                rs6.close();
                System.out.println("cm22");
                stmt5.setInt(1,mid);

                stmt5.setInt(4,50);
                stmt5.setInt(5,50);
                stmt5.setString(6, "[ ]");
                stmt5.setString(7, "[ ]");


                System.out.println("cm23");
                for(int i = 0; i < totalDates.size();i++) {
                    stmt5.setDate(2, java.sql.Date.valueOf(totalDates.get(i)));
                    out.println(totalDates.get(i));
                    if(slot1 != null) {stmt5.setInt(3,9); stmt5.executeUpdate();}
                    if(slot2 != null) {stmt5.setInt(3,12); stmt5.executeUpdate();}
                    if(slot3 != null) {stmt5.setInt(3,15); stmt5.executeUpdate();}
                    if(slot4 != null) {stmt5.setInt(3,18); stmt5.executeUpdate();}
                }
                System.out.println("cm24");



                stmt5.close();
                con.close();
            } catch (Exception e) {
                System.out.println("enter movie ke connection ke exception k bhitar");
                System.out.println(e);
                e.printStackTrace();
            }


        } catch(Exception e) {
            System.out.println("enter movie ke exception k bhitar");
            System.out.println(e);
            e.printStackTrace();
        }


    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
