package projet;
import java.io.*;
import java.util.Random;
import java.util.Arrays;
import mnisttools.MnistReader;


public class Matrice {


    /* Les donnees */
    public static final  int EPOCHMAX=10;


    // Générateur de nombres aléatoires
    public static int seed = 1234;
    public static Random GenRdm = new Random(seed);
    public static int Na=5000;
    public static int Nv=1000;
    public static float eta=0.1f;

    /*Une fonction qui prend les donnees:
    * Une tableau represente data de deux dimension
    * une tableau de w
    * sortie les plus possible classe de data */
    private static int Max(float[]nb){
        float prob=0;
        int Maxi=0;
        for(int i=0;i<nb.length;i++){
            if(prob<nb[i]){
                prob=nb[i];
                Maxi=i;
            }
        }
        return Maxi+10;
    }
    public static int[]Predict(float[][]data,float[][]w){
        int[]RefPredict=new int[data.length];
        float[]Probas=new float[12];
        for (int i = 0; i < data.length; i++) {
            Probas = Multi.InfPerceptron(data[i], w);
            //System.out.println(Arrays.toString(Probas));
            //System.out.println(Max(Probas));
            RefPredict[i]=Max(Probas);
        }

        return RefPredict;
    }

    public static void main(String[] args) {
        String path="";
        String labelDB=path+"train-labels-idx1-ubyte";
        String imageDB=path+"train-images-idx3-ubyte";
        System.out.println("# Load the database !");
        MnistReader db = new MnistReader(labelDB, imageDB);
        int DIM = db.getImage(1).length*db.getImage(1)[0].length+1;

        int[] trainRefs = new int[Na];
        float[][] trainData = new float[Na][DIM];

        int j=0;
        int n=0;
        while (n<Na) {
            if (db.getLabel(j+1)<22 && db.getLabel(j+1)>9) {
                trainRefs[n] = db.getLabel(j+1);
                trainData[n] = Image.ConvertImage(Image.BinariserImage(db.getImage(j+1),127));
                n+=1;
            }
            j+=1;
        }

        float[][] w=Multi.InitialiseW(DIM);

        for (int t=0; t<EPOCHMAX; t++) {
            Multi.epoque(eta, trainData, trainRefs, w);
        }
        /*Tableau de l'ensemble de validation*/
        int[] ValidRefs = new int[Nv];
        float[][] ValidData = new float [Nv][DIM];
        n=0;
        while (n<Nv) {
            if (db.getLabel(j+1)<22 && db.getLabel(j+1)>9) {
                ValidRefs[n]=db.getLabel(j+1);
                ValidData[n] = Image.ConvertImage(Image.BinariserImage(db.getImage(j+1),127));
                n+=1;
            }
            j+=1;
        }
        System.out.println(Arrays.toString(ValidRefs));
        //System.out.println(Arrays.toString(Multi.InfPerceptron(trainData[0], w)));
        int[] VrefPredict=Predict(ValidData,w);
        System.out.println(Arrays.toString(VrefPredict));


        //需要建立一个表格validrefspredict看看和validrefs的差别

    }
}
