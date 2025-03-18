/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import dao.implement.AccountDAO;
import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import models.Account;

/**
 *
 * @author trann
 */
@WebServlet(name = "AccountController", urlPatterns = {"/account"})
public class AccountController extends HttpServlet {

    private AccountDAO accountDAO;

    @Override
    public void init() throws ServletException {
        accountDAO = new AccountDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        try {
            String action = request.getParameter("action");
            if (action == null) {
                action = "list"; // Default action
            }

            switch (action) {
                case "list":
                    listAccounts(request, response);
                    break;
                case "view":
                    viewAccount(request, response);
                    break;
                case "create":
                    showCreateForm(request, response);
                    break;
                case "update":
                    showUpdateForm(request, response);
                    break;
                default:
                    response.sendError(HttpServletResponse.SC_NOT_FOUND, "Action not supported");
                    break;
            }
        } catch (Exception e) {
            request.setAttribute("error", "An unexpected error occurred: " + e.getMessage());
            request.getRequestDispatcher("/view/error.jsp").forward(request, response);
        }

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        try {
            String action = request.getParameter("action");
            if (action == null) {
                action = "create"; // Default action
            }

            switch (action) {
                case "create":
                    createAccount(request, response);
                    break;
                case "update":
                    updateAccount(request, response);
                    break;
                case "delete":
                    deleteAccount(request, response);
                    break;
                case "toggleActive":
                    toggleActive(request, response);
                    break;
                default:
                    response.sendError(HttpServletResponse.SC_NOT_FOUND, "Action not supported");
                    break;
            }
        } catch (Exception e) {
            request.setAttribute("error", "An unexpected error occurred: " + e.getMessage());
            request.getRequestDispatcher("/view/error.jsp").forward(request, response);
        }
    }

    private void listAccounts(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (!isAdmin(request)) {
            response.sendRedirect("auth?action=login");
            return;
        }
        List<Account> accounts = accountDAO.findAll();
        request.setAttribute("accounts", accounts);
        request.getRequestDispatcher("/view/account/accountList.jsp").forward(request, response);
    }

    private void viewAccount(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String accountId = request.getParameter("accountId");
        if (accountId == null || accountId.trim().isEmpty()) {
            request.setAttribute("error", "Account ID is required");
            request.getRequestDispatcher("/view/account/accountList.jsp").forward(request, response);
            return;
        }
        Account account = accountDAO.findByAccount(accountId);
        if (account == null) {
            request.setAttribute("error", "Account not found");
            request.getRequestDispatcher("/view/account/accountList.jsp").forward(request, response);
            return;
        }
        request.setAttribute("account", account);
        request.getRequestDispatcher("/view/account/accountDetail.jsp").forward(request, response);
    }

    private void showCreateForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (!isAdmin(request)) {
            response.sendRedirect("auth?action=login");
            return;
        }
        request.getRequestDispatcher("/view/account/accountCreate.jsp").forward(request, response);
    }

    private void showUpdateForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (!isAdmin(request)) {
            response.sendRedirect("auth?action=login");
            return;
        }
        String accountId = request.getParameter("accountId");
        if (accountId == null || accountId.trim().isEmpty()) {
            request.setAttribute("error", "Account ID is required");
            request.getRequestDispatcher("/view/account/accountList.jsp").forward(request, response);
            return;
        }
        Account account = accountDAO.findByAccount(accountId);
        if (account == null) {
            request.setAttribute("error", "Account not found");
            request.getRequestDispatcher("/view/account/accountList.jsp").forward(request, response);
            return;
        }
        request.setAttribute("account", account);
        request.getRequestDispatcher("/view/account/accountUpdate.jsp").forward(request, response);
    }

    private void createAccount(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (!isAdmin(request)) {
            response.sendRedirect("auth?action=login");
            return;
        }
        String accountId = request.getParameter("accountId");
        String password = request.getParameter("password");
        String lastName = request.getParameter("lastName");
        String firstName = request.getParameter("firstName");

        if (accountId == null || accountId.trim().isEmpty() || password == null || password.trim().isEmpty()) {
            request.setAttribute("error", "Account ID and password are required");
            request.getRequestDispatcher("/view/account/accountCreate.jsp").forward(request, response);
            return;
        }

        if (accountDAO.findByAccount(accountId) != null) {
            request.setAttribute("error", "Account ID already exists");
            request.getRequestDispatcher("/view/account/accountCreate.jsp").forward(request, response);
            return;
        }

        Account newAccount = new Account();
        newAccount.setAccount(accountId);
        newAccount.setPass(password);
        newAccount.setLastName(lastName);
        newAccount.setFirstName(firstName);
        newAccount.setGender(false); // Default value
        newAccount.setIsUse(true);   // Default value
        newAccount.setRoleInSystem(0); // Default value

        accountDAO.create(newAccount);
        response.sendRedirect("account?action=list");
    }

    private void updateAccount(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (!isAdmin(request)) {
            response.sendRedirect("auth?action=login");
            return;
        }
        String accountId = request.getParameter("accountId");
        String password = request.getParameter("password");
        String lastName = request.getParameter("lastName");
        String firstName = request.getParameter("firstName");

        if (accountId == null || accountId.trim().isEmpty()) {
            request.setAttribute("error", "Account ID is required");
            request.getRequestDispatcher("/view/account/accountUpdate.jsp").forward(request, response);
            return;
        }

        Account account = accountDAO.findByAccount(accountId);
        if (account == null) {
            request.setAttribute("error", "Account not found");
            request.getRequestDispatcher("/view/account/accountList.jsp").forward(request, response);
            return;
        }

        if (password != null && !password.trim().isEmpty()) {
            account.setPass(password);
        }
        account.setLastName(lastName);
        account.setFirstName(firstName);

        accountDAO.update(account);
        response.sendRedirect("account?action=list");
    }

    private void deleteAccount(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (!isAdmin(request)) {
            response.sendRedirect("auth?action=login");
            return;
        }
        String accountId = request.getParameter("accountId");
        if (accountId == null || accountId.trim().isEmpty()) {
            request.setAttribute("error", "Account ID is required");
            request.getRequestDispatcher("/view/account/accountList.jsp").forward(request, response);
            return;
        }
        accountDAO.delete(accountId);
        response.sendRedirect("account?action=list");
    }

    private boolean isAdmin(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            Account user = (Account) session.getAttribute("user");
            return user != null && user.getRoleInSystem() == 1;
        }
        return false;
    }
    
    private void toggleActive(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String accountId = request.getParameter("accountId");
        Account account = accountDAO.findByAccount(accountId);
        if (account == null) {
            request.setAttribute("error", "Tài khoản không tồn tại");
            request.getRequestDispatcher("/view/error.jsp").forward(request, response);
            return;
        }

        // Toggle the isUse status
        account.setIsUse(!account.isIsUse());
        accountDAO.update(account); // Update the account in the database
        response.sendRedirect(request.getContextPath() + "/account?action=list");
    }

}
