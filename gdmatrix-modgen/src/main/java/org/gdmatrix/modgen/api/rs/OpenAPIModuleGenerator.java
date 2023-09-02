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
package org.gdmatrix.modgen.api.rs;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.gdmatrix.modgen.ModuleFactory;
import org.gdmatrix.modgen.ModuleGenerator;
import org.gdmatrix.modgen.schema.ComplexType;
import org.gdmatrix.modgen.schema.ComplexTypifiedComponent;
import org.gdmatrix.modgen.schema.Enumeration;
import org.gdmatrix.modgen.schema.EnumerationValue;
import org.gdmatrix.modgen.schema.Identifier;
import org.gdmatrix.modgen.schema.Module;
import org.gdmatrix.modgen.schema.Operation;
import org.gdmatrix.modgen.schema.Parameter;
import org.gdmatrix.modgen.schema.Property;
import org.gdmatrix.modgen.schema.Response;
import org.gdmatrix.modgen.schema.Type;

/**
 *
 * @author realor
 */
public class OpenAPIModuleGenerator extends ModuleGenerator
{
  JsonObject model;
  static final Map<String, TypeInfo> TYPES = new HashMap<>();

  static class TypeInfo
  {
    final String jsonType;
    final String jsonFormat;
    final String resultClass;

    TypeInfo(String jsonType, String jsonFormat, String resultClass)
    {
      this.jsonType = jsonType;
      this.jsonFormat = jsonFormat;
      this.resultClass = resultClass;
    }
  }

  static
  {
    TYPES.put("xs:string", new TypeInfo("string", null, "ResultString"));
    TYPES.put("xs:boolean", new TypeInfo("boolean", null, "ResultBoolean"));
    TYPES.put("xs:int", new TypeInfo("integer", "int32", "ResultInteger"));
    TYPES.put("xs:long", new TypeInfo("integer", "int64", "ResultLong"));
    TYPES.put("xs:float", new TypeInfo("number", "float", "ResultFloat"));
    TYPES.put("xs:double", new TypeInfo("number", "double", "ResultDouble"));
    TYPES.put("xs:base64Binary", new TypeInfo("string", null, "ResultString"));
    TYPES.put("xs:anyType", new TypeInfo("string", null, "ResultString"));
    TYPES.put("mx:date", new TypeInfo("string", null, "ResultString"));
    TYPES.put("mx:time", new TypeInfo("string", null, "ResultString"));
    TYPES.put("mx:dateTime", new TypeInfo("string", null, "ResultString"));
  }

  public OpenAPIModuleGenerator(ModuleFactory moduleFactory)
  {
    super(moduleFactory);
  }

  public void readTemplate(File templateFile) throws IOException
  {
    try (FileReader reader = new FileReader(templateFile))
    {
      model = (JsonObject)JsonParser.parseReader(reader);
    }
  }

  @Override
  public void generateAllModules() throws Exception
  {
    super.generateAllModules();

    // generate result types
    JsonObject jsonSchemas =
      model.getAsJsonObject("components").getAsJsonObject("schemas");

    for (TypeInfo typeInfo : TYPES.values())
    {
      String resultClass = typeInfo.resultClass;

      JsonObject jsonType = jsonSchemas.getAsJsonObject(resultClass);
      if (jsonType == null)
      {
        jsonType = new JsonObject();
        jsonSchemas.add(resultClass, jsonType);
        jsonType.addProperty("type", "object");

        JsonObject jsonProps = new JsonObject();
        jsonType.add("properties", jsonProps);
        JsonObject jsonPropType = new JsonObject();
        jsonProps.add("result", jsonPropType);
        jsonPropType.addProperty("type", typeInfo.jsonType);
        if (typeInfo.jsonFormat != null)
        {
          jsonPropType.addProperty("format", typeInfo.jsonFormat);
        }
      }
    }

    try (PrintWriter writer = getWriter("openapi.json"))
    {
      Gson gson = new GsonBuilder().setPrettyPrinting().create();
      gson.toJson(model, writer);
    }
  }

  @Override
  public void generateModule(Module module) throws Exception
  {
    JsonArray jsonTags = model.getAsJsonArray("tags");
    if (jsonTags == null)
    {
      jsonTags = new JsonArray();
      model.add("tags", jsonTags);
    }

    JsonObject jsonTag = new JsonObject();
    jsonTag.addProperty("name", module.getName());
    jsonTag.addProperty("description", module.getTitle());
    jsonTags.add(jsonTag);

    JsonObject jsonComponents = model.getAsJsonObject("components");
    if (jsonComponents == null)
    {
      jsonComponents = new JsonObject();
      model.add("components", jsonComponents);
    }

    JsonObject jsonSchemas = jsonComponents.getAsJsonObject("schemas");
    if (jsonSchemas == null)
    {
      jsonSchemas = new JsonObject();
      jsonComponents.add("schemas", jsonSchemas);
    }

    List<Type> types = module.getTypes();
    for (Type type : types)
    {
      if (type instanceof ComplexType)
      {
        ComplexType complexType = (ComplexType)type;
        JsonObject jsonType = generateComplexType(complexType);
        jsonSchemas.add(type.getQName(), jsonType);
      }
    }

    JsonObject jsonPaths = model.getAsJsonObject("paths");
    if (jsonPaths == null)
    {
      jsonPaths = new JsonObject();
      model.add("paths", jsonPaths);
    }

    List<Operation> operations = module.getOperations();
    for (Operation operation : operations)
    {
      RestUtils.detectPathAndMethod(operation, moduleFactory);
      JsonObject jsonOper = generateOperation(operation);
      String path = "/" + module.getName() + operation.getAttribute("jaxrs.path");
      String method = operation.getAttribute("jaxrs.method").toLowerCase();

      JsonObject jsonPath = jsonPaths.getAsJsonObject(path);
      if (jsonPath == null)
      {
        jsonPath = new JsonObject();
        jsonPaths.add(path, jsonPath);
      }
      jsonPath.add(method, jsonOper);
    }
  }

