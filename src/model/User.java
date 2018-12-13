package model;

public class User {
    private String uid;
    private String username;
    private String password;
    private String vocName;
    private String quota;
    private String total;

    public User() {
    }

    public User(String uid, String username, String password, String vocName, String quota, String total) {
        this.uid = uid;
        this.username = username;
        this.password = password;
        this.vocName = vocName;
        this.quota = quota;
        this.total = total;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getVocName() {
        return vocName;
    }

    public void setVocName(String vocName) {
        this.vocName = vocName;
    }

    public String getQuota() {
        return quota;
    }

    public void setQuota(String quota) {
        this.quota = quota;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    @Override
    public String toString() {
        return "User{" +
                "uid='" + uid + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", vocName='" + vocName + '\'' +
                ", quota='" + quota + '\'' +
                ", total='" + total + '\'' +
                '}';
    }
}
