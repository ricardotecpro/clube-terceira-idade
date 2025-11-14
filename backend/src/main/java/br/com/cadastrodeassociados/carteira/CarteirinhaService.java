package br.com.cadastrodeassociados.carteira;

import br.com.cadastrodeassociados.associado.Associado;
import br.com.cadastrodeassociados.associado.AssociadoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class CarteirinhaService {

    private static final Logger log = LoggerFactory.getLogger(CarteirinhaService.class);

    @Autowired
    private AssociadoRepository associadoRepository;

    @Autowired
    private CarteirinhaRepository carteirinhaRepository;

    @Scheduled(cron = "0 0 2 * * ?")
    public void verificarValidadeCarteirinhas() {
        log.info("Iniciando verificação diária de validade das carteirinhas...");
        LocalDate hoje = LocalDate.now();
        LocalDate dataLimiteNotificacao = hoje.plusDays(30);

        List<Associado> todosAssociados = associadoRepository.findAll();

        for (Associado associado : todosAssociados) {
            Carteirinha carteirinha = associado.getCarteirinha();

            if (carteirinha == null) {
                log.info("Carteirinha ausente para o Associado ID {}", associado.getId());
                continue;
            }

            LocalDate validade = carteirinha.getDataValidade(); // ACESSO CORRIGIDO
            if (validade == null) continue;

            if (validade.isBefore(hoje)) {
                log.warn("CARTEIRINHA VENCIDA: Associado ID {}, Nome: {}", associado.getId(), associado.getNome());
            } else if (validade.isBefore(dataLimiteNotificacao)) {
                log.info("CARTEIRINHA PRÓXIMA DO VENCIMENTO: Associado ID {}, Nome: {}", associado.getId(), associado.getNome());
            }
        }
        log.info("Verificação de validade concluída.");
    }

    public void renovarCarteirinha(Long associadoId, LocalDate novaDataValidade) {
        Associado associado = associadoRepository.findById(associadoId)
                .orElseThrow(() -> new RuntimeException("Associado não encontrado!"));

        Carteirinha carteirinha = associado.getCarteirinha();

        if (carteirinha == null) {
            carteirinha = criarNovaCarteirinha(associado);
        }

        if (novaDataValidade.isBefore(LocalDate.now())) {
            throw new RuntimeException("A nova data de validade deve ser futura.");
        }

        carteirinha.setDataValidade(novaDataValidade);
        carteirinhaRepository.save(carteirinha);

        log.info("Carteirinha renovada para o Associado ID {} com validade até {}", associadoId, novaDataValidade);
    }

    public void gerenciarCriacaoCarteirinha(Associado associado) {
        if (associado.getId() == null || associado.getCarteirinha() == null) {
            criarNovaCarteirinha(associado);
        }
    }

    private Carteirinha criarNovaCarteirinha(Associado associado) {
        LocalDate hoje = LocalDate.now();

        Carteirinha novaCarteirinha = new Carteirinha();
        novaCarteirinha.setDataValidade(hoje.plusYears(1));
        novaCarteirinha.setAssociado(associado);
        novaCarteirinha.setNumeroCarteirinha(gerarNumeroCarteirinha(associado));

        associado.setCarteirinha(novaCarteirinha);

        return novaCarteirinha;
    }

    private String gerarNumeroCarteirinha(Associado associado) {
        String cpfBase = associado.getCpf() != null ? associado.getCpf().replaceAll("[^0-9]", "") : "00000000000";
        if (cpfBase.length() < 4) {
            cpfBase = String.format("%04d", 0);
        }
        return cpfBase.substring(0, 4) + "-" + LocalDate.now().getYear();
    }
}
