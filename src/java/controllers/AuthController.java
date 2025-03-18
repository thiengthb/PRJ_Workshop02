/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import dao.implement.AccountDAO;
import java.io.IOException;
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
@WebServlet(name = "AuthController", urlPatterns = {"/auth"})
public class AuthController extends HttpServlet {

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
                action = "login";
            }
            switch (action) {
                case "login":
                    showLoginPage(request, response);
                    break;
                case "logout":
                    handleLogout(request, response);
                    break;
                case "register":
                    showRegisterPage(request, response);
                    break;
                case "resetPassword":
                    showResetPasswordPage(request, response);
                    break;
                default:
                    response.sendError(HttpServletResponse.SC_NOT_FOUND, "Hành động không được hỗ trợ");
                    break;
            }
        } catch (Exception e) {
            request.setAttribute("error", "Đã xảy ra lỗi không mong muốn: " + e.getMessage());
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
                action = "login";
            }
            switch (action) {
                case "login":
                    handleLogin(request, response);
                    break;
                case "register":
                    handleRegister(request, response);
                    break;
                case "resetPassword":
                    handleResetPassword(request, response);
                    break;
                default:
                    response.sendError(HttpServletResponse.SC_NOT_FOUND, "Hành động không được hỗ trợ");
                    break;
            }
        } catch (Exception e) {
            request.setAttribute("error", "Đã xảy ra lỗi không mong muốn: " + e.getMessage());
            request.getRequestDispatcher("/view/error.jsp").forward(request, response);
        }
    }

    private String redirectURL(int role)
            throws ServletException, IOException {
        switch (role) {
            case 1:
                return "/view/admin/dashboard.jsp";
            case 2:
                return "/view/manager/dashboard.jsp";
            default:
                return "/view/home.jsp";
        }
    }

    private void showLoginPage(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("user") != null) {
            Account user = (Account) session.getAttribute("user");
            String url = redirectURL(user.getRoleInSystem());
            response.sendRedirect(request.getContextPath() + url);
        } else {
            request.getRequestDispatcher("/view/auth/login.jsp").forward(request, response);
        }
    }

    private void handleLogin(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String account = request.getParameter("account");
        String password = request.getParameter("password");

        if (account == null || account.trim().isEmpty() || password == null || password.trim().isEmpty()) {
            request.setAttribute("error", "Vui lòng nhập cả tên đăng nhập và mật khẩu");
            request.getRequestDispatcher("/view/auth/login.jsp").forward(request, response);
            return;
        }

        Account user = accountDAO.findByAccount(account);
        if (user == null || !user.getPass().equals(password)) {
            request.setAttribute("error", "Tên đăng nhập hoặc mật khẩu không đúng");
            request.getRequestDispatcher("/view/auth/login.jsp").forward(request, response);
            return;
        }

        if (!user.isIsUse()) {
            request.setAttribute("error", "Tài khoản của bạn đã bị vô hiệu hóa. Vui lòng liên hệ quản trị viên.");
            request.getRequestDispatcher("/view/auth/login.jsp").forward(request, response);
            return;
        }

        HttpSession session = request.getSession();
        session.setAttribute("user", user);
        String url = redirectURL(user.getRoleInSystem());
        response.sendRedirect(request.getContextPath() + url);
    }

    private void handleLogout(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        response.sendRedirect("auth?action=login");
    }

    private void showRegisterPage(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/view/auth/register.jsp").forward(request, response);
    }

    private void handleRegister(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String account = request.getParameter("account");
        String password = request.getParameter("password");
        String lastName = request.getParameter("lastName");
        String firstName = request.getParameter("firstName");

        if (accountDAO.findByAccount(account) != null) {
            request.setAttribute("error", "Tên đăng nhập đã tồn tại");
            request.getRequestDispatcher("/view/auth/register.jsp").forward(request, response);
            return;
        }

        Account newUser = new Account();
        newUser.setAccount(account);
        newUser.setPass(password);
        newUser.setLastName(lastName);
        newUser.setFirstName(firstName);
        newUser.setGender(false);
        newUser.setIsUse(true); // New users are active by default
        newUser.setRoleInSystem(3);

        accountDAO.create(newUser);
        response.sendRedirect("auth?action=login");
    }

    private void showResetPasswordPage(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/view/auth/resetPassword.jsp").forward(request, response);
    }

    private void handleResetPassword(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String account = request.getParameter("account");
        String newPassword = request.getParameter("newPassword");

        Account user = accountDAO.findByAccount(account);
        if (user == null) {
            request.setAttribute("error", "Tài khoản không tồn tại");
            request.getRequestDispatcher("/view/auth/resetPassword.jsp").forward(request, response);
            return;
        }

        user.setPass(newPassword);
        accountDAO.update(user);
        request.setAttribute("message", "Đặt lại mật khẩu thành công");
        request.getRequestDispatcher("/view/auth/login.jsp").forward(request, response);
    }

}
