public class BubbleSortTest
{
	public static void main (String[] args)
	{
		int[] values = {5,1,32,65,7,4,23};
	
		System.out.println("Original Order");
		for (int element : values)
		System.out.print(element + " ");

		IntBubbleSorter.bubbleSort(values);

		System.out.println("\nSorted Order: ");
		for (int element : values)
			System.out.print(element + " ");

		System.out.println();
 	}

	private static void bubbleSort(int[] array)
	{
		int lastPos;
		int index;
		int temp;

		for (lastPos = array.length; lastPos >= 0; lastPos--)
		{
			
		}
	}
}
