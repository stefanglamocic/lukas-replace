import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {
        if(args.length < 2){
            System.out.println("Greska, niste unijeli argumente!");
            return;
        }

        Path path = Paths.get(System.getProperty("user.dir"));
        String regex = args[0], replacement = args[1];
        Pattern pattern = Pattern.compile(".*\\.(jar|class)");

        if(args.length > 2){
            path = Paths.get(args[2]);
            if(!Files.exists(path)){
                System.out.println("Greska! Putanja ne postoji.");
                return;
            }
        }

        File[] files = path.toFile().listFiles();
        for(File f : files){
            Matcher matcher = pattern.matcher(f.getName());
            if(!f.isFile() || matcher.matches())
                continue;
            String text = "";
            try(BufferedReader reader = Files.newBufferedReader(f.toPath())){
                text = reader.lines().collect(Collectors.joining(System.lineSeparator()));
            }catch (Exception e){
                System.out.println("Greska tokom citanja fajla!");
            }

            text = text.replaceAll(regex, replacement);

            try(FileWriter writer = new FileWriter(f)){
                writer.write(text);
            }catch (Exception e){
                System.out.println("Greska pri upisu u fajl. Provjerite prava pisanja u fajl.");
            }
        }

        System.out.println("Zamjena uspjesno izvrsena!");
    }
}
