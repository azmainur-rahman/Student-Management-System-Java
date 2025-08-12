public class Student implements Comparable<Student> {
    private String name;
    private long id;
    private int batch;
    private String dept;
    private long phoneNumber;
    private String bloodGroup;

    // Constructors
    public Student(String name, long id, int batch, String dept, long phoneNumber, String bloodGroup) {
        this.name = name;
        this.id = id;
        this.batch = batch;
        this.dept = dept;
        this.phoneNumber = phoneNumber;
        this.bloodGroup = bloodGroup;
    }

    public Student() {
    }

    // Getters and Setters for all fields
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getBatch() {
        return batch;
    }

    public void setBatch(int batch) {
        this.batch = batch;
    }

    public String getDept() {
        return dept;
    }

    public void setDept(String dept) {
        this.dept = dept;
    }

    public long getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(long phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getBloodGroup() {
        return bloodGroup;
    }

    public void setBloodGroup(String bloodGroup) {
        this.bloodGroup = bloodGroup;
    }

    // compareTo method to sort students by ID
    @Override
    public int compareTo(Student other) {
        return Long.compare(this.id, other.id);
    }
}