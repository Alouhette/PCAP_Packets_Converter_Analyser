/*
    BeepBeep, an event stream processor
    Copyright (C) 2008-2026 Sylvain Hallé

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

import java.util.Queue;

import jakarta.xml.bind.DatatypeConverter;

import org.jnetpcap.packet.JPacket;
import org.jnetpcap.protocol.network.Ip4;
import org.jnetpcap.protocol.tcpip.Tcp;
import org.jnetpcap.protocol.tcpip.Udp;

import ca.uqac.lif.cep.Processor;
import ca.uqac.lif.cep.SynchronousProcessor;

/**
 * Discards packets from an input trace based on the content of its payload.
 * User can specify a byte[] or hex string as filter value, and only packets
 * containing that exact sequence of bytes pass through the filter.
 */
public class PayloadFilter extends SynchronousProcessor
{

  // Filter value as a sequence of bytes
  private byte[] filter;

  // for internal use only
  private JPacket packet;
  private byte[] payload;
  private int protocol;
  private Ip4 ip4;
  private Tcp tcp;
  private Udp udp;

  /**
   * Instantiates a default filter that allows all packets through
   */
  public PayloadFilter()
  {
    super(1, 1);
    filter = new byte[0];
  }

  /**
   * Sets the filter from a hex value in string form.
   * 
   * @param hex
   *          Hex value of the filter (eg. "2D45")
   * @return true if string parsing was successful, false otherwise
   */
  public boolean setFilter(String hex)
  {
    try
    {
      this.filter = DatatypeConverter.parseHexBinary(hex);
    }
    catch (IllegalArgumentException e)
    {
      // do not change anything
      return false;
    }
    return true;
  }

  /**
   * Sets the filter from a byte sequence.
   * 
   * @param filter
   *          Byte sequence
   * @return true if the affectation was successful
   */
  public boolean setFilter(byte[] filter)
  {
    this.filter = filter;
    return true;
  }

  @Override
  protected boolean compute(Object[] inputs, Queue<Object[]> outputs)
  {
    ip4 = new Ip4();
    packet = (JPacket) inputs[0];
    protocol = packet.getHeader(ip4).type();

    tcp = new Tcp();
    udp = new Udp();

    if (protocol == 6 && packet.hasHeader(tcp))
    {
      payload = tcp.getPayload();
    }

    if (protocol == 17 && packet.hasHeader(udp))
    {
      payload = udp.getPayload();
    }

    if (contains(payload, filter))
    {
      Object[] out = new Object[1];
      out[0] = packet;
      outputs.add(out);
      return true;
    }

    return true;
  }

  @Override
  public Processor clone()
  {
    PayloadFilter clone = new PayloadFilter();
    clone.filter = this.filter.clone();
    return clone;
  }

  /**
   * Checks if array <code>payload</code> contains the exact same sequence of
   * bytes in <code>filter</code> in that order.
   * 
   * @param payload
   *          Byte array to compare
   * @param filter
   *          Sequence of bytes to find in <code>payload</code>
   * @return true if <code>payload</code> contains <code>filter</code>, false
   *         otherwise
   */
  private boolean contains(byte[] payload, byte[] filter)
  {
    if (filter.length == 0)
      return true;
    if (filter.length > payload.length)
      return false;
    for (int i = 0; i < payload.length; i++)
    {
      if (payload[i] == filter[0])
      {
        for (int j = 0; j < filter.length && i + j < payload.length
            && payload[i + j] == filter[j]; j++)
        {
          if (j == filter.length - 1)
            return true;
        }
      }
    }
    return false;
  }

  @Override
  public Processor duplicate(boolean with_state)
  {
    return null;
  }
}
