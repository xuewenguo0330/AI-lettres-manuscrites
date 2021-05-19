package projet;
import mnisttools.MnistReader;
import perceptron.ImageOnlinePerceptron;
import java.util.Arrays;
import java.util.Random;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import java.io.IOException;
import java.awt.Color;

public class Multi {
    public static int[] OneHot(int indice) {
        int[] vecteur = new int[12];
        vecteur[indice-10]=1;
        return vecteur;
    }

    public static float dot(float[] x, float[] y) {
        float ps = 0;
        for (int i=0; i<x.length; i++) {
            ps+=x[i]*y[i];
        }
        return ps;
    }

    public static float[] InfPerceptron(float[] image, float[][] w1) {
        float[] probas = new float[12];
        float S = 0;

        for (int l=0; l<12; l++) {
            probas[l]=(float) Math.exp(dot(image,w1[l]));
            S+=probas[l];
        }

        for (int k=0; k<12; k++) {
            probas[k]/=S;
        }
        return probas;
    }

    public static int seed = 1234;
    public static Random GenRdm = new Random(seed);

    public static float[][] InitialiseW(int sizeW) {
        float alpha = 1/12;
        float[][] w = new float[12][sizeW];
        for (int i=0;i<12;i++) {
            for(int j=0; j<sizeW; j++) {
                w[i][j]=alpha*GenRdm.nextFloat();
            }
        }
        return w;
    }

    public static void mise_a_jour(int m, float[] image, int[] refs, float[][] w1, float eta) {
        float[] aa = InfPerceptron(image,w1);
        for (int l=0; l<12; l++) {
            for(int i=0; i<w1[0].length;i++) {
                w1[l][i]=w1[l][i]-eta*image[i]*(aa[l]-OneHot(refs[m])[l]);
            }
        }
    }

    public static void epoque(float eta, float[][] data, int[] refs, float[][] w1) {
        for (int m=0; m<data.length; m++) {
            mise_a_jour(m, data[m], refs, w1, eta);
        }
    }


    public static float CostFunction(float[][] w1, float[][] data, int refs[]) {
        float Etot = 0;
        float e=1;
        int M = data.length;
        for (int m=0; m<M; m++) {
            e=1;
            for (int l=0; l<12; l++) {
                e*=Math.pow(InfPerceptron(data[m],w1)[l],OneHot(refs[m])[l]);
            }
            Etot+=Math.log(e);
        }
        return Etot/M;
    }

    /*fonction max
     * donnée: tableau de float
     * return: le plus grand élément dans ce tableau*/
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

    /*Fonction Predict
    * données: tableau float de deux dimension represente les data , tableau float de deux dimension qui represente
    * les références
    * return: tableau de entiers qui contient les prediction de classe pour les data */
    public static int[]Predict(float[][]data,float[][]w){
        int[]RefPredict=new int[data.length];
        float[]Probas;
        for (int i = 0; i < data.length; i++) {
            Probas = InfPerceptron(data[i], w);
            RefPredict[i]=Max(Probas);
        }

        return RefPredict;
    }

/*Une fonction pour afficher les matrice*/
    private static void AfficherMatrice(int[][]M){
        for(int i=0;i<M.length;i++){
            for(int j=0;j<M.length;j++){
                System.out.print(M[i][j]+"  ");
            }
            System.out.println();
        }
    }

/*Fonction  CountFois
*données: Un tableau des entiers 'ref' qui contient tous les labels vraiment
* un tableau des entiers 'predict' qui contient les labels de prediction
* un entier 'classe' qui presente la class qu'on va mesurer
* un tanleau 'classBon' qui represente les qualité de predict, on va le modifier(ligne de matrice confusion )
* return : un entier qui presente les nombre des éléments de la class dans la prediction */
    private static int CountFois(int ref[],int[]predict,int classe,int classeBon[]){//classebon[12]
        int nbp=0;//nombre predict dans cette classe
        for(int i=0;i<ref.length;i++){
            if(predict[i]==classe) {
                nbp++;
                classeBon[ref[i]-10]+=1;
            }
        }
        return nbp;
    }

    /*Fonction clean, qui mettre tous les elements de tableau'tab' en 0*/
    private static void clean (int[]tab){
        for(int i=0;i<tab.length;i++){
            tab[i]=0;
        }
    }

