var 

fmtEstado = function(cellvalue, options, rowObject) {
    return (cellvalue == '1' ? "Informado" : 
        (cellvalue == '2' ? "Modificado" : 
        (cellvalue == '3' ? "Error" : 
        (cellvalue == '4' ? "Ok" : 
        (cellvalue == '5' ? "Informado Conflicto" : 
        (cellvalue == '6' ? "Modificado Conflicto" : 
        (cellvalue == '7' ? "Corregido" : 
        (cellvalue == '8' ? "Corregido Conflicto" : "Desconocido"))))))));
},

options = {
    mtype: 'POST',
    url : obtenerContexto("cliente/buscar.json"),
    datatype : "json",
    jsonReader : {
        root : 'listaCuentas',
        repeatitems : false,
        page : "page",
        total : "total",
        records : "records"
    },
    loadonce : false,
    caption : "Listado de Clientes",
    colNames : [ "C.Cliente", "Clave", "Identificaci&#243;n", "Nombre / Raz&#243;n Social", "Clas. FATCA", "Accionistas", "Est. FATCA", "Estado", "Item" ],
    colModel : [ 
        { name : "codigoCliente", index : "codigoCliente", width : 300 },
        { name : "tipoDocumento", index : "tipoDocumento", width : 200},
        { name : "nroDocumento", index : "nroDocumento", width : 300},
        { name : "nombre", index : "nombre", width : 500 },
        { name : "clasificacion", index : "clasificacion", width : 300 },
        { name : "accionistas", index : "accionistas", width : 200, align:'center'},
        { name : "nombreEstadoCuenta", index : "nombreEstadoCuenta", width : 300, align:'center' },
        { name : "estadoProceso", index : "estadoProceso", width : 300, align:'center', formatter: fmtEstado },
        { name: 'id', index:'id', width: 10, hidden:true }
    ],
    data : [],
    width        : 930,
    height       : 250,
    shrinkToFit : true,
    multiselect : true,
    cmTemplate : {
         resizable: false,
         sortable: false
    }
},

optionsAcciones = {
    colNames : ["Nombre", "Apellido Paterno", "Apellido Materno"],
    colModel : [ {
        name : "accNombre",
        index : "accNombre",
        width : 800,
        sortable : true,
        resizable : false
    }, {
        name : "accApellidoPaterno",
        index : "accApellidoPaterno",
        width : 800,
        sortable : true,
        resizable : false
    }, {
        name : "accApellidoMaterno",
        index : "accApellidoMaterno",
        width : 800,
        sortable : true,
        resizable : false
    }],
    data : [],
    width        : 500,
    height       : 200,
    shrinkToFit : true
},

optionsCuenta = {
    colNames : ["Nro. Cuenta", ""],
    colModel : [ {
        name : "nroCuenta",
        index : "nroCuenta",
        width : 400,
        sortable : true,
        resizable : true
    }, {
        name : "id",
        index : "id",
        hidden:true
    }],
    data : [],
    width        : 500,
    height       : 200,
    shrinkToFit : true
},

buscar = function() {
    if (validarBuscar()) {
        procesarBusqueda();
    }
},

