import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FileContent {

    String filename = new String();
    List<List<String>> file_content = new ArrayList<>();

    public FileContent(String filename){
        this.filename = filename;
        setFile_content(file_content);
        
    }
    
    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
        setFile_content(this.file_content);
    }

    public List<List<String>> getFile_content() {
        return file_content;
    }

    private void setFile_content(List<List<String>> file_content) {
        try (BufferedReader br = new BufferedReader(new FileReader(this.filename))) {
            String line;

            // Read each line from the file
            while ((line = br.readLine()) != null) {
                // Split the line by comma and convert to a List
                String[] values = line.split(",");
                List<String> lineData = Arrays.asList(values);

                // Add the line data to our main list
                file_content.add(lineData);
            }

        } catch (IOException e) {
            System.err.println("Error reading the CSV file: " + e.getMessage());
            e.printStackTrace();
        }
    }


}
