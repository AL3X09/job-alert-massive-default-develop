package co.com.ath.alert.massive.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;

import java.util.List;

/*
 * clase encargada de la configuración de Swagger
 */
@Configuration
public class SwaggerConfig {

	@Bean
	public OpenAPI customOpenAPI() {

		Server server = new Server();
		server.setUrl("/"); // URL relativa, el navegador usará el host y puerto actual

		return new OpenAPI()
				.info(new Info()
						.title("API de Alertas Móviles – Creación o Inactivación Alertas Masivas")
						.version("AM-330")
						.description(
								"Documentación de servicios de Alertas Móviles – Alerta Masivas e Inactivación Masivas"))
				.servers(List.of(server));
	}

}
