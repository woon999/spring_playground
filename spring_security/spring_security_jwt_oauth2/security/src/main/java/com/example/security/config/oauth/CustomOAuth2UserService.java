package com.example.security.config.oauth;

import java.util.Map;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.example.security.config.auth.PrincipalDetails;
import com.example.security.config.oauth.provider.FacebookUserInfo;
import com.example.security.config.oauth.provider.GoogleUserInfo;
import com.example.security.config.oauth.provider.NaverUserInfo;
import com.example.security.config.oauth.provider.OAuth2UserInfo;
import com.example.security.model.User;
import com.example.security.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

	private final UserRepository userRepository;

	// 구글로 부터 받은 userRequest 데이터에 대한 후처리되는 함수
	// 함수 종료시 @AuthenticationPrincipal 어노테이션이 만들어진다.
	@Override
	public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
		System.out.println("userRequest: " + userRequest.getClientRegistration());
		System.out.println("getAccessToken: " + userRequest.getAccessToken());

		// 구글로그인 버튼 클릭 -> 구글 로그인 창 -> 로그인 완료 -> code를 리턴(OAuth-Client 라이브러리) -> AccessToken 요청
		// userRequest 정보(AccessToken 있음, 유저정보 필요함) -> loadUser 함수 호출 -> 구글로부터 회원 프로필 받아줌
		OAuth2User oAuth2User = super.loadUser(userRequest);
		System.out.println("getAttributes: " + oAuth2User.getAttributes());

		OAuth2UserInfo oAuth2UserInfo = null;
		if(userRequest.getClientRegistration().getRegistrationId().equals("google")){
			System.out.println("구글 로그인 요청");
			oAuth2UserInfo = new GoogleUserInfo(oAuth2User.getAttributes());
		} else if(userRequest.getClientRegistration().getRegistrationId().equals("facebook")){
			System.out.println("페이스북 로그인 요청");
			oAuth2UserInfo = new FacebookUserInfo(oAuth2User.getAttributes());
		} else if(userRequest.getClientRegistration().getRegistrationId().equals("naver")){
			System.out.println("네이버 로그인 요청");
			oAuth2UserInfo = new NaverUserInfo((Map)oAuth2User.getAttributes().get("response"));

		} else {
			System.out.println("구글, 페이스북, 네이버만 지원됩니다.");
		}

		// OAuth2 회원가입 유저 정보
		StringBuilder sb = new StringBuilder();
		String provider = oAuth2UserInfo.getProvider();
		String providerId = oAuth2UserInfo.getProviderId();
		String username =sb.append(provider)
			.append("_")
			.append(providerId).toString(); // google_101897731656893138339
		String email = oAuth2UserInfo.getEmail();
		String password = new BCryptPasswordEncoder().encode("getinthere"); // 의미없음
		String role = "ROLE_USER";

		User user = userRepository.findByUsername(username);
		if(user == null) {
			user = User.builder()
				.username(username)
				.password(password)
				.email(email)
				.role(role)
				.provider(provider)
				.providerId(providerId)
				.build();
			userRepository.save(user);

		}
		return new PrincipalDetails(user, oAuth2User.getAttributes());
	}
}
