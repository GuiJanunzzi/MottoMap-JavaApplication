package br.com.fiap.mottomap.service;

import br.com.fiap.mottomap.model.Moto;
import br.com.fiap.mottomap.repository.MotoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

// Serviço que agrupa as regras de negócio relacionadas à entidade Moto.
@Service
public class MotoService {

    private final MotoRepository motoRepository;

    // Injeção de dependência do repositório de motos via construtor.
    public MotoService(MotoRepository motoRepository) {
        this.motoRepository = motoRepository;
    }

    // Retorna uma lista com todas as motos cadastradas.
    public List<Moto> buscarTodas() {
        return motoRepository.findAll();
    }

    // Busca uma moto específica pelo seu ID.
    // Se não encontrar, lança uma exceção para indicar o erro.
    public Moto buscarPorId(Long id) {
        return motoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Moto não encontrada com o ID: " + id));
    }

    // Salva uma nova moto ou atualiza os dados de uma moto existente.
    public void salvar(Moto moto) {
        motoRepository.save(moto);
    }

    // Deleta uma moto do banco de dados a partir do seu ID.
    public void deletarPorId(Long id) {
        // Usa o metodo buscarPorId para garantir que a moto existe antes de deletar.
        Moto motoParaDeletar = buscarPorId(id);
        motoRepository.delete(motoParaDeletar);
    }

    // Busca as motos de uma filial que ainda não foram alocadas em nenhuma posição no pátio.
    public List<Moto> buscarMotosSemPosicao(Long filialId) {
        return motoRepository.findMotosSemPosicaoNaFilial(filialId);
    }

    // Busca as motos de uma filial que têm problemas com status "não resolvido".
    public List<Moto> buscarMotosComProblemasNaoResolvidos(Long filialId) {
        return motoRepository.findMotosComProblemasNaoResolvidosNaFilial(filialId);
    }
}