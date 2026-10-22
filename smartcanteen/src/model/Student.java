
package model;

public class Student extends User {

    public Student(int userId, String name, String email,
                   String password) {

        super(userId, name, email, password, "STUDENT");
    }
}
