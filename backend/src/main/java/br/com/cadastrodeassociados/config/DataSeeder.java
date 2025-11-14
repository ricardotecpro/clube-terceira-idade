package br.com.cadastrodeassociados.config;

import br.com.cadastrodeassociados.associado.Associado;
import br.com.cadastrodeassociados.associado.AssociadoRepository;
import br.com.cadastrodeassociados.cidade.Cidade; // Importar Cidade
import br.com.cadastrodeassociados.cidade.CidadeRepository; // Importar CidadeRepository
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate; // Importar LocalDate
import java.time.format.DateTimeFormatter; // Importar DateTimeFormatter

@Configuration
public class DataSeeder {

    @Bean
    public CommandLineRunner initDatabase(AssociadoRepository associadoRepository, CidadeRepository cidadeRepository) { // Injetar CidadeRepository
        return args -> {
            // Verifica se as cidades já existem para evitar duplicação
            if (cidadeRepository.count() == 0) {
                System.out.println("Populando cidades...");
                Cidade sp = new Cidade(null, 12345000, "São Paulo", "SP"); // CEP adicionado
                Cidade rj = new Cidade(null, 23456000, "Rio de Janeiro", "RJ"); // CEP adicionado
                cidadeRepository.save(sp);
                cidadeRepository.save(rj);
                System.out.println("Cidades populadas.");
            }

            if (associadoRepository.count() > 0) {
                return; // O banco de dados já está populado com associados
            }

            System.out.println("Populando o banco de dados com dados de teste de associados...");

            // Formato da data para parsing
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

            // Recupera as cidades criadas
            Cidade sp = cidadeRepository.findByNome("São Paulo").orElseThrow(() -> new RuntimeException("Cidade SP não encontrada!"));
            Cidade rj = cidadeRepository.findByNome("Rio de Janeiro").orElseThrow(() -> new RuntimeException("Cidade RJ não encontrada!"));


            Associado associado1 = new Associado();
            associado1.setNome("João da Silva");
            associado1.setDataNascimento(LocalDate.parse("15/05/1950", formatter)); // Convertido para LocalDate
            associado1.setRg("11.222.333-4");
            associado1.setCpf("123.456.789-00");
            associado1.setEndereco("Rua das Flores, 123");
            associado1.setTelefone("18999998888");
            associado1.setEscolaridade("Ensino Médio Completo");
            associado1.setEmail("joao.silva@example.com");
            associado1.setCidade(sp); // Atribuir cidade
            associado1.setSituacao("Adimplente"); // Adicionar situação

            Associado associado2 = new Associado();
            associado2.setNome("Maria Oliveira");
            associado2.setDataNascimento(LocalDate.parse("22/08/1962", formatter)); // Convertido para LocalDate
            associado2.setRg("22.333.444-5");
            associado2.setCpf("987.654.321-00");
            associado2.setEndereco("Av. Rui Barbosa, 456");
            associado2.setTelefone("18988887777");
            associado2.setEscolaridade("Ensino Fundamental");
            associado2.setEmail("maria.oliveira@example.com");
            associado2.setCidade(rj); // Atribuir cidade
            associado2.setSituacao("Inadimplente"); // Adicionar situação

            associadoRepository.save(associado1);
            associadoRepository.save(associado2);

            System.out.println("Banco de dados populado com sucesso!");
        };
    }
}
