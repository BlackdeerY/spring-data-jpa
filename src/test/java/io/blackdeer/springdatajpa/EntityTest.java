package io.blackdeer.springdatajpa;

import io.blackdeer.springdatajpa.cabinet.Cabinet;
import io.blackdeer.springdatajpa.cabinet.CabinetService;
import io.blackdeer.springdatajpa.cloth.Cloth;
import io.blackdeer.springdatajpa.cloth.ClothService;
import io.blackdeer.springdatajpa.cloth.ClothType;
import io.blackdeer.springdatajpa.house.House;
import io.blackdeer.springdatajpa.house.HouseService;
import io.blackdeer.springdatajpa.person.Gender;
import io.blackdeer.springdatajpa.person.Person;
import io.blackdeer.springdatajpa.person.PersonService;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

@ExtendWith({SpringExtension.class})
@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Rollback(value = false)
public class EntityTest {

    private static final Logger logger = LoggerFactory.getLogger(EntityTest.class);

    @Autowired
    private PersonService personService;

    @Autowired
    private HouseService houseService;

    @Autowired
    private ClothService clothService;

    @Autowired
    private CabinetService cabinetService;

    @BeforeAll
    public static void beforeAll() {
    }

    @AfterAll
    public static void afterAll() {
    }

    @BeforeEach
    @Transactional
    public void beforeEach() {
    }

    @AfterEach
    @Transactional
    public void afterEach() {
    }

    @Test
    @Order(1)
    @DisplayName("삽입 테스트")
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void insertTest() {
        Person savePerson = null;
        House saveHouse = null;
        Cabinet saveCabinet = null;
        Cloth saveCloth = null;

        Person person01 = new Person("김민수", Gender.MALE, 21, Date.from(OffsetDateTime.of(2003, 6, 12, 7, 30, 0, 0, ZoneOffset.ofHours(9)).toInstant()), "사람01", "DB에 올라가지 않을 휘발성 정보 01");
        Person person02 = new Person("박영희", Gender.FEMALE,20, Date.from(OffsetDateTime.of(2002, 7, 14, 14, 20, 0, 0, ZoneOffset.ofHours(9)).toInstant()), "사람02", "DB에 올라가지 않을 휘발성 정보 02");
        Person person03 = new Person("이영호", Gender.MALE, 20, Date.from(OffsetDateTime.of(2002, 4, 8, 11, 0, 0, 0, ZoneOffset.ofHours(9)).toInstant()), "사람03", "DB에 올라가지 않을 휘발성 정보 03");
        Person person04 = new Person("윤수아", Gender.FEMALE, 20, Date.from(OffsetDateTime.of(2002, 8, 21, 10, 10, 0, 0, ZoneOffset.ofHours(9)).toInstant()), "사람04", "DB에 올라가지 않을 휘발성 정보 04");
        Person person05 = new Person("홍길동", Gender.MALE, 20, Date.from(OffsetDateTime.of(2002, 10, 7, 4, 5, 0, 0, ZoneOffset.ofHours(9)).toInstant()), "사람05", "DB에 올라가지 않을 휘발성 정보 06");
        Person person06 = new Person("김혁우", Gender.MALE, 20, Date.from(OffsetDateTime.of(2002, 5, 6, 11, 40, 0, 0, ZoneOffset.ofHours(9)).toInstant()), "사람06", "DB에 올라가지 않을 휘발성 정보 06");
//        savePerson = personService.savePerson(person01);
//        assert (savePerson.equals(person01));
//        savePerson = personService.savePerson(person02);
//        assert (savePerson.equals(person02));
//        savePerson = personService.savePerson(person03);
//        assert (savePerson.equals(person03));
//        savePerson = personService.savePerson(person04);
//        assert (savePerson.equals(person04));
//        savePerson = personService.savePerson(person05);
//        assert (savePerson.equals(person05));
//        savePerson = personService.savePerson(person06);
//        assert (savePerson.equals(person06));

        House house01 = new House("우정빌라", "집주소1");
        House house02 = new House("칠성아파트", null);
//        saveHouse = houseService.saveHouse(house01);
//        assert (saveHouse.equals(house01));
//        saveHouse = houseService.saveHouse(house02);
//        assert (saveHouse.equals(house02));

        house01.getPersons().add(person01);
        house01.getPersons().add(person02);
        house01.getPersons().add(person03);
        house02.getPersons().add(person04);
        house02.getPersons().add(person05);
        house02.getPersons().add(person06);
        person01.getHouses().add(house01);
        person02.getHouses().add(house01);
        person03.getHouses().add(house01);
        person04.getHouses().add(house02);
        person05.getHouses().add(house02);
        person06.getHouses().add(house02);
        savePerson = personService.savePerson(person01);
        assert (savePerson.equals(person01));
        savePerson = personService.savePerson(person02);
        assert (savePerson.equals(person02));
        savePerson = personService.savePerson(person03);
        assert (savePerson.equals(person03));
        savePerson = personService.savePerson(person04);
        assert (savePerson.equals(person04));
        savePerson = personService.savePerson(person05);
        assert (savePerson.equals(person05));
        savePerson = personService.savePerson(person06);
        assert (savePerson.equals(person06));

        /*
            Person만 저장해도,
            Person의 멤버변수 houses에 cascade = CASCADE.ALL 로 인해 영속성 전이로,
            인스턴스와 연관된 house들도 함께 저장됨.
         */

        Cabinet cabinet01 = new Cabinet("캐비닛01", house01);
        Cabinet cabinet02 = new Cabinet("캐비닛02", house01);
        Cabinet cabinet03 = new Cabinet("캐비닛03", house02);
        Cabinet cabinet04 = new Cabinet("캐비닛04", house02);
        house01.getCabinets().add(cabinet01);
        house01.getCabinets().add(cabinet02);
        house02.getCabinets().add(cabinet03);
        house02.getCabinets().add(cabinet04);
        saveCabinet = cabinetService.saveCabinet(cabinet01);
        assert (saveCabinet.equals(cabinet01));
        saveCabinet = cabinetService.saveCabinet(cabinet02);
        assert (saveCabinet.equals(cabinet02));
        saveCabinet = cabinetService.saveCabinet(cabinet03);
        assert (saveCabinet.equals(cabinet03));
        saveCabinet = cabinetService.saveCabinet(cabinet04);
        assert (saveCabinet.equals(cabinet04));

        Cloth cloth01 = new Cloth(ClothType.SHIRT, "흰 셔츠", cabinet01);
        Cloth cloth02 = new Cloth(ClothType.SHIRT, "검은 셔츠", cabinet01);
        Cloth cloth03 = new Cloth(ClothType.PANTS, "청바지", null);
        Cloth cloth04 = new Cloth(ClothType.JACKET, "양모재킷", cabinet02);
        cabinet01.getCloths().add(cloth01);
        cabinet01.getCloths().add(cloth02);
        cabinet02.getCloths().add(cloth04);

        List<Cloth> clothList = new ArrayList<>(4);
        clothList.add(cloth01);
        clothList.add(cloth02);
        clothList.add(cloth03);
        clothList.add(cloth04);
        List<Cloth> saveCloths = clothService.saveCloths(clothList);
        assert (saveCloths.equals(clothList));

        /*
            사실 JPARepository.save(Entity)는 즉시 DB에 insert/update하는 게 아니고,
            instance를 Persistent 상태로 관리하다가 필요할 때 DB로 쿼리한다.
         */
    }

