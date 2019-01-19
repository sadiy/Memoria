import extensions.CSVFile;
import extensions.Image;
import utils.Donnees;
import games.World;
import games.Item;

public class Quizz extends Program {

    String[] infos = new String[]{
            Donnees.IDENTIFIANT.valeurDefaut(),
            Donnees.SCORE.valeurDefaut(),
            Donnees.NIVEAU_ATTEINT.valeurDefaut()};
    boolean connecte = false, jouer = false;
    char [] alphabet = new char[62];

    void algorithm() {
        creer_alphabet();

        int selection = selectionMenu();

        while(selection != 1){

            if(selection == 2){
                if(!connecte){
                    connecte = connexionCompte();
                }else{
                    connecte = false;
                    infos = new String[]{
                            Donnees.IDENTIFIANT.valeurDefaut(),
                            Donnees.SCORE.valeurDefaut(),
                            Donnees.NIVEAU_ATTEINT.valeurDefaut()};
                }
            }else{
                wiki();
            }

            selection = selectionMenu();
        }

        jouerJeu();
    }







    /*****************************************************************************
     *                                                                            *
     *               FONCTIONS RESERVE A LA SELECTION DU MENU                     *
     *                                                                            *
     *  selectionMenu -> Choix d'une option et affichage du menu                  *
     *  connexionCompte -> Connexion d'un joueur à son compte                     *
     *  jouerJeu -> Fonction permettant d'accèder au jeu                          *
     *                                                                            *
     ******************************************************************************/

    int selectionMenu() {
        String[] menu = new String[]{"Jouer (" + infos[0] + ")", "Connexion", "Wiki"};

        if (connecte) menu[1] = "Déconnexion";

        for (int idx = 0; idx < length(menu); idx++) {
            println((idx + 1) + "- " + menu[idx]);
        }

        int selection, taille = length(menu);

        do {

            println("Veuillez entrer une option valide [1-" + taille + "] :");
            selection = readInt();

        } while (!(selection > 0 && selection <= taille));

        return selection;
    }

    boolean connexionCompte() {
        String id;
        boolean valid;

        do {
            println("Entrer un identifiant :");
            id = toLowerCase(readString());

            valid = estValide(id) && !equals(id, "test");

            if (!valid) {
                println("\nVeuillez uniquement entrez des caractères alphanumériques [Aa-Zz][0-9]");
            }

        } while (!valid);

        if (!possedeCompte(id)) {
            println("Identifiant inconnu, voulez-vous créer un compte avec cet identifiant ? (y/n)");

            String rep = readString();

            if(equals(toLowerCase(rep), "y") || equals(toLowerCase(rep), "yes")) {
                String motdepasse;

                do {
                    println("Entrez un mot de passe : ");
                    motdepasse = readString();

                    valid = estValide(motdepasse);

                    if (!valid) {
                        println("\nVeuillez uniquement entrez des caractères alphanumériques [Aa-Zz][0-9]");
                    }
                } while (!valid);

                creerCompte(id, crypter(motdepasse));

            }else{
                return false;
            }
        }else{
            String motdepasse;
            boolean occurence = false;

            do{
                println("Entrez votre mot de passe : ");
                motdepasse = readString();

                valid = estValide(motdepasse);

                if (!valid) {
                    println("\nVeuillez uniquement entrez des caractères alphanumériques [Aa-Zz][0-9]");
                }else {
                    occurence = equals(motdepasse, decrypter(corrigerString(infosCompte(id)[3])));

                    if(!occurence){
                        println("\nVotre mot de passe ne correspond pas");
                    }
                }
            }while(!valid || !occurence);

        }

        infos = infosCompte(id);

        return true;
    }

