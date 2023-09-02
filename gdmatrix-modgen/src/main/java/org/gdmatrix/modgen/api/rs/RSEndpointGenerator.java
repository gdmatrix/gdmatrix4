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

import org.gdmatrix.modgen.schema.Response;
import org.gdmatrix.modgen.schema.Parameter;
import org.gdmatrix.modgen.schema.Operation;
import java.util.List;
import org.gdmatrix.modgen.JavaModuleGenerator;

/**
 *
 * @author realor
 */
public class RSEndpointGenerator extends ManagerRestGenerator
{
  public RSEndpointGenerator(JavaModuleGenerator moduleGenerator)
  {
    super(moduleGenerator);
  }

  @Override
  public boolean isInterface()
  {
    return false;
  }

  @Override
  public boolean isAddParameterAnnotations()
  {
    return false;
  }

  @Override
  protected String getPackageName()
  {
    return "org.gdmatrix.svc." + module.getName();
  }

  @Override
  protected String getClassName()
  {
    return module.getManager() + "RSEndpoint";
  }

  @Override
  protected void generateImports()
  {
    printer.println("import java.util.List;");
    printer.println("import jakarta.ws.rs.*;");
    printer.println("import jakarta.inject.Inject;");
    printer.println("import jakarta.inject.Singleton;");
    printer.println("import jakarta.enterprise.inject.Instance;");
    printer.println("import org.matrix.base.Result;");
    printer.println("import org.matrix." + module.getName() + ".*;");
    printer.println();
  }

  @Override
  protected void generateClassAnnotations()
  {
    printer.println("@Path(\"" + module.getName() + "\")");
    printer.println("@Singleton");
  }

  @Override
  protected void generateClassSignature()
  {
    super.generateClassSignature();
    printer.print(" implements " + module.getManager() + "Rest");
  }

  @Override
  protected void generateOperationAnnotations(Operation operation)
  {
    RestUtils.detectPathAndMethod(operation, moduleGenerator.getModuleFactory());

    printer.println("  @Override");
  }

  @Override
  protected void generateOperationCode(Operation operation)
  {
    boolean literalResponse = false;

    printer.print("    ");
    Response response = operation.getResponse();
    if (response != null)
    {
      literalResponse = isLiteralResponse(response);
      if (literalResponse)
      {
        printer.print("return new Result<>(");
      }
      else
      {
        printer.print("return ");
      }
    }
    printer.print("instance.get()." + operation.getName() + "(");
    List<Parameter> parameters = operation.getParameters();
    for (int i = 0; i < parameters.size(); i++)
    {
      Parameter parameter = parameters.get(i);
      if (i > 0)
      {
        printer.print(", ");
      }
      printer.print(moduleGenerator.getJavaName(parameter));
    }
    if (literalResponse) printer.print(")");
    printer.println(");");
  }

  @Override
  protected void generateMembers()
  {
    printer.println("  @Inject");
    printer.println("  Instance<" + module.getManager() + "> instance;");
    printer.println();
  }

  protected boolean isLiteralResponse(Response response)
  {
    return response.isBasicType() && !response.isMultiValued();
  }
}
