<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html>
<head>
    <title>My Addresses</title>
    <style>

        body {
            background: #f6f6f6;
            font-family: Arial, sans-serif;
        }

        .container {
            width: 85%;
            margin: 30px auto;
        }

        h2 {
            color: #333;
        }

        .address-card {
            background: white;
            padding: 15px;
            margin-bottom: 18px;
            border-radius: 8px;
            border: 1px solid #ddd;
        }

        .btn {
            padding: 8px 14px;
            border-radius: 4px;
            color: white;
            text-decoration: none;
            display: inline-block;
            margin-top: 10px;
        }

        .btn-edit {
            background: #0077cc;
        }

        .btn-delete {
            background: #cc0000;
        }

        .btn-save {
            background: #28a745;
            border: none;
            cursor: pointer;
        }

        .form-card {
            background: #ffffff;
            padding: 20px;
            margin-top: 25px;
            border-radius: 8px;
            border: 1px solid #ccc;
        }

        input[type="text"] {
            width: 98%;
            padding: 8px;
            margin-bottom: 12px;
            border-radius: 4px;
            border: 1px solid #aaa;
        }

    </style>
</head>
<body>

<div class="container">

    <h2>My Saved Addresses</h2>

    <!-- If no addresses -->
    <c:if test="${empty addresses}">
        <p>No addresses added yet.</p>
    </c:if>

    <!-- Address List -->
    <c:forEach var="addr" items="${addresses}">
        <div class="address-card">
            <strong>${addr.street}</strong><br>
            ${addr.city}, ${addr.state} - ${addr.zip} <br>
            Landmark: ${addr.landmark} <br>

            <a class="btn btn-edit" href="addresses?action=edit&addressId=${addr.addressId}">
                Edit
            </a>

            <form action="addresses" method="post" style="display:inline;">
                <input type="hidden" name="action" value="delete">
                <input type="hidden" name="addressId" value="${addr.addressId}">
                <button type="submit" class="btn btn-delete"
                        onclick="return confirm('Delete this address?');">
                    Delete
                </button>
            </form>
        </div>
    </c:forEach>


    <!-- ADD / EDIT FORM -->
    <div class="form-card">

        <c:choose>

            <!-- Edit Form -->
            <c:when test="${not empty editAddress}">
                <h3>Edit Address</h3>

                <form action="addresses" method="post">

                    <input type="hidden" name="action" value="update"/>
                    <input type="hidden" name="addressId" value="${editAddress.addressId}"/>

                    <label>Street:</label><br>
                    <input type="text" name="street" value="${editAddress.street}" required>

                    <label>City:</label><br>
                    <input type="text" name="city" value="${editAddress.city}" required>

                    <label>State:</label><br>
                    <input type="text" name="state" value="${editAddress.state}" required>

                    <label>ZIP:</label><br>
                    <input type="text" name="zip" value="${editAddress.zip}" required>

                    <label>Landmark:</label><br>
                    <input type="text" name="landmark" value="${editAddress.landmark}">

                    <button type="submit" class="btn btn-save">Save Changes</button>

                </form>

            </c:when>

            <!-- Add Form -->
            <c:otherwise>
                <h3>Add New Address</h3>

                <form action="addresses" method="post">

                    <input type="hidden" name="action" value="add"/>

                    <label>Street:</label><br>
                    <input type="text" name="street" required>

                    <label>City:</label><br>
                    <input type="text" name="city" required>

                    <label>State:</label><br>
                    <input type="text" name="state" required>

                    <label>ZIP:</label><br>
                    <input type="text" name="zip" required>

                    <label>Landmark:</label><br>
                    <input type="text" name="landmark">

                    <button type="submit" class="btn btn-save">Add Address</button>

                </form>
            </c:otherwise>

        </c:choose>

    </div>

</div>

</body>
</html>
