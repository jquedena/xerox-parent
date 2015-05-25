var 
PARAMETRO_PADRE = 1,
PARAMETRO_HIJO = 2,

options = {
	caption : "Listado de Par\u00E1metros",
	colNames : [ "Id.", "Nombre", "Tipo", "Padre", "Estado" ],
	colModel : [ {name : "id",
		index : "id",
		width : 80,
		sortable : true,
		resizable : false
	}, {
		name : "nombre",
		index : "nombre",
		width : 200,
		sortable : true,
		resizable : true
	}, {
		name : "nombreTipo",
		index : "nombreTipo",
		width : 90,
		sortable : false,
		resizable : false
	}, {
		name : "nombrePadre",
		index : "nombrePadre",
		width : 200,
		sortable : false,
		resizable : true
	}, {
		name : "estadoDescripcion",
		index : "estadoDescripcion",
		width : 80,
		sortable : true,
		resizable : false
	} ],
	data : [],
	autowidth : true,
	height : 300,
	shrinkToFit : true
},

validaTipo = function() 
{
	if ($(this).val() == PARAMETRO_HIJO) 
	{
		$("#cmbFiltroPadre").removeAttr("disabled");
	} 
	else 
	{
		$("#cmbFiltroPadre").val("-1");
		$("#cmbFiltroPadre").attr("disabled","disabled");
	}
},

buscar = function() {
	if (validarBuscar()) {
	
		$("#hidFiltroTipo").val($("#cmbFiltroTipo").val());
		$("#hidFiltroPadre").val($("#cmbFiltroPadre").val());
		$("#hidFiltroNombre").val($("#txtFiltroNombre").val());
		$("#hidFiltroEstado").val($("#cmbFiltroEstado").val());

		procesarBusqueda();
		
	}
},

procesarBusqueda = function() {
	param = {
		"idTipo" : $("#hidFiltroTipo").val(),
		"idPadre" : $("#hidFiltroPadre").val(),
		"nombre" : $("#hidFiltroNombre").val(),
		"idEstado" : $("#hidFiltroEstado").val()
	};
	
	_post = $.post(obtenerContexto("param/buscarParametrosAction.json"), param);
	_post.success(function(data) {
		if(data.tipoResultado == "EXITO") {
			configurarGrid('pnlParametria', $.extend(options, {data: data.listaParametria}));
			
			if(data.listaParametria.length == 0){
				openJqInfo({
					content: "No existen datos"
				});
			}
		} else if(data.tipoResultado == "ERROR_SISTEMA") {
			openJqError({type: "SYS", content: StringUtil.escape(data.mensaje)});
		}
	});
},

abrir = function(title) {
	$("#dialogParametria").dialog({
		title : title,
		autoOpen : true,
		resizable : false,
		closeOnEscape : false,
		modal : true,
		minHeight: 'auto',
		width : 800,
		position: ['center', 'center']
	});
},

nuevo = function() {
	
	$("#cmbEdicionTipo").val(PARAMETRO_HIJO);
	// $("#row_padre").css("display","block");
	$("#row_padre").removeAttr("style");
	$("#cmbEdicionPadre").css("display", "block");
	$("#txtEdicionPadre").css("display", "none");
	if($("#cmbFiltroPadre").val() != "-1")
		$("#cmbEdicionPadre").val($("#cmbFiltroPadre").val());
	else
		$("#cmbEdicionPadre").val("-1");
	$("#cmbEdicionEstado").attr("disabled","disabled");
	$("#cmbEdicionEstado").val("A");
	$("#txtEdicionNombre").val("");
	$("#txtEdicionDescripcion").val("");
	$("#idParametro").val("0");
	
	mostrarCamposDinamicos();
	
	abrir("Nuevo Par\u00E1metro");
},

