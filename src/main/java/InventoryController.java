import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.Enumeration;
import java.util.List;

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
            viewItems(request, response);
        } else if ("edit".equals(action)) {
            editItems(request, response);
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        if ("add".equals(action)) {
            addItem(request, response);
        } else if ("edit".equals(action)) {
            updateItems(request, response);
        } else if ("delete".equals(action)) {
            deleteItems(request, response);
        }
    }

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


    private void editItems(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<Item> items = UtilInventoryManagement.listItems();
        request.setAttribute("items", items);
        RequestDispatcher dispatcher = request.getRequestDispatcher("edit-items.jsp");
        dispatcher.forward(request, response);
    }

    private void addItem(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String name = request.getParameter("name");
        String description = request.getParameter("description");
        String quantityStr = request.getParameter("quantity");
        String priceStr = request.getParameter("price");

        if (!name.matches(REGEX_VALID_NAME) ||
                description == null || quantityStr == null || priceStr == null) {
            forwardWithError(request, response, "Invalid input. Please ensure all fields are filled correctly.");
            return;
        }

        int quantity;
        double price;
        try {
            quantity = Integer.parseInt(quantityStr);
            price = Double.parseDouble(priceStr);
        } catch (NumberFormatException e) {
            forwardWithError(request, response, "Invalid quantity or price. Please enter numeric values.");
            return;
        }

        try {
            UtilInventoryManagement.createItem(name, description, quantity, price);
        } catch (Exception e) {
            forwardWithError(request, response, "An error occurred while adding the item. Please try again.");
            return;
        }

        response.sendRedirect("InventoryController?action=view");
    }

    private void updateItems(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Enumeration<String> parameterNames = request.getParameterNames();
        while (parameterNames.hasMoreElements()) {
            String paramName = parameterNames.nextElement();
            if (paramName.startsWith("name_") || paramName.startsWith("description_")
                    || paramName.startsWith("quantity_") || paramName.startsWith("price_")) {
                String[] parts = paramName.split("_");
                int itemId;
                try {
                    itemId = Integer.parseInt(parts[1]);
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                    continue;
                }

                String attribute = parts[0];
                String value = request.getParameter(paramName);
                UtilInventoryManagement.updateItemAttribute(itemId, attribute, value);
            }
        }

        response.sendRedirect("InventoryController?action=view");
    }

    private void deleteItems(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String[] itemIdsToDelete = request.getParameterValues("itemIds");
        UtilInventoryManagement.deleteItems(itemIdsToDelete);
        response.sendRedirect("InventoryController?action=view");
    }

    private void forwardWithError(HttpServletRequest request, HttpServletResponse response, String error)
            throws ServletException, IOException {
        request.setAttribute("error", error);
        RequestDispatcher dispatcher = request.getRequestDispatcher("add-item.jsp");
        dispatcher.forward(request, response);
    }
}
