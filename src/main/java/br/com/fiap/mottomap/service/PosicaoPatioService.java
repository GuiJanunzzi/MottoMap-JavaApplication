package br.com.fiap.mottomap.service;

import br.com.fiap.mottomap.model.PosicaoPatio;
import br.com.fiap.mottomap.repository.MotoRepository;
import br.com.fiap.mottomap.repository.PosicaoPatioRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PosicaoPatioService {
    private final PosicaoPatioRepository posicaoPatioRepository;
    private final MotoRepository motoRepository;

    public PosicaoPatioService(PosicaoPatioRepository posPatioRepo, MotoRepository motoRepo) {
        this.posicaoPatioRepository = posPatioRepo;
        this.motoRepository = motoRepo;
    }

    public List<PosicaoPatio> buscarTodas() {
        return posicaoPatioRepository.findAll();
    }

    public PosicaoPatio buscarPorId(Long id) {
        return posicaoPatioRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Posição não encontrada com o ID: " + id));
    }

    public void salvar(PosicaoPatio posicaoPatio) {
        // Garante que o status 'ocupado' esteja correto ao salvar
        posicaoPatio.setOcupado(posicaoPatio.getMoto() != null);
        posicaoPatioRepository.save(posicaoPatio);
    }

    public void deletarPorId(Long id) {
        PosicaoPatio posicao = buscarPorId(id);
        if (posicao.getOcupado()) {
            throw new IllegalStateException("Não é possível excluir uma posição que está ocupada.");
        }
        posicaoPatioRepository.delete(posicao);
    }

    public void ocuparPosicao(Long posicaoId, Long motoId) {
        var posicao = posicaoPatioRepository.findById(posicaoId).orElseThrow(() -> new EntityNotFoundException("Posição não encontrada"));
        if (posicao.getOcupado()) throw new IllegalStateException("Esta posição já está ocupada.");
        var moto = motoRepository.findById(motoId).orElseThrow(() -> new EntityNotFoundException("Moto não encontrada"));

        posicao.setOcupado(true);
        posicao.setMoto(moto);
        posicaoPatioRepository.save(posicao);
    }

    public void liberarPosicao(Long posicaoId) {
        var posicao = posicaoPatioRepository.findById(posicaoId).orElseThrow(() -> new EntityNotFoundException("Posição não encontrada"));

        posicao.setOcupado(false);
        posicao.setMoto(null); // Remove a associação com a moto
        posicaoPatioRepository.save(posicao);
    }

    public Optional<PosicaoPatio> buscarPorMotoId(Long motoId) {
        return posicaoPatioRepository.findByMotoId(motoId);
    }
}
