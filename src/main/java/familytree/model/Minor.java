package familytree.model;

public class Minor extends Person {
    public Minor(String id, String fullName, Gender gender, int birthYear) {
        super(id, fullName, gender, birthYear);
    }

    public boolean needsGuardian() {
        return true;
    }
}