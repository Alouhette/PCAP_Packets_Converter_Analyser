import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class Main {

    public static void infoToCsv(String[] headers, String[][] Data, String filename){
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            writer.write(String.join(",", headers));
            writer.newLine();

            for (String[] row : Data) {
                writer.write(String.join(",", row));
                writer.newLine();
            }

            System.out.println("\nFichier CSV créé avec succès ! \n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {

        //File name examples :
        //Please consider that these 2 files already have a CSV file created from them
        String filename = "test.pcap";
        //String filename = "media_1000_pcap_anonymize.pcap";

        /*
        //Uncomment this block only if you want to create/recreate a CSV file from a pcap file
        //Warning : Sometime the API used to read the pcap file might not work, in that case try again

        System.out.println("Currently Trying to read file : "+filename+"\n");
        
        String[][] Csv_Data = ReadCountPcap.PacketReader(filename);
        String[] headers = {"Source IP", "Destination IP", "Source Port", "Destination Port", "Payload Length", "Timestamp", "Protocol ID", "Protocol Name", "Packet Size","Flow Size"};
        infoToCsv(headers, Csv_Data, filename+"_output.csv");
        
        System.out.println("Conversion to CSV finished\n");
        */


        //This is how you create a FileContent object, which will read the CSV file and store its content in a List of List of String
        FileContent detect = new FileContent(filename+"_output.csv");


        /*
        //Uncomment this block only if you want to read all the CSV file
        System.out.println("\nData read from CSV file:\n");

        List<List<String>> data = detect.getFile_content();
        
        for (int i = 0; i < data.size(); i++) {
            List<String> row = data.get(i);
            System.out.println("Row " + i + ": " + String.join(", ", row));
        }
        */

        //Now we will use all the detections methods on the FileContent we created
        System.out.println("\n\nDetection of suspicious patterns:\n\n");

        List<String>[] timestampSuspicious = DetectionMethods.TimestampSuspicious(detect, 10L,1L);
        List<Integer> payloadLengthSuspicious = DetectionMethods.PayloadLength_1(detect);
        List<Integer> packetSizeSuspicious = DetectionMethods.PacketSizeSuspicious(detect);
        List<String>[] ipCountSuspicious = DetectionMethods.IpCountSuspicious(detect);
        List<Integer> portCountSuspicious = DetectionMethods.PortTargetSuspicious(detect);
        List<String>[] portChainingSuspicious = DetectionMethods.PortChainingSuspicious(detect);

    }
}
