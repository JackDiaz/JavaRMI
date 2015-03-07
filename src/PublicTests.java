
/*
 * Jack Diaz
 * 111499298
 */

import static org.junit.Assert.*;

import java.net.UnknownHostException;
import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Semaphore;

import library.Book;
import library.LibraryServer;
import library.LibraryServerImpl;
import member.Member;
import member.MemberImpl;

import org.junit.Test;

public class PublicTests {
	static int port = 1099;
	static String libraryName = "LibraryServer";
	static Semaphore control = new Semaphore(5);
	static CountDownLatch latch = new CountDownLatch(5);
	static int barNum = 5;
	static CyclicBarrier bar = new CyclicBarrier(barNum);

	private class Weirdo implements Runnable {
		private MemberImpl member;
		private List<String> books;
		private Weirdo(MemberImpl member, List<String> books) {
			this.member = member;
			this.books = books;
		}
		public void run() {
			try {
				Registry registry = LocateRegistry.getRegistry(PublicTests.port);
				LibraryServer remoteServer = (LibraryServer)(registry.lookup(PublicTests.libraryName));
				member.setServer(remoteServer);
				
				
				ArrayList<Book> chk = new ArrayList<Book>();

				String bk = books.get(0);

				chk.add(new Book(bk));
				
				assertFalse(member.returnBook(bk));
				assertFalse(member.checkoutBook(bk));
				
				assertTrue(member.register());
				assertFalse(member.returnBook(bk));
				
				assertTrue(member.checkoutBook(bk));

				
				bk = books.get(1);
				
				assertTrue(member.checkoutBook(bk));

				
				bk = books.get(2);
				
				assertTrue(member.checkoutBook(bk));

				
				bk = books.get(3);
				
				assertFalse(member.checkoutBook(bk));
				assertFalse(member.returnBook(bk));
				assertFalse(member.returnBook(bk));
				
				

				
				/*
				MemberImpl mem2 = new MemberImpl();
				mem2.setServer(remoteServer);
				assertTrue(mem2.register()); 
				System.out.println(member.data.getMemberId());
				System.out.println(mem2.data.getMemberId());
				assertFalse(member.data.getMemberId().equals(mem2.data.getMemberId()));
				member.data.setMemberId(1);
				assertTrue(member.data.getMemberId().equals(mem2.data.getMemberId()));
				assertFalse(member.checkoutBook(bk));
				assertTrue(member.register());
				assertTrue(member.checkoutBook(bk));
				member.data.setMemberId(1);
				assertTrue(mem2.checkoutBook(bk));
				bk = books.get(1);
				assertFalse(member.checkoutBook(bk));
				assertTrue(mem2.checkoutBook(bk));
				mem2.data.setMemberId(-1);
				
				assertFalse(mem2.returnBook(bk));
				bk = books.get(2);
				assertFalse(mem2.checkoutBook(bk));


				
				
				
				assertFalse(member.register());
				assertFalse(member.register());
				//MemberImpl mem2 = new MemberImpl();
				MemberImpl mem3 = new MemberImpl();
				assertFalse(mem2.equals(mem3));
				assertFalse(mem2.getName().equals(mem3.getName()));
				assertTrue(mem2.getName().equals(mem2.getName()));
				assertTrue(mem2.equals(mem2));
				
				System.out.println("weirdo "+member.getName());
				*/

			/*	bar.await();
				assertFalse(member.checkoutBook(bk));

				rW();

				rW();
				//assertTrue(member.checkoutBook(bk));
				rW();
				*/


			} catch (NotBoundException e) { // Shouldn't happen
				e.printStackTrace();
				fail("Failed to retrieve library server object");
			} catch (RemoteException e) {
				e.printStackTrace();
				fail("Failed to register a member");
			} /*catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (BrokenBarrierException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}*/

		}
	}

