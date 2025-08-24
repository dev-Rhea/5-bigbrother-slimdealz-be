package bigbrother.slimdealz.service.user;

import bigbrother.slimdealz.repository.user.UserRepository;
import bigbrother.slimdealz.entity.user.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public Long findUserIdByKakaoId(String kakaoId) {
        return userRepository.findByKakaoId(kakaoId)
                .map(Member::getId)
                .orElse(null);
    }
}
