package General.MergeSort;

import General.Params.InitParams;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class StringMergeSort extends MergeSort{


    /*
    CurrentValues: ключ - название входного файла, значение - строка из этого файла
    MinMax - значение минимума в случае сортировки по возр., значение макс. - по убыванию
    NameFileWithMinMax - название файла (в этом файле найден MinMax)
    prevMinMax - то же что и MinMax, но получен раньше
    prevNameFileWithMinMax - название файла (в этом файле найден prevMinMax)
    */


    private Map<String, String> CurrentValues;
    private String MinMax;
    private String NameFileWithMinMax;
    private String prevMinMax;
    private String prevNameFileWithMinMax;


    public StringMergeSort(){
        super();
        CurrentValues = new HashMap<>();
        MinMax = null;
        NameFileWithMinMax = null;
        prevMinMax = null;
        prevNameFileWithMinMax = null;
    }


    //заполнение CurrentValues значениями, исключая строки,
    //которые имеют пробелы (но не в начале и не в конце) и которые являются пустыми ("")
    //о неккоректных строках выводится сообщение
    private void FillingCurrentValues(){

        Set<Map.Entry<String, BufferedReader>> setReaders = getListReaders().entrySet(); // listreaders

        String str = null;
            for(Map.Entry<String, BufferedReader> it : setReaders){

                try {
                    str = it.getValue().readLine().trim();
                }catch (Exception ex){}

                while(str != null) {

                    if(str.indexOf(' ') >= 0 || str.equals("")){

                        PrintMessage(NameFileWithMinMax, str);
                        try {
                            str = it.getValue().readLine().trim();
                        }catch (Exception ex){
                            str = null;}

                    }
                    else{
                        CurrentValues.put(it.getKey(), str);
                        break;
                    }

                }

            }


    }


    //CurrentValues:
    //Замена новой строкой того значения, которое уже было использовано, либо если вх. файл пуст,
    //то удаление ключ-значения из CurrentValues.
    //неккоректные данные не допускаются - выводится сообщение
    private void ChangeCurrentValues(){

        String temp = null;
        try {
            temp = getListReaders().get(NameFileWithMinMax).readLine().trim();
        }catch (Exception ex){}

            while(temp != null) {

                if(temp.indexOf(' ') >= 0 || temp.equals("")){

                    PrintMessage(NameFileWithMinMax, temp);

                    try {
                        temp = getListReaders().get(NameFileWithMinMax).readLine().trim();
                    }
                    catch (Exception ex) {temp = null;}
                }
                else{
                    CurrentValues.put(NameFileWithMinMax, temp);
                    break;
                }
            }


        if (temp == null) {
            CurrentValues.remove(NameFileWithMinMax, MinMax);
        }
    }


    //если всего имеем 1 входной файл (CurrentValues.size = 1),
    //то пытаемся передать оттуда данные в выходной файл, учитывая условия:
    //строка не пустая, без пробелов и эти строки должны идти по возр.
    //Если одно из условий не выполняется, то эти данные не выводятся и происходит вывод сообщения о том,
    //в каком файле найдены неккоректные данные и что не вывелось
    private boolean ifCurrValuesNoMoreThanOneAsc(BufferedWriter bw){

        boolean StringsOfFilesAreNull = true;

        if (CurrentValues.size() == 1){

            for( String key : CurrentValues.keySet()){

                String tempStr = CurrentValues.get(key).trim();

                if(tempStr.indexOf(' ') == -1 && !tempStr.equals("")){
                    BufferWriter(bw, tempStr);
                }
                else{
                    PrintMessage(key, tempStr);
                }

                prevNameFileWithMinMax = key;
                prevMinMax = CurrentValues.get(key);

                String tempMin = null;
                try {
                    tempMin = getListReaders().get(key).readLine();
                }catch (Exception ex){}

                    while (tempMin  != null) {

                        tempMin = tempMin.trim();

                        if (prevMinMax.compareTo(tempMin) > 0 || tempMin.indexOf(' ') >= 0  || tempMin.equals("")) {
                            PrintMessage(prevNameFileWithMinMax, tempMin);
                        } else {
                            prevMinMax = tempMin;
                            BufferWriter(bw, tempMin);
                        }

                        try {
                            tempMin = getListReaders().get(key).readLine();
                        }catch (Exception ex){
                            tempMin = null;}

                    }

            }
            StringsOfFilesAreNull = false;
        }
        else if (CurrentValues.size() < 1) {
            StringsOfFilesAreNull = false;
        }

        return StringsOfFilesAreNull;
    }


    //то же самое, что и ifCurrValuesNoMoreThanOneAsc(), но по убыванию
    private boolean ifCurrValuesNoMoreThanOneDesc(BufferedWriter bw){

        boolean StringsOfFilesAreNull = true;

        if (CurrentValues.size() == 1){

            for( String key : CurrentValues.keySet()){

                String tempStr = CurrentValues.get(key).trim();

                if(tempStr.indexOf(' ') == -1 && !tempStr.equals("")){
                    BufferWriter(bw, tempStr);
                }
                else{
                    PrintMessage(key, tempStr);
                }

                prevNameFileWithMinMax = key;
                prevMinMax = CurrentValues.get(key);

                String tempMin = null;
                try {
                    tempMin = getListReaders().get(key).readLine();
                }catch (Exception ex){}

                    while (tempMin != null) {

                        tempMin = tempMin.trim();

                        if (prevMinMax.compareTo(tempMin) < 0 || tempMin.indexOf(' ') >= 0 || tempMin.equals("")) {
                            PrintMessage(prevNameFileWithMinMax, tempMin);
                        } else {
                            prevMinMax = tempMin;
                            BufferWriter(bw, tempMin);
                        }

                        try {
                            tempMin = getListReaders().get(key).readLine();
                        }catch (Exception ex){
                            tempMin = null;}

                    }

            }
            StringsOfFilesAreNull = false;
        }
        else if (CurrentValues.size() < 1) {
            StringsOfFilesAreNull = false;
        }

        return StringsOfFilesAreNull;
    }


    //Нахождение минимума (MinMax) среди значений CurrentValues.
    //И нахождение prevMinMax, если это возможно. Сравниваем prevMinMax с MinMax.
    //Находим по итогу общий минимум, если это возможно, и выводим его в вых. файл.
    //Если MinMax неккоректный, то выводим сообщение об ошибке.
    //findMin - для случая сортировки по возр.
    private void findMin(BufferedWriter bw){

        boolean flag = false;

        Set<Map.Entry<String, String>> setValues = CurrentValues.entrySet();

        for(Map.Entry<String, String> it : setValues){

            if(flag) {

                if(MinMax.compareTo(it.getValue()) >= 0) {
                    NameFileWithMinMax = it.getKey();
                    MinMax = it.getValue();
                }

            }
            else{
                NameFileWithMinMax = it.getKey();
                MinMax = it.getValue();
                flag = true;
            }

        }

        if(prevNameFileWithMinMax == null){

            if(MinMax.indexOf(' ') >= 0 || MinMax.equals("")){
                PrintMessage(NameFileWithMinMax, MinMax);
            }
            else {
                prevNameFileWithMinMax = NameFileWithMinMax;
                prevMinMax = MinMax;
                BufferWriter(bw, prevMinMax);
            }

        }
        else {
            if(prevMinMax.compareTo(MinMax) > 0 || MinMax.indexOf(' ') >= 0){
                PrintMessage(prevNameFileWithMinMax, MinMax);
            }
            else{
                prevNameFileWithMinMax = NameFileWithMinMax;
                prevMinMax = MinMax;
                BufferWriter(bw, prevMinMax);
            }
        }

    }


    //findMax - для случая сортировки по убыванию
    private void findMax(BufferedWriter bw){

        boolean flag = false;

        Set<Map.Entry<String, String>> setValues = CurrentValues.entrySet();

        for(Map.Entry<String, String> it : setValues){

            if(flag) {

                if(MinMax.compareTo(it.getValue()) <= 0) {
                    NameFileWithMinMax = it.getKey();
                    MinMax = it.getValue();
                }

            }
            else{
                NameFileWithMinMax = it.getKey();
                MinMax = it.getValue();
                flag = true;
            }

        }


        if(prevNameFileWithMinMax == null){

            if(MinMax.indexOf(' ') >= 0 || MinMax.equals("")){
                PrintMessage(NameFileWithMinMax, MinMax);
            }
            else {
                prevNameFileWithMinMax = NameFileWithMinMax;
                prevMinMax = MinMax;
                BufferWriter(bw, prevMinMax);
            }

        }
        else {
            if(prevMinMax.compareTo(MinMax) < 0 || MinMax.indexOf(' ') >= 0){
                PrintMessage(prevNameFileWithMinMax, MinMax);
            }
            else{
                prevNameFileWithMinMax = NameFileWithMinMax;
                prevMinMax = MinMax;
                BufferWriter(bw, prevMinMax);
            }
        }

    }


    //сортировка по возр.
    public void AscendingSort(InitParams initParams){

        List<File> infile = setInfiles(initParams.getInfiles());
        String outFile = setOutfile(initParams.getOutfile());

        BufferedWriter bw = InitBufferWriter(outFile);

        FillingListReaders(infile);

        FillingCurrentValues();

        boolean StringsOfFilesAreNull;

        StringsOfFilesAreNull = ifCurrValuesNoMoreThanOneAsc(bw);

        while (StringsOfFilesAreNull){

            findMin(bw);

            ChangeCurrentValues();

            StringsOfFilesAreNull = ifCurrValuesNoMoreThanOneAsc(bw);

        }

        closeBufferWriter(bw);
        closeBufferListReaders();


    }


    //сортировка по убыванию
    public void DescendingSort(InitParams initParams){

        List<File> infile = setInfiles(initParams.getInfiles());
        String outFile = setOutfile(initParams.getOutfile());

        BufferedWriter bw = InitBufferWriter(outFile);

        FillingListReaders(infile);

        FillingCurrentValues();

        boolean StringsOfFilesAreNull;

        StringsOfFilesAreNull = ifCurrValuesNoMoreThanOneDesc(bw);

        while (StringsOfFilesAreNull){

            findMax(bw);

            ChangeCurrentValues();

            StringsOfFilesAreNull = ifCurrValuesNoMoreThanOneDesc(bw);

        }

        closeBufferWriter(bw);
        closeBufferListReaders();


    }




}
