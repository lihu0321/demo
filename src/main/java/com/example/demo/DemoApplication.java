package com.example.demo;

import com.example.demo.framework.common.exception.handler.SystemExceptionAdviceHalder;
import com.example.demo.rbac.filter.AuthInteceptor;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.deser.std.StringDeserializer;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * springboot源码：https://blog.csdn.net/qq_26000415/article/category/7369081/3
 */
@SpringBootApplication
public class DemoApplication  extends SpringBootServletInitializer implements WebMvcConfigurer{
    @Bean
    public Module module() {
        JsonSerializer<Long> longSerializer = new StdSerializer<Long>(Long.class) {
            @Override
            public void serialize(Long value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
                if (value == null) {
                    gen.writeNull();
                    return;
                }
                gen.writeString(String.valueOf(value));
            }
        };
        return new SimpleModule()
                .addSerializer(Long.TYPE, longSerializer)
                .addSerializer(Long.class, longSerializer)
                .addDeserializer(String.class, new StdDeserializer<String>(String.class) {
                    @Override
                    public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
                        String result = StringDeserializer.instance.deserialize(p, ctxt);
                        if (StringUtils.isBlank(result)) return null;
                        return StringUtils.trim(result);
                    }
                });
    }

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        //排除掉原来的MappingJackson2HttpMessageConverter
        List<MappingJackson2HttpMessageConverter> originalConverters = new ArrayList<>();
        for (HttpMessageConverter<?> converter : converters) {
            if (converter instanceof MappingJackson2HttpMessageConverter) {
                originalConverters.add((MappingJackson2HttpMessageConverter) converter);
            }
        }
        if (CollectionUtils.isNotEmpty(originalConverters)) {
            converters.removeAll(originalConverters);
        }
        MappingJackson2HttpMessageConverter jackson2HttpMessageConverter = new MappingJackson2HttpMessageConverter();
        ObjectMapper objectMapper = new ObjectMapper();

        // 将long类型的数据转为String类型
        SimpleModule simpleModule = new SimpleModule();
        simpleModule.addSerializer(Long.class, ToStringSerializer.instance);
        simpleModule.addSerializer(Long.TYPE, ToStringSerializer.instance);
        simpleModule.addSerializer(long.class, ToStringSerializer.instance);
        objectMapper.registerModule(simpleModule);
        jackson2HttpMessageConverter.setObjectMapper(objectMapper);
        converters.add(jackson2HttpMessageConverter);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(DemoApplication.class);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 添加一个拦截器，连接以/admin为前缀的 url路径
        registry.addInterceptor(new AuthInteceptor()).addPathPatterns("/**");
    }

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

    @Bean
    public SystemExceptionAdviceHalder commonExceptionAdviceHandler() {
        return new SystemExceptionAdviceHalder();
    }

}
