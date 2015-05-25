package com.bureau.xerox.controller;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import com.everis.web.constant.Produces;

import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.bureau.batch.domain.JobConfig;
import com.bureau.xerox.domain.Parametro;
import com.bureau.xerox.enums.Configuracion;
import com.bureau.xerox.enums.Respuesta;
import com.bureau.xerox.model.ParametroModel;
import com.bureau.xerox.service.ParametroService;
import com.bureau.xerox.service.SchedulerService;
import com.everis.core.enums.Estado;
import com.everis.core.security.CurrentUser;
import com.everis.enums.FormatoFecha;
import com.everis.enums.Resultado;
import com.everis.util.FechaUtil;
import com.everis.web.constant.Session;
import com.everis.web.controller.impl.AbstractSpringControllerImpl;
import com.everis.web.enums.MappingBasic;

/**
 * Controlador para el acceso a la tabla de par\u00E1metros
 * 
 * @author jquedena
 *
 */
@Controller("parametroController")
@Scope("prototype")
@RequestMapping(value = "param")
@SessionAttributes({ Session.CURRENT_USER })
public class ParametroController extends AbstractSpringControllerImpl {

    private static final long serialVersionUID = 1L;
    private static final Logger logger = Logger.getLogger(ParametroController.class);
    private String[] exclude = new String[] { "*.class", "*.parametro", "*.parametroTipo", "*.parametros" };
    private String ERROR_CODIGO_EXISTE = "El c\u00F3digo personalizado ingresado ya existe, ingrese uno diferente.";
    private String ERROR_NOMBRE_EXISTE = "El nombre del par\u00E1metro ingresado ya existe.";
    private String EXITO_ACTUALIZACION = "Los datos fueron actualizados con \u00E9xito.";
    private String EXITO_REGISTRO = "Los datos fueron registrados con \u00E9xito.";

    @Resource(name = "parametroService")
    private ParametroService parametroService;

    @Resource(name = "schedulerService")
    private SchedulerService schedulerService;

    /**
     * P\u00E1gina de inicio, redirecciona: params/configuracionParametria.jsp
     * Instancia el objeto ParametroModel(con los siguientes valores listaTipo,
     * listaPadres, listaPadrePermiteHijos)
     * 
     * @param model
     * @return
     */
    @RequestMapping("index")
    public String index(Model model) {
        model.addAttribute("procesoClass", "");
        model.addAttribute("consultaClass", "");
        model.addAttribute("administracionClass", "ui-state-active-bbva");

        ParametroModel paramModel = new ParametroModel();
        try {
            paramModel.setListaTipos(parametroService.listarHijos(Configuracion.PB_TIPOS_PARAMETRO.getKey()));

            if (paramModel.getListaTipos() != null && !paramModel.getListaTipos().isEmpty()) {
                paramModel.setListaPadres(parametroService.listarPadres());
                paramModel.setListaPadresPermiteHijos(parametroService.listarPadresPermitenHijos());
            }

            paramModel.setIdEstado(Estado.ACTIVO.getEstado());
            paramModel.setIdPadre(Configuracion.PB_TIPOS_PARAMETRO.getKey());
            model.addAttribute("idEdicionTipo", "-1");
            model.addAttribute("idEdicionPadre", "-1");
            model.addAttribute("idEdicionEstado", "-1");
            model.addAttribute("paramModel", paramModel);
        } catch (Exception e) {
            logger.error("iniciarConfiguracionParametroAction", e);
            return MappingBasic.ERROR.toString();
        }
        return "params/configuracionParametria";
    }

    /**
     * Permite la b\u00FAsqueda de par\u00E1metros
     * Devuelve el objeto ParametroModel(con los siguientes valores tipoResultado,
     * mensaje, listaParametria) serializado
     * en formato JSON
     * 
     * @param paramModel
     * @param result
     * @return
     */
    @RequestMapping(value = "buscarParametrosAction", method = { RequestMethod.POST }, produces = Produces.JSON)
    public @ResponseBody String buscar(@ModelAttribute("paramModel") ParametroModel paramModel, BindingResult result) {
        String json = "";
        try {
            if (!result.hasErrors()) {
                List<Parametro> listaParametro = this.parametroService.listar(paramModel.getIdTipo(), paramModel.getIdPadre(), paramModel.getNombre(), paramModel.getIdEstado());

                paramModel.setListaParametria(listaParametro);
                paramModel.setTipoResultado(Resultado.EXITO);
                json = this.renderModelJsonDeepExclude(paramModel, exclude);
            } else {
                json = this.renderErrorSistema(result.getAllErrors());
            }
        } catch (Exception e) {
            logger.error("buscarParametros", e);
            json = this.renderErrorSistema(e);
        }

        return json;
    }

