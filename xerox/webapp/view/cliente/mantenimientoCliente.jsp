<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<tiles:insertDefinition name="default">
    <tiles:putAttribute name="body">
        <script type="text/javascript"
                src="${pageContext.request.contextPath}/view/cliente/js/jsMantenimientoCliente.js"></script>
        <div id="pnlClientes" class="ui-accordion ui-widget ui-helper-reset">
            <label class="ui-accordion-header ui-accordion-header-active ui-corner-top ui-widget-header">Consulta de
                Clientes</label>

            <div class="ui-accordion-content ui-helper-reset ui-widget-content ui-corner-bottom">
                <table style="width: 100%; padding: 10px;">
                    <tr>
                        <td> Entidad</td>
                        <td style="width:20px">&nbsp;</td>
                        <td> Periodo</td>
                        <td style="width:20px">&nbsp;</td>
                        <td> Nro. Cuenta</td>
                        <td style="width:20px">&nbsp;</td>
                        <td colspan="2"> Nombre / Raz&oacute;n Social</td>
                    </tr>
                    <tr>
                        <td>
                            <div class="pui-dropdown-container">
                                <select id="cmbEntidad" name="cmbEntidad" class="sel">
                                    <option value="-1">[SELECCIONE]</option>
                                </select>
                            </div>
                        </td>
                        <td style="width:20px">&nbsp;</td>
                        <td>
                            <select id="cmbPeriodo" name="cmbPeriodo" class="sel">
                                <option value="-1">[SELECCIONE]</option>
                            </select>
                        </td>
                        <td style="width:20px">&nbsp;</td>
                        <td>
                            <input id="txtCuenta" name="txtCuenta" class="ui-text" style="width:150px;"/>
                        </td>
                        <td style="width:20px">&nbsp;</td>
                        <td>
                            <input id="txtRazonSocial" name="txtRazonSocial" class="ui-text" style="width:250px"/>
                        </td>
                    </tr>
                    <tr style="height: 5px;">
                        <td colspan="7" align="right"></td>
                    </tr>
                    <tr>
                        <td colspan="6">
                            <table id="tblAplicarEstado" class="hide">
                                <tr>
                                    <td>Estado</td>
                                    <td colspan="2"></td>
                                </tr>
                                <tr>
                                    <td style="width: 120px;">
                                        <select id="cmbEstatus" name="cmbEstatus" class="sel">
                                            <option value="-1"> [Seleccione]</option>
                                        </select>
                                    </td>
                                    <td style="width:15px" colspan="3">&nbsp;</td>
                                    <td align="right">
                                        <button id="btnAplicarEstatus"> Aplicar</button>
                                    </td>
                                </tr>
                            </table>
                        </td>
                        <td align="right">
                            <button id="btnBuscar"> Buscar</button>
                            &nbsp;
                            <button id="btnLimpiar"> Limpiar</button>
                            &nbsp;
                            <button id="btnEditar"> Editar</button>
                        </td>
                    </tr>
                </table>
                <br>

                <div id="pnlListadoClientes" style="margin: auto;"></div>
            </div>
        </div>
        <div id="dialogCliente" class="hide">
            <ul>
                <li><a href="#panelCliente" class="titulo_tabla"> Datos del Cliente </a></li>
                <li><a href="#panelAccionistas" class="titulo_tabla, hide" id="hrAccionistas"> Accionistas </a></li>
                <li><a href="#panelCuentas" class="titulo_tabla" id="hrCuentas"> Cuentas </a></li>
            </ul>

            <!-- PANEL CLIENTE -->
            <div id="panelCliente" class="ui-accordion-content ui-helper-reset ui-widget-content ui-corner-bottom">
                <input type="hidden" id="idCuenta"/>
                <fieldset>
                    <legend>Datos B&aacute;sicos</legend>
                    <table style="border-spacing: 4px; border-collapse: separate; padding: 4px;">
                        <tr>
                            <td>
                                <label id="lblEtiquetaPerido"> Fecha de Corte : </label>
                            </td>
                            <td>&nbsp;</td>
                            <td>
                                <div id="lblPeriodo" class="ui-label" style="width: 320px;"></div>
                            </td>
                        </tr>
                        <tr>
                            <td>
                                <label id="lblEtiquetaNumCliente">C&oacute;d. Cliente : </label>
                            </td>
                            <td>&nbsp;</td>
                            <td>
                                <div id="lblNumCliente" class="ui-label" style="width: 320px;"></div>
                            </td>
                        </tr>
                        <tr>
                            <td>
                                <label id="lblEtiquetaNumDocumento">Clave de Identificaci&oacute;n : </label>
                            </td>
                            <td>&nbsp;</td>
                            <td>
                                <div id="lblNumDocumento" class="ui-label" style="width: 320px;"></div>
                            </td>
                        </tr>
                        <tr>
                            <td>
                                <label id="lblEtiquetaNombre">Nombre : </label>
                            </td>
                            <td>&nbsp;</td>
                            <td>
                                <input type="text" id="txtNombreEdicion" style="width: 320px;" maxlength="40">
                            </td>
                        </tr>
                        <tr id="filaApellidoEdicion">
                            <td>
                                <label id="lblEtiquetaApellido">Apellido : </label>
                            </td>
                            <td>&nbsp;</td>
                            <td>
                                <input type="text" id="txtApellidoEdicion" style="width: 320px;" maxlength="40">
                            </td>
                        </tr>
                        <tr>
                            <td>
                                <label id="lblEtiquetaTin">TIN : </label>
                            </td>
                            <td>&nbsp;</td>
                            <td>
                                <input type="text" id="txtTINEdicion" style="width: 320px;" maxlength="19">
                            </td>
                        </tr>
                    </table>
                </fieldset>
                <br>
                <fieldset>
                    <legend>Informaci&oacute;n de Domicilio</legend>
                    <table style="border-spacing: 4px; border-collapse: separate; padding: 4px;">
                        <tr>
                            <td>
                                <label id="lblEtiquetaDireccion">Direcci&oacute;n : </label>
                            </td>
                            <td>&nbsp;</td>
                            <td>
                                <input type="text" id="txtDireccionEdicion" style="width: 320px;" maxlength="60">
                            </td>
                        </tr>
                        <tr>
                            <td>
                                <label id="lblEtiquetaPais">Pa&iacute;s : </label>
                            </td>
                            <td>&nbsp;</td>
                            <td>
                                <select id="cmbPais" name="cmbPais" class="sel" style="width: 320px;">
                                    <option value="-1">[SELECCIONE]</option>
                                </select>
                            </td>
                        </tr>
                        <tr>
                            <td>
                                <label id="lblEtiquetaCiudad">Ciudad : </label>
                            </td>
                            <td>&nbsp;</td>
                            <td>
                                <input type="text" id="txtCiudadEdicion" style="width: 320px;" maxlength="30">
                            </td>
                        </tr>
                        <tr>
                            <td>
                                <label id="lblEtiquetaProvincia">Provincia : </label>
                            </td>
                            <td>&nbsp;</td>
                            <td>
                                <input type="text" id="txtProvinciaEdicion" style="width: 320px;" maxlength="30">
                            </td>
                        </tr>
                        <tr>
                            <td>
                                <label id="lblEtiquetaCodPostal">C&oacute;d. Postal : </label>
                            </td>
                            <td>&nbsp;</td>
                            <td>
                                <input type="text" id="txtCodPostalEdicion" style="width: 320px;" maxlength="5">
                            </td>
                        </tr>
                    </table>
                </fieldset>
                <br>
                <table style="width:100%">
                    <tr>
                        <td style="width:70px">
                            <label id="lblEtiquetaEstatus">Estado : </label>
                        </td>
                        <td>&nbsp;</td>
                        <td>
                            <select id="cmbEstatusEdicion" name="cmbEstatusEdicion" class="sel">
                                <option value="-1">[SELECCIONE]</option>
                            </select>
                        </td>
                    </tr>
                    <tr>
                        <td colspan="3">&nbsp;</td>
                    </tr>
                    <tr>
                        <td align="right" colspan="3">
                            <table>
                                <tr>
                                    <td>
                                        <button id="btnGuardarEdicion">Guardar</button>
                                    </td>
                                    <td>&nbsp;</td>
                                    <td>
                                        <button id="btnRegresarEdicion">Regresar</button>
                                    </td>
                                </tr>
                            </table>
                        </td>
                    </tr>
                </table>
            </div>

            <!-- PANEL ACCIONISTAS -->
            <div id="panelAccionistas">
                <table>
                    <tr>
                        <td>
                            <label id="lblEtiquetaPeriodoAccionista">Fecha de Corte : </label>
                        </td>
                        <td>&nbsp;</td>
                        <td>
                            <label id="lblPeriodoAccionista"></label>
                        </td>
                    </tr>
                    <tr>
                        <td colspan="3">&nbsp;</td>
                    </tr>
                    <tr>
                        <td>
                            <label id="lblEtiquetaNroDocumentoAccionista">Clave de Identificaci&oacute;n : </label>
                        </td>
                        <td>&nbsp;</td>
                        <td>
                            <label id="lblNroDocumentoAccionista"></label>
                        </td>
                    </tr>
                    <tr>
                        <td colspan="3">&nbsp;</td>
                    </tr>
                    <tr>
                        <td>
                            <label id="lblEtiquetaRazonSocialAccionista">Raz&oacute;n Social : </label>
                        </td>
                        <td>&nbsp;</td>
                        <td>
                            <label id="lblRazonSocialAccionista"></label>
                        </td>
                    </tr>
                </table>
                <br>
                <fieldset>
                    <legend>Listado de Accionistas</legend>
                    <table>
                        <tr>
                            <td>
                                <div id="pnlListadoAccionistas"></div>
                            </td>
                        </tr>
                    </table>
                </fieldset>
            </div>

            <!-- PANEL CUENTAS -->
            <div id="panelCuentas">
                <table>
                    <tr>
                        <td>
                            <label id="lblEtiquetaPeriodoCuentas">Fecha de Corte : </label>
                        </td>
                        <td>&nbsp;</td>
                        <td>
                            <label id="lblPeriodoCuentas"></label>
                        </td>
                    </tr>
                    <tr>
                        <td colspan="3">&nbsp;</td>
                    </tr>
                    <tr>
                        <td>
                            <label id="lblEtiquetaNroDocumentoCuentas">Clave de Identificaci&oacute;n : </label>
                        </td>
                        <td>&nbsp;</td>
                        <td>
                            <label id="lblNroDocumentoCuentas"></label>
                        </td>
                    </tr>
                    <tr>
                        <td colspan="3">&nbsp;</td>
                    </tr>
                    <tr>
                        <td>
                            <label id="lblEtiquetaRazonSocialCuentas">Raz&oacute;n Social : </label>
                        </td>
                        <td>&nbsp;</td>
                        <td>
                            <label id="lblRazonSocialCuentas"></label>
                        </td>
                    </tr>
                </table>
                <br>
                <fieldset>
                    <legend>Listado de Cuentas</legend>
                    <table>
                        <tr>
                            <td>
                                <div id="pnlListadoCuentas"></div>
                            </td>
                        </tr>
                    </table>
                </fieldset>
            </div>
        </div>
    </tiles:putAttribute>
</tiles:insertDefinition>