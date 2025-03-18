package controllers;

import dao.implement.AccountDAO;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import models.Account;

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
                action = "list";
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
                case "profileUpdate":
                    showProfileUpdateForm(request, response);
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
                action = "create";
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
                case "profileUpdate":
                    updateProfile(request, response);
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
            request.setAttribute("error", "Mã tài khoản là bắt buộc");
            request.getRequestDispatcher("/view/account/accountList.jsp").forward(request, response);
            return;
        }
        Account account = accountDAO.findByAccount(accountId);
        if (account == null) {
            request.setAttribute("error", "Không tìm thấy tài khoản");
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
            response.sendRedirect("account?action=profileUpdate");
            return;
        }
        String accountId = request.getParameter("accountId");
        if (accountId == null || accountId.trim().isEmpty()) {
            request.setAttribute("error", "Mã tài khoản là bắt buộc");
            request.getRequestDispatcher("/view/account/accountList.jsp").forward(request, response);
            return;
        }
        Account account = accountDAO.findByAccount(accountId);
        if (account == null) {
            request.setAttribute("error", "Không tìm thấy tài khoản");
            request.getRequestDispatcher("/view/account/accountList.jsp").forward(request, response);
            return;
        }
        request.setAttribute("account", account);
        request.getRequestDispatcher("/view/account/accountUpdate.jsp").forward(request, response);
    }

    private void showProfileUpdateForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect("auth?action=login");
            return;
        }
        request.getRequestDispatcher("/view/account/profileUpdate.jsp").forward(request, response);
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
            request.setAttribute("error", "Mã tài khoản và mật khẩu là bắt buộc");
            request.getRequestDispatcher("/view/account/accountCreate.jsp").forward(request, response);
            return;
        }

        if (accountDAO.findByAccount(accountId) != null) {
            request.setAttribute("error", "Mã tài khoản đã tồn tại");
            request.getRequestDispatcher("/view/account/accountCreate.jsp").forward(request, response);
            return;
        }

        Account newAccount = new Account();
        newAccount.setAccount(accountId);
        newAccount.setPass(password);
        newAccount.setLastName(lastName);
        newAccount.setFirstName(firstName);
        newAccount.setGender(false);
        newAccount.setIsUse(true);
        newAccount.setRoleInSystem(3); // Default to Customer

        accountDAO.create(newAccount);
        response.sendRedirect("account?action=list");
    }

    private void updateAccount(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (!isAdmin(request)) {
            response.sendRedirect("account?action=profileUpdate");
            return;
        }
        String accountId = request.getParameter("accountId");
        String roleInSystemStr = request.getParameter("roleInSystem");

        if (accountId == null || accountId.trim().isEmpty()) {
            request.setAttribute("error", "Mã tài khoản là bắt buộc");
            request.getRequestDispatcher("/view/account/accountUpdate.jsp").forward(request, response);
            return;
        }

        Account account = accountDAO.findByAccount(accountId);
        if (account == null) {
            request.setAttribute("error", "Không tìm thấy tài khoản");
            request.getRequestDispatcher("/view/account/accountList.jsp").forward(request, response);
            return;
        }

        HttpSession session = request.getSession(false);
        Account currentUser = (Account) session.getAttribute("user");

        // Restrict changes to current admin's roleInSystem
        if (account.getAccount().equals(currentUser.getAccount())) {
            if (!roleInSystemStr.equals(String.valueOf(currentUser.getRoleInSystem()))) {
                request.setAttribute("error", "Không thể thay đổi vai trò hệ thống của tài khoản đang đăng nhập");
                request.getRequestDispatcher("/view/account/accountUpdate.jsp").forward(request, response);
                return;
            }
        } else {
            try {
                int roleInSystem = Integer.parseInt(roleInSystemStr);
                account.setRoleInSystem(roleInSystem);
            } catch (NumberFormatException e) {
                request.setAttribute("error", "Vai trò hệ thống không hợp lệ");
                request.getRequestDispatcher("/view/account/accountUpdate.jsp").forward(request, response);
                return;
            }
        }

        accountDAO.update(account);
        response.sendRedirect("account?action=list");
    }

    private void updateProfile(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect("auth?action=login");
            return;
        }

        Account currentUser = (Account) session.getAttribute("user");
        String accountId = request.getParameter("accountId");
        if (!accountId.equals(currentUser.getAccount())) {
            request.setAttribute("error", "Bạn chỉ có thể cập nhật thông tin của chính mình");
            request.getRequestDispatcher("/view/account/profileUpdate.jsp").forward(request, response);
            return;
        }

        String password = request.getParameter("password");
        String lastName = request.getParameter("lastName");
        String firstName = request.getParameter("firstName");
        String birthdayStr = request.getParameter("birthday");
        String genderStr = request.getParameter("gender");
        String phone = request.getParameter("phone");

        Account account = accountDAO.findByAccount(accountId);
        if (account == null) {
            request.setAttribute("error", "Không tìm thấy tài khoản");
            request.getRequestDispatcher("/view/account/profileUpdate.jsp").forward(request, response);
            return;
        }

        if (password != null && !password.trim().isEmpty()) {
            account.setPass(password);
        }
        account.setLastName(lastName);
        account.setFirstName(firstName);

        try {
            if (birthdayStr != null && !birthdayStr.trim().isEmpty()) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Date birthday = sdf.parse(birthdayStr);
                account.setBirthday((java.sql.Date) birthday); // Compatible with java.util.Date
            }
        } catch (Exception e) {
            request.setAttribute("error", "Định dạng ngày sinh không hợp lệ");
            request.getRequestDispatcher("/view/account/profileUpdate.jsp").forward(request, response);
            return;
        }

        if (genderStr != null) {
            account.setGender(Boolean.parseBoolean(genderStr));
        }
        account.setPhone(phone);

        accountDAO.update(account);
        session.setAttribute("user", account); // Update session
        response.sendRedirect("view/home.jsp");
    }

    private void deleteAccount(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (!isAdmin(request)) {
            response.sendRedirect("auth?action=login");
            return;
        }
        String accountId = request.getParameter("accountId");
        if (accountId == null || accountId.trim().isEmpty()) {
            request.setAttribute("error", "Mã tài khoản là bắt buộc");
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
        if (!isAdmin(request)) {
            response.sendRedirect("auth?action=login");
            return;
        }
        String accountId = request.getParameter("accountId");
        Account account = accountDAO.findByAccount(accountId);
        if (account == null) {
            request.setAttribute("error", "Tài khoản không tồn tại");
            request.getRequestDispatcher("/view/error.jsp").forward(request, response);
            return;
        }
        account.setIsUse(!account.isIsUse());
        accountDAO.update(account);
        response.sendRedirect(request.getContextPath() + "/account?action=list");
    }
}