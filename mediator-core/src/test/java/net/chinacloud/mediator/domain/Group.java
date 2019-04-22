package net.chinacloud.mediator.domain;

import java.util.ArrayList;
import java.util.List;

public class Group {
    private Long id;
    private String name;
    private List<User1> users = new ArrayList<User1>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<User1> getUsers() {
        return users;
    }

    public void setUsers(List<User1> users) {
        this.users = users;
    }

	@Override
	public String toString() {
		return "Group [id=" + id + ", name=" + name + ", users=" + users + "]";
	}
    
}