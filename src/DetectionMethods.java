import java.util.ArrayList;
import java.util.List;

public class DetectionMethods {

    public static  List<String>[] TimestampSuspicious(FileContent fileContent, Long ts_diff_max, Long ts_diff_Hmax){

        List<List<String>> data = fileContent.getFile_content();
        List<String> suspicious_rows = new ArrayList<>();
        List<String> Highly_suspicious_rows = new ArrayList<>();

        for (int i = 2; i < data.size(); i++) {
            List<String> row = data.get(i);
            List<String> row_before = data.get(i-1);
            Long ts_diff = Long.parseLong(row.get(5)) - Long.parseLong(row_before.get(5));
            try {
                if (ts_diff == 0){
                    Highly_suspicious_rows.add((i-1)+"-"+i);
                }   
                else if (ts_diff <= ts_diff_Hmax && ts_diff >= ts_diff_Hmax*-1) {
                    Highly_suspicious_rows.add((i-1)+"-"+i);
                }
                else if (ts_diff <= ts_diff_max && ts_diff >= ts_diff_max*-1) {  
                    suspicious_rows.add((i-1)+"-"+i);
                }
            } catch (NumberFormatException e) {
                System.err.println("Row : " + i + " (Error)\n");
            }
        }
        System.out.println("Rows with HIGHLY suspicious timestamp differences: " + Highly_suspicious_rows);
        System.out.println("Rows with suspicious timestamp differences: " + suspicious_rows + "\n");
        return new List[]{suspicious_rows, Highly_suspicious_rows};
    }

    public static List<Integer> PayloadLength_1(FileContent fileContent){
        List<List<String>> data = fileContent.getFile_content();
        List<Integer> suspicious_rows = new ArrayList<>();
        for (int i = 1; i < data.size(); i++) {
            List<String> row = data.get(i);
            if (row.get(4).equals("-1")) {
                suspicious_rows.add(i);
            }
        }
        System.out.println("Rows with suspicious payload lengths: " + suspicious_rows + "\n");
        return suspicious_rows;
    }

    public static List<Integer> PacketSizeSuspicious(FileContent fileContent){
        List<List<String>> data = fileContent.getFile_content();
        List<Integer> suspicious_rows = new ArrayList<>();
        for (int i = 1; i < data.size(); i++) {
            List<String> row = data.get(i);
            if ( Long.parseLong(row.get(8))< 60 || Long.parseLong(row.get(8)) > 1500) {
                suspicious_rows.add(i);
            }
        }
        System.out.println("Rows with suspicious packet sizes: " + suspicious_rows + "\n");
        return suspicious_rows;
    }

    public static List<String>[] IpCountSuspicious(FileContent fileContent){
        List<List<String>> data = fileContent.getFile_content();
        List<String> rows = new ArrayList<>();
        List<Integer> counts = new ArrayList<>();
        List<String> ports = new ArrayList<>();

        for (int i = 1; i<data.size();i++){
            List<String> row = data.get(i);
            String[] content = {row.get(0), row.get(1)};
            for (String ip : content){
                 String ipPortsUsed = "";
                 if(!rows.contains(ip)){
                    int count = 0;
                    for (int j = 1; j<data.size();j++){
                        List<String> row_j = data.get(j);
                        if(row_j.get(0).equals(ip)){
                            count++;
                            ipPortsUsed = ipPortsUsed + row_j.get(2)+ " ";
                        }
                        else if (row_j.get(1).equals(ip)){
                            count++;
                            ipPortsUsed = ipPortsUsed + row_j.get(3)+ " ";
                        }
                    }
                    rows.add(ip);
                    counts.add(count);
                    ports.add(ipPortsUsed);
                }
            }
        }
        System.out.println("All Ips: " + rows + "\nCount: " + counts + "\nPorts used: " + ports + "\n");
        return new List[]{rows, ports, counts};
    }

    public static List<Integer> PortTargetSuspicious(FileContent fileContent){
        List<List<String>> data = fileContent.getFile_content();
        List<Integer> suspicious_rows = new ArrayList<>();
        for (int i = 1; i < data.size(); i++) {
            List<String> row = data.get(i);
            if (row.get(3).equals("22") || row.get(3).equals("80") || row.get(3).equals("443") || row.get(3).equals("21") || row.get(3).equals("23")) {
                suspicious_rows.add(i);
            }
        }
        System.out.println("Rows with suspicious target ports: " + suspicious_rows + "\n");
        return suspicious_rows;
    }

    public static List<String>[] PortChainingSuspicious(FileContent fileContent){
        List<List<String>> data = fileContent.getFile_content();
        List<String> suspicious_rows = new ArrayList<>();
        List<String> registeredIp = null;
        List<String> registeredPorts = new ArrayList<>();
        int[] start_end = new int[2];
        for (int i = 1; i < data.size(); i++) {
            List<String> row = data.get(i);
            if (registeredIp == null){
                registeredIp = row;
                start_end[0] = Integer.parseInt(row.get(2));
                start_end[1] = Integer.parseInt(row.get(2));

            }
            else if(registeredIp.get(0).equals(row.get(0)) && (Integer.parseInt(registeredIp.get(3)) == (Integer.parseInt(row.get(3)) + 1))){
                start_end[1] = Integer.parseInt(row.get(3));
            }
            else{
                if(start_end[1] != start_end[0]){
                    suspicious_rows.add(registeredIp.get(0));
                    registeredPorts.add(start_end[0] + "->" + start_end[1]);
                }
                registeredIp = row;
                start_end[0] = Integer.parseInt(row.get(2));
                start_end[1] = Integer.parseInt(row.get(2));
            }
        }
        System.out.println("Rows with suspicious port chaining : " + suspicious_rows + "\n");
        return new List[]{suspicious_rows, registeredPorts};
    }
}