	private class BasicClient implements Runnable {
		private Member member;
		private List<String> books;
		private BasicClient(Member member, List<String> books) {
			this.member = member;
			this.books = books;
		}
		public void run() {
			try {
				Registry registry = LocateRegistry.getRegistry(PublicTests.port);
				LibraryServer remoteServer = (LibraryServer)(registry.lookup(PublicTests.libraryName));
				member.setServer(remoteServer);
				ArrayList<Book> chk = new ArrayList<Book>();
				ArrayList<String> chkRet = new ArrayList<String>();

				String bk = books.get(0);

				chk.add(new Book(bk));
				assertTrue(member.register()); // Will fail until you implement library's registration
				System.out.println(member.getName());
				assertTrue(member.checkoutBook(bk));
				
				
				bk = books.get(1);
				chk.add(new Book(bk));
				assertTrue(member.checkoutBook(bk));
			
				bk = books.get(2);
				chk.add(new Book(bk));
				assertTrue(member.checkoutBook(bk));

				bk = books.get(3);
				chk.add(new Book(bk));
				assertTrue(member.checkoutBook(bk));
				
				
				bk = books.get(2);
				chk.remove(new Book(bk));
				chkRet.add(bk);
				assertTrue(member.returnBook(bk));

				bk = books.get(3);
				chk.remove(new Book(bk));
				chkRet.add(bk);
				assertTrue(member.returnBook(bk));
				
				
				bk = books.get(4);
				chk.add(new Book(bk));
				assertTrue(member.checkoutBook(bk));
				bk = books.get(5);
				chk.add(new Book(bk));
				assertTrue(member.checkoutBook(bk));
				
				assertTrue(member.getBooksCurrCheckedOut().equals(chk));
				
				bk = books.get(4);
				chk.remove(new Book(bk));
				chkRet.add(bk);
				assertTrue(member.returnBook(bk));

				bk = books.get(5);
				chk.remove(new Book(bk));
				chkRet.add(bk);
				assertTrue(member.returnBook(bk));
				
				
				assertTrue(member.getBooksRead().equals(chkRet));
				
				
/*
				bar.await();

				rW();
				assertTrue(member.returnBook(bk));
				rW();
				
				rW();
				*/
				


				/*
				assertFalse(member.returnBook("pikachu"));
				assertFalse(member.returnBook(bk));

				assertTrue(member.checkoutBook(bk));

				assertTrue(member.getBooksCurrCheckedOut().equals(chk));

				assertFalse(member.checkoutBook(bk));
				assertFalse(member.checkoutBook("pikachu"));

				assertTrue(member.returnBook(bk));
				assertFalse(member.getBooksCurrCheckedOut().equals(chk));

				chk.remove(0);
				assertTrue(member.getBooksCurrCheckedOut().equals(chk));
				assertTrue(member.checkoutBook(bk));
				assertFalse(member.getBooksCurrCheckedOut().equals(chk));

				assertTrue(member.returnBook(bk));

				// currently if a book is returned twice then it is in the read list once
				System.out.println(member.getBooksRead()); 

				assertTrue(member.getBooksCurrCheckedOut().equals(chk));

				System.out.println(member.getName());

				assertFalse(member.register());

				member.getServer();
				 */

			} catch (NotBoundException e) { // Shouldn't happen
				e.printStackTrace();
				fail("Failed to retrieve library server object");
			} catch (RemoteException e) {
				e.printStackTrace();
				fail("Failed to register a member");
			} /*catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (BrokenBarrierException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}*/

		}
	}

	private void rW() throws InterruptedException, BrokenBarrierException{
		if(bar.isBroken()){
			bar.reset();
		}
		bar.await();
	}

	@Test
	public void basicTest() throws UnknownHostException, RemoteException, AlreadyBoundException, NotBoundException, InterruptedException {
		int numBooks = 20;
		int copiesPerBook = 4;
		int booksPerMember = 3;

		// Simulate the server
		LibraryServerImpl library = new LibraryServerImpl(numBooks, copiesPerBook, booksPerMember);
		LibraryServer stub = (LibraryServer) java.rmi.server.UnicastRemoteObject.exportObject(library, 0);
		Registry registry = LocateRegistry.createRegistry(port);
		registry.bind(libraryName, stub);
		List<String> poss = library.getBookListings();

		ArrayList<Thread> threads = new ArrayList<Thread>();

		MemberImpl member1 = new MemberImpl();
		Thread thread1 = new Thread(new Weirdo(member1, poss));
		threads.add(thread1);
		thread1.start();
/*
		int numThreads = 4;
		for(int i = 0; i < numThreads; i++){
			MemberImpl member = new MemberImpl();
			Thread thread = new Thread(new BasicClient(member, poss));
			threads.add(thread);
			thread.start();
		}

		for(int i = 0; i < threads.size(); i++){
			threads.get(i).join();
		}*/
		
		thread1.join();

	}
}
