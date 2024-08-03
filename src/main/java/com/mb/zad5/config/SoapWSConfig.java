package com.mb.zad5.config;

import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.ws.config.annotation.EnableWs;
import org.springframework.ws.config.annotation.WsConfigurerAdapter;
import org.springframework.ws.transport.http.MessageDispatcherServlet;
import org.springframework.ws.wsdl.wsdl11.DefaultWsdl11Definition;
import org.springframework.xml.xsd.SimpleXsdSchema;
import org.springframework.xml.xsd.XsdSchema;

@EnableWs
@Configuration
public class SoapWSConfig extends WsConfigurerAdapter {

    public static final String BOOK_NAMESPACE = "localhost/books";

    @Bean
    public ServletRegistrationBean<MessageDispatcherServlet> messageDispatcherServletServlet(ApplicationContext context) {
        MessageDispatcherServlet servlet = new MessageDispatcherServlet();
        servlet.setApplicationContext(context);
        servlet.setTransformSchemaLocations(true);
        servlet.setTransformWsdlLocations(true);

        return new ServletRegistrationBean<>(servlet, "/ws/*");
    }

    @Bean(name="books")
    public DefaultWsdl11Definition defaultWsdl11Definition(XsdSchema booksSchema) {
        DefaultWsdl11Definition wsdl11Definition = new DefaultWsdl11Definition();
        wsdl11Definition.setPortTypeName("BookPort");
        wsdl11Definition.setLocationUri("/ws/books");
        wsdl11Definition.setTargetNamespace(BOOK_NAMESPACE);
        wsdl11Definition.setSchema(booksSchema);

        return wsdl11Definition;
    }

    @Bean
    public XsdSchema booksSchema() {
        return new SimpleXsdSchema(new ClassPathResource("books.xsd"));
    }

}
