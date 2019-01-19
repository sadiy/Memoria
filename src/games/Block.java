package games;

import extensions.RGBColor;

public enum Block {

    SKY("Ciel" , RGBColor.SKYBLUE , RGBColor.SKYBLUE),
    CLOUD("Nuage" , RGBColor.WHITE , RGBColor.WHITE),
    DIRT("Terre" , RGBColor.BURLYWOOD , RGBColor.BURLYWOOD ),
    STONE("Pierre" , RGBColor.GRAY , RGBColor.GRAY),
    WATER("Eau" , RGBColor.AQUAMARINE , RGBColor.AQUAMARINE),
    LAVA("Lave" , RGBColor.ORANGERED , RGBColor.ORANGERED),
    WOOD("Bois" , RGBColor.BROWN , RGBColor.BROWN),
    BLACK_WOOD("Bois noir", RGBColor.BLACK, RGBColor.BLACK),
    GRASS("Terre" , RGBColor.GREEN , RGBColor.OLIVE),
    FOLIAGE("Feuillage" , RGBColor.OLIVE , RGBColor.OLIVE);

    protected String name;
    private RGBColor color;
    private RGBColor secondcolor;

    Block(String name, RGBColor color, RGBColor secondcolor){
        this.name = name;
        this.color = color;
        this.secondcolor = secondcolor;
    }

    public String getName() {
        return name;
    }

    public RGBColor getColor() {
        return color;
    }

    public RGBColor getSecondcolor() {
        return secondcolor;
    }
}
