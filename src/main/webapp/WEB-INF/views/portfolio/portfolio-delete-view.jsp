<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ page isELIgnored="false" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Delete portfolio</title>
    <link rel="stylesheet" href="https://cdn.simplecss.org/simple.min.css">
</head>
<body>
<div>
    <p><a href="<c:out value="/portfolio"/>">Back to portfolio selection</a></p>
    <h2>Delete portfolio</h2>
    <h3><mark>${portfolio.name}</mark> with all its data to be deleted!</h3>
    <p>Please confirm or <a href="<c:out value="/portfolio"/>">Cancel</a><p>
    <form:form method="post" modelAttribute="portfolio">
        <form:hidden path="id"/>
        <form:button>DELETE</form:button>
    </form:form>
</div>
</body>
</html>
