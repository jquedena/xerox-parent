<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<tiles:insertDefinition name="default">
    <tiles:putAttribute name="body">
        <table align="center">
            <tr>
                <td>
                    <img src="${pageContext.request.contextPath}/public/img/marca/fondo.png" style="margin: auto;"/>
                </td>
            </tr>
        </table>
    </tiles:putAttribute>
</tiles:insertDefinition>