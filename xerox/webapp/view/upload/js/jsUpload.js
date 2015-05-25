var uploadFormData = function() {
    if(validarArchivo()) {
        $("#idParametro").val($("#fileUpload").attr("idParametro"));
        $("#nombreArchivo").val($("#fileUpload").attr("nombreArchivo"));
        
        var frmUpload = $("#frmUpload");
        frmUpload.attr("action", obtenerContexto('upload/upload.html'));
        frmUpload.ajaxForm({
            success: function(request) {
                request = $.parseJSON( request );
                if(request.tipoResultado == 'EXITO') {
                    if(request.mensaje != null) {
                        openJqInfo({content: request.mensaje});
                        $("#btnUpload").button("disable");
                        $("#lblNombreArchivo").html("");
                        $("#fileUpload").val("");
                    }
                } else if(request.tipoResultado == 'ERROR_SISTEMA') {
                    openJqError({type: "SYS", content: request.mensaje});
                } else if(request.tipoResultado == 'ADVERTENCIA') {
                    openJqWarn({content: request.mensaje});
                }
            },
            dataType:"text"
        });
        frmUpload.submit();
    }
},

validarArchivo = function(){
    var pathArchivo = $("#fileUpload").val().split("\\"),
        nombreArchivo = pathArchivo[pathArchivo.length - 1],
        nombreArchivoEsperado = $("#fileUpload").attr("nombreArchivo"),
        result = false;
    
    if (nombreArchivo.toLowerCase() != nombreArchivoEsperado.toLowerCase()){
        openJqWarn({content: "Archivo incorrecto, el nombre debe ser: <b>" + nombreArchivoEsperado + "</b>"});
        $("#btnUpload").button("disable");
        $("#lblNombreArchivo").html("");
        $("#fileUpload").val("");
    } else {
        $("#btnUpload").button("enable");
        $("#lblNombreArchivo").html(nombreArchivo);
        result = true;
    }
    
    return result;
},

listar = function() {
    AjaxUtil({
    	url : obtenerContexto('upload/list.json'),
    	data : {},
    	onSuccess : function(request) {
    		request.listaArchivos.push({nombre: "[Seleccione]"});
    		for(var row in request.listaArchivos) {
    			request.listaArchivos[row].label = request.listaArchivos[row].nombre;
    			request.listaArchivos[row].value = request.listaArchivos[row].nombre;
    		}
    		
    		$("#txtTipoArchivo").puiautocomplete({
    	        completeSource: request.listaArchivos,
    	        dropdown: true,
    	        content: function(data) {  
                    return '<span>' + data.value.nombre + '</span>';  
                },
                select: function(event, item) {
            		if($('#txtTipoArchivo').val() == '[Seleccione]') {
            			if(!$("#fileUpload").hasClass("hide")) {
            				$("#fileUpload").addClass("hide");
    	            		$("#btnFileUpload").button("disable");
    	            	}
            		} else {
            			if($("#fileUpload").hasClass("hide")) {
            				$("#fileUpload").removeClass("hide");
    	            		$("#btnFileUpload").button("enable");
    	            	}
            		}
                	$("#fileUpload")
                		.attr("idParametro", item.data('value').id)
                		.attr("extension", item.data('value').texto2)
                		.attr("nombreArchivo", item.data('value').texto);
            		$("#btnUpload").button("disable");
                }
    	    });
    		
    		$('#txtTipoArchivo').val('[Seleccione]'); 
    	}
    });
};

$(document).ready(function() {
    
    $("button").button();
    $("#btnFileUpload").button("disable");
    $("#btnUpload")
        .button("disable")
        .on("click", uploadFormData);
    
    $("#fileUpload").on("change", function(){
        if($(this).val() == "") {
            $("#btnUpload").button("disable");
        } else {
            var pathArchivo = $(this).val().split("\\"),
            nombreArchivo = pathArchivo[pathArchivo.length - 1],
            nombreArchivoEsperado = $(this).attr("nombreArchivo");
            $("#btnUpload").button("enable");
            $("#lblNombreArchivo").html(nombreArchivo);
        }
    });
    
    listar();
});