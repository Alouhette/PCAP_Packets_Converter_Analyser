package examples;

import ca.uqac.lif.cep.Connector;
import ca.uqac.lif.cep.functions.ApplyFunction;
import ca.uqac.lif.cep.tmf.QueueSink;
import netp.NetworkInterfaceSource;
import netp.functions.GetSourceIp;

/**
 * Shows how to capture packets live from a network interface, and displays
 * their source ip.
 */

public class LiveCapture
{

  public static void main(String[] args) throws Exception
  {
    // outputs packets from a network interface
    // "any" takes packets from all devices
    NetworkInterfaceSource source = new NetworkInterfaceSource("any");

    // extract source IP address of packet
    ApplyFunction sourceIp = new ApplyFunction(new GetSourceIp());
    Connector.connect(source, sourceIp);

    // retrieve results
    QueueSink sink = new QueueSink(1);
    Connector.connect(sourceIp, sink);

    // compute the first 100 packets
    for (int i = 0; i < 100; i++)
    {
      source.push();
      String output = (String) sink.remove()[0];
      System.out.println(i + ": " + output);
    }

  }

}
