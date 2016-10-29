package models;

import java.util.Set;

public class DetailSpecialty {
    private Detail detail;
    private String warehouse;
    private String factory;
    private Set<Designer> designers;

    public DetailSpecialty(Detail detail, String warehouse, String factory, Set<Designer> designers) {
        this.detail = detail;
        this.warehouse = warehouse;
        this.factory = factory;
        this.designers = designers;
    }

    public Detail getDetail() {
        return detail;
    }

    public String getWarehouse() {
        return warehouse;
    }

    public String getFactory() {
        return factory;
    }

    public Set<Designer> getDesigners() {
        return designers;
    }
}
