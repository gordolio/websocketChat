package com.gordonchild.websocket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.dozer.DozerBeanMapper;
import org.dozer.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
public class WebConfig extends WebMvcConfigurationSupport {

    private static final Logger LOG = LoggerFactory.getLogger(WebConfig.class);

    @Bean
    public Mapper mapper() throws IOException {
        List<String> mappings = new ArrayList<>();
        mappings.add("gameMappings.xml");
        DozerBeanMapper mapper = new DozerBeanMapper();
        for(String mapping : mappings) {
            InputStream stream = WebConfig.class.getResourceAsStream("/dozer/" + mapping);
            mapper.addMapping(stream);
            stream.close();
        }
        return mapper;
    }

    @Bean
    public MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter() {
        MappingJackson2HttpMessageConverter jsonConverter = new MappingJackson2HttpMessageConverter();
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        jsonConverter.setObjectMapper(objectMapper);
        return jsonConverter;
    }

    @Bean
    public MappingJackson2HttpMessageConverter customJackson2HttpMessageConverter() {
        MappingJackson2HttpMessageConverter jsonConverter = new MappingJackson2HttpMessageConverter();
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        jsonConverter.setObjectMapper(objectMapper);
        return jsonConverter;
    }

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        converters.add(this.customJackson2HttpMessageConverter());
        super.addDefaultHttpMessageConverters(converters);
    }

    @Override
    protected void addResourceHandlers(ResourceHandlerRegistry registry) {
        try {
            InputStream stream = WebConfig.class.getResourceAsStream("/resourcesMapping.csv");
            BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
            String line;
            while(null != (line = reader.readLine())) {
                String[] split = line.split("\\|");
                if(split.length > 1 && StringUtils.isNotEmpty(split[0]) && StringUtils.isNotEmpty(split[1])) {
                    registry.addResourceHandler(split[0].trim()).addResourceLocations(split[1].trim());
                }
            }
        } catch(IOException ex) {
            LOG.error("Error mapping resources", ex);
        }
    }
}
