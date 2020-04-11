package sparsearray;

import java.io.*;

/**
 * 目标 : 五子棋数据的保存及复盘。
 *
 * 实现过程 : 1、通过二维数据记录五子棋数据，1表示白旗，2表示黑旗，0表示未落子。
 *            2、通过稀疏数组，将五子棋数据保存到磁盘中，节省空间。
 */
public class SparseArray {

    public static int size = 10;

    public static void main(String[] args) {
        int[][] array = initChess();
        System.out.println("初始化五子棋 : ");
        print(array);

        //将二维数组，转化为稀疏数组
        int[][] sparseArray = transToSparseArray(array);
        System.out.println("稀疏数组 : ");
        print(sparseArray);

        //将稀疏数组读入文件中，并保存到磁盘里
        String path = writeFile(sparseArray);

        //从磁盘中读取文件，并转化为稀疏数组
        int[][] sparseArray1 = readFile(path, sparseArray.length);

        //将稀疏数组转化为二维数组
        int[][] array2 = transToArray(sparseArray1);
        System.out.println("还原的二维数组 : ");
        print(array2);
    }

    /**
     * 初始化一个二维数组，保存五子棋数据
     * @return 二维数组
     */
    private static int[][] initChess() {
        //使用二维数组，保存五子棋数据
        int[][] array = new int[size][size];

        //初始化部分数据
        array[1][2] = 1;
        array[2][2] = 1;
        array[2][3] = 2;

        return array;
    }

    /**
     * 将二维数组转化为稀疏数组
     * @param array 二维数组
     * @return 稀疏数组
     */
    private static int[][] transToSparseArray(int[][] array) {
        //遍历查询二维数组中元素个数，并根据元素个数n初始化一个(n+1)*3的二维稀疏数组
        int sum = 0;
        for (int[] temp : array) {
            for (int data : temp) {
                if (data != 0){
                    sum++;
                }
            }
        }

        //初始化稀疏数组
        int[][] sparseArray = new int[sum + 1][3];
        //稀疏数组第一行保存二维数组的长度、宽度及元素个数
        sparseArray[0][0] = size;
        sparseArray[0][1] = size;
        sparseArray[0][2] = sum;

        //遍历二维数组元素，并将相应数据存储到稀疏数组中
        //记录稀疏数组的行数，第i行保存第i个二维数组元素的数据
        int k = 1;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (array[i][j] != 0) {
                    sparseArray[k][0] = i;
                    sparseArray[k][1] = j;
                    sparseArray[k][2] = array[i][j];
                    k++;
                }
            }
        }
        return sparseArray;
    }

    /**
     * 将稀疏数组，转化为二维数组
     * @param sparseArray 稀疏数组
     * @return 二维数组
     */
    private static int[][] transToArray(int[][] sparseArray) {
        if (null == sparseArray || sparseArray.length == 0) {
            System.out.println("稀疏数组为空");
            return new int[size][size];
        }

        //基于稀疏数组第一行元素，初始化二维数组
        int[][] array = new int[sparseArray[0][0]][sparseArray[0][1]];

        //遍历稀疏数组，并将相应值存储到二维数组中
        for (int i = 1; i < sparseArray.length; i++) {
            array[sparseArray[i][0]][sparseArray[i][1]] = sparseArray[i][2];
        }

        return array;
    }

    /**
     * 将稀疏数组数据写到文件中
     * @return 文件名称
     */
    private static String writeFile(int[][] array) {
        System.out.println("开始将文件数据写入磁盘中!");
        String path = "sparseArray.txt";
        File file = new File(path);

        FileWriter writer = null;
        try {
            writer = new FileWriter(file);
            for (int[] temp : array) {
                for (int data : temp) {
                    writer.write(data + "\t");
                }
                writer.write("\n");
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != writer) {
                    writer.flush();
                    writer.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        return path;
    }

    /**
     * 读取文件数据,转化为稀疏数组
     */
    private static int[][] readFile(String path, int size) {
        System.out.println("开始读取文件数据！");
        int[][] sparseArray = new int[size][3];

        File file = new File(path);
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));

            String temp = null;
            int k = 0;
            while ((temp = reader.readLine()) != null) {
                String[] str = temp.split("\t");
                sparseArray[k][0] = Integer.parseInt(str[0]);
                sparseArray[k][1] = Integer.parseInt(str[1]);
                sparseArray[k][2] = Integer.parseInt(str[2]);
                k++;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (null != reader) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return sparseArray;
    }

    /**
     * 打印二维数组
     */
    private static void print(int[][] array) {
        if (null == array || array.length == 0) {
            System.out.println("数组为空");
            return;
        }

        for (int[] temp : array) {
            for (int data : temp) {
                System.out.print(data + "\t");
            }
            System.out.println();
        }
    }
}
