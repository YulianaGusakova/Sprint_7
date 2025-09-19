package POJO;

public class Courier {
    private String login;
    private String password;
    private String firstName;
    private Integer id;

    public Integer getId() {
        return id;
    }

    public Courier withId(Integer id) {
        this.id = id;
        return this;
    }

    public String getLogin() {
        return login;
    }

    public Courier withLogin(String login) {
        this.login = login;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public Courier withPassword(String password) {
        this.password = password;
        return this;
    }

    public String getFirstName() {
        return firstName;
    }

    public Courier withFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }
}
