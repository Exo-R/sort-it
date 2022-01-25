package General;

import General.MergeSort.IntMergeSort;
import General.MergeSort.MergeSort;
import General.MergeSort.StringMergeSort;
import General.Params.InitParams;

public class MainLauncher {

    public static void main(String[] args){


        InitParams initParams = new InitParams(args);

        MergeSort mergeSort;

        if ( initParams.getIntData() ){

            mergeSort = new IntMergeSort();

        }
        else{
            mergeSort = new StringMergeSort();
        }

        if ( initParams.getAscending() ){

            mergeSort.AscendingSort(initParams);

        }
        else{

            mergeSort.DescendingSort(initParams);

        }

    }
}
