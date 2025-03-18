/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import dao.implement.ProductDAO;
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
import models.Product;
import models.Category;

/**
 *
 * @author trann
 */
@WebServlet(name = "ProductController", urlPatterns = {"/product"})
public class ProductController extends HttpServlet {

    private ProductDAO productDAO;
    private CategoryDAO categoryDAO;

    @Override
    public void init() throws ServletException {
        productDAO = new ProductDAO();
        categoryDAO = new CategoryDAO(); // Initialize CategoryDAO
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
                    listProducts(request, response);
                    break;
                case "view":
                    viewProduct(request, response);
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
                action = "create"; // Default action
            }

            switch (action) {
                case "create":
                    createProduct(request, response);
                    break;
                case "update":
                    updateProduct(request, response);
                    break;
                case "delete":
                    deleteProduct(request, response);
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

    private void listProducts(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Fetch all categories
        List<Category> categories = categoryDAO.findAll();
        request.setAttribute("categories", categories);

        // Debug: Log categories
        System.out.println("Categories fetched: " + (categories != null ? categories.size() : "null"));

        // Get selected category typeId (if any)
        String typeIdParam = request.getParameter("typeId");
        List<Product> products;
        if (typeIdParam == null || typeIdParam.equals("all")) {
            products = productDAO.findAll(); // Show all products
        } else {
            try {
                int typeId = Integer.parseInt(typeIdParam);
                products = productDAO.findByTypeId(typeId); // Filter by category
            } catch (NumberFormatException e) {
                products = productDAO.findAll(); // Fallback to all if typeId is invalid
                request.setAttribute("error", "Danh mục không hợp lệ.");
            }
        }
        request.setAttribute("products", products);
        request.setAttribute("selectedTypeId", typeIdParam); // For dropdown persistence

        request.getRequestDispatcher("/view/product/productList.jsp").forward(request, response);
    }

    // View a specific product
    private void viewProduct(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String productId = request.getParameter("productId");
        if (productId == null || productId.trim().isEmpty()) {
            request.setAttribute("error", "Mã sản phẩm là bắt buộc");
            request.getRequestDispatcher("/view/product/productList.jsp").forward(request, response);
            return;
        }
        Product product = productDAO.findById(productId);
        if (product == null) {
            request.setAttribute("error", "Không tìm thấy sản phẩm");
            request.getRequestDispatcher("/view/product/productList.jsp").forward(request, response);
            return;
        }
        request.setAttribute("product", product);
        request.getRequestDispatcher("/view/product/productDetail.jsp").forward(request, response);
    }

    // Show create product form (admin only)
    private void showCreateForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (!isAdmin(request)) {
            response.sendRedirect("auth?action=login");
            return;
        }
        request.getRequestDispatcher("/view/product/productCreate.jsp").forward(request, response);
    }

    // Show update product form (admin only)
    private void showUpdateForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (!isAdmin(request)) {
            response.sendRedirect("auth?action=login");
            return;
        }
        String productId = request.getParameter("productId");
        if (productId == null || productId.trim().isEmpty()) {
            request.setAttribute("error", "Mã sản phẩm là bắt buộc");
            request.getRequestDispatcher("/view/product/productList.jsp").forward(request, response);
            return;
        }
        Product product = productDAO.findById(productId);
        if (product == null) {
            request.setAttribute("error", "Không tìm thấy sản phẩm");
            request.getRequestDispatcher("/view/product/productList.jsp").forward(request, response);
            return;
        }
        request.setAttribute("product", product);
        request.getRequestDispatcher("/view/product/productUpdate.jsp").forward(request, response);
    }

    // Create a new product (admin only)
    private void createProduct(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (!isAdmin(request)) {
            response.sendRedirect("auth?action=login");
            return;
        }
        String productId = request.getParameter("productId");
        String productName = request.getParameter("productName");
        String priceStr = request.getParameter("price");

        if (productId == null || productId.trim().isEmpty() || productName == null || productName.trim().isEmpty()) {
            request.setAttribute("error", "Mã sản phẩm và tên sản phẩm là bắt buộc");
            request.getRequestDispatcher("/view/product/productCreate.jsp").forward(request, response);
            return;
        }

        int price;
        try {
            price = Integer.parseInt(priceStr);
        } catch (NumberFormatException e) {
            request.setAttribute("error", "Định dạng giá không hợp lệ");
            request.getRequestDispatcher("/view/product/productCreate.jsp").forward(request, response);
            return;
        }

        Product newProduct = new Product();
        newProduct.setProductId(productId);
        newProduct.setProductName(productName);
        newProduct.setPrice(price);

        productDAO.create(newProduct);
        response.sendRedirect("product?action=list");
    }

    // Update an existing product (admin only)
    private void updateProduct(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (!isAdmin(request)) {
            response.sendRedirect("auth?action=login");
            return;
        }
        String productId = request.getParameter("productId");
        String productName = request.getParameter("productName");
        String priceStr = request.getParameter("price");

        // Validate required fields
        if (productId == null || productId.trim().isEmpty() || productName == null || productName.trim().isEmpty()) {
            request.setAttribute("error", "Mã sản phẩm và tên sản phẩm là bắt buộc");
            request.getRequestDispatcher("/view/product/productUpdate.jsp").forward(request, response);
            return;
        }

        Product product = productDAO.findById(productId);
        if (product == null) {
            request.setAttribute("error", "Không tìm thấy sản phẩm");
            request.getRequestDispatcher("/view/product/productList.jsp").forward(request, response);
            return;
        }

        // Parse price
        int price;
        try {
            price = Integer.parseInt(priceStr);
        } catch (NumberFormatException e) {
            request.setAttribute("error", "Định dạng giá không hợp lệ");
            request.getRequestDispatcher("/view/product/productUpdate.jsp").forward(request, response);
            return;
        }

        product.setProductName(productName);
        product.setPrice(price);

        productDAO.update(product);
        response.sendRedirect("product?action=list");
    }

    // Delete a product (admin only)
    private void deleteProduct(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (!isAdmin(request)) {
            response.sendRedirect("auth?action=login");
            return;
        }
        String productId = request.getParameter("productId");
        if (productId == null || productId.trim().isEmpty()) {
            request.setAttribute("error", "Mã sản phẩm là bắt buộc");
            request.getRequestDispatcher("/view/product/productList.jsp").forward(request, response);
            return;
        }
        productDAO.delete(productId);
        response.sendRedirect("product?action=list");
    }

    // Check if the user is an admin
    private boolean isAdmin(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            Account user = (Account) session.getAttribute("user");
            return user != null && user.getRoleInSystem() == 1; // Admin check
        }
        return false;
    }
}