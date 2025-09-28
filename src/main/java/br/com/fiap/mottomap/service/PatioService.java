package br.com.fiap.mottomap.service;

import br.com.fiap.mottomap.model.Filial;
import br.com.fiap.mottomap.model.PosicaoPatio;
import br.com.fiap.mottomap.repository.PosicaoPatioRepository;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// Serviço focado na lógica de negócio relacionada à visualização do pátio.
@Service
public class PatioService {
    private final PosicaoPatioRepository posicaoPatioRepository;

    public PatioService(PosicaoPatioRepository posicaoPatioRepository) {
        this.posicaoPatioRepository = posicaoPatioRepository;
    }

    // Metodo principal para construir a representação visual do pátio.
    // Ele transforma a lista de posições do banco em uma estrutura de "grade" (matriz).
    public Map<Integer, Map<Integer, PosicaoPatio>> montarGradePatio(Filial filial) {
        // 1. Busca todas as posições da filial, já ordenadas por linha e coluna.
        List<PosicaoPatio> posicoes = posicaoPatioRepository.findByFilialOrderByNumeroLinhaAscNumeroColunaAsc(filial);

        // 2. Cria um Mapa de Mapas para simular a grade (ex: grade[linha][coluna]).
        Map<Integer, Map<Integer, PosicaoPatio>> grade = new HashMap<>();

        // 3. Itera sobre cada posição encontrada e a insere na coordenada correta da grade.
        for (PosicaoPatio posicao : posicoes) {
            grade.computeIfAbsent(posicao.getNumeroLinha(), k -> new HashMap<>())
                    .put(posicao.getNumeroColuna(), posicao);
        }

        // 4. Retorna a grade pronta para ser usada pelo Thymeleaf na renderização do mapa.
        return grade;
    }
}