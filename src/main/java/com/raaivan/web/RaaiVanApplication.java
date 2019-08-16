package com.raaivan.web;

import com.raaivan.util.PublicConsts;
import com.raaivan.util.PublicMethods;
import com.raaivan.util.RVBeanFactory;
import com.raaivan.util.rvsettings.RaaiVanSettings;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
public class RaaiVanApplication {

	public static void main(String[] args) {
		ApplicationContext context = SpringApplication.run(RaaiVanApplication.class, args);

		RVBeanFactory.setApplicationContext(context);
		PublicConsts._setDependencies(RVBeanFactory.getBean(RaaiVanSettings.class));
	}

	@RestController
	@RequestMapping("/api")
	public class ApiController {
		@GetMapping("/gesi")
		public String get(){
			return "Gesi Chaghochi is the best :)";
		}
	}
}
