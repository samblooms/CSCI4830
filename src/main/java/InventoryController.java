

import java.io.IOException;

import java.util.List;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import Model.Item;
import Util.UtilInventoryManagement;


@WebServlet("/InventoryController")
public class InventoryController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final String REGEX_VALID_NAME = "^[a-zA-Z0-9\\s]+$";
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String action = request.getParameter("action");
        if ("view".equals(action)) {
            // Extracted method for viewing items
            viewItems(request, response);
        }
        // Additional GET actions can be added here
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String action = request.getParameter("action");
        if ("add".equals(action)) {
            // Extracted method for adding an item
            addItem(request, response);
        }
        // Additional POST actions can be added here
    }

    // Separated logic for viewing items into its own method
    private void viewItems(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String sort = request.getParameter("sort");
        String order = request.getParameter("order");
        String minQuantity = request.getParameter("minQuantity");
        String maxQuantity = request.getParameter("maxQuantity");
        String search = request.getParameter("search");
        
        List<Item> items = UtilInventoryManagement.listItems(sort, order, minQuantity, maxQuantity, search);
        request.setAttribute("items", items);
        RequestDispatcher dispatcher = request.getRequestDispatcher("view-items.jsp");
        dispatcher.forward(request, response);
    }

    // Separated logic for adding an item into its own method
    private void addItem(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String name = request.getParameter("name");
        String description = request.getParameter("description");
        String quantityStr = request.getParameter("quantity");
        String priceStr = request.getParameter("price");

        // Validate inputs
        if (!name.matches(REGEX_VALID_NAME) || 
            description == null || quantityStr == null || priceStr == null) {
            forwardWithError(request, response, "Invalid input. Please ensure all fields are filled correctly.");
            return;
        }

        // Parse and validate numeric fields
        int quantity;
        double price;
        try {
            quantity = Integer.parseInt(quantityStr);
            price = Double.parseDouble(priceStr);
        } catch (NumberFormatException e) {
            forwardWithError(request, response, "Invalid quantity or price. Please enter numeric values.");
            return;
        }

        // Create an Item object and add it to the database
        try {
            UtilInventoryManagement.createItem(name, description, quantity, price);
        } catch (Exception e) {
            forwardWithError(request, response, "An error occurred while adding the item. Please try again.");
            return;
        }
        
        // Redirect to view all items
        response.sendRedirect("InventoryController?action=view");
    }

    // Method to forward to the add-item page with an error message
    private void forwardWithError(HttpServletRequest request, HttpServletResponse response, String error)
            throws ServletException, IOException {
        request.setAttribute("error", error);
        RequestDispatcher dispatcher = request.getRequestDispatcher("add-item.jsp");
        dispatcher.forward(request, response);
    }
}

