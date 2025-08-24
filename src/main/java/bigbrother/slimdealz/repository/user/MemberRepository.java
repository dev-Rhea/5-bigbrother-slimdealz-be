package bigbrother.slimdealz.repository.user;

import bigbrother.slimdealz.entity.user.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    @Query("select m from Member m where m.kakao_Id = :kakao_Id")
    Optional<Member> findByKakaoId(@Param("kakao_Id") String kakao_Id);
}