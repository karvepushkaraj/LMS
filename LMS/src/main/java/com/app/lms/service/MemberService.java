package com.app.lms.service;

import com.app.lms.model.Member;
import com.app.lms.util.InvalidBusinessCondition;

/**
 * Service interface for all operations related to Members.
 * 
 * @author karve
 *
 */

public interface MemberService {

	Member getMember(int memberId) throws InvalidBusinessCondition;

	void addMember(Member member) throws InvalidBusinessCondition;

	void updateMember(Member member) throws InvalidBusinessCondition;

	void deleteMember(int memberId) throws InvalidBusinessCondition;

	void addSubscription(int memberId, int pkgId) throws InvalidBusinessCondition;

}
