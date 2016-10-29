package models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class Detail implements Serializable {
    private int id;
    private Date creation_date;
    private int warehouses_id;
    private int factories_id;
    private int cost;
    private int designers_groups_id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getCreation_date() {
        return creation_date;
    }

    public void setCreation_date(Date creation_date) {
        this.creation_date = creation_date;
    }

    public int getWarehouses_id() {
        return warehouses_id;
    }

    public void setWarehouses_id(int warehouses_id) {
        this.warehouses_id = warehouses_id;
    }

    public int getFactories_id() {
        return factories_id;
    }

    public void setFactories_id(int factories_id) {
        this.factories_id = factories_id;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public int getDesigners_groups_id() {
        return designers_groups_id;
    }

    public void setDesigners_groups_id(int designers_groups_id) {
        this.designers_groups_id = designers_groups_id;
    }
}
