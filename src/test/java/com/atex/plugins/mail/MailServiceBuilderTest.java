package com.atex.plugins.mail;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.atex.plugins.mail.service.EmailException;
import com.atex.plugins.mail.service.MailService;
import com.polopoly.cm.app.policy.CheckboxPolicy;
import com.polopoly.cm.client.CMException;
import com.polopoly.cm.policy.Policy;
import com.polopoly.cm.policy.PolicyCMServer;

/**
 * MailServiceBuilderTest
 *
 * @author mnova
 */
@RunWith(MockitoJUnitRunner.class)
public class MailServiceBuilderTest {

    @Mock
    private PolicyCMServer cmServer;

    @Mock
    private MailSettingsPolicy globalSettingsPolicy;

    @Mock
    private MailSettingsPolicy siteSettingsPolicy;

    @Mock
    private Policy sitePolicy;

    private MailServiceBuilder builder;

    @Before
    public void before() throws CMException {
        builder = Mockito.spy(new MailServiceBuilder(cmServer));

        Mockito.doReturn(globalSettingsPolicy)
               .when(builder)
               .getGlobalConfiguration();

        Mockito.doReturn(siteSettingsPolicy)
               .when(sitePolicy)
               .getChildPolicy(Mockito.eq("mailConfiguration"));

    }

    @Test(expected = UnsupportedOperationException.class)
    public void noServiceAvailable() throws EmailException {
        final MailService service = builder
                .globalFallback(false)
                .build();

        Assert.assertNotNull(service);
        Assert.assertEquals(false, service.isEnabled());

        // this method should throw UnsupportedOperationException
        service.send(null, null, null, false);
    }

    @Test
    public void serviceAvailable() {
        Mockito.doReturn(true)
               .when(globalSettingsPolicy)
               .isEnabled();

        final MailService service = builder
                .build();

        Assert.assertNotNull(service);
        Assert.assertEquals(true, service.isEnabled());
    }

    @Test
    public void useSiteOverride() throws CMException {
        setupOverride(true);

        // global will return disabled.

        Mockito.doReturn(false)
               .when(globalSettingsPolicy)
               .isEnabled();

        // site will return enabled.

        Mockito.doReturn(true)
               .when(siteSettingsPolicy)
               .isEnabled();

        final MailService service = builder
                .sitePolicy(sitePolicy)
                .build();

        Assert.assertNotNull(service);
        Assert.assertEquals(true, service.isEnabled());
    }

    @Test
    public void dontUseSiteOverride() throws CMException {
        setupOverride(false);

        // global will return disabled.

        Mockito.doReturn(false)
               .when(globalSettingsPolicy)
               .isEnabled();

        // site will return enabled.

        Mockito.doReturn(true)
               .when(siteSettingsPolicy)
               .isEnabled();

        final MailService service = builder
                .sitePolicy(sitePolicy)
                .build();

        Assert.assertNotNull(service);
        Assert.assertEquals(false, service.isEnabled());
    }

    private void setupOverride(final boolean value) throws CMException {
        final CheckboxPolicy checkboxPolicy = Mockito.mock(CheckboxPolicy.class);
        Mockito.doReturn(checkboxPolicy)
               .when(sitePolicy)
               .getChildPolicy(Mockito.eq("overrideMailConfiguration"));
        Mockito.doReturn(value)
               .when(checkboxPolicy)
               .getBooleanValue();
    }
}