package br.com.fiap.mottomap.service;

import br.com.fiap.mottomap.model.PosicaoPatio;
import br.com.fiap.mottomap.repository.MotoRepository;
import br.com.fiap.mottomap.repository.PosicaoPatioRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

// Serviço que centraliza as regras de negócio para a entidade PosicaoPatio.
@Service
public class PosicaoPatioService {
    private final PosicaoPatioRepository posicaoPatioRepository;
    private final MotoRepository motoRepository;

    public PosicaoPatioService(PosicaoPatioRepository posPatioRepo, MotoRepository motoRepo) {
        this.posicaoPatioRepository = posPatioRepo;
        this.motoRepository = motoRepo;
    }

    // Retorna todas as posições cadastradas no pátio.
    public List<PosicaoPatio> buscarTodas() {
        return posicaoPatioRepository.findAll();
    }

    // Busca uma posição específica pelo ID, lançando erro se não encontrar.
    public PosicaoPatio buscarPorId(Long id) {
        return posicaoPatioRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Posição não encontrada com o ID: " + id));
    }

    // Salva ou atualiza uma posição.
    public void salvar(PosicaoPatio posicaoPatio) {
        // garante que o status 'ocupado' sempre reflita se há uma moto na vaga.
        posicaoPatio.setOcupado(posicaoPatio.getMoto() != null);
        posicaoPatioRepository.save(posicaoPatio);
    }

    // Deleta uma posição do pátio.
    public void deletarPorId(Long id) {
        PosicaoPatio posicao = buscarPorId(id);
        // impede a exclusão de uma vaga que esteja ocupada.
        if (posicao.getOcupado()) {
            throw new IllegalStateException("Não é possível excluir uma posição que está ocupada.");
        }
        posicaoPatioRepository.delete(posicao);
    }

    // Lógica para alocar uma moto em uma posição vaga.
    public void ocuparPosicao(Long posicaoId, Long motoId) {
        // Busca a posição e a moto pelos seus IDs.
        var posicao = buscarPorId(posicaoId);
        // Validação para garantir que a posição não seja ocupada duas vezes.
        if (posicao.getOcupado()) throw new IllegalStateException("Esta posição já está ocupada.");
        var moto = motoRepository.findById(motoId).orElseThrow(() -> new EntityNotFoundException("Moto não encontrada"));

        // Atualiza o estado da posição.
        posicao.setOcupado(true);
        posicao.setMoto(moto);
        posicaoPatioRepository.save(posicao);
    }

    // Lógica para liberar uma posição que está ocupada.
    public void liberarPosicao(Long posicaoId) {
        var posicao = buscarPorId(posicaoId);

        // Reseta o estado da posição para "vazio".
        posicao.setOcupado(false);
        posicao.setMoto(null); // Remove a associação com a moto.
        posicaoPatioRepository.save(posicao);
    }

    // Busca uma posição a partir da moto que a ocupa.
    public Optional<PosicaoPatio> buscarPorMotoId(Long motoId) {
        return posicaoPatioRepository.findByMotoId(motoId);
    }
}