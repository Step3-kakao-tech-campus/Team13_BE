package com.theocean.fundering.domain.celebrity.domain;

import com.theocean.fundering.celebrity.domain.constant.CelebGender;
import com.theocean.fundering.celebrity.domain.constant.CelebType;
import com.theocean.fundering.domain.follow.domain.Follow;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "celebrity",
        indexes = @Index(columnList = "celebName")
)
@Entity
public class Celebrity {
    @Id
    @Column(name = "celebId")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long celebId;

    @Column(nullable = false, length = 15)
    private String celebName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CelebGender celebGender;

    @Column(nullable = false)
    private CelebType celebType;

    @Column(length = 50)
    private String celebGroup;

    @Column(nullable = false)
    private String profileImage;

    @OneToMany(mappedBy = "celebrity")
    private List<Follow> followers;

    /*
        이후 fundering 객체로 리팩토링 예정
    */

    @Column(nullable = false)
    private Integer funderingCount;

    @Column(nullable = false)
    private Integer funderAmount;

    /*
        AuditingEntity로 리팩토링 예정
    */

    @CreatedDate
    @Column(nullable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime modifiedAt;

    public void changeCelebName(String celebName) {
        this.celebName = celebName;
    }

    public void changeCelebGender(CelebGender celebGender) {
        this.celebGender = celebGender;
    }

    public void changeCeleType(String celebGroup) {
        this.celebType = celebType;
    }

    public void changeCeleGroup(CelebType celebType) {
        this.celebType = celebType;
    }

    public void changeProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    @Builder
    public Celebrity(String celebName, CelebGender celebGender, CelebType celebType,
                     String celebGroup, String profileImage) {
        this.celebName = celebName;
        this.celebGender = celebGender;
        this.celebType = celebType;
        this.celebGroup = celebGroup;
        this.profileImage = profileImage;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Celebrity celebrity)) return false;
        return Objects.equals(celebId, celebrity.celebId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(celebId);
    }
}
