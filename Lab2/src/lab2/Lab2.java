package lab2;

import java.util.Random;
import java.util.Scanner;
import javax.swing.JOptionPane;

public class Lab2 {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        
        System.out.println("=============================================");
        //Nhập tham số cho phương trình ax2 + bx + c = 0
        System.out.print("Nhập a: ");
        float a = sc.nextFloat();
        System.out.print("Nhập b: ");
        float b = sc.nextFloat();
        System.out.print("Nhập c: ");
        float c = sc.nextFloat();
        giaiPTBac2(a, b, c);
        
        Random r = new Random();
          
        System.out.println("=============================================");
        //Nhập số phần tử cho mảng phát sinh ngẫu nhiên
        System.out.print("Nhập số phần tử của mảng: ");
        int n = sc.nextInt();
        int x[] = new int[n];
        //Xuất ra màn hình mảng phát sinh ngẫu nhiên
        for(int i = 0; i < n; i++){
            x[i] = r.nextInt(100);
            System.out.println("x[" + (i+1) + "] = " + x[i]);
        }
        //Xuất ra màn hình dãy các phần tử là số nguyên tố có trong mảng
        System.out.print("Dãy số nguyên tố trong mảng là: ");
        for(int i = 0; i < n; i++)
        {
            if(ktSNT(x[i]))
                System.out.println(x[i]);
        }
          
        System.out.println("=============================================");
        //Nhập số dòng, số cột cho ma trận phát sinh ngẫu nhiên
        System.out.print("Nhập số dòng cho ma trận: ");
        int dong = sc.nextInt();
        System.out.print("Nhập số cột cho ma trận: ");
        int cot = sc.nextInt();
        int y[][] = new int[dong][cot];        
        //Nhập vào dong thu k
        System.out.print("Nhập vào dòng thứ k: ");
        int k = sc.nextInt();
        int sum = 0;
        //Xuất ra màn hình ma trận phát sinh ngẫu nhiên
        System.out.println("Ma trận ngẫu nhiên:");
        for(int i = 0; i < dong; i++)
        {
            for(int j = 0; j < cot; j++)
            {
                //Phát sinh ngẫu nhiên các phần tử của ma trận
                y[i][j] = r.nextInt(50);
                System.out.print(y[i][j] + "  ");
                //Tính tổng trên dòng thứ k
                if(i == k)
                {
                    y[i][j] = y[k][j];
                    sum += y[i][j];
                }
            }
            System.out.println();
        }
        //Xuất ra màn hình tổng các phần tử trên dòng thứ k của ma trận
        System.out.println("Tổng các phần tử trên dòng thứ k: " + sum);
          
        System.out.println("=============================================");
        //Nhập một chuỗi
        System.out.print("Nhập vào một chuỗi ký tự: ");
        String chuoi = sc.nextLine();
        String upperCase = "";
        Scanner scanChuoi = new Scanner(chuoi);
        while(scanChuoi.hasNext())
        {
            String nextWord = scanChuoi.next();
            upperCase += Character.toUpperCase(nextWord.charAt(0)) + nextWord.substring(1) + " ";
        }
        System.out.print(upperCase.trim() + "\n");
        
        System.out.println("=============================================");
        //Try... catch...
        int m;
        try {
            m = Integer.parseInt(JOptionPane.showInputDialog(null, "Nhập số nguyên m: ", ""));
        } catch (java.lang.NumberFormatException e) {
            System.out.println(e.toString());
        }
        finally{
            System.out.println("Good morning!!!");
        }
    }
    
    public static void giaiPTBac2(float a, float b, float c) {
        if (a == 0) {
            if (b == 0) {
                System.out.println("Phương trình vô số nghiệm!");
            } else {
                System.out.println("Phương trình có một nghiệm: "
                        + "x = " + (-c / b));
            }
            return;
        }
        
        float delta = b*b - 4*a*c;
        float x1;
        float x2;

        if (delta > 0) {
            x1 = (float) ((-b + Math.sqrt(delta)) / (2*a));
            x2 = (float) ((-b - Math.sqrt(delta)) / (2*a));
            System.out.println("Phương trình có 2 nghiệm là: "
                    + "x1 = " + x1 + " và x2 = " + x2);
        } else if (delta == 0) {
            x1 = (-b / (2 * a));
            System.out.println("Phương trình có nghiệm kép: "
                    + "x1 = x2 = " + x1);
        } else {
            System.out.println("Phương trình vô nghiệm!");
        }
    }
    
    public static boolean ktSNT(int n)
    {
        if (n < 2)
            return false;
        for(int i = 2; i <= Math.sqrt(n); i++)
        {
            if(n % i == 0)
                return false;
        }
        return true;
    }
}
