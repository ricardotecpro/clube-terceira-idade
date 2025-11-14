package br.com.cadastrodeassociados.documento;

import br.com.cadastrodeassociados.associado.Associado;
import br.com.cadastrodeassociados.associado.AssociadoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files; // Adicionado
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/documentos")
public class DocumentoController {

    @Autowired
    private DocumentoRepository documentoRepository;

    @Autowired
    private AssociadoRepository associadoRepository;

    @Autowired
    private FileStorageService fileStorageService;

    @PostMapping("/upload/{associadoId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'SECRETARIO')")
    public ResponseEntity<String> uploadDocumento(
            @PathVariable Long associadoId,
            @RequestParam("file") MultipartFile file) {
        try {
            Associado associado = associadoRepository.findById(associadoId)
                    .orElseThrow(() -> new RuntimeException("Associado não encontrado!"));

            String fileName = fileStorageService.storeFile(file);
            String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path("/api/documentos/download/")
                    .path(fileName)
                    .toUriString();

            Documento documento = new Documento();
            documento.setNomeArquivo(file.getOriginalFilename());
            documento.setTipoConteudo(file.getContentType());
            documento.setCaminhoArmazenamento(fileName); // Salva o nome gerado pelo FileStorageService
            documento.setDataUpload(LocalDateTime.now());
            documento.setAssociado(associado);

            documentoRepository.save(documento);

            return ResponseEntity.status(HttpStatus.CREATED).body("Upload realizado com sucesso! URL: " + fileDownloadUri);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao realizar upload: " + e.getMessage());
        }
    }

    @GetMapping("/associado/{associadoId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'SECRETARIO')")
    public ResponseEntity<List<Documento>> listarDocumentosPorAssociado(@PathVariable Long associadoId) {
        associadoRepository.findById(associadoId)
                .orElseThrow(() -> new RuntimeException("Associado não encontrado!"));
        List<Documento> documentos = documentoRepository.findByAssociadoId(associadoId);
        return ResponseEntity.ok(documentos);
    }

    @GetMapping("/download/{fileName}")
    @PreAuthorize("hasAnyRole('ADMIN', 'SECRETARIO')")
    public ResponseEntity<Resource> downloadDocumento(@PathVariable String fileName) {
        try {
            Path filePath = fileStorageService.loadFileAsResource(fileName);
            Resource resource = new UrlResource(filePath.toUri());

            if (resource.exists()) {
                // Tenta determinar o tipo de conteúdo do arquivo
                String contentType = Files.probeContentType(filePath);
                if (contentType == null) {
                    contentType = "application/octet-stream"; // Fallback
                }

                return ResponseEntity.ok()
                        .contentType(MediaType.parseMediaType(contentType))
                        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                        .body(resource);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (MalformedURLException e) {
            return ResponseEntity.badRequest().build();
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{documentoId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteDocumento(@PathVariable Long documentoId) {
        return documentoRepository.findById(documentoId)
                .map(documento -> {
                    try {
                        fileStorageService.deleteFile(documento.getCaminhoArmazenamento());
                        documentoRepository.delete(documento);
                        return ResponseEntity.noContent().<Void>build(); // Especificar <Void>
                    } catch (IOException e) {
                        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).<Void>build(); // Especificar <Void>
                    }
                })
                .orElse(ResponseEntity.notFound().<Void>build()); // Especificar <Void>
    }
}
