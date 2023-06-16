package union_find.successor_with_delete;

public class Main {
    public static void main(String[] args) {
        UnionFindSuccessorWithDelete uf = new UnionFindSuccessorWithDelete(10);
        System.out.println(uf.findSuccessor(3)); // 4
        uf.remove(3);
        System.out.println(uf.findSuccessor(3)); // 4
        uf.remove(5);
        System.out.println(uf.findSuccessor(4)); // 6
        uf.remove(4);
        System.out.println(uf.findSuccessor(3)); // 6
    }
}
