package games;

import java.util.Random;

public class World {

    public int windowX = 0, windowY = 0;
    public Block blocks[][];
    public int difficulty;
    public Item[] items = new Item[]{new Item("Arbre", 0, "Marron"), new Item("Tonneau", 0, "Noir"), new Item("Lac d'eau", 0, "Bleu"),
            new Item("Lac de lave", 0, "Rouge"), new Item("Buisson", 0, "Vert"), new Item("Puit", 0, "Gris"), new Item("Maison", 0, "Gris")};

    public World(int windowX, int windowY, int difficulty){
        this.windowX = windowX;
        this.windowY = windowY;
        this.difficulty = difficulty;

        blocks = new Block[windowY][windowX];

        flat();

        int a = 20;
        int b = 5;
        int decalage = 0;

        while( b<windowX-5 ){
            decalage = genererRandom(a,b);
            b = b + decalage+(10-difficulty/10);
        }
    }

    public void addBlock(int x, int y, Block block){
        if(isCorrectPosition(x, y)) {
            blocks[x][y] = block;
        }
    }

    public Item randomItem(){
        int r = random(items.length);

        return items[r];
    }

    Block randomBlock(int proba , Block block1 , Block block2 ){
        Block block;

        if((int)(random(100)) <= proba ){
            block = block1;
        }else{
            block = block2;
        }

        return block;
    }

    void flat(){
        for(int l=0;l<blocks.length;l++){
            for(int c=0;c< blocks[l].length;c++){
                if (l >=20){
                    if ( l <40 ){
                        addBlock(l,c,randomBlock((l-20)*10-60, Block.DIRT, Block.GRASS));
                        //W_placeBlock(l,c,dirt);
                    }else{
                        addBlock(l,c,randomBlock((l-40)*8, Block.STONE, Block.DIRT));
                    }
                }else if (l >= 10){
                    addBlock(l,c,randomBlock( 99, Block.SKY , Block.CLOUD ));
                }else{
                    addBlock(l,c,randomBlock( 90, Block.SKY , Block.CLOUD ));
                }
            }
        }
    }

    int genererRandom(int param_l , int param_c){
        int random = random(100);
        int decalage=0;

        if (random <10){
            decalage = tree(param_l,param_c);
            items[0].quantity++;
        }else if(random<20){
            decalage = barrel(param_l,param_c);
            items[1].quantity++;
        }else if(random<30){
            decalage = lack(param_l,param_c, Block.WATER);
            items[2].quantity++;
        }else if(random<40){
            decalage = lack(param_l,param_c, Block.LAVA);
            items[3].quantity++;
        }else if(random<50){
            decalage = bush(param_l,param_c);
            items[4].quantity++;
        }else if(random<55){
            decalage = well(param_l,param_c);
            items[5].quantity++;
        }else if(random<60){
            decalage = house(param_l,param_c);
            items[6].quantity++;
        }else if(random<80){
            addBlock(param_l,param_c, Block.SKY);
            decalage = 1;
        }else{
            addBlock(param_l-1,param_c, Block.GRASS);
            decalage = 1;
        }

        return decalage;
    }

    int tree(int param_l , int param_c){
        int randomH = random(4)+6;
        int randomW = random(3)+4;

        for(int l=param_l-1;l > param_l - randomH ; l--){
            addBlock(l,param_c, Block.WOOD);
        }

        for(int l = param_l - randomH ; l > param_l - randomH*2; l--){
            for(int c = param_c - randomW -1; c < (param_c - randomW)+2*randomW ;c++){
                addBlock(l,c,randomBlock( 80, Block.FOLIAGE , Block.SKY ));
            }
        }

        return 2;
    }


    int lack(int param_l , int param_c, Block param_block){
        int randomH = random(8)+6;
        int randomW = random(20)+4;
        int decal = randomW + 2;

        for(int l=param_l; l<param_l + randomH;l++){
            for(int c=param_c;c<param_c+randomW;c++){
                if(l == param_l){
                    addBlock(l,c, Block.SKY);
                }else{
                    addBlock(l,c, param_block);
                }
            }
            if(randomW>0){
                randomW=randomW-2;
                param_c++;
            }
        }

        return decal;
    }

    int barrel(int param_l , int param_c){
        addBlock(param_l-1,param_c, Block.BLACK_WOOD);
        addBlock(param_l-2,param_c, Block.BLACK_WOOD);

        return 2;
    }

    int bush(int param_l , int param_c){
        addBlock(param_l-1,param_c, Block.FOLIAGE);
        addBlock(param_l-2,param_c, Block.FOLIAGE);
        addBlock(param_l-1,param_c+1, Block.FOLIAGE);
        addBlock(param_l-2,param_c+1, Block.FOLIAGE);

        return 3;
    }

    int house(int param_l , int param_c){
        int randomW = random(7)+4;
        int randomH = random(4)+4;

        for(int l=param_l-1;l>param_l-randomH;l--){
            for(int c = param_c;c<param_c+randomH;c++){
                if (l == param_l-1){
                    addBlock(l,c, Block.STONE);
                }else if(l == param_l-randomH+1){
                    addBlock(l,c, Block.STONE);
                }else{
                    addBlock(l,c, Block.WOOD);
                }
            }
        }
        return randomW + 2;
    }

    int well(int param_l , int param_c){
        int randomW = random(2)+4;
        int randomH = random(6)+6;

        addBlock(param_l-1,param_c, Block.STONE);
        addBlock(param_l-1,param_c+randomW-1, Block.STONE);

        for(int l=param_l;l<param_l+randomH;l++){
            for(int c = param_c;c<param_c+randomW;c++){
                if(c != param_c && c != param_c+randomW-1) {
                    if (l <= param_l+randomH-3 ){
                        addBlock(l,c, Block.SKY);
                    }else{
                        if(l == param_l+randomH-1){
                            addBlock(l,c, Block.STONE);
                        }else{
                            addBlock(l,c, Block.WATER);
                        }
                    }
                }else{
                    addBlock(l,c, Block.STONE);
                }
            }
        }
        return randomW+2;
    }

    public boolean isCorrectPosition(int x, int y){
        return (x < windowY && x >= 0 && y < windowX && y >= 0);
    }

    private int random(int interval){
        return new Random().nextInt(interval);
    }
}
