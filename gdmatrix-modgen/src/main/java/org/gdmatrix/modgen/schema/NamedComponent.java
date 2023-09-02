/*
 * GDMatrix
 *
 * Copyright (C) 2020-2023, Ajuntament de Sant Feliu de Llobregat
 *
 * This program is licensed and may be used, modified and redistributed under
 * the terms of the European Public License (EUPL), either version 1.1 or (at
 * your option) any later version as soon as they are approved by the European
 * Commission.
 *
 * Alternatively, you may redistribute and/or modify this program under the
 * terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either  version 3 of the License, or (at your option)
 * any later version.
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *
 * See the licenses for the specific language governing permissions, limitations
 * and more details.
 *
 * You should have received a copy of the EUPL1.1 and the LGPLv3 licenses along
 * with this program; if not, you may find them at:
 *
 * https://joinup.ec.europa.eu/software/page/eupl/licence-eupl
 * http://www.gnu.org/licenses/
 * and
 * https://www.gnu.org/licenses/lgpl.txt
 */
package org.gdmatrix.modgen.schema;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author realor
 */
public abstract class NamedComponent extends Component
{
  private String name;
  private Map<String, String> attributes;
  private final List<Documentation> documentations = new ArrayList<>();

  public String getName()
  {
    return name;
  }

  public void setName(String name)
  {
    this.name = name;
  }

  public List<Documentation> getDocumentations()
  {
    return documentations;
  }

  public String getAttribute(String name)
  {
    return attributes == null ? null : attributes.get(name);
  }

  public void setAttribute(String name, String value)
  {
    if (attributes == null)
    {
      attributes = new HashMap<>();
    }
    this.attributes.put(name, value);
  }

  public Set<String> getAttributeNames()
  {
    return attributes == null ? Collections.EMPTY_SET : attributes.keySet();
  }

  @Override
  public String toString()
  {
    return getClass().getSimpleName() + " " + getName();
  }
}
