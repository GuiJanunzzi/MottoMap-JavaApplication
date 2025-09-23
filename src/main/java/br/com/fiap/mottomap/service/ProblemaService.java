package br.com.fiap.mottomap.service;

import br.com.fiap.mottomap.model.Problema;
import br.com.fiap.mottomap.model.Usuario;
import br.com.fiap.mottomap.repository.ProblemaRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class ProblemaService {

    private final ProblemaRepository problemaRepository;
    private final MotoService motoService;

    public ProblemaService(ProblemaRepository problemaRepository, MotoService motoService) {
        this.problemaRepository = problemaRepository;
        this.motoService = motoService;
    }

    public void registrarNovoProblema(Problema problema, Long motoId, UserDetails userDetails) {
        var moto = motoService.buscarPorId(motoId);

        //Converte os detalhes de segurança para a entidade Usuario
        Usuario usuarioLogado = (Usuario) userDetails;

        //Define os relacionamentos e valores padrão no objeto Problema
        problema.setMoto(moto);
        problema.setUsuario(usuarioLogado);
        problema.setDataRegistro(LocalDate.now());
        problema.setResolvido(false);

        //Salva o problema no banco de dados
        problemaRepository.save(problema);
    }

    public List<Problema> buscarPorMotoId(Long motoId) {
        return problemaRepository.findByMotoId(motoId);
    }

    public void marcarComoResolvido(Long problemaId) {
        //Busca o problema no banco
        Problema problema = problemaRepository.findById(problemaId)
                .orElseThrow(() -> new EntityNotFoundException("Problema não encontrado"));

        //Altera o status
        problema.setResolvido(true);

        //Salva a alteração
        problemaRepository.save(problema);
    }

    public void deletar(Long problemaId) {
        //Busca o problema
        Problema problema = problemaRepository.findById(problemaId)
                .orElseThrow(() -> new EntityNotFoundException("Problema não encontrado"));

        //Deleta o problema
        problemaRepository.delete(problema);
    }

    public Problema buscarPorId(Long id) {
        return problemaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Problema não encontrado"));
    }
}
