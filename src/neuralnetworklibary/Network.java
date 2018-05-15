package neuralnetworklibary;

public class Network {

    private double weights[][];
    private double bias[][];
    int inputNeurons = 20;
    int outputNeurons = 4;
    int neurons[] = new int[]{40, outputNeurons};

    //Alustetaan muuttujat ja asetetaan verkolle satunnaiset painot.
    public Network() {
        weights = new double[neurons.length][0];
        bias = new double[neurons.length][0];
        int amount = inputNeurons;
        for (int i = 0; i < neurons.length; i++) {
            double layerWeights[] = new double[amount * neurons[i]];
            double biasWeights[] = new double[neurons[i]];
            //double interval = 1.0 / Math.sqrt(amount + 1);
            double interval = 20.0;
            for (int j = 0; j < neurons[i] * amount; j++) {
                layerWeights[j] = Math.random() * (interval - (-interval)) + (-interval);
                if (j < biasWeights.length) {
                    biasWeights[j] = Math.random() * (interval - (-interval)) + (-interval);
                }
            }
            weights[i] = layerWeights;
            bias[i] = biasWeights;
            amount = neurons[i];
        }
    }

    public Network(double weights[][], double bias[][]) {
        this.weights = weights;
        this.bias = bias;
    }

    //Saatu data virtaa verkon läpi
    public double[] compute(double inputVector[]) {
        double calculated[] = new double[0];
        for (int layer = 0; layer < weights.length; layer++) {
            int weight = 0;
            calculated = new double[neurons[layer]];
            for (int j = 0; j < neurons[layer]; j++) {
                double a[] = activation(inputVector, layer, weight, j);
                //calculated[j] = reLU(a[0]);
                calculated[j] = sigmoid(a[0]);
                weight = (int) a[1];
            }
            inputVector = calculated;
        }
        return calculated;
    }

    //Lasketaan neuronille aktivoitumis arvo.
    public double[] activation(double inputVector[], int layer, int weight, int biasINDEX) {
        double aValue = -bias[layer][biasINDEX];
        for (int j = 0; j < inputVector.length; j++, weight++) {
            aValue += inputVector[j] * weights[layer][weight];
        }
        return new double[]{aValue, weight};
    }

    public double sigmoid(double x) {
        return 1 / (1 + Math.exp(-x));
    }

    public double reLU(double x) {
        return Math.max(x, 0);
    }

    public double[] softMax(double input[]) {
        double output[] = new double[input.length];
        double sum = 0;
        for (int j = 0; j < input.length; j++) {
            sum += Math.exp(input[j]);
        }
        for (int i = 0; i < input.length; i++) {
            output[i] = Math.exp(input[i]);
            output[i] /= sum;
        }
        return output;
    }

    public double[][] getWeights() {
        return weights;
    }

    public double[][] getBiasweights() {
        return bias;
    }

    public int getIntputNeurons() {
        return inputNeurons;
    }

}
