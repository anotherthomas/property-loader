package com.tngtech.propertyloader;

import com.google.common.collect.Lists;
import com.tngtech.propertyloader.impl.PropertySuffix;
import com.tngtech.propertyloader.impl.helpers.HostsHelper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class PropertySuffixTest {

    private PropertySuffix propertySuffix;
    private List<String> list;
    String[] testAddLocalHostNamesAndAddSuffixList = {"testAddLocalHostNames","testAddSuffixList"};


    @Mock
    private HostsHelper hostshelper;

    @Before
    public void SetUp() {
        list = Lists.newArrayList(testAddLocalHostNamesAndAddSuffixList);
        propertySuffix = new PropertySuffix((hostshelper));

        when(hostshelper.getLocalHostNames()).thenReturn(list);
    }

    @Test
    public void testAddUserName() {
        assertEquals(propertySuffix, propertySuffix.addUserName());
        assertTrue(propertySuffix.getSuffixes().contains(System.getProperty("user.name")));
    }

    @Test
    public void testAddLocalHostNames() {
        assertEquals(propertySuffix, propertySuffix.addLocalHostNames());
        assertTrue(propertySuffix.getSuffixes().contains("testAddLocalHostNames"));
    }
    @Test
    public void testAddString() {
        assertEquals(propertySuffix, propertySuffix.addString("testAddString"));
        assertTrue(propertySuffix.getSuffixes().contains("testAddString"));
    }
    @Test
    public void testAddSuffixList() {
        assertEquals(propertySuffix, propertySuffix.addSuffixList(list));
        assertTrue(propertySuffix.getSuffixes().contains("testAddSuffixList"));
    }
    @Test
    public void testAddDefaultConfig() {
        assertEquals(propertySuffix, propertySuffix.addDefaultConfig());
        assertTrue(propertySuffix.getSuffixes().contains("testAddLocalHostNames"));
        assertTrue(propertySuffix.getSuffixes().contains(System.getProperty("user.name")));
        assertTrue(propertySuffix.getSuffixes().contains("override"));
    }


}
