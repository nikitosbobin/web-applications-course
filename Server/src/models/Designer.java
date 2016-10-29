package models;

public class Designer {
    private int id;
    private String name;
    private String surname;
    private String second_name;
    private int designers_groups_id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getSecond_name() {
        return second_name;
    }

    public void setSecond_name(String second_name) {
        this.second_name = second_name;
    }

    public int getDesigners_groups_id() {
        return designers_groups_id;
    }

    public void setDesigners_groups_id(int designers_groups_id) {
        this.designers_groups_id = designers_groups_id;
    }
}
