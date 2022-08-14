package io.blackdeer.springdatajpa.cabinet;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import io.blackdeer.springdatajpa.cloth.Cloth;
import io.blackdeer.springdatajpa.house.House;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@SequenceGenerator(name = "SEQ_CABINET", sequenceName = "cabinet_sequence")
public class Cabinet {

    private static final Logger logger = LoggerFactory.getLogger(Cabinet.class);

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_CABINET")
    private Long id;

    @Column(nullable = false, length = 10)
    private String name;

    /*
        cascade = CASCADE.REMOVE가 포함되어 있으면,
        이 Entity를 지울 때 멤버변수로 갖는 다른 Entity도 함께 지우려고 함.
        cascade = CASCADE.ALL로 다른 영속성 전이는 하되,
        @PreRemove에서 하위 Entity들을 비워버리기로.
     */
    @JsonBackReference
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(nullable = true)
    private House house;

    /*
        cascade = CASCADE.REMOVE가 포함되어 있으면,
        이 Entity를 지울 때 멤버변수로 갖는 다른 Entity도 함께 지우려고 함.
        cascade = CASCADE.ALL로 다른 영속성 전이는 하되,
        @PreRemove에서 하위 Entity들을 비워버리기로.
     */
    @JsonManagedReference
    @OneToMany(mappedBy = "cabinet", cascade = CascadeType.ALL)    // 1:N에서, N쪽에서 참조하는 변수명
    private List<Cloth> cloths;

    // JPA에서 DB->instance를 위한 protected 생성자
    protected Cabinet() {
    }

    public Cabinet(@NonNull String name,
                   @Nullable House houseOrNull) {
        assert (name != null && name.length() > 0 && name.length() < 11);
        this.name = name;
        this.house = houseOrNull;
        this.cloths = new ArrayList<>();
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public House getHouse() {
        return house;
    }

    public List<Cloth> getCloths() {
        return cloths;
    }

    public void setName(@NonNull String name) {
        assert (name != null && name.length() > 0 && name.length() < 11);
        this.name = name;
    }

    public void setHouse(@Nullable House houseOrNull) {
        this.house = houseOrNull;
    }

    /*
        아래부턴 JPA에서 DB->instance를 위한 protected setter
     */

    protected void setId(Long id) {
        this.id = id;
    }

    protected void setCloths(List<Cloth> cloths) {
        this.cloths = cloths;
    }

    @PreRemove
    private void preRemove() {
        this.house = null;
        for (Cloth cloth : cloths) {
            if (cloth.getCabinet().equals(this)) {
                cloth.setCabinet(null);
            }
        }
        this.cloths.clear();
    }
}
