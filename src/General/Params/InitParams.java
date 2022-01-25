package General.Params;

import java.util.ArrayList;
import java.util.List;

public class InitParams {


    /*
    Если ascending = true - сортировка будет проводиться по возрастанию, false - по убыванию
    Если intData = true - тип данных int, false - String
    Если isAscDesc = 1 - в args[] явно указывается тип сортировки, isAscDesc = 0 - неявное задание сортировки по возр.
    */


    private String[] args;
    private boolean ascending;
    private boolean intData;
    private int isAscDesc;


    public InitParams(String[] args){
        this.args = args;
        AnalysisParams();
    }


    private void AnalysisParams(){

        ascending = true;
        intData = true;
        isAscDesc = 0;

        if (args[0].equals("-s") || args[1].equals("-s")) {
            intData = false;
        }

        if (args[0].equals("-d") || args[1].equals("-d")) {
            ascending = false;
        }

        if(args[0].equals("-d") || args[0].equals("-a") || args[1].equals("-d") || args[1].equals("-a"))
            isAscDesc = 1;

    }


    public List<String> getInfiles(){

        List<String> inFiles = new ArrayList<>();
        for (int i = 0; i < args.length - isAscDesc - 2; i++)
            inFiles.add(args[isAscDesc + i + 2]);

        return inFiles;
    }


    public String getOutfile(){
        return args[isAscDesc + 1];
    }


    public boolean getAscending() {
        return ascending;
    }


    public boolean getIntData() {
        return intData;
    }

}
