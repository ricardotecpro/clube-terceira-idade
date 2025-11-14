package br.com.cadastrodeassociados.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

@Service
public class RestoreService {

    @Autowired
    private DataSource dataSource;

    public String performRestore(String backupFileName) throws SQLException, IOException {
        String backupDirectory = "backups";
        String backupPath = backupDirectory + File.separator + backupFileName;

        File backupFile = new File(backupPath);
        if (!backupFile.exists()) {
            throw new IOException("Arquivo de backup não encontrado: " + backupPath);
        }

        // Lógica de restauração específica para H2
        try (Connection conn = dataSource.getConnection();
             Statement stmt = conn.createStatement()) {
            // Para H2, é comum dropar todas as tabelas e depois executar o script
            // Isso é perigoso em produção, mas aceitável para H2 em dev
            stmt.execute("DROP ALL OBJECTS;"); // Limpa o banco de dados
            stmt.execute("RUNSCRIPT FROM '" + backupPath + "'"); // Executa o script de backup
            return "Restauração do H2 realizada com sucesso a partir de: " + backupPath;
        }
    }
}
