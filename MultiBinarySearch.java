import java.io.File;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * 
 * @author Aimee Stuit (456737)
 * This class sorts a file of 466.544 Strings by means of merge sort.
 * Next, the class searches for a certain string by means of Multi-Thread Binary Search and Single-Thread Binary Search
 * Lastly, the class compares the performances of both binary searches by means of comparison of the elapsed nanotime
 *
 */
public class MultiBinarySearch {

	public static long elapsedTimeSingleThreadSearch;
	public static long elapsedTimeMultiThreadSearch;
	public static boolean needleInArray;

	public static void main(String[] args) throws Exception
	{
		//Here I read the file of words and put them in an arraylist
		File fileWords = new File("C:/Users/aimee/eclipse-workspace/Assignment3/src/words.txt");
		BufferedReader br = new BufferedReader(new FileReader(fileWords));
		String readFile;
		ArrayList<String> words = new ArrayList<String>();
		while((readFile= br.readLine())!=null)
		{
			words.add(readFile);
		}
		
		//Here I convert the arraylist to an array, because I find this easier to work with
		String[] wordsArray = new String[words.size()];
		wordsArray = words.toArray(wordsArray);	
		
		//Here the array of words is sorted by means of the method 'sort' which uses merge sort
		sort(wordsArray);
		
		//Here I ask the user which word they are looking for
		System.out.println("Which word are you looking for?");
		Scanner scan = new Scanner(System.in);
		String word = scan.nextLine();
		
		//First we use single-thread binary search and calculate the elapsed time
		long startTime2 = System.nanoTime();
		if(binarySearch(wordsArray,word)==-1) 
		{
			System.out.println("The needle" + " " + "'"+word+"'" + " " + "is not found");
			
		}
		else
		{
			System.out.println("The needle" + " " + "'"+word+"'"+ " " + "is found at index:" + " " +binarySearch(wordsArray,word) + " " + "with single-thread binary search");
			long stopTime2 = System.nanoTime();
			elapsedTimeSingleThreadSearch = stopTime2 - startTime2;
		    System.out.println("The elapsed time with single-thread equals:" + " " + elapsedTimeSingleThreadSearch);
		}
		
	    //Next, we use multi-thread binary search and calculate the elapsed time
	    multiSearch(wordsArray, word);	
	    }

	/**
	 * Merge Sort: This method sorts the file of words by means of merge sort
	 * @param a: The array of words that needs to be sorted
	 */
	public static void sort(String[] a)
	{
		if(a.length<=1)
		{
			return;
		}
		String[] first = new String[a.length/2];
		String[] second = new String[a.length-first.length];
		
		for(int i=0;i<first.length;i++)
		{
			first[i]=a[i];
		}
		for(int j=0;j<second.length;j++)
		{
			second[j]=a[first.length+j];
		}
		sort(first);
		sort(second);
		merge(first,second,a);
	}	
	
	public static void merge(String[] first, String[] second, String[] a)
	{
		int nextFirst=0;
		int nextSecond=0;
		int nextA=0;
		
		while(nextFirst<first.length && nextSecond<second.length)
		{
			if((first[nextFirst]).compareTo(second[nextSecond])<0)
			{
				a[nextA]=first[nextFirst];
				nextFirst++;
			}
			else
			{
				a[nextA] = second[nextSecond];
				nextSecond++;
			}
			nextA++;
		}
		while(nextFirst<first.length)
		{
			a[nextA]=first[nextFirst];
			nextFirst++;
			nextA++;
		}
		while(nextSecond<second.length)
		{
			a[nextA]=second[nextSecond];
			nextSecond++;
			nextA++;
		}
	}
	
	/**
	 * This method searches for the 'needle' by means of multi binary search.
	 * It first splits the array into a certain number of parts and then performs binary search on every part seperately
	 * @param a : The sorted array in which we have to look for the needle
	 * @param needle : The string for which we are looking
	 */
	public static void multiSearch(String[] a, String needle)
	{
		ArrayList<String> sortedWordsList = new ArrayList<String>(Arrays.asList(a));
		int numberOfParts = 10000;
		ArrayList<ArrayList<String>> choppedList = choppedArrayList(sortedWordsList, numberOfParts); // Here we are calling the method which splits the array in a certain number of parts
		int numberOfElementsPerArray = sortedWordsList.size()/numberOfParts;
				
		//Here we take each part of the list separately and then use binary search on each part separately 
		// until we have found the needle or it ends because the needle is not in the list
		for(int i=0; i<choppedList.size(); i++)
		{
			int elementsPrior = (i*numberOfElementsPerArray);
			String[] choppedListArray = new String[choppedList.get(i).size()];
			choppedListArray =choppedList.get(i).toArray(choppedListArray);
			multiThreading task = new multiThreading(choppedListArray, needle, elementsPrior, elapsedTimeSingleThreadSearch);
			task.start();
		}
	}
	