    void jouerJeu(){
        jouer = true;

        if(!connecte){
            println("Voici les règles du jeu :");

            defilerTexte("Un monde apparait avec différents objets, et l'objectif est de retenir un maximum d'informations sur chaque image.", 55
            );
            defilerTexte("Chaque tour, le niveau en difficulté augmente, et chaque tour devient plus complexe", 55);
            defilerTexte("Vous disposez de 3 vies et à chaque fin de tour, vous avez l'occasion de vous rattraper avec 2 questions", 55);

            println("\n                            [ENTREE]                                 ");
            String s;

            do{
                s = readString();
            }while (!equals(s, ""));
        }

        println("...");
        jeuDeMemoire();
    }

    void wiki(){
        println("Wiki très complet");
    }









    /*****************************************************************************
     *                                                                            *
     *                          FONCTIONS UTILES                                  *
     *                                                                            *
     *  estValide -> Vérifie si une chaine de caractère est bien alphanumérique   *
     *  afficherTable -> Conversion d'un tableau en chaine de caractère           *
     *  defilerTexte -> Affiche le texte avec un délai d'affichage par caracctère *
     *                                                                            *
     ******************************************************************************/

    boolean estValide(String str) {
        str = toLowerCase(str);

        for (int idx = 0; idx < length(str); idx++) {
            char c = charAt(str, idx);

            if (!(c >= alphabet[0] && c < alphabet[8]) && !(c >= alphabet[8] && c < alphabet[34]) && !(c >= alphabet[34] && c <= alphabet[61])) {
                return false;
            }
        }

        return true;
    }

    void defilerTexte(String texte, int delai){

        for(int idx = 0; idx < length(texte); idx++){
            delay(delai);
            print(charAt(texte, idx));
        }

        println();
    }

    void creer_alphabet(){

        int curseur = 0;
        for(int nbr = (int)'0';nbr<=(int)'9';nbr++){
            alphabet[curseur] = (char)nbr;
            curseur++;
        }
        for(int maj = (int)'A';maj<=(int)'Z';maj++){
            alphabet[curseur] = (char)maj;
            curseur++;
        }
        for(int min = (int)'a';min<=(int)'z';min++){
            alphabet[curseur] = (char)min;
            curseur++;
        }
    }

    String crypter(String str){

        String crypte = "";
        int curs;
        int cpt;
        for(int i=0;i<length(str);i++){
            curs = -1;
            cpt=0;
            while( curs<0 ){
                if( alphabet[cpt] == charAt(str,i) ){
                    curs = cpt;
                }
                cpt++;
            }
            crypte = crypte + alphabet[( ( curs * 29721 )%62 )];
        }
        return crypte;
    }

    String decrypter(String str){

        String decrypte = "";
        int curs;
        int cpt;
        for(int i=0;i<length(str);i++){
            curs = -1;
            cpt=0;
            while( curs<0 ){
                if( alphabet[cpt] == charAt(str,i) ){
                    curs = cpt;
                }
                cpt++;
            }
            decrypte = decrypte + alphabet[( ( curs * 27 )%62 )];
        }
        return decrypte;
    }

    String corrigerString(String str){
        String mot = "";

        for(int i=0; i < length(str); i++){
            if(charAt(str, i) != ' '){
                mot += charAt(str, i);
            }
        }

        return mot;
    }









    /*****************************************************************************
     *                                                                            *
     *               FONCTIONS RESERVE AU SYSTEME DE COMPTE                       *
     *                                                                            *
     *  creerCompte -> Créer un fichier CSV comportant les infos du joueur        *
     *  possedeCompte -> Vérification de l'existence du compte                    *
     *  infosCompte -> Renvoie le contenu du fichier CSV du joueur                *
     *                                                                            *
     ******************************************************************************/

    void creerCompte(String identifiant, String motdepasse) {
        saveCSV(new String[][]{{identifiant + ","+Donnees.SCORE.valeurDefaut()+","+Donnees.NIVEAU_ATTEINT.valeurDefaut()+","+motdepasse}}, identifiant + ".csv");
    }

