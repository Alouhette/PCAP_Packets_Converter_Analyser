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

import ca.uqac.lif.cep.functions.UnaryFunction;

/**
 * FlowFunction to get a network flow size (number of packets in the flow)
 *
 */
public class GetFlowSize extends UnaryFunction<JFlow, Integer>
{

  public GetFlowSize()
  {
    super(JFlow.class, Integer.class);
  }

  @Override
  public Integer getValue(JFlow flow)
  {
    return flow.size();
  }

  @Override
  public GetFlowSize duplicate(boolean with_state)
  {
    return this;
  }
}
