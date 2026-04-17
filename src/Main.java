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

        String filename = "test.pcap";
        //String filename = "media_1000_pcap_anonymize.pcap";
        /* 

        System.out.println("Currently Trying to read file : "+filename+"\n");
        
        String[][] Csv_Data = ReadCountPcap.PacketReader(filename);
        String[] headers = {"Source IP", "Destination IP", "Source Port", "Destination Port", "Payload Length", "Timestamp", "Protocol ID", "Protocol Name", "Packet Size","Flow Size"};
        infoToCsv(headers, Csv_Data, filename+"_output.csv");
        
        System.out.println("Conversion to CSV finished\n");
        */

        System.out.println("\nData read from CSV file:\n");

        FileContent detect = new FileContent(filename+"_output.csv");
        List<List<String>> data = detect.getFile_content();
        for (int i = 0; i < data.size(); i++) {
            List<String> row = data.get(i);
            System.out.println("Row " + i + ": " + String.join(", ", row));
        }

        System.out.println("\n\nDetection of suspicious patterns:\n\n");
        List<String>[] timestampSuspicious = DetectionMethods.timestampSuspicious(detect, 10L,1L);
        List<Integer> payloadLengthSuspicious = DetectionMethods.PayloadLength_1(detect);
        List<Integer> packetSizeSuspicious = DetectionMethods.PacketSizeSuspicious(detect);
        List<String>[] ipCountSuspicious = DetectionMethods.IpCountSuspicious(detect);
        List<Integer> portCountSuspicious = DetectionMethods.PortTargetSuspicious(detect);
        List<String>[] portChainingSuspicious = DetectionMethods.PortChainingSuspicious(detect);

    }
}
