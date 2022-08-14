package io.blackdeer.springdatajpa.house;

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
public class HouseService {

    private static final Logger logger = LoggerFactory.getLogger(HouseService.class);

    private final HouseRepository houseRepository;

    @Autowired
    public HouseService(HouseRepository houseRepository) {
        this.houseRepository = houseRepository;
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public House getHouseOrNullById(@NonNull Long id) {
        assert (id != null);
        Optional<House> houseOptional = houseRepository.findById(id);
        if (houseOptional.isPresent()) {
            return houseOptional.get();
        } else {
            logger.error(String.format("id:%d인 House가 없습니다!", id));
            return null;
        }
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public List<House> getAllHouses() {
        return houseRepository.findAll();
    }

    @Transactional(propagation = Propagation.MANDATORY)
    public House saveHouse(@NonNull House house) {
        assert (house != null);
        return houseRepository.save(house);
    }

    @Transactional(propagation = Propagation.MANDATORY)
    public List<House> saveHouses(@NonNull Iterable<House> houses) {
        assert (houses != null);
        return houseRepository.saveAll(houses);
    }

    @Transactional(propagation = Propagation.MANDATORY)
    public void deleteHouse(@NonNull House house) {
        assert (house != null);
        houseRepository.delete(house);
    }
}
