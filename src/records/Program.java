package records;


public class Program {
    public static void main(String[] args) {
        Person person1 = new Person("John Doe", 30);
        Person person2 = new Person("Jane Smith", 25);
        System.out.println(person1);
        System.out.println(person2);

        System.out.println("Are the two persons the same? " + isSamePerson(person1, person2));

    }

    private static boolean isSamePerson(Person person1, Person person2) {
        return person1.equals(person2);
    }
}