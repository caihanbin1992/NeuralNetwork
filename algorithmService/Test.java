package com.nettyrpc.test.algorithmService;

import com.csvreader.CsvReader;
import com.csvreader.CsvWriter;
import org.encog.ml.data.MLDataSet;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by admin on 2018/3/17.
 */
public class Test {

    public static void main(String[] args) throws Exception {

        //初始化加载数据训练集、测试集
        DHElmanServiceImpl elman=new DHElmanServiceImpl("G:\\五粮液资料汇总\\水分仪\\五粮液实验数据\\total.csv",
                "G:\\五粮液资料汇总\\水分仪\\五粮液实验数据\\test1.csv",6,1);

        //输入数据归一化处理
        MLDataSet trainingSet=elman.inputNomalize();
        //创建神经网络
        elman.setBasicNetwork();
        //训练神经网络
        double error=elman.trainNetWork("DH-elman",trainingSet,5000,0.005);
        //计算输出结果
        elman.computeOutput();
/***********************************/
//          BPServiceImpl bp=new BPServiceImpl("C:\\Users\\admin\\Desktop\\test1.csv",
//                  "C:\\Users\\admin\\Desktop\\test2.csv"6,1);
//          MLDataSet trainingSet=bp.inputNomalize();
//
//          bp.setBasicNetwork();
//
//          double error=bp.trainNetWork("bp",trainingSet,5000,0.005);
//
//          bp.computeOutput();
/*********************************/
//        CsvReader csvReader=new CsvReader("C:\\Users\\admin\\Desktop\\数据1.csv");
//        CsvWriter csvWriter=new CsvWriter("C:\\Users\\admin\\Desktop\\normal1.csv");
//        ArrayList<String> strList1=new ArrayList<String>();
//        ArrayList<String> strList2=new ArrayList<String>();
//        while (csvReader.readRecord()) {
//            // System.out.println(csvReader.getRawRecord());
//            String[] strarr = csvReader.getRawRecord().split(" ");
//            //System.out.println(strarr[2]+" "+strarr[7]);
//            String[] strings = new String[]{strarr[2], strarr[7]};
//            csvWriter.writeRecord(strings);
//
//        }
//        csvWriter.close();
    }


    public static void normalData(String readPath,String savePath,int m)throws Exception{
        CsvWriter csvWriter=new CsvWriter("C:\\Users\\admin\\Desktop\\normal.csv");
       // CsvWriter csvWriter=new CsvWriter(readPath);
        CsvReader csvReader=new CsvReader("C:\\Users\\admin\\Desktop\\训练.csv");
       // CsvReader csvReader=new CsvReader(savePath);
        ArrayList<String> strList=new ArrayList<String>();
        while(csvReader.readRecord()){
            System.out.print(csvReader.getRawRecord()+" ");
            strList.add(csvReader.getRawRecord());
        }
        for (int i=0;i<strList.size()-m;i++){
            String[] strArr=new String[m];
            for(int j=i;j<i+m;j++){
                strArr[j-i]=strList.get(j);
            }
            csvWriter.writeRecord(strArr);
        }
        csvWriter.close();

    }



}
