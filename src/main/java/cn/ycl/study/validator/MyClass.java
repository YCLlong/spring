package cn.ycl.study.validator;

import java.util.List;
import java.util.Map;

public class MyClass {
    private String name;
    private Integer no;
    private List<Person> studentList;
    private Person teacher;
    private Map<String,Person> committee;

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

    public Person getTeacher() {
        return teacher;
    }

    public void setTeacher(Person teacher) {
        this.teacher = teacher;
    }

    public Map<String, Person> getCommittee() {
        return committee;
    }

    public void setCommittee(Map<String, Person> committee) {
        this.committee = committee;
    }
}
