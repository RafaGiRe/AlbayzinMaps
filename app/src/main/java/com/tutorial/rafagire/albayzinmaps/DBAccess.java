package com.tutorial.rafagire.albayzinmaps;

import java.util.List;

public interface DBAccess{
    void createTable();
    void deleteTable();
    void add(Site site) throws DBException;
    void update(Site site) throws DBException;
    void remove(int id) throws DBException;
    Site findById(int id) throws DBException;
    List<Site> findByFilters(boolean viewpoint, boolean church, boolean mosque, boolean monument, boolean zambra, boolean restaurant, boolean fountain) throws DBException;
    List<Site> findAll() throws DBException;
}
