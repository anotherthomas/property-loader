package com.tngtech.configbuilder;

import com.tngtech.configbuilder.annotations.*;
import com.tngtech.configbuilder.impl.ConfigLoader;
import com.tngtech.propertyloader.PropertyLoader;
import org.apache.commons.cli.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.lang.annotation.Annotation;
import java.util.Properties;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ConfigLoaderTest {

    private ConfigLoader configLoader;
    private Properties properties;
    private Properties errors;

    @Mock
    private PropertyLoader propertyLoader;

    @Before
    public void setUp(){
        configLoader = new ConfigLoader(propertyLoader);
        properties = new Properties();
        errors = new Properties();
        properties.put("thisisaproperty","");
        errors.put("thisisanerrormessage","");
    }

    @Test
    public void testLoadPropertiesFromAnnotation(){
        Annotation[] annotations = Config.class.getDeclaredAnnotations();

        when(propertyLoader.loadProperties("demoapp-configuration", "properties")).thenReturn(properties);
        when(propertyLoader.loadProperties("errors", "properties")).thenReturn(errors);
        assertTrue(configLoader.loadPropertiesFromAnnotation((PropertiesFile)annotations[0]).containsKey("thisisaproperty"));
        assertTrue(configLoader.loadPropertiesFromAnnotation((ErrorMessageFile)annotations[1]).containsKey("thisisanerrormessage"));

    }

    @Test
    public void testLoadStringFromAnnotation(){
        try{
            Annotation[] annotations = Config.class.getDeclaredField("userName").getDeclaredAnnotations();

            String[] args = new String[]{"-u", "Mueller"};
            Options options = new Options();
            options.addOption("u", false, "userName");
            CommandLineParser parser = new GnuParser();
            try {
                CommandLine commandLineArgs = parser.parse( options, args);
                Properties properties = new Properties();
                properties.put("user.name","Meier");

                for(Annotation annotation : annotations){
                    if(annotation.annotationType() == DefaultValue.class){
                        DefaultValue defaultValue = (DefaultValue) annotation;
                        String result =  configLoader.loadStringFromAnnotation(defaultValue);
                        assertEquals("user",result);
                    }
                    else if(annotation.annotationType() == PropertyValue.class){
                        PropertyValue propertyValue = (PropertyValue) annotation;
                        String result =  configLoader.loadStringFromAnnotation(propertyValue, properties);
                        assertEquals("Meier",result);
                    }
                    else if(annotation.annotationType() == CommandLineValue.class){
                        CommandLineValue commandLineValue = (CommandLineValue) annotation;
                        String result =  configLoader.loadStringFromAnnotation(commandLineValue, commandLineArgs);
                        assertEquals("Mueller",result);
                    }
                }
            } catch (ParseException e) {}


        }
        catch (NoSuchFieldException e){}


    }
}