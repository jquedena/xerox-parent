<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/public/css/ui.jqgrid.css"/>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/public/css/ui.multiselect.css"/>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/public/css/ui.autocomplete.extends.css"/>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/public/css/jquery.validity.css"/>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/public/theme/blitzer/jquery-ui-1.10.4.css"/>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/public/css/jquery-ui-timepicker-addon.css"/>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/public/css/jquery.handsontable.full.css"/>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/public/css/primeui-1.1.css"/>

<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/public/css/ui.application.css"/>
<!--[if ie 7]><link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/public/css/ui.application.7ie.css"/><![endif]-->
<!--[if ie 8]><link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/public/css/ui.application.8ie.css"/><![endif]-->
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/public/css/ui.fg.menu.css"/>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/public/css/jquery-te-1.4.0.css"/>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/public/css/codemirror.css"/>
<link rel="stylesheet" type="text/css"
      href="<%=request.getContextPath()%>/public/js/ui/codemirror/addon/fold/foldgutter.css"/>

<script type="text/javascript" src="<%=request.getContextPath()%>/public/js/ui/jquery-1.9.1.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/public/js/ui/jquery-ui-1.10.1.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/public/js/ui/jquery.jqGrid.src.js"></script>
<script type="text/javascript"
        src="<%=request.getContextPath()%>/public/js/ui/jquery-ui-autocomplete-extends.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/public/js/ui/jquery-ui-timepicker-addon.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/public/js/ui/ui.multiselect.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/public/js/ui/jquery.handsontable.full.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/public/js/ui/i18n/datepicker.locale-es.js"></script>
<script type="text/javascript"
        src="<%=request.getContextPath()%>/public/js/ui/i18n/jquery-ui-timepicker-es.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/public/js/ui/i18n/grid.locale-es.js"></script>

<script type="text/javascript" src="<%=request.getContextPath()%>/public/js/ui/jquery.fg.menu.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/public/js/ui/jquery.autoNumeric-1.6.2.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/public/js/ui/jquery.autoText-1.0.0.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/public/js/ui/jquery.validity.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/public/js/ui/jquery.validity.lang.es.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/public/js/ui/primeui-1.1.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/public/js/ui/jquery-te-1.4.0.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/public/js/ui/jquery.form.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/public/js/ui/codemirror.js"></script>
<script type="text/javascript"
        src="<%=request.getContextPath()%>/public/js/ui/codemirror/addon/fold/foldcode.js"></script>
<script type="text/javascript"
        src="<%=request.getContextPath()%>/public/js/ui/codemirror/addon/fold/foldgutter.js"></script>
<script type="text/javascript"
        src="<%=request.getContextPath()%>/public/js/ui/codemirror/addon/fold/brace-fold.js"></script>
<script type="text/javascript"
        src="<%=request.getContextPath()%>/public/js/ui/codemirror/addon/fold/xml-fold.js"></script>
<script type="text/javascript"
        src="<%=request.getContextPath()%>/public/js/ui/codemirror/addon/fold/markdown-fold.js"></script>
<script type="text/javascript"
        src="<%=request.getContextPath()%>/public/js/ui/codemirror/addon/fold/comment-fold.js"></script>
<script type="text/javascript"
        src="<%=request.getContextPath()%>/public/js/ui/codemirror/mode/javascript/javascript.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/public/js/ui/codemirror/mode/sql/sql.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/public/js/ui/selectorCSS.js"></script>

<script type="text/javascript" src="<%=request.getContextPath()%>/public/js/common/browser.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/public/js/common/application.js"></script>

<script type="text/javascript">
    var useFunctionLoading = null,
            obtenerContexto = function (_url) {
                var context = "<%=request.getContextPath()%>";
                var url = document.URL;
                var tmp = url.split(context);

                return tmp[0] + context + "/" + _url;
            };
</script>