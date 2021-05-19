package Image;
import mnisttools.MnistReader;

public class LectureImage {
    /*Fonction qui binarise une image,
    * prendre les donnees : une tableau image de deux dimension
    * un entier seuil doit etre entre 0 et 255 qui represente un niveau de gris
    * return un tableau image d'entier contient 0 et 1
    * on change le parametre image */

    public static int[][] binarisation(int[][] image, int seuil){
        for(int colonne=0;colonne<image.length;colonne++) {
            for(int ligne=0;ligne<image[0].length;ligne++) {
                if(image[colonne][ligne]<=seuil) {
                    image[colonne][ligne]=0;
                }
                else {
                    image[colonne][ligne]=1;
                }
            }
        }
        return image;
    }

    public static void main(String[] args) {
        String path="/Users/xuewen/Desktop/S3/l'apprentissage automatique/Mini-projet1"; // TODO : indiquer le chemin correct
        String labelDB=path+"/train-labels-idx1-ubyte";
        String imageDB=path+"/train-images-idx3-ubyte";
        // Creation de la base de donnees
        MnistReader db = new MnistReader(labelDB, imageDB);
        // Acces a la premiere image
        int idx = 100; // une variable pour stocker l'index
        // Attention la premiere valeur est 1.
        int [][] image = db.getImage(idx); /// On recupere la premiere l'image numero idx
        // Son etiquette ou label
        int label = db.getLabel(idx);
        // Affichage du label
        System.out.print("Le label est "+ label+"\n");
        // note: le caractère \n est le 'retour charriot' (retour a la ligne).
        // Affichage du nombre total d'image
        System.out.print("Le total est "+ db.getTotalImages()+"\n");
        /* A vous de jouer pour la suite */

        //On veux tester la fonction binarisation
        /*
        for(int colonne=0;colonne<image.length;colonne++) {
            for(int ligne=0;ligne<image[0].length;ligne++) {
                System.out.print(image[ligne][colonne]);
            }
        }*/
        int seuil=150;
        binarisation(image,seuil);
        System.out.println("Seuil= "+seuil);
        for(int colonne=0;colonne<image.length;colonne++) {
            for(int ligne=0;ligne<image[0].length;ligne++) {
                System.out.print(image[ligne][colonne]);
            }
            System.out.println();
        }
        System.out.println();
        //un affichage console qui affiche un espace lorsque la valeur est 0 et un ''X'' lorsqu'elle est à 1
        for(int colonne=0;colonne<image.length;colonne++) {
            for(int ligne=0;ligne<image[0].length;ligne++) {
                if(image[ligne][colonne]==1)
                    System.out.print("X");
                else
                    System.out.print(" ");
            }
            System.out.println();
        }
    }




}
