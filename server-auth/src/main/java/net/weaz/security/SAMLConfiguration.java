package net.weaz.security;

import org.apache.commons.httpclient.HttpClient;
import org.opensaml.saml2.metadata.provider.FilesystemMetadataProvider;
import org.opensaml.saml2.metadata.provider.HTTPMetadataProvider;
import org.opensaml.saml2.metadata.provider.MetadataProvider;
import org.opensaml.saml2.metadata.provider.MetadataProviderException;
import org.opensaml.xml.parse.ParserPool;
import org.opensaml.xml.parse.StaticBasicParserPool;
import org.opensaml.xml.parse.XMLParserException;
import org.opensaml.xml.security.x509.PKIXTrustEvaluator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.saml.SAMLAuthenticationProvider;
import org.springframework.security.saml.SAMLEntryPoint;
import org.springframework.security.saml.SAMLProcessingFilter;
import org.springframework.security.saml.context.SAMLContextProvider;
import org.springframework.security.saml.context.SAMLContextProviderLB;
import org.springframework.security.saml.key.JKSKeyManager;
import org.springframework.security.saml.key.KeyManager;
import org.springframework.security.saml.log.SAMLDefaultLogger;
import org.springframework.security.saml.log.SAMLLogger;
import org.springframework.security.saml.metadata.CachingMetadataManager;
import org.springframework.security.saml.metadata.ExtendedMetadata;
import org.springframework.security.saml.metadata.ExtendedMetadataDelegate;
import org.springframework.security.saml.metadata.MetadataManager;
import org.springframework.security.saml.processor.HTTPPostBinding;
import org.springframework.security.saml.processor.HTTPRedirectDeflateBinding;
import org.springframework.security.saml.processor.SAMLBinding;
import org.springframework.security.saml.processor.SAMLProcessor;
import org.springframework.security.saml.processor.SAMLProcessorImpl;
import org.springframework.security.saml.trust.CertPathPKIXTrustEvaluator;
import org.springframework.security.saml.trust.MetadataCredentialResolver;
import org.springframework.security.saml.trust.PKIXInformationResolver;
import org.springframework.security.saml.util.VelocityFactory;
import org.springframework.security.saml.websso.WebSSOProfile;
import org.springframework.security.saml.websso.WebSSOProfileConsumer;
import org.springframework.security.saml.websso.WebSSOProfileConsumerHoKImpl;
import org.springframework.security.saml.websso.WebSSOProfileConsumerImpl;
import org.springframework.security.saml.websso.WebSSOProfileImpl;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;

@Configuration
public class SAMLConfiguration {

    @Bean
    public SAMLLogger samlLogger() {
        return new SAMLDefaultLogger();
    }

    private MetadataProvider metadataProvider(SAMLSecurityConfigurationProperties samlSecurityConfigurationProperties,
                                              StaticBasicParserPool parserPool) {
        Resource metadataResource = samlSecurityConfigurationProperties.getMetadataResource();
        if (metadataResource.toString().startsWith("http")) {
            try {
                HTTPMetadataProvider httpMetadataProvider = new HTTPMetadataProvider(new Timer(), new HttpClient(), metadataResource.toString());
                httpMetadataProvider.setParserPool(parserPool);
                return httpMetadataProvider;
            } catch (MetadataProviderException e) {
                e.printStackTrace();
                return null;
            }
        } else {
            File samlMetadata = null;
            try {
                samlMetadata = metadataResource.getFile();
            } catch (IOException e) {
                e.printStackTrace();
            }

            FilesystemMetadataProvider filesystemMetadataProvider = null;
            try {
                filesystemMetadataProvider = new FilesystemMetadataProvider(samlMetadata);
            } catch (MetadataProviderException e) {
                e.printStackTrace();
            }
            if (filesystemMetadataProvider != null) {
                filesystemMetadataProvider.setParserPool(parserPool);
            }

            return filesystemMetadataProvider;
        }
    }

    @Bean
    public SAMLProcessingFilter samlProcessingFilter(SAMLAuthenticationProvider samlAuthenticationProvider,
                                                     SAMLContextProvider contextProvider,
                                                     SAMLProcessor samlProcessor) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder = new AuthenticationManagerBuilder(new ObjectPostProcessor<Object>() {
            public <T> T postProcess(T object) {
                return object;
            }
        });

        authenticationManagerBuilder.authenticationProvider(samlAuthenticationProvider);

