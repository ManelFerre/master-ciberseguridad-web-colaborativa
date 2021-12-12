package controllers;

import models.Constants;
import models.User;
import play.mvc.*;
import play.i18n.Messages;

import java.util.List;

public class Application extends Controller {

    private static void checkTeacher(){
        checkUser();

        User u = (User) renderArgs.get("user");
        if (!u.getType().equals(Constants.User.TEACHER)){
            return;
        }
    }

    private static void checkUser(){
        if (session.contains("username")){
            User u = User.loadUser(session.get("username"));
            if (u != null){
                renderArgs.put("user", u);
                return;
            }
        }
        Secure.login();
    }

    public static void index() {
        checkUser();

        User u = (User) renderArgs.get("user");

        if (u.getType().equals(Constants.User.TEACHER)){
            List<User> students = User.loadStudents();
            render("Application/teacher.html", u, students);
        }else{
            render("Application/student.html", u);
        }
    }


    public static void removeStudent(String student) {
        if (session.contains("username")){
           checkTeacher();
           User.remove(student);
           index();
        }
        flash.put("error", Messages.get("Acceso restringido"));
        Secure.login();  
    }


    public static void setMark(String student) {
        User u = User.loadUser(student);
        render(u);
    }

    public static void doSetMark(String student, Integer mark) {
        User u = User.loadUser(student);
        u.setMark(mark);
        u.save();
        index();
    }
}
