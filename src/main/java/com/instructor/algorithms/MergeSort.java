package com.instructor.algorithms;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class MergeSort {

    /**
     * Sorts a list of integers in ascending order using the merge sort algorithm.
     * This method serves as an entry point that initializes the recursive sorting
     * process.
     *
     * @param list The list of integers to be sorted. The list is sorted in-place.
     */
    public static <T extends Comparable<T>> void mergeSort(List<T> list, boolean ascending) {
        if (list.size() <= 1) {
            return; // Base case: a list of size 0 or 1 is already sorted
        }

        // Start the mergeSort helper function with additional parameters for indexing
        mergeSort(list, 0, list.size() - 1, ascending);
    }

    /**
     * Recursively divides the list into two halves until the base case of a
     * list of size 0 or 1 is reached. Then, the sorted halves are merged back
     * together in place.
     *
     * @param list  The list to be sorted
     * @param left  The starting index of the sub-list to be sorted
     * @param right The ending index of the sub-list to be sorted
     */
    private static <T extends Comparable<T>> void mergeSort(List<T> list, int left, int right, boolean ascending) {
        if (left < right) {
            int mid = (left + right) / 2;

            // Use CompletableFuture to parallelize sorting of the two halves
            CompletableFuture<Void> leftSort = CompletableFuture
                    .runAsync(() -> mergeSort(list, left, mid, ascending));
            CompletableFuture<Void> rightSort = CompletableFuture
                    .runAsync(() -> mergeSort(list, mid + 1, right, ascending));

            // Wait for both sides to be sorted before merging
            CompletableFuture<Void> allOf = CompletableFuture.allOf(leftSort, rightSort);
            try {
                allOf.get(); // Ensure both halves are sorted before proceeding
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }

            // Merge the sorted halves in place
            merge(list, left, mid, right, ascending);
        }
    }

    /**
     * Merges two contiguous sublists within the given list in place. The sublists
     * are defined by the indices [left, mid] and [mid+1, right]. This method
     * assumes that both sublists are already sorted and merges them into a single
     * sorted sublist.
     *
     * @param list  The list containing the sublists to be merged.
     * @param left  The starting index of the first sublist.
     * @param mid   The ending index of the first sublist, which is also one less
     *              than the starting index of the second sublist.
     * @param right The ending index of the second sublist.
     */
    private static <T extends Comparable<T>> void merge(List<T> list, int left, int mid, int right, boolean ascending) {
        int i = left; // Left sublist pointer
        int j = mid + 1; // Right sublist pointer

        // Iterate over the left and right sublists to merge
        while (i <= mid && j <= right) {
            // Determine if ascending or descending
            if ((ascending && list.get(i).compareTo(list.get(j)) <= 0)
                    || (!ascending && list.get(i).compareTo(list.get(j)) >= 0)) {
                i++; // Already sortedS
            } else {
                // Left > Right Ascending, Right > Left Descending
                T temp = list.get(j); // Store element
                list.remove(j); // Remove element
                list.add(i, temp); // Add right element to the left sublist

                i++;
                mid++;
                j++;
            }
        }
    }
}