	/**
	 * This is the method which chops the list of words into 'numberOfParts' number of lists
	 * @param list : Is the big list of all words which we have to chop in a certain number of parts
	 * @param numberOfParts : Is the number of partial lists we want to end up with
	 * @return : The Big Array List which contains of all partial Array Lists
	 */
	public static <T>ArrayList<ArrayList<T>> choppedArrayList(ArrayList<T> list, final int numberOfParts)
	{
		ArrayList<ArrayList<T>> wordListParts = new ArrayList<ArrayList<T>>();
		final int m = list.size();
		int n = list.size()/numberOfParts;
		for(int i=0; i<m; i+=n)
		{
			wordListParts.add(new ArrayList<T>(list.subList(i, Math.min(m,i+n))));
		}
		return wordListParts;
	}
	
	/**
	 * Here we perform the single-thread binary search. 
	 * @param a: The sorted array in which we have to look for the needle
	 * @param needle : The string for which we are looking 
	 * @return : The index at which the string is found
	 */
	public static int binarySearch(String[] a, String needle)
	{
		int low = 0;
		int high = a.length-1;
		while(low<=high)
		{
			int mid = (low+high+1)/2;

			int compare = (needle.compareTo(a[mid]));
			if(compare==0)
			{
				return mid;
			}
			else if(compare>0)
			{
				low = mid+1;
			}
			else
			{
				high = mid-1;
			}
		}
		return -1;
	}
}

/**
 * 
 * @author Aimee Stuit (456737)
 * This is a seperate class which extends the Thread class. Every time I call a new element on this class, 
 * 	this creates a task. All tasks are performed at the same time 
 * 	as I add as many threads as there are 'chopped lists' to this class in a loop. 
 * In this case: 10.000 threads are made at the same time, and they are all being searched for the needle in a binarysearch at the same time
 * 
 *
 */
class multiThreading extends Thread
{
	String[] choppedArray;
	String needle;
	int elementsPrior;
	long timeSingleThread;
	
	/**
	 * 
	 * @param choppedArray: this is the chopped array which is being searched for. Each chopped array is a thread
	 * @param needle: this is the string where we are looking for
	 * @param elementsPrior: this is the number of words that come before the needle
	 * @param timeSingleThread: this is the elapsed time in the single-thread binary search
	 */
	public multiThreading(String[] choppedArray, String needle, int elementsPrior, long timeSingleThread)
	{
		this.choppedArray = choppedArray;
		this.needle = needle;
		this.elementsPrior = elementsPrior;
		this.timeSingleThread = timeSingleThread;

	}
	/**
	 * Here we start each thread and we take actions on each thread at the same time
	 */
	public void run()
	{
		long startTime = System.nanoTime();			//We may start the time here, as it starts with the first thread
		if(binarySearch(choppedArray, needle)!=-1) 	//This is the case if the needle is found in partial list i
		{
			long stopTime = System.nanoTime();		//Once we have found the needle, we may stop the time
	
			int foundIt = elementsPrior + binarySearch(choppedArray, needle);
			System.out.println("The needle" + " " + "'"+needle+"'"+ " " + "is found at index:" + " " + foundIt + " " + "with multi-thread binary search");
			long timeMultiThread = stopTime - startTime;
			System.out.println("The elapsed time with multi-threads equals:" + " " + timeMultiThread);
			
			//Here, we calculate the improvement of the multi-thread search in comparison with the single-thread search in terms of percentage
			double improvement = (double)(timeSingleThread-timeMultiThread)/(timeMultiThread+timeSingleThread);
		    System.out.println(" ");
		    System.out.println("The elapsed time improved with" + " " + (improvement*100)+"%" + " " + "with the use of Multi-Thread Binary search");
		}
	}
	
	public static int binarySearch(String[] a, String needle)
	{
		{
			int low = 0;
			int high = a.length-1;
			while(low<=high)
			{
				int mid = (low+high+1)/2;

				int compare = (needle.compareTo(a[mid]));
				if(compare==0)
				{
					return mid;
				}
				else if(compare>0)
				{
					low = mid+1;
				}
				else
				{
					high = mid-1;
				}
			}
			return -1;
		}
	}
}

