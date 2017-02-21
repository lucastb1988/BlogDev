package br.com.lucas.blog.config;

import java.text.SimpleDateFormat;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.hibernate4.Hibernate4Module;

/**
 * @author Lucas.
 * 
 *         Classe que representa as configurações de MVC do Spring com seus respectivos métodos/beans.
 * 
 */

@Configuration
@EnableWebMvc
@ComponentScan("br.com.lucas.blog")
public class SpringMVCConfig extends WebMvcConfigurerAdapter {		
	
	@Override
	public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {

		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.registerModule(new Hibernate4Module());
		
		Jackson2ObjectMapperBuilder builder = new Jackson2ObjectMapperBuilder();
		builder.dateFormat(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss"));
		
		MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter(builder.build());
		converter.setObjectMapper(objectMapper);
		
		converters.add(new ByteArrayHttpMessageConverter());
		converters.add(converter);
	}

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {

		registry.addResourceHandler("/css/**").addResourceLocations("/WEB-INF/static/css/");
		
		registry.addResourceHandler("/js/**").addResourceLocations("/WEB-INF/static/js/");
	}

	@Bean
	public MultipartResolver multipartResolver() {
		CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver();
		multipartResolver.setMaxUploadSize(100000); //aproximadamente 100k
		
		return multipartResolver;
	}

	@Bean(name = "jspViewResolver")
	public InternalResourceViewResolver resourceViewResolver() {
		InternalResourceViewResolver resolver = new InternalResourceViewResolver();
		resolver.setPrefix("/WEB-INF/views/");
		resolver.setSuffix(".jsp");
		resolver.setViewClass(JstlView.class);
		
		return resolver;		
	}
}