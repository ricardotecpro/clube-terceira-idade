package br.com.cadastrodeassociados.mensagens;

import br.com.cadastrodeassociados.associado.Associado;
import br.com.cadastrodeassociados.associado.AssociadoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/mensagens")
public class MensagemControler {

    @Autowired
    private MensagemRepository mensagemRepository;

    @Autowired
    private AssociadoRepository associadoRepository;

    @GetMapping
    public String listarMensagens(Model model) {
        model.addAttribute("mensagens", mensagemRepository.findAll());
        return "mensagens/historico";
    }

    @GetMapping("/nova")
    public String mostrarFormulario(Model model) {
        List<Associado> associados = associadoRepository.findAll();
        model.addAttribute("associados", associados);
        model.addAttribute("mensagem", new Mensagem());
        return "mensagens/formulario";
    }

    @PostMapping("/enviar")
    public String enviarMensagem(@ModelAttribute("mensagem") Mensagem mensagem,
                                 @RequestParam(required = false) List<Long> associadoIds,
                                 RedirectAttributes ra) {

        System.out.println("--- ENVIANDO MENSAGEM ---");
        System.out.println("Título: " + mensagem.getTitulo());
        System.out.println("Conteúdo: " + mensagem.getConteudo());

        List<Associado> destinatarios;
        if (associadoIds == null || associadoIds.isEmpty()) {
            destinatarios = associadoRepository.findAll(); // Get all associates
            System.out.println("Destinatários: Todos");
        } else {
            destinatarios = associadoRepository.findAllById(associadoIds); // Get specific associates by IDs
            System.out.println("Destinatários: " + destinatarios.stream().map(Associado::getNome).toList());
        }
        mensagem.setDestinatarios(destinatarios);

        mensagemRepository.save(mensagem);
        ra.addFlashAttribute("mensagem", "Mensagem enviada com sucesso!");
        return "redirect:/mensagens";
    }
}
