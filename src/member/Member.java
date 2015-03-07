package member;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

import library.Book;
import library.LibraryServer;

public interface Member extends Remote {
	
	/**
	 * Returns the name of the current member.
	 * 
	 * @return String
	 * @throws RemoteException
	 */
	public String getName() throws RemoteException;
	
	/**
	 * Registers the current member with the LibraryServer. 
	 * Maintains the returned member id for future authentication.
	 * Returns true if successful.
	 * 
	 * @return boolean
	 * @throws RemoteException
	 */
	public boolean register() throws RemoteException;
	
	/**
	 * The current member tries to checkout the given Book from the LibraryServer. 
	 * The book is added to the booksCurrCheckOut list. Returns true if successful.
	 * 
	 * @return boolean
	 * @throws RemoteException
	 */
	public boolean checkoutBook(String bookName) throws RemoteException;
	
	/**
	 * The current member tries to return the given Book from the LibraryServer. 
	 * The book is removed from the booksCurrCheckOut list. 
	 * The book is added to the booksRead list. Returns true if successful.
	 * 
	 * @return boolean
	 * @throws RemoteException
	 */
	public boolean returnBook(String bookName) throws RemoteException;
	
	/**
	 * Returns the LibraryServer instance that the current member is registered with.
	 * 
	 * @return LibraryServer
	 * @throws RemoteException
	 */
	public LibraryServer getServer() throws RemoteException;
	
	/**
	 * Sets the LibraryServer instance that the current member is registered with.
	 * 
	 * @param LibraryServer
	 * @throws RemoteException
	 */
	public void setServer(LibraryServer server) throws RemoteException;
	
	/**
	 * Returns the books currently checked out by the current member.
	 * 
	 * @return List<Book>
	 * @throws RemoteException
	 */
	public List<Book> getBooksCurrCheckedOut() throws RemoteException;
	
	/**
	 * Returns the names of the books the current member has read. 
	 * This list should maintain an order based on the order reading the books.
	 * 
	 * 
	 * @return List<Book>
	 * @throws RemoteException
	 */
	public List<String> getBooksRead() throws RemoteException;
}
