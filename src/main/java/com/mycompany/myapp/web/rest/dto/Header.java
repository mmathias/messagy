package com.mycompany.myapp.web.rest.dto;

/**
 * Created by magno on 27/06/15.
 */
public class Header {

    private String id;
    private String label;
    private String type;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
