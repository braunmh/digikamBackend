package org.braun.digikam.backend;

import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author mbraun
 */
@WebFilter(filterName = "AuthenticationFilter", urlPatterns = {"/rest/*"})
public class AuthenticationFilter implements Filter {

    private static final Logger LOG = LogManager.getLogger();
    
    // The filter configuration object we are associated with.  If
    // this value is null, this filter instance is not currently
    // configured. 
    private FilterConfig filterConfig = null;
    
    public AuthenticationFilter() {
    }    
    
    /**
     *
     * @param request The servlet request we are processing
     * @param response The servlet response we are creating
     * @param chain The filter chain we are processing
     *
     * @exception IOException if an input/output error occurs
     * @exception ServletException if a servlet error occurs
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
        FilterChain chain)
        throws IOException, ServletException {
//        if (request instanceof HttpServletRequest) { 
//            try (BufferedReader reader = request.getReader()) {
//                String line;
//                while (null != (line = reader.readLine())) {
//                    LOG.info(line);
//                    LOG.info("\n");
//                }
//            } catch (IOException e) {
//                LOG.error(e.getMessage(), e);
//            }
//        }
        chain.doFilter(request, response);
    }

    /**
     * Return the filter configuration object for this filter.
     * @return filterConfig
     */
    public FilterConfig getFilterConfig() {
        return (this.filterConfig);
    }

    /**
     * Destroy method for this filter
     */
    @Override
    public void destroy() {        
    }

    /**
     * Init method for this filter
     */
    @Override
    public void init(FilterConfig filterConfig) {        
        this.filterConfig = filterConfig;
    }

    
}
