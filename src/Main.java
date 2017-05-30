
import java.util.ArrayList;
import java.util.Collections;
import java.util.stream.IntStream;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Admin
 */
public class Main {

    public static void main(String args[]) {

        ArrayList<Employee> al = new ArrayList<Employee>();

        al.add(new Employee(101, "Peter", 23));

        al.add(new Employee(106, "Marry", 29));

        al.add(new Employee(105, "John", 21));
        System.out.println(al.contains(new Employee(101, "Peter", 23)));

//Sắp xếp list employee
        Collections.sort(al);

        for (Employee st : al) {
            System.out.println(st.id + " " + st.name + " " + st.age);

        }

//        map.entrySet()
//                .parallelStream()
//                .forEach(entry -> System.out.println(entry.getKey() + ":" + entry.getValue()));
        System.setProperty("java.util.concurrent.ForkJoinPool.common.parallelism", "500");
        long start = System.currentTimeMillis();
        IntStream s = IntStream.range(0, 500);
//System.setProperty("java.util.concurrent.ForkJoinPool.common.parallelism", "20");
        s.parallel().forEach(i -> {
            try {
                Thread.sleep(100);
            } catch (Exception ignore) {
            }
            System.out.print((System.currentTimeMillis() - start) + " ");
        });
    }

}

class Employee implements Comparable<Employee> {

    int id;

    String name;

    int age;

    Employee(int id, String name, int age) {

        this.id = id;

        this.name = name;

        this.age = age;

    }

    public int compareTo(Employee employee) {

        if (age == employee.age) {
            return 0;
        } else if (age > employee.age) {
            return 1;
        } else {
            return -1;
        }

    }

}
