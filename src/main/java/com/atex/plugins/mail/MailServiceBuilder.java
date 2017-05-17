package com.atex.plugins.mail;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.atex.plugins.mail.service.MailService;
import com.atex.plugins.mail.service.MailServiceImpl;
import com.atex.plugins.mail.service.MailServiceNotAvailable;
import com.polopoly.cm.ContentId;
import com.polopoly.cm.ExternalContentId;
import com.polopoly.cm.app.policy.CheckboxPolicy;
import com.polopoly.cm.client.CMException;
import com.polopoly.cm.policy.Policy;
import com.polopoly.cm.policy.PolicyCMServer;

/**
 * Create a new instance of {@link MailService}, you can specify a site from which it will
 * eventually take configuration overrides.
 *
 * @author mnova
 */
public class MailServiceBuilder {

    private static final Logger LOGGER = Logger.getLogger(MailServiceBuilder.class.getName());

    private ContentId siteId = null;
    private Policy sitePolicy = null;
    private boolean globalFallback = true;
    private final PolicyCMServer cmServer;

    /**
     * Constructor.
     *
     * @param cmServer the {@link PolicyCMServer}.
     */
    public MailServiceBuilder(final PolicyCMServer cmServer) {
        this.cmServer = cmServer;
    }

    /**
     * You can specify the site through its contentId.
     *
     * @param siteId a {@link ContentId}.
     * @return itself.
     */
    public MailServiceBuilder siteId(final ContentId siteId) {
        this.siteId = siteId;
        return this;
    }

    /**
     * You can specify the site through its policy.
     *
     * @param sitePolicy a {@link Policy}.
     * @return itself.
     */
    public MailServiceBuilder sitePolicy(final Policy sitePolicy) {
        this.sitePolicy = sitePolicy;
        return this;
    }

    /**
     * You can decide to fallback (or not) to the global configuration.
     * By default we will fallback on the global one.
     *
     * @param globalFallback true if you want to fallback or false otherwise.
     * @return itself.
     */
    public MailServiceBuilder globalFallback(final boolean globalFallback) {
        this.globalFallback = globalFallback;
        return this;
    }

    /**
     * Build the {@link MailService} service.
     * It will throw a {@link RuntimeException} which wraps a {@link CMException}
     * if we cannot access the site or the global configuration.
     *
     * @return a not null {@link MailService} service.
     */
    public MailService build() {
        try {
            MailSettingsPolicy settingsPolicy = null;

            // check if we have been provided a site policy
            // or a contentId.

            if (sitePolicy == null && siteId != null) {
                sitePolicy = cmServer.getPolicy(siteId.getContentId());
            }

            // if we have a site policy then check if there is any
            // override available.

            if (sitePolicy != null) {
                final CheckboxPolicy checkboxPolicy = (CheckboxPolicy) sitePolicy.getChildPolicy("overrideMailConfiguration");
                if (checkboxPolicy != null && checkboxPolicy.getBooleanValue()) {
                    settingsPolicy = (MailSettingsPolicy) sitePolicy.getChildPolicy("mailConfiguration");
                }
            }

            // otherwise fetch the global one.

            if (settingsPolicy == null && globalFallback) {
                settingsPolicy = getGlobalConfiguration();
            }

            // build the service.

            if (settingsPolicy == null) {
                return new MailServiceNotAvailable();
            } else {
                return new MailServiceImpl(settingsPolicy);
            }
        } catch (CMException e) {
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    MailSettingsPolicy getGlobalConfiguration() throws CMException {
        final Policy configPolicy = cmServer.getPolicy(
                cmServer.findContentIdByExternalId(
                        new ExternalContentId("plugins.com.atex.plugins.mail.Config")
                )
        );
        return (MailSettingsPolicy) configPolicy.getChildPolicy("settings");
    }

}
