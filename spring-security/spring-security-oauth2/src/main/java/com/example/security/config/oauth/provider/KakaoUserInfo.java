package com.example.security.config.oauth.provider;

import java.util.Map;

public class KakaoUserInfo implements OAuth2UserInfo{
	private Map<String, Object> attributes; // oAuth2User.getAttributes()
	private Map<String, Object> kakaoAccount;

	public KakaoUserInfo(Map<String, Object> attributes) {
		this.attributes = attributes;
		kakaoAccount = (Map<String, Object>)attributes.get("kakao_account");
	}

	@Override
	public String getProviderId() {
		return ""+attributes.get("id");
	}

	@Override
	public String getProvider() {
		return "kakao";
	}

	@Override
	public String getEmail() {
		return (String)kakaoAccount.get("email");
	}

	@Override
	public String getName() {
		return (String)((Map<String, Object>)kakaoAccount.get("profile")).get("nickname");
	}
}
