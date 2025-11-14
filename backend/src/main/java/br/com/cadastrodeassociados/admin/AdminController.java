package br.com.cadastrodeassociados.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*; // Importar RequestParam

import java.io.IOException;
import java.sql.SQLException;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private BackupService backupService;

    @Autowired
    private RestoreService restoreService; // Injetar RestoreService

    @PostMapping("/backup")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> backupData() {
        try {
            String result = backupService.performBackup();
            return ResponseEntity.ok(result);
        } catch (SQLException | IOException e) {
            return ResponseEntity.internalServerError().body("Erro ao realizar backup: " + e.getMessage());
        }
    }

    @PostMapping("/restore")
    @PreAuthorize("hasRole('ADMIN')") // Apenas ADMIN pode realizar restore
    public ResponseEntity<String> restoreData(@RequestParam String backupFileName) {
        try {
            String result = restoreService.performRestore(backupFileName);
            return ResponseEntity.ok(result);
        } catch (SQLException | IOException e) {
            return ResponseEntity.internalServerError().body("Erro ao realizar restauração: " + e.getMessage());
        }
    }
}
