package com.nettyrpc.test.algorithmService;


import org.encog.engine.network.activation.ActivationLinear;
import org.encog.engine.network.activation.ActivationSigmoid;
import org.encog.ml.data.MLDataSet;
import org.encog.neural.networks.BasicNetwork;
import org.encog.neural.networks.layers.BasicLayer;
import org.encog.neural.networks.training.propagation.back.Backpropagation;
import org.encog.neural.networks.training.propagation.resilient.ResilientPropagation;

/**
 *
 * Created by admin on 2018/3/18.
 */
public class BPServiceImpl  extends DHElmanServiceImpl {

    /**
     * 设置本地训练网络模型
     *
     * @param trainingPath
     * @param targetPath
     */
    public BPServiceImpl(String trainingPath, String targetPath, int inSize, int outSize) {
        super(trainingPath, targetPath, inSize, outSize);
    }


    @Override
    public BasicNetwork createNetwork() {
        BasicNetwork network = new BasicNetwork();
        network.addLayer(new BasicLayer(null, true, 7));
        network.addLayer(new BasicLayer(new ActivationSigmoid(), true, 9));
        network.addLayer(new BasicLayer(new ActivationLinear(), false, 1));
        network.getStructure().finalizeStructure();
        network.reset();
        return network;
    }

    @Override
    public void setBasicNetwork() {
        super.basicNetwork = this.createNetwork();
    }

    @Override
    public double trainNetWork(String name, MLDataSet trainingSet, int epoch, double errorDelta) {
        final Backpropagation train = new Backpropagation(super.basicNetwork, trainingSet);
        //final ResilientPropagation train = new ResilientPropagation(super.basicNetwork, trainingSet);
        int epoch1 = 0;
        while (true) {
            train.iteration();
            System.out.println("Training " + name + ", Epoch #" + epoch1 + " Error:" + train.getError());
            epoch1++;
            if (train.getError() < errorDelta || epoch1 > epoch) {
                break;
            }
        }
        return train.getError();
       //return super.trainNetWork(name,trainingSet,epoch,errorDelta);
    }
}