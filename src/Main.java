import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Main {
    public static Queue<File> getValidFiles(String path){
        Queue<File> files = new LinkedList<>();
        Queue<File> directories = new LinkedList<>();
        directories.add(new File(path));
        while(!directories.isEmpty()){
            File[] filesInDirectory = directories.remove().listFiles();
            if(filesInDirectory != null){
                for(File file : filesInDirectory){
                    if (file.isDirectory()) {
                        directories.add(file);
                    }else if(file.isFile()){
                        files.add(file);
                    }
                }
            }
        }
        return files;
    }

    public static void writeInTxtFile(HashMap<String, HashMap<String, Integer>> hashMap, String name){
        try {
            FileWriter myWriter = new FileWriter(name + ".txt");

            for(String key: hashMap.keySet()){
                HashMap<String, Integer> innerMap = hashMap.get(key);
                myWriter.write(key);
                for(String word: innerMap.keySet()) {
                    myWriter.write(" <" + word + ", " + innerMap.get(word).toString() + ">");
                }
                myWriter.write(System.getProperty( "line.separator" ));
            }
            myWriter.close();
            System.out.println("Successfully wrote to the file.");
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }
    public static void main(String[] args) {
        List<String> stopWords;
        try (Stream<String> lines = Files.lines(Paths.get("C:/Users/hospo/OneDrive/Documente/facultate/anul4/sem2/RIW/Proiect1/stopwords.txt"))){
            stopWords = lines.collect(Collectors.toList());
        }catch(Exception ex){
            ex.printStackTrace();
            return;
        }

        List<String> exceptions;
        try (Stream<String> lines = Files.lines(Paths.get("C:/Users/hospo/OneDrive/Documente/facultate/anul4/sem2/RIW/Proiect1/exceptions.txt"))){
            exceptions = lines.collect(Collectors.toList());
        }catch(Exception ex){
            ex.printStackTrace();
            return;
        }

        String directoryPath = "C:/Users/hospo/OneDrive/Documente/facultate/anul4/sem2/RIW/Proiect1/test-Files";
        Queue<File> files = getValidFiles(directoryPath);

        Index index = new Index(files, exceptions, stopWords);

        writeInTxtFile(index.indexDirect, "IndexDirect");
        writeInTxtFile(index.indexInvers, "IndexInvers");

        String query = "pretens + ";

        Set<String> searchResults = BooleanSearch.booleanSearch(index.indexInvers, query);
        if (searchResults != null)
        {
            System.out.println("\nResult:");
            for (String doc : searchResults) {
                System.out.println("\t" + doc);
            }
        }
        else
        {
            System.out.println("No result!");
        }
    }
}