procesarBusqueda = function() {
    param = {
        "cuenta.entidadContrato" : $("#cmbEntidad").val(),
        "cuenta.periodo.id" : $("#cmbPeriodo").val(),
        "cuenta.nroCuenta" : $("#txtCuenta").val(),
        "cuenta.nombre" : $("#txtRazonSocial").val().toUpperCase(),
        "cuenta.estado" : "-1",
        "page": 1
    };
    
    _post = $.post(obtenerContexto("cliente/buscar.json"), param);
    _post.success(function(data) {
        if(data.tipoResultado == "EXITO") {
            $("#cmbEstatus").val("-1");
            
            configurarGrid('pnlListadoClientes', $.extend(options, {
                datatype: 'json', 
                postData: { 
                    "cuenta.entidadContrato" : $("#cmbEntidad").val(),
                    "cuenta.periodo.id" : $("#cmbPeriodo").val(),
                    "cuenta.nroCuenta" : $("#txtCuenta").val(),
                    "cuenta.nombre" : $("#txtRazonSocial").val(),
                    "cuenta.estado" : "-1"
                }
            }));
            
            if(data.listaCuentas.length == 0  || (data.listaCuentas.length > 0 && data.listaCuentas[0].estadoPeriodo == '0')){
                // openJqInfo({ content: "No existen datos"});
                if(!$("#tblAplicarEstado").hasClass("hide")) {
                    $("#tblAplicarEstado").addClass("hide");
                }
                
                $("#btnEditar").button("disable");
                $("#btnAplicarEstatus").button("disable");
            } else {
                if($("#tblAplicarEstado").hasClass("hide")) {
                    $("#tblAplicarEstado").removeClass("hide");
                }
                
                $("#btnEditar").button("enable");
                $("#btnAplicarEstatus").button("enable");
            }
        } else if(data.tipoResultado == "ERROR_SISTEMA") {
            openJqError({type: "SYS", content: data.mensaje});
        }
    });
},

abrir = function(title) {
    $("#dialogCliente").dialog({
        title : title,
        autoOpen : true,
        resizable : false,
        closeOnEscape : false,
        modal : true,
        minHeight: 'auto',
        width : 600
    });
    
    $("#dialogCliente").tabs("option", "active", 0);
},

modificar = function() {
    $.validity.clear();
    var rows = $("#tbl_pnlListadoClientes").jqGrid('getGridParam', 'selarrrow');
    
    if(rows.length > 1) {
        openJqWarn({content: "Solo puede editar una fila a la vez." });
        return;
    }
    
    var rowid = $("#tbl_pnlListadoClientes").jqGrid('getGridParam', 'selrow');
    
    if (rowid){
        row = $("#tbl_pnlListadoClientes").jqGrid('getRowData', rowid);
        param = {
            "cuenta.codigoCliente" : row.codigoCliente,
            "cuenta.periodo.id" : $("#cmbPeriodo").val()
        };
        
        _post = $.post(obtenerContexto("cliente/obtenerCuenta.json"), param);
        _post.success(function(data){
            if (data.tipoResultado == 'EXITO'){
                //SI EXISTEN DATOS DE LA CUENTA
                if(data.cuenta != null){
                    //SI ES PERSONA NATURAL
                    $("#idCuenta").val(data.cuenta.id);
                    $("#lblPeriodo").html(data.cuenta.periodo.periodo);
                    $("#lblNumCliente").html(data.cuenta.codigoCliente);
                    $("#lblNumDocumento").html(data.cuenta.tipoDocumento + " - " + data.cuenta.nroDocumento);
                    $("#txtNombreEdicion").val(data.cuenta.nombre);
                    $("#txtTINEdicion").val(data.cuenta.tin);
                    $("#txtDireccionEdicion").val(data.cuenta.direccion);
                    $("#cmbPais").val(data.cuenta.paisResidencia);
                    $("#txtCiudadEdicion").val(data.cuenta.ciudad);
                    $("#txtProvinciaEdicion").val(data.cuenta.provincia);
                    $("#txtCodPostalEdicion").val(data.cuenta.codigoPostal);
                    $("#cmbEstatusEdicion").val(data.cuenta.estado);
                    
                    if(!$("#hrAccionistas").hasClass('hide')) {
                        $("#hrAccionistas").addClass('hide');
                    }
                    
                    if(data.cuenta.tipoPersona=="N"){
                        $("#txtApellidoEdicion").val(data.cuenta.apellido);
                        
                        $("#lblEtiquetaNombre").html("Nombre : ");
                        $("#lblEtiquetaTin").html("TIN : ");
                        
                        $("#filaApellidoEdicion").removeClass("hide");
                    }else{
                        $("#txtApellidoEdicion").val("");
                        
                        $("#lblEtiquetaNombre").html("Raz\u00F3n Social : ");
                        $("#lblEtiquetaTin").html("GIIN : ");
                        
                        $("#filaApellidoEdicion").addClass("hide");
                    }
                    
                    mostrarAcciones(data);
                    mostrarCuentas(data);
                    
                    if(data.cuenta.periodo.estado == '1') {
                        $("#btnGuardarEdicion").removeClass("hide");
                        $("#txtNombreEdicion").removeAttr("disabled");
                        $("#txtApellidoEdicion").removeAttr("disabled");
                        $("#txtTINEdicion").removeAttr("disabled");
                        $("#txtDireccionEdicion").removeAttr("disabled");
                        $("#cmbPais").removeAttr("disabled");
                        $("#txtCiudadEdicion").removeAttr("disabled");
                        $("#txtProvinciaEdicion").removeAttr("disabled");
                        $("#txtCodPostalEdicion").removeAttr("disabled");
                        $("#cmbEstatusEdicion").removeAttr("disabled");
                    } else {
                        $("#btnGuardarEdicion").addClass("hide");
                        $("#txtNombreEdicion").attr("disabled", "disabled");
                        $("#txtApellidoEdicion").attr("disabled", "disabled");
                        $("#txtTINEdicion").attr("disabled", "disabled");
                        $("#txtDireccionEdicion").attr("disabled", "disabled");
                        $("#cmbPais").attr("disabled", "disabled");
                        $("#txtCiudadEdicion").attr("disabled", "disabled");
                        $("#txtProvinciaEdicion").attr("disabled", "disabled");
                        $("#txtCodPostalEdicion").attr("disabled", "disabled");
                        $("#cmbEstatusEdicion").attr("disabled", "disabled");
                    }
                }
                
                abrir("Edici\u00F3n de Cliente");
            }else if (data.tipoResultado == 'ERROR_SISTEMA'){
                openJqError({type : "SYS", content : data.mensaje});
            }    
        });
    } else {
        openJqWarn({content: "Seleccione la fila a modificar." });
    }
},

