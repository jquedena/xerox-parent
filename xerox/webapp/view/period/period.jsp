<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<tiles:insertDefinition name="default">
    <tiles:putAttribute name="body">
        <script type="text/javascript" src="${pageContext.request.contextPath}/view/period/js/jsPeriod.js"></script>
        <div id="pnlPeriodos" class="ui-accordion ui-widget ui-helper-reset">
            <label class="ui-accordion-header ui-accordion-header-active ui-corner-top ui-widget-header">Consulta de
                Periodos</label>

            <div class="ui-accordion-content ui-helper-reset ui-widget-content ui-corner-bottom">
                <table style="width: 100%; padding: 10px;">
                    <tr>
                        <td align="right">
                            <button id="btnActivar"> Activar</button>
                        </td>
                    </tr>
                    <tr>
                        <td align="center">
                            <br>

                            <div id="pnlListadoPeriodos" style="margin: auto;"></div>
                        </td>
                    </tr>
                </table>
            </div>
        </div>
    </tiles:putAttribute>
</tiles:insertDefinition>