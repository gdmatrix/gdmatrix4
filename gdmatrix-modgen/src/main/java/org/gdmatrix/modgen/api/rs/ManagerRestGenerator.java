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

import org.gdmatrix.modgen.AbstractManagerGenerator;
import org.gdmatrix.modgen.JavaModuleGenerator;
import org.gdmatrix.modgen.schema.Response;
import org.gdmatrix.modgen.schema.Parameter;
import org.gdmatrix.modgen.schema.Operation;

/**
 *
 * @author realor
 */
public class ManagerRestGenerator extends AbstractManagerGenerator
{
  public ManagerRestGenerator(JavaModuleGenerator moduleGenerator)
  {
    super(moduleGenerator);
  }

  @Override
  public boolean isInterface()
  {
    return true;
  }

  @Override
  public boolean isAddParameterAnnotations()
  {
    return true;
  }

  @Override
  protected String getPackageName()
  {
    return module.getJavaPackage();
  }

  @Override
  protected String getClassName()
  {
    return module.getManager() + "Rest";
  }

  @Override
  protected void generateImports()
  {
    printer.println("import org.matrix.base.Result;");
    printer.println("import java.util.List;");
    printer.println("import jakarta.ws.rs.*;");
    printer.println("import static jakarta.ws.rs.core.MediaType.*;");
    printer.println();
  }

  @Override
  protected void generateClassAnnotations()
  {
    printer.println("@Path(\"" + module.getName() + "\")");
  }

  @Override
  protected String getResponseTypeName(Response response)
  {
    if (!response.isMultiValued())
    {
      String typeName = response.getType();
      if ("xs:string".equals(typeName))
      {
        return "Result<String>";
      }
      else if ("xs:boolean".equals(typeName))
      {
        return "Result<Boolean>";
      }
      else if ("xs:int".equals(typeName))
      {
        return "Result<Integer>";
      }
      else if ("xs:long".equals(typeName))
      {
        return "Result<Long>";
      }
      else if ("xs:float".equals(typeName))
      {
        return "Result<Float>";
      }
      else if ("xs:double".equals(typeName))
      {
        return "Result<Double>";
      }
      else
      {
        return moduleGenerator.getJavaTypeName(response);
      }
    }
    else
    {
      return moduleGenerator.getJavaTypeName(response);
    }
  }

  @Override
  protected boolean isOperationIgnored(Operation operation)
  {
    return "true".equals(operation.getAttribute("jaxrs.ignore"));
  }

  @Override
  protected void generateOperationAnnotations(Operation operation)
  {
    RestUtils.detectPathAndMethod(operation, moduleGenerator.getModuleFactory());

    String method = operation.getAttribute("jaxrs.method");
    String path = operation.getAttribute("jaxrs.path");

    printer.println("  @" + method);
    printer.println("  @Path(\"" + path + "\")");
    
    String consumes = operation.getAttribute("jaxrs.consumes");
    if (consumes == null)
    {
      consumes = "APPLICATION_JSON";
    }
    printer.println("  @Consumes(" + consumes + ")");

    String produces = operation.getAttribute("jaxrs.produces");
    if (produces == null)
    {
      produces = "APPLICATION_JSON";
    }
    printer.println("  @Produces(" + produces + ")");
  }

  @Override
  protected void generateParameterAnnotations(Parameter parameter)
  {
    String input = parameter.getAttribute("jaxrs.input");

    if ("path".equals(input))
    {
      printer.println("    @PathParam(\"" + parameter.getName() + "\")");
    }
    else if ("header".equals(input))
    {
      printer.println("    @HeaderParam(\"" + parameter.getName() + "\")");
    }
    else if ("query".equals(input))
    {
      printer.println("    @QueryParam(\"" + parameter.getName() + "\")");
    }
    // else request body
  }
}
