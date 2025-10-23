package familytree.service;

import familytree.model.Person;
import familytree.model.Gender;
import familytree.pattern.PersonFactory;
import familytree.pattern.TraversalStrategy;
import familytree.pattern.DFSTraversal;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;

public class FamilyTree {
    private final Map<String, Person> people;
    private TraversalStrategy traversalStrategy;

    public FamilyTree() {
        this.people = new HashMap<>();
        this.traversalStrategy = new DFSTraversal(); // Default strategy
    }

    public void setTraversalStrategy(TraversalStrategy strategy) {
        this.traversalStrategy = strategy;
    }

    public Person addPerson(String fullName, Gender gender, int birthYear, Integer deathYear) {
        Person person = PersonFactory.createPerson(fullName, gender, birthYear, deathYear);
        people.put(person.getId(), person);
        return person;
    }

    public void addParentChildRelationship(String parentId, String childId) {
        Person parent = getPerson(parentId);
        Person child = getPerson(childId);
        parent.addChild(child);
    }

    public void marry(String person1Id, String person2Id, int year) {
        Person person1 = getPerson(person1Id);
        Person person2 = getPerson(person2Id);

        if (!person1.marry(person2, year)) {
            throw new IllegalArgumentException("Marriage failed: one or both persons are already married");
        }
    }

    public List<Person> getAncestors(String personId, int generations) {
        Person person = getPerson(personId);
        List<Person> ancestors = new ArrayList<>();
        collectAncestors(person, ancestors, 0, generations);
        return ancestors;
    }

    private void collectAncestors(Person person, List<Person> ancestors, int currentDepth, int maxDepth) {
        if (currentDepth > maxDepth) return;

        if (currentDepth > 0) { // Don't add self for ancestors
            ancestors.add(person);
        }

        for (Person parent : person.getParents()) {
            collectAncestors(parent, ancestors, currentDepth + 1, maxDepth);
        }
    }

    public List<Person> getDescendants(String personId, int generations) {
        Person person = getPerson(personId);
        return traversalStrategy.traverse(person, generations);
    }

    public List<Person> getSiblings(String personId) {
        Person person = getPerson(personId);
        List<Person> siblings = new ArrayList<>();

        for (Person parent : person.getParents()) {
            for (Person child : parent.getChildren()) {
                if (!child.equals(person) && !siblings.contains(child)) {
                    siblings.add(child);
                }
            }
        }

        return siblings;
    }

    public List<Person> getChildren(String personId) {
        Person person = getPerson(personId);
        return person.getChildren();
    }

    public Person getSpouse(String personId) {
        Person person = getPerson(personId);
        return person.getSpouse();
    }

    public Person getPerson(String id) {
        Person person = people.get(id);
        if (person == null) {
            throw new IllegalArgumentException("Unknown person ID: " + id);
        }
        return person;
    }

    public boolean personExists(String id) {
        return people.containsKey(id);
    }
}