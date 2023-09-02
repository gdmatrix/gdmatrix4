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
package org.gdmatrix.modgen;

import org.gdmatrix.modgen.schema.Module;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

/**
 *
 * @author realor
 */
public abstract class ModuleGenerator
{
  protected ModuleFactory moduleFactory;
  protected File outputDirectory;

  public ModuleGenerator(ModuleFactory moduleFactory)
  {
    this.moduleFactory = moduleFactory;
  }

  public ModuleFactory getModuleFactory()
  {
    return moduleFactory;
  }

  public File getOutputDirectory()
  {
    return outputDirectory;
  }

  public void setOutputDirectory(File outputDirectory)
  {
    this.outputDirectory = outputDirectory;
  }

  public void generateAllModules() throws Exception
  {
    List<String> moduleNames = moduleFactory.getModuleNames();
    for (String moduleName : moduleNames)
    {
      generateModule(moduleName);
    }
  }

  public void generateModule(String moduleName) throws Exception
  {
    generateModule(moduleFactory.getModule(moduleName));
  }

  public abstract void generateModule(Module module) throws Exception;

  protected PrintWriter getWriter(String relativePath) throws IOException
  {
    File file = new File(outputDirectory, relativePath);
    file.getParentFile().mkdirs();
    return new PrintWriter(file, "UTF-8");
  }
}
