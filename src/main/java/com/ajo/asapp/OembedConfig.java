package com.ajo.asapp;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import net.sf.ehcache.CacheManager;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import ac.simons.oembed.OembedEndpoint;
import ac.simons.oembed.OembedResponse.Format;
import ac.simons.oembed.OembedService;

@Configuration
public class OembedConfig {
  
  private OembedEndpoint youTube;
  
  public OembedConfig() {
    youTube = new OembedEndpoint();
    youTube.setName("youtube");
    youTube.setFormat(Format.json);
    youTube.setMaxWidth(480);
    youTube.setEndpoint("https://www.youtube.com/oembed");
    youTube.setUrlSchemes(Arrays.asList("https?://(www|de)\\.youtube\\.com/watch\\?v=.*"));
    // Optional, specialised renderer, not included here
    // endpoint.setResponseRendererClass(YoutubeRenderer.class);
    youTube.setEndpoint("http://www.youtube.com/oembed");
    
  }
  
  @Bean
  public OembedService oembedService() {
    
    List<OembedEndpoint> endpoints = new LinkedList<>();
    endpoints.add(youTube);
    
    final OembedService oembedService = new OembedService(new DefaultHttpClient(), null, endpoints, "some-app"); 
    oembedService.setAutodiscovery(true);
    /*if(this.cacheName != null) {
        oembedService.setCacheName(cacheName);
    }
    if(this.defaultCacheAge != null) {
        oembedService.setDefaultCacheAge(defaultCacheAge);
    }
    */
    return oembedService;
  }
  
}
