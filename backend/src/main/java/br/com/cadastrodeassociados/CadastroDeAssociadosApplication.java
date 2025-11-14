package br.com.cadastrodeassociados;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling; // Importar

@SpringBootApplication
@EnableScheduling // Habilitar agendamento de tarefas
public class CadastroDeAssociadosApplication {

	public static void main(String[] args) {
		SpringApplication.run(CadastroDeAssociadosApplication.class, args);
	}

}
