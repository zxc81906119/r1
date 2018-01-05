package model;

import java.util.Date;

public class dollstorage {
	private String dollname;
	private int dollcount;
	private Date thisdate;
	
	public String getDollname() {
		return dollname;
	}
	public void setDollname(String dollname) {
		this.dollname = dollname;
	}
	public int getDollcount() {
		return dollcount;
	}
	public void setDollcount(int dollcount) {
		this.dollcount = dollcount;
	}
}
