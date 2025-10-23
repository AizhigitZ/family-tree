package familytree.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public abstract class Person {
    private final String id;
    private final String fullName;
    private final Gender gender;
    private final int birthYear;
    private Integer deathYear;
    private Person spouse;
    private Integer marriageYear;
    private final List<Person> children;
    private final List<Person> parents;

    public Person(String id, String fullName, Gender gender, int birthYear) {
        validateInput(id, fullName, birthYear);

        this.id = id;
        this.fullName = fullName;
        this.gender = gender;
        this.birthYear = birthYear;
        this.children = new ArrayList<>();
        this.parents = new ArrayList<>();
    }

    private void validateInput(String id, String fullName, int birthYear) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("ID cannot be null or empty");
        }
        if (fullName == null || fullName.trim().isEmpty()) {
            throw new IllegalArgumentException("Full name cannot be null or empty");
        }
        if (birthYear < 0) {
            throw new IllegalArgumentException("Birth year must be positive");
        }
    }

    public void setDeathYear(Integer deathYear) {
        if (deathYear != null && deathYear < birthYear) {
            throw new IllegalArgumentException("Death year cannot be before birth year");
        }
        this.deathYear = deathYear;
    }

    public boolean marry(Person spouse, int year) {
        if (this.spouse != null || spouse.getSpouse() != null) {
            return false;
        }
        if (year < this.birthYear || year < spouse.getBirthYear()) {
            throw new IllegalArgumentException("Marriage year cannot be before birth year");
        }

        this.spouse = spouse;
        this.marriageYear = year;
        spouse.spouse = this;
        spouse.marriageYear = year;
        return true;
    }

    public void divorce() {
        if (this.spouse != null) {
            Person exSpouse = this.spouse;
            this.spouse = null;
            this.marriageYear = null;
            exSpouse.spouse = null;
            exSpouse.marriageYear = null;
        }
    }

    public boolean addChild(Person child) {
        if (child == this) {
            throw new IllegalArgumentException("Cannot add self as child");
        }
        if (hasCycle(child)) {
            throw new IllegalArgumentException("Cycle detected: cannot make descendant an ancestor");
        }
        if (!children.contains(child)) {
            children.add(child);
            return child.addParent(this);
        }
        return false;
    }

    private boolean addParent(Person parent) {
        if (parents.size() >= 2) {
            throw new IllegalArgumentException("Cannot have more than 2 parents");
        }
        if (!parents.contains(parent)) {
            parents.add(parent);
            return true;
        }
        return false;
    }

    private boolean hasCycle(Person potentialChild) {
        return potentialChild == this || potentialChild.getAncestors().contains(this);
    }

    public List<Person> getAncestors() {
        List<Person> ancestors = new ArrayList<>();
        for (Person parent : parents) {
            ancestors.add(parent);
            ancestors.addAll(parent.getAncestors());
        }
        return ancestors;
    }

    // Getters
    public String getId() { return id; }
    public String getFullName() { return fullName; }
    public Gender getGender() { return gender; }
    public int getBirthYear() { return birthYear; }
    public Integer getDeathYear() { return deathYear; }
    public Person getSpouse() { return spouse; }
    public Integer getMarriageYear() { return marriageYear; }
    public List<Person> getChildren() { return new ArrayList<>(children); }
    public List<Person> getParents() { return new ArrayList<>(parents); }

    public boolean isAlive() { return deathYear == null; }
    public int getAge(int currentYear) {
        return currentYear - birthYear;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Person)) return false;
        Person person = (Person) o;
        return Objects.equals(id, person.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return String.format("%s | %s | %s | b.%d", id, fullName, gender, birthYear);
    }
}