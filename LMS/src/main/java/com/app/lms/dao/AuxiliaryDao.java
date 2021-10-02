package com.app.lms.dao;

import java.util.List;
import java.util.Optional;

import com.app.lms.model.BookCopy;
import com.app.lms.model.BookTransaction;
import com.app.lms.model.LibrarySection;
import com.app.lms.model.Member;

/**
 * Auxiliary Dao for application specific implementations.
 * @author karve
 *
 */

public interface AuxiliaryDao {

	/**
	 * Get {@link LibrarySection} id if the book is free i.e. not issued to {@link Member}
	 * @param bookid {@link BookCopy} id
	 * @return {@link LibrarySection} id
	 */
	Optional<String> getFreeBookSection(String bookid);

	/**
	 * Get list of {@link LibrarySection} ids if the member is free 
	 * i.e book is not issued to the member of section to which member has subscribed. 
	 * @param memberid
	 * @return List of {@link LibrarySection} ids
	 */
	List<String> getFreeMemberSections(int memberid);

	/**
	 * Get active book transaction i.e. transaction where book is not returned by the member.
	 * @param bookid {@link BookCopy} id
	 * @param memberid {@link Member} id
	 * @return
	 */
	Optional<BookTransaction> getActBkTrans(String bookid, int memberid);

}
