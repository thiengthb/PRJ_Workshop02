/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao.implement;

import dao.interfaces.IProductDAO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import models.Product;
import utils.DBUtils;

/**
 *
 * @author trann
 */
public class ProductDAO implements IProductDAO {
    private static final String INSERT_SQL = 
            "INSERT INTO products (productId, productName, productImage, brief, postedDate, typeId, account, unit, price, discount) " +
            "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    
    private static final String SELECT_BY_ID_SQL = 
            "SELECT * FROM products WHERE productId = ?";
    
    private static final String SELECT_ALL_SQL = 
            "SELECT * FROM products";
    
    private static final String UPDATE_SQL = 
            "UPDATE products SET productName = ?, productImage = ?, brief = ?, postedDate = ?, typeId = ?, " +
            "account = ?, unit = ?, price = ?, discount = ? WHERE productId = ?";
    
    private static final String DELETE_SQL = 
            "DELETE FROM products WHERE productId = ?";
    
    private static final String SELECT_BY_TYPE_ID = 
            "SELECT * FROM products WHERE typeId = ?"; // Fixed table name to "products"

    @Override
    public void create(Product product) {
        Connection conn = null;
        try {
            conn = DBUtils.getConnection();
            try (PreparedStatement pstmt = conn.prepareStatement(INSERT_SQL)) {
                pstmt.setString(1, product.getProductId());
                pstmt.setString(2, product.getProductName());
                pstmt.setString(3, product.getProductImage());
                pstmt.setString(4, product.getBrief());
                if (product.getPostedDate() != null) {
                    pstmt.setDate(5, new java.sql.Date(product.getPostedDate().getTime()));
                } else {
                    pstmt.setNull(5, java.sql.Types.DATE);
                }
                pstmt.setInt(6, product.getTypeId());
                pstmt.setString(7, product.getAccount());
                pstmt.setString(8, product.getUnit());
                pstmt.setInt(9, product.getPrice());
                pstmt.setInt(10, product.getDiscount());
                
                pstmt.executeUpdate();
            }
        } catch (SQLException ex) {
            Logger.getLogger(ProductDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            DBUtils.closeConnection(conn);
        }
    }

    @Override
    public Product findById(String productId) {
        Connection conn = null;
        try {
            conn = DBUtils.getConnection();
            try (PreparedStatement pstmt = conn.prepareStatement(SELECT_BY_ID_SQL)) {
                pstmt.setString(1, productId);
                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        return mapResultSetToProduct(rs);
                    }
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(ProductDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            DBUtils.closeConnection(conn);
        }
        return null;
    }

    @Override
    public List<Product> findAll() {
        List<Product> products = new ArrayList<>();
        Connection conn = null;
        try {
            conn = DBUtils.getConnection();
            try (PreparedStatement pstmt = conn.prepareStatement(SELECT_ALL_SQL);
                 ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    products.add(mapResultSetToProduct(rs));
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(ProductDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            DBUtils.closeConnection(conn);
        }
        return products;
    }

    @Override
    public void update(Product product) {
        Connection conn = null;
        try {
            conn = DBUtils.getConnection();
            try (PreparedStatement pstmt = conn.prepareStatement(UPDATE_SQL)) {
                pstmt.setString(1, product.getProductName());
                pstmt.setString(2, product.getProductImage());
                pstmt.setString(3, product.getBrief());
                if (product.getPostedDate() != null) {
                    pstmt.setDate(4, new java.sql.Date(product.getPostedDate().getTime()));
                } else {
                    pstmt.setNull(4, java.sql.Types.DATE);
                }
                pstmt.setInt(5, product.getTypeId());
                pstmt.setString(6, product.getAccount());
                pstmt.setString(7, product.getUnit());
                pstmt.setInt(8, product.getPrice());
                pstmt.setInt(9, product.getDiscount());
                pstmt.setString(10, product.getProductId());

                pstmt.executeUpdate();
            }
        } catch (SQLException ex) {
            Logger.getLogger(ProductDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            DBUtils.closeConnection(conn);
        }
    }

    @Override
    public void delete(String productId) {
        Connection conn = null;
        try {
            conn = DBUtils.getConnection();
            try (PreparedStatement pstmt = conn.prepareStatement(DELETE_SQL)) {
                pstmt.setString(1, productId);
                pstmt.executeUpdate();
            }
        } catch (SQLException ex) {
            Logger.getLogger(ProductDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            DBUtils.closeConnection(conn);
        }
    }

    @Override
    public List<Product> findByTypeId(int typeId) {
        List<Product> products = new ArrayList<>();
        Connection conn = null;
        try {
            conn = DBUtils.getConnection();
            try (PreparedStatement pstmt = conn.prepareStatement(SELECT_BY_TYPE_ID)) {
                pstmt.setInt(1, typeId);
                try (ResultSet rs = pstmt.executeQuery()) {
                    while (rs.next()) {
                        products.add(mapResultSetToProduct(rs));
                    }
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(ProductDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            DBUtils.closeConnection(conn);
        }
        return products;
    }

    private Product mapResultSetToProduct(ResultSet rs) {
        try {
            Product product = new Product();
            product.setProductId(rs.getString("productId"));
            product.setProductName(rs.getString("productName"));
            product.setProductImage(rs.getString("productImage"));
            product.setBrief(rs.getString("brief"));
            product.setPostedDate(rs.getDate("postedDate"));
            product.setTypeId(rs.getInt("typeId"));
            product.setAccount(rs.getString("account"));
            product.setUnit(rs.getString("unit"));
            product.setPrice(rs.getInt("price"));
            product.setDiscount(rs.getInt("discount"));
            return product;
        } catch (SQLException ex) {
            Logger.getLogger(ProductDAO.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
}