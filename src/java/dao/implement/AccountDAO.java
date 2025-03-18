/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao.implement;

import dao.interfaces.IAccountDAO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import models.Account;
import utils.DBUtils;

/**
 *
 * @author trann
 */
public class AccountDAO implements IAccountDAO {

    private static final String INSERT_SQL
            = "INSERT INTO accounts (account, pass, lastName, firstName, birthday, gender, phone, isUse, roleInSystem) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

    private static final String SELECT_BY_ACCOUNT_SQL
            = "SELECT * FROM accounts WHERE account = ?";

    private static final String SELECT_ALL_SQL
            = "SELECT * FROM accounts";

    private static final String UPDATE_SQL
            = "UPDATE accounts SET pass = ?, lastName = ?, firstName = ?, birthday = ?, gender = ?, phone = ?, isUse = ?, roleInSystem = ? WHERE account = ?";

    private static final String DELETE_SQL
            = "DELETE FROM accounts WHERE account = ?";

    @Override
    public void create(Account account) {
        Connection conn = null;
        try {
            conn = DBUtils.getConnection();
            try (PreparedStatement pstmt = conn.prepareStatement(INSERT_SQL)) {
                pstmt.setString(1, account.getAccount());
                pstmt.setString(2, account.getPass());
                pstmt.setString(3, account.getLastName());
                pstmt.setString(4, account.getFirstName());
                if (account.getBirthday() != null) {
                    pstmt.setDate(5, new java.sql.Date(account.getBirthday().getTime()));
                } else {
                    pstmt.setNull(5, java.sql.Types.DATE);
                }
                pstmt.setBoolean(6, account.isGender());
                pstmt.setString(7, account.getPhone());
                pstmt.setBoolean(8, account.isIsUse());
                pstmt.setInt(9, account.getRoleInSystem());

                pstmt.executeUpdate();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            DBUtils.closeConnection(conn);
        }
    }

    @Override
    public Account findByAccount(String account) {
        Connection conn = null;
        try {
            conn = DBUtils.getConnection();
            try (PreparedStatement pstmt = conn.prepareStatement(SELECT_BY_ACCOUNT_SQL)) {
                pstmt.setString(1, account);
                ResultSet rs = pstmt.executeQuery();

                if (rs.next()) {
                    return mapResultSetToAccount(rs);
                }
                return null;
            }
        } catch (SQLException ex) {
            Logger.getLogger(AccountDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            DBUtils.closeConnection(conn);
        }
        return null;
    }

    @Override
    public List<Account> findAll() {
        List<Account> accounts = new ArrayList<>();
        Connection conn = null;
        try {
            conn = DBUtils.getConnection();
            try (PreparedStatement pstmt = conn.prepareStatement(SELECT_ALL_SQL);
                    ResultSet rs = pstmt.executeQuery()) {

                while (rs.next()) {
                    accounts.add(mapResultSetToAccount(rs));
                }
                return accounts;
            }
        } catch (SQLException ex) {
            Logger.getLogger(AccountDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            DBUtils.closeConnection(conn);
        }
        return null;
    }

    @Override
    public void update(Account account) {
        Connection conn = null;
        try {
            conn = DBUtils.getConnection();
            try (PreparedStatement pstmt = conn.prepareStatement(UPDATE_SQL)) {
                pstmt.setString(1, account.getPass());
                pstmt.setString(2, account.getLastName());
                pstmt.setString(3, account.getFirstName());
                if (account.getBirthday() != null) {
                    pstmt.setDate(4, new java.sql.Date(account.getBirthday().getTime()));
                } else {
                    pstmt.setNull(4, java.sql.Types.DATE);
                }
                pstmt.setBoolean(5, account.isGender());
                pstmt.setString(6, account.getPhone());
                pstmt.setBoolean(7, account.isIsUse());
                pstmt.setInt(8, account.getRoleInSystem());
                pstmt.setString(9, account.getAccount());

                pstmt.executeUpdate();
            }
        } catch (SQLException ex) {
            Logger.getLogger(AccountDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            DBUtils.closeConnection(conn);
        }
    }

    @Override
    public void delete(String account) {
        Connection conn = null;
        try {
            conn = DBUtils.getConnection();
            try (PreparedStatement pstmt = conn.prepareStatement(DELETE_SQL)) {
                pstmt.setString(1, account);
                pstmt.executeUpdate();
            }
        } catch (SQLException ex) {
            Logger.getLogger(AccountDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            DBUtils.closeConnection(conn);
        }
    }

    private Account mapResultSetToAccount(ResultSet rs) {
        try {
            Account account = new Account();
            account.setAccount(rs.getString("account"));
            account.setPass(rs.getString("pass"));
            account.setLastName(rs.getString("lastName"));
            account.setFirstName(rs.getString("firstName"));
            account.setBirthday(rs.getDate("birthday"));
            account.setGender(rs.getBoolean("gender"));
            account.setPhone(rs.getString("phone"));
            account.setIsUse(rs.getBoolean("isUse"));
            account.setRoleInSystem(rs.getInt("roleInSystem"));
            return account;
        } catch (SQLException ex) {
            Logger.getLogger(AccountDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
}
