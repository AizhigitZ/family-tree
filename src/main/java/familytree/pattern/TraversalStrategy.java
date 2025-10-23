package familytree.pattern;

import familytree.model.Person;
import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public interface TraversalStrategy {
    List<Person> traverse(Person person, int maxDepth);
}