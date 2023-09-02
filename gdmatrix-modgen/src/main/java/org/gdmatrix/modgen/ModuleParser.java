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

import org.gdmatrix.modgen.schema.Struct;
import org.gdmatrix.modgen.schema.Response;
import org.gdmatrix.modgen.schema.Property;
import org.gdmatrix.modgen.schema.Parameter;
import org.gdmatrix.modgen.schema.Operation;
import org.gdmatrix.modgen.schema.NamedComponent;
import org.gdmatrix.modgen.schema.Module;
import org.gdmatrix.modgen.schema.Import;
import org.gdmatrix.modgen.schema.Identifier;
import org.gdmatrix.modgen.schema.Error;
import org.gdmatrix.modgen.schema.EnumerationValue;
import org.gdmatrix.modgen.schema.Enumeration;
import org.gdmatrix.modgen.schema.Entity;
import org.gdmatrix.modgen.schema.Documentation;
import org.gdmatrix.modgen.schema.ComplexTypifiedComponent;
import org.gdmatrix.modgen.schema.ComplexType;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 * @author realor
 */
public class ModuleParser
{
  public Module parse(InputStream is) throws IOException
  {
    try
    {
      Module module = new Module();
      DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
      DocumentBuilder builder = factory.newDocumentBuilder();
      Document document = builder.parse(is);
      Node node = document.getFirstChild();
      Element element = nextElement(node);
      if (element != null && "module".equals(element.getNodeName()))
      {
        parseNamedComponent(element, module);
        module.setTitle(getAttribute(element, "title"));
        module.setNamespace(getAttribute(element, "namespace"));
        module.setManager(getAttribute(element, "manager"));
        module.setVersion(getAttribute(element, "version"));
        module.setAuthors(getAttribute(element, "authors"));
        module.setStatus(getAttribute(element, "status"));
        node = node.getFirstChild();
        element = nextElement(node);
        do
        {
          if (null != element.getNodeName())
          {
            switch (element.getNodeName())
            {
              case "imports":
                parseImports(element, module);
                break;
              case "types":
                parseTypes(element, module);
                break;
              case "operations":
                parseOperations(element, module);
                break;
              default:
                break;
            }
          }
          node = element.getNextSibling();
          element = nextElement(node);
        } while (element != null);
      }
      return module;
    }
    catch (Exception ex)
    {
      throw new IOException(ex);
    }
  }

  // private methods

  private Element nextElement(Node node)
  {
    Element element = null;
    boolean found = false;
    while (node != null && !found)
    {
      if (node instanceof Element)
      {
        element = (Element)node;
        found = true;
      }
      else node = node.getNextSibling();
    }
    return element;
  }

  private void parseImports(Element element, Module module)
  {
    Node node = element.getFirstChild();
    element = nextElement(node);
    while (element != null)
    {
      if ("import".equals(element.getNodeName()))
      {
        Import _import = new Import();
        _import.setParent(module);
        _import.setModuleName(getAttribute(element, "module"));
        _import.setPrefix(getAttribute(element, "prefix"));
        _import.setNamespace(getAttribute(element, "namespace"));
        _import.setLocation(getAttribute(element, "location"));
        _import.setVirtual(Boolean.parseBoolean(getAttribute(element, "virtual")));
        module.getImports().add(_import);
      }
      node = element.getNextSibling();
      element = nextElement(node);
    }
  }

  private void parseTypes(Element element, Module module)
  {
    Node node = element.getFirstChild();
    element = nextElement(node);
    while (element != null)
    {
      if ("entity".equals(element.getNodeName()))
      {
        Entity entity = new Entity();
        entity.setParent(module);
        parseComplexType(element, entity);
        module.getTypes().add(entity);
      }
      else if ("struct".equals(element.getNodeName()))
      {
        Struct struct = new Struct();
        struct.setParent(module);
        parseComplexType(element, struct);
        module.getTypes().add(struct);
      }
      else if ("enumeration".equals(element.getNodeName()))
      {
        Enumeration enumeration = new Enumeration();
        enumeration.setParent(module);
        parseEnumeration(element, enumeration);
        module.getTypes().add(enumeration);
      }
      node = element.getNextSibling();
      element = nextElement(node);
    }
  }

  private void parseOperations(Element element, Module module)
  {
    Node node = element.getFirstChild();
    element = nextElement(node);
    while (element != null)
    {
      if ("operation".equals(element.getNodeName()))
      {
        Operation operation = new Operation();
        operation.setParent(module);
        parseOperation(element, operation);
        module.getOperations().add(operation);
      }
      node = element.getNextSibling();
      element = nextElement(node);
    }
  }

