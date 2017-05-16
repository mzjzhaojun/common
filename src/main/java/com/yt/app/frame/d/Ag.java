package com.yt.app.frame.d;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

@Configuration
public class Ag extends WebMvcConfigurerAdapter {
	public void configureMessageConverters(List<HttpMessageConverter<?>> paramList) {
		paramList.add(k());
	}

	@Bean
	public MappingJackson2HttpMessageConverter k() {
		MappingJackson2HttpMessageConverter localMappingJackson2HttpMessageConverter = new MappingJackson2HttpMessageConverter();
		ObjectMapper localObjectMapper = new ObjectMapper();
		localObjectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
		SimpleModule localSimpleModule = new SimpleModule();
		localSimpleModule.addSerializer(Long.class, ToStringSerializer.instance);
		localSimpleModule.addSerializer(Long.TYPE, ToStringSerializer.instance);
		localObjectMapper.registerModule(localSimpleModule);
		localMappingJackson2HttpMessageConverter.setObjectMapper(localObjectMapper);
		return localMappingJackson2HttpMessageConverter;
	}

	public void addResourceHandlers(ResourceHandlerRegistry paramResourceHandlerRegistry) {
		paramResourceHandlerRegistry.addResourceHandler(new String[] { "/static/**" }).addResourceLocations(
				new String[] { "classpath:/templates/static/" });
	}

	@Bean
	public InternalResourceViewResolver l() {
		InternalResourceViewResolver localInternalResourceViewResolver = new InternalResourceViewResolver();
		localInternalResourceViewResolver.setPrefix("/");
		localInternalResourceViewResolver.setSuffix(".html");
		return localInternalResourceViewResolver;
	}
}