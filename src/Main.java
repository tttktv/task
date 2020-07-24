import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;


public class Main {


    public static void metod_bliz_ranga(ArrayList<Long> arrayList, double proc){
        Collections.sort(arrayList);
        ArrayList<Long> newArrayList = new ArrayList<>();
        for (Long element : arrayList) {
            if (!newArrayList.contains(element)) {
                newArrayList.add(element);
            }
        }
        double n=(proc/100.0)*newArrayList.size();
        String hms = String.format("%02d часов %02d минут", TimeUnit.MILLISECONDS.toHours(newArrayList.get((int)n-1)),
                TimeUnit.MILLISECONDS.toMinutes(newArrayList.get((int)n-1)) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(newArrayList.get((int)n-1))));
        System.out.println("90-й процентиль времени полёта между городами Владивосток и Тель-Авив: "+hms);

    }

    public static void main(String[] args) {
        ArrayList<Long> arrayList=new ArrayList<>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yy kk:mm");

        FileReader reader = null;
        try {
            reader = new FileReader("tickets.json");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        JSONParser parser = new JSONParser();
        JSONObject  jsonObject = null;
        try {
            jsonObject = (JSONObject) parser.parse(reader);
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }


        JSONArray a = (JSONArray) Objects.requireNonNull(jsonObject).get("tickets");

        long cnt=0;

        for (Object o : a)
        {
            JSONObject person = (JSONObject) o;

            String date1 = (String) person.get("departure_date");
            String time1=(String) person.get("departure_time");

            String date2 = (String) person.get("arrival_date");
            String time2=(String) person.get("arrival_time");
            try {
                Date data1 = dateFormat.parse(date1+" "+time1);
                Date data2= dateFormat.parse(date2+" "+time2);

                long milliseconds = data2.getTime() - data1.getTime();
                arrayList.add(milliseconds);
                cnt+=milliseconds;

            } catch (java.text.ParseException e) {
                e.printStackTrace();
            }

        }
        cnt/=a.size();

        String hms = String.format("%02d часов %02d минут", TimeUnit.MILLISECONDS.toHours(cnt), TimeUnit.MILLISECONDS.toMinutes(cnt) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(cnt)));
        System.out.println("Среднее время полёта между городами Владивосток и Тель-Авив: "+hms);

        metod_bliz_ranga(arrayList,90.0);

    }

}
