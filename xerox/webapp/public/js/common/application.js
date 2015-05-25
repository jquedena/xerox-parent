var TITULO_APLICACION = "XEROX - Facturaci\u00F3n Electr\u00F3nica",

formatterDate = function(cellvalue, options, rowObject) {
	return cellvalue == null ? "UNKNOWN" : DateUtil.toString(DateUtil.longToDate(cellvalue), DateUtil.DDMMYYYYHHmmss);
},

formatterSpace = function(cellvalue, options, rowObject) {
	return "";
},

/**
 * Cierra una ventana jDialog
 */
closeDialog = function(id) {
	if(id.substr(0, 1) != "#") id = '#' + id;
	$(id).dialog('close');
},

/**
 * Crea un jDialog a partir de un componente div
 * id: Identificador del div
 * options: Objeto con las opciones personalizadas para el jDialog
 */
createDialogHTML = function(id, options) {
	if(id.substr(0, 1) != "#") id = '#' + id;
	
	var _options = $.extend({
		title: TITULO_APLICACION,
		buttons: {},
		autoOpen: false,
		resizable: false,
		closeOnEscape: true,
		modal: true
	}, options);
	
	$(id).dialog(_options);
},

/**
 * Crea un jDialog generando el codigo html base para los mensaje
 * id: Identificador del div
 * icon: Icono del cuadro de mensaje 
 * options: Objeto con las opciones personalizadas para el jDialog
 */
createDialog = function(id, icon, options) {
	var _options = $.extend({
		title: TITULO_APLICACION,
		buttons: {},
		resizable: false,
		autoOpen: false,
		closeOnEscape: true,
		modal: true,
		minWidth: 330,
		maxHeight: 450
	}, options);
	
	div = $("<div>").appendTo($("#panelMensaje"))
		.attr("id", id)
		.css({"display": "none", "vertical-align": "middle"});
	
	table = $("<table>").css({"width": "100%", "height": "100%"}).appendTo(div);
	tr = $("<tr>").appendTo(table);
	td = $("<td style='width: 36px;text-align: center; padding-right: 15px;'><span class='" + icon + "' style='float:left; margin:0 7px 7px 0;'></span></td>").appendTo(tr);
	td = $("<td style='text-align: left;'><span id='content" + id + "' class='contentDialog'></span></td>").appendTo(tr);
	
	div.dialog(_options);
},

/**
 * Crea un jDialog a partir de una URL
 * options: Objeto con las opciones y eventos personalizadas para el jDialog
 */
openDialogURL = function(options) {
	options = $.extend({
		  id: ""
		, url: "/"
		, data: {}
		, width: 400
		, height: 250
		, title: TITULO_APLICACION
		, success: function(request){}
	}, options);
	
	var id = "#" + options.id;
	$(id).dialog('option', 'title', options.title);
	$(id).dialog('option', 'width', options.width);
	$(id).dialog('option', 'height', options.height);
	
	var _post = $.post(obtenerContexto(options.url), options.data);;
	_post.success(function(request){
		$(id).html(request);
		
		if($.isFunction(options.success)){
			options.success(request);
		}
		
		$(id).dialog('open');
	});
},

/**
 * Configura el mensaje del jDialog
 * options: Objeto con las opciones y eventos personalizadas para el jDialog
 */
openDialogContent = function(options) {
	options = $.extend({
		  id: ""
		, content: "/"
		, width: 550
		, minHeight: 'auto'
		, maxHeight: 600
		, title: TITULO_APLICACION
		, buttons: {
			  "Aceptar": function(){closeDialog($(this).attr("id"));}
			, "Cancelar": function(){closeDialog($(this).attr("id"));}
		}
		, close: function(){}
	}, options);
	
	var id = "#" + options.id;
	$(id).find('.contentDialog').html(options.content);
	$(id).dialog('option', 'title', options.title);
	if(options.type == "SYS") {
		$(id).dialog('option', 'width', options.width);
	}
	if($.isPlainObject(options.buttons)){
		$(id).dialog('option', 'buttons', options.buttons);
	}
	if($.isFunction(options.close)) {
		$(id).dialog('option', 'close', options.close);
	}	
	$(id).dialog('open');
},

/**
 * Configura el mensaje del jDialog en modo Confirmacion
 * options: Objeto con las opciones y eventos personalizadas para el jDialog
 */
