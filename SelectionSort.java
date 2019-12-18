import java.io.File;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * 
 * @author Aimee Stuit (456737)
 * This class sorts a list of 196 countries/regions by means of selection sort
 *  and searches for a 'user-given' country/area by means of binary search
 * The class returns the index at which the 'user-given' country/area is found 
 *  and the class tells if the country/area could not be found
 *
 */
public class SelectionSort 
{
	public static void main(String[] args) throws Exception
	{
		//Here I read the file with countries and I add it into an arraylist
		File fileCountries = new File("C:/Users/aimee/eclipse-workspace/Assignment3/src/countries.txt");
		BufferedReader br = new BufferedReader(new FileReader(fileCountries));
		String readFile;
		ArrayList<String> countries = new ArrayList<String>();
		while((readFile= br.readLine())!=null)
		{
			countries.add(readFile);
		}
		
		//Now I convert the arraylist into an array, because this is easier to work with
		String[] countriesArray = new String[countries.size()];
		countriesArray = countries.toArray(countriesArray);
		
		//Here, the user can look for a certain country/area
		System.out.println("Which country/area are you looking for?");
		Scanner scan = new Scanner(System.in);
		String country = scan.nextLine();
		
		//Here, we call the method to sort the array of countries by means of selection sort
		String[] sortedCountriesArray = sort(countriesArray);
		
		//Here we search for the 'user-given' country/area
		if(search(sortedCountriesArray,country)==-1)
		{
			System.out.println("The country/area is not found in the list");
		}
		else 
		{
			System.out.println("The country/area is found at index:" + " " + search(sortedCountriesArray, country));
		}
	}

	/**
	 * Selection Sort
	 * @param a : The array of countries and areas which we have to sort by means of selection sort
	 * @return : The sorted array of countries and areas
	 */
	public static String[] sort(String[] a)
	{
		for(int i=0; i<a.length; i++)
		{
			int minPos = minimumPosition(a,i);
			swap(a,minPos,i);
		}
		return a;
	}

	/**
	 * 
	 * @param a : The array of countries and areas which we have to sort
	 * @param from : Is the index which we have to check if it should be placed before or after the position after 'from'
	 * @return : The current minimum position
	 */
	private static int minimumPosition(String[] a, int from)
	{
		int minPos = from;
		for(int i=from+1;i<a.length;i++)
		{
			if (a[i].compareTo(a[minPos])<0) // This is true if the string at index i should come before the string at index minPos
			{
				minPos=i;
			}
		}
		return minPos;
	}
	
	/**
	 * 
	 * @param a : The array of countries and areas which we have to sort
	 * @param i : Is one of the indices of which we have to swap the string
	 * @param j : Is one of the indices of which we have to swap the string
	 */
	public static void swap(String[] a, int i, int j)
	{
		String temp = a[i];
		a[i] = a[j];
		a[j] = temp;
	}
	
	/**
	 * Binary Search
	 * @param a : The sorted area of countries/areas
	 * @param stringWeSearchFor : The country/area where the user is looking for
	 * @return : The index at which the country/area where the user is looking for, is found
	 */
	public static int search(String[] a, String stringWeSearchFor)
	{
		int low = 0;
		int high = a.length-1;
		while(low<=high)
		{
			int mid = (low+high+1)/2;

			int compare = (stringWeSearchFor.replaceAll("[^a-zA-Z0-9]", "")).compareTo(a[mid].replaceAll("[^a-zA-Z0-9]", ""));
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
