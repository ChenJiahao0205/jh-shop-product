package pers.jhshop.product;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableSwagger2
@MapperScan(basePackages = "pers.jhshop.product.mapper")
@SpringBootApplication
public class JhShopProductServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(JhShopProductServiceApplication.class, args);
    }

}