openJqConfirm = function(options) {
	options = $.extend({id: "jqConfirm"
		, buttons: {
			  "Aceptar": function(){closeDialog($(this).attr("id"));}
			, "Cancelar": function(){closeDialog($(this).attr("id"));}
		}
	}, options);
	openDialogContent(options);
},

/**
 * Configura el mensaje del jDialog en modo Informativo
 * options: Objeto con las opciones y eventos personalizadas para el jDialog
 */
openJqInfo = function(options) {
	options = $.extend({id: "jqInfo"
		, buttons: {
			"Aceptar": function(){closeDialog($(this).attr("id"));}
		}
	}, options);
	openDialogContent(options);
},

/**
 * Configura el mensaje del jDialog en modo Advertencia
 * options: Objeto con las opciones y eventos personalizadas para el jDialog
 */
openJqWarn = function(options) {
	options = $.extend({id: "jqWarn"
		, buttons: {
			"Aceptar": function(){closeDialog($(this).attr("id"));}
		}
	}, options);
	openDialogContent(options);
},


/**
 * Configura el mensaje del jDialog en modo Error
 * options: Objeto con las opciones y eventos personalizadas para el jDialog
 */
openJqError = function(options) {
	options = $.extend({id: "jqError"
		, buttons: {
			"Aceptar": function() { closeDialog($(this).attr("id")); }
		}
	}, options);
	if(options.type == "SYS") {
		options.width = 530;
		options.height = 300;
		options.buttons = {
			"Ver Detalle": function() { 
				var o = $("[aria-describedby=jqError]").find(".ui-dialog-buttonpane").find(".ui-button").eq(0).find(".ui-button-text");
				if(o.text() == "Ver Detalle") {
					o.text("Ocultar Detalle");
				} else {
					o.text("Ver Detalle");
				}
				$('#error').toggleClass('hide'); 
				$('#jqError').dialog("option", "position", { my: "center", at: "center", of: window });
			},
			"Aceptar": function() { closeDialog($(this).attr("id")); }
		};

		options.content = "Error del sistema, comuniquese con su Administrador de Sistema.<br/>" +
			"<div id=\"error\" class=\"hide\" style=\"overflow: auto; width: 440px; height: 150px; padding-top: 15px;\">" + options.content + "</div>";
		openDialogContent(options);
	} else {	
		openDialogContent(options);
	}
},

irAPagina = function(e) {
	var ctrl = $(this),
		ctrlPag = $(e.data.paginator),
		ctrlParent = ctrl.parent(),
		pagx = parseInt(ctrlPag.attr("data-pag"), 10);
	
	if(ctrl.hasClass("first")) {
		pagx = 1;
	} else if(ctrl.hasClass("prev")) {
		pagx--;
	} else if(ctrl.hasClass("next")) {
		pagx++;
	} else if(ctrl.hasClass("end")) {
		pagx = ctrl.attr("data-pag");
	} else if(ctrl.hasClass( "goto")) {
		pagx = ctrlPag.val();
		
		if(parseInt(pagx, 10) > parseInt(ctrlParent.find(".end").attr("data-pag"), 10)) {
			pagx = ctrlParent.find(".end").attr("data-pag");
		}
	}
	
	ctrlPag.val(pagx);
	
	if($.isFunction(e.data.fnAction)) {
		e.data.fnAction();
		
		ctrlPag.attr("data-pag", pagx);
		ctrlPag.val(pagx);
		
		if($("#hdnPagina").length > 0) {
			$("#hdnPagina").val(pagx);
		}

		if(pagx == 1) {
			ctrlParent.find(".first").button("disable");
			ctrlParent.find(".prev").button("disable");
			ctrlParent.find(".next").button("enable");
			ctrlParent.find(".end").button("enable");
		} else if(pagx == ctrl.attr("data-pag") ) {
			ctrlParent.find(".first").button("enable");
			ctrlParent.find(".prev").button("enable");
			ctrlParent.find(".next").button("disable");
			ctrlParent.find(".end").button("disable");
		} else {
			ctrlParent.find(".first").button("enable");
			ctrlParent.find(".prev").button("enable");
			ctrlParent.find(".next").button("enable");
			ctrlParent.find(".end").button("enable");
		}
	}
},

