package library;

/*
 * Jack Diaz
 * 111499298
 */

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import member.MemberData;

public class LibraryServerImpl implements LibraryServer {
	private ArrayList<Book> books;
	private int booksPerMember;
	private AtomicInteger currID;
	private Map<String, Integer> mems;
	private Map<String, Integer> stacks;
	private Map<Integer, List<Book>> memDat;
	/**
	 * Constructor for the library server. It is given a number total books to have, number of
	 * copies per book, and maximum books per member. 
	 * Creates a number of Book objects based on numBooks to give them to members when checking them out.  
	 * The server maintains the properties and enforces them for future transactions.
	 * 
	 * @param numBooks
	 * @param copiesPerBook
	 * @param booksPerMember
	 */
	public LibraryServerImpl(int numBooks, int copiesPerBook, int booksPerMember) {
		this.booksPerMember = booksPerMember;
		this.currID = new AtomicInteger();
		this.books = new ArrayList<Book>();
		this.stacks = new HashMap<String, Integer>();
		for(int i = 0; i < numBooks; i++){
			Book add = new Book("Book "+i);
			this.books.add(add);
			this.stacks.put(add.getName(), copiesPerBook);
		}
		this.mems = new HashMap<String, Integer>();
		this.memDat = new HashMap<Integer, List<Book>>();
	}

	/* (non-Javadoc)
	 * @see library.LibraryServer#registerMember(member.Member)
	 */
	@Override
	public synchronized Integer registerMember(MemberData memberdata) throws RemoteException {
		String name = memberdata.getName();
		if(!mems.containsKey(name)){
			int ret = currID.incrementAndGet();
			mems.put(name, ret);
			memDat.put(ret, new ArrayList<Book>());
			return ret;
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see library.LibraryServer#checkoutBook(java.lang.String, member.Member)
	 */
	@Override
	public synchronized Book checkoutBook(String bookName, MemberData memberdata) throws RemoteException {
		/*
		System.out.println(Thread.currentThread());
		System.out.println(this);
		 */
		
		if(!check(memberdata)){ // memberdata's name and id correspond to the lib's records
			return null;
		}
		List<Book> memBooks = getBooks(memberdata.getMemberId());

		if(!(memBooks.size() >= booksPerMember) && // they're not exceeding their borrow limit
				!bookListHasBook(memBooks, bookName) && // they don't already have a copy of this book
				libHasBook(bookName)){ // the library has the book in stock
			Book bk = getBook(bookName);
			memBooks.add(bk);
			reduceInventory(bookName);
			return bk;
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see library.LibraryServer#returnBook(java.lang.String, member.Member)
	 */
	@Override
	public synchronized boolean returnBook(String bookName, MemberData memberdata) throws RemoteException {
		if(!check(memberdata)){ // memberdata's name and id correspond to the lib's records
			return false;
		}
		List<Book> memBooks = getBooks(memberdata.getMemberId());

		if(bookListHasBook(memBooks, bookName)){ // they have taken out the book
			removeBook(bookName, memBooks); // remove book from their current borrows
			increaseInventory(bookName);
			return true;
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see library.LibraryServer#getBookListings()
	 */
	@Override
	public synchronized List<String> getBookListings() throws RemoteException {
		List<String> ret = new ArrayList<String>();
		for(Book b : books){
			ret.add(b.getName());
		}
		return ret;
	}

	/* (non-Javadoc)
	 * @see library.LibraryServer#getAvailableBookListings()
	 */
	@Override
	public synchronized List<String> getAvailableBookListings() throws RemoteException {
		List<String> ret = new ArrayList<String>();
		String name;
		for(Book b : books){
			name = b.getName();
			if(libHasBook(name)){
				ret.add(name);
			}
		}
		return ret;
	}

	// compare name and ID of given memberdata with the name and ID in the library's records
	private synchronized boolean check(MemberData in){
		String name = in.getName();
		if(!mems.containsKey(name)){
			return false;
		}
		return (mems.get(name)).equals(in.getMemberId());
	}

	// check if the booklist contains a book with the given name
	private synchronized boolean bookListHasBook(List<Book> bookList, String name){
		for(Book b : bookList){
			if(b.getName().equals(name)){
				return true;
			}
		}
		return false;
	}

	// check if library currently has the book in stock
	private synchronized boolean libHasBook(String name){
		if(stacks.containsKey(name) && (stacks.get(name) > 0)){
			return true;
		}
		return false;
	}

	// get a particular book object by name
	private synchronized Book getBook(String name){
		for(Book b : books){
			if(b.getName().equals(name)){
				return b;
			}
		}
		return null;
	}

	// remove a particular book from a book list
	private synchronized List<Book> removeBook(String name, List<Book> bookList){
		Book remove = new Book("");
		for(Book b : bookList){
			if(b.getName().equals(name)){
				remove = b;
			}
		}
		bookList.remove(remove);
		return bookList;
	}

	// get list of books currently checked out for member with ID in
	private synchronized List<Book> getBooks(int in){
		return memDat.get(in);
	}
	
	private synchronized void reduceInventory(String bookName){
		stacks.put(bookName, stacks.get(bookName)-1);
	}
	
	private synchronized void increaseInventory(String bookName){
		stacks.put(bookName, stacks.get(bookName) + 1);
	}

	public static void main(String[] args) {
		int numBooks = 10;
		int copiesPerBook = 3;
		int booksPerMember = 2;

		Integer port = (args.length < 1) ? 1099 : new Integer(args[0]);
		try {
			LibraryServer library = new LibraryServerImpl(numBooks, copiesPerBook, booksPerMember);
			LibraryServer stub = (LibraryServer) UnicastRemoteObject.exportObject(library, 0);

			// Create the registry and register the stub
			Registry registry = LocateRegistry.createRegistry((int)port);
			registry.bind("lib", stub);

			// Print ready message
			System.err.println("Server ready");
		} catch (Exception e) {
			System.err.println("Server exception: " + e.toString());
			e.printStackTrace();
		}
	}

}
