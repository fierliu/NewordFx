package dao;

import model.UserWord;

import java.sql.Connection;

public interface WordDaoInterface {
    public void update(UserWord userWord, String vocName);
    public void delete(Integer wid, String vocName);
    public UserWord getByRandom(String vocName);
    public UserWord getByAscend(String vocName);
    public UserWord getByDescend(String vocName);
    public UserWord getByWordId(Integer wid, String vocName);

}