    /**
     * Listas los campos que se deben mostrar en el formulario de mantenimiento
     * Devuelve el objeto ParametroModel(con los siguientes valores tipoResultado,
     * mensaje, item) serializado
     * 
     * @param paramModel
     * @param result
     * @return
     */
    @RequestMapping(value = "mostrarCamposDinamicosAction", method = RequestMethod.POST, produces = Produces.JSON)
    public @ResponseBody String mostrarCamposDinamicos(@ModelAttribute("paramModel") ParametroModel paramModel, BindingResult result) {
        String json = "";
        try {
            if (!result.hasErrors()) {
                Parametro Parametro = this.parametroService.obtener(paramModel.getItem().getId());
                paramModel.setItem(Parametro);
                paramModel.setTipoResultado(Resultado.EXITO);
                json = this.renderModelJsonDeepExclude(paramModel, exclude);
            } else {
                json = this.renderErrorSistema(result.getAllErrors());
            }
        } catch (Exception e) {
            logger.error("mostrarCamposDinamicosAction", e);
            json = this.renderErrorSistema(e);
        }
        return json;
    }

    /**
     * Obtiene el par\u00E1metro para su edici\u00F3n
     * Devuelve el objeto ParametroModel(con los siguientes valores tipoResultado,
     * mensaje, item) serializado
     * 
     * @param paramModel
     * @param result
     * @return
     */
    @RequestMapping(value = "obtenerParametroAction", method = RequestMethod.POST, produces = Produces.JSON)
    public @ResponseBody String obtener(@ModelAttribute("paramModel") ParametroModel paramModel, BindingResult result) {
        String json = "";
        try {
            if (!result.hasErrors()) {
                Parametro Parametro = paramModel.getItem();
                paramModel.setItem(this.parametroService.obtener(Parametro.getId()));
                paramModel.setTipoResultado(Resultado.EXITO);
                json = this.renderModelJsonDeepExclude(paramModel, exclude);
            } else {
                json = this.renderErrorSistema(result.getAllErrors());
            }
        } catch (Exception e) {
            logger.error("mostrarCamposDinamicosAction", e);
            json = this.renderErrorSistema(e);
        }
        return json;
    }