configurarPaginator = function(xpag, nroRegistros, fnAction, pag) {
	var paginas = Math.ceil(nroRegistros / 20);

	if(paginas > 1) {
		$('<button>', {"class": "first", html: "Primera P\u00E1gina", "css": {"height": "19px"} })
			.button({
				icons: {
					primary: "ui-icon-seek-first"
				},
				text: false,
				disabled: (pag == 1)
			})
			.on("click", {"fnAction": fnAction, "paginator": "#paginator_" + $(xpag).attr("id")}, irAPagina)
			.appendTo($(xpag));
	
		$('<button>', {"class": "prev", html: "P\u00E1gina Anterior", "css": {"height": "19px"} })
			.button({
				icons: {
					primary: "ui-icon-seek-prev"
				},
				text: false,
				disabled: (pag == 1)
			})
			.on("click", {"fnAction": fnAction, "paginator": "#paginator_" + $(xpag).attr("id")}, irAPagina)
			.appendTo($(xpag));
		
		$('<input>', {
				"id": "paginator_" + $(xpag).attr("id"),
				"maxlength": 3,
				"class": "ui-text-clean",
				"css": {"width": "45px", "margin-right": "5px;", "padding-right": "5px;"},
				"disabled": (paginas == 1),
				"data-pag": pag,
				"val": pag
			})
			.addClass("ui-corner-left")
			.appendTo($(xpag))
			.autoNumeric({mDec: 0, aNeg: ""});
	
		$('<button>', {html: "Ir a", 'class': 'goto', "css": {"height": "19px"} })
			.button({
				icons: {
					primary: "ui-icon-extlink"
				},
				text: false,
				disabled: (paginas == 1)
			})
			.removeClass("ui-corner-all")
			.addClass("ui-corner-right")
			.on("click", {"fnAction": fnAction, "paginator": "#paginator_" + $(xpag).attr("id")}, irAPagina)
			.appendTo($(xpag));
		
		$('<button>', {"class": "next", html: "Siguiente P\u00E1gina", "css": {"height": "19px"} })
			.button({
				icons: {
					primary: "ui-icon-seek-next"
				},
				text: false,
				disabled: (pag == paginas)
			})
			.on("click", {"fnAction": fnAction, "paginator": "#paginator_" + $(xpag).attr("id")}, irAPagina)
			.appendTo($(xpag));
		
		$('<button>', {"class": "end", html: "\u00DAltima P\u00E1gina", 'data-pag': paginas, "css": {"height": "19px"} })
			.button({
				icons: {
					primary: "ui-icon-seek-end"
				},
				text: false,
				disabled: (pag == paginas)
			})
			.on("click", {"fnAction": fnAction, "paginator": "#paginator_" + $(xpag).attr("id")}, irAPagina)
			.appendTo($(xpag));
	}
},

/**
 * Configura el control jqGrid
 * id: Identificador del div donde se mostrara el jqGrid
 * options: Objeto con las opciones y eventos personalizadas para el jqGrid
 */
configurarGridHost = function(id, options, nroRegistros, pagina, fnAction, optionsGroup) {
	var pag = "pag_" + id,
		pgx = "pgx_" + id,
		tbl = "tbl_" + id,
		xpnl = "#" + id,
		xtbl = "#" + tbl,
		xpag = "#" + pag,
		paginas = Math.ceil(nroRegistros / 20);
	
	if(paginas > 1) {
		$(xpnl).html("<table id='" + tbl + "'/><table align='center' cellpadding=2><tr><td id='" + pag + "'>" + (paginas > 1 ? ("P&aacute;g. " + pagina + " de " + paginas + " ") : "") + "<td><td align='center' id='" + pgx + "'><td>" + (nroRegistros == undefined ? "" : "Registros encontrados " + nroRegistros) + "</td></td></tr></table>");
	} else {
		$(xpnl).html("<table id='" + tbl + "'/><table align='center' cellpadding=2><tr><td id='" + pag + "'>" + (paginas > 1 ? ("P&aacute;g. " + pagina + " de " + paginas + " ") : "") + "<td><td align='center' id='" + pgx + "'><td>" + (nroRegistros == undefined ? "" : "Registros encontrados " + nroRegistros) + "</td></td></tr></table><input type='hidden' id='paginator_" + pag + "' value='1' />");
	}
	
	// loadComplete: function (data){},
	// rowList: [5,10,15,20,25],
	// recordtext : "{0} - {1} de {2} elementos",
	// pgtext : 'Pag: {0} de {1}',
	// pager : xpag,
	// width : "100%",
	// cellEdit: true,
	// cellsubmit: 'clientArray',
	// rowNum : 15,
    // loadError : function(xhr, st, err) {s},
	options = $.extend({
        scroll: 1,
        datatype : "local",
		data : {},
		rownumbers : false,
		height : 400,
		emptyrecords : 'No hay resultados',
		viewrecords : true,
		scrollOffset : 1,
		multiselect : false,
		subGrid : false,
		footerrow : false,
		loadonce : true,
		shrinkToFit : false,
		cmTemplate : {
			 resizable: true,
			 sortable: true
		}
	}, options);
	
	$(xtbl).jqGrid(options);
	
	if($.isPlainObject(optionsGroup)) {
		$(xtbl).jqGrid('setGroupHeaders', optionsGroup);
		$(xtbl).jqGrid('setFrozenColumns');
	}
	
	
	configurarPaginator(xpag, nroRegistros, fnAction, pagina);
},

