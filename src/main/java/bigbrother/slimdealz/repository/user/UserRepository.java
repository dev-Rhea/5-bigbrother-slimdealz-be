package bigbrother.slimdealz.repository.user;

import bigbrother.slimdealz.entity.user.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<Member, Long> {

    @Query("SELECT m FROM Member m WHERE m.kakao_Id = :kakaoId")
    Optional<Member> findByKakaoId(String kakaoId);
}
