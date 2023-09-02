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
package org.gdmatrix.modgen.api.ws;

import org.gdmatrix.modgen.schema.Parameter;
import org.gdmatrix.modgen.schema.Operation;
import org.gdmatrix.modgen.JavaModuleGenerator;
import java.util.List;

/**
 *
 * @author realor
 */
public class WSEndpointGenerator extends ManagerPortGenerator
{
  public WSEndpointGenerator(JavaModuleGenerator moduleGenerator)
  {
    super(moduleGenerator);
  }

  @Override
  public boolean isInterface()
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
    return module.getManager() + "WSEndpoint";
  }

  @Override
  protected void generateImports()
  {
    printer.println("import java.util.List;");
    printer.println("import jakarta.jws.*;");
    printer.println("import jakarta.enterprise.inject.Instance;");
    printer.println("import jakarta.servlet.annotation.WebServlet;");
    printer.println("import jakarta.inject.Inject;");
    printer.println("import jakarta.inject.Singleton;");
    printer.println("import org.matrix." + module.getName() + ".*;");
    printer.println();
  }

  @Override
  protected void generateClassAnnotations()
  {
    printer.println("@WebServlet(\"/services/" + module.getName() + "\")");
    printer.println("@WebService(name = \"" + module.getName() + "\",");
    printer.println("  serviceName = \"" + module.getManager() + "Service\",");
    printer.println("  portName = \"" + module.getManager() + "Port\",");
    printer.println("  endpointInterface = \"" + module.getJavaPackage() + "." + module.getManager() + "Port\",");
    printer.println("  wsdlLocation = \"/WEB-INF/wsdl/" + module.getName() + ".wsdl\",");
    printer.println("  targetNamespace = \"" + module.getNamespace() + "\")");
    printer.println("@Singleton");
  }

  @Override
  protected void generateClassSignature()
  {
    super.generateClassSignature();
    printer.print(" implements " + module.getManager() + "Port");
  }

  @Override
  protected void generateOperationAnnotations(Operation operation)
  {
    printer.println("  @Override");
  }

  @Override
  protected void generateOperationCode(Operation operation)
  {
    printer.print("    ");
    if (operation.getResponse() != null)
    {
      printer.print("return ");
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
    printer.println(");");
  }

  @Override
  protected void generateMembers()
  {
    printer.println("  @Inject");
    printer.println("  Instance<" + module.getManager() + "> instance;");
    printer.println();
  }
}
