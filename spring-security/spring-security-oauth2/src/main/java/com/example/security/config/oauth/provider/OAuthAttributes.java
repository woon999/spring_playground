package com.example.security.config.oauth.provider;

import java.util.Map;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.example.security.model.User;

import lombok.Builder;
import lombok.Getter;

@Getter
public class OAuthAttributes {
	private String provider;
	private String providerId;
	private String username;
	private String password;
	private String email;

	@Builder
	public OAuthAttributes(String provider, String providerId, String username, String password, String email) {
		this.provider = provider;
		this.providerId = providerId;
		this.username = username;
		this.password = password;
		this.email = email;
	}

	public static OAuthAttributes of(String provider, Map<String, Object> attributes){
		if("google".equals(provider)){
			System.out.println("구글 로그인 요청");
			return createUser(new GoogleUserInfo(attributes));
		} else if("facebook".equals(provider)){
			System.out.println("페이스북 로그인 요청");
			return createUser(new FacebookUserInfo(attributes));
		}else if("naver".equals(provider)){
			System.out.println("네이버 로그인 요청");
			return createUser(new NaverUserInfo((Map)attributes.get("response")));
		}else if("kakao".equals(provider)){
			System.out.println("카카오 로그인 요청");
			return createUser(new KakaoUserInfo(attributes));
		}

		throw new NullPointerException("해당 Provider는 존재하지 않습니다.");
	}

	private static OAuthAttributes createUser(OAuth2UserInfo oAuth2UserInfo){
		StringBuilder sb = new StringBuilder();
		return OAuthAttributes.builder()
			.provider(oAuth2UserInfo.getProvider())
			.providerId(sb.append(oAuth2UserInfo.getProvider())
				.append("_")
				.append(oAuth2UserInfo.getProviderId()).toString())
			.username(oAuth2UserInfo.getName())
			.email(oAuth2UserInfo.getEmail())
			.password(new BCryptPasswordEncoder().encode("getinthere"))
			.build();
	}

	public User toEntity(){
		return User.builder()
			.provider(provider)
			.providerId(providerId)
			.username(username)
			.password(password)
			.email(email)
			.role("ROLE_USER")
			.build();
	}
}
