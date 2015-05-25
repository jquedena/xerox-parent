var

formatterJobInstance = function(cellvalue, options, rowObject) {
    return $.isPlainObject(rowObject.jobInstance) ? rowObject.jobInstance.jobName : rowObject.jobInstance;
},

formatterExitStatus = function(cellvalue, options, rowObject) {
    return rowObject.exitStatus.exitCode;
},

formatterValue = function(cellvalue, options, rowObject) {
    return rowObject.type == "DATE" ? DateUtil.toString(DateUtil.longToDate(cellvalue), DateUtil.DDMMYYYYHHmmss) : cellvalue;
},

configurarJobExecutions = function(runningJobInstances) {
    configurarGrid("pnlJobExecutions", {
        datatype : "local",
        data : runningJobInstances,
        height : "auto",
        colNames : [ 'Nombre', 'Inici\u00F3', 'Termin\u00F3',
                '\u00DAltima Ejecuci\u00F3n', 'Estado' ],
        colModel : [ {
            name : 'jobInstance',
            index : 'jobInstance',
            width : 250,
            formatter : formatterJobInstance
        }, {
            name : 'startTime',
            index : 'startTime',
            width : 140,
            formatter : formatterDate,
            align : "center"
        }, {
            name : 'endTime',
            index : 'endTime',
            width : 140,
            formatter : formatterDate,
            align : "center"
        }, {
            name : 'lastUpdated',
            index : 'lastUpdated',
            width : 140,
            formatter : formatterDate,
            align : "center"
        }, {
            name : 'exitStatus',
            index : 'exitStatus',
            width : 110,
            formatter : formatterExitStatus,
            align : "center"
        } ]
    });

    if (runningJobInstances.length > 0) {
        configurarJobParameters(runningJobInstances[0].jobParameters.parameters);
        configurarStepExecutions(runningJobInstances[0].stepExecutions);
    }
},

configurarJobParameters = function(jobExecution) {
    var jobParameters = [];
    for ( var p in jobExecution) {
        if (jobExecution.hasOwnProperty(p)) {
            jobParameters.push({
                "key" : p,
                "value" : jobExecution[p].value,
                "type" : jobExecution[p].type
            });
        }
    }
    configurarGrid("pnlParametros", {
        datatype : "local",
        data : jobParameters,
        height : "auto",
        colNames : [ 'Nombre', 'Valor' ],
        colModel : [ {
            name : 'key',
            index : 'key',
            width : 140
        }, {
            name : 'value',
            index : 'value',
            formatter : formatterValue,
            width : 500
        } ]
    });
},

configurarStepExecutions = function(stepExecutions) {
    configurarGrid("pnlPasos", {
        datatype : "local",
        data : stepExecutions,
        height : "auto",
        width : 900,
        colNames : [ 'Paso', 'Estado', 'Est. Paso', 'Inici\u00F3',
                'Termin\u00F3', 'Commit', 'Rollback', 'Ley\u00F3',
                'Escribi\u00F3' ],
        colModel : [ {
            name : 'stepName',
            index : 'stepName',
            width : 300
        }, {
            name : 'status',
            index : 'status',
            width : 90
        }, {
            name : 'statusStep',
            index : 'statusStep',
            formatter : formatterExitStatus,
            width : 90
        }, {
            name : 'startTime',
            index : 'startTime',
            width : 140,
            formatter : formatterDate,
            align : "center"
        }, {
            name : 'endTime',
            index : 'endTime',
            width : 140,
            formatter : formatterDate,
            align : "center"
        }, {
            name : 'commitCount',
            index : 'commitCount',
            width : 100,
            align : "right"
        }, {
            name : 'rollbackCount',
            index : 'rollbackCount',
            width : 100,
            align : "right"
        }, {
            name : 'readCount',
            index : 'readCount',
            width : 100,
            align : "right"
        }, {
            name : 'writeCount',
            index : 'writeCount',
            width : 100,
            align : "right"
        } ]
    });
},

