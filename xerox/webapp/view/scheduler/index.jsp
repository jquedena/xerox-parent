<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<tiles:insertDefinition name="default">
    <tiles:putAttribute name="body">
        <style>
            #jqgh_tbl_pnlTrigger_exitCode,
            #jqgh_tbl_pnlTrigger_nextFireTime {
                height: 36px;
            }
        </style>
        <script type="text/javascript" src="${pageContext.request.contextPath}/view/scheduler/js/index.js"></script>
        <div id="pnlTareas" class="ui-accordion ui-widget ui-helper-reset">
            <label class="ui-accordion-header ui-accordion-header-active ui-corner-top ui-widget-header">
                <table style="width: 100%">
                    <tr>
                        <td>Estado de las tareas programadas</td>
                        <td align="right">
                            <button id="btnActualizar">Actualizar</button>
                        </td>
                    </tr>
                </table>
            </label>

            <div class="ui-accordion-content ui-helper-reset ui-widget-content ui-corner-bottom">
                <div id="pnlTrigger"></div>
            </div>
        </div>

        <div id="pnlDetalleTarea" class="ui-accordion ui-widget ui-helper-reset hide">
            <label class="ui-accordion-header ui-accordion-header-active ui-corner-top ui-widget-header">
                <table style="width: 100%">
                    <tr>
                        <td>Estado de la &uacute;ltima ejecuci&#243;n</td>
                        <td align="right">
                            <button id="btnVolverPanel">Volver al panel</button>
                        </td>
                    </tr>
                </table>
            </label>

            <div id="pnlDesconocido"
                 class="hide ui-accordion-content ui-helper-reset ui-widget-content ui-corner-bottom">No existe
                informaci&#243;n sobre el estado del trabajo
            </div>
            <div id="pnlDetalleEjecucion"
                 class="hide ui-accordion-content ui-helper-reset ui-widget-content ui-corner-bottom">
                <div>Trabajo</div>
                <div id="pnlJobExecutions"></div>
                <br/>

                <div>Par&aacute;metros</div>
                <div id="pnlParametros"></div>
                <br/>

                <div>Pasos</div>
                <div id="pnlPasos"></div>
            </div>
        </div>

    </tiles:putAttribute>
</tiles:insertDefinition>
