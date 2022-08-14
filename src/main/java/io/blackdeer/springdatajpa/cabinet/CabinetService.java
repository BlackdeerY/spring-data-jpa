package io.blackdeer.springdatajpa.cabinet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class CabinetService {

    private static final Logger logger = LoggerFactory.getLogger(CabinetService.class);

    private final CabinetRepository cabinetRepository;

    @Autowired
    public CabinetService(CabinetRepository cabinetRepository) {
        this.cabinetRepository = cabinetRepository;
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public Cabinet getCabinetOrNullById(@NonNull Long id) {
        assert (id != null);
        Optional<Cabinet> cabinetOptional = cabinetRepository.findById(id);
        if (cabinetOptional.isPresent()) {
            return cabinetOptional.get();
        } else {
            logger.error(String.format("id:%d인 Cabinet이 없습니다!", id));
            return null;
        }
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public List<Cabinet> getAllCabinets() {
        return cabinetRepository.findAll();
    }

    @Transactional(propagation = Propagation.MANDATORY)
    public Cabinet saveCabinet(@NonNull Cabinet cabinet) {
        assert (cabinet != null);
        return cabinetRepository.save(cabinet);
    }

    @Transactional(propagation = Propagation.MANDATORY)
    public List<Cabinet> saveCabinets(@NonNull Iterable<Cabinet> cabinets) {
        assert (cabinets != null);
        return cabinetRepository.saveAll(cabinets);
    }

    @Transactional(propagation = Propagation.MANDATORY)
    public void deleteCabinet(@NonNull Cabinet cabinet) {
        assert (cabinet != null);
        cabinetRepository.delete(cabinet);
    }
}
