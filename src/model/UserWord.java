package model;
/*
* 用于背词过程中词的操作
* */
public class UserWord {
    private Integer id;
    private String en;
    private String ch;
    private String PhoneticSymbol;
    private String remark;
    private Integer times;
    private Integer Conquer;

    public UserWord() {
    }

    public UserWord(Integer id, String en, String ch, String phoneticSymbol, String remark, Integer times, Integer conquer) {
        this.id = id;
        this.en = en;
        this.ch = ch;
        PhoneticSymbol = phoneticSymbol;
        this.remark = remark;
        this.times = times;
        Conquer = conquer;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
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

    public String getPhoneticSymbol() {
        return PhoneticSymbol;
    }

    public void setPhoneticSymbol(String phoneticSymbol) {
        PhoneticSymbol = phoneticSymbol;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Integer getTimes() {
        return times;
    }

    public void setTimes(Integer times) {
        this.times = times;
    }

    public Integer getConquer() {
        return Conquer;
    }

    public void setConquer(Integer conquer) {
        Conquer = conquer;
    }

}
