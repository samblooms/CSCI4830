<%@ page import="java.util.List" %>
<%@ page import="Model.Item" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="ISO-8859-1">
    <title>View All Items</title>
    <link rel="stylesheet" type="text/css" href="styles.css">
</head>
<body>
    <div class="container">
        <h1>All Items</h1>
        
        <!-- Form for Sorting, Filtering, and Searching -->
		<form action="InventoryController" method="get" class="item-form">
		    <!-- Sorting -->
		    <label for="sort">Sort By:</label>
			<select name="sort" class="form-control">
			    <option value="id">ID</option>
			    <option value="name">Name</option>
			    <option value="quantity">Quantity</option> <!-- Added line -->
			    <option value="price">Price</option> <!-- Added line -->
			</select>
		    
		    <label for="order">Order:</label>
		    <select name="order" class="form-control">
		        <option value="asc">Ascending</option>
		        <option value="desc">Descending</option>
		    </select>
		    
		    <!-- Filtering (Example for Quantity) -->
		    <label for="minQuantity">Min Quantity:</label>
		    <input type="number" name="minQuantity" class="form-control">
		    
		    <label for="maxQuantity">Max Quantity:</label>
		    <input type="number" name="maxQuantity" class="form-control">
		    
		    <!-- Searching -->
		    <label for="search">Search:</label>
		    <input type="text" name="search" class="form-control">
		    
		    <!-- Submit Button -->
		    <input type="submit" value="Apply" class="button">
		    <input type="hidden" name="action" value="view">
		</form>
        
        <table>
            <tr>
                <th>ID</th>
                <th>Name</th>
                <th>Description</th>
                <th>Quantity</th>
                <th>Price</th>
            </tr>
            <% 
            List<Item> items = (List<Item>) request.getAttribute("items");
            if(items != null) {
                for(Item item : items) {
            %>
                <tr>
                    <td><%= item.getId() %></td>
                    <td><%= item.getName() %></td>
                    <td><%= item.getDescription() %></td>
                    <td><%= item.getQuantity() %></td>
                    <td><%= item.getPrice() %></td>
                </tr>
            <% 
                }
            } else {
            %>
                <tr>
                    <td colspan="5">No items available</td>
                </tr>
            <% 
            }
            %>
        </table>
        <a class="button" href="index.jsp">Back to Main Menu</a>
    </div>
</body>
</html>
