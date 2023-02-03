
class RBNode{
    int key;
    int color;
    int size;
    RBNode parent;
    RBNode left;
    RBNode right;

    public RBNode(int key) {
        this.key = key;
        this.color = 0;
        this.size = 1;
        this.parent = this.left = this.right = null;
    }
}

public class RedBlackTree {
    RBNode root;

    public RedBlackTree() {
        root = null;
    }
    public boolean search(int key) {
        return search_Helper(root,key) != null;
    }

    public boolean insert(int key) {
        RBNode temp = search_Helper(root,key);
        if(temp == null) {
            RBNode z = new RBNode(key);
            insert_Helper(z);
            return true;
        } else {
            return false;
        }
    }

    public boolean delete(int key) {
        RBNode z = search_Helper(root,key);
        if(z == null) { // node with key doesn't exist
            return false;
        } else {
            delete_Helper(z);
            return true;
        }
    }

    public int countValuesLessThan(int key) {
        return countHelper(root,key);
    }

    private int countHelper(RBNode z, int key) {
        if(z == null) {
            return 0;
        }

        if(key == z.key) {
            return size(z.left);
        } else if(key < z.key) {
            return countHelper(z.left,key);
        } else {
            return 1 + size(z.left) + countHelper(z.right,key);
        }
    }

    boolean isEmpty() {
        return root == null;
    }

    private int size(RBNode x) {
        if(x == null) {
            return 0;
        } else {
            return x.size;
        }
    }

    private int color(RBNode x) {
        if(x == null) {
            return 0;
        } else {
            return x.color;
        }
    }

    private void leftRotate(RBNode x) {
        RBNode y = x.right;
        x.right = y.left;
        if(y.left != null) {
            y.left.parent = x;
        }
        y.parent = x.parent;
        if(x.parent == null) {
            root = y;
        } else if(x.parent.left == x){
            x.parent.left = y;
        } else {
            x.parent.right = y;
        }
        y.left = x;
        x.parent = y;

        // Update the size
        x.size = size(x.left) + size(x.right) + 1;
        y.size = size(y.left) + size(y.right) + 1;
    }

    private void rightRotate(RBNode x) {
        RBNode y = x.left;
        x.left = y.right;
        if(y.right != null) {
            y.right.parent = x;
        }
        y.parent = x.parent;
        if(x.parent == null) {
            root = y;
        } else if(x.parent.left == x){
            x.parent.left = y;
        } else {
            x.parent.right = y;
        }
        y.right = x;
        x.parent = y;

        // Update the size
        x.size = size(x.left) + size(x.right) + 1;
        y.size = size(y.left) + size(y.right) + 1;
    }

    private void fixSizeInsert(RBNode y) {
        if(y == null)
            return;
        else {
            y.size++;
            fixSizeInsert(y.parent);
        }
    }

    private void fixSizeDelete(RBNode y) {
        if(y == null)
            return;
        else {
            y.size--;
            fixSizeDelete(y.parent);
        }
    }

    private RBNode search_Helper(RBNode x, int key) {
        if(x == null || x.key == key) {
            return x;
        } else if(key<x.key) {
            return search_Helper(x.left, key);
        } else {
            return search_Helper(x.right,key);
        }
    }

    private RBNode getMin(RBNode x) {
        if(x.left == null) {
            return x;
        } else {
            return getMin(x.left);
        }
    }

    private void insert_Helper(RBNode z) {
        RBNode y = null;
        RBNode x = root;

        while(x!=null) {
            y = x;
            if(z.key < x.key) {
                x = x.left;
            } else {
                x = x.right;
            }
        }

        z.parent = y;
        if(y == null) {
            root = z;
        } else if(z.key < y.key) {
            y.left = z;
        } else {
            y.right = z;
        }

        // only size(z) correct, fixing up till the root
        fixSizeInsert(z.parent);

        z.color = 1;
        insertFixup(z);
    }

