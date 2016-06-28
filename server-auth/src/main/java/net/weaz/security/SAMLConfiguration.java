package net.weaz.security;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.opensaml.PaosBootstrap;
import org.opensaml.saml2.metadata.provider.FilesystemMetadataProvider;
import org.opensaml.saml2.metadata.provider.HTTPMetadataProvider;
import org.opensaml.saml2.metadata.provider.MetadataProvider;
import org.opensaml.saml2.metadata.provider.MetadataProviderException;
import org.opensaml.xml.ConfigurationException;
import org.opensaml.xml.parse.StaticBasicParserPool;
import org.opensaml.xml.parse.XMLParserException;
import org.opensaml.xml.security.keyinfo.NamedKeyInfoGeneratorManager;
import org.opensaml.xml.security.x509.PKIXTrustEvaluator;
import org.opensaml.xml.security.x509.X509KeyInfoGeneratorFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.saml.SAMLAuthenticationProvider;
import org.springframework.security.saml.SAMLConstants;
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
import java.io.InputStream;
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
        bindings.add(new HTTPRedirectDeflateBinding(parserPool));
        bindings.add(new HTTPPostBinding(parserPool, VelocityFactory.getEngine()));
        return new SAMLProcessorImpl(bindings);
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
        try {
            PaosBootstrap.bootstrap();
        } catch (ConfigurationException e) {
            e.printStackTrace();
        }

        NamedKeyInfoGeneratorManager manager = org.opensaml.xml.Configuration.getGlobalSecurityConfiguration().getKeyInfoGeneratorManager();
        X509KeyInfoGeneratorFactory generator = new X509KeyInfoGeneratorFactory();
        generator.setEmitEntityCertificate(true);
        generator.setEmitEntityCertificateChain(true);
        manager.registerFactory(SAMLConstants.SAML_METADATA_KEY_INFO_GENERATOR, generator);

        Map<String, String> passwords = new HashMap<>();
        passwords.put("foobar", "test");

        return new JKSKeyManager(new ClassPathResource("keystore.jks"), "foobar", passwords, "foobar");
    }

    @Bean
    public ExtendedMetadataDelegate extendedMetadataDelegate(SAMLSecurityConfigurationProperties samlSecurityConfigurationProperties,
                                                             StaticBasicParserPool parserPool, ExtendedMetadata extendedMetadata) {
        MetadataProvider result;
        Resource metadataResource = samlSecurityConfigurationProperties.getMetadataResource();

        if (metadataResource.toString().startsWith("http")) {
            try {
                HTTPMetadataProvider httpMetadataProvider = new HTTPMetadataProvider(new Timer(), new HttpClient(), metadataResource.toString());
                httpMetadataProvider.setParserPool(parserPool);
                result = httpMetadataProvider;
            } catch (MetadataProviderException e) {
                throw new RuntimeException("Exception thrown creating httpMetadataProvider", e);
            }
        } else {
            try {
                InputStream inputStream = metadataResource.getInputStream();
                File metadataFile = File.createTempFile("metadata", ".xml");

                try {
                    FileUtils.copyInputStreamToFile(inputStream, metadataFile);
                } finally {
                    IOUtils.closeQuietly(inputStream);
                }

                FilesystemMetadataProvider filesystemMetadataProvider = new FilesystemMetadataProvider(metadataFile);
                filesystemMetadataProvider.setParserPool(parserPool);
                result = filesystemMetadataProvider;
            } catch (IOException | MetadataProviderException e) {
                throw new RuntimeException("Exception thrown creating filesystemMetadataProvider", e);
            }
        }

        ExtendedMetadataDelegate extendedMetadataDelegate = new ExtendedMetadataDelegate(result, extendedMetadata);
        extendedMetadataDelegate.setMetadataTrustCheck(false);
        extendedMetadataDelegate.setMetadataRequireSignature(false);
        return extendedMetadataDelegate;
    }

    @Bean
    public ExtendedMetadata extendedMetadata() {
        ExtendedMetadata extendedMetadata = new ExtendedMetadata();
        extendedMetadata.setIdpDiscoveryEnabled(true);
        extendedMetadata.setSignMetadata(true);
        return extendedMetadata;
    }

    @Bean
    public CachingMetadataManager cachingMetadataManager(ExtendedMetadataDelegate extendedMetadataDelegate,
                                                         KeyManager keyManager) {
        try {
            List<MetadataProvider> providers = new ArrayList<>();
            providers.add(extendedMetadataDelegate);
            CachingMetadataManager cachingMetadataManager = new CachingMetadataManager(providers);
            cachingMetadataManager.setKeyManager(keyManager);
            return cachingMetadataManager;
        } catch (MetadataProviderException e) {
            throw new RuntimeException("Exception thrown creating cachingMetadataManager", e);
        }
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
    public SAMLAuthenticationProvider samlAuthenticationProvider(SAMLLogger samlLogger,
                                                                 WebSSOProfileConsumer webSSOprofileConsumer) {
        SAMLAuthenticationProvider samlAuthenticationProvider = new SAMLAuthenticationProvider();
        samlAuthenticationProvider.setForcePrincipalAsString(false);
        samlAuthenticationProvider.setSamlLogger(samlLogger);
        samlAuthenticationProvider.setConsumer(webSSOprofileConsumer);
        return samlAuthenticationProvider;
    }

    @Bean
    public StaticBasicParserPool staticBasicParserPool() {
        try {
            StaticBasicParserPool parserPool = new StaticBasicParserPool();
            parserPool.initialize();
            return parserPool;
        } catch (XMLParserException e) {
            throw new RuntimeException("Exception thrown initializing staticBasicParserPool", e);
        }
    }
}
