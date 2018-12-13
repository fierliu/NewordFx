package model;

public class VocName {
    private String vid;
    private String name;
    private String wordAmount;

    public VocName(String vid, String name, String wordAmount) {
        this.vid = vid;
        this.name = name;
        this.wordAmount = wordAmount;
    }

    public VocName() {

    }

    public String getId() {
        return vid;
    }

    public void setId(String id) {
        this.vid = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getWordAmount() {
        return wordAmount;
    }

    public void setWordAmount(String wordAmount) {
        this.wordAmount = wordAmount;
    }
}
