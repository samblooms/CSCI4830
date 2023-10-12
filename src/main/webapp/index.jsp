<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="ISO-8859-1">
    <title>Inventory Management</title>
    <link rel="stylesheet" type="text/css" href="styles.css">
</head>
<body>
    <div class="container">
        <h1>Welcome to the Inventory Management System</h1>
        <nav>
            <a class="button" href="add-item.jsp">Add New Item</a>
            <a class="button" href="InventoryController?action=view">View All Items</a>
            <a class="button" href="InventoryController?action=edit">Edit Items</a>
        </nav>
    </div>
</body>
</html>
