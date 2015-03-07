/**
 * 
 */
package member;

import java.io.Serializable;
import java.util.List;

import library.Book;

/**
 * The data object that holds the member details. Used for RMI communication.  
 * This class implementation is provided for you. You may add other functionality if needed.
 *
 */
public class MemberData implements Serializable {
	private static final long serialVersionUID = 433L;
	
	private String name;
	private List<Book> booksCurrCheckedOut;
	private Integer memberId;
	private List<String> booksRead;

	public MemberData(String name, List<Book> booksCurrCheckedOut, Integer memberId, List<String> booksRead) {
		this.name = name;
		this.booksCurrCheckedOut = booksCurrCheckedOut;
		this.memberId = memberId;
		this.booksRead = booksRead;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public List<Book> getBooksCurrCheckedOut() {
		return booksCurrCheckedOut;
	}

	public void setBooksCurrCheckedOut(List<Book> booksCurrCheckedOut) {
		this.booksCurrCheckedOut = booksCurrCheckedOut;
	}
	
	public Integer getMemberId() {
		return memberId;
	}
	
	public void setMemberId(Integer memberId) {
		this.memberId = memberId;
	}

	public List<String> getBooksRead() {
		return booksRead;
	}

	public void setBooksRead(List<String> booksRead) {
		this.booksRead = booksRead;
	}
	
}
