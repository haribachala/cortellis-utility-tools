package com.clarivate.cortellis;

import org.springframework.boot.autoconfigure.PropertyPlaceholderAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.DispatcherServletAutoConfiguration;
import org.springframework.boot.autoconfigure.web.EmbeddedServletContainerAutoConfiguration;
import org.springframework.boot.autoconfigure.web.ServerPropertiesAutoConfiguration;
import org.springframework.boot.autoconfigure.web.WebMvcAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@Import(value={ServerPropertiesAutoConfiguration.class,
		EmbeddedServletContainerAutoConfiguration.class,
		WebMvcAutoConfiguration.class,
		DispatcherServletAutoConfiguration.class,
		PropertyPlaceholderAutoConfiguration.class

		})
@EnableScheduling
public class CortellisUtilityToolsApplication extends SpringBootServletInitializer {
   public static void main(String[] args) {
		//SpringApplication.run(CortellisUtilityToolsApplication.class, args);
         new CortellisUtilityToolsApplication().configure(
                new SpringApplicationBuilder(CortellisUtilityToolsApplication.class)).run(args);
	}
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        application.sources(CortellisUtilityToolsApplication.class);
        return application;
    }
   }
