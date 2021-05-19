package Multiclasse;

import Image.LectureImage;
import mnisttools.MnistReader;
import perceptron.ImageOnlinePerceptron;
import java.util.Arrays;
import java.util.Random;

public class MultiClasse {
    public static String path="";
    public static String labelDB=path+"train-labels-idx1-ubyte";
    public static String imageDB=path+"train-images-idx3-ubyte";
    // Le meilleur taux_apprendisage
    private static float taux_apprendisage = (float) 0.018;

    //public static float[][]w;// les references
    // Générateur de nombres aléatoires
    public static int seed = 1234;
    public static Random GenRdm = new Random(seed);

    /*Methode OneHot
    * prend en donnees un entier etiquette entre 0-9
    * return un tableau de taille 10,
    * et la 'etiquette' eme de sortie est 1, les autres sont 0
    * */
    public static int[] OneHot(int etiquette) {
        int[] sorti = new int[10];
        for (int i = 0; i < 10; i++) {
            sorti[i] = 0;
        }
        sorti[etiquette] = 1;
        return sorti;
    }
    /*methode infPerceptron
    * donnees: une tableau float data[],
    * tous les poids du perceptron
    * renvoie un vecteur de float contenant la probabilite d'appartenir a une classe
    * formule: */
    /*public static float[] InfPerceptron1(float[] data) {
        float[] somme = new float[10];//element en haut
        float total = 0;//element en bas
        float[] exp = new float[10];
        for (int i = 0; i < 10; i++) {//on a 10 classe
            for (int l = 1; l < data.length; l++) {//et j data
                exp[i] += w[i][l] * data[l-1];//xi*wil
            }
            exp[i] += w[i][0];
            somme[i] = (float) Math.exp(exp[i]);
        }
        for (int i = 0; i < 10; i++) {
            total += somme[i];
        }
        for (int i = 0; i < 10; i++) {

            somme[i] /= total;
        }
        return somme;
    }*/
    /*
    * w est tous les poids du perceptron
    * D est la dimension de data
    * k est le nombre de classe
    * on veux calculer la probablite de l classe sur la somme de tous les classe
    * et on va return un tableau de 10 proba*/
    private  static float SommeProduitS(float[]x,float[][]w,int D,int l){
        float Somme=0;
        for(int i=0;i<D;i++){
            Somme+=x[i]*w[l][i]+w[0][1];
        }
        return Somme;
    }

    public static float[]Infperceptron(float[] data,float[][]w) {
        float proba[] = new float[10];
        float probaSommeClasse = 0;
        float probaUneClasse[] = new float[10];
        int D=data.length;

        for (int l = 0; l < 10; l++) {
            probaUneClasse[l]=SommeProduitS(data,w,D,l);
            probaSommeClasse+=probaUneClasse[l];
        }
        for(int l=0;l<10;l++){
            proba[l]=probaUneClasse[l]/probaSommeClasse;
        }
        return proba;
    }


    /*initialise les poids du perceptron
    *eta=1/dimension
    * donnee: un entier D qui est la dimension d'une classe
    * return un tableau deux dimension de float,avec 10 classe et chaque classe a D elements
    * */
    public static float[][] IntialPoidDuPerceptron(int D) {
        Random GenRdm = new Random();
        float[][] x = new float[10][D];
        float eta = (float) 1 / D;
        for (int i = 0; i < 10; i++) {
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


    /*Mise a jour des poids du perceptron
    * */


    public static void main(String[] args) {
        MnistReader db = new MnistReader(labelDB, imageDB);
        int [][] image = db.getImage(1);
        int ref=db.getLabel(1);
        int D=image.length*image[0].length+1;
        float[][]validData=new float[ImageOnlinePerceptron.Nv][D+1];

        //tester la methode one hut
        for(int i=0;i<5;i++){
            System.out.println(Arrays.toString(OneHot(i)));
        }
        float[]data=ImageOnlinePerceptron.ConvertImage(image);
        float[][]percep=IntialPoidDuPerceptron(data.length);
        float proba[]=Infperceptron(data,percep);
        float somme=0;
        for(int i=0;i<proba.length;i++){
            if(proba[i]<0)
                System.out.println("Probabilite negatif");
            somme+=proba[i];
        }
        System.out.println(somme);
        System.out.println(Arrays.toString((proba)));
        for(int i=0;i<percep.length;i++){
            percep=MAJLICLASSE(data,percep,i);
        }
        proba=Infperceptron(data,percep);
        int resultat=validation(proba);
        System.out.println(resultat);
        System.out.println(ref);
        int[][] test=LectureImage.binarisation(image,130);
    }
}
