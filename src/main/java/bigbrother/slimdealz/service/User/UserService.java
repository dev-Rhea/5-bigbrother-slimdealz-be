package bigbrother.slimdealz.service.User;

import bigbrother.slimdealz.repository.User.UserRepository;
import bigbrother.slimdealz.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/*
@RequiredArgsConstructor 어노테이션을 사용하면 생성자를 자동으로 생성해 주므로
@Autowired 어노테이션을 사용하지 않아도 됩니다.
 */

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

//    @Autowired
//    public UserService(UserRepository userRepository) {
//        this.userRepository = userRepository;
//    }

    /*
    메서드 명에서도 camelCase적용해주세요
     */
    public Long findUserIdByKakao_Id(String kakao_Id) {
        return userRepository.findByKakao_Id(kakao_Id)
                .map(Member::getId)
                .orElse(null);
    }
}
