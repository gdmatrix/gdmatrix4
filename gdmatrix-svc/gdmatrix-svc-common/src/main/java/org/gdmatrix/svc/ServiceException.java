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
package org.gdmatrix.svc;

import java.util.ResourceBundle;

/**
 *
 * @author realor
 */
public class ServiceException extends RuntimeException
{

  private final String code;
  private final Object[] arguments;

  public ServiceException(String code)
  {
    this(code, new Object[0]);
  }

  public ServiceException(String code, Object... arguments)
  {
    this.code = code;
    this.arguments = arguments;
  }

  public String getCode()
  {
    return code;
  }

  public Object[] getArguments()
  {
    return arguments;
  }

  @Override
  public String getMessage()
  {
    String message = code;
    if (arguments.length > 0)
    {
      message += " " + getDetail();
    }
    return message;
  }

  public String getDetail()
  {
    //TODO: translate detail
    StringBuilder builder = new StringBuilder();
    for (int i = 0; i < arguments.length; i++)
    {
      if (i > 0)
      {
        builder.append(", ");
      }
      Object arg = arguments[i];
      if (arg instanceof String)
      {
        String value = (String) arg;
        value = value.replaceAll("'", "\'");
        builder.append('\'').append(value).append('\'');
      }
      else
      {
        builder.append(arg);
      }
    }
    return builder.toString();
  }

//  protected ResourceBundle getBundle()
//  {
//    int index = code.indexOf(":");
//    if (index == -1)
//    {
//      return ResourceBundle.getBundle("gdmatrix");
//    }
//    else
//    {
//      String module = code.substring(0, index);
//      return ResourceBundle.getBundle(module);
//    }
//  }
}
