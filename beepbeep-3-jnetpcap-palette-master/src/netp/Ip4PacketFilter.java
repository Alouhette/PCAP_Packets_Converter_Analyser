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

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Queue;

import ca.uqac.lif.cep.Processor;
import org.jnetpcap.packet.JPacket;
import org.jnetpcap.protocol.network.Ip4;
import org.jnetpcap.protocol.tcpip.Tcp;
import org.jnetpcap.protocol.tcpip.Udp;

import ca.uqac.lif.cep.SynchronousProcessor;

/**
 * Discards packets from an input trace based on a selection criterion. These
 * criterion are net protocol, source and destination IP, and source and
 * destination port. A packet must satisfy ALL criterion to pass through the
 * filter. At least, it lets only IPv4 packets through, and discards the rest.
 */
public class Ip4PacketFilter extends SynchronousProcessor
{

  public static final int ICMP = 1;
  public static final int TCP = 6;
  public static final int UDP = 17;
  public static final int ANY_PROTOCOL = 0;

  // filter criterion
  private int protocol;
  private ArrayList<byte[]> sourceIp;
  private int sourcePortLow;
  private int sourcePortHigh;
  private ArrayList<byte[]> destinationIp;
  private int destinationPortLow;
  private int destinationPortHigh;

  // variable for inside use only
  private Ip4 ip4;
  int tmpProtocol;
  private Tcp tcp;
  private Udp udp;
  private boolean found;

  /**
   * Instantiates a default filter that allows all packets through
   */
  public Ip4PacketFilter()
  {
    super(1, 1);
    protocol = ANY_PROTOCOL;
    sourcePortLow = 0;
    sourcePortHigh = 65535;
    destinationPortLow = 0;
    destinationPortHigh = 65535;
    sourceIp = new ArrayList<byte[]>();
    destinationIp = new ArrayList<byte[]>();
  }

  /**
   * Adds a source IP to allow through the filter. If the IP list is empty, all
   * IPs pass through.
   * 
   * @param src
   *          IP address in string form (e.g. "192.168.0.1")
   */
  public void addSourceIp(String src)
  {
    try
    {
      InetAddress ip = InetAddress.getByName(src);
      byte[] b = ip.getAddress();
      sourceIp.add(b);
    }
    catch (UnknownHostException e)
    {
      e.printStackTrace();
    }
  }

  /**
   * Remove a source IP from the filter. If the IP list is empty, all IPs pass
   * through.
   * 
   * @param src
   *          IP address in string form (e.g. "192.168.0.1")
   */
  public void removeSourceIp(String src)
  {
    try
    {
      InetAddress ip = InetAddress.getByName(src);
      byte[] b = ip.getAddress();
      sourceIp.remove(b);
    }
    catch (UnknownHostException e)
    {
      e.printStackTrace();
    }
  }

  /**
   * Adds a destination IP to allow through the filter. If the IP list is empty,
   * all IPs pass through.
   * 
   * @param dst
   *          IP address in string form (e.g. "192.168.0.1")
   */
  public void addDestinationIp(String dst)
  {
    try
    {
      InetAddress ip = InetAddress.getByName(dst);
      byte[] b = ip.getAddress();
      destinationIp.add(b);
    }
    catch (UnknownHostException e)
    {
      e.printStackTrace();
    }
  }

  /**
   * Remove a destination IP from the filter. If the IP list is empty, all IPs
   * pass through.
   * 
   * @param dst
   *          IP address in string form (e.g. "192.168.0.1")
   */
  public void removeDestinationIp(String dst)
  {
    try
    {
      InetAddress ip = InetAddress.getByName(dst);
      byte[] b = ip.getAddress();
      destinationIp.remove(b);
    }
    catch (UnknownHostException e)
    {
      e.printStackTrace();
    }
  }

