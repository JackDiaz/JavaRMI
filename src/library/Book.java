package library;

import java.io.Serializable;
import java.util.Random;

/**
 * The data object that holds a book details. Used for RMI communication.  
 * This class implementation is provided for you. 
 * You may play around with the text, author, and name of book to define them as you please.
 * You may add other functionality if needed.
 *
 */
public class Book implements Serializable {
	private static final long serialVersionUID = 433L;
	private static final String LOREM_IPSUM = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Quisque sapien ipsum, luctus quis orci non, vestibulum congue tellus. Morbi non diam diam. Pellentesque malesuada in justo vel interdum. Ut ornare sagittis hendrerit. Fusce feugiat bibendum mi, sit amet elementum felis malesuada elementum. Donec pulvinar urna id ultricies lobortis. Fusce vel commodo nulla.";
	private String name;
	private String author;
	private String text;

	public Book(String name) {
		this.name = name;
		author = "Author of " + name;
		text = "";
		generateText();
	}

	public String getName() {
		return name;
	}

	public String getAuthor() {
		return author;
	}

	public String getText(){
		return text;
	}

	private void generateText() {
		int randParagraphs = new Random().nextInt(50-5+1)+5; //between 5 and 50 paragraphs
		for(int i = 0; i < randParagraphs; i++){
			text += LOREM_IPSUM;
		}
	}

	public boolean equals(Object o){
		if (o instanceof Book)
			return this.name.equals(((Book)o).getName()); 
		else
			return false;

	}
}
