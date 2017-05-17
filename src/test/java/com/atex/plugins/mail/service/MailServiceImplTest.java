package com.atex.plugins.mail.service;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.atex.plugins.mail.MailSettingsPolicy;

@RunWith(MockitoJUnitRunner.class)
public class MailServiceImplTest {

    @Mock
    private MailSettingsPolicy settings;

    private MailService mailService;

    @Before
    public void before() {
        Mockito.doReturn("^.+@(?!\\.)\\S+\\.\\S+$").when(settings).getValidateRe();
        final MailServiceImpl service = new MailServiceImpl(settings);
        mailService = service;
    }

    @Test
    public void testValidEmailAddress() {

        // from http://en.wikipedia.org/wiki/Email_address

        final String[] emails = new String[] {
                "email@example.com",
                "test.email@example.com",
                "email+tag@example.com",
                "email@third.example.com",
                "a.little.lengthy.but.fine@dept.example.com",
                "disposable.style.email.with+symbol@example.com",
                "other.email-with-dash@example.com",
                "\"much.more unusual\"@example.com",
                "\"very.unusual.@.unusual.com\"@example.com",
                "\"very.(),:;<>[]\\\".VERY.\\\"very@\\\\ \\\"very\\\".unusual\"@strange.example.com",
                //"admin@mailserver1", // commented out, we are sending emails to outside, so we require TLD.
                "!#$%&'*+-/=?^_`{}|~@example.org",
                "\"()<>[]:,;@\\\\\\\"!#$%&'*+-/=?^_`{}| ~.a\"@example.org",
                "\" \"@example.org",
                "üñîçøðé@example.com",
                "üñîçøðé@üñîçøðé.com"
        };
        for (final String email : emails) {
            Assert.assertTrue(email, mailService.validateMailAddress(email));
        }
    }

    @Test
    public void testInvalidEmailAddress() {
        final String[] emails = new String[] {
                null,
                "@example.com",
                "email@.example.com",
                "email.example.com"

                // the following should be invalid but
                //"A@b@c@example.com",
                //"a\"b(c)d,e:f;g<h>i[j\\k]l@example.com",
                //"just\"not\"right@example.com",
                //"this is\"not\\allowed@example.com",
                //"this\\ still\\\"not\\\\allowed@example.com",
                //"john..doe@example.com",
                //"john.doe@example..com"
        };
        for (final String email : emails) {
            Assert.assertFalse(email, mailService.validateMailAddress(email));
        }
    }

}