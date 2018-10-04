package model;

public class Word {
	private String id;
	private String en;
	private String ch;
	private String date;
	private String know;

	public String getId(){
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getEn() {
		return en;
	}

	public void setEn(String en) {
		this.en = en;
	}

	public String getCh() {
		return ch;
	}

	public void setCh(String ch) {
		this.ch = ch;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getKnow() {
		return know;
	}

	public void setKnow(String know) {
		this.know = know;
	}

}