    boolean possedeCompte(String identifiant) {
        String[] files = getAllFilesFromCurrentDirectory();

        int idx = 0;
        boolean exist;

        identifiant += ".csv";

        do {
            exist = equals(files[idx], identifiant);
            idx++;
        } while (!exist && idx < length(files));

        return exist;
    }

    String[] infosCompte(String identifiant) {
        CSVFile compte = loadCSV(identifiant + ".csv", ',');
        String[] tab = new String[columnCount(compte)];

        for (int idx = 0; idx < length(tab); idx++) {
            tab[idx] = getCell(compte, 0, idx);
        }

        return tab;
    }






    /*****************************************************************************
     *                                                                            *
     *                      FONCTIONS RESERVE AU JEU                              *
     *                                                                            *
     *  sauvegarde -> Permet la sauvegarde de la partie d'un joueur               *
     *                                                                            *
     ******************************************************************************/

    void sauvegarde(){
        saveCSV(new String[][]{infos}, infos[0] + ".csv");
    }

    void nouvelleValeur(Donnees type, String valeur){
        switch (type){
            case IDENTIFIANT:
                infos[0] = valeur;
                break;
            case SCORE:
                infos[1] = valeur;
                break;
            case NIVEAU_ATTEINT:
                infos[2] = valeur;
                break;
            default:
                break;
        }
    }

    void incrementerValeur(Donnees type, int i){
        switch (type){
            case SCORE:
                infos[1] = ""+(ToInt(infos[1])+i);

                break;
            case NIVEAU_ATTEINT:
                infos[2] = ""+(ToInt(infos[2])+i);
                println("int: "+infos[2]);
                break;
            default:
                break;
        }
    }

    String recupererValeur(Donnees type){
        switch (type) {
            case IDENTIFIANT:
                return infos[0];
            case SCORE:
                return infos[1];
            case NIVEAU_ATTEINT:
                return infos[2];
            default:
                return "0";
        }
    }


    int quizz(String fichier, String theme, int max){
        String[][] quizz = contenuCSVFILE(fichier, ',', 10);
        if(length(quizz) == 0) return -1;

        String[] questions = triAleatoire(listeQuestions(quizz, theme), 1);
        int score = 0;

        score = affichage(questions, theme, score, max);

        incrementerValeur(Donnees.NIVEAU_ATTEINT, score);
        println(toString(infos));

        return score;
    }

    int quizzGeneral(String fichier, int max){
        String[][] quizz = contenuCSVFILE(fichier, ',', 10);
        if(length(quizz) == 0) return -1;


        String[] questions = triAleatoire(listeQuestions(quizz), 0);

        String theme = "Culture générale";
        int score = 0;


        score = affichage(questions, theme, score, max);

        return score;
    }

    int affichage(String[] questions, String theme, int score, int max){
        int idx = 0;
        do{
            String[] question = couperChaine(questions[idx], '?');
            String bonneRep = nettoyerChaine(question[1]);
            String[] reponses = triAleatoire(couperChaine(question[2], ';'), 0);

            println(theme+" - "+question[0]+"?");
            println();

            int pos = 0, bRep = 0;
            for(int i = 0; i < length(reponses); i++){
                if(reponses[i] != null && !equals(reponses[i], "")){
                    pos++;
                    println(pos+" - "+reponses[i]);
                    if(equals(nettoyerChaine(reponses[i]), bonneRep)) bRep = i;
                    reponses[i]=""+pos;
                }
            }

            String repJoueur = nettoyerChaine(readString());

            if(equals(repJoueur, bonneRep) || equals(repJoueur, reponses[bRep])){
                score++;
            }

            idx++;
        }while(idx < max && idx < length(questions) && questions[idx] != null);

        return score;
    }

    String[] listeQuestions(String[][] quizz){
        String[] tab = new String[length(quizz, 1)*length(quizz, 2)];

        int idx = 0;

        for(int i=0; i < length(quizz, 1); i++){
            for(int j=0; j < length(quizz, 2); j++){
                String c = quizz[i][j];

                if(c != null && j != 0){
                    tab[idx] = c;
                    idx++;
                }
            }
        }

        return tab;
    }

