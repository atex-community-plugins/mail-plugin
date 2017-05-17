package com.atex.plugins.mail.service;

import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

import org.apache.commons.mail.HtmlEmail;

import com.atex.plugins.mail.MailSettingsPolicy;
import com.google.common.base.Strings;

/**
 * Implementation of {@link MailService}.
 *
 * @author mnova
 */
public class MailServiceImpl implements MailService {

    private static final Logger LOGGER = Logger.getLogger(MailServiceImpl.class.getName());

    private static final String SYSTEM_PROPERTY_MAIL_SMTP_CONNECTIONTIMEOUT = "mail.smtp.connectiontimeout";
    private static final String SYSTEM_PROPERTY_MAIL_SMTP_TIMEOUT = "mail.smtp.timeout";
    private static final String SYSTEM_PROPERTY_MAIL_SMTP_CLASS = "mail.smtp.class";
    private static final String SMTP_SSL_TRANSPORT = "com.sun.mail.smtp.SMTPSSLTransport";

    private boolean enabled;
    private Pattern validateRe;
    private String smtpHostName;
    private String smtpDefaultFromAddr;
    private int smtpPort;
    private String smtpUser;
    private String smtpPasswd;
    private int smtpSocketTimeout;
    private int smtpConnectionTimeout;
    private boolean smtpStartTLS;
    private boolean smtpUseSSL;
    private boolean smtpDebug;

    public MailServiceImpl(final MailSettingsPolicy settings) {
        enabled = settings.isEnabled();
        validateRe = asPattern(settings.getValidateRe());
        smtpHostName = settings.getSMTPServerHostname();
        smtpPort = settings.getSMTPServerPort();
        smtpUser = settings.getSMTPServerUsername();
        smtpPasswd = settings.getSMTPServerPassword();
        smtpSocketTimeout = settings.getSMTPServerSocketTimeout();
        smtpConnectionTimeout = settings.getSMTPServerConnectionTimeout();
        smtpDefaultFromAddr = settings.getDefaultFromAddress();
        smtpStartTLS = settings.useTLS();
        smtpUseSSL = settings.useSSL();
        smtpDebug = settings.isDebugEnabled();
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public boolean validateMailAddress(final String mailAddress) {

        LOGGER.fine("validating email '" + mailAddress + "'");

        boolean returnValue = false;

        if ((mailAddress != null) && (validateRe != null)) {
            returnValue = (validateRe.matcher(mailAddress).matches());
        }

        LOGGER.fine("validating email '" + mailAddress + "' return value: " + returnValue);

        return returnValue;
    }

    @Override
    public String send(final String from, final String to, final String subject, final String content,
                       final boolean useHtml) throws EmailException {

        LOGGER.info(String.format("Send from %s to %s, subject %s (html? %b)",
                from, to, subject, useHtml));

        final HtmlEmail email = new HtmlEmail();
        email.setDebug(smtpDebug);
        email.setTLS(smtpStartTLS);
        email.setSSL(smtpUseSSL);
        email.setCharset("UTF-8");
        if (!Strings.isNullOrEmpty(smtpUser)) {
            email.setAuthentication(smtpUser, Strings.nullToEmpty(smtpPasswd));
        }
        email.setHostName(smtpHostName);
        email.setSmtpPort(smtpPort);
        if (smtpUseSSL) {
            email.setSslSmtpPort(Integer.toString(smtpPort));
        }
        try {
            email.addTo(to);
            email.setFrom(from);
            email.setSubject(subject);
            if (useHtml) {
                email.setHtmlMsg(content);
            } else {
                email.setTextMsg(content);
            }
            email.getMailSession().getProperties().setProperty(SYSTEM_PROPERTY_MAIL_SMTP_TIMEOUT, Integer.toString(smtpSocketTimeout));
            email.getMailSession().getProperties().setProperty(SYSTEM_PROPERTY_MAIL_SMTP_CONNECTIONTIMEOUT, Integer.toString(smtpConnectionTimeout));
            if (smtpUseSSL) {
                email.getMailSession().getProperties().setProperty(SYSTEM_PROPERTY_MAIL_SMTP_CLASS, SMTP_SSL_TRANSPORT);
            }

            final String messageId = email.send();

            LOGGER.info("Mail sent: " + messageId);

            return messageId;

        } catch (org.apache.commons.mail.EmailException e) {
            LOGGER.log(Level.SEVERE, "Unable to send email to " + to, e);
            throw new EmailException("Unable to send email to " + to, e);
        }
    }

    @Override
    public String send(final String to, final String subject, final String content, final boolean useHtml)
            throws EmailException {
        return send(smtpDefaultFromAddr, to, subject, content, useHtml);
    }

    private Pattern asPattern(final String re) {
        if (re != null) {
            return Pattern.compile(re);
        }
        return null;
    }

}