configurarGrid = function(id, optionsLocal, fnAction, optionsGroup) {
	
	var pag = "pag_" + id,
		tbl = "tbl_" + id,
		xpnl = "#" + id,
		xtbl = "#" + tbl,
		xpag = "#" + pag;
	
	$(xpnl).html("<div id='" + pag + "'/><table id='" + tbl + "'/>");
	
	// loadComplete: function (data){},
	// rowList: [5,10,15,20,25],
	// width : "100%",
	// cellEdit: true,
	// cellsubmit: 'clientArray',
	// rowNum : 15,
    // loadError : function(xhr, st, err) {s},
	var options = $.extend({
        /* scroll: 1, */
		/* data : {}, */
		datatype : 'local',
		rownumbers : true,
		recordtext : "{0} - {1} de {2} elementos",
		pgtext : 'P\u00E1g: {0} de {1}',
		pager : xpag,
		rowNum : 15,
		height : 300,
		emptyrecords : 'No hay resultados',
		viewrecords : true,
		scrollOffset : 1,
		multiselect : false,
		subGrid : false,
		footerrow : false,
		loadonce : true,
		shrinkToFit : false,
		cmTemplate : {
			 resizable: true,
			 sortable: true
		}
	}, optionsLocal);

	options.onPaging = function(pgButton) {
   		// Change de Page
   		var o = $('#cb_tbl_' + id);
   		
   		if($.isFunction(optionsLocal.onPaging)) {
   			optionsLocal.onPaging();
   		}
   	};

	options.loadComplete = function(data) {
   		// Load
   		// $(xpnl).find("input[type=checkbox]").puicheckbox();
   		if($.isFunction(optionsLocal.loadComplete)) {
   			optionsLocal.loadComplete();	
   		}
   	};

	options.onSelectRow = function(rowid, status, e) {
		/* console.log(rowid + "+" + status)
   		selectPrimeUI(rowid, status, id);
   		if($.isFunction(optionsLocal.onSelectRow)) {
   			optionsLocal.onSelectRow();
   		}
   		 */
   	}

	$(xtbl).jqGrid(options);
	/*
	$('#cb_tbl_' + id).puicheckbox({"change": function(event, checked) {
		var __pag = $("#pg_pag_" + id).find(".ui-pg-input").eq(0).val() - 1,
			__chk = $('#cb_tbl_' + id).prop("checked");
		console.log(checked);
		s(optionsLocal.rowNum, __pag, __chk, xtbl);
		selectPrimeUIHeader(__chk);
		console.log(checked);
	}});
	*/

	if($.isPlainObject(optionsGroup)) {
		$(xtbl).jqGrid('setGroupHeaders', optionsGroup);
		$(xtbl).jqGrid('setFrozenColumns');
	}
	
	$(window).resize();
},

s = function(rowNum, __pag, __chk, xtbl) {
	for(var i = (rowNum * __pag); i < ((rowNum * __pag) + 15); i++) {
		if(__chk) {
			$(xtbl).jqGrid('setSelection', i);
		} else {
			$(xtbl).jqGrid('resetSelection', i);
		}
	}
},

selectPrimeUIHeader = function(status, id) {
	var o = $('#cb_tbl_' + id).parent().parent();
	console.log(status);
	if(status) {
		if(!o.find(".pui-chkbox-box").hasClass("ui-state-active")) {
			o.find(".pui-chkbox-box").addClass("ui-state-active");
			o.find(".pui-chkbox-icon").addClass("ui-icon ui-icon-check");
			// console.log("ssss");
		}

	} else {
		if(o.find(".pui-chkbox-box").hasClass("ui-state-active")) {
			o.find(".pui-chkbox-box").removeClass("ui-state-active");
			o.find(".pui-chkbox-icon").removeClass("ui-icon ui-icon-check");
			// console.log("ssssddddd");
		}
	}
},