    /**
     * Guardar los cambios del par\u00E1metro en la base de datos
     * Devuelve el objeto ParametroModel(con los siguientes valores tipoResultado,
     * mensaje) serializado
     * Si el parametro pertenede al Tipo Tareas Programadas, invoca al servicio 
     * SchedulerService#rescheduler
     * 
     * @param paramModel
     * @param currentUser
     * @param result
     * @return
     */
    @RequestMapping(value = "guardarParametroAction", method = RequestMethod.POST, produces = Produces.JSON)
    public @ResponseBody String guardar(@ModelAttribute("paramModel") ParametroModel paramModel, @ModelAttribute(Session.CURRENT_USER) CurrentUser currentUser, BindingResult result) {
        String json = "";
        try {
            if (!result.hasErrors()) {
                Parametro param = paramModel.getItem();

                if (paramModel.getFecha() != null && paramModel.getFecha().length() > 0)
                    param.setFecha(FechaUtil.parseFecha(paramModel.getFecha(), FormatoFecha.DDMMYYYY_WITH_SEPARATOR));

                if (param.getId() == 0L) {
                    Parametro paramRepos = this.parametroService.obtener(param.getParametro().getId());

                    param.setCodigoHabil(paramRepos.getCodigoHabil());
                    param.setCodigoEti(paramRepos.getCodigoEti());
                    param.setEnteroHabil(paramRepos.getEnteroHabil());
                    param.setEnteroEti(paramRepos.getEnteroEti());
                    param.setDecimalesHabil(paramRepos.getDecimalesHabil());
                    param.setDecimalesEti(paramRepos.getDecimalesEti());
                    param.setTextoHabil(paramRepos.getTextoHabil());
                    param.setTextoEti(paramRepos.getTextoEti());
                    param.setTextoHabil2(paramRepos.getTextoHabil2());
                    param.setTextoEti2(paramRepos.getTextoEti2());
                    param.setTextoHabil3(paramRepos.getTextoHabil3());
                    param.setTextoEti3(paramRepos.getTextoEti3());
                    param.setFechaHabil(paramRepos.getFechaHabil());
                    param.setFechaEti(paramRepos.getFechaEti());
                    param.setHoraHabil(paramRepos.getHoraHabil());
                    param.setHoraEti(paramRepos.getHoraEti());
                    param.setBooleanoHabil(paramRepos.getBooleanoHabil());
                    param.setBooleanoEti(paramRepos.getBooleanoEti());

                    if (this.parametroService.nombreExiste(param.getNombre(), param.getTipo(), -1L, param.getParametro().getId())) {
                        paramModel.setTipoResultado(Resultado.ERROR);
                        paramModel.setMensaje(this.ERROR_NOMBRE_EXISTE);
                    } else if (param.getCodigoHabil() == Respuesta.SI.toCharacter() && this.parametroService.codigoExiste(param.getCodigo(), param.getTipo(), -1L, param.getParametro().getId())) {
                        paramModel.setTipoResultado(Resultado.ERROR);
                        paramModel.setMensaje(this.ERROR_CODIGO_EXISTE);
                    } else {
                        param.setUsuarioCreacion(currentUser.getCodigoRegistro());
                        param.setFechaCreacion(new Timestamp(new Date().getTime()));

                        this.parametroService.insertar(param);

                        paramModel.setTipoResultado(Resultado.EXITO);
                        paramModel.setMensaje(this.EXITO_REGISTRO);
                    }
                } else {
                    if (this.parametroService.nombreExiste(param.getNombre(), param.getTipo(), param.getId(), param.getParametro() == null ? -1L : param.getParametro().getId())) {
                        paramModel.setTipoResultado(Resultado.ERROR);
                        paramModel.setMensaje(this.ERROR_NOMBRE_EXISTE);
                    } else if (param.getCodigo() != null
                            && this.parametroService.codigoExiste(param.getCodigo(), param.getTipo(), param.getId(), param.getParametro() == null ? -1L : param.getParametro().getId())) {
                        paramModel.setTipoResultado(Resultado.ERROR);
                        paramModel.setMensaje(this.ERROR_CODIGO_EXISTE);
                    } else {
                        param.setUsuarioModificacion(currentUser.getCodigoRegistro());
                        param.setFechaModificacion(new Timestamp(new Date().getTime()));

                        if (Configuracion.PB_CARGAR_CUENTAS.getKey().compareTo(param.getId()) == 0 || Configuracion.PB_GENERAR_ARCHIVO.getKey().compareTo(param.getId()) == 0) {
                            if (Configuracion.PB_CARGAR_CUENTAS.getKey().compareTo(param.getId()) == 0) {
                                param.setTexto("procesoCargaArchivoCuentas:paramProcesoCargaArchivoCuentas");
                            } else if (Configuracion.PB_GENERAR_ARCHIVO.getKey().compareTo(param.getId()) == 0) {
                                param.setTexto("procesoGeneracionArchivo:paramProcesoGenerarArchivo");
                            }

                            JobConfig jobConfig = new JobConfig();
                            String hora[] = param.getHora().split(":");
                            String job[] = param.getTexto().split(":");

                            jobConfig.setJobName(job[0]);
                            jobConfig.setJobProcessorParameter(job[1]);
                            jobConfig.setCodigoRegistro(currentUser.getCodigoRegistro());
                            jobConfig.setDayOfMonth(FechaUtil.getDia(param.getFecha()));
                            jobConfig.setMonth(FechaUtil.getMes(param.getFecha()));
                            jobConfig.setYear(FechaUtil.getAnio(param.getFecha()));
                            jobConfig.setHour(Integer.parseInt(hora[0]));
                            jobConfig.setMinute(Integer.parseInt(hora[1]));

                            schedulerService.rescheduler(jobConfig);
                        }

                        this.parametroService.modificar(param);

                        paramModel.setTipoResultado(Resultado.EXITO);
                        paramModel.setMensaje(this.EXITO_ACTUALIZACION);
                    }
                }

                if (Resultado.EXITO.compareTo(paramModel.getTipoResultado()) == 0 && paramModel.getIdTipo() != 0L) {
                    List<Parametro> listaParametro = this.parametroService.listar(paramModel.getIdTipo(), paramModel.getIdPadre(), paramModel.getNombre(), paramModel.getIdEstado());

                    paramModel.setListaParametria(listaParametro);
                }

                json = this.renderModelJsonDeepExclude(paramModel, exclude);
            } else {
                json = this.renderErrorSistema(result.getAllErrors());
            }
        } catch (Exception e) {
            logger.error("mostrarCamposDinamicosAction", e);
            json = this.renderErrorSistema(e);
        }
        return json;
    }
}
