package perceptron;
import java.util.Arrays;
/*
* On a les data, et tout d'abord, on avoir le module random, et on par tester le module,
* on va MAJ le module
* et on va counter on modifier le module combien fois en totale
* */

public class OnlinePerceptron {
    public static final int DIM = 3; // dimension de l'espace de representation
    public static float[] w = new float[DIM]; // parametres du modèle
    public static float[][] data = { // les observations
            {1,0,0}, {1,0,1} , {1,1,0},
            {1,1,1}
    };
    public static int[] refs = {-1, -1, -1, 1}; // les references
    public static int fois=0;
/*Quand on besoin de MAJ
* un etier 'reference'
* un tableau de float 'x' qui est la valeur de data[i]
* un tableau de float 'nouveau' qui est le nouveau module apres MAJ */
    public static void MAJ(float[]nouveau, float[]x, int dim,int reference,float eta) {
        nouveau[0]+=reference;
        for(int i=1;i<dim;i++) {
            nouveau[i]+=x[i]*(reference)*eta;
        }
    }//MAJ : reference=y; x est la valeur
    /*
    * Fonction pour tester est-ce que on a besoin de maj le module
    * on prend les données : un tableau 'donne' qui represente un ligne de data qui est un vecteur
    * un tableau 'sortie' qui represente les références (le module)
    * un entier 'dim' qui present les dimension de tableau
    * un float 'init' qui est la somme de (xi*wi)*reference */
    public static float produitS(float[]donne,float[]sortie,int reference,int dim,float ini) {
        float produitS=ini;
        for(int i=0;i<dim;i++) {
            produitS=produitS+donne[i]*sortie[i];
        }
        produitS=produitS*reference;
        return produitS;
    }// la sommes de (xi*wi)*reference


    public static float[] W(float[]x,int reference, int dim,float eta){
        float[] sortie=new float[dim];//on declarer un tableau avec le meme taille de data dim=3
        for(int i=0;i<dim;i++) {
            sortie[i]=5;//on initialise le tableau avec 5
        }
        float signe=produitS(x,sortie,reference,dim,0);// on tester est-ce qu'on besoin de MAJ le module
        while(signe<=0) {//si signe est negatif, on a besoin de MAJ
            MAJ(sortie,x,dim,reference,eta);
            signe=produitS(x,sortie,reference,dim,0);
            fois++;
        }
        return sortie;
    }// on fait la comparaison avec 0, pour savoir est-ce qu'on va modifier la w
    public static void main(String[] args) {
        // Exemple de boucle qui parcourt tous les exemples d'apprentissage
        // pour en afficher a chaque fois l'observation et la reference.
        /*DIM=3, 3 paramètre, il y a toujours un bias qui est 1 (w0).
         * */
        float[]wi=new float[data.length];// wi est le parametre de model
        for (int i = 0; i < data.length; i++) {// il y a combien data, on va MAJ le module combien fois
            float[] x = data[i];
            //System.out.println("x= "+Arrays.toString(x)+ " / y = "+refs[i]);
            float eta=(float)0.005;
            wi=W(x,refs[i],3,eta);//On MAJ le module
            System.out.println(Arrays.toString(wi));
        }
        System.out.println(fois);
    }
}
