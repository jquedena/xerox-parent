<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<tiles:insertDefinition name="default">
    <tiles:putAttribute name="body">
        <script type="text/javascript" src="${pageContext.request.contextPath}/view/upload/js/jsUpload.js"></script>
        <div id="pnlTareas" class="ui-accordion ui-widget ui-helper-reset">
            <label class="ui-accordion-header ui-accordion-header-active ui-corner-top ui-widget-header">Carga de
                Archivos</label>

            <div class="ui-accordion-content ui-helper-reset ui-widget-content ui-corner-bottom">
                <table>
                    <tr>
                        <td>Tipo de Archivo:</td>
                        <td><input type="text" id="txtTipoArchivo" style="width: 240px;"/></td>
                        <td colspan="2"></td>

                    </tr>
                    <tr>
                        <td>Nombre del Archivo:</td>
                        <td>
                            <div class="ui-label" id="lblNombreArchivo" style="width: 450px;"/>
                        </td>
                        <td>
                            <div class="button-file">
                                <button id="btnFileUpload" class="button-file-1">Examinar</button>
                                <form id="frmUpload" method="post" enctype="multipart/form-data">
	                                <input name="fileUpload" id="fileUpload" type="file" class="hide"/>
	                                <input id="idParametro"   name="idParametro"   type="hidden" />
	                                <input id="nombreArchivo" name="nombreArchivo" type="hidden" />
                                </form>
                            </div>
                        </td>
                        <td>
                            <button id="btnUpload">Subir</button>
                        </td>
                    </tr>
                </table>
            </div>
        </div>
    </tiles:putAttribute>
</tiles:insertDefinition>