  /**
   * Set the protocol to allow through the filter. The filtered protocol is the
   * one within the IPv4 header.
   * 
   * @param protocol
   *          the protocol ID, or <code>PacketFilter.ANY_PROTOCOL</code> to allow
   *          all protocols
   */
  public void setProtocol(int protocol)
  {
    this.protocol = protocol;
  }

  /**
   * Set the source port to allow through the filter
   * 
   * @param src
   *          the port number
   */
  public void setSourcePort(int src)
  {
    sourcePortLow = src;
    sourcePortHigh = src;
  }

  /**
   * Set a range of source ports to allow through the filter
   * 
   * @param low
   *          the lower bound of the port range to allow
   * @param high
   *          the upper bound of the port range to allow
   */
  public void setSourcePortRange(int low, int high)
  {
    sourcePortLow = low;
    sourcePortHigh = high;
  }

  /**
   * Set the destination port to allow through the filter
   * 
   * @param dst
   *          the port number
   */
  public void setDestinationPort(int dst)
  {
    destinationPortLow = dst;
    destinationPortHigh = dst;
  }

  /**
   * Set a range of destination ports to allow through the filter
   * 
   * @param low
   *          the lower bound of the port range to allow
   * @param high
   *          the upper bound of the port range to allow
   */
  public void setDestinationPortRange(int low, int high)
  {
    destinationPortLow = low;
    destinationPortHigh = high;
  }

  @Override
  protected boolean compute(Object[] inputs, Queue<Object[]> outputs)
  {
    JPacket packet = (JPacket) inputs[0];
    ip4 = new Ip4(); // retrieve IP header
    if (!packet.hasHeader(ip4))
    {
      return true;
    }

    // check protocol
    if (protocol > 0 && ip4.type() != protocol)
    {
      return true;
    }

    tmpProtocol = ip4.type();

    // check source and destination ports
    if (tmpProtocol == TCP)
    {
      tcp = new Tcp();
      if (!packet.hasHeader(tcp))
      {
        return true;
      }
      if (!(sourcePortLow <= tcp.source() && tcp.source() <= sourcePortHigh)
          || !(destinationPortLow <= tcp.destination() && tcp.destination() <= destinationPortHigh))
      {
        return true;
      }
    }
    if (tmpProtocol == UDP)
    {
      udp = new Udp();
      if (!packet.hasHeader(udp))
      {
        return true;
      }
      if (!(sourcePortLow <= udp.source() && udp.source() <= sourcePortHigh)
          || !(destinationPortLow <= udp.destination() && udp.destination() <= destinationPortHigh))
      {
        return true;
      }
    }

    // check source and destination IPs
    if (!sourceIp.isEmpty())
    { // allow all ips if sourceIp is empty
      found = false;
      for (byte[] ip : sourceIp)
      {
        if (Arrays.equals(ip, ip4.source()))
        {
          found = true;
          break;
        }
      }
      if (!found)
      {
        return true;
      }
    }

    if (!destinationIp.isEmpty())
    { // allow all ips if destinationIp is
      // empty
      found = false;
      for (byte[] ip : destinationIp)
      {
        if (Arrays.equals(ip, ip4.destination()))
        {
          found = true;
          break;
        }
      }
      if (!found)
      {
        return true;
      }
    }

    // Packet went through the filter
    Object[] out = new Object[1];
    out[0] = packet;
    outputs.add(out);

    return true;
  }

  @SuppressWarnings("unchecked")
  @Override
  public Ip4PacketFilter clone()
  {
    Ip4PacketFilter clone = new Ip4PacketFilter();
    clone.protocol = protocol;
    clone.sourceIp = (ArrayList<byte[]>) sourceIp.clone();
    clone.sourcePortLow = sourcePortLow;
    clone.sourcePortHigh = sourcePortHigh;
    clone.destinationIp = (ArrayList<byte[]>) destinationIp.clone();
    clone.destinationPortLow = destinationPortLow;
    clone.destinationPortHigh = destinationPortHigh;
    return clone;
  }

  @Override
  public Processor duplicate(boolean with_state)
  {
    return null;
  }
}
