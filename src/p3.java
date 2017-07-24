import java.io.BufferedReader;
import java.io.FileReader;

/**
 * Created by matt on 4/9/15.
 */
public class p3 {
    public static void main(String args[]) {
        boolean debug = false;
        long start = System.nanoTime();


        if (args.length != 1) {
            System.out.println("Error, please enter a single argument");
            return;
        }

        BufferedReader bufferedReader = null;
        try {
            bufferedReader = new BufferedReader(new FileReader(args[0]));
        } catch (Exception e) {
            System.out.println("File doesn't exist");
        }

        // P1 - Lexical Analyzer
        Reader rd = new Reader(bufferedReader);
        rd.scanContents();
        //rd.printLines();

        // P2 - Parser / Syntax
        Parser p = new Parser(rd.tokens);
        p.parse();

        // P3 - Semantics
        Semantics semantics = new Semantics(rd.tokens);
        Scope scope = semantics.convertTokens();
        //System.out.println("------------");
        System.out.println("ACCEPT");


        long end = System.nanoTime();
        long total = (end - start)/((long)Math.pow(10,6));

        if(debug) {
            System.out.println("###############");
            System.out.println("#  " + total + " milliseconds");
            System.out.println("###############");
        }
    }

}
