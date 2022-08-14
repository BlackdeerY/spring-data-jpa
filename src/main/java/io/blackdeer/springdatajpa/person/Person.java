package io.blackdeer.springdatajpa.person;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import io.blackdeer.springdatajpa.house.House;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@SequenceGenerator(name = "SEQ_PERSON", sequenceName = "person_sequence")
public class Person {

    private static final Logger logger = LoggerFactory.getLogger(Person.class);

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_PERSON")
    private Long id;

    @Column(nullable = false, length = 10)    // null이 아닌 코드를 작성하면 자동으로 nullable = false로 해주지만 명시적으로 해 둠.
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Gender gender;

    @Column(nullable = false)
    private Integer age = new Integer(1);

    @Temporal(TemporalType.DATE)
    @Column(nullable = false)    // null이 아닌 코드를 작성하면 자동으로 nullable = false로 해주지만 명시적으로 해 둠.
    private Date birthday;

    @JsonIgnore    // json형식으로 값을 반환할 때 제외된다.
    @Column(nullable = true, length = 100)
    private String memo;

    @JsonIgnore    // json형식으로 값을 반환할 때 제외된다.
    @Transient    // DB와 연동시키지 않을 값
    private String noDBInfo;

    /*
        cascade = CASCADE.REMOVE가 포함되어 있으면,
        이 Entity를 지울 때 멤버변수로 갖는 다른 Entity도 함께 지우려고 함.
        cascade = CASCADE.ALL로 다른 영속성 전이는 하되,
        @PreRemove에서 하위 Entity들을 비워버리기로.
     */
    @JsonManagedReference
    @ManyToMany(cascade = CascadeType.ALL)
    // ManyToMany는 소유자가 아래의 정보를 갖는다.
    @JoinTable(name = "person_house",
            joinColumns = @JoinColumn(name = "person_id"),
            inverseJoinColumns = @JoinColumn(name = "house_id"))
    private List<House> houses;

    // JPA에서 DB->instance를 위한 protected 생성자
    protected Person() {
    }

    public Person(@NonNull String name,
                  @NonNull Gender gender,
                  @NonNull Integer age,
                  @NonNull Date birthday,
                  @Nullable String memoOrNull,
                  @Nullable String noDBInfoOrNull) {
        assert (name != null && name.length() > 0 && name.length() < 11);
        assert (gender != null);
        assert (age != null && age > 0);
        assert (birthday != null);
        this.name = name;
        this.gender = gender;
        this.age = age;
        this.birthday = birthday;
        this.memo = (memoOrNull != null && memoOrNull.length() > 100) ? memoOrNull.substring(0, 100) : memoOrNull;
        this.noDBInfo = noDBInfoOrNull;
        this.houses = new ArrayList<>();
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Gender getGender() {
        return gender;
    }

    public Integer getAge() {
        return age;
    }

    public Date getBirthday() {
        return birthday;
    }

    public String getMemo() {
        return memo;
    }

    public String getNoDBInfo() {
        return noDBInfo;
    }

    public List<House> getHouses() {
        return houses;
    }

    public void setName(@NonNull String name) {
        assert (name != null && name.length() > 0 && name.length() < 11);
        this.name = name;
    }

    public void setGender(@NonNull Gender gender) {
        assert (gender != null);
        this.gender = gender;
    }

    public void setAge(@NonNull Integer age) {
        assert (age != null && age > 0);
        this.age = age;
    }

    public void setBirthday(@NonNull Date birthday) {
        assert (birthday != null);
        this.birthday = birthday;
    }

    public void setMemo(@Nullable String memoOrNull) {
        this.memo = memoOrNull;
    }

    public void setNoDBInfo(@Nullable String noDBInfoOrNull) {
        this.noDBInfo = noDBInfoOrNull;
    }

    /*
        아래부턴 JPA에서 DB->instance를 위한 protected setter
     */

    protected void setId(Long id) {
        this.id = id;
    }

    protected void setHouses(List<House> houses) {
        this.houses = houses;
    }

    @PreRemove
    private void preRemove() {
        for (House house : houses) {
            house.getPersons().remove(this);
        }
        this.houses.clear();
    }
}
