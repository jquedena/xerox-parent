package com.everis.core.service;

import java.io.Serializable;
import java.util.List;

public interface IDataManipulationService<T> extends Serializable {

    T insertar(T o);

    T actualizar(T o);

    List<T> actualizar(List<T> o);

    void eliminar(T o);

    void eliminar(List<T> o);
}