modificar = function() 
{
	var rowid = $("#tbl_pnlParametria").jqGrid('getGridParam', 'selrow');
	
	if (rowid) 
	{
		row = $("#tbl_pnlParametria").jqGrid('getRowData', rowid);
		
		param = {
			"item.id" : row.id,
			"item.tipo" : row.tipo
		};
		_post = $.post(obtenerContexto("param/obtenerParametroAction.json"), param);		
		_post.success(function(data) 
		{
				if (data.tipoResultado == 'EXITO') 
				{	
					$("#cmbEdicionEstado").removeAttr("disabled");
					$("#cmbEdicionTipo").val(data.item.tipo);
	
					if(data.item.tipo == PARAMETRO_HIJO) 
					{
						$("#row_padre").removeAttr("style");
						$("#cmbEdicionPadre").css("display", "none");
						$("#txtEdicionPadre").css("display", "block");
					}
					else
					{
						$("#row_padre").css("display","none");
					}
					
					$("#txtEdicionPadre").val(data.item.nombrePadre);
					$("#txtEdicionNombre").val(data.item.nombre);
					$("#txtEdicionDescripcion").val(data.item.descripcion);
					$("#idParametro").val(data.item.id);
					$("#cmbEdicionEstado").val(data.item.estado);
					
					$(".row_dinamico").css("display","none");
					$(".habil").prop("checked",false);
					
					if(data.item.tipo != PARAMETRO_PADRE)
					{
						if(data.item.codigoHabil == "S")
						{
							$("#row_codigo").removeAttr("style");
							$("#lblCodigoEti").text(data.item.codigoEti);
							$("#chkCodigoHabil").prop("checked", true);
							$("#txtEdicionCodigo").val(data.item.codigo);						
						}
						if(data.item.enteroHabil == "S")
						{							
							$("#row_entero").removeAttr("style");
							$("#lblEnteroEti").text(data.item.enteroEti);
							$("#chkEnteroHabil").prop("checked", true);
							$("#txtEdicionEntero").val(data.item.entero);						
						}
						if(data.item.decimalesHabil == "S")
						{							
							$("#row_decimal").removeAttr("style");
							$("#lblDecimalEti").text(data.item.decimalesEti);
							$("#chkDecimalHabil").prop("checked", true);
							$("#txtEdicionDecimal").val(NumeroUtil.format(data.item.decimales));						
						}
						if(data.item.textoHabil == "S")
						{							
							$("#row_texto").removeAttr("style");
							$("#lblTextoEti").text(data.item.textoEti);
							$("#chkTextoHabil").prop("checked", true);
							$("#txtEdicionTexto").val(data.item.texto);						
						}
						if(data.item.textoHabil2 == "S")
						{							
							$("#row_texto2").removeAttr("style");
							$("#lblTextoEti2").text(data.item.textoEti2);
							$("#chkTextoHabil2").prop("checked", true);
							$("#txtEdicionTexto2").val(data.item.texto2);						
						}
						if(data.item.textoHabil3 == "S")
						{							
							$("#row_texto3").removeAttr("style");
							$("#lblTextoEti3").text(data.item.textoEti3);
							$("#chkTextoHabil3").prop("checked", true);
							$("#txtEdicionTexto3").val(data.item.texto3);						
						}
						if(data.item.fechaHabil == "S")
						{							
							$("#row_fecha").removeAttr("style");
							$("#lblFechaEti").text(data.item.fechaEti);
							$("#chkFechaHabil").prop("checked", true);
							$("#txtEdicionFecha").val(data.item.fechaString);						
						}
						if(data.item.horaHabil == "S")
						{							
							$("#row_hora").removeAttr("style");
							$("#lblHoraEti").text(data.item.horaEti);
							$("#chkHoraHabil").prop("checked", true);
							$("#txtEdicionHora").val(data.item.hora);						
						}
						if(data.item.booleanoHabil == "S")
						{							
							$("#row_boolean").removeAttr("style");
							$("#lblBooleanEti").text(data.item.booleanoEti);
							$("#chkBooleanHabil").prop("checked", true);
							$("#chkEdicionBoolean").prop("checked", (data.item.booleano == "S"));						
						}
						if(data.item.funcionHabil == "S")
						{							
							$("#row_funcion").removeAttr("style");
							$("#btnFuncion").text(data.item.funcionEti);
							$("#chkFuncionHabil").prop("checked", true);
							$("#btnFuncion").button({icons: {primary: 'ui-icon-gear'}}).bind('click', ejecutarFuncion);
						}
					}
										
					abrir("Edici\u00F3n Par\u00E1metro");
					
				}
				else if (data.tipoResultado == 'ERROR_SISTEMA') 
				{
					openJqError({type : "SYS", content : StringUtil.escape(data.mensaje)});
				}			
		});
	} else {
		openJqWarn({content: "Seleccione la fila a modificar." });
	}
},

