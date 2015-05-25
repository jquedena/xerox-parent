<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<%@ attribute name="id" required="true" rtexprvalue="true" %>
<%@ attribute name="path" required="true" rtexprvalue="true" %>
<%@ attribute name="items" required="true" rtexprvalue="true" type="java.util.List" %>
<%@ attribute name="headerLabel" required="true" rtexprvalue="true" %>
<%@ attribute name="headerValue" required="true" rtexprvalue="true" %>
<%@ attribute name="itemLabel" required="true" rtexprvalue="true" %>
<%@ attribute name="itemValue" required="true" rtexprvalue="true" %>
<%@ attribute name="cssStyle" required="true" rtexprvalue="true" %>
<%@ attribute name="disabled" required="true" rtexprvalue="true" %>

<form:select id="${id}" path="${path}" cssStyle="${cssStyle}" disabled="${disabled}">
    <option value="${headerValue}">${headerLabel}</option>
    <form:options items="${items}" itemLabel="${itemLabel}" itemValue="${itemValue}"/>
</form:select>