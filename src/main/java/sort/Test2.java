package sort;

import java.util.ArrayList;
import java.util.Arrays;

public class Test2 {
    //冒泡排序
    // 算法描述
    //
    //    步骤1: 比较相邻的元素。如果第一个比第二个大，就交换它们两个；
    //    步骤2: 对每一对相邻元素作同样的工作，从开始第一对到结尾的最后一对，这样在最后的元素应该会是最大的数；
    //    步骤3: 针对所有的元素重复以上的步骤，除了最后一个；
    //    步骤4: 重复步骤1~3，直到排序完成。
    public static int[] bubbleSort(int[]array){

        if(array.length==0){
            return array;
        }
        for (int i=0;i<array.length;i++){
            for (int j=0;j<array.length-1-i;j++){
                if(array[j+1]<array[j]){
                    int temp=array[j+1];
                    array[j+1]=array[j];
                    array[j]=temp;
                }
            }
        }
        return array;
        //算法分析
        //
        //    最佳情况：T(n) = O(n)
        //    最差情况：T(n) = O(n2)
        //    平均情况：T(n) = O(n2)
    }

    //选择排序:
    //思想：不停遍历获取最小值放到未整理区队首
    //无论什么数据进去都是O(n2)的时间复杂度，使用时数据规模越小越好
    public static int[]selectionSort(int[]array){
        if(array.length==0){
            return array;
        }
        for(int i=0;i<array.length;i++){
            int minIndex=i;
            for(int j=i;j<array.length;j++) {
                //找到最小的数
                if (array[j] < array[minIndex]) {
                    //将最小的数索引保存
                    minIndex = j;
                }
            }
            int temp = array[minIndex];
            array[minIndex] = array[i];
            array[i] = temp;
        }
        return array;
    //算法分析
    //最佳情况：T(n) = O(n2)
    //最差情况：T(n) = O(n2)
    //平均情况：T(n) = O(n2)
    }

    //直接插入排序
    //思想：从a[0]开始有序区域扩大，先将前两个数排序，第三个开始插入，
    //每次新插入的数依次和有序区的数从大到小比较，j的值为新增数要插入位置-1，
    //最后直接插入末尾或插入数组其余后移
    public static int[] insert_sort(int a[]/*待排序的数组*/) {
        int i, j, k;
        int n=a.length;
        //
        for (i = 1; i < n; i++) {
            //为a[i]在前面的a[0...i-1]有序区间中找一个合适的位置
            for (j = i - 1; j >= 0; j--) {
                if (a[j] < a[i]) {
                    break;
                }
            }
            //如找到了一个合适的位置
            if (j != i - 1) {
                //将比a[i]大的数据向后移
                int temp = a[i];
                for (k = i - 1; k > j; k--) {
                    a[k + 1] = a[k];
                }
                //将a[i]放到正确位置上
                a[k + 1] = temp;
            }
        }
        return a;
        //直接插入排序的时间复杂度是O(N2)。
    }

    //希尔排序
    //以数组长度/2作为首次步长，以后每次步长/2，两数一组进行比较交换
    //当步长为0时结束循环
    public static int[] ShellSort(int[] array) {
        int len = array.length;
        int temp, gap/*步长*/ = len / 2;
        while (gap > 0) {
            //分组个数
            for (int i = gap; i < len; i++) {
                temp = array[i];
                int preIndex = i - gap;//偏移量
                while (preIndex >= 0 && array[preIndex] > temp) {
                    array[preIndex + gap] = array[preIndex];
                    preIndex -= gap;
                }
                array[preIndex + gap] = temp;
            }
            gap /= 2;
        }
        return array;
        //算法分析
        //最佳情况：T(n) = O(nlog2 n)
        //最坏情况：T(n) = O(nlog2 n)
        //平均情况：T(n) =O(nlog2n)
    }

    //归并排序
    //思路：递归将数组二分，分到一组只有一个为止，将头两个传进合并函数,函数内有几个数组合并
    public static int[] MergeSort(int[] array) {
        if (array.length < 2) {
            return array;
        }
        int mid = array.length / 2;
        int[] left = Arrays.copyOfRange(array, 0, mid);
        int[] right = Arrays.copyOfRange(array, mid, array.length);

        return merge(MergeSort(left), MergeSort(right));
    }

    //归并排序——将两段排序好的数组结合成一个排序数组
    public static int[] merge(int[] left, int[] right) {
        int[] result = new int[left.length + right.length];
        for (int index = 0, i = 0, j = 0; index < result.length; index++) {
            if (i >= left.length) {
                result[index] = right[j++];
            } else if (j >= right.length) {
                result[index] = left[i++];
            } else if (left[i] > right[j]) {
                result[index] = right[j++];
            } else {
                result[index] = left[i++];
            }
        }
        for (int i=0;i<result.length;i++){
            System.out.print(result[i]);
        }
        System.out.println();
        return result;
//        算法分析
//
//        最佳情况：T(n) = O(nlogn)
//        最差情况：T(n) = O(n2)
//        平均情况：T(n) = O(nlogn)
    }