    /*Fonction Matrice
    * données: tableau d'entier 'RefPredict' qui est le tableau contient tous les reférences qu'on a predit
    * tableau d'entiers 'ValidRefs'qui est le tanleau contient toue les références vrais
    * tableau d'entier predictV qui contient la qualité de chaque classe et on va le completer par cette fonction
    * return : une matrice de confulsion et aussi on modifier tableau predictV */

    public static int[][]Matrice(int[] RefPredict,int[]ValidRefs,int[]predictV){
        int[] Bonc = new int[12];//Bonc est la ligne de matrice confusion,represente pour chaque classe la qualité de prediction
        int[][] Mt = new int[13][13];// Matrice de confusion
        for (int i = 1; i < 13; i++) {
            Mt[0][i] = i + 9;
            Mt[i][0] = i + 9;
        }
        for (int i = 1; i < 13; i++) {//classe ligne
            predictV[i] = CountFois(ValidRefs, RefPredict, i + 9, Bonc);
            for (int k = 1; k < 13; k++) {//colone
                Mt[i][k] = Bonc[k - 1];
            }
            clean(Bonc);
        }// mettre les valeurs dans la matrice
        return Mt;
    }

    /*Fonction ProblemClass: pour savoir la class qui est le plus problèmatique
    * données: un tableau d'entiers contient la qualité de chaque class
    * une matrice de 'Mt' qui est la matrice de confulsion
    * return la class plus problèmatique et affiche une ligne pour dire la class plus problèmatiqe et les erreurs qu'il a eu*/
    public static int ProblemClass(int[]predictV,int[][]Mt){
        int Maxmalclasse = 0;// le plus problematique classe
        int classmal = 0;
        int malclasse;
        int MaxnbErruer;//le nb d'erreur de la classe plus problèmatique
        for (int i = 1; i < 13; i++) {
            malclasse = predictV[i] - Mt[i][i];
            if (malclasse > Maxmalclasse) {
                classmal = i + 9;
                Maxmalclasse = malclasse;
            }
        }
        MaxnbErruer = Mt[classmal - 9][classmal - 9] - Maxmalclasse;
        System.out.println("La class " + classmal + " est la class le plus problematique, il a " + MaxnbErruer + " erreurs");
        return classmal;
    }


    /*fonction Indix5Image: pour savoir les 5 indix d'image bien classés de la class plus problèmatique
    * données: tableau des entiers 'ref'qui contiens les references vrais
    * tableau des entiers 'predict' qui contient les references de prediction
    * un entier classe qui est la plus problèmatique classe
    * return un tableau des entiers */
    private static int[] Indix5Image(int ref[],int[]predict,int classe){//classebon[12]
        int[]indix=new int[5];//tableau d'indix
        int j=0;
        for(int i=0;i<ref.length;i++) {
            if (predict[i] == classe) {
                if(ref[i]==classe){
                    indix[j]=i;
                    j+=1;
                    if(j==5)
                        return indix;
                }
            }
        }
        return null;
    }

    /*fonction Indix5ImageMalclasse: pour savoir les 5 indix d'image mal classée de la class plus problèmatique
     * données: tableau des entiers 'ref'qui contiens les references vrais
     * tableau des entiers 'predict' qui contient les references de prediction
     * un entier classe qui est la plus problèmatique classe  */
    private static int[] Indix5ImageMalclasse(int ref[],int[]predict,int classe){//classebon[12]
        int[]indix=new int[5];//tableau d'indix
        int j=0;
        for(int i=0;i<ref.length;i++) {
            if (predict[i] == classe) {
                if(ref[i]!=classe){
                    indix[j]=i;
                    j+=1;
                    if(j==5)
                        return indix;
                }
            }
        }
        return null;
    }


