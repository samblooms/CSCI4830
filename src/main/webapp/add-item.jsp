<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Add Item</title>
    <link rel="stylesheet" type="text/css" href="styles.css">
</head>
<body>
    <div class="container">
    	<nav>
        	<a href="index.jsp" class="button">Back to Main Menu</a>
            <a href="InventoryController?action=view" class="button">View All Items</a>
            <a href="InventoryController?action=view" class="button">Edit Items</a>
        </nav>
        <h1>Add New Item</h1>
        <% String error = (String) request.getAttribute("error"); %>
        <% if (error != null) { %>
            <p class="error"><%= error %></p>
        <% } %>
        <form action="InventoryController" method="post" class="item-form">
            <label for="name">Name:</label>
            <input type="text" name="name" required pattern="^[a-zA-Z0-9\s]+$" title="Only alphanumeric characters and spaces allowed"><br>
            
            <label for="description">Description:</label>
            <input type="text" name="description" required><br>
            
            <label for="quantity">Quantity:</label>
            <input type="number" name="quantity" required min="1"><br>
            
            <label for="price">Price:</label>
            <input type="number" step="0.01" name="price" required min="0"><br>
            
            <input type="submit" value="Add Item" class="button">
            <input type="hidden" name="action" value="add">
        </form>
    </div>
</body>
</html>
