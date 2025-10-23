package familytree.cli;

import familytree.service.FamilyTree;
import familytree.model.Person;
import familytree.model.Gender;
import familytree.pattern.BFSTraversal;
import familytree.pattern.DFSTraversal;
import java.util.List;
import java.util.Scanner;

public class CLI {
    private final FamilyTree familyTree;
    private final Scanner scanner;

    public CLI() {
        this.familyTree = new FamilyTree();
        this.scanner = new Scanner(System.in);
    }

    public void start() {
        System.out.println("=== Family Tree Management System ===");
        System.out.println("Available commands:");
        System.out.println("ADD_PERSON \"Name\" GENDER BirthYear [DeathYear]");
        System.out.println("ADD_PARENT_CHILD ParentID ChildID");
        System.out.println("MARRY Person1ID Person2ID Year");
        System.out.println("ANCESTORS PersonID Generations");
        System.out.println("DESCENDANTS PersonID Generations");
        System.out.println("SIBLINGS PersonID");
        System.out.println("CHILDREN PersonID");
        System.out.println("SPOUSE PersonID");
        System.out.println("SHOW PersonID");
        System.out.println("SET_TRAVERSAL DFS|BFS");
        System.out.println("EXIT");
        System.out.println();

        while (true) {
            System.out.print("> ");
            String input = scanner.nextLine().trim();

            if (input.equalsIgnoreCase("EXIT")) {
                break;
            }

            try {
                processCommand(input);
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    private void processCommand(String input) {
        String[] parts = input.split("\\s+", 2);
        String command = parts[0].toUpperCase();
        String arguments = parts.length > 1 ? parts[1] : "";

        switch (command) {
            case "ADD_PERSON":
                handleAddPerson(arguments);
                break;
            case "ADD_PARENT_CHILD":
                handleAddParentChild(arguments);
                break;
            case "MARRY":
                handleMarry(arguments);
                break;
            case "ANCESTORS":
                handleAncestors(arguments);
                break;
            case "DESCENDANTS":
                handleDescendants(arguments);
                break;
            case "SIBLINGS":
                handleSiblings(arguments);
                break;
            case "CHILDREN":
                handleChildren(arguments);
                break;
            case "SPOUSE":
                handleSpouse(arguments);
                break;
            case "SHOW":
                handleShow(arguments);
                break;
            case "SET_TRAVERSAL":
                handleSetTraversal(arguments);
                break;
            default:
                System.out.println("Unknown command: " + command);
        }
    }

    private void handleAddPerson(String args) {
        // Parse quoted name and other parameters
        String[] parts = args.split("\"");
        if (parts.length < 3) {
            throw new IllegalArgumentException("Invalid format. Use: ADD_PERSON \"Name\" GENDER BirthYear [DeathYear]");
        }

        String name = parts[1].trim();
        String[] rest = parts[2].trim().split("\\s+");

        if (rest.length < 2) {
            throw new IllegalArgumentException("Missing gender or birth year");
        }

        Gender gender = Gender.valueOf(rest[0].toUpperCase());
        int birthYear = Integer.parseInt(rest[1]);
        Integer deathYear = rest.length > 2 ? Integer.parseInt(rest[2]) : null;

        Person person = familyTree.addPerson(name, gender, birthYear, deathYear);
        System.out.println("-> " + person.getId());
    }

    private void handleAddParentChild(String args) {
        String[] parts = args.split("\\s+");
        if (parts.length != 2) {
            throw new IllegalArgumentException("Need exactly 2 IDs");
        }
        familyTree.addParentChildRelationship(parts[0], parts[1]);
        System.out.println("OK");
    }

    private void handleMarry(String args) {
        String[] parts = args.split("\\s+");
        if (parts.length != 3) {
            throw new IllegalArgumentException("Need 2 IDs and marriage year");
        }
        familyTree.marry(parts[0], parts[1], Integer.parseInt(parts[2]));
        System.out.println("OK");
    }

    private void handleAncestors(String args) {
        String[] parts = args.split("\\s+");
        if (parts.length != 2) {
            throw new IllegalArgumentException("Need person ID and generations");
        }
        List<Person> ancestors = familyTree.getAncestors(parts[0], Integer.parseInt(parts[1]));
        printPersonList(ancestors, "Ancestors");
    }

    private void handleDescendants(String args) {
        String[] parts = args.split("\\s+");
        if (parts.length != 2) {
            throw new IllegalArgumentException("Need person ID and generations");
        }
        List<Person> descendants = familyTree.getDescendants(parts[0], Integer.parseInt(parts[1]));
        printPersonList(descendants, "Descendants");
    }

    private void handleSiblings(String args) {
        List<Person> siblings = familyTree.getSiblings(args.trim());
        printPersonList(siblings, "Siblings");
    }

    private void handleChildren(String args) {
        List<Person> children = familyTree.getChildren(args.trim());
        printPersonList(children, "Children");
    }

    private void handleSpouse(String args) {
        Person spouse = familyTree.getSpouse(args.trim());
        if (spouse != null) {
            System.out.println("Spouse: " + spouse);
        } else {
            System.out.println("No spouse");
        }
    }

    private void handleShow(String args) {
        Person person = familyTree.getPerson(args.trim());
        System.out.println(person + " | spouse=" +
                (person.getSpouse() != null ? person.getSpouse().getId() : "none") +
                " | children=" + person.getChildren().size());
    }

    private void handleSetTraversal(String args) {
        switch (args.trim().toUpperCase()) {
            case "DFS":
                familyTree.setTraversalStrategy(new DFSTraversal());
                System.out.println("Traversal strategy set to DFS");
                break;
            case "BFS":
                familyTree.setTraversalStrategy(new BFSTraversal());
                System.out.println("Traversal strategy set to BFS");
                break;
            default:
                throw new IllegalArgumentException("Unknown traversal strategy. Use DFS or BFS");
        }
    }

    private void printPersonList(List<Person> people, String title) {
        if (people.isEmpty()) {
            System.out.println("<none>");
        } else {
            System.out.println(title + ":");
            for (Person person : people) {
                System.out.println("- " + person);
            }
        }
    }
}