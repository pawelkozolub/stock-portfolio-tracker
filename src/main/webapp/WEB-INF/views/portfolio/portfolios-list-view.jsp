<%@ page isELIgnored="false" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<html>
<head>
    <title>Portfolio index</title>
    <link rel="stylesheet" href="https://cdn.simplecss.org/simple.min.css">
</head>
<body>
<div>
    <h2>Available stock portfolios</h2>
    <c:choose>
        <c:when test="${portfolioList.size() > 0}">
            <table>
                <thead>
                    <tr>
                        <th>#</th>
                        <th>Portfolio name</th>
                        <th>Description</th>
                        <th>Action</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach items="${portfolioList}" var="portfolio" varStatus="ind">
                        <tr>
                            <td><c:out value="${ind.index+1}"/></td>
                            <td><c:out value="${portfolio.name}"/></td>
                            <td><c:out value="${portfolio.description}"/></td>
                            <td>
                                <a href="<c:out value="/portfolio/${portfolio.id}"/>">View</a>
                                <a href="<c:out value="/"/>">Edit</a>
                                <a href="<c:out value="/"/>">Delete</a>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </c:when>
        <c:otherwise>
            <p>
                No stock portfolios available. Please create a new one.
            </p>
        </c:otherwise>
    </c:choose>

<%--    <hr/>--%>
    <h4>Create a new stock portfolio</h4>
    <form:form method="post" modelAttribute="portfolio">
        <p>
            <label>Portfolio name: </label>
            <form:input path="name"/><form:errors path="name"/><br/>
            <label>Portfolio description: </label>
            <form:textarea rows="4" path="description"/><form:errors path="description"/><br/>
        </p>
        <input type="submit" value="Create portfolio">
    </form:form>

</div>
</body>
</html>
