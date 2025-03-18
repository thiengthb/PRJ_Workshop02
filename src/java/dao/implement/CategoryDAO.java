/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao.implement;

import dao.interfaces.ICategoryDAO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import models.Category;
import utils.DBUtils;

/**
 *
 * @author trann
 */
public class CategoryDAO implements ICategoryDAO {
    private static final String INSERT_SQL = 
            "INSERT INTO categories (typeId, categoryName, memo) VALUES (?, ?, ?)";
    
    private static final String SELECT_BY_ID_SQL = 
            "SELECT * FROM categories WHERE typeId = ?";
    
    private static final String SELECT_ALL_SQL = 
            "SELECT * FROM categories";
    
    private static final String UPDATE_SQL = 
            "UPDATE categories SET categoryName = ?, memo = ? WHERE typeId = ?";
    
    private static final String DELETE_SQL = 
            "DELETE FROM categories WHERE typeId = ?";
    
    @Override
    public void create(Category category) {
        Connection conn = null;
        try {
            conn = DBUtils.getConnection();
            try (PreparedStatement pstmt = conn.prepareStatement(INSERT_SQL)) {
                pstmt.setInt(1, category.getTypeId());
                pstmt.setString(2, category.getCategoryName());
                pstmt.setString(3, category.getMemo());
                
                pstmt.executeUpdate();
            }
        } catch (SQLException ex) {
            Logger.getLogger(CategoryDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            DBUtils.closeConnection(conn);
        }
    }

    @Override
    public Category findById(int typeId) {
        Connection conn = null;
        try {
            conn = DBUtils.getConnection();
            try (PreparedStatement pstmt = conn.prepareStatement(SELECT_BY_ID_SQL)) {
                pstmt.setInt(1, typeId);
                ResultSet rs = pstmt.executeQuery();
                
                if (rs.next()) {
                    return mapResultSetToCategory(rs);
                }
                return null;
            }
        } catch (SQLException ex) {
            Logger.getLogger(CategoryDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            DBUtils.closeConnection(conn);
        }
        return null;
    }

    @Override
    public List<Category> findAll() {
        List<Category> categories = new ArrayList<>();
        Connection conn = null;
        try {
            conn = DBUtils.getConnection();
            try (PreparedStatement pstmt = conn.prepareStatement(SELECT_ALL_SQL);
                 ResultSet rs = pstmt.executeQuery()) {
                
                while (rs.next()) {
                    categories.add(mapResultSetToCategory(rs));
                }
                return categories;
            }
        } catch (SQLException ex) {
            Logger.getLogger(CategoryDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            DBUtils.closeConnection(conn);
        }
        return null;
    }

    @Override
    public void update(Category category) {
        Connection conn = null;
        try {
            conn = DBUtils.getConnection();
            try (PreparedStatement pstmt = conn.prepareStatement(UPDATE_SQL)) {
                pstmt.setString(1, category.getCategoryName());
                pstmt.setString(2, category.getMemo());
                pstmt.setInt(3, category.getTypeId());
                
                pstmt.executeUpdate();
            }
        } catch (SQLException ex) {
            Logger.getLogger(CategoryDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            DBUtils.closeConnection(conn);
        }
    }

    @Override
    public void delete(int typeId) {
        Connection conn = null;
        try {
            conn = DBUtils.getConnection();
            try (PreparedStatement pstmt = conn.prepareStatement(DELETE_SQL)) {
                pstmt.setInt(1, typeId);
                pstmt.executeUpdate();
            }
        } catch (SQLException ex) {
            Logger.getLogger(CategoryDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            DBUtils.closeConnection(conn);
        }
    }

    private Category mapResultSetToCategory(ResultSet rs) {
        try {
            Category category = new Category();
            category.setTypeId(rs.getInt("typeId"));
            category.setCategoryName(rs.getString("categoryName"));
            category.setMemo(rs.getString("memo"));
            return category;
        } catch (SQLException ex) {
            Logger.getLogger(CategoryDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
}
