package com.bureau.batch.aop;

import org.apache.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

import com.everis.core.dao.impl.HibernateDAO;
import com.everis.core.exception.BussinesException;

/**
 * Interceptor de la ejecuci\u00F3n de los m\u00E9todos de la capa de persistencia
 * para poder asignar el perfil configurado en hibernate-config.xml, de c\u00F3mo
 * se van a extraer los datos
 * @author jquedena
 *
 */
@Aspect
public class FetchProfileInterceptor {

    private static final Logger LOG = Logger.getLogger(FetchProfileInterceptor.class);
    private static final String FETCH_PROFILE = "fetchProfile";

    /**
     * Muestra los datos referentes a la invocaci\u00F3n en el archivo de trazas de la
     * aplicaci\u00F3n seg\u00FAn la configuraci\u00F3n del Log4 (Nivel de traza:  DEBUG)
     * @param joinPoint
     * @param lazy
     */
    protected void infoInvoke(ProceedingJoinPoint joinPoint, boolean lazy) {
        if (LOG.isDebugEnabled()) {
            StringBuilder sb = new StringBuilder();
            sb.append("\n\tClass: ");
            sb.append(joinPoint.getTarget().getClass().getName());
            sb.append("\n\tMethod Invoke: ");
            sb.append(joinPoint.getSignature().getName());
            sb.append("\n\tArgs Length: ");
            sb.append(joinPoint.getArgs().length);
            sb.append("\n\tKind: ");
            sb.append(joinPoint.getKind());
            sb.append("\n\tLazy: ");
            sb.append(lazy ? "Use fetchProfile" : "No use fetchProfile");
            LOG.info(sb.toString());
        }
    }

    /**
     * Intercepta los m\u00E9todos de la capa de persistencia que contengan
     * como par\u00E1metros final <b>boolean lazy</b>
     * @param joinPoint, objeto que contiene la informaci\u00F3n del m\u00E9todo que ha realizado la invocaci\u00F3n 
     * @param lazy, indica si se usa el perfil
     * @return Object, objetos de salida del m\u00E9todo invocado
     * @throws BussinesException
     */
    @Around("execution(* com.bbva.*.dao.impl.*.* (..)) && args(.., lazy)")
    public Object invoke(ProceedingJoinPoint joinPoint, boolean lazy) throws BussinesException {
        Object o = null;
        infoInvoke(joinPoint, lazy);

        HibernateDAO<?> dao = (HibernateDAO<?>) joinPoint.getTarget();
        if (lazy && !dao.getCurrentSession().isFetchProfileEnabled(FETCH_PROFILE)) {
            dao.getCurrentSession().enableFetchProfile(FETCH_PROFILE);
            LOG.info("enableFetchProfile: [" + FETCH_PROFILE + "]");
        }

        try {
            o = joinPoint.proceed();
        } catch (Throwable t) {
            throw new BussinesException("FetchProfileInterceptor:invoke(ProceedingJoinPoint joinPoint, boolean lazy)", t);
        } finally {
            if (lazy) {
                dao.getCurrentSession().disableFetchProfile(FETCH_PROFILE);
            }
        }

        return o;
    }
}
