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
