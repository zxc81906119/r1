package model;

import java.util.Date;

public class detaildollcost {
	private Date thisdate;
	private String dollname;
	private int todollmachinecount;
	private int outdollmachinecount;
	private doll thisdoll;
	public doll getThisdoll() {
		return thisdoll;
	}
	public void setThisdoll(doll thisdoll) {
		this.thisdoll = thisdoll;
	}
	public Date getThisdate() {
		return thisdate;
	}
	public void setThisdate(Date thisdate) {
		this.thisdate = thisdate;
	}
	public String getDollname() {
		return dollname;
	}
	public void setDollname(String dollname) {
		this.dollname = dollname;
	}
	public int getTodollmachinecount() {
		return todollmachinecount;
	}
	public void setTodollmachinecount(int todollmachinecount) {
		this.todollmachinecount = todollmachinecount;
	}
	public int getOutdollmachinecount() {
		return outdollmachinecount;
	}
	public void setOutdollmachinecount(int outdollmachinecount) {
		this.outdollmachinecount = outdollmachinecount;
	}
}
