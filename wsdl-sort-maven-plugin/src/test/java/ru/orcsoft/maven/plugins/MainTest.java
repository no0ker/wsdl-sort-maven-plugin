package ru.orcsoft.maven.plugins;

public class MainTest {
    /**
     * you can put your wsdl files into test resources folder and test how it works
     *
     * @throws Exception
     */
//    @org.junit.Test
    public void execute() throws Exception {
        new Main().setWsdlPath("src/test/resources/wsdls").execute();
    }
}