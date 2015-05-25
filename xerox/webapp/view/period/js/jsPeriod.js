var

fmtEstado = function(cellvalue, options, rowObject) {
	return cellvalue == '1' ? "Activo" : 'Inactivo';
}, 

options = {
	caption : "Listado de Periodos",
	colNames : [ "Periodo", "Estado", "Creado", "Modificado", "" ],
	colModel : [
	    {name : "periodo", index : "periodo", width : 100, align : "center"},
	    {name : "estado", index : "estado", width : 90, formatter: fmtEstado, align: "center" },
	    {name : "fechaCreacion", index : "fechaCreacion", width : 130, formatter: formatterDate, align : "center"},
	    {name : "fechaModificacion", index : "fechaModificacion", width : 130, formatter: formatterDate, align : "center"},
	    {name : "id", index : "id", hidden: true}
	],
	data : [],
	width		: "auto",
	height   	: 250,
	shrinkToFit : true
},

buscar = function(request) {
	if(request.listaPeriodos.length <= 1) {
		$("#btnActivar").button("disable");
	} else {
		$("#btnActivar").button("enable");
	}
	configurarGrid('pnlListadoPeriodos', $.extend(options, {data: request.listaPeriodos}));
}

activar = function() {
	var rowid = $("#tbl_pnlListadoPeriodos").jqGrid('getGridParam', 'selrow'), param;
	if (rowid){
		row = $("#tbl_pnlListadoPeriodos").jqGrid('getRowData', rowid);
		param = {
			"periodo.periodo" : row.periodo,
			"periodo.id" : row.id
		};
	
		AjaxUtil({
			url : obtenerContexto('period/active.json'),
			data : param,
			action : 'save',
			content : '\u00BF Desea activar el periodo ' + row.periodo + '\u003F',
			onSuccess : function(request) {
				buscar(request);
			}
		});
	} else {
		openJqWarn({content: "Seleccione el periodo para activar." });
	}
};

$(document).ready(function() {
	$("#btnActivar").button({icons: {primary: 'ui-icon-play'}}).bind('click', activar);
	
	var _post = $.post(obtenerContexto("period/list.json"), {});
	_post.success(function(request) {
		if(request.tipoResultado == "EXITO") {
			buscar(request);
		} else if(request.tipoResultado == "ERROR") {
			openJqError({content: request.mensaje});
		} else if(request.tipoResultado == "ERROR_SISTEMA") {
			openJqError({type: "SYS", content: request.mensaje});
		}						
	});
});

$(window).on("resize", function() {
	var browser = new Browser();
	$("#tbl_pnlListadoPeriodos").jqGrid('setGridHeight', browser.viewportHeight - 400);
});