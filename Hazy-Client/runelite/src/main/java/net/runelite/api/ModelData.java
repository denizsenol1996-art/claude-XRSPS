package net.runelite.api;

public interface ModelData extends Mesh<ModelData>, Renderable
{
    int DEFAULT_AMBIENT = 64;
    int DEFAULT_CONTRAST = 768;
    int DEFAULT_X = -50;
    int DEFAULT_Y = -10;
    int DEFAULT_Z = -50;

    /**
     * Gets colors as Jagex HSL
     *
     * @see JagexColor
     */
    short[] getFaceColors();

    /**
     * Lights a model.
     *
     * The produced model shares verticies, face transparencies, face indicies, and textures with
     * the underlying ModelData. If any of these may be mutated the corresponding {@code cloneX}
     * method should be called before {@code light}ing
     */
    Model light(int ambient, int contrast, int x, int y, int z);

    /**
     * Lights a model with default values
     *
     * @see #light(int, int, int, int, int)
     */
    Model light();

    /**
     * Applies a recolor using Jagex's HSL format. You should call {@link #cloneColors()} ()} before calling
     * this method
     */
    ModelData recolor(short colorToReplace, short colorToReplaceWith);

    /**
     * Applies a retexture, changing texture ids. You should call {@link #cloneTextures()} before calling
     * this method
     */
    ModelData retexture(short find, short replace);

    /**
     * Shallow-copies a model. Does not copy any arrays, which are still shared with this object.
     */
    ModelData shallowCopy();

    /**
     * Clones {@link #getVerticesX()}, {@link #getVerticesY()}, and {@link #getVerticesZ()} so
     * they can be safely mutated
     */
    ModelData cloneVertices();

    /**
     * Clones {@link #getFaceColors()} so they can be safely mutated
     */
    ModelData cloneColors();

    /**
     * Clones {@link #getFaceTextures()} so they can be safely mutated
     */
    ModelData cloneTextures();

    /**
     * Clones {@link #getFaceTransparencies()} so they can be safely mutated
     */
    ModelData cloneTransparencies();
}