verDetalle = function(jobName) {
    AjaxUtil({
        url : obtenerContexto("scheduler/detail/" + jobName + ".json"),
        onSuccess : function(request) {
            if (request.runningJobInstances.length > 0) {
                if (!$("#pnlDesconocido").hasClass("hide")) {
                    $("#pnlDesconocido").addClass("hide");
                }
                $("#pnlDetalleEjecucion").removeClass("hide");

                if (request.runningJobInstances[0].jobInstance == undefined) {
                    request.runningJobInstances[0].jobInstance = request.jobInstance;
                }

                configurarJobExecutions(request.runningJobInstances);
            } else {
                $("#pnlDesconocido").removeClass("hide");
                if (!$("#pnlDetalleEjecucion").hasClass("hide")) {
                    $("#pnlDetalleEjecucion").addClass("hide");
                }
            }

            $("#pnlDetalleTarea").removeClass("hide");
            $("#pnlTareas").addClass("hide");
        }
    });
},

fmtSemaforo = function(cellvalue, options, rowObject) {
    src = cellvalue == 'COMPLETED' ? obtenerContexto('public/img/green-circle-icone-4156-16.png')
            : cellvalue == 'FAILED' ? obtenerContexto('public/img/red-circle-icone-5751-16.png')
                    : obtenerContexto('public/img/yellow-circle-icone-7011-16.png');

    return "<a href='javascript:void(0);' onclick=\"verDetalle('"
            + rowObject.jobName + "')\"><img src='" + src + "'></a>";
},

configurarTrigger = function(request) {
    configurarGrid("pnlTrigger", {
        datatype : "local",
        data : request.triggerInstances,
        height : "auto",
        colNames : [ 'Nombre', 'Grupo', 'Nombre', 'Grupo', 'Estado', 'Tipo',
                'Pr\u00F3xima<br>Ejecuci\u00F3n',
                'Est. \u00DAlt.<br>Ejecuci\u00F3n' ],
        colModel : [ {
            name : 'triggerName',
            index : 'triggerName',
            width : 200
        }, {
            name : 'triggerGroup',
            index : 'triggerGroup',
            width : 70
        }, {
            name : 'jobName',
            index : 'jobName',
            width : 200
        }, {
            name : 'jobGroup',
            index : 'jobGroup',
            width : 70
        }, {
            name : 'triggerState',
            index : 'triggerState',
            width : 60
        }, {
            name : 'triggerType',
            index : 'triggerType',
            width : 40
        }, {
            name : 'nextFireTime',
            index : 'nextFireTime',
            width : 140,
            formatter : formatterDate,
            align : "center"
        }, {
            name : 'exitCode',
            index : 'exitCode',
            width : 60,
            formatter : fmtSemaforo,
            align : "center"
        } ]
    }, null, {
        useColSpanStyle : true,
        groupHeaders : [ {
            startColumnName : 'triggerName',
            numberOfColumns : 2,
            titleText : 'Disparador'
        }, {
            startColumnName : 'jobName',
            numberOfColumns : 2,
            titleText : 'Trabajo'
        } ]
    });
},

listar = function() {
    var _ajax = $.ajax({
        url : obtenerContexto("scheduler/listar.json")
    });

    _ajax.success(function(request) {
        if (request.tipoResultado == 'EXITO') {
            configurarTrigger(request);
        } else if (request.tipoResultado == 'ERROR_SISTEMA') {
            openJqError({
                type : "SYS",
                content : request.mensaje
            });
        }
    });
};

$(document).ready(function() {
    listar();

    $("#btnVolverPanel").button().on("click", function() {
        listar();
        $("#pnlDetalleTarea").addClass("hide");
        $("#pnlTareas").removeClass("hide");
    });
    $("#btnActualizar").button().on("click", listar);
});

$(window).on("resize", function() {
    var browser = new Browser();
    $("#tbl_pnlTrigger").jqGrid('setGridHeight', browser.viewportHeight - 320);
});