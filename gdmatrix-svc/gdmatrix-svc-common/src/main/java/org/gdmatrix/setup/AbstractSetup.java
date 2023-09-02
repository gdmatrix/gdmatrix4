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
package org.gdmatrix.setup;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.InstanceCreator;
import jakarta.annotation.PostConstruct;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;

/**
 *
 * @author realor
 */
public abstract class AbstractSetup
{
  @PostConstruct
  public void init()
  {
    load();
  }

  public void load()
  {
    File file = getFile();
    if (file.exists())
    {
      Class cls = getClass();
      Gson gson = new GsonBuilder()
        .registerTypeAdapter(cls, (InstanceCreator) (Type type) -> this)
        .create();

      try (FileReader reader = new FileReader(file);)
      {
        gson.fromJson(reader, cls);
      }
      catch (IOException ex)
      {
        // TODO: log
        System.out.println(ex);
      }
    }
  }

  public void save()
  {
    File file = getFile();
    file.mkdirs();
    Gson gson = new GsonBuilder().setPrettyPrinting().create();
    String json = gson.toJson(this);
    try (FileWriter writer = new FileWriter(file))
    {
      writer.write(json);
    }
    catch (IOException ex)
    {
      // TODO: log
      System.out.println(ex);
    }
  }

  protected File getFile()
  {
    String dirname = System.getProperty("gdmatrix.dir");
    if (dirname == null)
    {
      dirname = "/etc/matrix";
    }
    Class cls = getClass();

    return new File(dirname, cls.getName() + ".json");
  }
}
