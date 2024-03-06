package edu.esprit.services;

import java.sql.SQLException;
import java.util.Set;

public interface IService<T> {
    public void ajouter (T p)throws SQLException;
    public void modifier (T p)throws SQLException;
    public void supprimer(int id)throws SQLException;
    public T getOneById(int id)throws SQLException;
    public Set<T> getAll()throws SQLException;


}
