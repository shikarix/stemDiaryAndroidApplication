package com.coistem.stemdiary;

import android.app.Activity;
import android.content.Context;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;

public class JSONFileEditing {
    String filename = "file.txt";
    String outputString = "{\n" +
            "   \"eremin15\":[{\n" +
            "     \"name\":\"Vadim\",\n" +
            "     \"surname\": \"Eremin\",\n" +
            "     \"thirdname\":\"Kto-to tamovich\",\n" +
            "     \"coins\": \"12223\",\n" +
            "\t \"accessType\":\"student\",\n" +
            "\t\"avatarUrl\":\"something\"}],\n" +
            "   \"yeliseyenko23\":[{\n" +
            "     \"name\":\"Yuriy\",\n" +
            "     \"surname\": \"Yeliseyenko\",\n" +
            "     \"thirdname\":\"Andreevich\",\n" +
            "     \"coins\": \"13242\",\n" +
            "\t \"accessType\":\"admin\",\n" +
            "\t\"avatarUrl\":\"https://sun9-55.userapi.com/c639831/v639831860/5afc/nPV_f_uJFV0.jpg\"}],\n" +
            "   \"vasilev75\":[{\n" +
            "     \"name\":\"Alexey\",\n" +
            "     \"surname\": \"Vasilev\",\n" +
            "     \"thirdname\":\"Il'ich\",\n" +
            "     \"coins\": \"1337\",\n" +
            "\t \"accesType\":\"teacher\",\n" +
            "\t\"avatarUrl\":\"https://sun9-48.userapi.com/c638322/v638322602/1d5dd/yd6qcSe5Fdw.jpg\"}],\n" +
            "   \"user\":[{\n" +
            "     \"name\":\"User\",\n" +
            "     \"surname\": \"Defoltnniy\",\n" +
            "     \"thirdname\":\"Alexeevich\",\n" +
            "     \"coins\": \"1337228322\",\n" +
            "\t \"accessType\":\"admin\",\n" +
            "\t\"avatarUrl\":\"https://ic.pics.livejournal.com/lev_dmitrich/32679866/221346/221346_original.jpg\"}]\n" +
            "}";
    String line;

    public String getJsonData(Context context) {
        try {
            FileOutputStream outputStream = context.openFileOutput(filename, Context.MODE_PRIVATE);
            outputStream.write(outputString.getBytes());
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            FileInputStream inputStream = context.openFileInput(filename);
            BufferedReader r = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder total = new StringBuilder();
            while ((line = r.readLine()) != null) {
                total.append(line);
            }
            r.close();
            inputStream.close();
            return total.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
