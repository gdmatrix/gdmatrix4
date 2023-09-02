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

import org.gdmatrix.modgen.AbstractManagerGenerator;
import org.gdmatrix.modgen.JavaModuleGenerator;
import org.gdmatrix.modgen.schema.Response;
import org.gdmatrix.modgen.schema.Parameter;
import org.gdmatrix.modgen.schema.Operation;

/**
 *
 * @author realor
 */
public class ManagerPortGenerator extends AbstractManagerGenerator
{
  public ManagerPortGenerator(JavaModuleGenerator moduleGenerator)
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
    return module.getManager() + "Port";
  }

  @Override
  protected void generateImports()
  {
    printer.println("import java.util.List;");
    printer.println("import jakarta.jws.*;");
    printer.println();
  }

  @Override
  protected void generateClassAnnotations()
  {
    printer.println("@WebService(name = \"" + module.getManager()
      + "Port\", targetNamespace = \"" + module.getNamespace() + "\")");
  }

  @Override
  protected boolean isOperationIgnored(Operation operation)
  {
    return "true".equals(operation.getAttribute("jaxws.ignore"));
  }

  @Override
  protected void generateOperationAnnotations(Operation operation)
  {
    printer.println("  @WebMethod");

    Response response = operation.getResponse();
    if (response != null)
    {
      printer.println("  @WebResult(name = \"" + response.getName() + "\", targetNamespace = \"\")");
    }
  }

  @Override
  protected void generateParameterAnnotations(Parameter paramater)
  {
    printer.println("    @WebParam(name = \"" + paramater.getName() + "\", targetNamespace = \"\")");
  }
}
