/*
 * *********************************************************
 *  Copyright (c) 2017 Brava Home Inc.  All rights reserved.
 * *********************************************************
 */
package bravahome.common.util.aux;

public enum Mode
{
    CREATE (0),
    READ   (1),
    UPDATE (2),
    DELETE (3),
    APPEND (4);

    private final int operation;

    Mode(int operation) {
        this.operation = operation;
    }

    public int getMode() {
        return operation;
    }

    public static void main(String[] args) {
        Mode op = Mode.CREATE;
        System.out.println("==> " + op);
        System.out.println("==> " + op.toString());
        System.out.println("==> " + op.getMode());
    }

}