guardar = function() {
	
	if (validarGuardar()) {
		procesarGuardar();
	}
	
},

procesarGuardar = function() {
	
	var param = {
		"idTipo" : ($("#hidFiltroTipo").val() == "" ? "0" : $("#hidFiltroTipo").val()),
		"idPadre" : ($("#hidFiltroPadre").val() == "" ? "0" : $("#hidFiltroPadre").val()),
		"nombre" : $("#hidFiltroNombre").val(),
		"idEstado" : $("#hidFiltroEstado").val(),
		"item.id" : $("#idParametro").val(),
		"item.tipo" : $("#cmbEdicionTipo").val(),
		"item.parametro.id" : $("#cmbEdicionPadre").val(),
		"item.nombre" : $("#txtEdicionNombre").val(),
		"item.descripcion" : $("#txtEdicionDescripcion").val(),
		"item.estado" : $("#cmbEdicionEstado").val()	
	};
	
	if($("#chkCodigoHabil").is(":checked"))
		$.extend(param, ({"item.codigo" : $("#txtEdicionCodigo").val()}));
	if($("#chkEnteroHabil").is(":checked"))
		$.extend(param, ({"item.entero" : $("#txtEdicionEntero").val()}));
	if($("#chkDecimalHabil").is(":checked"))
		$.extend(param, ({"item.decimales" : $("#txtEdicionDecimal").val()}));
	if($("#chkTextoHabil").is(":checked"))
		$.extend(param, ({"item.texto" : $("#txtEdicionTexto").val()}));					
	if($("#chkTextoHabil2").is(":checked"))
		$.extend(param, ({"item.texto2" : $("#txtEdicionTexto2").val()}));
	if($("#chkTextoHabil3").is(":checked"))
		$.extend(param, ({"item.texto3" : $("#txtEdicionTexto3").val()}));	
	if($("#chkFechaHabil").is(":checked"))
		$.extend(param, ({"fecha" : $("#txtEdicionFecha").val()}));
	if($("#chkHoraHabil").is(":checked"))
		$.extend(param, ({"item.hora" : $("#txtEdicionHora").val()}));			
	if($("#chkBooleanHabil").is(":checked"))
		$.extend(param, ({"item.booleano" : ($("#chkEdicionBoolean").is(":checked") ? "S" : "N")}));					
										
	_post = $.post(obtenerContexto("param/guardarParametroAction.json"), param);
	_post.success(function(data) {
		
		if(data.tipoResultado == "EXITO")
		{
			openJqInfo({
				content: StringUtil.escape(data.mensaje)
			});
			
			if(data.listaParametria != null && data.listaParametria.length > 0)
				configurarGrid('pnlParametria', $.extend(options, {data: data.listaParametria}));
							
			$("#dialogParametria").dialog("close");
		}	
		else if(data.tipoResultado == "ERROR")
		{
			openJqError({content: StringUtil.escape(data.mensaje)});
		}
		else if(data.tipoResultado == "ERROR_SISTEMA")
		{
			openJqError({type: "SYS", content: StringUtil.escape(data.mensaje)});
		}						
	});
},

limpiar = function() 
{
	
	$("#hidFiltroTipo").val("");
	$("#hidFiltroPadre").val("");
	$("#hidFiltroNombre").val("");
	$("#hidFiltroEstado").val("A");
	
	$("#cmbFiltroTipo").val("-1");
	$("#cmbFiltroPadre").val("-1");
	$("#txtFiltroNombre").val("");
	$("#cmbFiltroEstado").val("-1");
	
	configurarGrid('pnlParametria', options);
	
},

cancelar = function() {
	$("#dialogParametria").dialog('close');
},

isSelect = function(element) {
	return parseInt(element.value) != -1;
},

isEmpty = function(element) {
	return ($.trim(element.value).length > 0);
},

isMaxLength500 = function(element) {
	return ($.trim(element.value).length <= 500);
},