    private void insertFixup(RBNode z) {
        while(color(z.parent) == 1) {
            if(z.parent == z.parent.parent.left) {
                RBNode y = z.parent.parent.right;
                if(color(y) == 1) { //case 1
                    z.parent.color = 0;
                    y.color = 0;
                    z.parent.parent.color = 1;
                    z = z.parent.parent;
                } else {
                    if(z == z.parent.right) {
                        z = z.parent;
                        leftRotate(z);
                    }
                    z.parent.color = 0;
                    z.parent.parent.color = 1;
                    rightRotate(z.parent.parent);
                }
            } else {
                RBNode y = z.parent.parent.left;
                if(color(y) == 1) { //case 1
                    z.parent.color = 0;
                    y.color = 0;
                    z.parent.parent.color = 1;
                    z = z.parent.parent;
                } else {
                    if(z == z.parent.left) {
                        z = z.parent;
                        rightRotate(z);
                    }
                    z.parent.color = 0;
                    z.parent.parent.color = 1;
                    leftRotate(z.parent.parent);
                }
            }
        }
        root.color = 0;
    }

    private void delete_Helper(RBNode z) {
        RBNode x,y, parent_x; // parent_x required if x is null

        // when x is null, we my require this
        boolean isXLeftChild = false;
        if (z.left == null || z.right == null) {
            y = z;
        } else {
            y = getMin(z.right);
        }

        if(y.left != null) {
            x = y.left;
        } else {
            x = y.right;
        }

        if(x != null) {
            x.parent = y.parent;
        }
        // when x is null, we may need this value
        parent_x = y.parent;

        if(y.parent == null) {
            root = x;
        } else if(y == y.parent.left) {
            y.parent.left = x;
            isXLeftChild = true;
        } else {
            y.parent.right = x;
        }

        if(y!=z) {
            z.key = y.key;
        }

        //fixSize now
        fixSizeDelete(parent_x);

        if(color(y) == 0) {
            deleteFixUp(x,parent_x,isXLeftChild);
        }
    }

    private void deleteFixUp(RBNode x, RBNode parent_x, boolean isXLeftChild) {
        while(color(x) == 0 && x!=root) {
            RBNode w;
            if(isXLeftChild) {
                w = parent_x.right;
                if(color(w) == 1) {
                    w.color = 0;
                    parent_x.color = 1;
                    leftRotate(parent_x);
                    w = parent_x.right;
                }
                if(color(w.left)==0 && color(w.right) == 0) {
                    w.color = 1;
                    x = parent_x;
                    parent_x = x.parent;
                } else {
                    if(color(w.right) == 0) {
                        w.left.color = 0;
                        w.color = 1;
                        rightRotate(w);
                        w = parent_x.right;
                    }
                    w.color = parent_x.color;
                    parent_x.color = 0;
                    w.right.color = 0;
                    leftRotate(parent_x);
                    x = root;
                }
            } else {
                w = parent_x.left;
                if(color(w) == 1) {
                    w.color = 0;
                    parent_x.color = 1;
                    rightRotate(parent_x);
                    w = parent_x.left;
                }
                if(color(w.right)==0 && color(w.left) == 0) {
                    w.color = 1;
                    x = parent_x;
                    parent_x = x.parent;
                } else {
                    if(color(w.left) == 0) {
                        w.right.color = 0;
                        w.color = 1;
                        leftRotate(w);
                        w = parent_x.left;
                    }
                    w.color = parent_x.color;
                    parent_x.color = 0;
                    w.left.color = 0;
                    rightRotate(parent_x);
                    x = root;
                }
            }

            // updating isXLeftChild
            if(x != root) { // if x == root, then loop terminates
                if(x == parent_x.left){
                    isXLeftChild = true;
                } else {
                    isXLeftChild = false;
                }
            }
        }

        if(x!= null) { // when tree is deleted
            x.color = 0;
        }
    }
}












