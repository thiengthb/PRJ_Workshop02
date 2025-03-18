/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao.interfaces;

import java.util.List;
import models.Product;

/**
 *
 * @author trann
 */
public interface IProductDAO {
    void create(Product product);
    Product findById(String productId);
    List<Product> findAll();
    void update(Product product);
    void delete(String productId);
}