    private void selectTest() {
        List<Person> personList = personService.getAllPersons();
        assert (personList.size() == 6);
        List<House> houseList = houseService.getAllHouses();
        assert (houseList.size() == 2);
        List<Cabinet> cabinetList = cabinetService.getAllCabinets();
        assert (cabinetList.size() == 4);
        List<Cloth> clothList = clothService.getAllCloths();
        assert (clothList.size() == 4);

        assert (personList.get(0).getHouses().contains(houseList.get(0)));
        assert (personList.get(1).getHouses().contains(houseList.get(0)));
        assert (personList.get(2).getHouses().contains(houseList.get(0)));
        assert (personList.get(3).getHouses().contains(houseList.get(1)));
        assert (personList.get(4).getHouses().contains(houseList.get(1)));
        assert (personList.get(5).getHouses().contains(houseList.get(1)));

        assert (houseList.get(0).getPersons().contains(personList.get(0)));
        assert (houseList.get(0).getPersons().contains(personList.get(1)));
        assert (houseList.get(0).getPersons().contains(personList.get(2)));
        assert (houseList.get(1).getPersons().contains(personList.get(3)));
        assert (houseList.get(1).getPersons().contains(personList.get(4)));
        assert (houseList.get(1).getPersons().contains(personList.get(5)));

        assert (houseList.get(0).getCabinets().contains(cabinetList.get(0)));
        assert (houseList.get(0).getCabinets().contains(cabinetList.get(1)));
        assert (houseList.get(1).getCabinets().contains(cabinetList.get(2)));
        assert (houseList.get(1).getCabinets().contains(cabinetList.get(3)));

        assert (cabinetList.get(0).getHouse().equals(houseList.get(0)));
        assert (cabinetList.get(1).getHouse().equals(houseList.get(0)));
        assert (cabinetList.get(2).getHouse().equals(houseList.get(1)));
        assert (cabinetList.get(3).getHouse().equals(houseList.get(1)));

        assert (cabinetList.get(0).getCloths().contains(clothList.get(0)));
        assert (cabinetList.get(0).getCloths().contains(clothList.get(2)));
        assert (cabinetList.get(1).getCloths().contains(clothList.get(1)));

        assert (clothList.get(0).getCabinet().equals(cabinetList.get(0)));
        assert (clothList.get(2).getCabinet().equals(cabinetList.get(0)));
        assert (clothList.get(1).getCabinet().equals(cabinetList.get(1)));
    }

    @Test
    @Order(2)
    @DisplayName("조회 테스트 - 실패 기대(LazyInitialization)")
    public void selectTest01() {
        /*
            @Transactional 범위도 없고,
            상호 참조가 fetch = FetchType.EAGER 도 아니므로,
            LazyLoading이라 값을 사용할 수 없음!!
            테스트 실패해야 함.

            failed to lazily initialize a collection of role: io.blackdeer.springdatajpa.person.Person.houses, could not initialize proxy - no Session
            org.hibernate.LazyInitializationException: failed to lazily initialize a collection of role: io.blackdeer.springdatajpa.person.Person.houses, could not initialize proxy - no Session
         */
        selectTest();
    }

    @Test
    @Order(2)
    @DisplayName("조회 테스트 - 성공 기대(@Transactional)")
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void selectTest02() {
        // @Transactional 범위 내에서 상호 참조 로딩 완료
        selectTest();
    }

    @Test
    @Order(3)
    @DisplayName("삭제 테스트")
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void deleteTest01() {
        /*
            Cabinet 클래스의 멤버 변수 cloths가 cascade = CASCADE.ALL 이더라도,
            @PreRemove에서 미리 cloth들과의 참조를 해지해두므로,
            Cabinet이 지워질 때 Cloth가 함께 지워지진 않음.
            DB의 CLOTH table에서 cabinet_id가 null로 변함.
         */
        Cabinet cabinet = cabinetService.getCabinetOrNullById(1L);
        cabinetService.deleteCabinet(cabinet);
    }
}
