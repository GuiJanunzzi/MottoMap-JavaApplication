package br.com.fiap.mottomap.service;

import br.com.fiap.mottomap.model.Filial;
import br.com.fiap.mottomap.repository.FilialRepository;
import br.com.fiap.mottomap.repository.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

// Serviço que contém a lógica de negócio para a entidade Filial.
@Service
public class FilialService {

    private final FilialRepository filialRepository;
    private final UsuarioRepository usuarioRepository;

    // Injeção de dependências via construtor
    public FilialService(FilialRepository filialRepository, UsuarioRepository usuarioRepository) {
        this.filialRepository = filialRepository;
        this.usuarioRepository = usuarioRepository;
    }

    // Retorna uma lista com todas as filiais cadastradas.
    public List<Filial> buscarTodas() {
        return filialRepository.findAll();
    }

    // Busca uma filial pelo seu ID. Lança uma exceção se não encontrar.
    public Filial buscarPorId(Long id) {
        return filialRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Filial não encontrada com o ID: " + id));
    }

    // Salva uma nova filial ou atualiza uma existente.
    public void salvar(Filial filial) {
        filialRepository.save(filial);
    }

    // Deleta uma filial, mas antes faz uma verificação de segurança.
    public void deletarPorId(Long id) {
        // Garante que a filial existe antes de tentar deletar.
        Filial filialParaDeletar = buscarPorId(id);

        // Regra de negócio: não permite excluir uma filial se ela tiver usuários vinculados.
        if (!usuarioRepository.findByFilialId(id).isEmpty()){
            // Se a verificação encontrar usuários, lança um erro com uma mensagem clara.
            throw new RuntimeException("Não é possível excluir a filial, pois existem usuários associados a ela.");
        }

        // Se a verificação passar, deleta a filial.
        filialRepository.delete(filialParaDeletar);
    }
}