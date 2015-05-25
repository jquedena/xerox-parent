package com.bureau.xerox.dao.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.bureau.xerox.dao.ParametroDAO;
import com.bureau.xerox.domain.Parametro;
import com.bureau.xerox.enums.Respuesta;
import com.bureau.xerox.enums.TipoParametro;
import com.everis.core.dao.impl.HibernateDAO;
import com.everis.core.enums.Estado;

@Repository("parametroDAO")
public class ParametroDAOImpl extends HibernateDAO<Parametro> implements ParametroDAO {

    private static final long serialVersionUID = 1L;

    @Override
    @SuppressWarnings("unchecked")
    public List<Parametro> listarHijos(long idParametroPadre, Estado estado) {
        Criteria criterioParametro = super.getCriteria(Parametro.class);
        criterioParametro.setFetchMode("parametro", FetchMode.JOIN);
        criterioParametro.setFetchMode("parametroTipo", FetchMode.JOIN);
        criterioParametro.add(Restrictions.eq("parametro.id", idParametroPadre));
        if (estado != null) {
            criterioParametro.add(Restrictions.eq("estado", estado.toString()));
        }
        criterioParametro.addOrder(Order.asc("id"));
        return (List<Parametro>) criterioParametro.list();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Parametro> listarHijosOrdPorEntero(long idParametroPadre, Estado estado) {
        Criteria criterioParametro = super.getCriteria(Parametro.class);
        criterioParametro.setFetchMode("parametro", FetchMode.JOIN);
        criterioParametro.setFetchMode("parametroTipo", FetchMode.JOIN);
        criterioParametro.add(Restrictions.eq("parametro.id", idParametroPadre));
        if (estado != null) {
            criterioParametro.add(Restrictions.eq("estado", estado.toString()));
        }
        criterioParametro.addOrder(Order.asc("entero"));
        return (List<Parametro>) criterioParametro.list();
    }

    @Override
    public List<Parametro> listarHijos(long idParametroPadre) {
        return (List<Parametro>) listarHijos(idParametroPadre, Estado.ACTIVO);
    }

    @Override
    public List<Parametro> listarHijosOrdPorEntero(long idParametroPadre) {
        return (List<Parametro>) listarHijosOrdPorEntero(idParametroPadre, Estado.ACTIVO);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Parametro> listarPadres(Estado estado, Respuesta permiteHijos) {

        Criteria criterioParametro = super.getCriteria(Parametro.class);
        criterioParametro.setFetchMode("parametro", FetchMode.JOIN);
        criterioParametro.setFetchMode("parametroTipo", FetchMode.JOIN);
        criterioParametro.add(Restrictions.eq("tipo", TipoParametro.PARAMETRO_PADRE.toLong()));

        if (estado != null) {
            criterioParametro.add(Restrictions.eq("estado", estado.toString()));
        }

        if (permiteHijos != null) {
            criterioParametro.add(Restrictions.eq("permiteHijo", permiteHijos.toCharacter()));
        }

        criterioParametro.addOrder(Order.asc("nombre"));

        return (List<Parametro>) criterioParametro.list();
    }

    @Override
    public List<Parametro> listarPadres() {
        return listarPadres(Estado.ACTIVO, null);
    }

    @Override
    public List<Parametro> listarPadresPermitenHijos() {
        return listarPadres(Estado.ACTIVO, Respuesta.SI);
    }

    @Override
    public Parametro obtener(long idParametro) {
        Criteria criterioParametro = super.getCriteria(Parametro.class);
        criterioParametro.setFetchMode("parametro", FetchMode.JOIN);
        criterioParametro.setFetchMode("parametroTipo", FetchMode.JOIN);
        criterioParametro.add(Restrictions.idEq(idParametro));

        return (Parametro) criterioParametro.uniqueResult();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Parametro> listar(long idTipo, long idPadre, String nombre, String idEstado) {
        Criteria criterioParametro = super.getCriteria(Parametro.class);
        criterioParametro.setFetchMode("parametro", FetchMode.JOIN);
        criterioParametro.setFetchMode("parametroTipo", FetchMode.JOIN);

        if (idPadre != -1L) {
            criterioParametro.add(Restrictions.eq("parametro.id", idPadre));
        }

        if (idTipo != -1L) {
            criterioParametro.add(Restrictions.eq("tipo", idTipo));
        }

        criterioParametro.add(Restrictions.ilike("nombre", "%" + nombre + "%"));

        if (!"-1".equalsIgnoreCase(idEstado)) {
            criterioParametro.add(Restrictions.eq("estado", idEstado));
        }

        criterioParametro.addOrder(Order.asc("id"));

        return (List<Parametro>) criterioParametro.list();
    }

    @Override
    public boolean codigoExiste(String codigo, long idTipo, long idParametro, long idPadre) {
        Criteria criterioParametro = super.getCriteria(Parametro.class);

        criterioParametro.add(Restrictions.eq("tipo", idTipo));
        if (idParametro != -1L) {
            criterioParametro.add(Restrictions.ne("id", idParametro));
        }
        if (idPadre != -1L) {
            criterioParametro.add(Restrictions.eq("parametro.id", idPadre));
        }
        criterioParametro.add(Restrictions.eq("codigo", codigo.trim()).ignoreCase());

        return !criterioParametro.list().isEmpty();
    }

    @Override
    public boolean nombreExiste(String nombre, long idTipo, long idParametro, long idPadre) {
        Criteria criterioParametro = super.getCriteria(Parametro.class);

        criterioParametro.add(Restrictions.eq("tipo", idTipo));
        if (idParametro != -1L) {
            criterioParametro.add(Restrictions.ne("id", idParametro));
        }
        if (idPadre != -1L) {
            criterioParametro.add(Restrictions.eq("parametro.id", idPadre));
        }
        criterioParametro.add(Restrictions.eq("nombre", nombre.trim()).ignoreCase());

        return !criterioParametro.list().isEmpty();
    }

    @Override
    public void insertar(Parametro objparametro) {
        super.save(objparametro);
    }

    @Override
    public void modificar(Parametro objparametro) {
        Parametro objparametroRepositorio = this.obtener(objparametro.getId());

        objparametroRepositorio.setNombre(objparametro.getNombre());
        objparametroRepositorio.setDescripcion(objparametro.getDescripcion());
        objparametroRepositorio.setEstado(objparametro.getEstado());

        if (objparametro.getCodigo() != null) {
            objparametroRepositorio.setCodigo(objparametro.getCodigo());
        }

        if (objparametro.getEntero() != null) {
            objparametroRepositorio.setEntero(objparametro.getEntero());
        }

        if (objparametro.getDecimales() != null) {
            objparametroRepositorio.setDecimales(objparametro.getDecimales());
        }

        if (objparametro.getTexto() != null) {
            objparametroRepositorio.setTexto(objparametro.getTexto());
        }

        if (objparametro.getTexto2() != null) {
            objparametroRepositorio.setTexto2(objparametro.getTexto2());
        }

        if (objparametro.getTexto3() != null) {
            objparametroRepositorio.setTexto3(objparametro.getTexto3());
        }

        if (objparametro.getFecha() != null) {
            objparametroRepositorio.setFecha(objparametro.getFecha());
        }

        if (objparametro.getHora() != null) {
            objparametroRepositorio.setHora(objparametro.getHora());
        }

        if (objparametro.getBooleano() != null) {
            objparametroRepositorio.setBooleano(objparametro.getBooleano());
        }

        objparametroRepositorio.setUsuarioModificacion(objparametro.getUsuarioModificacion());
        objparametroRepositorio.setFechaModificacion(objparametro.getFechaModificacion());

        super.update(objparametroRepositorio);
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Parametro> listarParametrosPorTipo(Long idParametria, Long idTipo) {
        Criteria criterioParametro = super.getCriteria(Parametro.class);
        criterioParametro.setFetchMode("parametro", FetchMode.JOIN);
        criterioParametro.add(Restrictions.or(Restrictions.eq("parametro.id", idParametria), Restrictions.and(Restrictions.eq("tipo", idTipo), Restrictions.gt("parametro.id", idParametria))));
        criterioParametro.addOrder(Order.asc("id"));

        return (List<Parametro>) criterioParametro.list();
    }

    @Override
    public Parametro obtenerParametroPorCodigo(Long idPadre, String codigo) {
        Criteria criterioParametro = super.getCriteria(Parametro.class);
        if (idPadre != -1L) {
            criterioParametro.add(Restrictions.eq("parametro.id", idPadre));
        }
        if (codigo != null) {
            criterioParametro.add(Restrictions.eq("codigo", codigo.trim()).ignoreCase());
        }
        return (Parametro) criterioParametro.uniqueResult();
    }

}
