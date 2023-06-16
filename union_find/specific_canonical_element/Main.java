package union_find.specific_canonical_element;

public class Main {
    public static void main(String[] args) {
        UnionFindSpecificCanonicalElement uf = new UnionFindSpecificCanonicalElement(10);
        uf.union(1,2);
        uf.union(1,9);
        uf.union(2,6);
        System.out.println(uf.find(1));
        System.out.println(uf.find(2));
        System.out.println(uf.find(6));
        System.out.println(uf.find(9));
    }
}
