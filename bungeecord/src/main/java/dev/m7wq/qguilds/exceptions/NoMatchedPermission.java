package dev.m7wq.qguilds.exceptions;

public class NoMatchedPermission extends RuntimeException{

    public NoMatchedPermission(){
        super("No permission matched with your name Permission#getByName");
    }
}
