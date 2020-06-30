import util.Mathematical;

public class Test {
    public static void main(String[] args) {
        float val = 123556.54f;
        String val1 = "123556.54";


        int PDCP_UpOctDlData = (int)Float.parseFloat(val1);

        int min = 100, max = 110;
        double rd = (double) (min + (int) (Math.random() * ((max - min) + 1))) / 100;
        System.out.println(String.valueOf((int)(PDCP_UpOctDlData * 8 / (rd * 20))));

    }
}