validarBuscar = function() {
	$.validity.start();

	$("#cmbFiltroTipo").assert(isSelect, "Debe seleccionar un valor de la lista.");

	if ($("#cmbFiltroTipo").val() == PARAMETRO_HIJO) {
		$("#cmbFiltroPadre").assert(isSelect, "Debe seleccionar un valor de la lista.");
	}

	var result = $.validity.end();
	return result.valid;
},

validarGuardar = function() {
	$.validity.start();

	if ($("#cmbFiltroTipo").attr("disabled") != null && $("#cmbFiltroTipo").val() == PARAMETRO_HIJO) {
		$("#cmbEdicionPadre").assert(isSelect, "Debe seleccionar un valor de la lista.");
	}	
	$("#txtEdicionNombre").assert(isEmpty, "Debe ingresar un valor.");
	$("#txtEdicionDescripcion").assert(isMaxLength500, "Debe ingresar m\u00E1ximo 500 caracteres.");	
	$("#cmbEdicionEstado").assert(isSelect, "Debe seleccionar un valor de la lista.");
	
	if($("#chkCodigoHabil").is(":checked"))
		$("#txtEdicionCodigo").require();	
	if($("#chkEnteroHabil").is(":checked"))
		$("#txtEdicionEntero").require();
	if($("#chkDecimalHabil").is(":checked"))
		$("#txtEdicionDecimal").require();			
	if($("#chkTextoHabil").is(":checked")) {
		$("#txtEdicionTexto").require();
		$("#txtEdicionTexto").assert(isMaxLength500, "Debe ingresar m\u00E1ximo 500 caracteres.");
	}
	if($("#chkTextoHabil2").is(":checked")) {
		$("#txtEdicionTexto2").require();
		$("#txtEdicionTexto2").assert(isMaxLength500, "Debe ingresar m\u00E1ximo 500 caracteres.");
	}
	if($("#chkTextoHabil3").is(":checked")) {
		$("#txtEdicionTexto3").require();
		$("#txtEdicionTexto3").assert(isMaxLength500, "Debe ingresar m\u00E1ximo 500 caracteres.");
	}
	if($("#chkFechaHabil").is(":checked")) {
		$("#txtEdicionFecha")
		.require()
		.match("date");
	}					
	if($("#chkHoraHabil").is(":checked"))
		$("#txtEdicionHora").require().match("time24");						
		
	var result = $.validity.end();
	return result.valid;
},