    /*fonction CinqImages, pour afficher 5 images */
    public static void CinqImages(MnistReader db,int[]IndixImage,int[]Indix,String path,int BienouMal)throws IOException{
        int numberOfColumns = 28;//image.length;
        int numberOfRows = 28; //image[0].length;
        BufferedImage bimage = new BufferedImage(numberOfColumns, numberOfRows, BufferedImage.TYPE_BYTE_GRAY);
        int iAfficher;
        for (int fois = 0; fois < 5; fois++) {
            iAfficher = IndixImage[fois];
            int[][] aff = Image.BinariserImage(db.getImage(Indix[iAfficher]), 127);
            for (int i = 0; i < 28; i++) {
                for (int l = 0; l < 28; l++) {
                    int c = aff[i][l]; // ici 0 pour noir, 255 pour blanc
                    if (c == 1)
                        c = 255;
                    int rgb = new Color(c, c, c).getRGB();
                    bimage.setRGB(i, l, rgb);
                }
            }
            if(BienouMal==1){
                File outputfile = new File(path + "imagesBienClasse" + fois + ".png");
                ImageIO.write(bimage, "png", outputfile);
            }else{
                File outputfile = new File(path + "imagesMalclasse" + fois + ".png");
                ImageIO.write(bimage, "png", outputfile);
            }

        }
    }

    public static void main(String[] args)throws IOException {
        long a = System.currentTimeMillis();// debut de mesurer le temps pour classes inférées par le perceptron sur l'ensemble de validation
        String path = "";
        String labelDB = path + "train-labels-idx1-ubyte";
        String imageDB = path + "train-images-idx3-ubyte";
        MnistReader db = new MnistReader(labelDB, imageDB);
        int[][] image = db.getImage(1);
        int D = image.length * image[0].length + 1;
        float[] data = ImageOnlinePerceptron.ConvertImage(image);
        float[][] percep = InitialiseW(data.length);
        float proba[] = InfPerceptron(data, percep);
        float somme=0;
        for (int i = 0; i < proba.length; i++) {
            if (proba[i] < 0)
                System.out.println("Probabilite negatif");
            somme += proba[i];
        }
        int Na = 500;
        int[] trainRefs = new int[Na];
        float[][] trainData = new float[Na][D];
        int j = 0;
        int n = 0;
        while (n < Na) {
            if (db.getLabel(j + 1) < 22 && db.getLabel(j + 1) > 9) {
                trainRefs[n] = db.getLabel(j + 1);
                trainData[n] = Image.ConvertImage(Image.BinariserImage(db.getImage(j + 1), 127));
                n += 1;
            }
            j += 1;
        }
        float[][] w = Multi.InitialiseW(D);
        for (int t = 0; t < 50; t++) {
            Multi.epoque(0.01f, trainData, trainRefs, w);
        }
        /*Tableau de l'ensemble de validation*/
        int Nv = 1000;
        int[] ValidRefs = new int[Nv];
        float[][] ValidData = new float[Nv][D];
        int[] Indix = new int[Nv];
        n = 0;
        while (n < Nv) {
            if (db.getLabel(j + 1) < 22 && db.getLabel(j + 1) > 9) {
                ValidRefs[n] = db.getLabel(j + 1);
                Indix[n] = j + 1;
                ValidData[n] = Image.ConvertImage(Image.BinariserImage(db.getImage(j + 1), 127));
                n += 1;
            }
            j += 1;
        }


        int[] VrefPredict = Predict(ValidData, w);//VrePredict est le tableau de labels de predictions
        System.out.println("\r<br> Le temps pour excuter est  : " + (System.currentTimeMillis() - a) / 1000f + " second ");// fin de mesurer le temps
        System.out.println();

        //Matrice de conflusion
        int[] predictV = new int[13];//predictV est la qualité de prediction dans une tableau de taille 13
        int[][]Mt=Matrice(VrefPredict,ValidRefs,predictV);
        AfficherMatrice(Mt);
        //System.out.println(Arrays.toString(predictV));

        //Pour savoir le classe qui est le plus problèmatique
        int classmal=ProblemClass(predictV,Mt);

        //pour afficher 5 images bien classées par le perceptron mais avec le plus mauvais score d'inférence.
        int[] IndixImage = Indix5Image(ValidRefs, VrefPredict, classmal); //les indix de 5 images
        CinqImages(db,IndixImage,Indix,path,1);
        System.out.println("Success");

       //pour afficher 5 images mal classees par le perceptron  avec le plus mauvais score d'inférence.
        int[] IndixMalImage=Indix5ImageMalclasse(ValidRefs,VrefPredict,classmal);
        CinqImages(db,IndixMalImage,Indix,path,0);
    }
}
