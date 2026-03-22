package com.hazy.entity;

import com.hazy.collection.Cacheable;
import com.hazy.entity.model.Model;
import com.hazy.entity.model.VertexNormal;
import net.runelite.rs.api.RSModel;
import net.runelite.rs.api.RSNode;
import net.runelite.rs.api.RSRenderable;

public class Renderable extends Cacheable implements RSRenderable {

    public int modelBaseY;
    public VertexNormal[] normals;

    public void setSceneId(int sceneId) {
        this.sceneId = sceneId;
    }
    public int sceneId = (int) (System.currentTimeMillis() / 1000L);

    public void renderAtPoint(int i, int j, int k, int l, int i1, int j1, int k1, int l1, long i2) {
        Model model = get_rotated_model();
        if(model != null) {
            this.modelBaseY = model.modelBaseY;
            model.renderAtPoint(i, j, k, l, i1, j1, k1, l1, i2);
            sceneId++;
        }
    }

    protected Model get_rotated_model() {
        return null;
    }


    @Override
    public void draw(int orientation, int pitchSin, int pitchCos, int yawSin, int yawCos, int x, int y, int z, long hash) {
        renderAtPoint(orientation,pitchSin,pitchCos,yawSin,yawCos,x,y,z,hash);
    }

    @Override
    public boolean isHidden() {
        return false;
    }

    public Renderable() {
        modelBaseY = 1000;
    }

    //TODO clear underlays etc

    @Override
    public int getModelHeight() {
        return modelBaseY;
    }

    @Override
    public void setModelHeight(int modelHeight) {
        modelBaseY = modelHeight;
    }

    @Override
    public RSModel getModel() {
        return get_rotated_model();
    }


    @Override
    public RSNode getNext() {
        return null;
    }

    @Override
    public void setPrevious(RSNode var1) {

    }

    @Override
    public long getHash() {
        return 0;
    }

    @Override
    public void setNext(RSNode var1) {

    }

    @Override
    public RSNode getPrevious() {
        return null;
    }

    @Override
    public void unlink() {

    }

    @Override
    public void onUnlink() {

    }

}
