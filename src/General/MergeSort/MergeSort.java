package General.MergeSort;

import General.Params.InitParams;
import java.io.*;
import java.util.*;

public abstract class MergeSort {


    /*
    ListReaders: ключ - название входного файла, значение - BufferedReader входного файла ключа;
    DIR - папка с тестовыми файлами.
    */


    protected static final String DIR = "resources/"; // "../resources/"


    private Map<String, BufferedReader> ListReaders;


    public MergeSort(){
        ListReaders = new HashMap<>();
    }


    protected Map<String, BufferedReader> getListReaders(){
        return ListReaders;
    }


    // заполнение массива infile входными файлами
    protected List<File> setInfiles(List<String> infiles){

        List<File> infile = new ArrayList<>();

        for(int i = 0; i < infiles.size(); i++){
            infile.add(new File(/*DIR + */infiles.get(i)));
        }
        return infile;
    }


    protected String setOutfile(String outfile){

        return outfile;
    }


    // заполнение ListReaders
    protected void FillingListReaders(List<File> infile){

        try {
            for (File file : infile) {
                ListReaders.put(file.toString(), new BufferedReader(new FileReader(DIR + file)));
            }
        }
        catch (Exception ex){ex.printStackTrace();}

    }


    //инициализация выходного файла
    protected BufferedWriter InitBufferWriter(String outFile){

        BufferedWriter bw = null;
        try {
            bw = new BufferedWriter(new FileWriter(DIR + outFile)); //change
        }
        catch (Exception ex){ex.printStackTrace();}

        return bw;
    }


    //запись MinMaxVal в файл
    protected void BufferWriter(BufferedWriter bw, String MinMaxVal){

        try {
            bw.write(MinMaxVal + "\n");
        }
        catch (Exception ex){ex.printStackTrace();}

    }


    protected void BufferWriter(BufferedWriter bw, int MinMaxVal){

        try {
            bw.write(MinMaxVal + "\n");
        }
        catch (Exception ex){ex.printStackTrace();}

    }


    //закрытие потоков входных файлов
    protected void closeBufferListReaders(){
        try {
            for (String key : ListReaders.keySet()){
                ListReaders.get(key).close();
            }
        }
        catch (Exception ex){ex.printStackTrace();}
    }


    //закрытие потока выходного файла
    protected void closeBufferWriter(BufferedWriter bw){
        try {
            bw.close();
        }
        catch (Exception ex){ex.printStackTrace();}
    }


    //вывод сообщения в консоль о том, что в файле NameFile строка ErrorString является неккоректной
    protected void PrintMessage(String NameFile, String ErrorString){
        System.out.println("Unsorted file: " + NameFile + "; str [" + ErrorString + "] was removed");
    }


    protected void PrintMessage(String NameFile, int ErrorString){
        System.out.println("Unsorted file: " + NameFile + "; str [" + ErrorString + "] was removed");
    }


    abstract public void AscendingSort(InitParams initParams);
    abstract public void DescendingSort(InitParams initParams);

}
