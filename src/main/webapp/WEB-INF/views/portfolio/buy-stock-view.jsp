<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ page isELIgnored="false" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Add stock</title>
    <link rel="stylesheet" href="https://cdn.simplecss.org/simple.min.css">
</head>
<body>
<div>
    <p><a href="<c:out value="/portfolio/${portfolio.id}"/>">Back to portfolio</a></p>
    <form:form method="post" modelAttribute="stock">
        <label>Stock name: </label>
        <form:input path="stock"/><form:errors path="stock"/><br/>
        <label>Quantity: </label>
        <form:input path="quantity"/><form:errors path="quantity"/><br/>
        <label>Buy price: </label>
        <form:input path="buyPrice"/><form:errors path="buyPrice"/><br/>
        <label>Buy date: </label>
        <form:input path="buyDate"/><form:errors path="buyDate"/><br/>
        <input type="submit" value="Buy stock">
    </form:form>
</div>
</body>
</html>