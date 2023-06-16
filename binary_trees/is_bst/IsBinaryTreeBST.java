package binary_trees.is_bst;

    /**
     * Check if a binary tree is a BST. Given a binary tree where each Node contains a key, determine whether it is
     * a binary search tree. Use extra space proportional to the height of the tree.
      */
public class IsBinaryTreeBST {
    public static <T extends Comparable<T>> boolean isBST(Tree<T> tree) {
        if (tree.root == null) return true;
        return checkNode(tree.root, null, false);
    }

    private static <T extends Comparable<T>> boolean checkNode(Node<T> node, T parentValue, boolean isLeftChild) {
        if (node == null) return true;

        if (node.leftChild == null && node.rightChild == null)
            return true;

        if (node.leftChild != null)
            if (node.value.compareTo(node.leftChild.value) < 0)
                return false;

        if (node.rightChild != null)
            if (node.value.compareTo(node.rightChild.value) > 0)
                return false;

        if (parentValue != null) {
            if (isLeftChild) {
                if (parentValue.compareTo(node.rightChild.value) < 0)
                    return false;
            } else if (parentValue.compareTo(node.leftChild.value) > 0)
                return false;
        }

        return checkNode(node.leftChild, node.value, true) && checkNode(node.rightChild, node.value, false);
    }

    public static void main(String[] args) {
        Tree<Integer> integerTree = new Tree<>();
        Node<Integer> root = new Node<>();
        Node<Integer> left = new Node<>();
        Node<Integer> right = new Node<>();

        // Empty tree is BST
        System.out.println(isBST(integerTree));

        // Single root is BST
        integerTree.root = root;
        root.value = 0;
        System.out.println(isBST(integerTree));

        // Ordered tree is BST
        root.leftChild = left;
        root.rightChild = right;
        left.value = -2;
        right.value = 2;
        System.out.println(isBST(integerTree));

        // Unordered tree is not BST: left child
        left.value = 2;
        System.out.println(!isBST(integerTree));
        left.value = -2;

        // Unordered tree is not BST: right child
        right.value = -2;
        System.out.println(!isBST(integerTree));
        right.value = 2;

        // Left child has right child greater than parent
        Node<Integer> leftRight = new Node<>();
        leftRight.value = 1;
        left.rightChild = leftRight;
        System.out.println(!isBST(integerTree));
        leftRight.value = -1;


        // Right child has left child smaller than parent
        Node<Integer> rightLeft = new Node<>();
        rightLeft.value = -1;
        right.leftChild = rightLeft;
        System.out.println(!isBST(integerTree));
        rightLeft.value = 1;

        // Everything in order!
        System.out.println(isBST(integerTree));
    }
}
