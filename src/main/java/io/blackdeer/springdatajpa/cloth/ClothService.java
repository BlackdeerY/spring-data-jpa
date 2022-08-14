package io.blackdeer.springdatajpa.cloth;

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
public class ClothService {

    private static final Logger logger = LoggerFactory.getLogger(ClothService.class);

    private final ClothRepository clothRepository;

    @Autowired
    public ClothService(ClothRepository clothRepository) {
        this.clothRepository = clothRepository;
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public Cloth getClothOrNullById(@NonNull Long id) {
        assert (id != null);
        Optional<Cloth> clothOptional = clothRepository.findById(id);
        if (clothOptional.isPresent()) {
            return clothOptional.get();
        } else {
            logger.error(String.format("id:%d인 Cloth가 없습니다!", id));
            return null;
        }
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public List<Cloth> getAllCloths() {
        return clothRepository.findAll();
    }

    @Transactional(propagation = Propagation.MANDATORY)
    public Cloth saveCloth(@NonNull Cloth cloth) {
        assert (cloth != null);
        return clothRepository.save(cloth);
    }

    @Transactional(propagation = Propagation.MANDATORY)
    public List<Cloth> saveCloths(@NonNull Iterable<Cloth> cloths) {
        assert (cloths != null);
        return clothRepository.saveAll(cloths);
    }

    @Transactional(propagation = Propagation.MANDATORY)
    public void deleteCloth(@NonNull Cloth cloth) {
        assert (cloth != null);
        clothRepository.delete(cloth);
    }
}
