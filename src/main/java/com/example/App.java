package com.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.HashMap;

public class App {

    static String url = "jdbc:mysql://localhost:3306/TAS";
    static String user = "yaffa";
    static String password = "yaffa";
    static HashMap<String, Record> records = new HashMap<>(); //to keep track of the records
    static Connection conn;

    public static void main(String[] args) {

        try {
            conn = DriverManager.getConnection(url, user, password);
            String query = "SELECT clock_id, employee_id, transaction_symbol, transaction_time FROM clock_data      WHERE posted='N'";
            Statement s = conn.createStatement();
            ResultSet res = s.executeQuery(query);
            Record rec;
            while (res.next()) {
                int clock_id = res.getInt("clock_id");
                String employee_id = res.getString("employee_id");
                String transaction_symbol = res.getString("transaction_symbol");
                Timestamp transaction_time = res.getTimestamp("transaction_time");
                boolean isStart = defineSymbol(transaction_symbol, clock_id);
                if (!records.containsKey(employee_id)) {
                    rec = new Record(employee_id, null, null);
                    records.put(employee_id, rec);
                } else {
                    rec = records.get(employee_id);
                }

                if (isStart) {
                    rec.setStart_time(transaction_time);
                } else {
                    rec.setEnd_time(transaction_time);
                }
                System.out.println(rec);

            }

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    //defining whether the symbol is start or end based on the clock id
    private static boolean defineSymbol(String transaction_symbol, int clock_id) throws Exception {
        try {
            String query = "SELECT CASE  WHEN clock_start_symbol  = ? THEN 'start'   WHEN clock_end_symbol = ? THEN 'end' END AS symbol_type FROM  clock_symbol WHERE clock_terminal_id= ?;";
            PreparedStatement prep = conn.prepareStatement(query);
            prep.setString(1, transaction_symbol);
            prep.setString(2, transaction_symbol);
            prep.setInt(3, clock_id);
            ResultSet res = prep.executeQuery();

            if (res.next()) {
                return "start".equals(res.getString("symbol_type"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        throw new Exception("Mismatch in symbol");
    }

}
