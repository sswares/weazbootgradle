package net.weaz.security.remote;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.rcp.RemoteAuthenticationProvider;
import org.springframework.stereotype.Component;

@Component
public class CustomRemoteAuthenticationProvider extends RemoteAuthenticationProvider {

    @Autowired
    public CustomRemoteAuthenticationProvider(CustomRemoteAuthenticationManager customRemoteAuthenticationManager) {
        setRemoteAuthenticationManager(customRemoteAuthenticationManager);
    }
}