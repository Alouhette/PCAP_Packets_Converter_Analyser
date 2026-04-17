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

package netp;

import java.io.FileNotFoundException;
import java.util.Queue;

import org.jnetpcap.Pcap;
import org.jnetpcap.packet.PcapPacket;
import org.jnetpcap.packet.PcapPacketHandler;
import org.jnetpcap.util.PcapPacketArrayList;

import ca.uqac.lif.cep.Processor;
import ca.uqac.lif.cep.tmf.Source;

/**
 * Outputs packets from a source .pcap file
 *
 */
public class PacketSource extends Source
{

  private Pcap pcap;

  private PcapPacketHandler<PcapPacketArrayList> jpacketHandler = new PcapPacketHandler<PcapPacketArrayList>()
  {
    public void nextPacket(PcapPacket packet, PcapPacketArrayList PacketsList)
    {
      PacketsList.add(packet);
    }
  };

  public PacketSource(String fileName)
  {
    super(1);

    final StringBuilder errbuf = new StringBuilder();
    pcap = Pcap.openOffline(fileName, errbuf);
    if (pcap == null)
    {
      try
      {
        throw new FileNotFoundException(errbuf.toString());
      }
      catch (FileNotFoundException e)
      {
        e.printStackTrace();
      }
    }
  }

  @Override
  protected boolean compute(Object[] inputs, Queue<Object[]> outputs)
  {
    PcapPacketArrayList packets = new PcapPacketArrayList();
    pcap.loop(1, jpacketHandler, packets);
    if (packets.isEmpty())
    {
      return false;
    }
    outputs.add(new Object[] {packets.get(0)});
    return true;
  }

  @Override
  public Processor clone()
  {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Processor duplicate(boolean with_state)
  {
    return null;
  }
}
