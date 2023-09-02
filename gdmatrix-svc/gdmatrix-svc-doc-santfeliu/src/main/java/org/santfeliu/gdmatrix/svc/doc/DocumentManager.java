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
package org.santfeliu.gdmatrix.svc.doc;

import jakarta.activation.DataHandler;
import jakarta.enterprise.context.RequestScoped;
import jakarta.mail.util.ByteArrayDataSource;
import java.util.List;
import org.matrix.doc.Content;
import org.matrix.doc.ContentInfo;
import org.matrix.doc.Document;
import org.matrix.doc.DocumentFilter;
import org.matrix.doc.DocumentManagerMetaData;
import org.matrix.doc.DocumentMetaData;

/**
 *
 * @author realor
 */
@RequestScoped
public class DocumentManager implements org.gdmatrix.svc.doc.DocumentManager
{

  @Override
  public Content loadContent(String contentId)
  {
    System.out.println("ContentId: " + contentId);
    Content content = new Content();
    content.setContentId(contentId);
    content.setContentType("text/plain");
    content.setLanguage("en");
    String text = "HELLO WORLD";
    byte[] bytes = text.getBytes();
    DataHandler dh = new DataHandler(
      new ByteArrayDataSource(bytes, "text/plain"));
    content.setData(dh);
    content.setSize((long)bytes.length);

    return content;
  }

  @Override
  public Document loadDocument(String docId, int version, ContentInfo contentInfo)
  {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public boolean removeDocument(String docId, int version)
  {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public boolean removeContent(String contentId)
  {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public DocumentMetaData getDocumentMetaData()
  {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public DocumentManagerMetaData getManagerMetaData()
  {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public Document storeDocument(Document document)
  {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public void lockDocument(String docId, int version)
  {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public void unlockDocument(String docId, int version)
  {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public List<Document> findDocuments(DocumentFilter filter)
  {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public int countDocuments(DocumentFilter filter)
  {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public Content storeContent(Content content)
  {
    return content;
  }

  @Override
  public DataHandler markupContent(String contentId, String searchExpression)
  {
    throw new UnsupportedOperationException("Not supported yet.");
  }

}
