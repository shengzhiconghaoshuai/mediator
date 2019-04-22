package net.chinacloud.mediator.domain;

import java.util.Date;
import java.util.Map;

public class User1 {
    private Long id;
    private String name;
    private Date birthday;
    private Map<String,Integer> params;

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

	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	public Map<String, Integer> getParams() {
		return params;
	}

	public void setParams(Map<String, Integer> params) {
		this.params = params;
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", name=" + name + ", birthday=" + birthday
				+ ", params=" + params + "]";
	}
}