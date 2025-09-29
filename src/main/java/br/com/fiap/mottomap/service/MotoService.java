package br.com.fiap.mottomap.service;

import br.com.fiap.mottomap.model.Moto;
import br.com.fiap.mottomap.repository.MotoRepository;
import br.com.fiap.mottomap.repository.PosicaoPatioRepository;
import br.com.fiap.mottomap.repository.ProblemaRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

// Serviço que agrupa as regras de negócio relacionadas à entidade Moto.
@Service
public class MotoService {

    private final MotoRepository motoRepository;
    private final ProblemaRepository problemaRepository;     // ADICIONAR
    private final PosicaoPatioRepository posicaoPatioRepository;

    // Injeção de dependência do repositório de motos via construtor.
    public MotoService(MotoRepository motoRepository, ProblemaRepository problemaRepository, PosicaoPatioRepository posicaoPatioRepository) {
        this.motoRepository = motoRepository;
        this.problemaRepository = problemaRepository;
        this.posicaoPatioRepository = posicaoPatioRepository;
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
        // Busca no banco se já existe uma moto com a mesma placa.
        Optional<Moto> motoExistente = motoRepository.findByPlaca(moto.getPlaca());

        // Se encontrou uma moto, verifica se não é a mesma moto que estamos editando.
        if (motoExistente.isPresent() && !motoExistente.get().getId().equals(moto.getId())) {
            // Se for uma moto diferente, lança um erro com uma mensagem clara.
            throw new IllegalStateException("A placa '" + moto.getPlaca() + "' já está cadastrada no sistema.");
        }

        // Se encontrou uma moto, verifica se não é a mesma moto que estamos editando.
        Optional<Moto> motoPorChassi = motoRepository.findByChassi(moto.getChassi());
        if (motoPorChassi.isPresent() && !motoPorChassi.get().getId().equals(moto.getId())) {
            // Se for uma moto diferente, lança um erro com uma mensagem clara.
            throw new IllegalStateException("O chassi '" + moto.getChassi() + "' já está cadastrado no sistema.");
        }

        // Se a placa for única salva normalmente.
        motoRepository.save(moto);
    }

    // Deleta uma moto do banco de dados a partir do seu ID.
    public void deletarPorId(Long id) {
        // Garante que a moto realmente existe no banco.
        Moto motoParaDeletar = buscarPorId(id);

        // Verifica se a moto está alocada em uma posição no pátio.
        if (posicaoPatioRepository.findByMotoId(id).isPresent()) {
            // Se estiver, a exclusão é bloqueada para não deixar a vaga inconsistente.
            throw new IllegalStateException("Não é possível excluir a moto, pois ela está alocada em uma posição no pátio.");
        }

        // Verifica se a moto possui um histórico de problemas registrados.
        if (!problemaRepository.findByMotoId(id).isEmpty()) {
            // Se possuir, a exclusão também é bloqueada para não perder o histórico de manutenções.
            throw new IllegalStateException("Não é possível excluir a moto, pois ela possui registros de problemas associados.");
        }

        // Se todas as verificações passarem, a moto pode ser deletada com segurança.
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