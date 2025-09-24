package br.com.fiap.mottomap.service;

import br.com.fiap.mottomap.model.Filial;
import br.com.fiap.mottomap.model.PosicaoPatio;
import br.com.fiap.mottomap.repository.MotoRepository;
import br.com.fiap.mottomap.repository.PosicaoPatioRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PatioService {

    private final PosicaoPatioRepository posicaoPatioRepository;
    private final MotoRepository motoRepository;

    public PatioService(PosicaoPatioRepository posicaoPatioRepository, MotoRepository motoRepository) {
        this.posicaoPatioRepository = posicaoPatioRepository;
        this.motoRepository = motoRepository;
    }

    public Map<Integer, Map<Integer, PosicaoPatio>> montarGradePatio(Filial filial) {
        List<PosicaoPatio> posicoes = posicaoPatioRepository.findByFilialOrderByNumeroLinhaAscNumeroColunaAsc(filial);
        Map<Integer, Map<Integer, PosicaoPatio>> grade = new HashMap<>();

        for (PosicaoPatio posicao : posicoes) {
            grade.computeIfAbsent(posicao.getNumeroLinha(), k -> new HashMap<>())
                    .put(posicao.getNumeroColuna(), posicao);
        }
        return grade;
    }

    public void ocuparPosicao(Long posicaoId, Long motoId) {
        var posicao = posicaoPatioRepository.findById(posicaoId)
                .orElseThrow(() -> new EntityNotFoundException("Posição não encontrada"));

        if (posicao.getOcupado()) {
            throw new IllegalStateException("Esta posição já está ocupada.");
        }

        var moto = motoRepository.findById(motoId)
                .orElseThrow(() -> new EntityNotFoundException("Moto não encontrada"));

        posicao.setOcupado(true);
        posicao.setMoto(moto);

        posicaoPatioRepository.save(posicao);
    }
}
