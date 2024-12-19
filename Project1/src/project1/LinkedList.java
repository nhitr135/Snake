/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package project1;

/**
 *
 * @author sophi
 */
 class Node {
    public Tile data;
    public Node next;
    
    public Node(Tile data) {
        this.data = data;
        this.next = null; }   
}
public class LinkedList {
    public Node tail; 
    public Node head;
    
    public LinkedList() {
        this.tail = null;
        this.head = null;
    }
    
    public void add(Tile data) {
    Node newNode = new Node(data); 
    if (tail == null) {
        head = newNode;
        tail = newNode;
    } else {
        tail.next = newNode; 
        tail = newNode;      
    }
}
    
    public int size() {
        int size = 0;
        Node current = head;
        while (current != null) {
            size++;
            current = current.next;
        }
        return size;
    }
    
    public Tile get(int index) {
        Node current = head;
        int i = 0;
        while (current != null) {
            if (i == index) {
                return current.data;}
            current = current.next;
            i++;
        }
        return null;  
    }
    
    public void set(int index, Tile data) {
        Node current = head;
        int i = 0;
        while (current != null) {
            if (i == index) {
                current.data = data;
                return;
            }
            current = current.next;
            i++;
        }
    }    
}