selectPrimeUI = function(rowid, status, id) {
	var o = $("#jqg_tbl_" + id + "_" + rowid).parent().parent();
	if(status) {
		if(!o.find(".pui-chkbox-box").hasClass("ui-state-active")) {
			o.find(".pui-chkbox-box").addClass("ui-state-active");
			o.find(".pui-chkbox-icon").addClass("ui-icon ui-icon-check");
		}
	} else {
		if(o.find(".pui-chkbox-box").hasClass("ui-state-active")) {
			o.find(".pui-chkbox-box").removeClass("ui-state-active");
			o.find(".pui-chkbox-icon").removeClass("ui-icon ui-icon-check");
		}
	}
},

/**
 * Configura el control jqGrid
 * id: Identificador del div donde se mostrara el jqGrid
 * options: Objeto con las opciones y eventos personalizadas para el jqGrid
 * optionColumnHeader: Objeto con las opciones adicionales al jqGrid para combinar columnas
 */
configurarGridGroupHeaders = function(id, options, optionColumnHeader, nroRegistros, pagina, fnPaginador) {	
	configurarGrid(id, options, nroRegistros, pagina, fnPaginador, optionColumnHeader);
},

/**
 * Pone una clases para que resalta el input seleccionado
 */
textFocus = function() {
	$(this).toggleClass("ui-text-highlight");
},

/**
 * Pone una clases para que resalta el input cuando no tiene valor
 */
textBlur = function() {
	$(this).toggleClass("ui-text-highlight");
},

/**
 * Uso de la matriz asociativa suministrado como argumento 'obj', reemplazar los tokens
 * del formato #{<key>} en la 'cadena' argumento con valor clave.
 */
formatRow = function(str, obj) {
    for (var p in obj) {
        if(obj.hasOwnProperty(p)) {
            str = str.replace(new RegExp("#\\{" + p + "\\}", "g"), obj[p] == null ? "&nbsp;" : obj[p]);
        }
    }
    return str;
}, 

s4 = function() {
  return Math.floor((1 + Math.random()) * 0x10000)
             .toString(16)
             .substring(1);
},

guid = function() {
  return s4() + s4() + '-' + s4() + '-' + s4() + '-' + s4() + '-' + s4() + s4() + s4();
},

DateUtil = {
	yyyymmdd: 'yyyyMMdd',
	DDMMYYYY: 'dd/MM/yyyy',
	DDMMYYYYHHmmss: 'dd/MM/yyyy HH:mm:ss',
	MMDDYYYY: 'MM/dd/yyyy',
	YYYYMMDD: 'yyyy/dd/MM',
	parse: function(val, format) {
		var dd = 1, mm = 1, yy = 1900;
		
		if(format == this.DDMMYYYY) {
			dd = parseInt(val.substring(0,2), 10);
			mm = parseInt(val.substring(3,5), 10);
			yy = parseInt(val.substring(6,10), 10);
		} else if(format == this.MMDDYYYY) {
			mm = parseInt(val.substring(0,2), 10);
			dd = parseInt(val.substring(3,5), 10);
			yy = parseInt(val.substring(6,10), 10);
		} else if(format == this.YYYYMMDD) {
			yy = parseInt(val.substring(0,4), 10);
			mm = parseInt(val.substring(5,7), 10);
			dd = parseInt(val.substring(8,10), 10);
		} else if(format == this.yyyymmdd) {
			yy = parseInt(val.substring(0,4), 10);
			mm = parseInt(val.substring(4,6), 10);
			dd = parseInt(val.substring(6,8), 10);
		}
		
		return new Date(yy, mm - 1, dd, 0, 0, 0);
	},
	compareTo: function(val1, val2, format) {
		return (this.parse(val1, format).getTime() > this.parse(val2, format).getTime());
	},
	addDays: function(date, numDays) {
		return new Date(date.getTime() + (numDays * 24 * 3600 * 1000));
	},
	lastDay: function(val, format) {
		var date = this.parse(val, format);
		
		d = 1;
		m = date.getMonth() + 1;
		y = (m == 12 ? 1 : 0) + date.getFullYear();
		m = (m == 12 ? 0 : m);
		
		date = new Date(y, m, d, 0, 0, 0);
		
		return this.addDays(date, -1);
	},
	toString: function(date, format) {
		var dd = '01', mm = '01', yy = '1900', hh = "00", nn = "00", ss = "", val = "01/01/1900";
		
		if(format == this.DDMMYYYY) {
			dd = date.getDate();
			mm = date.getMonth() + 1;
			yy = date.getFullYear();
			
			val  = (dd < 10 ? '0' : '') + dd + "/";
			val += (mm < 10 ? '0' : '') + mm + "/";
			val += yy;
		} else if(format == this.DDMMYYYYHHmmss) {
			dd = date.getDate();
			mm = date.getMonth() + 1;
			yy = date.getFullYear();
			hh = date.getHours();
			nn = date.getMinutes();
			ss = date.getSeconds();
			
			val  = (dd < 10 ? '0' : '') + dd + "/";
			val += (mm < 10 ? '0' : '') + mm + "/";
			val += yy;
			val += " ";
			val += (hh < 10 ? '0' : '') + hh + ":";
			val += (nn < 10 ? '0' : '') + nn + ":";
			val += (ss < 10 ? '0' : '') + ss;
		}
		
		return val;
	},
	longToDate: function(milliseconds) {
		return new Date(milliseconds);
	}
},

