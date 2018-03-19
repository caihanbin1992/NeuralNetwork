package com.nettyrpc.test.algorithmService;

import com.nettyrpc.server.RpcService;
import org.encog.engine.network.activation.ActivationSigmoid;
import org.encog.ml.CalculateScore;
import org.encog.ml.data.MLData;
import org.encog.ml.data.MLDataPair;
import org.encog.ml.data.MLDataSet;
import org.encog.ml.data.basic.BasicMLDataSet;
import org.encog.ml.train.MLTrain;
import org.encog.ml.train.strategy.Greedy;
import org.encog.ml.train.strategy.HybridStrategy;
import org.encog.ml.train.strategy.StopTrainingStrategy;
import org.encog.neural.networks.BasicNetwork;
import org.encog.neural.networks.training.TrainingSetScore;
import org.encog.neural.networks.training.anneal.NeuralSimulatedAnnealing;
import org.encog.neural.networks.training.propagation.back.Backpropagation;
import org.encog.util.csv.CSVFormat;
import org.encog.util.simple.TrainingSetUtil;

import java.util.Arrays;
import java.util.List;

/**
 * DH-Elman神经网络算法
 * Created by admin on 2018/3/15.
 */
@RpcService(NeuralNetworkService.class)
public class DHElmanServiceImpl implements NeuralNetworkService{


    protected BasicNetwork basicNetwork;

    protected MLDataSet trainingSet;

    protected MLDataSet targetSet;

    protected double[] minMax;


    /**
     * 设置本地训练网络模型
     */
    public DHElmanServiceImpl(String trainingPath, String targetPath,int inputSize,int outputSize) {

        this.trainingSet=getDataSet(trainingPath,inputSize,outputSize);
        this.targetSet=getDataSet(targetPath,inputSize,outputSize);


    }

    /**
     * 设置神经网络
     */
    public void setBasicNetwork(){
        this.basicNetwork=createNetwork();
    }

    @Override
   public MLDataSet getDataSet(String path,int inputSize,int outputSize){
        return TrainingSetUtil.loadCSVTOMemory(CSVFormat.ENGLISH, path,false,inputSize,outputSize);
   }


    @Override
    public MLDataSet inputNomalize() {
        double[][] input=new double[trainingSet.size()][];
        double[][] target=new double[trainingSet.size()][];
        System.out.println(trainingSet.size());
        for(int i=0;i<trainingSet.size();i++){
            MLDataPair m=trainingSet.get(i);
            input[i]=m.getInputArray();
            target[i]=m.getIdealArray();
        }
        input=DataUtil.getInstance().inputNomalize(input,0.6,0.2);
        minMax=DataUtil.getInstance().minMax(target);
        System.out.println(Arrays.toString(minMax));
        target=DataUtil.getInstance().outputNormalize(target,0.6,0.2);

        //构造训练集
        final MLDataSet dataset=new BasicMLDataSet(input,target);
        for(MLDataPair m:dataset){
            System.out.println(Arrays.toString(m.getInputArray())+Arrays.toString(m.getIdealArray()));

        }
        return dataset;
    }


    @Override
    public BasicNetwork createNetwork() {
        //自定义的Elman神经网络
       DHElmenPattern pattern=new DHElmenPattern();
        //ElmanPattern pattern=new ElmanPattern();
        pattern.setActivationFunction(new ActivationSigmoid());
        pattern.setInputNeurons(6);
        pattern.addHiddenLayer(8);
        pattern.setOutputNeurons(1);
        return (BasicNetwork) pattern.generate();
    }

    @Override
    public double trainNetWork(String name,MLDataSet trainingSet,int epoch,double errorDelta) {
        CalculateScore score=new TrainingSetScore(trainingSet);
        final MLTrain trainAlt=new NeuralSimulatedAnnealing(basicNetwork,score,10,2,100);
        final MLTrain trainMain = new Backpropagation(basicNetwork, trainingSet, 0.000001, 0.9);

        final StopTrainingStrategy stop = new StopTrainingStrategy();
        trainMain.addStrategy(new Greedy());
        trainMain.addStrategy(new HybridStrategy(trainAlt));
        trainMain.addStrategy(stop);
        int epoch1 = 0;
        while (true) {
            trainMain.iteration();
            System.out.println("Training " + name + ", Epoch #" + epoch1 + " Error:" + trainMain.getError());
            epoch1++;

            if(epoch%100==0){
                //循环过程逻辑处理
                String[] strs=new String[]{Double.toString(trainMain.getError())};
                //csvWriter.write(Double.toString(trainMain.getError()),true);

            }
            if(trainMain.getError()<errorDelta||epoch1>epoch){
                break;
            }
        }
        return trainMain.getError();

    }

    @Override
    public void computeOutput() {
        double delta=0;
        double[][] targetInput=new double[targetSet.size()][];
        double[][] targetIdeal=new double[targetSet.size()][];
        for(int i=0;i<targetSet.size();i++){
            targetInput[i]=targetSet.get(i).getInputArray();
            targetIdeal[i]=targetSet.get(i).getIdealArray();
        }
        targetInput=DataUtil.getInstance().inputNomalize(targetInput,0.6,0.2);
        MLDataSet outDataSet=new BasicMLDataSet(targetInput,targetIdeal);
        for(int i=0;i<outDataSet.size();i++){
            MLData output1=basicNetwork.compute(outDataSet.get(i).getInput());
            double actual=(output1.getData(0)-0.2)*(minMax[1]-minMax[0])/0.6+minMax[0];
            double ideal=outDataSet.get(i).getIdeal().getData(0);
            delta+=Math.abs(actual-ideal);
            System.out.println("actual:="+actual+",ideal="+ideal+" 误差: "+(Math.abs(actual-ideal)));
        }
        System.out.print(delta);

    }

    @Override
    public double[][] readFromCsv(String path) {
        return new double[0][];
    }

    @Override
    public double[][] readFromDB(String tableName, String timeStamp) {
        return new double[0][];
    }

    @Override
    public double[][] getResultToArray(double[][] input) {
        return new double[0][];
    }

    @Override
    public List<Double> getResultToList(double[][] input) {
        return null;
    }

    @Override
    public void getResultToFile(String path) {

    }

    @Override
    public void reTrainNetWork(BasicNetwork dh_Elman) {

    }
}
