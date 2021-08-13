import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.regex.Pattern;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello world!");
//        MainWindow mainWindow = new MainWindow();

        try {
            //testPattern();
            printJson();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void testPattern() throws Exception {
        String value = "2020-12-28T12:38:2+01:00";
        System.out.println(value);
        value = URLEncoder.encode(value, "UTF-8");
        System.out.println(value);

        value = URLDecoder.decode(value, "UTF-8"); // urldecode
        System.out.println(value);

        Pattern pattern;
        pattern = Pattern.compile("^((19|20)[0-9][0-9])[-](0[1-9]|1[012])[-](0[1-9]|[12][0-9]|3[01])[T]([01][1-9]|[2][0-3])[:]([0-5][0-9])[:]([0-5][0-9])([+|-]([01][0-9]|[2][0-3])[:]([0-5][0-9])){0,1}$");
        boolean b = pattern.matcher(value).find();
        System.out.println(b);
    }

    public static void printJson() {
        String str = "{\n   \"Date_start\" : \"07/14/2021 13:00:01\",\n   \"Date_end\" : \"07/14/2021 13:00:01\",\n   \"Timezone\" : \"+10\",\n   \"Source\" : \"003\",\n   \"SourceEntityID\" : \"123e4567-e89b-12d3-a456-426655440000\",\n   \"SourceEntityLink\" : \"SourceEntityLink\",\n   \"Channel\" : \"003\",\n   \"InteractionForm\" : \"003\",\n   \"SberProfileId\" : \"b415ff413de060fab27cb544d2529f08874ece7b9865b3c23745a723c2d7d67bb868785f1a8f64b6\",\n   \"SubId\" : \"1120869641145968538\",\n   \"DynamicID\" : \"DynamicID\",\n   \"ClientContact\" : \"aaa@aa.ru\",\n   \"SberAON\" : \"aaa@aa.ru\",\n   \"Employee_Name\" : \"Иванов Иван Иванович\",\n   \"Division\" : \"Название подразделения\",\n   \"Head_Division\" : \"Название головного подразделения\",\n   \"Division_RegionID\" : \"1245987834\",\n   \"Division_AgencyID\" : \"1245777674\",\n   \"Division_BranchID\" : \"12456\",\n   \"Company\" : \"Название компании\",\n   \"Call_back_Employee\" : \"+7-999-123-45-67\",\n   \"Call_back_Division\" : \"AB_1200\",\n   \"Employee_Num\" : \"КЦ_1255.Operator-1\",\n   \"Employee_Login\" : \"Иванов И.И.\",\n   \"Type\" : \"Тематика\",\n   \"Status\" : \"003\",\n   \"Description\" : \"Тематика\",\n   \"ChainId\" : \"123e4567-e89b-12d3-a456-42665544\",\n   \"PrevId\" : \"123e4567-e89b-12d3-a456-42665544\",\n   \"NextId\" : \"123e4567-e89b-12d3-a456-42665544\"\n}";
        System.out.println(str);
    }
}
