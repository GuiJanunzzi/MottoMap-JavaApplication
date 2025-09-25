package br.com.fiap.mottomap.service;

import br.com.fiap.mottomap.model.Filial;
import br.com.fiap.mottomap.model.PosicaoPatio;
import br.com.fiap.mottomap.repository.PosicaoPatioRepository;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PatioService {
    private final PosicaoPatioRepository posicaoPatioRepository;

    public PatioService(PosicaoPatioRepository posicaoPatioRepository) {
        this.posicaoPatioRepository = posicaoPatioRepository;
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
}
