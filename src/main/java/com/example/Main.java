package com.example;

import com.example.dao.UserDAO;
import com.example.dao.UserDAOImpl;
import com.example.entity.User;

import java.util.List;
import java.util.Scanner;

public class Main {

    private static final UserDAO userDAO = new UserDAOImpl();
    public static void main(String[] args) {

        try (Scanner scanner = new Scanner(System.in)) {
            while(true){

                System.out.println("\n1 - Create user");
                System.out.println("2 - Get user");
                System.out.println("3 - List users");
                System.out.println("4 - Update user");
                System.out.println("5 - Delete user");
                System.out.println("0 - Exit");

                int choice = scanner.nextInt();
                scanner.nextLine();

                switch(choice){

                    case 1:

                        System.out.print("Name: ");
                        String name = scanner.nextLine();

                        System.out.print("Email: ");
                        String email = scanner.nextLine();

                        System.out.print("Age: ");
                        int age = scanner.nextInt();

                        User user = new User(name, email, age);

                        userDAO.create(user);

                        break;

                    case 2:

                        System.out.print("User id: ");
                        Long id = scanner.nextLong();

                        System.out.println(userDAO.getById(id));

                        break;

                    case 3:

                        List<User> users = userDAO.getAll();

                        users.forEach(System.out::println);

                        break;

                    case 4:

                        System.out.print("User id: ");
                        Long uid = scanner.nextLong();
                        scanner.nextLine();

                        User u = userDAO.getById(uid);

                        if(u != null){

                            System.out.print("New name: ");
                            u.setName(scanner.nextLine());

                            userDAO.update(u);

                        }

                        break;

                    case 5:

                        System.out.print("User id: ");
                        Long did = scanner.nextLong();

                        userDAO.delete(did);

                        break;

                    case 0:

                        System.exit(0);
                }

            }
        }

    }

}