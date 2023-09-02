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

import org.gdmatrix.setup.ManagerSetup;
import jakarta.enterprise.inject.Instance;
import jakarta.enterprise.inject.Produces;
import jakarta.inject.Inject;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author realor
 */
public abstract class AbstractManagerProducer<T>
{
  @Inject
  protected ManagerSetup managerSetup;

  @Inject
  protected Instance<T> managerInstance;

  public abstract String getManagerClassName();

  @Produces
  public T get()
  {
    String className = getManagerClassName();
    if (StringUtils.isBlank(className))
      throw new ServiceException("SERVICE_NOT_AVAILABLE");

    try
    {
      Class<T> managerClass = (Class<T>)Class.forName(className);

      T manager = managerInstance.select(managerClass).get();

      return manager;
    }
    catch (Exception ex)
    {
      throw new RuntimeException(ex);
    }
  }
}