NumeroUtil = {
	format: function(num, decPlaces, currencySymbol, decSeparator, thouSeparator) {
		if(decPlaces == undefined) {
			decPlaces = 2;
		}

		if(decSeparator == undefined) {
			decSeparator = ".";
		}
		
		if(thouSeparator == undefined) {
			thouSeparator = ",";
		}
		
		if(currencySymbol == undefined) {
			currencySymbol = "S/. ";
		}
		
		if(num == undefined || num == null || isNaN(num)){
			return "";
		}
	
	    var n = num,
	        sign = n < 0 ? "-" : "",
	        i = parseInt(n = Math.abs(+n || 0).toFixed(decPlaces)) + "",
	        j = (j = i.length) > 3 ? j % 3 : 0;
	
	    return sign + currencySymbol + (j ? i.substr(0, j) + thouSeparator : "") + i.substr(j).replace(/(\d{3})(?=\d)/g, "$1" + thouSeparator) + (decPlaces ? decSeparator + Math.abs(n - i).toFixed(decPlaces).slice(2) : "");
	}
},

/*
ValidUtil = function(objectID) {
	var objectos = $(content).find("[data-valid]"), o = null;

	for(i in objectos) {
		o = objectos[i];
		console.log(o.attr("data-valid"));
	}
}
*/

StringUtil = {
	escape: function(text) {
		return text.replace(/[\u00ED]/g, "&#237;")
			.replace(/[\u00FA]/g, "&#250;")
			.replace(/[\u00F3]/g, "&#243;")
			.replace(/[\u00E9]/g, "&#233;")
			.replace(/[\u00E1]/g, "&#225;")
			.replace(/[\u00F1]/g, "&#241;")
			.replace(/[\u00CD]/g, "$#205;")
			.replace(/[\u00DA]/g, "$#218;")
			.replace(/[\u00D3]/g, "$#211;")
			.replace(/[\u00C9]/g, "$#201;")
			.replace(/[\u00C1]/g, "$#193;")
			.replace(/[\u00D1]/g, "$#209;");
	}
},

