package projet;
import java.io.*;
import java.util.Random;

import mnisttools.MnistReader;

public class Image {
    /* Les donnees */
    public static final  int EPOCHMAX=10;


    // Générateur de nombres aléatoires
    public static int seed = 1234;
    public static Random GenRdm = new Random(seed);

    public static int[][] BinariserImage(int[][] image, int seuil) {
        for(int i=0;i<image.length;i++) {
            for(int j=0;j<image[i].length;j++) {
                if(image[i][j]<seuil) {
                    image[i][j]=0;
                }
                else
                    image[i][j]=1;
            }
        }
        return image;
    }

    /*
     *  ConvertImage :
     *      image: une image int binarisée à deux dimensions
     *
     *  1. on convertit l'image en deux dimension dx X dy, en un tableau unidimensionnel de taille dx.dy
     *  2. on rajoute un élément en première position du tableau qui sera à 1
     *  La taille finale renvoyée sera dx.dy + 1
     *
     */
    public static float[] ConvertImage(int[][] image) {
        float[] image_1D = new float[image.length * image[0].length + 1];
        image_1D[0]=1;
        for(int i=0; i<image.length;i++) {
            for(int j=0;j<image[0].length;j++) {
                image_1D[1+i*image.length+j]=image[i][j];
            }
        }
        return image_1D;
    }


    public static void main(String[] args) throws IOException {
        String path="";
        String labelDB=path+"train-labels-idx1-ubyte";
        String imageDB=path+"train-images-idx3-ubyte";

        System.out.println("# Load the database !");
        /* Lecteur d'image */
        MnistReader db = new MnistReader(labelDB, imageDB);

        /*creation des données*/

        int DIM = db.getImage(1).length*db.getImage(1)[0].length+1;

        choix_Na(db,DIM,0.1f);

        //choix_eta(db,DIM);
    }



    public static void choix_Na(MnistReader db, int DIM, float eta) throws IOException {
        int Nv=1000;
        int[] tous_les_Na = new int[10];
        for (int i=0; i<10; i++) {
            tous_les_Na[i]=1000+1000*i;
        }

        FileWriter fw = new FileWriter("choix_Na.d");

        for (int i=0; i<10; i++) {
            int Na=tous_les_Na[i];


            /*phase d'apprentissage*/
            int[] trainRefs = new int[Na];
            float[][] trainData = new float [Na][DIM];
            int j=0;
            int n=0;
            while (n<Na) {
                if (db.getLabel(j+1)<22 && db.getLabel(j+1)>9) {
                    trainRefs[n] = db.getLabel(j+1);
                    trainData[n] = ConvertImage(BinariserImage(db.getImage(j+1),127));
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
                    ValidData[n] = ConvertImage(BinariserImage(db.getImage(j+1),127));
                    n+=1;
                }
                j+=1;
            }


            /*fonction de co�t*/
            float E = Multi.CostFunction(w, ValidData, ValidRefs);

            /*mis dans un fichier*/
            fw.write(Na+" "+E+"\n");
        }
        fw.close();
    }



    public static float[] choix_eta(MnistReader db, int DIM) throws IOException {
        int Nv=1000;
        int Na=10000;
        int Nt=1000;
        float[] tous_les_eta = new float [10];
        for (int i=0; i<10; i++) {
            tous_les_eta[i]=0.1f+0.1f*i;
        }

        /*donn�es d'apprentissage*/
        int[] trainRefs = new int[Na];
        float[][] trainData = new float [Na][DIM];
        int j=0;
        int n=0;
        while (n<Na) {
            if (db.getLabel(j+1)<22 && db.getLabel(j+1)>9) {
                trainRefs[n] = db.getLabel(j+1);
                trainData[n] = ConvertImage(BinariserImage(db.getImage(j+1),127));
                n+=1;
            }
            j+=1;
        }

        /*donn�es de validation*/
        int[] ValidRefs = new int[Nv];
        float[][] ValidData = new float [Nv][DIM];
        n=0;
        while (n<Nv) {
            if (db.getLabel(j+1)<22 && db.getLabel(j+1)>9) {
                ValidRefs[n]=db.getLabel(j+1);
                ValidData[n] = ConvertImage(BinariserImage(db.getImage(j+1),127));
                n+=1;
            }
            j+=1;
        }


        /*donn�es de test*/
        int[] testRefs = new int[Nt];
        float[][] testData = new float [Nt][DIM];
        n=0;
        while (n<Nt) {
            if (db.getLabel(j+1)<22 && db.getLabel(j+1)>9) {
                ValidRefs[n]=db.getLabel(j+1);
                ValidData[n] = ConvertImage(BinariserImage(db.getImage(j+1),127));
                n+=1;
            }
            j+=1;
        }

        /*tableau des Ev*/
        float[] tous_les_Ev = new float[10];

        FileWriter fw = new FileWriter("choix_eta.d");

        for (int i=0; i<10; i++) {
            float eta = tous_les_eta[i];

            float[][] w=Multi.InitialiseW(DIM);

            for (int t=0; t<EPOCHMAX; t++) {
                Multi.epoque(eta, trainData, trainRefs, w);
            }

            /*fonction de co�t*/
            float Ev = Multi.CostFunction(w, ValidData, ValidRefs);
            tous_les_Ev[i]=Ev;

            /*mis dans un fichier*/
            fw.write(eta+" "+Ev+"\n");
        }

        fw.close();

        /*recherche du meilleur eta*/
        int i_max = 0;
        for (int i=1; i<10; i++) {
            if (tous_les_Ev[i]>tous_les_Ev[i_max]) {
                i_max=i;
            }
        }

        float eta=tous_les_eta[i_max];

        float[][] w=Multi.InitialiseW(DIM);

        for (int t=0; t<EPOCHMAX; t++) {
            Multi.epoque(eta, trainData, trainRefs, w);
        }

        float Ev = Multi.CostFunction(w, ValidData, ValidRefs);
        float Et = Multi.CostFunction(w, testData, testRefs);

        float[] E = new float[2];
        E[0]=Ev;
        E[1]=Et;

        return E;
    }
}
