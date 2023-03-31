package edu.upc.talent.swqa.campus.domain;

public record User(String id, String name, String surname, String email, String role, String groupName) {

    @Override
    public boolean equals(Object userObj) {
        User user = (User) userObj;
        return name.equals(user.name)
                && surname.equals(user.surname)
                && email.equals(user.email)
                && role.equals(user.role)
                && groupName.equals(user.groupName);
    }
}
