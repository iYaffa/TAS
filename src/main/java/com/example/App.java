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

            String query = "SELECT clock_id, employee_id, transaction_symbol, transaction_time, "
                    + "CASE WHEN transaction_symbol = (SELECT clock_start_symbol FROM clock_symbol WHERE clock_terminal_id = clock_data.clock_id AND transaction_type='CHECK') THEN 'start' "
                    + "WHEN transaction_symbol = (SELECT clock_end_symbol FROM clock_symbol WHERE clock_terminal_id = clock_data.clock_id  AND transaction_type='CHECK') THEN 'end' "
                    + "ELSE 'UNKNOWN' END AS symbol_type "
                    + "FROM clock_data WHERE posted = 'N'";

            try (Statement stmt = conn.createStatement(); ResultSet res = stmt.executeQuery(query)) {

                Record rec;
                while (res.next()) {
                    String employee_id = res.getString("employee_id");
                    Timestamp transaction_time = res.getTimestamp("transaction_time");
                    boolean isStart = "start".equals(res.getString("symbol_type"));

                    rec = records.computeIfAbsent(employee_id, k -> new Record(employee_id, null, null));

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
