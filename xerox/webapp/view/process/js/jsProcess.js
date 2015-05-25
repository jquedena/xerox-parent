var

createButton = function(rowObject, tipo, extension) {
    return ("<a href='javascript:void(0);' onclick=\"descargar('#{tipo}', '#{extension}', '#{jobName}', '#{jobExecutionId}', '#{createTime}');\" title='.#{extension}' class='ui-icon ui-icon-btn-#{extension}'></a>")
        .replace(new RegExp("#\\{tipo\\}", "g"), tipo)
        .replace(new RegExp("#\\{extension\\}", "g"), extension)
        .replace(new RegExp("#\\{jobName\\}", "g"), rowObject.jobName)
        .replace(new RegExp("#\\{jobExecutionId\\}", "g"), (rowObject.jobName == "procesoGeneracionArchivo" && tipo == "zipOutput" ? rowObject.jobExecutionId : rowObject.jobInstanceId))
        .replace(new RegExp("#\\{createTime\\}", "g"), DateUtil.toString(DateUtil.longToDate(rowObject.createTime), DateUtil.DDMMYYYYHHmmss));
},

formatterBotones = function(cellvalue, options, rowObject) {
    var result = "";
    
    if(rowObject.jobName == "procesoCargaArchivoCuentas" || rowObject.jobName == "procesoCargarAccionistas"){
        result = createButton(rowObject, 'text', 'log')
            + "&nbsp;&nbsp;" + createButton(rowObject, 'html', 'html');
    } else if(rowObject.jobName == "procesoDescompresionArchivo"){ 
        result = createButton(rowObject, 'xmlOutput', 'xml')
            + "&nbsp;&nbsp;" + createButton(rowObject, 'text', 'log')
            + "&nbsp;&nbsp;" + createButton(rowObject, 'html', 'html');
    } else if(rowObject.jobName == "procesoGeneracionArchivo"){
        result = createButton(rowObject, 'zipOutput', 'zip')
            + "&nbsp;&nbsp;" + createButton(rowObject, 'xmlOutput', 'xml')
            + "&nbsp;&nbsp;" + createButton(rowObject, 'text', 'log')
            + "&nbsp;&nbsp;" + createButton(rowObject, 'html', 'html');
    }
    
    return result;
},

options = {
    caption : "Listado de Procesos",
    colNames : [ "jobExecutionId", "jobInstanceId", "Proceso", "Creado", "Iniciado", "Terminado", "Estado", "Estado", "Descargar" ],
    colModel : [
        {name : "jobExecutionId", index : "jobExecutionId", hidden : true},
        {name : "jobInstanceId" , index : "jobInstanceId" , hidden : true},
        {name : "jobName"       , index : "jobName"       , width : 140},
        {name : "createTime"    , index : "createTime"    , width : 110, align: 'center', formatter: formatterDate},
        {name : "startTime"     , index : "startTime"     , width : 110, align: 'center', formatter: formatterDate},
        {name : "endTime"       , index : "endTime"       , width : 110, align: 'center', formatter: formatterDate},
        {name : "exitCode"      , index : "exitCode"      , hidden : true},
        {name : "status"        , index : "status"        , width : 60},
        {name : "exitMessage"   , index : "exitMessage"   , width : 90, align: 'center', formatter: formatterBotones}
    ],
    cmTemplate : { resizable: true, sortable: false },
    data : [],
    width : 920,
    height : 300,
    shrinkToFit : true
},

descargar = function(tipo, extension, jobName, jobExecutionId, createTime) {
    var fileName = jobName + "_" + jobExecutionId + "_" + createTime.replace(/\//g, '').replace(/:/g, '').replace(new RegExp(" ", "g"), '-'); 
    document.getElementById('frmDescarga').action = obtenerContexto("process/download/" + tipo + "/" + fileName + "." + extension);
    document.getElementById('frmDescarga').submit();
},

buscar = function() {
    AjaxUtil({
        url : obtenerContexto('process/list.json'),
        data : {
            "proceso.jobName" : $("#txtProceso").val() == '[Todos]' ? null : $("#txtProceso").val(),
            "proceso.exitCode" : $("#txtEstado").val() == '[Todos]' ? null : $("#txtEstado").val(),
            "proceso.createTimeTemp" : $("#txtFechaEjecucion").val()
        },
        onSuccess : function(request) {
            configurarGrid('pnlProcesos', $.extend(options, {data: request.procesos}));
        }
    });
}

limpiar = function() {
    $("#txtProceso").val("[Todos]");
    $("#txtEstado").val("[Todos]");
    $("#txtFechaEjecucion").val(DateUtil.toString(new Date(), DateUtil.DDMMYYYY));
    
    configurarGrid('pnlProcesos', $.extend(options, {data: []}));
};

$(document).ready(function() {
    $("#txtFechaEjecucion").datepicker({dateFormat: "dd/mm/yy"}).val(DateUtil.toString(new Date(), DateUtil.DDMMYYYY));
    
    $("#txtProceso").puiautocomplete({
        completeSource: ['[Todos]', 'procesoCargaArchivoCuentas', 'procesoCargarAccionistas', 'procesoGeneracionArchivo', 'procesoDescompresionArchivo'],
        dropdown: true
    }).val('[Todos]');
    
    $("#txtEstado").puiautocomplete({
        completeSource: ['[Todos]', 'STARTED', 'UNKNOWN', 'EXECUTING', 'FAILED', 'COMPLETED', 'PARTITIAL'],
        dropdown: true
    }).val('[Todos]');

    $("#btnBuscar").button({icons: {primary: 'ui-icon-search'}}).bind('click', buscar);
    $("#btnLimpiar").button({icons: {primary: 'ui-icon-circle-close'}}).bind('click', limpiar);
    
    buscar();
});

$(window).on("resize", function() {
    var browser = new Browser();
    $("#tbl_pnlProcesos").jqGrid('setGridHeight', browser.viewportHeight - 420);
});