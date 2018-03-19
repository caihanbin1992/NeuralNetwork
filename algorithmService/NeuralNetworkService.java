package com.nettyrpc.test.algorithmService;

import org.encog.ml.data.MLDataSet;
import org.encog.neural.networks.BasicNetwork;


import java.util.List;


/**
 * 神经网络服务接口设计
 * 与系统集成提供数据预测服务
 * @author admin
 *
 */
public interface NeuralNetworkService {

	/**
	 * 创建神经网络
	 * @return
	 */
	BasicNetwork createNetwork();

    /**
     * 设置数据训练集，测试集
     * @param path
     * @return
     */
     MLDataSet getDataSet(String path,int inputSize,int outputSize);
    /**
     * 数据归一化
     * @return
     */
     MLDataSet inputNomalize();

	/**
	 * 训练神经网络
	 * @param name 神经网络名称
     * @param trainingSet 训练集数据
	 * @param epoch 循环次数
	 * @param errorDelta 训练误差
	 * @return
	 */

	 double trainNetWork(String name,MLDataSet trainingSet,int epoch,double errorDelta);

	/**
	 * 输出预测值
	 */
	void computeOutput();
	/**
	 * 根据CSV文件路径读取，文件数据，并封装成数据集数组
	 * @param path
	 * @return
	 */

	 double[][] readFromCsv(String path);
	/**
	 * 根据表名和数据时间间隔从数据库读取数据，
	 * 构建数据训练集和测试集
	 * 返回数据集数组
	 * @param tableName
	 * @param timeStamp
	 * @return
	 */
	 double[][] readFromDB(String tableName,String timeStamp);
	/**
	 * 输出预测结果
	 * @param input
	 * @return
	 */
	 double[][] getResultToArray(double[][] input);
	/**
	 * 预测结果封装成list
	 * @param input
	 * @return
	 */
	 List<Double> getResultToList(double[][] input);
	/**
	 * 将预测结果保存对应路径文件
	 * @param path
	 */
	 void getResultToFile(String path);

	/**
	 *重新训练神经网络
	 * @param nnp
	 */
	 void reTrainNetWork(BasicNetwork nnp);
}