/* WORD LADDER Main.java
 * EE422C Project 3 submission by
 * Replace <...> with your actual data.
 * <Paawan Desai>
 * <pkd397>
 * <17140>>
 * <Student2 Name>
 * <Student2 EID>
 * <Student2 5-digit Unique No.>
 * Slip days used: <0>
 * Git URL:
 * Spring 2022
 */


package assignment3;
import java.util.*;
import java.io.*;

public class Main {

	// static variables and constants only here.
	static Set<String> dict;
	static char[] alpha;
	static Map<String, Node> wordmap;
	static boolean firstdfs;

	public static void main(String[] args) throws Exception {

		Scanner kb;	// input Scanner for commands
		PrintStream ps;	// output file, for student testing and grading only
		// If arguments are specified, read/write from/to files instead of Std IO.
		if (args.length != 0) {
			kb = new Scanner(new File(args[0]));
			ps = new PrintStream(new File(args[1]));
			System.setOut(ps);			// redirect output to ps
		} else {
			kb = new Scanner(System.in);// default input from Stdin
			ps = System.out;			// default output to Stdout
		}
		initialize();

		// TODO methods to read in words, output ladder
		ArrayList<String> startend = parse(kb);
		ArrayList<String> ladderBFS = getWordLadderBFS(startend.get(0), startend.get(1));
		startend=parse(kb);
		ArrayList<String> ladderDFS = getWordLadderDFS(startend.get(0), startend.get(1));
		printLadder(ladderDFS);
	}

	public static void initialize() {
		// initialize your static variables or constants here.
		// We will call this method before running our JUNIT tests.  So call it 
		// only once at the start of main.
		dict = makeDictionary();
		alpha = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
		firstdfs = false;
		wordmap = new HashMap<String, Node>();
	}

	/**
	 * @param keyboard Scanner connected to System.in
	 * @return ArrayList of Strings containing start word and end word. 
	 * If command is /quit, return null. 
	 */
	public static ArrayList<String> parse(Scanner keyboard) {
		// TO DO
		ArrayList<String> startend = new ArrayList<String>();
		String input1 = keyboard.next();
		String start;
		String end;
		if(input1.equals("/quit")) {
			start = input1;
			end = "end";
			startend.add(start);
			startend.add(end);
			return startend;
		}
		else {
			start = input1;
			end = keyboard.next();
			startend.add(start);
			startend.add(1, end);
		}
		return startend;
	}

	public static ArrayList<String> getWordLadderDFS(String start, String end) {

		// Returned list should be ordered start to end.  Include start and end.
		// If ladder is empty, return list with just start and end.
		// TODO some code
		if(start.equals("/quit")) {
			return null;
		}
		start = start.toUpperCase();
		end = end.toUpperCase();
		ArrayList<String> ladder = new ArrayList<String>();
		Queue<Node> wordqueue = new LinkedList<>();
		Node currentNode = new Node();
		String newword = null;
		if(!firstdfs) {
			currentNode = new Node(start, null, 0);
			wordmap.put(start, currentNode);
			firstdfs=true;
		}
		if(wordmap.containsKey(end)) {
			Node endnode = wordmap.get(end);
			while(endnode.len!=0) {
				String endword = endnode.word;
				ladder.add(endnode.word);
				endnode = endnode.parent;
			}
			ladder.add(endnode.word);
			return ladder;
		}
		neighbors(wordqueue, start, end);
		while(!wordqueue.isEmpty()) {
			Node newcurrentNode = wordqueue.remove();
			ArrayList<String> result = getWordLadderDFS(newcurrentNode.word, end);
			if(result.size()!=0 && result.get(0).equals(end)) {
				return ladder;
			}
		}
		if(wordqueue.isEmpty()) {
			ArrayList<String> result = new ArrayList<>();
			ladder.add(start);
			ladder.add(end);
		}
		return ladder;
	}

	public static ArrayList<String> getWordLadderBFS(String start, String end) {

		// TODO some code

		return null; // replace this line later with real return
	}


	public static void printLadder(ArrayList<String> ladder) {
		if(ladder==null) 
			return;
		if(ladder.size()==2) {
			System.out.println("no word ladder can be found between " + ladder.get(0).toLowerCase() + "and " + ladder.get(ladder.size()-1).toLowerCase() + ".");
		}
		else {
			System.out.println("a " + (ladder.size() - 2) + "-rung word ladder exists between " + ladder.get(ladder.size() - 1).toLowerCase() + " and " + ladder.get(0).toLowerCase() + ".");
			for (int i = 0; i < ladder.size(); i++) {
				System.out.println(ladder.get((ladder.size() - 1) - i).toLowerCase());
			}
		}

	}
	// TODO
	// Other private static methods here
	public static void neighbors(Queue<Node> queue, String start, String end) {
		Queue<Node> zero = new LinkedList<>();
		Queue<Node> one = new LinkedList<>();
		Queue<Node> two = new LinkedList<>();
		Queue<Node> three = new LinkedList<>();
		Queue<Node> four = new LinkedList<>();
		Queue<Node> five = new LinkedList<>();
		char [] currentwords = start.toCharArray();

		for(int i=0;i<5;i++) {
			char old = currentwords[i];
			for(int j=0;j<alpha.length;j++) {
				String newstr = new String(currentwords).toUpperCase();
				if(dict.contains(newstr) && !wordmap.containsKey(newstr)) {
					Node newnode = new Node(newstr, wordmap.get(start), wordmap.get(start).len+1);
					wordmap.put(newstr, newnode);
					int let=0;
					for(int a=0;a<5;a++) {
						if(newstr.charAt(a)==end.charAt(a)) {
							let++;
						}
					}
					if(let==5) {
						five.add(newnode);
					}
					else if(let==4) {
						four.add(newnode);
					}
					else if(let==3) {
						three.add(newnode);
					}
					else if(let==2) {
						two.add(newnode);
					}
					else if(let==1) {
						one.add(newnode);
					}
					else  {
						zero.add(newnode);
					}
				}
			}
			currentwords[i]=old;
		}
		queue.addAll(five);
		queue.addAll(four);
		queue.addAll(three);
		queue.addAll(two);
		queue.addAll(one);
		queue.addAll(zero);
	}


	/* Do not modify makeDictionary */
	public static Set<String>  makeDictionary () {
		Set<String> words = new HashSet<String>();
		Scanner infile = null;
		try {
			infile = new Scanner (new File("five_letter_words.txt"));
		} catch (FileNotFoundException e) {
			System.out.println("Dictionary File not Found!");
			e.printStackTrace();
			System.exit(1);
		}
		while (infile.hasNext()) {
			words.add(infile.next().toUpperCase());
		}
		return words;
	}
}
