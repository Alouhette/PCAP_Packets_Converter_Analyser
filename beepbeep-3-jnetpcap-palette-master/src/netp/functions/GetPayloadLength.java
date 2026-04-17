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
import org.jnetpcap.packet.Payload;

import ca.uqac.lif.cep.functions.UnaryFunction;

/**
 * PacketFunction to get payload packet lenght of a network packet (in bytes)
 *
 */
public class GetPayloadLength extends UnaryFunction<JPacket, Integer>
{

  private Payload payload;

  public GetPayloadLength()
  {
    super(JPacket.class, Integer.class);
    payload = new Payload();
  }

  @Override
  public Integer getValue(JPacket packet)
  {
    if (packet.hasHeader(payload))
    {
      return payload.getLength();
    }
    return -1;
  }

  @Override
  public GetPayloadLength duplicate(boolean with_state)
  {
    return this;
  }
}
