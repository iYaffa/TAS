package com.example;

import java.sql.Timestamp;

public class Record {

    private String employee_id;
    private Timestamp start_time;
    private Timestamp end_time;

    public Record(String employee_id, Timestamp start_time, Timestamp end_time) {
        this.employee_id = employee_id;
        this.start_time = start_time;
        this.end_time = end_time;
    }

    public String getEmployee_id() {
        return employee_id;
    }

    public void setEmployee_id(String employee_id) {
        this.employee_id = employee_id;
    }

    public Timestamp getStart_time() {
        return start_time;
    }

    public void setStart_time(Timestamp start_time) {
        this.start_time = start_time;
    }

    public Timestamp getEnd_time() {
        return end_time;
    }

    public void setEnd_time(Timestamp end_time) {
        this.end_time = end_time;
    }

    @Override
    public String toString() {
        return "Record [employee_id=" + employee_id + ", start_time=" + start_time + ", end_time=" + end_time + "]";
    }

}
