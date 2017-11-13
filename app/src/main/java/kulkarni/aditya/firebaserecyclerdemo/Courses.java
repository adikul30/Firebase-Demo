package kulkarni.aditya.firebaserecyclerdemo;

/**
 * Created by adicool on 13/11/17.
 */

public class Courses {
    public int id;
    public String code,branch,name,year;

    public Courses(int id, String code, String branch, String name, String year) {
        this.id = id;
        this.code = code;
        this.branch = branch;
        this.name = name;
        this.year = year;
    }

    public Courses() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }
}
