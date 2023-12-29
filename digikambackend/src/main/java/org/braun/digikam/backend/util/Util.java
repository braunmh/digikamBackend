package org.braun.digikam.backend.util;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.Properties;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author mbraun
 */
public final class Util {

    private static final Logger LOG = LogManager.getLogger();

    private Util() {
    }

    public static class EJB {

        private static final Properties INITIAL_CONTEXT_PROPERTIES = new Properties();

        static {
            INITIAL_CONTEXT_PROPERTIES.put("java.naming.factory.initial", "org.apache.openejb.client.LocalInitialContextFactory");
        }

        private EJB() {
        }

        public static <T> T lookup(Class<T> clazz) {
            String name = "java:module/" + clazz.getSimpleName();
            try {
                InitialContext ic = new InitialContext(INITIAL_CONTEXT_PROPERTIES);
                Object o = ic.lookup(name);
                return clazz.cast(o);
            } catch (NamingException e) {
                LOG.error("Lookup failed for " + name, e);
            }
            return null;
        }
    }
    
    public static LocalDateTime convert(Date date) {
        if (date == null) {
            return null;
        }
        
        return LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
    }
}
