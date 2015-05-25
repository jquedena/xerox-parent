<div id="menu">
    <div class="ui-widget-header ui-corner-all">
        <table class="panelMenu">
            <tr>
                <td>
                    <table class="tblMenuL">
                        <tr>
                            <td>
                                <a id="menu1" class="fg-button fg-button-ltr ${requestScope.procesoClass}" href="#">
                                    <div class="title title-ico title-ico-procesos">Procesos</div>
                                </a>

                                <div class="hide">
                                    <ul>
                                        <li id="menu1.menuItem1"><a
                                                href="<%=request.getContextPath()%>/scheduler/index.html">Estados de Tareas</a></li>
                                        <li id="menu1.menuItem2"><a
                                                href="<%=request.getContextPath()%>/process/index.html">Reporte de Procesos</a></li>
                                    </ul>
                                </div>
                            </td>
                            <td>
                                <a id="menu3" class="fg-button ${requestScope.administracionClass}" href="#">
                                    <div class="title title-ico title-ico-administracion">Administraci&oacute;n</div>
                                </a>

                                <div class="hide">
                                    <ul>
                                        <li id="menu3.menuItem1"><a
                                                href="<%=request.getContextPath()%>/param/index.html">Par&aacute;metros</a>
                                        </li>
                                    </ul>
                                </div>
                            </td>
                        </tr>
                    </table>
                </td>
            </tr>
        </table>
    </div>
</div>
