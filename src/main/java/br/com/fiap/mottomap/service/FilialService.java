package br.com.fiap.mottomap.service;

import br.com.fiap.mottomap.model.Filial;
import br.com.fiap.mottomap.repository.FilialRepository;
import br.com.fiap.mottomap.repository.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FilialService {

    private final FilialRepository filialRepository;
    private final UsuarioRepository usuarioRepository;

    public FilialService(FilialRepository filialRepository, UsuarioRepository usuarioRepository) {
        this.filialRepository = filialRepository;
        this.usuarioRepository = usuarioRepository;
    }

    public List<Filial> buscarTodas() {
        return filialRepository.findAll();
    }

    public Filial buscarPorId(Long id) {
        return filialRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Filial não encontrada com o ID: " + id));
    }

    public void salvar(Filial filial) {
        filialRepository.save(filial);
    }

    public void deletarPorId(Long id) {
        Filial filialParaDeletar = buscarPorId(id);

        //Verifica se existem usuarios associados a esta filial
        if (!usuarioRepository.findByFilialId(id).isEmpty()){
            //Se existir lança uma excecao
            throw new RuntimeException("Não é possível excluir a filial, pois existem usuários associados a ela.");
        }

        filialRepository.delete(filialParaDeletar);
    }
}