  public JsonObject generateOperation(Operation operation)
  {
    JsonObject jsonOper = new JsonObject();
    jsonOper.addProperty("operationId", operation.getName());

    JsonObject jsonBody = null;
    JsonArray jsonParams = new JsonArray();
    List<Parameter> parameters = operation.getParameters();
    for (Parameter parameter : parameters)
    {
      String input = parameter.getAttribute("jaxrs.input");
      if (input == null)
      {
        jsonBody = new JsonObject();
        JsonObject jsonContent = new JsonObject();
        jsonBody.add("content", jsonContent);
        JsonObject jsonJson = new JsonObject();
        jsonContent.add("application/json", jsonJson);
        jsonJson.add("schema", getSchema(parameter));
      }
      else
      {
        JsonObject jsonParam = new JsonObject();
        jsonParam.addProperty("in", input);
        jsonParam.addProperty("name", parameter.getName());
        jsonParam.add("schema", getSchema(parameter));
        jsonParams.add(jsonParam);
      }
    }
    if (!jsonParams.isEmpty())
    {
      jsonOper.add("parameters", jsonParams);
    }
    if (jsonBody != null)
    {
      jsonOper.add("requestBody", jsonBody);
    }
    JsonObject jsonResponses = new JsonObject();
    jsonOper.add("responses", jsonResponses);
    JsonObject jsonDefault = new JsonObject();
    jsonResponses.add("default", jsonDefault);
    JsonObject jsonContent = new JsonObject();
    jsonDefault.add("content", jsonContent);
    JsonObject jsonJson = new JsonObject();
    jsonContent.add("application/json", jsonJson);
    if (operation.getResponse() != null)
    {
      jsonJson.add("schema", getSchema(operation.getResponse()));
    }

    jsonOper.addProperty("summary", operation.getName());
    JsonArray jsonTags = new JsonArray();
    jsonTags.add(operation.getModule().getName());
    jsonOper.add("tags", jsonTags);

    return jsonOper;
  }

  public JsonObject generateComplexType(ComplexType complexType)
  {
    JsonObject jsonType = new JsonObject();
    jsonType.addProperty("type", "object");

    JsonObject jsonProps = new JsonObject();
    jsonType.add("properties", jsonProps);

    Identifier identifier = complexType.getIdentifier();
    if (identifier != null)
    {
      JsonObject propType = new JsonObject();
      jsonProps.add(identifier.getName(), propType);
      propType.addProperty("type", "string");
    }
    for (Property property : complexType.getProperties())
    {
      JsonObject propType = getSchema(property);
      jsonProps.add(property.getName(), propType);
    }
    return jsonType;
  }

  public JsonObject getSchema(ComplexTypifiedComponent component)
  {
    JsonObject jsonCompType = new JsonObject();

    if (component.isMultiValued())
    {
      jsonCompType.addProperty("type", "array");
      JsonObject jsonItems = new JsonObject();
      jsonCompType.add("items", jsonItems);
      jsonCompType = jsonItems;
    }

    boolean isResponse = component instanceof Response;

    String typeName = component.getType();
    Type refType = moduleFactory.getType(typeName);
    if (refType instanceof Enumeration)
    {
      Enumeration enumeration = (Enumeration) refType;
      jsonCompType.addProperty("type", "string");
      List<EnumerationValue> values = enumeration.getValues();
      JsonArray jsonArray = new JsonArray();
      jsonCompType.add("enum", jsonArray);
      for (EnumerationValue value : values)
      {
        jsonArray.add(value.getName());
      }
    }
    else
    {
      TypeInfo typeInfo = TYPES.get(typeName);
      if (typeInfo == null)
      {
        jsonCompType.addProperty("$ref", "#/components/schemas/" + typeName);
      }
      else
      {
        if (isResponse)
        {
          jsonCompType.addProperty("$ref", "#/components/schemas/" + typeInfo.resultClass);
        }
        else
        {
          jsonCompType.addProperty("type", typeInfo.jsonType);
          if (typeInfo.jsonFormat != null)
          {
            jsonCompType.addProperty("format", typeInfo.jsonFormat);
          }
        }
      }
    }
    return jsonCompType;
  }

  public static void main(String[] args) throws Exception
  {
    if (args.length < 3)
    {
      System.out.println("Usage: java " +
        OpenAPIModuleGenerator.class.getCanonicalName() +
        " <module_dir> <template_file> <output_dir>");
    }
    else
    {
      ModuleFactory moduleFactory = new ModuleFactory(args[0]);
      OpenAPIModuleGenerator gen = new OpenAPIModuleGenerator(moduleFactory);
      gen.readTemplate(new File(args[1]));
      gen.setOutputDirectory(new File(args[2]));
      gen.generateAllModules();
    }
  }

}
