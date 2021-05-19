package perceptron;

import java.util.Arrays;
import java.util.Random;
import mnisttools.MnistReader;
public class ImageOnlinePerceptron {
    /* Les donnees */
    public static String path="";
    public static String labelDB=path+"train-labels-idx1-ubyte";
    public static String imageDB=path+"train-images-idx3-ubyte";

    /* Parametres */
    // Na exemples pour l'ensemble d'apprentissage
    public static final int Na = 10000;
    // Nv exemples pour l'ensemble d'évaluation qui est l'indix 1000-10000 sur la database
    public static final int Nv = 9000;
    //Nt exemples pour l'ensemble test
    //pour la deuxieme question public static final int Nt=1000;
    // Nombre d'epoque max
    public final static int EPOCHMAX=40;
    // Classe positive (le reste sera considere comme des ex. negatifs):
    public static int  classe = 5 ;

    // Générateur de nombres aléatoires
    public static int seed = 1234;
    public static Random GenRdm = new Random(seed);
    public static float w[][]=new float[10][];

    /*
     *  BinariserImage :
     *      image: une image int à deux dimensions (extraite de MNIST)
     *      seuil: parametre pour la binarisation
     *
     *  on binarise l'image à l'aide du seuil indiqué
     *
     */
    public static int[][] BinariserImage(int[][] image, int seuil) {
        int[][]resultat=image;
        for(int colonne=0;colonne<image.length;colonne++) {
            for(int ligne=0;ligne<image[0].length;ligne++) {
                if(image[colonne][ligne]<=seuil) {
                    resultat[colonne][ligne]=0;
                }
                else {
                    resultat[colonne][ligne]=1;
                }
            }
        }
        return resultat;
    }

    /*
     *  ConvertImage :
     *      image: une image int binarisée à deux dimensions
     *
     *  1. on convertit l'image en deux dimension dx X dy, en un tableau unidimensionnel de tail dx.dy
     *  2. on rajoute un élément en première position du tableau qui sera à 1
     *  La taille finale renvoyée sera dx.dy + 1
     *
     */
    public static float[] ConvertImage(int[][] image) {
        float[]Image1Dim=new float[image.length*image[0].length+1];
        Image1Dim[0]=1;
        for(int i=0;i<image.length;i++){
            for(int j=0;j<image[0].length;j++){
                //System.out.println(i*image[0].length+j+1);
                Image1Dim[i*image[0].length+j+1]=image[i][j];
            }
        }
        return Image1Dim;
    }

    /*
     *  InitialiseW :
     *      sizeW : la taille du vecteur de poids (la taille de module)
     *      alpha : facteur à rajouter devant le nombre aléatoire
     *
     *  le vecteur de poids est crée et initialisé à l'aide d'un générateur
     *  de nombres aléatoires.
     */
    public static float[] InitialiseW(int sizeW, float alpha) {
        float[] w=new float[sizeW];
        for(int i=0;i<sizeW;i++){
            w[i]=GenRdm.nextFloat()*alpha;
        }
        return w;
    }

    public static float taux_erreur(float[][] validData,  int[] validRefs, float[] vecteur) {
        float res;
        int right = 0;
        for (int i = 0; i < Nv ; i++) {
            int temp = 0;
            for (int j = 1; j < validData[0].length+1 ; j++) {
                temp += validData[i][j-1]*vecteur[j];
            }
            temp += vecteur[0];
            int sign = (temp>7)?1:-1;
            if(sign == validRefs[i]) {
                System.out.println("Right "+ i + " " + validRefs[i] + " " + sign + " " + Math.round(temp));
                right++;
            } else {
                System.out.println("Wrong "+ i + " " + validRefs[i] + " " + sign + " " + Math.round(temp));
            }
        }
        res = (float)right/Nv;
        return res;
    }



    public static void main(String[] args) {
        System.out.println("# Load the database !");
        /* Lecteur d'image */
        MnistReader db = new MnistReader(labelDB, imageDB);

        /* Creation des donnees */
        System.out.println("# Build train for digit "+ classe);
        /* Tableau où stocker les données */
        // tester convertImage
        int[][] test = { // les observations
                {1,0,0}, {1,0,1} };
        float[] test1=ConvertImage(test);
        System.out.println(Arrays.toString(test1));

        //main
        // Acces a la premiere image
        int idx = 100; // une variable pour stocker l'index
        // Attention la premiere valeur est 1.
        int [][] image = db.getImage(idx);
        int D=image.length*image[0].length+1;
        float[]Stockee=new float[Na*D+1];// le tableau pour stocker
        float[][]trainData=new float[Na][D+1];// il y a Nv image, donc Nv ligne et chaque ligne compose uniDim(image)
        int[]trainRefs=new int[Na];//les references contient -1 ou 1(bonne classe)

        int erreurfois=0;
        for(int i=1;i<Na+1;i++){
            int [][] imageB = db.getImage(i);
            imageB=BinariserImage(imageB,100);
            trainData[i-1]=ConvertImage(imageB);
            if(db.getLabel(i)==classe) {
                trainRefs[i-1]=1;
            }else {
                trainRefs[i-1]=-1;
                erreurfois++;
            }
        }
        System.out.println(erreurfois);

        float[][]validData=new float[Nv][D+1];
        int[]validRefs=new int[Nv];
        for(int i=0;i<Nv;i++) {
            int[][] imageB=db.getImage(idx+1+Na);
            imageB=BinariserImage(imageB,100);
            validData[idx]=ConvertImage(imageB);
        }
        for(int i=0;i<Nv;i++) {
            if(db.getLabel(i+1)==classe) {
                validRefs[i]=1;
            }else {
                validRefs[i]=-1;
                erreurfois++;
            }
        }

        float[] sortie=OnlinePerceptron.W(validData[0],validRefs[0],validData[0].length,0.05f);
        System.out.println(Arrays.toString(sortie));
    }
}
