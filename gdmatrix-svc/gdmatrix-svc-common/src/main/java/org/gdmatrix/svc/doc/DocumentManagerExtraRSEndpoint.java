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
package org.gdmatrix.svc.doc;

import jakarta.activation.DataHandler;
import jakarta.inject.Singleton;
import jakarta.mail.util.ByteArrayDataSource;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;

/**
 *
 * @author realor
 */

@Path("doc/ext/")
@Singleton
public class DocumentManagerExtraRSEndpoint
{
  @GET
  @Path("/Document/{docId}")
  @Consumes(APPLICATION_JSON)
  @Produces(APPLICATION_JSON)
  public Pojo getDocument(@PathParam("docId") String docId)
  {
    Pojo pojo = new Pojo();
    pojo.setName("Sample");
    pojo.setData("HOLA".getBytes());
    return pojo;
  }

  @GET
  @Path("/bytes")
  @Consumes(APPLICATION_JSON)
  @Produces(APPLICATION_JSON)
  public byte[] getBytes()
  {
    return "HOLA MUNDO".getBytes();
  }

  @GET
  @Path("/data")
  @Consumes(APPLICATION_JSON)
  @Produces(APPLICATION_JSON)
  public Pojo2 getData()
  {
    String text = "HELLO WORLD";
    byte[] bytes = text.getBytes();
    DataHandler dh = new DataHandler(
      new ByteArrayDataSource(bytes, "text/plain"));
    Pojo2 pojo2 = new Pojo2();
    pojo2.setName("Abc");
    pojo2.setData(dh);

    return pojo2;
  }
}
