package com.hazy.collection;

import net.runelite.rs.api.RSNode;

public class Node implements RSNode {

    public long key;
    public Node prev;
    public Node next;

    public final void remove() {
        if (next != null) {
            next.prev = prev;
            prev.next = next;
            prev = null;
            next = null;
        }
        onUnlink();
    }

    public final boolean hasPrevious() {
        if(next == null)
            return false;

        return true;

    }

    @Override
    public RSNode getNext() {
        return next;
    }

    @Override
    public void setPrevious(RSNode var1) {

    }

    @Override
    public long getHash() {
        return key;
    }

    @Override
    public void setNext(RSNode var1) {

    }

    @Override
    public RSNode getPrevious() {
        return prev;
    }

    @Override
    public void unlink() {
        remove();
    }

    @Override
    public void onUnlink() {

    }
}
