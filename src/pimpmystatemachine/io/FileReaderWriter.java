/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pimpmystatemachine.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.stream.Collectors;

/**
 * Utility class, for reading from or writing to a file
 *
 * @author Justin ROSSMANN
 */
public class FileReaderWriter {

    //TODO create an object that contains the constants associated with each filetype
    /**
     * Write a string to a file
     *
     * @param file file to write to
     * @param str string to write
     * @throws java.io.IOException exception
     */
    public static void write(File file, String str) throws IOException {
        FileWriter fileW = new FileWriter(file);
        fileW.write(str);
        fileW.flush();
        fileW.close();
    }

    /**
     * Read a file into a string
     *
     * @param file file to read from
     * @return a string containing all the characters in the file
     * @throws java.io.FileNotFoundException exception
     */
    public static String read(File file) throws FileNotFoundException {
        BufferedReader br = new BufferedReader(new FileReader(file));
        return br.lines().collect(Collectors.joining(System.lineSeparator()));
    }
}
