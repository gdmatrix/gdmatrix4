package org.gdmatrix.modgen;

import org.gdmatrix.modgen.schema.Response;
import org.gdmatrix.modgen.schema.Parameter;
import org.gdmatrix.modgen.schema.Operation;
import org.gdmatrix.modgen.schema.Module;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

/**
 *
 * @author realor
 */
public abstract class AbstractManagerGenerator
{
  protected JavaModuleGenerator moduleGenerator;
  protected Module module;
  protected PrintWriter printer;

  public AbstractManagerGenerator(JavaModuleGenerator moduleGenerator)
  {
    this.moduleGenerator = moduleGenerator;
  }

  public abstract boolean isInterface();

  public boolean isAddParameterAnnotations()
  {
    return false;
  }

  public void generate(Module module) throws IOException
  {
    this.module = module;

    String packageName = getPackageName();
    String className = getClassName();
    String path = packageName + "." + className;

    path = path.replace('.', '/');

    try (PrintWriter writer = moduleGenerator.getWriter(path + ".java"))
    {
      this.printer = writer;

      String pkg = getPackageName();
      printer.println("package " + pkg + ";");
      printer.println();

      generateImports();
      generateClassAnnotations();
      generateClassSignature();
      printer.println();
      printer.println("{");
      if (!isInterface())
      {
        generateMembers();
      }
      List<Operation> operations = module.getOperations();
      for (Operation operation : operations)
      {
        if (!isOperationIgnored(operation))
        {
          generateOperation(operation);
        }
      }
      printer.println("}");
    }
  }

  protected abstract String getPackageName();

  protected abstract String getClassName();

  protected void generateImports()
  {
  }

  protected void generateClassAnnotations()
  {
  }

  protected void generateClassSignature()
  {
    String className = getClassName();
    if (isInterface())
    {
      printer.print("public interface " + className);
    }
    else
    {
      printer.print("public class " + className);
    }
  }

  protected void generateOperation(Operation operation)
  {
    String operationName = operation.getName();
    Response response = operation.getResponse();
    List<Parameter> parameters = operation.getParameters();

    generateOperationAnnotations(operation);

    String javaRespTypeName;

    if (response != null)
    {
      javaRespTypeName = getResponseTypeName(response);
    }
    else
    {
      javaRespTypeName = "void";
    }

    printer.print("  public " + javaRespTypeName + " " + operationName + "(");
    if (isAddParameterAnnotations())
    {
      if (!parameters.isEmpty())
      {
        printer.println();
      }
      for (int i = 0; i < parameters.size(); i++)
      {
        Parameter parameter = parameters.get(i);
        String javaParamTypeName = moduleGenerator.getJavaTypeName(parameter);
        String javaParamName = moduleGenerator.getJavaName(parameter);

        generateParameterAnnotations(parameter);
        printer.print("    ");
        printer.print(javaParamTypeName + " " + javaParamName);
        if (i < parameters.size() - 1)
        {
          printer.println(",");
        }
      }
      printer.print(")");
    }
    else
    {
      for (int i = 0; i < parameters.size(); i++)
      {
        if (i > 0)
        {
          printer.print(", ");
        }
        Parameter parameter = parameters.get(i);
        String javaParamTypeName = moduleGenerator.getJavaTypeName(parameter);
        String javaParamName = moduleGenerator.getJavaName(parameter);

        printer.print(javaParamTypeName + " " + javaParamName);
      }
      printer.print(")");
    }
    if (isInterface())
    {
      printer.println(";");
      printer.println();
    }
    else
    {
      printer.println();
      printer.println("  {");
      generateOperationCode(operation);
      printer.println("  }");
      printer.println();
    }
  }

  protected boolean isOperationIgnored(Operation operation)
  {
    return false;
  }

  protected String getResponseTypeName(Response response)
  {
    return moduleGenerator.getJavaTypeName(response);
  }

  protected void generateMembers()
  {
  }

  protected void generateOperationAnnotations(Operation operation)
  {
  }

  protected void generateParameterAnnotations(Parameter paramater)
  {
  }

  protected void generateOperationCode(Operation operation)
  {
  }
}
