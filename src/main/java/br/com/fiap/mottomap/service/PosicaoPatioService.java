package br.com.fiap.mottomap.service;

import br.com.fiap.mottomap.repository.MotoRepository;
import br.com.fiap.mottomap.repository.PosicaoPatioRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class PosicaoPatioService {
    private final PosicaoPatioRepository posicaoPatioRepository;
    private final MotoRepository motoRepository;

    public PosicaoPatioService(PosicaoPatioRepository posPatioRepo, MotoRepository motoRepo) {
        this.posicaoPatioRepository = posPatioRepo;
        this.motoRepository = motoRepo;
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
}
