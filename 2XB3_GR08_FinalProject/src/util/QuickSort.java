package util;

import java.util.List;

/**
 * @brief QuickSort function, edited to also work for Lists. Taken from Algorithms textbook.
 * @author Kenneth Mak - makk4
 *
 */
public class QuickSort {
	public static <T extends Comparable<T>> void sort(T[] a) {
		sort(a, 0, a.length-1);
	}
	
	private static <T extends Comparable<T>> void sort(T[] a, int lo, int hi) {
		if (hi <= lo) return;
		int j = partition(a, lo, hi);
		sort(a, lo, j-1);
		sort(a, j+1, hi);
	}
	
	private static <T extends Comparable<T>> int partition(T[] a, int lo, int hi) {
		int i = lo; int j = hi+1;
		
		T v = a[lo];
		while(true){
			while(a[++i].compareTo(v) < 0) if (i == hi) break;
			while(v.compareTo(a[--j]) < 0) if (j == lo) break;
			if (i >= j) break;
			
			T temp = a[i];
			a[i] = a[j];	
			a[j] = temp;
		}

		T temp = a[lo];
		a[lo] = a[j];
		a[j] = temp;
		return j;
	}

	
	public static <T extends Comparable<T>> void sort(List<T> a) {
		sort(a, 0, a.size()-1);
	}
	
	private static <T extends Comparable<T>> void sort(List<T> a, int lo, int hi) {
		if (hi <= lo) return;
		int j = partition(a, lo, hi);
		sort(a, lo, j-1);
		sort(a, j+1, hi);
	}
	
	private static <T extends Comparable<T>> int partition(List<T> a, int lo, int hi) {
		int i = lo; int j = hi+1;
		
		T v = a.get(lo);
		while(true){
			while(a.get(++i).compareTo(v) < 0) if (i == hi) break;
			while(v.compareTo(a.get(--j)) < 0) if (j == lo) break;
			if (i >= j) break;
			
			T temp = a.get(i);
			a.set(i, a.get(j));	
			a.set(j, temp);
		}

		T temp = a.get(lo);
		a.set(lo, a.get(j));	
		a.set(j, temp);
		
		return j;
	}
	
	
}
