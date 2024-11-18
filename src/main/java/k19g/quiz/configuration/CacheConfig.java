package k19g.quiz.configuration;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.github.benmanes.caffeine.cache.Caffeine;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.binder.cache.CaffeineCacheMetrics;

/**
 * <p>Configuration class for setting up caching in the application using <b>Caffeine</b> cache manager.</p>
 * <p>This class configures the cache settings such as initial capacity, maximum size, and expiration time for the cache.</p>
 * <p>The cache manager is also integrated with <b>Micrometer</b> to gather metrics about cache performance.</p>
 * 
 * <h3>Key Features</h3>
 * <ul>
 *   <li>Initial Cache Capacity: 100 entries</li>
 *   <li>Maximum Cache Size: 500 entries</li>
 *   <li>Cache Expiration: Entries expire after 20 minutes of inactivity</li>
 *   <li>Integration with Micrometer for cache metrics</li>
 * </ul>
 * 
 * <h3>Usage</h3>
 * <p>This configuration enables caching across the application and uses the Caffeine cache as the caching provider. It integrates cache metrics with Micrometer to enable cache monitoring.</p>
 */
@Configuration
@EnableCaching
public class CacheConfig {
	
	/**
     * <p>Defines the Caffeine cache configuration, including the initial capacity, maximum size, and expiration time.</p>
     * 
     * <h4>Cache Settings:</h4>
     * <ul>
     *   <li><b>Initial Capacity:</b> 100</li>
     *   <li><b>Maximum Size:</b> 500</li>
     *   <li><b>Expiration Time:</b> 20 minutes after last access</li>
     * </ul>
     * 
     * @return A configured instance of {@link Caffeine} for use in the cache manager.
     */
	 @Bean
	 public Caffeine<Object, Object> caffeineConfig() {
	        return Caffeine.newBuilder()
	                .initialCapacity(100)
	                .maximumSize(500)
	                .expireAfterAccess(20, java.util.concurrent.TimeUnit.MINUTES);
	    }   
	
	 /**
	  * <p>Creates and configures a {@link CacheManager} instance using the Caffeine cache settings defined in {@link #caffeineConfig()}.</p>
	  * <p>The cache manager is also configured to monitor cache metrics using Micrometer for monitoring cache performance.</p>
	  *
	  * @param caffeine The {@link Caffeine} configuration used to configure the cache manager.
	  * @param meterRegistry The {@link MeterRegistry} used for collecting cache metrics.
	  * @return A configured {@link CacheManager} instance that integrates with Micrometer for cache metrics.
	  */       
	 @Bean
	 public CacheManager cacheManager(Caffeine<Object, Object> caffeine, MeterRegistry meterRegistry) {
	    CaffeineCacheManager caffeineCacheManager = new CaffeineCacheManager(){
	          protected com.github.benmanes.caffeine.cache.Cache<Object, Object> createNativeCaffeineCache(String name) {
	          return CaffeineCacheMetrics.monitor(meterRegistry, super.createNativeCaffeineCache(name), name);
	      }

	    };
	    caffeineCacheManager.setCaffeine(caffeine);
		return caffeineCacheManager;
	}
}