    /**
     * 快速排序方法
     * @param array
     * @param start
     * @param end
     * @return
     */
    public static int[] QuickSort(int[] array, int start, int end) {
        if (array.length < 1 || start < 0 || end >= array.length || start > end) {
            return null;
        }
        int smallIndex = partition(array, start, end);
        if (smallIndex > start) {
            QuickSort(array, start, smallIndex - 1);
        }
        if (smallIndex < end) {
            QuickSort(array, smallIndex + 1, end);
        }
        return array;
    }
    /**
     * 快速排序算法——partition
     * @param array
     * @param start
     * @param end
     * @return
     */
    public static int partition(int[] array, int start, int end) {
        int pivot = (int) (start + Math.random() * (end - start + 1));
        for(int s=0;s<3;s++){
            System.out.print((int) (start + Math.random() * (end - start + 1))+"  ");
        }
        int smallIndex = start - 1;
        swap(array, pivot, end);
        for (int i = start; i <= end; i++) {
            if (array[i] <= array[end]) {
                smallIndex++;
                if (i > smallIndex) {
                    swap(array, i, smallIndex);
                }
            }
        }
        return smallIndex;
    }

    /**
     * 交换数组内两个元素
     * @param array
     * @param i
     * @param j
     */
    public static void swap(int[] array, int i, int j) {
        int temp = array[i];
        array[i] = array[j];
        array[j] = temp;
    }

    //声明全局变量，用于记录数组array的长度；
    static int len;
    /**
     * 堆排序算法
     * @param array
     * @return
     */
    public static int[] HeapSort(int[] array) {
        len = array.length;
        if (len < 1) {
            return array;
        }
        //1.构建一个最大堆
        buildMaxHeap(array);
        //2.循环将堆首位（最大值）与末位交换，然后在重新调整最大堆
        while (len > 0) {
            swap(array, 0, len - 1);
            len--;
            adjustHeap(array, 0);
        }
        return array;
    }
    /**
     * 建立最大堆
     *
     * @param array
     */
    public static void buildMaxHeap(int[] array) {
        //从最后一个非叶子节点开始向上构造最大堆
        //for循环这样写会更好一点：i的左子树和右子树分别2i+1和2(i+1)
        for (int i = (len/2- 1); i >= 0; i--) {
            adjustHeap(array, i);
        }
    }
    /**
     * 调整使之成为最大堆
     *
     * @param array
     * @param i
     */
    public static void adjustHeap(int[] array, int i) {
        int maxIndex = i;
        //如果有左子树，且左子树大于父节点，则将最大指针指向左子树
        if (i * 2 < len && array[i * 2] > array[maxIndex]) {
            maxIndex = i * 2;   //感谢网友矫正，之前是i*2+1
        }
        //如果有右子树，且右子树大于父节点，则将最大指针指向右子树
        if (i * 2 + 1 < len && array[i * 2 + 1] > array[maxIndex]) {
            maxIndex = i * 2 + 1;   //感谢网友矫正，之前是i*2+2
        }
        //如果父节点不是最大值，则将父节点与最大值交换，并且递归调整与父节点交换的位置。
        if (maxIndex != i) {
            swap(array, maxIndex, i);
            adjustHeap(array, maxIndex);
        }
    }

    /**
     * 桶排序
     * @param array
     * @param bucketSize
     * @return
     */
    public static ArrayList<Integer> BucketSort(ArrayList<Integer> array, int bucketSize) {
        if (array == null || array.size() < 2) {
            return array;
        }
        int max = array.get(0), min = array.get(0);
        // 找到最大值最小值
        for (int i = 0; i < array.size(); i++) {
            if (array.get(i) > max) {
                max = array.get(i);
            }
            if (array.get(i) < min) {
                min = array.get(i);
            }
        }
        int bucketCount = (max - min) / bucketSize + 1;
        ArrayList<ArrayList<Integer>> bucketArr = new ArrayList<ArrayList<Integer>>(bucketCount);
        ArrayList<Integer> resultArr = new ArrayList<Integer>();
        for (int i = 0; i < bucketCount; i++) {
            bucketArr.add(new ArrayList<Integer>());
        }
        for (int i = 0; i < array.size(); i++) {
            bucketArr.get((array.get(i) - min) / bucketSize).add(array.get(i));
        }
        for (int i = 0; i < bucketCount; i++) {
            if (bucketSize == 1) { // 如果带排序数组中有重复数字时
                for (int j = 0; j < bucketArr.get(i).size(); j++) {
                    resultArr.add(bucketArr.get(i).get(j));
                }
            } else {
                if (bucketCount == 1) {
                    bucketSize--;
                }
                ArrayList<Integer> temp = BucketSort(bucketArr.get(i), bucketSize);
                for (Integer integer : temp) {
                    resultArr.add(integer);
                }
            }
        }
        return resultArr;
    }

    public static void main(String[] args) {//{5,4,1,8,7,9,6,2}
        int[] b={2,5,1,4,7,3,9,0,6};
        int[]a=QuickSort(b,0,b.length-1);
        for(int i=0;i<a.length;i++){
            System.out.print(a[i]);
        }
    }
}
