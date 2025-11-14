package br.com.cadastrodeassociados.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class BackupService {

    @Autowired
    private DataSource dataSource;

    public String performBackup() throws SQLException, IOException {
        String backupFileName = "backup_" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")) + ".sql";
        String backupDirectory = "backups"; // Diretório para armazenar backups

        File dir = new File(backupDirectory);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        String backupPath = backupDirectory + File.separator + backupFileName;

        // Lógica de backup específica para H2 (em memória ou arquivo)
        try (Connection conn = dataSource.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute("SCRIPT TO '" + backupPath + "'");
            return "Backup do H2 realizado com sucesso em: " + backupPath;
        }
    }
}
