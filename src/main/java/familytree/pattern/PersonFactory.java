package familytree.pattern;

import familytree.model.Person;
import familytree.model.Adult;
import familytree.model.Minor;
import familytree.model.Gender;

public class PersonFactory {
    private static int idCounter = 1;

    private PersonFactory() {}

    public static Person createPerson(String fullName, Gender gender, int birthYear, Integer deathYear) {
        String id = generateId();
        Person person;

        if (birthYear > 2005) { // Simple rule: born after 2005 is Minor
            person = new Minor(id, fullName, gender, birthYear);
        } else {
            person = new Adult(id, fullName, gender, birthYear);
        }

        if (deathYear != null) {
            person.setDeathYear(deathYear);
        }

        return person;
    }

    private static String generateId() {
        return String.format("P%03d", idCounter++);
    }

    public static void resetCounter() {
        idCounter = 1;
    }
}