public class Test {
    public static void main(String[] args) {
        Object o = new Object() {
            {
                System.out.println(this.hashCode());
            }
        };
        System.out.println(o.getClass().getName());
    }
}
