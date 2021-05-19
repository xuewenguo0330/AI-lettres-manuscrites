/*package perceptron;

public class Test {
    public class OnlinePerceptron {

        public static int DIM = 3; // dimension de l'espace de representation
        public static float[] w = new float[DIM]; // parametres du modèle
        public static float[][] data = { // les observations
                {1,0,0}, {1,0,1} , {1,1,0},
                {1,1,1}
        };
        //   public static float eta=(float)0.005;

        public static int fois=0;
        public static int[] refs = {-1, -1, -1, 1}; // les references

        public static void MAJ(float[]donne, float[]valeur, int dim,int reference,float eta) {
            donne[0]+=reference;
            for(int i=1;i<dim;i++) {
                donne[i]+=valeur[i]*(reference)*eta;
            }
        }//MAJ : reference=y; x est la valeur
        public static float produitS(float[]donne,float[]sortie,int reference,int dim,float ini) {
            float produitS=ini;
            for(int i=0;i<dim;i++) {
                produitS=produitS+donne[i]*sortie[i];
            }
            produitS=produitS*reference;
            return produitS;
        }// la sommes de (xi*wi)*reference

        public static float[] PoidsDuPerceptron(float[]x,int reference, int dim,float eta){
            float[] sortie=new float[dim];
            for(int i=0;i<dim;i++) {
                sortie[i]=5;
            }
            float signe=produitS(x,sortie,reference,dim,0);
            while(signe<=0) {
                MAJ(sortie,x,dim,reference,eta);
                signe=produitS(x,sortie,reference,dim,0);
                fois++;
            }
            return sortie;
        }// on fait la comparaison avec 0, pour savoir est-ce qu'on va modifier la w

        public static void main(String[] args) {

            float[]wi=new float[data.length];
            for (int i = 0; i < data.length; i++) {
                float[] x = data[i];
                //System.out.println("x= "+Arrays.toString(x)+ " / y = "+refs[i]);
                float eta=(float)0.005;
                wi=PoidsDuPerceptron(x,refs[i],3,eta);
                System.out.println(Arrays.toString(wi));
            }
            System.out.println(fois);
        }


    }
}*/
