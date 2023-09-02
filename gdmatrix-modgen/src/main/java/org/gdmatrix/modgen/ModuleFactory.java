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

import org.gdmatrix.modgen.schema.Type;
import org.gdmatrix.modgen.schema.Module;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author realor
 */
public class ModuleFactory
{
  private final HashMap<String, Module> modules = new HashMap<>();
  private final HashMap<String, Type> types = new HashMap<>();
  private final File inputDirectory;

  public ModuleFactory(String dir)
  {
    this.inputDirectory = new File(dir);
  }

  public ModuleFactory(File inputDirectory)
  {
    this.inputDirectory = inputDirectory;
  }

  public File getInputDirectory()
  {
    return inputDirectory;
  }

  public List<String> getModuleNames()
  {
    List<String> moduleNames = new ArrayList<>();
    File[] moduleFiles = inputDirectory.listFiles(
      file -> file.getName().endsWith(".xml"));

    Arrays.sort(moduleFiles, (f1, f2) -> f1.getName().compareTo(f2.getName()));

    for (File file : moduleFiles)
    {
      String filename = file.getName();
      int index = filename.lastIndexOf(".");
      String moduleName = filename.substring(0, index);
      moduleNames.add(moduleName);
    }
    return moduleNames;
  }

  public Module getModule(String moduleName)
  {
    Module module = modules.get(moduleName);
    if (module == null)
    {
      File moduleFile = new File(inputDirectory, moduleName + ".xml");
      if (!moduleFile.exists()) return null;

      try (FileInputStream fis = new FileInputStream(moduleFile))
      {
        ModuleParser parser = new ModuleParser();
        module = parser.parse(fis);
        module.setLocation(moduleFile.toURI().toURL());
        modules.put(moduleName, module);

        // register types;
        for (Type type : module.getTypes())
        {
          types.put(type.getQName(), type);
        }
      }
      catch (Exception ex)
      {
        System.out.println(ex);
      }
    }
    return module;
  }

  public Type getType(String qname)
  {
    Type type = types.get(qname);
    if (type == null)
    {
      int index = qname.indexOf(":");
      String moduleName = qname.substring(0, index);
      Module module = getModule(moduleName);
      type = module == null ? null : types.get(qname);
    }
    return type;
  }

  public static void main(String[] args)
  {
    ModuleFactory factory = new ModuleFactory("/home/realor/gdmatrix/gdmatrix-api/src/main/resources/modules");

    List<String> moduleNames = factory.getModuleNames();
    for (String moduleName : moduleNames)
    {
      Module module = factory.getModule(moduleName);
      //System.out.println(module);
    }
  }
}
