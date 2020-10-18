package jpabook.jpashop;

import org.springframework.context.annotation.Bean;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

public class Swagger2Config {

    @Bean
    public Docket api() {
        return new Docket(
                DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("jpabook.jpashop"))
                .paths(PathSelectors.any())
                .build();
    }


}
