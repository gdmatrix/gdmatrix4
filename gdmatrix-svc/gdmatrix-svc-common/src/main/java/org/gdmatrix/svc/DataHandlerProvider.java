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

import jakarta.activation.DataHandler;
import jakarta.mail.util.ByteArrayDataSource;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.MediaType;
import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;
import jakarta.ws.rs.core.MultivaluedMap;
import jakarta.ws.rs.ext.MessageBodyReader;
import jakarta.ws.rs.ext.MessageBodyWriter;
import jakarta.ws.rs.ext.Provider;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import org.gdmatrix.svc.doc.Pojo;

/**
 *
 * @author realor
 */
@Provider
@Consumes(APPLICATION_JSON)
@Produces(APPLICATION_JSON)
public class DataHandlerProvider
  //implements MessageBodyWriter<Pojo>, MessageBodyReader<Pojo>
{
//  @Override
//  public boolean isWriteable(Class<?> type, Type genericType,
//    Annotation[] annotations, MediaType mediaType)
//  {
//    return true;
//  }
//
//  @Override
//  public void writeTo(Pojo dataHandler, Class<?> genericType, Type type,
//    Annotation[] annotations, MediaType mediaType,
//    MultivaluedMap<String, Object> properties, OutputStream outputStream)
//    throws IOException, WebApplicationException
//  {
//    PrintWriter writer = new PrintWriter(outputStream);
//    String s = "{ \"bytes\" : \"ab865875bv\", \"contentType\": \"text/plain\" }";
//    System.out.println("write Pojo " + dataHandler + " " + s);
//
//    writer.print(s);
//    writer.close();
//  }
//
//  @Override
//  public boolean isReadable(Class<?> type, Type type1, Annotation[] antns, MediaType mt)
//  {
//    return true;
//  }
//
//  @Override
//  public Pojo readFrom(Class<Pojo> type, Type genericType,
//    Annotation[] annotations, MediaType mediaType,
//    MultivaluedMap<String, String> properties, InputStream inputStream)
//    throws IOException, WebApplicationException
//  {
//    String text = "HELLO WORLD";
//    byte[] bytes = text.getBytes();
//    DataHandler dh = new DataHandler(
//      new ByteArrayDataSource(bytes, "text/plain"));
//    Pojo pojo = new Pojo();
//    pojo.setName("Abc");
//    pojo.setData(dh);
//    return pojo;
//  }

}
