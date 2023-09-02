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

import org.gdmatrix.modgen.api.ws.WSEndpointGenerator;
import org.gdmatrix.modgen.api.rs.RSEndpointGenerator;
import org.gdmatrix.modgen.api.rs.ManagerRestGenerator;
import org.gdmatrix.modgen.api.ws.ManagerPortGenerator;
import org.gdmatrix.modgen.schema.Type;
import org.gdmatrix.modgen.schema.Property;
import org.gdmatrix.modgen.schema.NamedComponent;
import org.gdmatrix.modgen.schema.Module;
import org.gdmatrix.modgen.schema.Identifier;
import org.gdmatrix.modgen.schema.EnumerationValue;
import org.gdmatrix.modgen.schema.Enumeration;
import org.gdmatrix.modgen.schema.ComplexTypifiedComponent;
import org.gdmatrix.modgen.schema.ComplexType;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *
 * @author realor
 */
public class JavaModuleGenerator extends ModuleGenerator
{
  public static final String API_MODE = "api";
  public static final String SVC_MODE = "svc";

  String generationMode = API_MODE; // api | svc
  Set<String> enabledApis = new HashSet<>();

  public JavaModuleGenerator(ModuleFactory moduleFactory)
  {
    super(moduleFactory);
  }

  public String getGenerationMode()
  {
    return generationMode;
  }

  public void setGenerationMode(String generationMode)
  {
    this.generationMode = generationMode;
  }

  public Set<String> getEnabledApis()
  {
    return enabledApis;
  }

  @Override
  public void generateModule(Module module) throws IOException
  {
    if (API_MODE.equals(generationMode))
    {
      List<Type> types = module.getTypes();
      for (Type type : types)
      {
        generateType(type);
      }

      if (enabledApis.contains("ws"))
      {
        ManagerPortGenerator managerPortGenerator = new ManagerPortGenerator(this);
        managerPortGenerator.generate(module);
      }

      if (enabledApis.contains("rs"))
      {
        ManagerRestGenerator managerRestGenerator = new ManagerRestGenerator(this);
        managerRestGenerator.generate(module);
      }
    }
    else if (SVC_MODE.equals(generationMode))
    {
      ManagerGenerator managerGenerator = new ManagerGenerator(this);
      managerGenerator.generate(module);

      generateManagerProducer(module);

      if (enabledApis.contains("ws"))
      {
        WSEndpointGenerator wsEndpointGenerator = new WSEndpointGenerator(this);
        wsEndpointGenerator.generate(module);
      }

      if (enabledApis.contains("rs"))
      {
        RSEndpointGenerator rsEndpointGenerator = new RSEndpointGenerator(this);
        rsEndpointGenerator.generate(module);
      }
    }
  }

  public void generateManagerProducer(Module module) throws IOException
  {
    String path = "org/gdmatrix/svc/" + module.getName() + "/" +
      module.getManager() + "Producer.java";

    try (PrintWriter printer = getWriter(path))
    {
      printer.println("package org.gdmatrix.svc." + module.getName() + ";");

      printer.println("import jakarta.enterprise.context.ApplicationScoped;");
      printer.println("import org.gdmatrix.svc.AbstractManagerProducer;");
      printer.println();

      printer.println("@ApplicationScoped");
      printer.println("public class " + module.getManager() + "Producer " +
        "extends AbstractManagerProducer<" + module.getManager() + ">");
      printer.println("{");
      printer.println("  @Override");
      printer.println("  public String getManagerClassName()");
      printer.println("  {");
      printer.println("    return managerSetup.get" + module.getManager() + "Impl();");
      printer.println("  }");
      printer.println("}");
    }
  }

  public void generateType(Type type) throws IOException
  {
    if (type instanceof ComplexType)
    {
      generateComplexType((ComplexType)type);
    }
    else if (type instanceof Enumeration)
    {
      generateEnumeration((Enumeration)type);
    }
  }

