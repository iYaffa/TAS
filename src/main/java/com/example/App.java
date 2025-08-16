package com.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.HashMap;

public class App {

    static String url = "jdbc:mysql://localhost:3306/TAS";
    static String user = "yaffa";
    static String password = "yaffa";
    static HashMap<String, Record> records = new HashMap<>();

    public static void main(String[] args) {

        try (Connection conn = DriverManager.getConnection(url, user, password)) {

            String query = "SELECT cd.clock_id, cd.employee_id, cd.transaction_symbol, cd.transaction_time, "
                    + "CASE WHEN cs.clock_start_symbol = cd.transaction_symbol THEN 'start' "
                    + "WHEN cs.clock_end_symbol = cd.transaction_symbol THEN 'end' ELSE 'UNKNOWN' END AS symbol_type "
                    + "FROM clock_data cd "
                    + "JOIN clock_symbol cs ON cd.clock_id = cs.clock_terminal_id "
                    + "WHERE cd.posted = 'N'";

            try (Statement stmt = conn.createStatement(); ResultSet res = stmt.executeQuery(query)) {

                while (res.next()) {
                    String employee_id = res.getString("employee_id");
                    Timestamp transaction_time = res.getTimestamp("transaction_time");
                    boolean isStart = "start".equals(res.getString("symbol_type"));

                    Record rec = records.computeIfAbsent(employee_id, k -> new Record(employee_id, null, null));

                    if (isStart) {
                        rec.setStart_time(transaction_time);
                    } else {
                        rec.setEnd_time(transaction_time);
                    }

                    System.out.println(rec);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
