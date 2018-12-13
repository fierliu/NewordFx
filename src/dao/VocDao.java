package dao;
/*
* for create, load and delete user_voc
* */
public interface VocDao {
    public void create(String newVocName, String primVocName);
    public void delete(String vocName);
    public int getVocSize(String vocName);
}
