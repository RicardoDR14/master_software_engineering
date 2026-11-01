package pt.isec.a2022147797;

import java.util.Arrays;
import java.util.List;

public class Ex3 {
    public static void main(String[] args) {
        System.out.println("Enter the size of the array: ");
        int n = Integer.parseInt(new java.util.Scanner(System.in).nextLine());
        int[] array = new int[n];

        for (int i = 0; i < n; i++) {
            System.out.println("Enter next element ["+i+"]: ");
            array[i] = Integer.parseInt(new java.util.Scanner(System.in).nextLine());
        }

        System.out.println("Array:");
        printArray(array);

        System.out.println("Smallest: " + smallest(array));
        System.out.println("Largest: " + largest(array));
        System.out.println("Sum: " + sum(array));
        System.out.println("Average: " + average(array));

        reverse(array);
        System.out.println("Reversed array:");
        printArray(array);

        System.out.println("Elements greater than average: " + Arrays.toString(greaterThanAverage(array)));
    }

    private static void reverse(int[] array) {
        for (int i = 0; i < array.length / 2; i++) {
            int temp = array[i];
            array[i] = array[array.length - 1 - i];
            array[array.length - 1 - i] = temp;
        }
    }

    private static int sum(int[] array) {
        int sum = 0;
        for (int j : array) {
            sum += j;
        }
        return sum;
    }
    private static int[] greaterThanAverage(int[] array) {
        int sum = 0;
        for (int num : array) {
            sum += num;
        }

        int average = sum / array.length;

        int count = 0;
        for (int num : array) {
            if (num > average) {
                count++;
            }
        }

        int[] result = new int[count];
        int index = 0;

        for (int num : array) {
            if (num > average) {
                result[index++] = num;
            }
        }
        return result;
    }

    private static int average(int[] array) {
        return sum(array) / array.length;
    }

    private static int largest(int[] array) {
        int largest = array[0];
        for (int i = 1; i < array.length; i++) {
            if (array[i] > largest) {
                largest = array[i];
            }
        }
        return largest;
    }

    private static int smallest(int[] array) {
        int smallest = array[0];
        for (int i = 1; i < array.length; i++) {
            if (array[i] < smallest) {
                smallest = array[i];
            }
        }
        return smallest;
    }

    private static void printArray(int[] array) {
        for (int j : array) {
            System.out.print(j + " ");
        }
        System.out.println();
    }
}