    String[] listeQuestions(String[][] quizz, String theme){
        int indiceLigne = -1;

        do{
            indiceLigne++;
        }while(indiceLigne < length(quizz, 2) && !equals(toLowerCase(quizz[indiceLigne][0]), toLowerCase(theme)));

        if(indiceLigne == length(quizz, 2)-1) return new String[0];

        return quizz[indiceLigne];
    }

    String[][] contenuCSVFILE(String repertoire, char separateur, int questionMax){
        CSVFile file = loadCSV(repertoire);
        int row = rowCount(file);
        String[][] contenu = new String[row][questionMax];

        for(int j = 0; j < row; j++){
            int column = columnCount(file, j);
            for(int k = 0; k < column; k++){
                contenu[j][k] = getCell(file, j, k);
            }
        }

        return contenu;
    }







    void jeuDeMemoire(){
        String[] questions = new String[]{"Quel est la couleur de l'objet ", "Combien y a t-il de "};
        int idQ = 0,vie = 3,score = 0, difficulty = ToInt(recupererValeur(Donnees.NIVEAU_ATTEINT));

        while (vie > 0 && idQ < length(questions) && difficulty < 100){
            World monde = new World(130, 60, difficulty);
            affichageinit(monde);
            int q = 0;

            do{
                if(difficulty > 6){
                    idQ = 1;
                }

                String question = questions[idQ];
                Item item = monde.randomItem();

                String reps[] = new String[4];
                String bonneRep;
                boolean valid;

                if(idQ == 0){
                    bonneRep = item.color;

                    String[] couleurs = triAleatoire(new String[]{"Gris", "Noir", "Marron", "Vert", "Bleu", "Rouge", "Blanc", "Jaune", "Creme", "Beige", "Rose"}, 0);

                    reps[0] = bonneRep;
                    reps[1] = couleurs[0];
                    reps[2] = couleurs[1];
                    reps[3] = couleurs[2];

                    reps = triAleatoire(reps, 0);
                    valid = reponse(question+item.name+" ?", reps, bonneRep);
                }else{
                    bonneRep = ""+item.quantity;

                    println(question+item.name+" ? | Vie : "+vie);


                    println("["+(int) random()*item.quantity+"-"+(int) (random()*(monde.difficulty/5)+item.quantity+1)+"]");

                    String rep = nettoyerChaine(readString());

                    valid = equals(rep, bonneRep);
                }

                if(valid){
                    score++;
                    difficulty++;
                }else {
                    vie--;
                }

                q++;
            }while (vie > 0 && difficulty < 100 && q < (monde.difficulty/20+3));

            println("QUESTION FLASH (BONUS VIE) : ");

            vie += quizzGeneral("quizz.csv", 1);
            println("Vie : "+vie+" | Difficulté : "+difficulty);


            nouvelleValeur(Donnees.NIVEAU_ATTEINT, ""+difficulty);
            incrementerValeur(Donnees.SCORE, score);

            if(connecte) sauvegarde();
        }

        if(difficulty == 100){
            println("FIN DU JEU : Bravo vous avez atteint le niveau maximum");
        }else{
            println("Bravo ! Votre score : "+score+" point(s) et "+vie+" vie(s)!");
        }

    }

    void affichageinit(World world){
        Image img = newImage(10*world.windowX,10*world.windowY);// image
        setGrid( img, world.windowY, world.windowX); // matrice
        for (int l=0;l<length(world.blocks,1);l++){
            for (int c=0;c<length(world.blocks,2);c++){
                set( img,l,c, world.blocks[l][c].getColor());
            }
        }

        show(img);
        delay(15000-world.difficulty*100);
        close(img);
    }

