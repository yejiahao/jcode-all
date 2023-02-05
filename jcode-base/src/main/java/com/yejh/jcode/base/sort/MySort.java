package com.yejh.jcode.base.sort;

import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;

/**
 * 数组排序算法
 *
 * @author <a href="mailto:yejh.1248@qq.com">Ye Jiahao</a>
 * @create 2023-02-05
 * @since 1.0.0
 */
@Slf4j
public class MySort {

    private static final int[] ARR = {5, 9, 6, 7, 3, 1, 8, 2, 4, 8, 0};

    private static void swap(int[] arr, int idxA, int idxB) {
        int temp = arr[idxA];
        arr[idxA] = arr[idxB];
        arr[idxB] = temp;
    }

    /**
     * 插入排序
     */
    public static void insertSort(int[] arr) {
        for (int i = 1, len = arr.length; i < len; i++) {
            for (int k = i; k > 0; k--) {
                if (arr[k] >= arr[k - 1]) break;
                swap(arr, k, k - 1);
            }
            log.info("i: {}, arr: {}", i, arr);
        }
        log.info("--------------------------------------------------------");
    }

    /**
     * 选择排序
     */
    public static void selectSort(int[] arr) {
        for (int i = 0, len = arr.length; i < len - 1; i++) {
            int minIdx = i;
            for (int k = i + 1; k < len; k++) {
                if (arr[k] < arr[minIdx]) {
                    minIdx = k;
                }
            }
            if (minIdx != i) {
                swap(arr, minIdx, i);
            }
            log.info("i: {}, arr: {}", i, arr);
        }
        log.info("--------------------------------------------------------");
    }

    /**
     * 冒泡排序
     */
    public static void bubbleSort(int[] arr) {
        for (int i = 1, len = arr.length; i < len; i++) {
            boolean alreadySorted = true;
            for (int k = 0; k < len - i; k++) {
                if (arr[k] > arr[k + 1]) {
                    alreadySorted = false;
                    swap(arr, k, k + 1);
                }
            }
            if (alreadySorted) {
                break;
            }
            log.info("i: {}, arr: {}", i, arr);
        }
        log.info("--------------------------------------------------------");
    }

    /**
     * 快速排序
     */
    public static void quickSort(int[] arr) {
        recursiveQuickSort(arr, 0, arr.length - 1);
        log.info("--------------------------------------------------------");
    }

    private static void recursiveQuickSort(int[] arr, int min, int max) {
        if (min > max) return;
        int midVal = arr[min];
        int left = min;
        int right = max;
        while (left < right) {
            while (left < right && arr[right] >= midVal) right--;
            arr[left] = arr[right];
            while (left < right && arr[left] <= midVal) left++;
            arr[right] = arr[left];
        }
        int mid = left;
        arr[mid] = midVal;
        log.info("mid: {}, arr: {}", mid, arr);
        recursiveQuickSort(arr, min, mid - 1);
        recursiveQuickSort(arr, mid + 1, max);
    }

    /**
     * 归并排序
     */
    public static void mergeSort(int[] arr) {
        recursiveMergeSort(arr, 0, arr.length - 1);
        log.info("--------------------------------------------------------");
    }

    private static void recursiveMergeSort(int[] arr, int min, int max) {
        if (min == max) return;
        int mid = min + max >>> 1;
        recursiveMergeSort(arr, min, mid);
        recursiveMergeSort(arr, mid + 1, max);

        int[] tempArr = new int[max - min + 1];
        int tempIdx = 0;

        int p = min;
        int q = mid + 1;
        while (p <= mid && q <= max) {
            if (arr[p] <= arr[q]) {
                tempArr[tempIdx++] = arr[p++];
            } else {
                tempArr[tempIdx++] = arr[q++];
            }
        }
        while (p <= mid) {
            tempArr[tempIdx++] = arr[p++];
        }
        while (q <= max) {
            tempArr[tempIdx++] = arr[q++];
        }

        for (int i = 0, len = tempArr.length; i < len; i++) {
            arr[i + min] = tempArr[i];
        }
        log.info("mid: {}, arr: {}", mid, arr);
    }

    public static void main(String[] args) {
        int[] arr1 = Arrays.copyOf(ARR, ARR.length);
        int[] arr2 = Arrays.copyOf(ARR, ARR.length);
        int[] arr3 = Arrays.copyOf(ARR, ARR.length);
        int[] arr4 = Arrays.copyOf(ARR, ARR.length);
        int[] arr5 = Arrays.copyOf(ARR, ARR.length);
        insertSort(arr1);
        selectSort(arr2);
        bubbleSort(arr3);
        quickSort(arr4);
        mergeSort(arr5);
    }
}
