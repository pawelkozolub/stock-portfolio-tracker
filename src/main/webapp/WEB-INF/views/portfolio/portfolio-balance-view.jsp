<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page isELIgnored="false" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Portfolio: </title>
    <link rel="stylesheet" href="https://cdn.simplecss.org/simple.min.css">
</head>
<body>
<div>
    <p><a href="<c:out value="/portfolio"/>">Back to portfolio selection</a></p>
    <h2>Portfolio: <mark>${portfolio.name}</mark></h2>
    <h3>Summary</h3>
    <p>
        Invested:<br/>
        Profit/Loss:<br/>
        Profit/Loss:<br/>
    </p>
    <h3>Available stocks</h3>
    <h3>Transaction history</h3>
    <a class="button" href="/portfolio/${portfolio.id}/buy">Buy stock</a>
    <a class="button" href="/portfolio/${portfolio.id}/sell">Sell stock</a>
    <c:choose>
        <c:when test="${portfolio.transactions.size() > 0}">
            <table>
                <thead>
                <tr>
                    <th>#</th>
                    <th>Date</th>
                    <th>Type</th>
                    <th>Stock</th>
                    <th>Quantity</th>
                    <th>Price</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach items="${portfolio.transactions}" var="transaction" varStatus="ind">
                    <tr>
                        <td><c:out value="${ind.index+1}"/></td>
                        <td><c:out value="${transaction.date}"/></td>
                        <td><c:out value="${transaction.type}"/></td>
                        <td><c:out value="${transaction.stock}"/></td>
                        <td><c:out value="${transaction.quantity}"/></td>
                        <td><c:out value="${transaction.price}"/></td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </c:when>
        <c:otherwise>
            <p>
                No transactions in the portfolio available. Please buy a selected stock.
            </p>
        </c:otherwise>
    </c:choose>
</div>

</body>
</html>
