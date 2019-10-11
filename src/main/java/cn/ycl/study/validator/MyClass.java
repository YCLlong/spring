package cn.ycl.study.validator;

import java.util.List;

public class MyClass {
    private String name;
    private Integer no;
    List<Person> studentList;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getNo() {
        return no;
    }

    public void setNo(Integer no) {
        this.no = no;
    }

    public List<Person> getStudentList() {
        return studentList;
    }

    public void setStudentList(List<Person> studentList) {
        this.studentList = studentList;
    }
}
