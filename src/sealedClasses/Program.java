package sealedClasses;

public class Program {

    public static void main(String[] args) {
        Circle circle = new Circle(5);
        Rectangle rectangle = new Rectangle(5,5);
        Triangle triangle = new Triangle(5,5);

        double circleArea = calculateArea(circle);
        double rectangleArea = calculateArea(rectangle);
        double triangleArea = calculateArea(triangle);

        System.out.println(circleArea);
        System.out.println(rectangleArea);
        System.out.println(triangleArea);

    }

    private static double calculateArea(Shape shape) {
        return switch (shape) {
            case Circle c -> c.area();
            case Rectangle r -> r.area();
            case Triangle t -> t.area();
        };
    }
}
