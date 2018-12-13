package dao;

import model.User;
import model.VocName;

import java.util.List;

public interface VocNameDao {
    public void add(VocName vocName);
    public void mod(VocName vocName);
    public void del(String vid);
    public VocName load(String name);
    public List<VocName> findAll();
}