mostrarAcciones= function(data) {
    if(data.tipoResultado == "EXITO") {
        $("#lblPeriodoAccionista").html(data.cuenta.periodo.periodo);
        $("#lblNroDocumentoAccionista").html(data.cuenta.tipoDocumento + " - " + data.cuenta.nroDocumento);
        $("#lblRazonSocialAccionista").html(data.cuenta.nombre + ' ' + (data.cuenta.apellido || ''));
        
        if(data.listaAccionistas.length > 0) {
            $("#hrAccionistas").removeClass('hide');
        }
        
        configurarGrid('pnlListadoAccionistas', $.extend(optionsAcciones, {data: data.listaAccionistas}));
    }
},

mostrarCuentas = function(data){
    $("#lblPeriodoCuentas").html(data.cuenta.periodo.periodo);
    $("#lblNroDocumentoCuentas").html(data.cuenta.tipoDocumento + " - " + data.cuenta.nroDocumento);
    $("#lblRazonSocialCuentas").html(data.cuenta.nombre + ' ' + (data.cuenta.apellido || ''));
    
    if(data.tipoResultado == "EXITO") {
        configurarGrid('pnlListadoCuentas', $.extend(optionsCuenta, {data: data.listaCuentas}));
    }
},

cambiarEstado = function() {
    $.validity.start();

    $("#cmbEstatus").selectSelected("-1", "Debe seleccionar un valor de la lista.");

    var result = $.validity.end(), params, rows, row, ids = '';
    
    if(result.valid) {
        rows = $("#tbl_pnlListadoClientes").jqGrid('getGridParam', 'selarrrow');

        for(i = 0; i < rows.length; i++) {
            row = $("#tbl_pnlListadoClientes").jqGrid('getRowData', rows[i]);
            ids += (i == 0 ? '' : '|') + row.codigoCliente;
        }
        
        params = {
            "cuenta.periodo.id" : $("#cmbPeriodo").val(),
            "cuenta.estado" : $("#cmbEstatus").val(),
            "cuenta.entidadContrato" : $("#cmbEntidad").val(),
            "ids" : rows.length == 0 ? null : ids
        };
        
        AjaxUtil({
            url : obtenerContexto('cliente/aplicarEstado.json'),
            data : params,
            action : 'save',
            content : rows.length == 0 ? '\u00BF Desea cambiar el estado de todos los elementos \u003F' : '\u00BF Desea cambiar el estado de todos los elementos seleccionados \u003F',
            onSuccess : function(request) {
                buscar();
            }
        });
    }
},

