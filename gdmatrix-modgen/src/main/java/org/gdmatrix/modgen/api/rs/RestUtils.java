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
package org.gdmatrix.modgen.api.rs;

import java.util.List;
import org.gdmatrix.modgen.ModuleFactory;
import org.gdmatrix.modgen.schema.Enumeration;
import org.gdmatrix.modgen.schema.Operation;
import org.gdmatrix.modgen.schema.Response;
import org.gdmatrix.modgen.schema.Module;
import org.gdmatrix.modgen.schema.Parameter;
import org.gdmatrix.modgen.schema.Type;

/**
 *
 * @author realor
 */
public class RestUtils
{
  public static void detectPathAndMethod(Operation operation,
    ModuleFactory moduleFactory)
  {
    // detect path
    String path = operation.getAttribute("jaxrs.path");
    if (path == null)
    {
      String operationName = operation.getName();
      if (operationName.startsWith("load"))
      {
        Parameter parameter = operation.getParameters().get(0);
        String idName = parameter.getName();
        if (!idName.endsWith("Id"))
        {
          idName += "Id";
          parameter.setName(idName);
        }
        parameter.setAttribute("jaxrs.input", "path");
        path = "/" + getObjectName(operation) + "/{" + idName + "}";
      }
      else if (operationName.startsWith("store"))
      {
        path = "/" + getObjectName(operation);
      }
      else if (operationName.startsWith("remove"))
      {
        Parameter parameter = operation.getParameters().get(0);
        String idName = parameter.getName();
        if (!idName.endsWith("Id"))
        {
          idName += "Id";
          parameter.setName(idName);
        }
        parameter.setAttribute("jaxrs.input", "path");
        path = "/" + getObjectName(operation) + "/{" + idName + "}";
      }
      else if (operationName.startsWith("find"))
      {
        path = "/" + getObjectName(operation);
      }
      else if (operationName.startsWith("count"))
      {
        path = "/" + getObjectName(operation) + "/count";
      }
      else if (operationName.startsWith("get"))
      {
        path = "/" + operationName.substring(3);
      }
      else
      {
        path = "/" + operationName;
      }
      operation.setAttribute("jaxrs.path", path);
    }

    // detect method
    String method = operation.getAttribute("jaxrs.method");
    if (method == null)
    {
      String operationName = operation.getName();
      if (operationName.startsWith("load") ||
          operationName.startsWith("get") ||
          operationName.startsWith("list"))
      {
        method = "GET";
      }
      else if (operationName.startsWith("store"))
      {
        method = "PUT";
      }
      else if (operationName.startsWith("remove"))
      {
        method = "DELETE";
      }
      else
      {
        method = "POST";
      }
      operation.setAttribute("jaxrs.method", method);
    }

    // complete parameters
    List<Parameter> parameters = operation.getParameters();
    for (Parameter parameter : parameters)
    {
      String input = parameter.getAttribute("jaxrs.input");

      if (input == null)
      {
        if (path.contains("{" + parameter.getName() + "}"))
        {
          parameter.setAttribute("jaxrs.input", "path");
        }
        else
        {
          String paramTypeName = parameter.getType();
          Type paramType = moduleFactory.getType(paramTypeName);
          if (paramType instanceof Enumeration
            || paramTypeName.startsWith("xs:")
            || paramTypeName.startsWith("mx:date")
            || paramTypeName.startsWith("mx:time"))
          {
            parameter.setAttribute("jaxrs.input", "query");
          }
        }
      }
    }
  }

  public static String getObjectName(Operation operation)
  {
    Module module = operation.getModule();
    String objectName = operation.getName();
    Response response = operation.getResponse();
    if (response != null)
    {
      String typeName = response.getType();
      if (typeName.startsWith(module.getName() + ":"))
      {
        objectName = typeName.substring(module.getName().length() + 1);
      }
      else
      {
        int i = 0;
        while (i < objectName.length())
        {
          char ch = objectName.charAt(i);
          if (ch >= 'A' && ch <= 'Z') break;
          i++;
        }
        objectName = objectName.substring(i);
        if (objectName.endsWith("ies"))
        {
          objectName = objectName.substring(0, objectName.length() - 3) + "y";
        }
        else if (objectName.endsWith("s"))
        {
          objectName = objectName.substring(0, objectName.length() - 1);
        }
      }
    }
    return objectName;
  }
}
