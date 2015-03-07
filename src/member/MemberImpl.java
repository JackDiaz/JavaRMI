package member;

/*
 * Jack Diaz
 * 111499298
 */

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import library.*;

public class MemberImpl implements Member{
	private static AtomicInteger memCnt = new AtomicInteger();
	private static AtomicInteger memName = new AtomicInteger();
	private MemberData data;
	private LibraryServer lib;

	/**
	 * Default constructor of the member client. Initializes variables. 
	 * You may add other constructors if you need.
	 * 
	 */
	public MemberImpl() {
		this.data = new MemberData(("Member "+memName.incrementAndGet()), new ArrayList<Book>(), memCnt.decrementAndGet(), new ArrayList<String>());
		this.lib = null;
	}

	/* (non-Javadoc)
	 * @see member.Member#getName()
	 */
	public String getName() throws RemoteException{
		return data.getName();
	}

	/* (non-Javadoc)
	 * @see member.Member#register()
	 */
	public boolean register() throws RemoteException{
		Integer ret = lib.registerMember(data);
		if(ret != null){
			data.setMemberId(ret);
			return true;
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see member.Member#checkoutBook(java.lang.String)
	 */
	public boolean checkoutBook(String bookName) throws RemoteException{
		Book bk = lib.checkoutBook(bookName, data);
		if(bk != null){
			data.getBooksCurrCheckedOut().add(bk);
			return true;
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see member.Member#returnBook(java.lang.String)
	 */
	public boolean returnBook(String bookName) throws RemoteException{
		if(lib.returnBook(bookName, data)){
			String bk = removeBook(bookName, data.getBooksCurrCheckedOut()).getName();
			List<String> read = data.getBooksRead();
			if(!read.contains(bk)){//?
				read.add(bk);
			}
			return true;
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see member.Member#getServer()
	 */
	public LibraryServer getServer() throws RemoteException{
		return lib;
	}

	/* (non-Javadoc)
	 * @see member.Member#setServer(library.LibraryServer)
	 */
	public void setServer(LibraryServer server) throws RemoteException {
		this.lib = server;
	}

	/* (non-Javadoc)
	 * @see member.Member#getBooksCheckedOut()
	 */
	public List<Book> getBooksCurrCheckedOut() throws RemoteException{
		return data.getBooksCurrCheckedOut();
	}

	/* (non-Javadoc)
	 * @see member.Member#getBooksRead()
	 */
	public List<String> getBooksRead() throws RemoteException {
		return data.getBooksRead();
	}

	// removes a book from a list, returning the removed book
	private Book removeBook(String name, List<Book> bookList){
		Book remove = new Book("");
		for(Book b : bookList){
			if(b.getName().equals(name)){
				remove = b;
			}
		}
		bookList.remove(remove);
		return remove;
	}

	public static void main(String[] args) {

		Integer port = (args.length < 1) ? 1099 : new Integer(args[0]);
		try {
			Registry registry = LocateRegistry.getRegistry(port);
			LibraryServer server = (LibraryServer) registry.lookup("lib");
			MemberImpl mem = new MemberImpl();
			mem.setServer(server);
			System.out.println("Client connected");
		} catch (Exception e) {
			System.err.println("Client exception: " + e.toString());
			e.printStackTrace();
		}		
	}
}
