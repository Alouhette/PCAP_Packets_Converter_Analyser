/*
    BeepBeep, an event stream processor
    Copyright (C) 2008-2017 Sylvain Hallé

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

import java.util.LinkedList;

import ca.uqac.lif.cep.functions.UnaryFunction;

/**
 * Compares every previous entries on a one-to-one basis. This function takes
 * width as an argument which specifies the number of entries to compare.
 * Complexity O(n)
 */
public class AllDifferent extends UnaryFunction<Object, Boolean>
{

  private int m_width;
  private int m_falseCount;
  private boolean allDifferent;

  private LinkedList<Object> m_previousInputs;

  /**
   * Instantiate this function with an infinite width.
   */
  public AllDifferent()
  {
    this(0);
  }

  /**
   * @param width
   *          The number of entries to compare one-to-one. 0 or less means
   *          infinite width.
   */
  public AllDifferent(int width)
  {
    super(Object.class, Boolean.class);
    m_width = Math.max(0, width);
    m_falseCount = 0;
    m_previousInputs = new LinkedList<Object>();
  }

  @Override
  public Boolean getValue(Object x)
  {

    // if a duplicate entry has been found and the width is infinite.
    if (m_falseCount > 0 && m_width <= 0)
      return false;

    allDifferent = true;

    // if duplicate entries were found in previous calls
    if (m_falseCount > 0)
    {
      m_falseCount--;
      allDifferent = false;
    }

    // compare this entry to every previous one
    for (int i = m_previousInputs.size() - 1; i >= 0; i--)
    {
      if (m_previousInputs.get(i).equals(x))
      {
        // this function will return false at least until this entry is removed
        m_falseCount = Math.max(m_falseCount, i + (m_width - m_previousInputs.size() - 1));
        allDifferent = false;
        break;
      }
    }

    // update previous entries
    m_previousInputs.add(x);
    if (m_width > 0 && m_previousInputs.size() >= m_width)
    {
      m_previousInputs.remove();
    }

    return allDifferent;
  }

  public int getWidth()
  {
    return m_width;
  }

}
