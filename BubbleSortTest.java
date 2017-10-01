public class BubbleSortTest
{
	public static void main (String[] args)
	{
		int[] values = {5,1,32,65,7,4,-4,23};
	
		System.out.println("Original Order");
		for (int element : values)
			System.out.print(element + " ");

		selectionSort(values);

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
			for (index = 0; index < lastPos - 1; index++)
			{
				if (array[index] > array[index+1])
				{
					temp = array[index];
					array[index] = array[index + 1];
					array[index + 1] = temp;
				}
			}
		}
	}

	//private static int findLowestNumber(int[] array)
	//{
	//	int lowest = Integer.MAX_VALUE;
	//	for (int x = 0;
	//	
	//}

	private static void selectionSort(int[] array)
	{
		int first_index = 0;
		int lowest = Integer.MAX_VALUE;
		int lowest_index = 0;
		int temp = 0;

		while (first_index != array.length + 1)
		{
			for (int x = first_index; x < array.length; x++)
			{
				if ( array[x] < lowest )
				{
					lowest = array[x];
					lowest_index = x;
				}
			}
			if (lowest != Integer.MAX_VALUE)
			{
				temp = array[first_index];
				array[first_index] = lowest;
				array[lowest_index] = temp;
				lowest = Integer.MAX_VALUE;
				lowest_index = 0;
				first_index++;
			}
			else 
			{
				first_index++;
				continue;
			}
		}
	}
}
