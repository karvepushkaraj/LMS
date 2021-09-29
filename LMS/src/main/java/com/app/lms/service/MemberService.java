package com.app.lms.service;

import com.app.lms.model.Member;

public interface MemberService {

	Member getMember(int memberId);

	void addMember(Member member);

//	void updateMember(Member member);

	boolean deleteMember(int memberId);

	void addSubscription(int memberId, int pkgId);

}