  public void generateComplexType(ComplexType type) throws IOException
  {
    String className = type.getJavaClassName();
    String path = className.replace('.', '/');

    int index = className.lastIndexOf(".");
    String pkg = className.substring(0, index);
    className = className.substring(index + 1);

    try (PrintWriter printer = getWriter(path + ".java"))
    {
      printer.println("package " + pkg + ";");
      printer.println();

      ComplexType complexType = (ComplexType)type;
      printer.println("import java.util.List;");
      printer.println("import java.io.Serializable;");
      printer.println("import jakarta.xml.bind.annotation.*;");
      printer.println("import com.fasterxml.jackson.annotation.*;");
      printer.println("import com.fasterxml.jackson.databind.annotation.*;");
      printer.println("import org.gdmatrix.databind.*;");

      printer.println();

      printer.println("@XmlAccessorType(XmlAccessType.FIELD)");
      printer.println("@XmlType(name = \"" + type.getName() + "\", propOrder = {");
      Identifier identifier = complexType.getIdentifier();
      if (identifier != null)
      {
        printer.println("  \"" + getJavaName(identifier) + "\",");
      }
      List<Property> properties = complexType.getProperties();
      for (int i = 0; i < properties.size(); i++)
      {
        Property property = properties.get(i);
        printer.print("  \"" + getJavaName(property) + "\"");
        if (i < properties.size() - 1) printer.println(",");
        else printer.println();
      }
      printer.println("})");

      printer.print("public class " + className);
      String extendsTypeName = type.getExtendsType();
      if (extendsTypeName != null)
      {
        String javaExtendsTypeName;
        Type extendsType = moduleFactory.getType(extendsTypeName);
        if (extendsType == null)
        {
          javaExtendsTypeName = getSimpleJavaTypeName(extendsTypeName, false);
        }
        else
        {
          javaExtendsTypeName = extendsType.getJavaClassName();
        }
        printer.print(" extends " + javaExtendsTypeName);
      }
      printer.println();
      printer.println("  implements Serializable");
      printer.println("{");

      if (identifier != null)
      {
        String name = getJavaName(identifier);
        printer.println("  protected String " + name + "; // identifier");
      }
      for (Property property : properties)
      {
        generateProperty(property, printer);
      }
      printer.println();

      if (identifier != null)
      {
        generateGetterAndSetter(identifier, printer);
      }
      for (Property property : properties)
      {
        generateGetterAndSetter(property, printer);
      }
      printer.println("}");
      printer.println();
    }
  }

  public void generateEnumeration(Enumeration enumeration) throws IOException
  {
    String className = enumeration.getJavaClassName();
    String path = className.replace('.', '/');

    int index = className.lastIndexOf(".");
    String pkg = className.substring(0, index);
    className = className.substring(index + 1);

    try (PrintWriter printer = getWriter(path + ".java"))
    {
      printer.println("package " + pkg + ";");
      printer.println();

      printer.println("import jakarta.xml.bind.annotation.XmlEnum;");
      printer.println("import jakarta.xml.bind.annotation.XmlType;");
      printer.println();

      printer.println("@XmlType(name = \"" + className + "\")");
      printer.println("@XmlEnum");
      printer.println("public enum " + className);
      printer.println("{");
      List<EnumerationValue> values = enumeration.getValues();
      for (int i = 0; i < values.size(); i++)
      {
        EnumerationValue value = values.get(i);
        printer.print("  " + value.getName());
        if (i < values.size() - 1) printer.println(",");
        else printer.println(";\n");
      }
      printer.println("  public String value()");
      printer.println("  {");
      printer.println("     return name();");
      printer.println("  }");
      printer.println();

      printer.println("  public static " + className + " fromValue(String v)");
      printer.println("  {");
      printer.println("     return valueOf(v);");
      printer.println("  }");

      printer.println("}");
    }
  }

  public void generateProperty(Property property, PrintWriter printer)
  {
    if ("true".equals(property.getAttribute("jaxws.isAttribute")))
    {
      Type type = moduleFactory.getType(property.getType());
      if (type instanceof Enumeration)
      {
        printer.println("  @XmlSchemaType(name = \"string\")");
      }

      String javaName = getJavaName(property);
      String javaTypeName = getSimpleJavaTypeName(property.getType(), false);

      printer.println("  @XmlAttribute(name = \"" + property.getName() +
        "\", required = true)");
      printer.println("  protected " + javaTypeName + " " + javaName + ";");
    }
    else
    {
      if (property.isNillable())
      {
        printer.println("  @XmlElement(nillable = true)");
      }
      Type propType = moduleFactory.getType(property.getType());
      if (propType instanceof Enumeration)
      {
        printer.println("  @XmlSchemaType(name = \"string\")");
      }
      String javaName = getJavaName(property);
      String javaTypeName = getComplexJavaTypeName(property);

      String contentTypes = property.getExpectedContentTypes();
      if (contentTypes != null)
      {
        printer.println("  @XmlMimeType(\"" + contentTypes + "\")");
      }
      if ("jakarta.activation.DataHandler".equals(javaTypeName))
      {
        printer.println("  @JsonSerialize(using = DataHandlerSerializer.class)");
        printer.println("  @JsonDeserialize(using = DataHandlerDeserializer.class)");
      }

      if (!javaName.equals(property.getName()))
      {
        printer.println("  @XmlElement(name = \"" + property.getName() + "\")");
      }

      printer.println("  protected " + javaTypeName + " " + javaName + ";");
    }
  }

