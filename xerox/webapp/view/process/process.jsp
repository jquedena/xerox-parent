<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<tiles:insertDefinition name="default">
    <tiles:putAttribute name="body">
        <script type="text/javascript" src="${pageContext.request.contextPath}/view/process/js/jsProcess.js"></script>
        <div id="pnlTareas" class="ui-accordion ui-widget ui-helper-reset">
            <label class="ui-accordion-header ui-accordion-header-active ui-corner-top ui-widget-header">Reporte de
                Procesos</label>

            <div class="ui-accordion-content ui-helper-reset ui-widget-content ui-corner-bottom">
                <table style="width: 100%;">
                    <tr>
                        <td>
                            <table>
                                <tr>
                                    <td>Proceso:&nbsp;</td>
                                    <td><input id="txtProceso" type="text" style="width: 190px;"></td>
                                    <td style="width: 20px"></td>
                                    <td>Fecha Ejecuci&oacute;n:&nbsp;</td>
                                    <td><input id="txtFechaEjecucion" type="text" class="ui-text ui-text-date"
                                               maxlength="10"/><span class="ui-text-date-icon"
                                                                     onclick="$('#txtFechaEjecucion').focus();"></span>
                                    </td>
                                    <td style="width: 20px"></td>
                                    <td>Estado:&nbsp;</td>
                                    <td><input id="txtEstado" type="text"></td>
                                    <td style="width: 20px"></td>
                                </tr>
                            </table>
                        </td>
                        <td align="right">
                            <button id="btnBuscar">Buscar</button>
                            <button id="btnLimpiar">Limpiar</button>
                        </td>
                    </tr>
                </table>
                <br>

                <div id="pnlProcesos"></div>
            </div>
        </div>
        <form id="frmDescarga" method="post">
            <input name="timestamp" type="hidden" value=""/>
        </form>
    </tiles:putAttribute>
</tiles:insertDefinition>
