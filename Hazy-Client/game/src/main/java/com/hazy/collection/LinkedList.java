package com.hazy.collection;

import net.runelite.rs.api.RSNode;
import net.runelite.rs.api.RSNodeDeque;

import java.util.Iterator;

public final class LinkedList implements RSNodeDeque {

    public final Node head;
    private Node current;

    public void transfer(LinkedList list) {
        Node start = head.prev;
        Node prev = head.next;
        head.next = start.next;
        start.next.prev = head;
        if (head == start) {
            return;
        }
        start.next = list.head.next;
        start.next.prev = start;
        prev.prev = list.head;
        list.head.next = prev;
    }

    public LinkedList() {
        head = new Node();
        head.prev = head;
        head.next = head;
    }

    public void insertBack(Node node) {
        if (node.next != null)
            node.remove();

        node.next = head.next;
        node.prev = head;
        node.next.prev = node;
        node.prev.next = node;
    }

    public void addFirst(Node node) {
        //ken comment, part of twisted bow ground item fix
        if (node == null)
            return;
        if (node.next != null)
            node.remove();
        node.next = head;
        node.prev = head.prev;
        node.next.prev = node;
        node.prev.next = node;
    }

    public void insert(Node node) {
        if (node.prev != null) {
            node.remove();
        }

        node.prev = head.prev;
        node.next = head;
        node.prev.next = node;
        node.next.prev = node;
    }

    public Node get() {
        Node node = head.next;
        if (node == head) {
            return null;
        } else {
            node.remove();
            return node;
        }
    }

    public Node pop() {
        Node node = head.prev;
        if (node == head) {
            return null;
        } else {
            node.remove();
            return node;
        }
    }

    public Node first() {
        Node node = head.prev;
        if (node == head) {
            current = null;
            return null;
        } else {
            current = node.prev;
            return node;
        }
    }

    @Override
    public RSNode getCurrent() {
        return current;
    }

    @Override
    public RSNode getSentinel() {
        return head;
    }

    public Node last() {
        Node node = head.next;
        if (node == head) {
            current = null;
            return null;
        } else {
            current = node.next;
            return node;
        }
    }

    public Node next() {
        Node node = current;
        if (node == head) {
            current = null;
            return null;
        } else {
            current = node.prev;
            return node;
        }
    }

    public Node previous() {
        Node node = current;
        if (node == head) {
            current = null;
            return null;
        }
        current = node.next;
        return node;
    }

    @Override
    public void addFirst(RSNode val) {

    }

    @Override
    public RSNode removeLast() {
        return null;
    }

    public void clear() {
        if (head.prev == head)
            return;
        do {
            Node node = head.prev;
            if (node == head)
                return;
            node.remove();
        } while (true);
    }



    public Node reverseGetFirst() {
        Node node = head.next;
        if (node == head) {
            current = null;
            return null;
        } else {
            current = node.next;
            return node;
        }
    }

    public Node reverseGetNext() {
        Node node = current;
        if (node == head) {
            current = null;
            return null;
        } else {
            current = node.next;
            return node;
        }
    }


    @Override
    public Iterator iterator() {
        return null;
    }
}
