package br.com.fiap.mottomap.service;

import br.com.fiap.mottomap.model.Moto;
import br.com.fiap.mottomap.repository.MotoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MotoService {

    private final MotoRepository motoRepository;

    public MotoService(MotoRepository motoRepository) {
        this.motoRepository = motoRepository;
    }

    public List<Moto> buscarTodas() {
        return motoRepository.findAll();
    }

    public Moto buscarPorId(Long id) {
        // O orElseThrow é ótimo para garantir que a moto exista
        return motoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Moto não encontrada com o ID: " + id));
    }

    public void salvar(Moto moto) {
        motoRepository.save(moto);
    }

    public void deletarPorId(Long id) {
        // Primeiro, verificamos se a moto existe antes de deletar
        Moto motoParaDeletar = buscarPorId(id);
        motoRepository.delete(motoParaDeletar);
    }

    public List<Moto> buscarMotosSemPosicao(Long filialId) {
        return motoRepository.findMotosSemPosicaoNaFilial(filialId);
    }
}
