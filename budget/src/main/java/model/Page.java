package model;

public class Page {

	public Page() {
	}

	private int totaldatacount;// 總筆數

	private int perpagedatacount = 5;// 一頁幾筆

	private int totalpagecount;// 總頁數

	private int thispagedatacount;// 此頁資料筆數

	public int getTotaldatacount() {
		return totaldatacount;
	}

	public void setTotaldatacount(int totaldatacount) {
		this.totaldatacount = totaldatacount;
	}

	public int getPerpagedatacount() {
		return perpagedatacount;
	}

	public void setPerpagedatacount(int perpagedatacount) {
		this.perpagedatacount = perpagedatacount;
	}

	public int getTotalpagecount() {
		return totalpagecount;
	}

	public void setTotalpagecount() {
		if(this.totaldatacount<this.perpagedatacount) {
			this.totalpagecount=1;
		}else {
			if(this.totaldatacount%this.perpagedatacount==0) {
				this.totalpagecount=this.totaldatacount/this.perpagedatacount;
			}else {
				this.totalpagecount=this.totaldatacount/this.perpagedatacount+1;
			}
		}
	}
	
	public int getThispagedatacount(int page) {
		if(this.totalpagecount==1) {
			return this.totaldatacount;
		}else {
			if(page==this.totalpagecount) {
				return totaldatacount - (page - 1) * perpagedatacount;
			}else {
				return this.perpagedatacount;
			}
		}
	}
}
