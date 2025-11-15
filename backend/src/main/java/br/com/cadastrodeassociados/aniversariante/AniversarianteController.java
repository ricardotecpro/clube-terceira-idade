package br.com.cadastrodeassociados.aniversariante;

import br.com.cadastrodeassociados.associado.Associado;
import br.com.cadastrodeassociados.associado.AssociadoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.temporal.WeekFields;
import java.util.List;
import java.util.Locale;

@RestController
@RequestMapping("/api/aniversariantes")
public class AniversarianteController {

    @Autowired
    private AssociadoRepository associadoRepository;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'SECRETARIO')")
    public ResponseEntity<List<Associado>> getAniversariantes(@RequestParam(defaultValue = "dia") String periodo) {
        LocalDate hoje = LocalDate.now();
        List<Associado> aniversariantes;

        switch (periodo.toLowerCase()) {
            case "mes":
                aniversariantes = associadoRepository.findByMesAniversario(hoje.getMonthValue());
                break;
            case "semana":
                LocalDate inicioSemana = hoje.with(java.time.DayOfWeek.MONDAY);
                LocalDate fimSemana = hoje.with(java.time.DayOfWeek.SUNDAY);
                aniversariantes = associadoRepository.findBySemanaAniversario(inicioSemana.getDayOfYear(), fimSemana.getDayOfYear());
                break;
            case "dia":
            default:
                aniversariantes = associadoRepository.findByDiaAniversario(hoje.getDayOfMonth(), hoje.getMonthValue());
                break;
        }

        return ResponseEntity.ok(aniversariantes);
    }
}
