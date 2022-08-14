package io.blackdeer.springdatajpa.house;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import io.blackdeer.springdatajpa.cabinet.Cabinet;
import io.blackdeer.springdatajpa.person.Person;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@SequenceGenerator(name = "SEQ_HOUSE", sequenceName = "house_sequence")
public class House {

    private static final Logger logger = LoggerFactory.getLogger(House.class);

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_HOUSE")
    private Long id;

    @Column(nullable = false, length = 10)
    private String name = "";

    @JsonIgnore    // json형식으로 값을 반환할 때 제외된다.
    @Column(nullable = true)
    private String address;

    /*
        cascade = CASCADE.REMOVE가 포함되어 있으면,
        이 Entity를 지울 때 멤버변수로 갖는 다른 Entity도 함께 지우려고 함.
        cascade = CASCADE.ALL로 다른 영속성 전이는 하되,
        @PreRemove에서 하위 Entity들을 비워버리기로.
     */
    @JsonBackReference
    @ManyToMany(mappedBy = "houses", cascade = CascadeType.ALL)    // 관계의 소유주는 반대편.
    private List<Person> persons;

    /*
        cascade = CASCADE.REMOVE가 포함되어 있으면,
        이 Entity를 지울 때 멤버변수로 갖는 다른 Entity도 함께 지우려고 함.
        cascade = CASCADE.ALL로 다른 영속성 전이는 하되,
        @PreRemove에서 하위 Entity들을 비워버리기로.
     */
    @JsonManagedReference
    @OneToMany(mappedBy = "house", cascade = CascadeType.ALL)    // 관계의 소유주는 반대편.
    private List<Cabinet> cabinets;

    // JPA에서 DB->instance를 위한 protected 생성자
    protected House() {
    }

    public House(@NonNull String name,
                 @Nullable String addressOrNull) {
        assert (name != null && name.length() > 0 && name.length() < 11);
        this.name = name;
        this.address = addressOrNull;
        this.persons = new ArrayList<>();
        this.cabinets = new ArrayList<>();
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public List<Person> getPersons() {
        return persons;
    }

    public List<Cabinet> getCabinets() {
        return cabinets;
    }

    public void setName(@NonNull String name) {
        assert (name != null && name.length() > 0 && name.length() < 11);
        this.name = name;
    }

    public void setAddress(@Nullable String addressOrNull) {
        this.address = addressOrNull;
    }

    /*
        아래부턴 JPA에서 DB->instance를 위한 protected setter
     */

    protected void setId(Long id) {
        this.id = id;
    }

    protected void setPersons(List<Person> persons) {
        this.persons = persons;
    }

    protected void setCabinets(List<Cabinet> cabinets) {
        this.cabinets = cabinets;
    }

    @PreRemove
    private void preRemove() {
        for (Person person : persons) {
            person.getHouses().remove(this);
        }
        this.persons.clear();
        for (Cabinet cabinet : cabinets) {
            cabinet.setHouse(null);
        }
        this.cabinets.clear();
    }
}
