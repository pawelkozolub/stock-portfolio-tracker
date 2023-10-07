<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ page isELIgnored="false" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Edit portfolio</title>
    <link rel="stylesheet" href="https://cdn.simplecss.org/simple.min.css">
</head>
<body>
<div>
    <p><a href="<c:out value="/portfolio"/>">Back to portfolio selection</a></p>
    <h2>Update portfolio information</h2>
    <form:form method="post" modelAttribute="portfolio">
        <p>
            <form:hidden path="id"/>
            <label>Portfolio name: </label>
            <form:input path="name"/><form:errors path="name"/><br/>
            <label>Portfolio description: </label>
            <form:textarea rows="4" path="description"/><form:errors path="description"/><br/>
        </p>
        <input type="submit" value="Edit portfolio">
    </form:form>
</div>
</body>
</html>