guardar = function() {
    if (validarGuardar()) {
        procesarGuardar();
    }
},

procesarGuardar = function() {
    var param = {
        "cuenta.id" : 0,
        "cuenta.periodo.id" : $("#cmbPeriodo").val(),
        "cuenta.codigoCliente" : $("#lblNumCliente").html(),
        "cuenta.nombre" : $("#txtNombreEdicion").val(),
        "cuenta.apellido" : $("#txtApellidoEdicion").val(),
        "cuenta.tin" : $("#txtTINEdicion").val(),
        "cuenta.direccion" : $("#txtDireccionEdicion").val(),
        "cuenta.paisResidencia" : $("#cmbPais").val(),
        "cuenta.ciudad" : $("#txtCiudadEdicion").val(),
        "cuenta.provincia" : $("#txtProvinciaEdicion").val(),
        "cuenta.codigoPostal" : $("#txtCodPostalEdicion").val(),
        "cuenta.estado" : $("#cmbEstatusEdicion").val()
    };
    
    _post = $.post(obtenerContexto("cliente/guardar.json"), param);
    _post.success(function(data) {
        if(data.tipoResultado == "EXITO") {
            openJqInfo({
                content: data.mensaje,
                close: function( event, ui ) {
                    buscar();
                }
            });
            
            $("#dialogCliente").dialog("close");
        } else if(data.tipoResultado == "ADVERTENCIA") {
            openJqError({content: data.mensaje});
        } else if(data.tipoResultado == "ERROR") {
            openJqError({content: data.mensaje});
        } else if(data.tipoResultado == "ERROR_SISTEMA") {
            openJqError({type: "SYS", content: data.mensaje});
        }
    });
},

limpiar = function() {
    $("#cmbEntidad").val("-1");
    $("#cmbPeriodo").val("-1");
    $("#cmbEstatus").val("-1");
    $("#txtCuenta").val("");
    $("#txtRazonSocial").val("");
    
    if(!$("#tblAplicarEstado").hasClass("hide")) {
        $("#tblAplicarEstado").addClass("hide");
    }
    
    configurarGrid('pnlListadoClientes', $.extend(options, {datatype: 'local', data: []}));
},

cancelar = function() {
    $("#dialogCliente").dialog('close');
},

validarBuscar = function() {
    $.validity.start();

    $("#cmbEntidad").selectSelected("-1", "Debe seleccionar un valor de la lista.");
    $("#cmbPeriodo").selectSelected("-1", "Debe seleccionar un valor de la lista.");

    var result = $.validity.end();
    return result.valid;
},

validarGuardar = function() {
    $.validity.start();
    
    $("#txtNombreEdicion")
        .require("Debe ingresar un valor.")
        .isMaxLength(1, 40);
    
    if(!$("#filaApellidoEdicion").hasClass("hide")) {
        $("#txtApellidoEdicion")
            .require("Debe ingresar un valor.")
            .isMaxLength(1, 40);
    }
    
    $("#txtDireccionEdicion").require("Debe ingresar un valor.");
    $("#cmbPais").selectSelected("-1", "Debe seleccionar un valor de la lista.");
    $("#txtCiudadEdicion").require("Debe ingresar un valor.");
    $("#txtProvinciaEdicion").require("Debe ingresar un valor.");
    $("#cmbEstatusEdicion").selectSelected("-1", "Debe seleccionar un valor de la lista.");
    if($("#txtCodPostalEdicion").val().length > 0) {
        $("#txtCodPostalEdicion").isMaxLength(1, 5);
    }
    
    if($("#lblEtiquetaTin").html() != "GIIN : ") {
        $("#txtTINEdicion")
            .require("Debe ingresar un valor.")
            .isMaxLength(9, 11);
    } else  {
        $("#txtTINEdicion")
            .require("Debe ingresar un valor.")
            .isLengthVal1Val2(11, 19);
    }
    
    var result = $.validity.end();
    return result.valid;
},