  private void parseComplexType(Element element, ComplexType complexType)
  {
    parseNamedComponent(element, complexType);
    complexType.setExtendsType(getAttribute(element, "extends"));
    Node node = element.getFirstChild();
    element = nextElement(node);
    while (element != null)
    {
      if (null != element.getNodeName())
      {
        switch (element.getNodeName())
        {
          case "identifier":
            Identifier identifier = new Identifier();
            identifier.setParent(complexType);
            parseIdentifier(element, identifier);
            complexType.setIdentifier(identifier);
            break;
          case "property":
            Property property = new Property();
            property.setParent(complexType);
            parseProperty(element, property);
            complexType.getProperties().add(property);
            break;
          default:
            break;
        }
      }
      node = element.getNextSibling();
      element = nextElement(node);
    }
  }

  private void parseEnumeration(Element element, Enumeration enumeration)
  {
    parseNamedComponent(element, enumeration);
    enumeration.setExtendsType(getAttribute(element, "extends"));
    Node node = element.getFirstChild();
    element = nextElement(node);
    while (element != null)
    {
      if ("value".equals(element.getNodeName()))
      {
        EnumerationValue value = new EnumerationValue();
        value.setParent(enumeration);
        parseEnumerationValue(element, value);
        enumeration.getValues().add(value);
      }
      node = element.getNextSibling();
      element = nextElement(node);
    }
  }

  private void parseOperation(Element element, Operation operation)
  {
    parseNamedComponent(element, operation);
    Node node = element.getFirstChild();
    element = nextElement(node);
    while (element != null)
    {
      if (null != element.getNodeName())
      {
        switch (element.getNodeName())
        {
          case "parameter":
            Parameter parameter = new Parameter();
            parameter.setParent(operation);
            parseParameter(element, parameter);
            operation.getParameters().add(parameter);
            break;
          case "response":
            Response response = new Response();
            response.setParent(operation);
            parseResponse(element, response);
            operation.setResponse(response);
            break;
          case "error":
            Error error = new Error();
            error.setParent(operation);
            parseError(element, error);
            operation.getErrors().add(error);
            break;
          default:
            break;
        }
      }
      node = element.getNextSibling();
      element = nextElement(node);
    }
  }

  private void parseIdentifier(Element element, Identifier identifier)
  {
    parseNamedComponent(element, identifier);
  }

  private void parseProperty(Element element, Property property)
  {
    parseComplexTypifiedComponent(element, property);
  }

  private void parseParameter(Element element, Parameter parameter)
  {
    parseComplexTypifiedComponent(element, parameter);
  }

  private void parseResponse(Element element, Response response)
  {
    parseComplexTypifiedComponent(element, response);
  }

  private void parseEnumerationValue(Element element, EnumerationValue value)
  {
    parseNamedComponent(element, value);
  }

  private void parseAttribute(Element element, NamedComponent namedComponent)
  {
    String name = element.getAttribute("name");
    String value = element.getAttribute("value");
    namedComponent.setAttribute(name, value);
  }

  private void parseComplexTypifiedComponent(Element element,
    ComplexTypifiedComponent component)
  {
    parseNamedComponent(element, component);
    component.setType(getAttribute(element, "type"));
    component.setMinOccurs(getAttribute(element, "minOccurs"));
    component.setMaxOccurs(getAttribute(element, "maxOccurs"));
    component.setReferences(getAttribute(element, "references"));
    component.setNillable(Boolean.parseBoolean(getAttribute(element, "nillable")));
    component.setReadOnly(Boolean.parseBoolean(getAttribute(element, "readOnly")));
    component.setExpectedContentTypes(getAttribute(element, "expectedContentTypes"));
  }

  private void parseError(Element element, Error error)
  {
    parseNamedComponent(element, error);
    error.setMessage(getAttribute(element, "message"));
  }

  private void parseNamedComponent(Element element, NamedComponent component)
  {
    component.setName(getAttribute(element, "name"));
    Node node = element.getFirstChild();
    element = nextElement(node);
    while (element != null)
    {
      if ("attribute".equals(element.getNodeName()))
      {
        parseAttribute(element, component);
      }
      else if ("documentation".equals(element.getNodeName()))
      {
        Documentation documentation = new Documentation();
        documentation.setParent(component);
        parseDocumentation(element, documentation);
        component.getDocumentations().add(documentation);
      }
      node = element.getNextSibling();
      element = nextElement(node);
    }
  }

  private void parseDocumentation(Element element, Documentation documentation)
  {
    documentation.setSource(getAttribute(element, "source"));
    documentation.setLanguage(getAttribute(element, "language"));
    documentation.setText(elementToString(element));
  }

  private String getAttribute(Element element, String name)
  {
    if (element.hasAttribute(name))
    {
      return element.getAttribute(name);
    }
    return null;
  }

  private String elementToString(Element element)
  {
    try
    {
      TransformerFactory transFactory = TransformerFactory.newInstance();
      Transformer transformer = transFactory.newTransformer();
      StringWriter writer = new StringWriter();
      StreamResult stream = new StreamResult(writer);
      transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
      NodeList list = element.getChildNodes();
      for (int i = 0; i < list.getLength(); i++)
      {
        transformer.transform(new DOMSource(list.item(i)), stream);
      }
      writer.close();
      return writer.toString();
    }
    catch (Exception ex)
    {
      return element.toString();
    }
  }
}
