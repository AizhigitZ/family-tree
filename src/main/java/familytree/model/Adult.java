package familytree.model;

public class Adult extends Person {
    public Adult(String id, String fullName, Gender gender, int birthYear) {
        super(id, fullName, gender, birthYear);
    }

    public boolean canAdopt() {
        return true;
    }
}