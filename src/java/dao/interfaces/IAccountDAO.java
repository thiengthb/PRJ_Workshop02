/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao.interfaces;

import java.util.List;
import models.Account;

/**
 *
 * @author trann
 */
public interface IAccountDAO {
    void create(Account account);
    Account findByAccount(String account);
    List<Account> findAll();
    void update(Account account);
    void delete(String account);
}
