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

/**
 *
 * @author realor
 */

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import org.matrix.base.ApiError;

/**
 *
 * @author realor
 */
public class ApiExceptionMapper
{
  @Provider
  public static class ServiceExceptionMapper
    implements ExceptionMapper<ServiceException>
  {
    @Override
    public Response toResponse(ServiceException ex)
    {
      ApiError error = new ApiError();

      error.setCode(ex.getCode());
      error.setDetail(ex.getDetail());

      return Response.serverError().entity(error).build();
    }
  }

  @Provider
  public static class DefaultExceptionMapper
    implements ExceptionMapper<Exception>
  {
    @Override
    public Response toResponse(Exception ex)
    {
      ex.printStackTrace();

      ApiError error = new ApiError();

      error.setCode("INTERNAL_ERROR");
      String message = ex.getMessage();
      if (message == null)
      {
        message = ex.toString();
      }
      error.setDetail(message);
      return Response.serverError().entity(error).build();
    }
  }
}
