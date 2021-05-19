/*package projet;
import Image.LectureImage;
import mnisttools.MnistReader;
import perceptron.ImageOnlinePerceptron;
import java.util.Arrays;
import java.util.Random;

public class MatriceConfusion {
    public static String path="";
    public static String labelDB=path+"train-labels-idx1-ubyte";
    public static String imageDB=path+"train-images-idx3-ubyte";
    // Le meilleur taux_apprendisage
    private static float taux_apprendisage = (float) 0.018;

    // Générateur de nombres aléatoires
    public static int seed = 1234;
    public static Random GenRdm = new Random(seed);

    /*Methode OneHot
     * prend en donnees un entier etiquette entre 0-9
     * return un tableau de taille 10,
     * et la 'etiquette' eme de sortie est 1, les autres sont 0
     * *//*
    public static int[] OneHot(int etiquette) {
        int[] sorti = new int[12];
        for (int i = 0; i < 10; i++) {
            sorti[i] = 0;
        }
        sorti[etiquette-10] = 1;
        return sorti;
    }
    private  static float SommeProduitS(float[]x,float[][]w,int D,int l){
        float Somme=0;
        for(int i=0;i<D;i++){
            Somme+=x[i]*w[l][i]+w[0][1];
        }
        return Somme;
    }

    public static float[]Infperceptron(float[] data,float[][]w) {
        float proba[] = new float[12];
        float probaSommeClasse = 0;
        float probaUneClasse[] = new float[12];
        int D=data.length;

        for (int l = 0; l < 12; l++) {
            probaUneClasse[l]=SommeProduitS(data,w,D,l);
            probaSommeClasse+=probaUneClasse[l];
        }
        for(int l=0;l<12;l++){
            proba[l]=probaUneClasse[l]/probaSommeClasse;
        }
        return proba;
    }
    /*initialise les poids du perceptron
     *eta=1/dimension
     * donnee: un entier D qui est la dimension d'une classe
     * return un tableau deux dimension de float,avec 10 classe et chaque classe a D elements
     * *//*
    public static float[][] IntialPoidDuPerceptron(int D) {
        Random GenRdm = new Random();
        float[][] x = new float[12][D];
        float eta = (float) 1 / D;
        for (int i = 0; i < 12; i++) {
            for (int j = 0; j < D; j++) {
                x[i][j] = GenRdm.nextFloat() * eta;
            }
        }
        return x;
    }

    public static float[][] MAJLICLASSE(float data[],float[][]w,int etiquette) {
        int[] P=OneHot(etiquette);
        float[]y=Infperceptron(data,w);
        for(int i=0;i<10;i++){
            for(int l=0;l<data.length;l++){
                float moin=y[i]-P[i];
                w[i][l]-=data[l]*taux_apprendisage*moin;
                w[0][l]-=taux_apprendisage*moin;
            }
        }
        return w;
    }

    public static int validation(float proba[]){
        float max=0;
        int classe=0;
        for(int i=0;i<proba.length;i++){
            if(max<proba[i]){
                max=proba[i];
                classe=i;
            }
        }
        return classe;
    }

    public static void main(String[] args){
        MnistReader db = new MnistReader(labelDB, imageDB);
        int [][] image = db.getImage(1);
        int D=image.length*image[0].length+1;
        int Na=5000;
        int Nv=1000;
        int[][]Matrice=new int[12][12];
        for(int i=0;i<)
        float[][]W=IntialPoidDuPerceptron(D);
        for(int i=0;i<Na;i++){

        }
    }
}*/