limpiarCombo = function(idCombo){
    $('#'+idCombo).find('option').remove();
    var values="<option value='-1'>[Seleccione]</option>";
    $('#'+idCombo).html(values);
},

cambioCriterio = function() {
    $("#btnEditar").button("disable");
    $("#btnAplicarEstatus").button("disable");
},

cargarValores = function(entidades, periodos, estatus, paises) {
    limpiarCombo("cmbEntidad");
    limpiarCombo("cmbPeriodo");
    limpiarCombo("cmbEstatus");
    limpiarCombo("cmbPais");
    
    //LLENAMOS EL COMBO DE ENTIDADES
    var values="<option value='-1'>[Seleccione]</option>";
    $.each(entidades, function (i, fb) {
        values+="<option value='"+fb.nombre+"'>"+fb.nombre+"</option>";
    });
    $("#cmbEntidad").html(values);
    
    //LLENAMOS EL COMBO DE PERIODOS
    values="<option value='-1'>[Seleccione]</option>";
    $.each(periodos, function (i, fb) {
        values+="<option value='"+fb.id+"'>"+fb.periodo+"</option>";
    });
    $("#cmbPeriodo").html(values);
    
    //LLENAMOS EL COMBO DE ESTADOS
    values="<option value='-1'>[Seleccione]</option>";
    $.each(estatus, function (i, fb) {
        values+="<option value='"+fb.codigo+"'>"+fb.nombre+"</option>";
    });
    $("#cmbEstatus").html(values);
    $("#cmbEstatusEdicion").html(values);
    
    //LLENAMOS EL COMBO DE PAISES
    values="<option value='-1'>[Seleccione]</option>";
    $.each(paises, function (i, fb) {
        values+="<option value='"+fb.codigo+"'>"+fb.nombre+"</option>";
    });
    $("#cmbPais").html(values);
};

$(document).ready(function() {
    // $('select').puidropdown(); 
    $("#btnBuscar").button({icons: {primary: 'ui-icon-search'}}).bind('click', buscar);
    $("#btnLimpiar").button({icons: {primary: 'ui-icon-circle-close'}}).bind('click', limpiar);
    $("#btnEditar").button({icons: {primary: 'ui-icon-pencil'}}).bind('click', modificar);
    $("#btnGuardarEdicion").button({icons: {primary: 'ui-icon-disk'}}).bind('click', guardar);
    $("#btnRegresarEdicion").button({icons: {primary: 'ui-icon ui-icon-arrowreturn-1-w'}}).bind('click', cancelar);
    $("#btnAplicarEstatus").button().bind('click', cambiarEstado);
    $("#dialogCliente").tabs({
        activate: function(event, ui) {
            $('#dialogCliente').dialog("option", "position", { my: "center", at: "center", of: window });
        }
    });
    
    var _post = $.post(obtenerContexto("cliente/valoresListas.json"), {});
    _post.success(function(request) {
        if(request.tipoResultado == "EXITO") {
            cargarValores(request.listaEntidades, request.listaPeriodos, request.listaEstatus, request.listaPaises);
        } else if(request.tipoResultado == "ERROR") {
            openJqError({content: request.mensaje});
        } else if(request.tipoResultado == "ERROR_SISTEMA") {
            openJqError({type: "SYS", content: request.mensaje});
        }
    });
    
    $("#cmbEntidad").on("change", cambioCriterio);
    $("#cmbPeriodo").on("change", cambioCriterio);
    $("#txtCuenta").on("change", cambioCriterio);
    $("#txtRazonSocial").on("change", cambioCriterio);

});

$(window).on("resize", function() {
    var browser = new Browser();
    $("#tbl_pnlListadoClientes").jqGrid('setGridHeight', browser.viewportHeight - 450);
});