package General.MergeSort;

import General.Params.InitParams;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class IntMergeSort extends MergeSort{


    /*
    CurrentValues: ключ - название входного файла, значение - строка из этого файла
    MinMax - значение минимума в случае сортировки по возр., значение макс. - по убыванию
    NameFileWithMinMax - название файла (в этом файле найден MinMax)
    prevMinMax - то же что и MinMax, но получен раньше
    prevNameFileWithMinMax - название файла (в этом файле найден prevMinMax)
     */


    private Map<String, Integer> CurrentValues;
    private int MinMax;
    private String NameFileWithMinMax;
    private int prevMinMax;
    private String prevNameFileWithMinMax;


    public IntMergeSort(){
        super();
        CurrentValues = new HashMap<>();
        MinMax = 0;
        NameFileWithMinMax = null;
        prevMinMax = 0;
        prevNameFileWithMinMax = null;
    }


    //заполнение CurrentValues значениями, исключая строки, которые не удовлетворяют Integer.parseInt(str)
    private void FillingCurrentValues(){

        Set<Map.Entry<String, BufferedReader>> setReaders = getListReaders().entrySet();

        try {
            for (Map.Entry<String, BufferedReader> it : setReaders) {

                String str = it.getValue().readLine().trim();
                boolean flag;

                while (str != null) {

                    flag = false;
                    int tempInt = 0;

                    try {
                        tempInt = Integer.parseInt(str);
                    } catch (Exception ex) {
                        flag = true;
                    }

                    if (flag) {
                        PrintMessage(it.getKey(), str);
                        str = it.getValue().readLine();
                    } else {
                        CurrentValues.put(it.getKey(), tempInt);
                        break;
                    }

                }
            }
        }catch (Exception ex){/*ex.printStackTrace();*/}

    }


    //если всего имеем 1 входной файл (CurrentValues.size = 1),
    //то пытаемся передать оттуда данные в выходной файл, учитывая условия:
    //эти данные являются числовыми и они идут строго по по возр.
    //Если одно из условий не выполняется, то эти данные не выводятся и происходит вывод сообщения о том,
    //в каком файле найдены неккоректные данные и что не вывелось
    private boolean ifCurrValuesNoMoreThanOneAsc(BufferedWriter bw){

        boolean StringsOfFilesAreNull = true;

        if (CurrentValues.size() == 1){

            for( String key : CurrentValues.keySet()){

                BufferWriter(bw, CurrentValues.get(key));

                prevNameFileWithMinMax = key;
                prevMinMax = CurrentValues.get(key);

                String temp = null;
                try {
                    while ((temp = getListReaders().get(key).readLine().trim()) != null) {

                        int tempMin = 0;
                        boolean isNotCorrect = false;
                        try {
                            tempMin = Integer.parseInt(temp);
                        } catch (Exception ex) {
                            isNotCorrect = true;
                        }

                        if (prevMinMax > tempMin || isNotCorrect) {
                            PrintMessage(prevNameFileWithMinMax, temp);
                        } else {
                            prevMinMax = tempMin;
                            BufferWriter(bw, tempMin);
                        }
                    }
                }catch (Exception ex){/*ex.printStackTrace();*/}

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

        Set<Map.Entry<String, Integer>> setValues = CurrentValues.entrySet();

        for(Map.Entry<String, Integer> it : setValues){

            if(flag) {
                if(MinMax > it.getValue()) {
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
            prevNameFileWithMinMax = NameFileWithMinMax;
            prevMinMax = MinMax;

            BufferWriter(bw, prevMinMax);
        }
        else {
            if(prevMinMax > MinMax){
                PrintMessage(prevNameFileWithMinMax, MinMax);
            }
            else{
                prevNameFileWithMinMax = NameFileWithMinMax;
                prevMinMax = MinMax;
                BufferWriter(bw, prevMinMax);
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

        }catch (Exception ex){/*ex.printStackTrace();*/}

        boolean flag;
        while(temp != null) {

            flag = false;
            int tempInt = 0;

            try {
                tempInt = Integer.parseInt(temp);
            } catch (Exception ex) {
                flag = true;
            }

            if (flag) {
                PrintMessage(NameFileWithMinMax, temp);

                temp = null;
                try {
                    temp = getListReaders().get(NameFileWithMinMax).readLine().trim();

                }catch (Exception ex){/*ex.printStackTrace();*/}

            } else {
                CurrentValues.put(NameFileWithMinMax, tempInt);
                break;
            }
        }
        if (temp == null) {
            CurrentValues.remove(NameFileWithMinMax, MinMax);
        }

    }


    // сортировка по возрастанию
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


    //то же, что ifCurrValuesNoMoreThanOneAsc(), но - по убыванию
    private boolean ifCurrValuesNoMoreThanOneDesc(BufferedWriter bw){


        boolean StringsOfFilesAreNull = true;

        if (CurrentValues.size() == 1){

            for( String key : CurrentValues.keySet()){

                BufferWriter(bw, CurrentValues.get(key));

                prevNameFileWithMinMax = key;
                prevMinMax = CurrentValues.get(key);

                String temp = null;
                try {
                    while ((temp = getListReaders().get(key).readLine().trim()) != null) {

                        int tempMin = 0;
                        boolean isNotCorrect = false;
                        try {
                            tempMin = Integer.parseInt(temp);
                        } catch (Exception ex) {
                            isNotCorrect = true;
                        }


                        if (prevMinMax < tempMin || isNotCorrect) {
                            PrintMessage(prevNameFileWithMinMax, temp);
                        } else {
                            prevMinMax = tempMin;
                            BufferWriter(bw, tempMin);
                        }
                    }
                }catch (Exception ex){/*ex.printStackTrace();*/}

            }
            StringsOfFilesAreNull = false;
        }
        else if (CurrentValues.size() < 1) {
            StringsOfFilesAreNull = false;
        }

        return StringsOfFilesAreNull;
    }


    //то же, что и findMin(), но для случая сортировки по возр.
    private void findMax(BufferedWriter bw){

        boolean flag = false;

        Set<Map.Entry<String, Integer>> setValues = CurrentValues.entrySet();

        for(Map.Entry<String, Integer> it : setValues){

            if(flag) {
                if(MinMax < it.getValue()) {
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
            prevNameFileWithMinMax = NameFileWithMinMax;
            prevMinMax = MinMax;
            BufferWriter(bw, prevMinMax);
        }
        else {
            if(prevMinMax < MinMax){
                PrintMessage(prevNameFileWithMinMax, MinMax);
            }
            else{
                prevNameFileWithMinMax = NameFileWithMinMax;
                prevMinMax = MinMax;
                BufferWriter(bw, prevMinMax);
            }
        }

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