AjaxUtil = function(options) {
	var invokeAjax = function(callback) {
		var xhr = $.ajax({ url: options.url, data: options.data });
		
		xhr.success(function(request) {
			if(request.tipoResultado == 'EXITO') {
				if(request.mensaje != null) {
					openJqInfo({content: request.mensaje});
				}
				if($.isFunction(callback)) {
		   			callback(request);	
		   		}
				if($.isFunction(options.onSuccess)) {
		   			options.onSuccess(request);	
		   		}
			} else if(request.tipoResultado == 'ERROR_SISTEMA') {
				openJqError({type: "SYS", content: request.mensaje});
			} else if(request.tipoResultado == 'ADVERTENCIA') {
				openJqWarn({content: request.mensaje});
			}
		});	
	}

	if(options.action === 'save') {
		var save = function() {
	   		openJqConfirm({
				content: options.content || "\u00BF Desea guardar los cambios hechos \u003F",
				buttons: {
					"Aceptar": function() {
					  	closeDialog($(this).attr("id"));
						invokeAjax();
					},
					"Cancelar": function() {
					  	closeDialog($(this).attr("id"));
					}
				}
			});			
		}

		if($.isFunction(options.validation)) {
		   	if(options.validation()) {
		   		save();
		   	} else {
		   		openJqWarn({content: "Verifique los datos ingresados, por favor."});
		   	}
		} else {
			save();
		}
	} else if(options.action === 'delete') {
		openJqConfirm({
			content: options.content || "\u00BF Desea eliminar el registro \u003F",
			buttons: {
				"Aceptar": function() {
					$("#jqConfirm").dialog("close");
					invokeAjax();
				}
			}
		});
	} else if(options.action === 'viewDetaill') {
		invokeAjax(function(request) {
			var o = request[options.element], e = null;
			for (var p in o) {
		        if(o.hasOwnProperty(p)) {
		        	e = $("#" + options.element + "\\." + p);
		        	if(e.is("input")) {
		        		e.val(o[p]);
		        	} else if(e.is("div")) {
		        		e.html(o[p]);
		        	}
		        }
		    }
		});
	} else {
		invokeAjax();
	}
};

$(document).ready(function() {

	createDialogHTML('jqLoad', {height: 150, width: 250, dialogClass: "hide-title-bar"});
	createDialog('jqConfirm', 'ui-icon-pers-question', {});
	createDialog('jqInfo', 'ui-icon-pers-info', {});
	createDialog('jqWarn', 'ui-icon-pers-warning', {});
	createDialog('jqError', 'ui-icon-pers-error', {});
	
	$.ajaxSettings.traditional = true;
	$.ajaxSetup({
		type: "post", 
		dataType: 'json',
	    accepts: {
	        xml: 'text/xml',
	        text: 'text/plain;utf-8',
	        json: 'application/json;utf-8'
	    },
		cache: false,
		error: function(request, status, error) {
			openJqError({type: 'SYS', content: "Error de comunicaci\u00F3n. <br/>(Estado: " + (status || "Desconocido") + ", Detalle: " + (error || "Indeterminado") + ")"});
		}
	});
	
	$(document).ajaxSuccess(function(event, xhr, settings) {
		// console.log(xhr.getAllResponseHeaders());
		/*var content = xhr.getResponseHeader("Content-Type").toLowerCase();
		if(content.indexOf('application/json') == -1 && content.indexOf('text/json') == -1){
			location.href = obtenerContexto("closeSessionAction.do");	
		}*/
	});
	
	$(document).ajaxStart(function(){
		if($.isFunction(useFunctionLoading)) {
    		useFunctionLoading('start');
    	} else {
        	$('#jqLoad').dialog('open');	
    	}
	});
	
	$(document).ajaxStop(function(){
    	if($.isFunction(useFunctionLoading)) {
    		useFunctionLoading('stop');
    		useFunctionLoading = null;
    	} else {
        	$('#jqLoad').dialog('close');
    	}
	});

	$('input[type="text"]').on("focus", textFocus);
	$('input[type="text"]').on("blur", textBlur);

	$("#layout").removeClass("hide");

	$("#menu1").menuBar({
        content: $("#menu1").next().html(),
        showSpeed: 1,
        flyOut: true
    });

	$("#menu2").menuBar({
        content: $("#menu2").next().html(),
        showSpeed: 1,
        flyOut: true
    });
	
	$("#menu3").menuBar({
        content: $("#menu3").next().html(),
        showSpeed: 1,
        flyOut: true
    });
	/*
	$(document).tooltip({
		items: "a",
		tooltipClass: "ui-state-highlight",
		position: { my: "left top+5"},
		content: function() {
			var element = $(this);
			if (element.is("a")) {
				return element.attr("title");
			}
		}
	});
	*/
});

/*
$('#jqLoad').dialog('open');
function onReady(callback) {
    var intervalID = window.setInterval(checkReady, 1000);

    function checkReady() {
        if (document.getElementsByTagName('body')[0] !== undefined) {
            window.clearInterval(intervalID);
            callback.call(this);
        }
    }
}
function show(id, value) {
	if(id == 'loading') {
		$('#jqLoad').dialog('close');
	} else {
		document.getElementById(id).style.display = value ? 'block' : 'none';	
	}
}
onReady(function () {
    show('layout', true);
    show('loading', false);
});
*/