    boolean reponse(String question, String[] reps, String bonneRep){
        println(question);

        for(int i = 0; i < length(reps); i++){
            println((i+1)+" - "+reps[i]);
        }

        String rep = readString();

        if(equals(rep, bonneRep) || length(rep) != 0 && length(rep) < 2 && charAt(rep, 0) != '\n' && stringToInt(rep) <= 5 && stringToInt(rep) > 0 && equals(reps[stringToInt(rep)-1], bonneRep)){
            return true;
        }else{
            return false;
        }
    }





    String toString(String[] tab){
        String str = "";

        for(int i = 0; i < length(tab); i++){
            str+=tab[i]+",";
        }

        return substring(str, 0, length(str)-1);
    }

    String toString(String[][] tab){
        String str = "";

        for (int j=0; j < length(tab, 1); j++){
            str += toString(tab[j])+"\n";
        }

        return str;
    }

    int ToInt(String str){
        int v=0;

        for(int i=0; i<length(str);i++){
            v+=(charToInt(charAt(str, i))*(10^i));
        }

        return v;
    }


    String[] couperChaine(String str, char separateur){
        if(str == null) return new String[0];
        String[] table = new String[20];

        String mot = "";
        int w = 0;

        for(int i = 0; i < length(str); i++){
            char c = charAt(str, i);

            if(charAt(str, i) != separateur){
                mot += c;
            }else{
                table[w] = mot;
                w++;

                mot = "";
            }
        }

        table[w] = mot;

        int taille = 0;

        while(taille < length(table) && table[taille] != null){
            taille++;
        }

        String[] copie = new String[taille];

        for(int i = 0; i < taille; i++){
            copie[i] = table[i];
        }

        return copie;
    }

    String nettoyerChaine(String str){
        int start = 0;

        if(length(str) == 0) return str;

        while(start < length(str)){
            char c = charAt(str, start);

            if(c != ' '){
                break;
            }

            start++;
        }

        int end = (length(str)-1);

        while(end >= 0){
            char c = charAt(str, end);

            if(c != ' '){
                break;
            }

            end--;
        }

        return toLowerCase(substring(str, start, end+1));
    }



    String[] triAleatoire(String[] tab, int start){
        int taille = 0;

        while(taille < length(tab) && tab[taille] != null){
            taille++;
        }

        String[] tabTrie = new String[taille-start];

        for(int idx=start; idx < taille; idx++){
            int r;

            do{
                r = (int) (random()*(length(tabTrie)));
            }while(tabTrie[r] != null && !equals(tabTrie[r], " "));

            tabTrie[r] = tab[idx];
            tab[idx] = null;
        }

        return tabTrie;
    }






    void testContenuCSVFILE(){
        String[][] content = new String[][]{{"un","deux","trois"}, {"quatre", "cinq", "six"}};
        saveCSV(content, "test.csv");

        assertArrayEquals(content, contenuCSVFILE("test.csv", ',', length(content, 2)));
    }

    void testNettoyerChaine(){
        String test = " Bla bla bla ";

        assertEquals(nettoyerChaine(test), "bla bla bla");
    }

    void testCouperChaine(){
        String test = "un, deux, trois";

        assertArrayEquals(couperChaine(test, ','), new String[]{"un", " deux", " trois"});
    }

    void testMatrixToString(){
        String[][] matrix = new String[][]{{"un","deux","trois"}, {"quatre", "cinq", "six"}};

        assertEquals(toString(matrix), "un,deux,trois\nquatre,cinq,six\n");
    }

    void testTableToString(){
        String[] tab = new String[]{"un", "deux", "trois"};

        assertEquals(toString(tab), "un,deux,trois");
    }

    /* FONCTIONS RESERVE AUX EVENEMENTS ET INTERACTIONS AVEC LE CLIENT */

    @Override
    void mouseHasMoved(int x, int y) {

    }

    @Override
    void mouseChanged(String name, int x, int y, int button, String event) {

    }

    @Override
    void mouseIsDragged(int x, int y, int button, int clickCount) {
    }

    @Override
    void keyChanged(char c, String event) {

    }

    @Override
    void textEntered(String text) {

    }
}
