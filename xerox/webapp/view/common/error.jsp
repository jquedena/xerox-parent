<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">

<html>
<head>
    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/public/css/ui.application.css"/>
    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/public/theme/bbva/jquery-ui-1.10.1.css"/>
    <title>Herramienta de Gestión de Carteras</title>
    <script>
        var PAGE_ERROR = true;
        function noBack() {
            window.history.forward(1);
        }
    </script>
</head>
<body onload="window.close();" onunload="noBack();">
<div class="content clearfix ui-widget ui-widget-content ui-corner-all"
     style="width: 800px; background-color: white; margin-left: 30px; margin-top: 30px;">
    <table border="0" cellpadding="0" cellspacing="0" align="center" width="100%">
        <tr>
            <td>
                <div>
                    <table width="100%" border="0" cellpadding="0" cellspacing="0">
                        <tr height="45px">
                            <td width="160px">
                                <img src="<%=request.getContextPath()%>/public/img/marca/logo_134x50.png"/>
                            </td>
                            <td align="right" class="titulo" style="padding-right:10px;">
                                <label style="font-size:18px;color: #7fc520;">Herramienta de Gestión de Carteras</label>
                            </td>
                        </tr>
                    </table>
                </div>
            </td>
        </tr>
        <tr>
            <td valign="middle" align="center" height="400px">
                <label style="font-size:22px;color: #00a4e2;">"Usted no tiene acceso al sistema."</label>
            </td>
        </tr>
        <tr>
            <td>
                <div style="padding-left: 5px; padding-bottom: 5px;">
                    &copy; BBVA Continental Copyright - Todos los Derechos Reservados.
                </div>
            </td>
        </tr>
    </table>
</div>
</body>
</html>