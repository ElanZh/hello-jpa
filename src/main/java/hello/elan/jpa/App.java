package hello.elan.jpa;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing(auditorAwareRef = "entityAudit")
@Slf4j
public class App {

	public static void main(String[] args) {
		SpringApplication.run(App.class, args);
		log.warn("启动成功");
	}

}
