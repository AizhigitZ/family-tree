package familytree.pattern;

import familytree.model.Person;
import java.util.List;
import java.util.ArrayList;

public class DFSTraversal implements TraversalStrategy {
    @Override
    public List<Person> traverse(Person person, int maxDepth) {
        List<Person> result = new ArrayList<>();
        traverseDFS(person, result, 0, maxDepth);
        return result;
    }

    private void traverseDFS(Person person, List<Person> result, int currentDepth, int maxDepth) {
        if (currentDepth > maxDepth) return;

        result.add(person);

        for (Person child : person.getChildren()) {
            traverseDFS(child, result, currentDepth + 1, maxDepth);
        }
    }
}