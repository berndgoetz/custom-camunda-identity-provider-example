package berndgoetz.camunda.service;

import org.camunda.bpm.engine.identity.Group;

import java.io.Serializable;

public class SimpleGroup implements Group, Serializable {

    private String id;
    private String name;
    private String type;

    public SimpleGroup(String id, String name, String type) {
        this.id = id;
        this.name = name;
        this.type = type;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    public void setType(String type) {
        this.type = type;
    }
}
