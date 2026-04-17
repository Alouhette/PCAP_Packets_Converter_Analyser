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

package netp.functions;

import org.jnetpcap.packet.JPacket;
import org.jnetpcap.protocol.tcpip.Tcp;
import org.jnetpcap.protocol.tcpip.Udp;

import ca.uqac.lif.cep.functions.UnaryFunction;

/**
 * PacketFunction to get source port from a network packet
 *
 */
public class GetSourcePort extends UnaryFunction<JPacket, Integer>
{

  private static final Tcp tcp = new Tcp();
  private static final Udp udp = new Udp();

  public GetSourcePort()
  {
    super(JPacket.class, Integer.class);
  }

  @Override
  public Integer getValue(JPacket packet)
  {
    if (packet.hasHeader(tcp))
    {
      return tcp.source();
    }
    if (packet.hasHeader(udp))
    {
      return udp.source();
    }
    return -1;
  }

  @Override
  public GetSourcePort duplicate(boolean with_state)
  {
    return this;
  }
}
