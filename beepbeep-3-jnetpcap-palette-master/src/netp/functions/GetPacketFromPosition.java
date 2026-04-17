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

import org.jnetpcap.packet.JFlow;
import org.jnetpcap.packet.JPacket;

import ca.uqac.lif.cep.functions.BinaryFunction;

/**
 * FlowIntegerFunction to get a network packet from a flow packet and his
 * position
 *
 */
public class GetPacketFromPosition extends BinaryFunction<JFlow, Integer, JPacket>
{

  public GetPacketFromPosition()
  {
    super(JFlow.class, Integer.class, JPacket.class);
  }

  @Override
  public JPacket getValue(JFlow flow, Integer position)
  {
    if (0 <= position && position < flow.size())
    {
      return flow.getAll().get(position);
    }
    else
    {
      return null;
    }
  }

  @Override
  public GetPacketFromPosition duplicate(boolean with_state)
  {
    return this;
  }
}
