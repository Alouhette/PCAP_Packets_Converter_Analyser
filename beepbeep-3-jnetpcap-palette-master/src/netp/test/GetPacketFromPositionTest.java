/*
    BeepBeep, an event stream processor
    Copyright (C) 2008-2016 Sylvain Hallé

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as published
    by the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package netp.test;

import static org.junit.Assert.*;

import org.junit.Test;

import ca.uqac.lif.cep.Connector;
import ca.uqac.lif.cep.Connector.ConnectorException;
import ca.uqac.lif.cep.functions.ApplyFunction;
import ca.uqac.lif.cep.functions.TurnInto;
import ca.uqac.lif.cep.tmf.Fork;
import ca.uqac.lif.cep.tmf.QueueSink;
import netp.FlowTransmitter;
import netp.PacketSource;
import netp.functions.GetPacketFromPosition;
import netp.functions.GetSourceIp;

public class GetPacketFromPositionTest
{

  // TODO complete this test !!!!!!
  @Test
  public void getPacketFromPositionTest()
  {
    PacketSource source = new PacketSource("test.pcap");

    Fork fork = new Fork(2);
    try
    {
      Connector.connect(source, fork);
    }
    catch (ConnectorException e)
    {
      e.printStackTrace();
    }

    FlowTransmitter flow = new FlowTransmitter();
    TurnInto position = new TurnInto((Integer) 0);
    try
    {
      Connector.connect(fork, flow);
      Connector.connect(fork, 1, position, 0);
    }
    catch (ConnectorException e)
    {
      e.printStackTrace();
    }

    ApplyFunction packet = new ApplyFunction(new GetPacketFromPosition());
    try
    {
      Connector.connect(flow, position, packet);
    }
    catch (ConnectorException e)
    {
      e.printStackTrace();
    }

    ApplyFunction srcIp = new ApplyFunction(new GetSourceIp());
    try
    {
      Connector.connect(packet, srcIp);
    }
    catch (ConnectorException e)
    {
      e.printStackTrace();
    }

    /*
     * Pullable p = srcIp.getPullableOutput(); String output = (String) p.pull();
     * System.out.println(output);
     */

    QueueSink sink = new QueueSink(1);
    try
    {
      Connector.connect(srcIp, sink);
    }
    catch (ConnectorException e)
    {
      e.printStackTrace();
    }

    source.push();
    String output = (String) sink.remove()[0];
    System.out.println(output);

    String expected = "172.16.24.194";
    assertEquals(expected, output);

  }

}
