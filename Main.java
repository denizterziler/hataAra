
import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
public class Main {

    public static void main(String[] args) throws FileNotFoundException {
        String originalData;
        int shiftValue;
        char[] dataShift;
        File file_1 = new File("C:\\Users\\ML105\\Desktop\\se375_Det_LAB6\\sample1.txt");
        Scanner first_scanner = new Scanner(file_1);
        File file_2 = new File("C:\\Users\\ML105\\Desktop\\se375_Det_LAB6\\sample2.txt");
        Scanner sec_scanner = new Scanner(file_2);
        File file_3 = new File("C:\\Users\\ML105\\Desktop\\se375_Det_LAB6\\sample3.txt");
        Scanner third_scanner = new Scanner(file_3);
        File file_4 = new File("C:\\Users\\ML105\\Desktop\\se375_Det_LAB6\\sample4.txt");
        Scanner fourth_scanner = new Scanner(file_4);
        ArrayList<String> files = new ArrayList<>();
        files.add(first_scanner.nextLine());
        files.add(sec_scanner.nextLine());
        files.add(third_scanner.nextLine());
        files.add(fourth_scanner.nextLine());
        ArrayList<Map<Integer, List<String>>> hashMapList = new ArrayList<>();
        ExecutorService executor = Executors.newFixedThreadPool(files.size());
        String[] first = new String[]{"u", "u", "l", "l"};
        String[] color = new String[]{"r","y","r","y"};
        shiftValue = 1;
        for (int i = 0;i< files.size();i++) {
            Map<Integer, List<String>> finalHashMap = Collections.synchronizedMap(new HashMap<>());
            originalData = files.get(i);

            dataShift = new char[files.get(i).length()];

            Thread oneThread = new oneThread(finalHashMap, first[i], originalData, dataShift, shiftValue, color[i]);
            executor.execute(oneThread);
            hashMapList.add(finalHashMap);
        }
        executor.shutdown();
        while (!executor.isTerminated()) {}
        System.out.println("Finished all threads");
        for (Map<Integer, List<String>> integerListMap : hashMapList) {
            StringBuilder firstAnswer = new StringBuilder();
            StringBuilder secAnswer = new StringBuilder();
            StringBuilder thirdAnswer = new StringBuilder();
            StringBuilder fourthAnswer = new StringBuilder();
            ArrayList<String> fifthAnswer = new ArrayList<>();
            for (int j = 0; j < integerListMap.size(); j++) {
                firstAnswer.append(integerListMap.get(j).get(0));
                secAnswer.append(integerListMap.get(j).get(1));
                thirdAnswer.append(integerListMap.get(j).get(2));
                fourthAnswer.append(integerListMap.get(j).get(3));
                fifthAnswer.add(integerListMap.get(j).get(4));
            }
            System.out.println("-*-*-*-*-*-*-*-*-*-*-*-*");
            /*System.out.println("Original\n" + firstAnswer);
            System.out.println("-*-*-*-*-*-*-*-*-*-*-*-*");
            System.out.println("After Case Change\n" + secAnswer);
            System.out.println("-*-*-*-*-*-*-*-*-*-*-*-*");
            System.out.println("After Shift\n" + thirdAnswer);
            System.out.println("-*-*-*-*-*-*-*-*-*-*-*-*");
            System.out.println("After Color Change\n" + fourthAnswer);
            System.out.println("-*-*-*-*-*-*-*-*-*-*-*-*");*/
            System.out.println("Number of Transformations\n" + fifthAnswer);
        }
    }
}
class oneThread extends Thread {
    //Common
    final Map<Integer, List<String>> finalHashMap;
    //1st thread
    String first_input;
    String originalData;
    //2nd thread
    char[] dataShift;
    int shiftValue;
    //3rd thread
    String color_input;
    public oneThread(Map<Integer, List<String>> finalHashMap, String first_input, String originalData, char[] dataShift, int shiftValue,
                     String color_input) {
        this.finalHashMap = finalHashMap;
        this.first_input = first_input;
        this.originalData = originalData;
        this.dataShift = dataShift;
        this.shiftValue = shiftValue;
        this.color_input = color_input;
        ArrayList<String> arrayListForEach;
        for (int i = 0; i < originalData.length(); i++) {
            arrayListForEach = new ArrayList<>();
            arrayListForEach.add(String.valueOf(originalData.charAt(i)));
            arrayListForEach.add("");
            arrayListForEach.add("");
            arrayListForEach.add("");
            arrayListForEach.add("0");
            finalHashMap.put(i, arrayListForEach);
        }
    }
    @Override
    public void run() {
        Thread caseThread = new caseThread(finalHashMap,originalData,first_input);
        Thread shiftThread = new shiftThread(finalHashMap,dataShift,originalData,shiftValue);
        Thread colorThread = new colorThread(finalHashMap,originalData,color_input);
        caseThread.start();
        shiftThread.start();
        colorThread.start();
        try {
            caseThread.join();
            shiftThread.join();
            colorThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
class caseThread extends Thread {
    String first_input;
    String data;
    String dataCaseChange;
    final Map<Integer, List<String>> finalHashMap;
    public caseThread(Map<Integer, List<String>> finalHashMap, String originalData, String first_input) {
        this.finalHashMap = finalHashMap;
        this.data = originalData;
        this.first_input = first_input;
    }
    @Override
    public void run() {
            if (first_input.equals("U") || first_input.equals("u")) {
                dataCaseChange = data.toUpperCase();
            } else if (first_input.equals("L") || first_input.equals("l")) {
                dataCaseChange = data.toLowerCase();
            }
            for (int i = 0; i < data.length(); i++) {
                finalHashMap.get(i).set(1, String.valueOf(dataCaseChange.charAt(i)));
            }
            for (int i = 0; i < data.length(); i++) {
                int count = Integer.parseInt(finalHashMap.get(i).get(4));
                count += 1;
                finalHashMap.get(i).set(4,String.valueOf(count));
            }

    }
}
class shiftThread extends Thread {
    String dataShiftString = "";
    char[] dataShift;
    String data;
    int shiftValue;
    final Map<Integer, List<String>> finalHashMap;
    public shiftThread(Map<Integer, List<String>> finalHashMap, char[] dataShift, String data, int shiftValue) {
        this.finalHashMap = finalHashMap;
        this.data = data;
        this.dataShift = dataShift;
        this.shiftValue = shiftValue;
    }
    @Override
    public void run() {
            for (int i = 0; i < dataShift.length; i++) {
                dataShift[i] = (char) (data.charAt(i) + shiftValue);
            }
            for (char c : dataShift) {
                dataShiftString += c;
            }
            for (int i = 0; i < data.length(); i++) {
                finalHashMap.get(i).set(2, String.valueOf(dataShiftString.charAt(i)));
            }
            for (int i = 0; i < data.length(); i++) {
                int count =Integer.parseInt(finalHashMap.get(i).get(4));
                count += 1;
                finalHashMap.get(i).set(4,String.valueOf(count));
            }
        }

}
class colorThread extends Thread {
    public final String ANSI_RESET = "\u001B[0m";
    public final String ANSI_RED = "\u001B[31m";
    public final String ANSI_YELLOW = "\u001B[33m";
    String color_input;
    String dataAfterColor;
    String originalData;
    final Map<Integer, List<String>> finalHashMap;
    ArrayList<String> dataStringCode = new ArrayList<>();

    public colorThread(Map<Integer, List<String>> finalHashMap, String originalData, String color_input) {
        this.finalHashMap = finalHashMap;
        this.color_input = color_input;
        this.originalData = originalData;
    }

    @Override
    public void run() {
        if (color_input.equals("R") || color_input.equals("r")) {
                dataAfterColor = ANSI_RED + originalData + ANSI_RESET;
                for (int i = 0; i < originalData.length(); i++) {
                    dataStringCode.add(ANSI_RED + originalData.charAt(i) + ANSI_RESET);
                }
            } else if (color_input.equals("Y") || color_input.equals("y")) {
                dataAfterColor = ANSI_YELLOW + originalData + ANSI_RESET;
                for (int i = 0; i < originalData.length(); i++) {
                    dataStringCode.add(ANSI_YELLOW + originalData.charAt(i) + ANSI_RESET);
                }
            }
            for (int i = 0; i < originalData.length(); i++) {
                finalHashMap.get(i).set(3, String.valueOf(dataStringCode.get(i)));
            }
            for (int i = 0; i < originalData.length(); i++) {
                int count = Integer.parseInt(finalHashMap.get(i).get(4));
                count += 1;
                finalHashMap.get(i).set(4, String.valueOf(count));
            }
        }

}
