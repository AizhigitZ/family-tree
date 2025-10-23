package familytree;

import familytree.model.Gender;
import familytree.model.Person;
import familytree.service.FamilyTree;

public class TestRunner {
    public static void main(String[] args) {
        System.out.println("=== Running Family Tree Tests ===");

        FamilyTree familyTree = new FamilyTree();
        int testsPassed = 0;
        int testsFailed = 0;

        // Test 1: Add Person
        try {
            Person person = familyTree.addPerson("John Doe", Gender.MALE, 1980, null);
            if (person != null && "P001".equals(person.getId())) {
                System.out.println("✅ testAddPerson PASSED");
                testsPassed++;
            } else {
                System.out.println("❌ testAddPerson FAILED");
                testsFailed++;
            }
        } catch (Exception e) {
            System.out.println("❌ testAddPerson FAILED with exception: " + e.getMessage());
            testsFailed++;
        }

        // Test 2: Parent-Child Relationship
        try {
            Person parent = familyTree.addPerson("Parent", Gender.MALE, 1970, null);
            Person child = familyTree.addPerson("Child", Gender.FEMALE, 2000, null);

            familyTree.addParentChildRelationship(parent.getId(), child.getId());

            if (parent.getChildren().size() == 1 && child.getParents().size() == 1) {
                System.out.println("✅ testAddParentChildRelationship PASSED");
                testsPassed++;
            } else {
                System.out.println("❌ testAddParentChildRelationship FAILED");
                testsFailed++;
            }
        } catch (Exception e) {
            System.out.println("❌ testAddParentChildRelationship FAILED with exception: " + e.getMessage());
            testsFailed++;
        }

        // Test 3: Max Two Parents
        try {
            Person child = familyTree.addPerson("Child2", Gender.MALE, 2010, null);
            Person parent1 = familyTree.addPerson("Parent1", Gender.MALE, 1980, null);
            Person parent2 = familyTree.addPerson("Parent2", Gender.FEMALE, 1982, null);
            Person parent3 = familyTree.addPerson("Parent3", Gender.MALE, 1985, null);

            familyTree.addParentChildRelationship(parent1.getId(), child.getId());
            familyTree.addParentChildRelationship(parent2.getId(), child.getId());

            // This should throw exception
            try {
                familyTree.addParentChildRelationship(parent3.getId(), child.getId());
                System.out.println("❌ testMaxTwoParents FAILED - should have thrown exception");
                testsFailed++;
            } catch (IllegalArgumentException e) {
                System.out.println("✅ testMaxTwoParents PASSED");
                testsPassed++;
            }
        } catch (Exception e) {
            System.out.println("❌ testMaxTwoParents FAILED with unexpected exception: " + e.getMessage());
            testsFailed++;
        }

        // Test 4: Marriage
        try {
            Person person1 = familyTree.addPerson("Person1", Gender.MALE, 1980, null);
            Person person2 = familyTree.addPerson("Person2", Gender.FEMALE, 1982, null);

            familyTree.marry(person1.getId(), person2.getId(), 2005);

            if (person1.getSpouse() == person2 && person2.getSpouse() == person1) {
                System.out.println("✅ testMarriage PASSED");
                testsPassed++;
            } else {
                System.out.println("❌ testMarriage FAILED");
                testsFailed++;
            }
        } catch (Exception e) {
            System.out.println("❌ testMarriage FAILED with exception: " + e.getMessage());
            testsFailed++;
        }

        System.out.println("\n=== Test Results ===");
        System.out.println("Passed: " + testsPassed);
        System.out.println("Failed: " + testsFailed);
        System.out.println("Total: " + (testsPassed + testsFailed));
    }
}