package io.blackdeer.springdatajpa.cloth;

import com.fasterxml.jackson.annotation.JsonBackReference;
import io.blackdeer.springdatajpa.cabinet.Cabinet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import javax.persistence.*;

@Entity
@SequenceGenerator(name = "SEQ_CLOTH", sequenceName = "cloth_sequence")
public class Cloth {

    private static final Logger logger = LoggerFactory.getLogger(Cloth.class);

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_CLOTH")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ClothType clothType;

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
    @JoinColumn(nullable = true)    // 옷은 캐비닛 안에 반드시 있지 않아도 된다고 가정
    private Cabinet cabinet;

    // JPA에서 DB->instance를 위한 protected 생성자
    protected Cloth() {
    }

    // 새로운 개체은 id가 null인 상태(Transient)에서 .save()로 동기화 중(Persistent)으로 됨
    public Cloth(@NonNull ClothType clothType,
                 @NonNull String name,
                 @Nullable Cabinet cabinetOrNull) {
        assert (clothType != null);
        assert (name != null && name.length() > 0 && name.length() < 11);
        this.clothType = clothType;
        this.name = name;
        this.cabinet = cabinetOrNull;
    }

    public Long getId() {
        return id;
    }

    public ClothType getClothType() {
        return clothType;
    }

    public String getName() {
        return name;
    }

    public Cabinet getCabinet() {
        return cabinet;
    }

    public void setName(@NonNull String name) {
        assert (name != null && name.length() > 0 && name.length() < 11);
        this.name = name;
    }

    public void setCabinet(@Nullable Cabinet cabinetOrNull) {
        this.cabinet = cabinetOrNull;
    }

    /*
        아래부턴 JPA에서 DB->instance를 위한 protected setter
     */

    protected void setId(Long id) {
        this.id = id;
    }

    protected void setClothType(ClothType clothType) {
        this.clothType = clothType;
    }

    @PreRemove
    private void preRemove() {
        this.cabinet = null;
    }
}
