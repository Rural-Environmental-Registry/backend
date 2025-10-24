package tools;

import net.sf.jasperreports.engine.JasperCompileManager;

import java.io.File;

public class ReportCompiler {
  public static void main(String[] args) throws Exception {
    File inputDir = new File(args[0]);
    File outputDir = new File(args[1]);
    outputDir.mkdirs();

    File[] jrxmlFiles = inputDir.listFiles((dir, name) -> name.endsWith(".jrxml"));
    if (jrxmlFiles != null) {
      for (File jrxml : jrxmlFiles) {
        File jasper = new File(outputDir, jrxml.getName().replace(".jrxml", ".jasper"));
        System.out.println("Compiling: " + jrxml.getName());
        JasperCompileManager.compileReportToFile(jrxml.getAbsolutePath(), jasper.getAbsolutePath());
      }
    }
  }
}
