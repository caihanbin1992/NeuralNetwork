package com.nettyrpc.test.algorithmService;

import java.util.Arrays;

/**
 * 数据处理工具类
 * Created by admin on 2018/3/16.
 */
public class DataUtil {
    private static DataUtil dataUtil;

    private DataUtil(){

    }

    public static DataUtil getInstance(){
        if (dataUtil==null){
            synchronized (DataUtil.class){
                if (dataUtil==null){
                    dataUtil=new DataUtil();
                }
            }
        }
        return dataUtil;
    }

    /**
     * 归一化方法，可自定义归一化的范围
     * @param input
     * @param up
     * @param lower
     * @return
     */
    public double[][] inputNomalize(double[][] input,double up,double lower){
        double[][] temp1 = new double[input.length][];
        for (int i = 0; i < input.length; i++) {
            double[] array = input[i];
            Arrays.sort(array);
            double max = array[array.length - 1];
            double min = array[0];
            temp1[i] = new double[input[i].length];
            for (int j = 0; j < array.length; j++) {
                temp1[i][j] = up * (input[i][j] - min) / (max - min) + lower;
            }
        }
        return temp1;
    }

    /**
     * 对于输出值的归一化
     * @return
     */
    public double[][] outputNormalize(double[][] target,double up,double lower){

        double[] temp=minMax(target);
        double max = temp[1];
        double min = temp[0];
        double[][] temp1 = new double[target.length][];
        for (int i = 0; i < target.length; i++) {
            for (int j = 0; j < target[0].length; j++) {
                temp1[i] = new double[target[i].length];
                temp1[i][j] = up * (target[i][j] - min) / (max - min) + lower;
            }
        }
        return temp1;
    }

    /**
     * 输出结果的反归一化
     * @param target
     * @return
     */
    public static double[] outputDeNormalize(double[][] target) {


        return null;
    }

    /**
     * 得到二维数组的最大值，最小值
     * @param target
     * @return
     */
    public double[] minMax(double[][] target){
        double max = 0;
        double min = target[0][0];
        for (int i = 0; i < target.length; i++) {
            for (int j = 0; j < target[0].length; j++) {
                if (target[i][j] > max) {
                    max = target[i][j];
                }
                if (target[i][j] < min) {
                    min = target[i][j];
                }
            }
        }
        double[] temp1 = new double[2];
        temp1[0]=min;
        temp1[1]=max;
        return temp1;
    }






}
