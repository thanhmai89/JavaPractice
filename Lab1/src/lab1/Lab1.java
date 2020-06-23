/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lab1;

import java.util.Scanner;

/**
 *
 * @author Administrator
 */
public class Lab1 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
//        b3();
        b7();
//        extra();
    }

    static void b3() {
        SinhVien sv1 = new SinhVien();
        sv1.printInfor();
    }

    static void b7() {
        MyArr myArr = new MyArr();

        myArr.printPrime();
        
        myArr.printMax();
        
        System.out.println("\nSo chinh phuong trong mang: " + myArr.countSquare());
        
        myArr.sort(true);
        
        System.out.println("\nMang sap xep tang dan: ");
        myArr.print();
    }
    
    static void extra() {
        Scanner sc = new Scanner(System.in);
        int n = 0;
        int x = 0;
        do {
            System.out.print("Nhap n > 0: ");   
            n = sc.nextInt();
        }while(n <= 0);
        System.out.print("Nhap x: ");   
        x = sc.nextInt();
        float s = 0.0f;
        for (int i = 1; i <= n; i++) {
            s -= (Math.pow(-1, (i + 1)) * factorial(i))/Math.pow(x, i);
        }
        System.out.println("Res = " + s);
        sc.close();
    }
    
    static double factorial(int number) {
        double res = 1.0;
        for (int i = number; i > 1; i--) {
            res *= i;
        }
        return res;
    }
}

class MyArr {

    int arr[];

    public MyArr() {
        Scanner sc = new Scanner(System.in);
        int arrLength;
        System.out.print("Nhap so luong phan tu mang: ");
        arrLength = sc.nextInt();
        arr = new int[arrLength];

        for (int i = 0; i < arrLength; i++) {
            System.out.print("Nhap phan tu thu [" + (i + 1) + "]: ");
            arr[i] = sc.nextInt();
        }

        System.out.print("Mang: ");
        for (int i = 0; i < arrLength; i++) {
            System.out.print(arr[i] + " ");
        }
        sc.close();
    }

    public void print() {
        System.out.print("Mang: ");
        for (int i = 0; i < arr.length; i++) {
            System.out.print(arr[i] + " ");
        }
    }
    
    public void printPrime() {
        System.out.print("\nSo nguyen to trong mang: ");
        for (int i = 0; i < arr.length; i++) {
            if (isPrime(arr[i])) {
                System.out.print(arr[i] + " ");
            }
        }
    }

    public void printMax() {
        int max = arr[0];

        for (int i = 0; i < arr.length; i++) {
            if (max < arr[i]) {
                max = arr[i];
            }
        }

        System.out.print("\nSo lon nhat trong mang: " + max);
    }

    public int countSquare() {
        int n = 0;
        for (int i = 0; i < arr.length; i++) {
            if (isSquare(arr[i])) {
                n++;
            }
        }
        return n;
    }

    public static boolean isSquare(int number) {
        return (Math.sqrt(number) * Math.sqrt(number) == number);
    }

    public static boolean isPrime(int number) {
        if (number < 2) {
            return false;
        }
        int u = 0;
        for (int i = 2; i <= Math.sqrt(number); i++) {
            if (number % i == 0) {
                u++;
            }
        }
        return u >= 1 ? false : true;
    }
    
    public void sort(boolean inc) {
        int temp;
        for (int i = 0; i < arr.length; i++) {
            for (int j = i + 1; j < arr.length; j++) {
                if (inc) {
                    if (arr[i] > arr[j]) {
                        temp = arr[i];
                        arr[i] = arr[j];
                        arr[j] = temp;
                    }
                } else {
                    if (arr[i] < arr[j]) {
                        temp = arr[i];
                        arr[i] = arr[j];
                        arr[j] = temp;
                    }
                }
            
            }
        }
    }

}

class SinhVien {

    public String name;
    public String id;
    public int birthYear;
    public float gpa;

    public SinhVien(String name, String id, int birthYear, float gpa) {
        this.name = name;
        this.id = id;
        this.birthYear = birthYear;
        this.gpa = gpa;
    }

    public SinhVien() {
        Scanner sc = new Scanner(System.in);
        System.out.print("Nhap mssv: ");
        this.id = sc.nextLine();
        System.out.print("Nhap ten: ");
        this.name = sc.nextLine();
        System.out.print("Nhap nam sinh: ");
        this.birthYear = sc.nextInt();
        System.out.print("Nhap diem trung binh: ");
        this.gpa = sc.nextFloat();
        sc.close();
    }

    public void printInfor() {
        System.out.println("Mssv: " + this.id);
        System.out.println("Ten: " + this.name);
        System.out.println("Tuoi: " + (2020 - this.birthYear));
        System.out.println("Diem trung binh: " + this.gpa);

    }

}
