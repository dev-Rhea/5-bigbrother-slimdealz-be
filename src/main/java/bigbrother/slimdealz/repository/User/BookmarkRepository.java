package bigbrother.slimdealz.repository.User;

import bigbrother.slimdealz.entity.Bookmark;
import bigbrother.slimdealz.entity.Member;
import bigbrother.slimdealz.entity.product.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public interface BookmarkRepository extends JpaRepository<Bookmark, Long>, QuerydslPredicateExecutor<Bookmark>, BookmarkRepositoryCustom {

    /*
    쿼리 작성시에 이 쿼리가 어떠한 작업을 수행하는지 주석으로 남겨 주셔야
    다른 사람이 필요시에 사용하기 쉽습니다.
    어떠한 쿼리인지 쿼리문 마다 주석으로 용도를 남겨주세요.
     */
    @Query("SELECT b FROM Bookmark b JOIN FETCH b.product p JOIN FETCH p.prices WHERE b.member.id = :memberId")
    List<Bookmark> findBookmarksWithProductsAndPrices(Long memberId);
    @Query("SELECT b FROM Bookmark b JOIN b.product p WHERE b.member.id = :memberId AND p.name = :productName")
    List<Bookmark> findBookmarksByMemberIdAndProductName(Long memberId, String productName);

    Optional<Bookmark> findByMemberAndProduct(Member member, Product product);
}