mostrarCamposDinamicos = function() {
	
	$(".row_dinamico").css("display","none");
	$(".habil").prop("checked",false);
		
	if($("#cmbEdicionPadre").val() != "-1") {
		param = {
				"item.id" : $("#cmbEdicionPadre").val()
			};

			_post = $.post(obtenerContexto("param/mostrarCamposDinamicosAction.json"), param);		
			_post.success(function(data) {
				
				if(data.tipoResultado == "EXITO") {
					if(data.item.codigoHabil == "S") {
						// $("#row_codigo").css("display","block");
						$("#row_codigo").removeAttr("style");
						$("#lblCodigoEti").text(data.item.codigoEti);
						$("#chkCodigoHabil").prop("checked", true);
						$("#txtEdicionCodigo").val('');						
					}
					if(data.item.enteroHabil == "S") {
						// $("#row_entero").css("display","block");
						$("#row_entero").removeAttr("style");
						$("#lblEnteroEti").text(data.item.enteroEti);
						$("#chkEnteroHabil").prop("checked", true);
						$("#txtEdicionEntero").val('');						
					}
					if(data.item.decimalesHabil == "S") {
						// $("#row_decimal").css("display","block");
						$("#row_decimal").removeAttr("style");
						$("#lblDecimalEti").text(data.item.decimalesEti);
						$("#chkDecimalHabil").prop("checked", true);
						$("#txtEdicionDecimal").val('');						
					}
					if(data.item.textoHabil == "S") {
						//$("#row_texto").css("display","block");
						$("#row_texto").removeAttr("style");
						$("#lblTextoEti").text(data.item.textoEti);
						$("#chkTextoHabil").prop("checked", true);
						$("#txtEdicionTexto").val('');						
					}
					if(data.item.textoHabil2 == "S") {
						//$("#row_texto").css("display","block");
						$("#row_texto2").removeAttr("style");
						$("#lblTextoEti2").text(data.item.textoEti2);
						$("#chkTextoHabil2").prop("checked", true);
						$("#txtEdicionTexto2").val('');						
					}
					if(data.item.textoHabil3 == "S") {
						//$("#row_texto").css("display","block");
						$("#row_texto3").removeAttr("style");
						$("#lblTextoEti3").text(data.item.textoEti3);
						$("#chkTextoHabil3").prop("checked", true);
						$("#txtEdicionTexto3").val('');						
					}
					if(data.item.fechaHabil == "S") {
						//$("#row_fecha").css("display","block");
						$("#row_fecha").removeAttr("style");
						$("#lblFechaEti").text(data.item.fechaEti);
						$("#chkFechaHabil").prop("checked", true);
						$("#txtEdicionFecha").val('');						
					}
					if(data.item.horaHabil == "S") {
						// $("#row_hora").css("display","block");
						$("#row_hora").removeAttr("style");
						$("#lblHoraEti").text(data.item.horaEti);
						$("#chkHoraHabil").prop("checked", true);
						$("#txtEdicionHora").val('');						
					}
					if(data.item.booleanoHabil == "S") {
						// $("#row_boolean").css("display","block");
						$("#row_boolean").removeAttr("style");
						$("#lblBooleanEti").text(data.item.booleanoEti);
						$("#chkBooleanHabil").prop("checked", true);
						$("#chkEdicionBoolean").prop("checked", false);						
					}
					if(data.item.funcionHabil == "S") {
						//$("#row_funcion").css("display","block");
						$("#row_funcion").removeAttr("style");
						$("#btnFuncion").text(data.item.funcionEti);
						$("#chkFuncionHabil").prop("checked", true);
						$("#btnFuncion").button({icons: {primary: 'ui-icon-gear'}}).bind('click', ejecutarFuncion);
					}
				}
				else if(data.tipoResultado == "ERROR") {
					openJqError({content: data.mensaje});
				}
				else if(data.tipoResultado == "ERROR_SISTEMA") {
					openJqError({type: "SYS", content: data.mensaje});
				}						
			});
	}	
},

ejecutarFuncion = function() {
	
	var param = {
		"item.id" : $("#idParametro").val()		
	};
																
	_post = $.post(obtenerContexto("param/ejecutarFuncionAction.json"), param);
	_post.success(function(data) {
		
		if(data.tipoResultado == "EXITO")
		{
			openJqInfo({
				content: data.mensaje
			});
			
		}	
		else if(data.tipoResultado == "ERROR_SISTEMA")
		{
			openJqError({type: "SYS", content: data.mensaje});
		}						
	});
};

$(document).ready(function() {
	
	$("#btnBuscar").button({icons: {primary: 'ui-icon-search'}}).bind('click', buscar);
	$("#btnNuevo").button({icons: {primary: 'ui-icon-circle-plus'}}).bind('click', nuevo);
	$("#btnModificar").button({icons: {primary: 'ui-icon-pencil'}}).bind('click', modificar);
	$("#btnLimpiar").button({icons: {primary: 'ui-icon-circle-close'}}).bind('click', limpiar);
	$("#btnGrabar").button({icons: {primary: 'ui-icon-disk'}}).bind('click', guardar);
	$("#btnCancelar").button({icons: {primary: 'ui-icon-arrowreturn-1-w'}}).bind('click', cancelar);
	
	$("#cmbFiltroTipo").bind('change',validaTipo);
	$("#cmbEdicionPadre").bind('change',mostrarCamposDinamicos);
	
	$("#txtEdicionEntero").autoNumeric({aNeg : '-',	mNum : 14, mDec : 0});
	$("#txtEdicionDecimal").autoNumeric({aNeg : '-', mNum : 14});
	$("#txtEdicionFecha").datepicker({dateFormat: "dd/mm/yy"});
	$('#txtEdicionHora').timepicker({		
		timeFormat: 'HH:mm'
	});
});

$(window).on("resize", function() {
	var browser = new Browser();
	$("#tbl_pnlParametria").jqGrid('setGridHeight', browser.viewportHeight - 450);
	// $("#tbl_pnlListadoClientes").jqGrid('setGridWidth', browser.viewportWidth - 250);
});