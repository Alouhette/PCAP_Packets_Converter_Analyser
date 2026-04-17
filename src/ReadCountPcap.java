import ca.uqac.lif.cep.Connector;
import ca.uqac.lif.cep.Connector.ConnectorException;
import ca.uqac.lif.cep.functions.ApplyFunction;
import ca.uqac.lif.cep.tmf.QueueSink;
import netp.FlowTransmitter;
import netp.PacketSource;
import netp.functions.GetDestinationIp;
import netp.functions.GetDestinationPort;
import netp.functions.GetFlowSize;
import netp.functions.GetPacketSize;
import netp.functions.GetPayloadLength;
import netp.functions.GetProtocolId;
import netp.functions.GetProtocolName;
import netp.functions.GetSourceIp;
import netp.functions.GetSourcePort;
import netp.functions.GetTimestamp;

public class ReadCountPcap {
    
    public static int countPackets(String filename) {
        PacketSource source = new netp.PacketSource(filename);
        ApplyFunction test = new ApplyFunction(new GetSourceIp());

         try{   
            Connector.connect(source, test);
        }
        catch (ConnectorException e){
            e.printStackTrace();
        }
         QueueSink sink= new QueueSink(1);
        try{
            Connector.connect(test, sink);
        }
        catch (ConnectorException e){
            e.printStackTrace();
        }
        int count = 0;

        source.push();
        while (sink.remove()[0] != null) {
            source.push();
            count++;
        }
        return count;
    }

    public static String[][] PacketReader(String filename){
        PacketSource Source1 = new netp.PacketSource(filename);
        PacketSource Source2 = new netp.PacketSource(filename);
        PacketSource Source3 = new netp.PacketSource(filename);
        PacketSource Source4 = new netp.PacketSource(filename);
        PacketSource Source5 = new netp.PacketSource(filename);
        PacketSource Source6 = new netp.PacketSource(filename);
        PacketSource Source7 = new netp.PacketSource(filename);
        PacketSource Source8 = new netp.PacketSource(filename);
        PacketSource Source9 = new netp.PacketSource(filename);
        PacketSource Source10 = new netp.PacketSource(filename);

        ApplyFunction srcIp = new ApplyFunction(new GetSourceIp());
        ApplyFunction dstIp = new ApplyFunction(new GetDestinationIp());
        ApplyFunction srcPort = new ApplyFunction(new GetSourcePort());
        ApplyFunction dstPort = new ApplyFunction(new GetDestinationPort());
        ApplyFunction payLength = new ApplyFunction(new GetPayloadLength());
        ApplyFunction tms = new ApplyFunction(new GetTimestamp());
        ApplyFunction PtId = new ApplyFunction(new GetProtocolId());
        ApplyFunction PtName = new ApplyFunction(new GetProtocolName());
        ApplyFunction PackSize = new ApplyFunction(new GetPacketSize());

        FlowTransmitter flow = new FlowTransmitter();
        ApplyFunction FlSize = new ApplyFunction(new GetFlowSize());

        try{
            Connector.connect(flow, FlSize);
        }catch (ConnectorException e){
            e.printStackTrace();
        }

        try{   
            Connector.connect(Source1, srcIp);
            Connector.connect(Source2, dstIp);
            Connector.connect(Source3, srcPort);
            Connector.connect(Source4, dstPort);
            Connector.connect(Source5, payLength);
            Connector.connect(Source6, tms);
            Connector.connect(Source7, PtId);
            Connector.connect(Source8, PtName);
            Connector.connect(Source9, PackSize);
            Connector.connect(Source10, flow);

            Connector.connect(flow, FlSize);
        }
        catch (ConnectorException e){
            e.printStackTrace();
        }
        QueueSink sink= new QueueSink(1);
        try{
            Connector.connect(srcIp, sink);
            Connector.connect(dstIp, sink);
            Connector.connect(srcPort, sink);
            Connector.connect(dstPort, sink);
            Connector.connect(payLength, sink);
            Connector.connect(tms, sink);
            Connector.connect(PtId, sink);
            Connector.connect(PtName, sink);
            Connector.connect(PackSize, sink);
            Connector.connect(FlSize, sink);

        }
        catch (ConnectorException e){
            e.printStackTrace();
        }


        int n = countPackets(filename);
        System.out.println("Nombre de paquets dans le fichier : " + n + "\n");
        String[][] Csv_Data = new String[n][10];

        for (int i = 0; i < n; i++){
            int j=0;
            
            Source1.push();
            Source2.push();
            Source3.push();
            Source4.push();
            Source5.push();
            Source6.push();
            Source7.push();
            Source8.push();
            Source9.push();
            Source10.push();

            String outputSRC = (String) sink.remove()[0];
            String outputDST = (String) sink.remove()[0];
            Integer outputSRCPort = (Integer) sink.remove()[0];
            Integer outputDSTPort = (Integer) sink.remove()[0];
            Integer outputPayloadLength = (Integer) sink.remove()[0];
            Long outputTimestamp = (Long) sink.remove()[0];
            Integer outputProtocolId = (Integer) sink.remove()[0];
            String outputProtocolName = (String) sink.remove()[0];
            Integer outputPacketSize = (Integer) sink.remove()[0];
            Integer outputFlowSize = (Integer) sink.remove()[0];

            Csv_Data[i][j++] = outputSRC;
            Csv_Data[i][j++] = outputDST;       
            Csv_Data[i][j++] = outputSRCPort.toString();
            Csv_Data[i][j++] = outputDSTPort.toString();
            Csv_Data[i][j++] = outputPayloadLength.toString();
            Csv_Data[i][j++] = outputTimestamp.toString();
            Csv_Data[i][j++] = outputProtocolId.toString();
            Csv_Data[i][j++] = outputProtocolName;
            Csv_Data[i][j++] = outputPacketSize.toString();
            Csv_Data[i][j++] = outputFlowSize.toString();

            //System.out.println(i + ": Payload Length: " + outputPayloadLength + ", Timestamp: " + outputTimestamp + ", Protocol ID: " + outputProtocolId + ", Protocol Name: " + outputProtocolName + ", Packet Size: " + outputPacketSize + ", Source IP: " + outputSRC + ", Destination IP: " + outputDST + ", Source Port: " + outputSRCPort + ", Destination Port: " + outputDSTPort +  ", Flow Size: " + outputFlowSize);
        }
        return Csv_Data;
    }


}
