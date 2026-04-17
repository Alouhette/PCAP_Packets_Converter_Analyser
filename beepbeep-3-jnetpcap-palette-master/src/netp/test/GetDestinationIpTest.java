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
import ca.uqac.lif.cep.tmf.QueueSink;
import netp.PacketSource;
import netp.functions.*;

public class GetDestinationIpTest
{

  @Test
  public void getDestinationIpTest()
  {
    PacketSource source = new PacketSource("test.pcap");

    ApplyFunction destIp = new ApplyFunction(new GetDestinationIp());
    try
    {
      Connector.connect(source, destIp);
    }
    catch (ConnectorException e)
    {
      e.printStackTrace();
    }

    QueueSink sink = new QueueSink(1);
    try
    {
      Connector.connect(destIp, sink);
    }
    catch (ConnectorException e)
    {
      e.printStackTrace();
    }

    source.push();
    String output = (String) sink.remove()[0];
    System.out.println(output);

    String expected = "230.0.0.1";
    assertEquals(expected, output);
  }

}