        SAMLProcessingFilter samlProcessingFilter = new SAMLProcessingFilter();
        samlProcessingFilter.setAuthenticationManager(authenticationManagerBuilder.build());
        samlProcessingFilter.setContextProvider(contextProvider);
        samlProcessingFilter.setSAMLProcessor(samlProcessor);
        return samlProcessingFilter;
    }

    @Bean
    public SAMLEntryPoint samlEntryPoint() {
        return new SAMLEntryPoint();
    }

    @Bean
    public SAMLProcessor samlProcessor(StaticBasicParserPool parserPool) {
        Collection<SAMLBinding> bindings = new ArrayList<>();
        bindings.add(httpRedirectDeflateBinding(parserPool));
        bindings.add(httpPostBinding(parserPool));
        return new SAMLProcessorImpl(bindings);
    }

    private HTTPPostBinding httpPostBinding(ParserPool parserPool) {
        return new HTTPPostBinding(parserPool, VelocityFactory.getEngine());
    }

    private HTTPRedirectDeflateBinding httpRedirectDeflateBinding(ParserPool parserPool) {
        return new HTTPRedirectDeflateBinding(parserPool);
    }

    @Bean
    public SAMLContextProvider contextProvider(SAMLSecurityConfigurationProperties samlSecurityConfigurationProperties,
                                               CachingMetadataManager cachingMetadataManager,
                                               KeyManager keyManager) {
        SAMLContextProviderLB contextProvider = new SAMLContextProviderLB();
        contextProvider.setMetadata(cachingMetadataManager);
        contextProvider.setScheme(samlSecurityConfigurationProperties.getProtocol());
        contextProvider.setServerName(samlSecurityConfigurationProperties.getHostName());
        contextProvider.setContextPath(samlSecurityConfigurationProperties.getBasePath());
        contextProvider.setKeyManager(keyManager);

        MetadataCredentialResolver resolver = new MetadataCredentialResolver(cachingMetadataManager, keyManager);
        PKIXTrustEvaluator pkixTrustEvaluator = new CertPathPKIXTrustEvaluator();
        PKIXInformationResolver pkixInformationResolver = new PKIXInformationResolver(resolver, cachingMetadataManager, keyManager);

        contextProvider.setPkixResolver(pkixInformationResolver);
        contextProvider.setPkixTrustEvaluator(pkixTrustEvaluator);
        contextProvider.setMetadataResolver(resolver);

        return contextProvider;
    }

    @Bean
    public KeyManager keyManager() {
        Map<String, String> passwords = new HashMap<>();
        passwords.put("something", "test");
        return new JKSKeyManager(new ClassPathResource("keystore.jks"), "foobar", passwords, "something");
    }

    @Bean
    public ExtendedMetadataDelegate extendedMetadataDelegate(SAMLSecurityConfigurationProperties samlSecurityConfigurationProperties,
                                                             StaticBasicParserPool parserPool) {
        ExtendedMetadataDelegate extendedMetadataDelegate = new ExtendedMetadataDelegate(metadataProvider(samlSecurityConfigurationProperties, parserPool), extendedMetadata());
        extendedMetadataDelegate.setMetadataTrustCheck(false);
        extendedMetadataDelegate.setMetadataRequireSignature(false);
        return extendedMetadataDelegate;
    }

    private ExtendedMetadata extendedMetadata() {
        ExtendedMetadata extendedMetadata = new ExtendedMetadata();
        extendedMetadata.setIdpDiscoveryEnabled(true);
        extendedMetadata.setSignMetadata(true);
        return extendedMetadata;
    }

    @Bean
    public CachingMetadataManager cachingMetadataManager(ExtendedMetadataDelegate extendedMetadataDelegate,
                                                         KeyManager keyManager) {
        List<MetadataProvider> providers = new ArrayList<>();
        providers.add(extendedMetadataDelegate);

        CachingMetadataManager cachingMetadataManager = null;
        try {
            cachingMetadataManager = new CachingMetadataManager(providers);
        } catch (MetadataProviderException e) {
            e.printStackTrace();
        }

        cachingMetadataManager.setKeyManager(keyManager);
        return cachingMetadataManager;
    }

    @Bean
    public WebSSOProfile webSSOprofile(SAMLProcessor samlProcessor, MetadataManager cachingMetadataManager) {
        return new WebSSOProfileImpl(samlProcessor, cachingMetadataManager);
    }

    @Bean
    public WebSSOProfileConsumer webSSOprofileConsumer() {
        return new WebSSOProfileConsumerImpl();
    }

    @Bean
    public WebSSOProfileConsumer hokWebSSOprofileConsumer() {
        return new WebSSOProfileConsumerHoKImpl();
    }

    @Bean
    public SAMLAuthenticationProvider samlAuthenticationProvider(SAMLLogger samlLogger, WebSSOProfileConsumer webSSOprofileConsumer) {
        SAMLAuthenticationProvider samlAuthenticationProvider = new SAMLAuthenticationProvider();
        samlAuthenticationProvider.setForcePrincipalAsString(false);
        samlAuthenticationProvider.setSamlLogger(samlLogger);
        samlAuthenticationProvider.setConsumer(webSSOprofileConsumer);
        return samlAuthenticationProvider;
    }

    @Bean
    public StaticBasicParserPool staticBasicParserPool() {
        StaticBasicParserPool parserPool = new StaticBasicParserPool();
        try {
            parserPool.initialize();
        } catch (XMLParserException e) {
            e.printStackTrace();
        }
        return parserPool;
    }
}
