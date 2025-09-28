package br.com.fiap.mottomap.service;

import br.com.fiap.mottomap.model.Problema;
import br.com.fiap.mottomap.model.Usuario;
import br.com.fiap.mottomap.repository.ProblemaRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

// Serviço para a lógica de negócio relacionada a Problemas.
@Service
public class ProblemaService {

    private final ProblemaRepository problemaRepository;
    private final MotoService motoService;

    public ProblemaService(ProblemaRepository problemaRepository, MotoService motoService) {
        this.problemaRepository = problemaRepository;
        this.motoService = motoService;
    }

    // Orquestra a criação de um novo registro de problema.
    public void registrarNovoProblema(Problema problema, Long motoId, UserDetails userDetails) {
        // Busca a moto que terá o problema associado.
        var moto = motoService.buscarPorId(motoId);

        // Converte o UserDetails do Spring Security para a nossa entidade Usuario.
        Usuario usuarioLogado = (Usuario) userDetails;

        // Define os relacionamentos e valores padrão do problema.
        problema.setMoto(moto);
        problema.setUsuario(usuarioLogado);
        problema.setDataRegistro(LocalDate.now());
        problema.setResolvido(false); // Todo novo problema começa como não resolvido.

        // Salva o problema no banco de dados.
        problemaRepository.save(problema);
    }

    // Busca a lista de todos os problemas de uma moto específica.
    public List<Problema> buscarPorMotoId(Long motoId) {
        return problemaRepository.findByMotoId(motoId);
    }

    // Altera o status de um problema para resolvido.
    public void marcarComoResolvido(Long problemaId) {
        // Busca o problema no banco.
        Problema problema = buscarPorId(problemaId);

        // Altera o status.
        problema.setResolvido(true);

        // Salva a alteração.
        problemaRepository.save(problema);
    }

    // Deleta o registro de um problema.
    public void deletar(Long problemaId) {
        // Garante que o problema existe antes de deletar.
        Problema problema = buscarPorId(problemaId);
        problemaRepository.delete(problema);
    }

    // Busca um problema pelo seu ID, lançando erro se não encontrar.
    public Problema buscarPorId(Long id) {
        return problemaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Problema não encontrado"));
    }
}