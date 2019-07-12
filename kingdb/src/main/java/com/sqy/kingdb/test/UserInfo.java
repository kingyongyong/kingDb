package com.sqy.kingdb.test;

import com.sqy.kingdb.annotion.KingField;
import com.sqy.kingdb.annotion.KingTable;

@KingTable("t_user_info")
public class UserInfo {
	@KingField(value = "f_name",primaryKey = true)
	public String name;
	@KingField(value = "f_age",notNull = true,unique = true)
    public Integer age;
	@KingField("f_money")
    public Float money;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getAge() {
		return age;
	}
	public void setAge(Integer age) {
		this.age = age;
	}
	public Float getMoney() {
		return money;
	}
	public void setMoney(Float money) {
		this.money = money;
	}

	@Override
	public String toString() {
		return name;
	}
}
