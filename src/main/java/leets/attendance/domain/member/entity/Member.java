package leets.attendance.domain.member.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PostPersist;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import leets.attendance.domain.attendance.entity.Attendance;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
public class Member implements UserDetails {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String name;

  @Enumerated(EnumType.STRING)
  private Part part;

  private String username;
  private String password;

  @Builder(builderMethodName = "createMember")
  public Member(String name, Part part, String username, String password) {
    this.name = name;
    this.part = part;
    this.username = username;
    this.password = password;
  }

  @OneToMany(mappedBy = "member", cascade = CascadeType.PERSIST)
  private List<Attendance> attendances = new ArrayList<>();

  @PostPersist
  public void postPersist() {
    /**
     * 동아리 시작 일을 지정해줘야 합니다.
     */
    LocalDate currentDate = LocalDate.of(2024, 3, 28);
    List<Attendance> attendances = new ArrayList<>();

    for (int i = 1; i <= 8; i++) {
      Attendance attendance = new Attendance(this, i, currentDate.plusWeeks(i - 1));
      attendances.add(attendance);
    }

    this.attendances = attendances;
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return List.of();
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }
}