  public void generateGetterAndSetter(NamedComponent component,
    PrintWriter printer)
  {
    String name = component.getName();
    String upperName = name.substring(0, 1).toUpperCase() + name.substring(1);
    if ("Class".equals(upperName)) upperName = "Clazz";

    String javaName = getJavaName(component);
    String javaType = getJavaTypeName(component);

    String verb = "boolean".equalsIgnoreCase(javaType) ? "is" : "get";

    printer.println("  public " + javaType + " " + verb + upperName + "()");
    printer.println("  {");
    printer.println("    return " + javaName + ";");
    printer.println("  }");
    printer.println();

    printer.print("  public void set" + upperName + "(");
    printer.println(javaType + " " + javaName + ")");
    printer.println("  {");
    printer.println("    this." + javaName + " = " + javaName + ";");
    printer.println("  }");
    printer.println();
  }

  public String getJavaName(NamedComponent component)
  {
    String name = component.getName();
    if ("case".equals(name) || "public".equals(name) || "new".equals(name))
    {
      name = "_" + name;
    }
    else if ("class".equals(name))
    {
      name = "clazz";
    }
    else if ("NIF".equals(name))
    {
      name = "nif";
    }
    else if ("CIF".equals(name))
    {
      name = "cif";
    }
    return name;
  }

  public String getJavaTypeName(NamedComponent component)
  {
    String javaTypeName;
    if (component instanceof ComplexTypifiedComponent)
    {
      javaTypeName = getComplexJavaTypeName((ComplexTypifiedComponent)component);
    }
    else // Identifier
    {
      javaTypeName = "String";
    }
    return javaTypeName;
  }

  public String getComplexJavaTypeName(ComplexTypifiedComponent component)
  {
    String javaTypeName;
    String javaItemTypeName;

    String typeName = component.getType();
    Type type = moduleFactory.getType(typeName);

    if (type != null)
    {
      Module module = component.getModule();
      String pkg = module.getJavaPackage();
      javaItemTypeName = type.getJavaClassName();
      if (javaItemTypeName.startsWith(pkg)
          && !javaItemTypeName.endsWith(".Class"))
      {
        javaItemTypeName = javaItemTypeName.substring(pkg.length() + 1);
      }
    }
    else
    {
      if ("xs:base64Binary".equals(typeName))
      {
        if (component.getExpectedContentTypes() != null)
        {
          javaItemTypeName = "jakarta.activation.DataHandler";
        }
        else
        {
          javaItemTypeName = "byte[]";
        }
      }
      else
      {
        String minOccurs = component.getMinOccurs();
        boolean nullable = "0".equals(minOccurs);
        javaItemTypeName = getSimpleJavaTypeName(typeName, nullable);
      }
    }

    if (component.isMultiValued())
    {
      javaTypeName = "List<" + javaItemTypeName + ">";
    }
    else
    {
      javaTypeName = javaItemTypeName;
    }

    return javaTypeName;
  }

  public String getSimpleJavaTypeName(String typeName, boolean nullable)
  {
    String javaTypeName;
    if (null == typeName)
    {
      javaTypeName = "Object";
    }
    else switch (typeName)
    {
      case "xs:string":
        javaTypeName = "String";
        break;
      case "xs:boolean":
        javaTypeName = nullable ? "Boolean" : "boolean";
        break;
      case "xs:int":
        javaTypeName = nullable ? "Integer" : "int";
        break;
      case "xs:long":
        javaTypeName = nullable ? "Long" : "long";
        break;
      case "xs:float":
        javaTypeName = nullable ? "Float" : "float";
        break;
      case "xs:double":
        javaTypeName = nullable ? "Double" : "double";
        break;
      case "mx:date":
        javaTypeName = "String";
        break;
      case "mx:time":
        javaTypeName = "String";
        break;
      case "mx:dateTime":
        javaTypeName = "String";
        break;
      case "mx:ManagerMetaData":
        javaTypeName = "org.matrix.base.ManagerMetaData";
        break;
      default:
        javaTypeName = "Object";
        break;
    }
    return javaTypeName;
  }

  public static void main(String[] args) throws Exception
  {
    if (args.length < 3)
    {
      System.out.println("Usage: java " +
        JavaModuleGenerator.class.getCanonicalName() +
        " <module_dir> <output_dir> (api|svc) [ws],[rs]");
    }
    else
    {
      ModuleFactory moduleFactory = new ModuleFactory(args[0]);
      JavaModuleGenerator gen = new JavaModuleGenerator(moduleFactory);
      gen.setOutputDirectory(new File(args[1]));
      gen.setGenerationMode(args[2]);
      if (args.length > 3)
      {
        String[] apis = args[3].split(",");
        for (String api : apis)
        {
          gen.getEnabledApis().add(api);
        }
      }
      gen.generateAllModules();
    }
  }

}
