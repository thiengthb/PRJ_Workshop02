/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao.interfaces;

import java.util.List;
import models.Category;

/**
 *
 * @author trann
 */
public interface ICategoryDAO {
    void create(Category category);
    Category findById(int typeId);
    List<Category> findAll();
    void update(Category category);
    void delete(int typeId);
}
