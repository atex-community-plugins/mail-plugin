package com.atex.plugins.mail.service;

/**
 * An empty implementation of the {@link MailService}.
 *
 * @author mnova
 */
public class MailServiceNotAvailable implements MailService {

    @Override
    public boolean isEnabled() {
        return false;
    }

    @Override
    public boolean validateMailAddress(final String mailAddress) {
        return false;
    }

    @Override
    public String send(final String from, final String to, final String subject, final String content, final boolean useHtml)
            throws EmailException {

        throw new UnsupportedOperationException("mail service not available");
    }

    @Override
    public String send(final String to, final String subject, final String content, final boolean useHtml)
            throws EmailException {

        throw new UnsupportedOperationException("mail service not available");
    }
}
