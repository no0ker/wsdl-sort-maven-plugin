package ru.orcsoft.maven.plugins;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import java.io.File;

@Mojo( name = "sayhi")
public class Main extends AbstractMojo
{
    @Parameter
    private String wsdlPath;

    public void execute() throws MojoExecutionException
    {
        getLog().error( "Hello, world." );
        getLog().error( "Hello, world." );
        getLog().error( "Hello, world." );
        getLog().error( "Hello, world." );
        getLog().error( "Hello, world." );
        getLog().error( "Hello, world." );
        getLog().error( "Hello, world." );
        getLog().error( "Hello, worldasdfdsfdsfdsfs." );
        getLog().error( wsdlPath );
        System.out.println(new File(wsdlPath).exists());
        System.out.println(new File(wsdlPath).list()[0]);
    }
}
