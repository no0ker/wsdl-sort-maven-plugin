package ru.orcsoft.maven.plugins;

import org.apache.commons.lang3.StringUtils;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import java.io.File;
import java.io.FileFilter;
import java.text.MessageFormat;

@Mojo(name = "sort")
public class Main extends AbstractMojo {

    @Parameter
    private String wsdlPath;

    public void execute() throws MojoExecutionException {
        File[] wsdlFiles = validateWsdlPath();
        getLog().info("wsdl-sort-maven-plugin starts...");
        for (File file : wsdlFiles) {
            try {
                getLog().info("start processing for file " + file.getName());
                new XmlSort().getSortedFile(file, file);
                getLog().info("end succesfull for file " + file.getName());
            } catch (Throwable e) {
                getLog().info("error processing of file " + file.getName());
                getLog().error(e);
            }
        }
    }

    private File[] validateWsdlPath() {
        File[] emptyResult = new File[0];

        if (StringUtils.isBlank(wsdlPath)) {
            getLog().error("wsdlPath shouldn't be empty!");
            return emptyResult;
        }

        File dir = new File(wsdlPath);
        if (!new File(wsdlPath).exists()) {
            getLog().error("wsdlPath should be exists!");
            return emptyResult;
        }

        if(dir.isFile()){
            return new File[]{dir};
        }

        File[] files = dir.listFiles(new FileFilter() {
            public boolean accept(File pathname) {
                return pathname.isFile() && isWsdlFile(pathname.getName());
            }

            private boolean isWsdlFile(String name) {
                return name.contains(".") && ".wsdl".equals(name.substring(name.lastIndexOf(".")));
            }
        });

        if (files == null || files.length == 0) {
            getLog().error(
                    MessageFormat.format("there aren''t wsdl files in parh: {0} !",
                            dir.getAbsolutePath()));
        }

        return files;
    }

    public Main setWsdlPath(String wsdlPath) {
        this.wsdlPath = wsdlPath;
        return this;
    }
}
