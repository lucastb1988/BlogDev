package br.com.lucas.blog.config;

import javax.servlet.FilterRegistration;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

import org.springframework.orm.jpa.support.OpenEntityManagerInViewFilter;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.filter.DelegatingFilterProxy;
import org.springframework.web.servlet.DispatcherServlet;

/**
 * @author Lucas.
 * 
 *         Classe que representa as configura��es de web.xml do Spring com suas respectivas funcionalidades.
 * 
 */

public class SpringWebXmlConfig implements WebApplicationInitializer{

	public void onStartup(ServletContext servletContext) throws ServletException {
		
		AnnotationConfigWebApplicationContext webContext = new AnnotationConfigWebApplicationContext();		
		servletContext.addListener(new ContextLoaderListener(webContext)); //configura o ContextLoadListener para habilitar o Fetch.Lazy (carrega os objetos a medida que se faz necess�rio para o usuario)		
		webContext.register(SpringMVCConfig.class);
		webContext.setServletContext(servletContext);
		
		DispatcherServlet dispatcherServlet = new DispatcherServlet(webContext);
		dispatcherServlet.setThrowExceptionIfNoHandlerFound(true); //precisa setar como true essa exce��o NoHandlerFoundException para ter acesso ao ExceptionController e tratar a exce��o
		
		ServletRegistration.Dynamic reDynamic = servletContext.addServlet("dispatcher", dispatcherServlet);
		reDynamic.setLoadOnStartup(1);
		reDynamic.addMapping("/");
		
		FilterRegistration.Dynamic encodingFilter = 
				servletContext.addFilter("encodingFilter", new CharacterEncodingFilter());
		encodingFilter.setInitParameter("encoding", "UTF-8");
		encodingFilter.setInitParameter("forceEncoding", "true");
		encodingFilter.addMappingForUrlPatterns(null, true, "/*");	
		
		//configura��o abaixo informada para que a p�gina seja carrega utilizando o Fetch.Lazy (carrega os objetos a medida que se faz necess�rio para o usuario)
		//(ex.categorias) a medida que vai renderizando a p�gina os m�todos Get categorias v�o sendo chamados e a lista relacionada a cada uma destas categorias vai sendo adicionada a p�gina
		//e assim n�o � lan�ada uma exce��o de LazyException
		FilterRegistration.Dynamic inViewSession = 
				servletContext.addFilter("Spring OpenEntityManagerInViewFilter", new OpenEntityManagerInViewFilter());
		inViewSession.setAsyncSupported(Boolean.TRUE);
		inViewSession.addMappingForUrlPatterns(null, true, "/*");
		
		//configura��o abaixo para Spring Security
		FilterRegistration.Dynamic securityFilter = 
				servletContext.addFilter("springSecurityFilterChain", new DelegatingFilterProxy());
		securityFilter.setAsyncSupported(Boolean.TRUE);
		securityFilter.addMappingForUrlPatterns(null, true, "/*");
	}	
}