<%@ page import="java.util.List" %>
<%@ page import="Model.Item" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="ISO-8859-1">
    <title>Edit Items</title>
    <link rel="stylesheet" type="text/css" href="styles.css">
</head>
<body>
    <div class="container">
    	<nav>
	        <a class="button" href="index.jsp">Back to Main Menu</a>
	        <a class="button" href="add-item.jsp">Add New Item</a>
	        <a class="button" href="InventoryController?action=view">View Items</a>
        </nav>
        <h1>Edit Items</h1>
        
        <form action="InventoryController" method="post" id="editForm">
            <table>
                <tr>
                    <th>ID</th>
                    <th>Name</th>
                    <th>Description</th>
                    <th>Quantity</th>
                    <th>Price</th>
                    <th>Select</th>
                </tr>
                <% 
                List<Item> items = (List<Item>) request.getAttribute("items");
                if(items != null) {
                    for(Item item : items) {
                %>
                    <tr>
                        <td><%= item.getId() %></td>
                        <td><input type="text" name="name_<%= item.getId() %>" value="<%= item.getName() %>"></td>
                        <td><input type="text" name="description_<%= item.getId() %>" value="<%= item.getDescription() %>"></td>
                        <td><input type="number" name="quantity_<%= item.getId() %>" value="<%= item.getQuantity() %>"></td>
                        <td><input type="number" step="0.01" name="price_<%= item.getId() %>" value="<%= item.getPrice() %>"></td>
                        <td><input type="checkbox" name="itemIds" value="<%= item.getId() %>"></td>
                    </tr>
                <% 
                    }
                } else {
                %>
                    <tr>
                        <td colspan="6">No items available</td>
                    </tr>
                <% 
                }
                %>
            </table>
            <input type="hidden" name="action" value="edit">
            <input type="submit" id="editButton" class="button" value="Save Changes">
            <button id="deleteButton" class="button" style="display:none;">Delete Selected Items</button>
        </form>
    </div>

   <script>
   document.addEventListener("DOMContentLoaded", function() {
	    const checkboxes = document.querySelectorAll("input[type=checkbox]");
	    const editButton = document.getElementById("editButton");
	    const deleteButton = document.getElementById("deleteButton");
	    const editForm = document.getElementById("editForm");

	    checkboxes.forEach(checkbox => {
	        checkbox.addEventListener("change", function() {
	            const isAnyChecked = [...checkboxes].some(ch => ch.checked);
	            deleteButton.style.display = isAnyChecked ? "inline-block" : "none";
	        });
	    });

	    editButton.addEventListener("click", function(event) {
	        event.preventDefault();
	        if(confirm("Are you sure you want to save changes?")) {
	            editForm.action = "InventoryController?action=edit";
	            editForm.submit();
	        }
	    });

	    deleteButton.addEventListener("click", function(event) {
	        event.preventDefault();
	        if(confirm("Are you sure you want to delete the selected items?")) {
	            editForm.action = "InventoryController?action=delete";
	            editForm.submit();
	        }
	    });
	});
	</script>
</body>
</html>
