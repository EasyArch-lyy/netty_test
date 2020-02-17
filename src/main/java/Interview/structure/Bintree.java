package Interview.structure;

import java.util.ArrayList;
import java.util.List;
/**
* 二叉树
**/
public class Bintree {

    public Bintree left;
    public Bintree right;
    public Bintree root;
    //    数据域
    private Object data;
    //    存节点
    public List<Bintree> datas;

    public Bintree(Bintree left, Bintree right, Object data){
        this.left=left;
        this.right=right;
        this.data=data;
    }
    //    将初始的左右孩子值为空
    public Bintree(Object data){
        this(null,null,data);
    }

    public Bintree() {

    }

    public void creat(Object[] objs){
        datas=new ArrayList<Bintree>();
        //        将一个数组的值依次转换为Node节点
        for(Object o:objs){
            datas.add(new Bintree(o));
        }
        //& & & & & & & &
//        第一个数为根节点
        root=datas.get(0);
//        建立二叉树
        for (int i = 0; i <objs.length/2; i++) {
//            左孩子(奇数节点)
            datas.get(i).left=datas.get(i*2+1);
//            右孩子（偶数节点）
            if(i*2+2<datas.size()){//避免偶数的时候 下标越界
                datas.get(i).right=datas.get(i*2+2);
            }
        }
    }
    //先序遍历
    public void preorder(Bintree root){
        if(root!=null){
            System.out.print(root.data+"  ");
            preorder(root.left);
            preorder(root.right);
        }
    }
    //中序遍历
    public void inorder(Bintree root){
        if(root!=null){
            inorder(root.left);
            System.out.println(root.data);
            inorder(root.right);
        }
    }
    //    后序遍历
    public void afterorder(Bintree root){
        if(root!=null){
            System.out.println(root.data);
            afterorder(root.left);
            afterorder(root.right);
        }
    }
    public static void main(String[] args) {
        Bintree bintree=new Bintree();
        Object []a={8,5,7,1,6,2,4};//2,4,5,7,1,6,12,32,51,22
        bintree.creat(a);
        bintree.preorder(bintree.root);
    }
}