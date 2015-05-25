<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@taglib prefix="formExt" tagdir="/WEB-INF/tags" %>
<tiles:insertDefinition name="default">
    <tiles:putAttribute name="body">
        <script type="text/javascript"
                src="${pageContext.request.contextPath}/view/params/js/jsConfiguracionParametria.js"></script>
        <div class="ui-accordion ui-widget ui-helper-reset">
            <label class="ui-accordion-header ui-accordion-header-active ui-corner-top ui-widget-header">Par&aacute;metros</label>

            <div class="ui-accordion-content ui-helper-reset ui-widget-content ui-corner-bottom">
                <input type="hidden" id="hidFiltroTipo"/>
                <input type="hidden" id="hidFiltroPadre"/>
                <input type="hidden" id="hidFiltroEstado"/>
                <input type="hidden" id="hidFiltroNombre"/>

                <table style="width:100%" border="0" cellpadding="2">
                    <tr>
                        <td><p class="typeFont">Tipo de par&aacute;metro:</p></td>
                        <td>
                            <formExt:select
                                    id="cmbFiltroTipo"
                                    path="paramModel.idTipo"
                                    items="${paramModel.listaTipos}"
                                    headerLabel="[Seleccione]"
                                    headerValue="-1"
                                    itemLabel="nombre"
                                    itemValue="id"
                                    cssStyle="width: 100px;"
                                    disabled="false"/>
                        </td>
                        <td><p class="typeFont">Par&aacute;metro padre:</p></td>
                        <td>
                            <formExt:select
                                    id="cmbFiltroPadre"
                                    path="paramModel.idPadre"
                                    items="${paramModel.listaPadres}"
                                    headerLabel="[Seleccione]"
                                    headerValue="-1"
                                    itemLabel="nombre"
                                    itemValue="id"
                                    cssStyle="width: 500px;"
                                    disabled="true"/>
                        </td>
                    </tr>
                    <tr>
                        <td><p class="typeFont">Estado:</p></td>
                        <td>
                            <formExt:select
                                    id="cmbFiltroEstado"
                                    path="paramModel.idEstado"
                                    items="${paramModel.listaEstados}"
                                    headerLabel="[Todos]"
                                    headerValue="-1"
                                    itemLabel="descripcion"
                                    itemValue="estado"
                                    cssStyle="width: 100px;"
                                    disabled="false"/>
                        </td>
                        <td><p class="typeFont">Nombre:</p></td>
                        <td>
                            <input id="txtFiltroNombre" name="paramModel.nombre" type="text" style="width: 250px;"
                                   maxlength="100" class="ui-text"/>
                        </td>
                    </tr>
                    <tr>
                        <td align="right" colspan="4">
                            <button id="btnBuscar">Buscar</button>
                            <button id="btnLimpiar">Limpiar</button>
                            <button id="btnNuevo">Nuevo</button>
                            <button id="btnModificar">Modificar</button>
                        </td>
                    </tr>
                </table>
                <br>
                <table style="width: 100%" cellpadding="2">
                    <tr>
                        <td>
                            <div id="pnlParametria"></div>
                        </td>
                    </tr>
                </table>
                <div id="dialogParametria" style="display: none;">
                    <input type="hidden" id="idParametro"/>
                    <table style="width: 100%;" cellpadding="2">
                        <tr>
                            <td style="width: 120px;" valign="top">Tipo Par&aacute;metro :</td>
                            <td>
                                <formExt:select
                                        path="idEdicionTipo"
                                        headerValue="-1"
                                        headerLabel="[Seleccione]"
                                        items="${paramModel.listaTipos}"
                                        itemValue="id"
                                        itemLabel="nombre"
                                        cssStyle="width: 100px;"
                                        id="cmbEdicionTipo"
                                        disabled="true"/>
                            </td>
                        </tr>
                        <tr id="row_padre">
                            <td valign="top">Par&aacute;metro Padre :</td>
                            <td>
                                <formExt:select
                                        path="idEdicionPadre"
                                        headerValue="-1"
                                        headerLabel="[Seleccione]"
                                        items="${paramModel.listaPadresPermiteHijos}"
                                        itemValue="id"
                                        itemLabel="nombre"
                                        cssStyle="width: 500px;"
                                        id="cmbEdicionPadre"
                                        disabled="false"/>
                                <input type="text" id="txtEdicionPadre" style="width: 500px;" disabled="disabled"/>
                            </td>
                        </tr>
                        <tr>
                            <td valign="top">Nombre :</td>
                            <td><input type="text" class="ui-text" id="txtEdicionNombre" style="width: 500px;"
                                       maxlength="300"/></td>
                        </tr>

                        <tr>
                            <td valign="top">Descripci&oacute;n :</td>
                            <td valign="top"><textarea rows="4" cols="100" class="ui-text" id="txtEdicionDescripcion"
                                                       maxlength="500"></textarea></td>
                        </tr>

                        <tr>
                            <td valign="top">Estado:</td>
                            <td>
                                <formExt:select
                                        path="idEdicionEstado"
                                        headerValue="-1"
                                        headerLabel="[Seleccione]"
                                        items="${paramModel.listaEstados}"
                                        itemValue="estado"
                                        itemLabel="descripcion"
                                        cssStyle="width: 100px;"
                                        id="cmbEdicionEstado"
                                        disabled="false"/>
                            </td>
                        </tr>
                        <tr class="row_dinamico" id="row_codigo">
                            <td valign="top">
                                <label id="lblCodigoEti" class="etiqueta"></label>
                                <input type="checkbox" id="chkCodigoHabil" class="habil" style="display: none;"/>
                            </td>
                            <td valign="top">
                                <input type="text" class="ui-text" id="txtEdicionCodigo" style="width: 100px;"
                                       maxlength="50"/>
                            </td>
                        </tr>
                        <tr class="row_dinamico" id="row_entero">
                            <td valign="top">
                                <label id="lblEnteroEti" class="etiqueta"></label>
                                <input type="checkbox" id="chkEnteroHabil" class="habil" style="display: none;"/>
                            </td>
                            <td valign="top">
                                <input type="text" class="ui-text" id="txtEdicionEntero" style="width: 120px;"
                                       maxlength="10"/>
                            </td>
                        </tr>
                        <tr class="row_dinamico" id="row_decimal">
                            <td valign="top">
                                <label id="lblDecimalEti" class="etiqueta"></label>
                                <input type="checkbox" id="chkDecimalHabil" class="habil" style="display: none;"/>
                            </td>
                            <td valign="top">
                                <input type="text" class="ui-text" id="txtEdicionDecimal" style="width: 120px;"
                                       maxlength="18"/>
                            </td>
                        </tr>
                        <tr class="row_dinamico" id="row_texto">
                            <td valign="top">
                                <label id="lblTextoEti" class="etiqueta"></label>
                                <input type="checkbox" id="chkTextoHabil" class="habil" style="display: none;"/>
                            </td>
                            <td valign="top">
                                <textarea rows="4" cols="100" class="ui-text" id="txtEdicionTexto"
                                          maxlength="500"></textarea>
                            </td>
                        </tr>
                        <tr class="row_dinamico" id="row_texto2">
                            <td valign="top">
                                <label id="lblTextoEti2" class="etiqueta"></label>
                                <input type="checkbox" id="chkTextoHabil2" class="habil" style="display: none;"/>
                            </td>
                            <td valign="top">
                                <textarea rows="4" cols="100" class="ui-text" id="txtEdicionTexto2"
                                          maxlength="500"></textarea>
                            </td>
                        </tr>
                        <tr class="row_dinamico" id="row_texto3">
                            <td valign="top">
                                <label id="lblTextoEti3" class="etiqueta"></label>
                                <input type="checkbox" id="chkTextoHabil3" class="habil" style="display: none;"/>
                            </td>
                            <td valign="top">
                                <textarea rows="4" cols="100" class="ui-text" id="txtEdicionTexto3"
                                          maxlength="500"></textarea>
                            </td>
                        </tr>
                        <tr class="row_dinamico" id="row_fecha">
                            <td valign="top">
                                <label id="lblFechaEti" class="etiqueta"></label>
                                <input type="checkbox" id="chkFechaHabil" class="habil" style="display: none;"/>
                            </td>
                            <td valign="top">
                                <input type="text" class="ui-text ui-text-date" id="txtEdicionFecha"
                                       maxlength="10"/><span class="ui-text-date-icon"
                                                             onclick="$('#txtEdicionFecha').focus();"></span>
                            </td>
                        </tr>
                        <tr class="row_dinamico" id="row_hora">
                            <td valign="top">
                                <label id="lblHoraEti" class="etiqueta"></label>
                                <input type="checkbox" id="chkHoraHabil" class="habil ui-text" style="display: none;"/>
                            </td>
                            <td valign="top">
                                <input type="text" class="ui-text ui-text-time" id="txtEdicionHora" maxlength="5"/><span
                                    class="ui-text-time-icon" onclick="$('#txtEdicionHora').focus();"></span>
                            </td>
                        </tr>
                        <tr class="row_dinamico" id="row_boolean">
                            <td valign="top">
                                <label id="lblBooleanEti" class="etiqueta"></label>
                                <input type="checkbox" id="chkBooleanHabil" class="habil" style="display: none;"/>
                            </td>
                            <td valign="top">
                                <input type="checkbox" id="chkEdicionBoolean"/>
                            </td>
                        </tr>
                        <tr class="row_dinamico" id="row_funcion">
                            <td colspan="2" align="center">
                                <input type="checkbox" id="chkFuncionHabil" class="habil" style="display: none;"/>
                                <button id="btnFuncion" class="etiqueta">[Nombre Funcion]</button>
                            </td>
                        </tr>
                        <tr>
                            <td align="right" colspan="2">
                                <button id="btnGrabar">Grabar</button>
                                <button id="btnCancelar">Cancelar</button>
                            </td>
                        </tr>
                        <tr style="width: 10px;">
                            <td>&nbsp;</td>
                        </tr>
                    </table>
                </div>
            </div>
        </div>
    </tiles:putAttribute>
</tiles:insertDefinition>