/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import dao.implement.CategoryDAO;
import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import models.Account;
import models.Category;

/**
 *
 * @author trann
 */
@WebServlet(name = "CategoryController", urlPatterns = {"/category"})
public class CategoryController extends HttpServlet {

    private CategoryDAO categoryDAO;

    @Override
    public void init() throws ServletException {
        categoryDAO = new CategoryDAO();
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
                    listCategories(request, response);
                    break;
                case "view":
                    viewCategory(request, response);
                    break;
                case "create":
                    showCreateForm(request, response);
                    break;
                case "update":
                    showUpdateForm(request, response);
                    break;
                default:
                    response.sendError(HttpServletResponse.SC_NOT_FOUND, "Hành động không được hỗ trợ");
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
                action = "create"; // Mặc định là tạo mới
            }

            switch (action) {
                case "create":
                    createCategory(request, response);
                    break;
                case "update":
                    updateCategory(request, response);
                    break;
                case "delete":
                    deleteCategory(request, response);
                    break;
                default:
                    response.sendError(HttpServletResponse.SC_NOT_FOUND, "Hành động không được hỗ trợ");
                    break;
            }
        } catch (Exception e) {
            request.setAttribute("error", "An unexpected error occurred: " + e.getMessage());
            request.getRequestDispatcher("/view/error.jsp").forward(request, response);
        }
    }

    private void listCategories(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<Category> categories = categoryDAO.findAll();
        request.setAttribute("categories", categories);
        request.getRequestDispatcher("/view/category/categoryList.jsp").forward(request, response);
    }

    private void viewCategory(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String typeIdStr = request.getParameter("typeId");
        if (typeIdStr == null || typeIdStr.trim().isEmpty()) {
            request.setAttribute("error", "Yêu cầu nhập ID danh mục");
            request.getRequestDispatcher("/view/category/categoryList.jsp").forward(request, response);
            return;
        }
        int typeId = Integer.parseInt(typeIdStr);
        Category category = categoryDAO.findById(typeId);
        if (category == null) {
            request.setAttribute("error", "Không tìm thấy danh mục");
            request.getRequestDispatcher("/view/category/categoryList.jsp").forward(request, response);
            return;
        }
        request.setAttribute("category", category);
        request.getRequestDispatcher("/view/category/categoryDetail.jsp").forward(request, response);
    }

    private void showCreateForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (!isAdmin(request)) {
            response.sendRedirect("auth?action=login");
            return;
        }
        request.getRequestDispatcher("/view/category/categoryCreate.jsp").forward(request, response);
    }

    private void showUpdateForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (!isAdmin(request)) {
            response.sendRedirect("auth?action=login");
            return;
        }
        String typeIdStr = request.getParameter("typeId");
        if (typeIdStr == null || typeIdStr.trim().isEmpty()) {
            request.setAttribute("error", "Yêu cầu nhập ID danh mục");
            request.getRequestDispatcher("/view/category/categoryList.jsp").forward(request, response);
            return;
        }
        int typeId = Integer.parseInt(typeIdStr);
        Category category = categoryDAO.findById(typeId);
        if (category == null) {
            request.setAttribute("error", "Không tìm thấy danh mục");
            request.getRequestDispatcher("/view/category/categoryList.jsp").forward(request, response);
            return;
        }
        request.setAttribute("category", category);
        request.getRequestDispatcher("/view/category/categoryUpdate.jsp").forward(request, response);
    }

    private void createCategory(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (!isAdmin(request)) {
            response.sendRedirect("auth?action=login");
            return;
        }
        String categoryName = request.getParameter("categoryName");
        String memo = request.getParameter("memo");

        if (categoryName == null || categoryName.trim().isEmpty()) {
            request.setAttribute("error", "Yêu cầu nhập tên danh mục");
            request.getRequestDispatcher("/view/category/categoryCreate.jsp").forward(request, response);
            return;
        }

        Category newCategory = new Category();
        newCategory.setCategoryName(categoryName);
        newCategory.setMemo(memo);

        categoryDAO.create(newCategory);
        response.sendRedirect("category?action=list");
    }

    private void updateCategory(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (!isAdmin(request)) {
            response.sendRedirect("auth?action=login");
            return;
        }
        String typeIdStr = request.getParameter("typeId");
        String categoryName = request.getParameter("categoryName");
        String memo = request.getParameter("memo");

        if (typeIdStr == null || typeIdStr.trim().isEmpty() || categoryName == null || categoryName.trim().isEmpty()) {
            request.setAttribute("error", "Yêu cầu nhập ID và tên danh mục");
            request.getRequestDispatcher("/view/category/categoryUpdate.jsp").forward(request, response);
            return;
        }

        int typeId = Integer.parseInt(typeIdStr);
        Category category = categoryDAO.findById(typeId);
        if (category == null) {
            request.setAttribute("error", "Không tìm thấy danh mục");
            request.getRequestDispatcher("/view/category/categoryList.jsp").forward(request, response);
            return;
        }

        category.setCategoryName(categoryName);
        category.setMemo(memo);
        categoryDAO.update(category);
        response.sendRedirect("category?action=list");
    }

    private void deleteCategory(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (!isAdmin(request)) {
            response.sendRedirect("auth?action=login");
            return;
        }
        String typeIdStr = request.getParameter("typeId");
        if (typeIdStr == null || typeIdStr.trim().isEmpty()) {
            request.setAttribute("error", "Yêu cầu nhập ID danh mục");
            request.getRequestDispatcher("/view/category/categoryList.jsp").forward(request, response);
            return;
        }
        int typeId = Integer.parseInt(typeIdStr);
        categoryDAO.delete(typeId);
        response.sendRedirect("category?action=list");
    }

    private boolean isAdmin(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            Account user = (Account) session.getAttribute("user");
            return user != null && user.getRoleInSystem() == 1;
        }
        return false;
    }

}
