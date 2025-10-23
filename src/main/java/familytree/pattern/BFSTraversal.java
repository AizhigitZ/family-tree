package familytree.pattern;

import familytree.model.Person;
import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class BFSTraversal implements TraversalStrategy {
    @Override
    public List<Person> traverse(Person person, int maxDepth) {
        List<Person> result = new ArrayList<>();
        Queue<Person> queue = new LinkedList<>();
        Queue<Integer> depthQueue = new LinkedList<>();

        queue.offer(person);
        depthQueue.offer(0);

        while (!queue.isEmpty()) {
            Person current = queue.poll();
            int depth = depthQueue.poll();

            if (depth <= maxDepth) {
                result.add(current);

                for (Person child : current.getChildren()) {
                    queue.offer(child);
                    depthQueue.offer(depth + 1);
                }
            }
        }

        return result;
    }
}