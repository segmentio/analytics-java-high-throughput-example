package com.segexample;

import com.jakewharton.retrofit.Ok3Client;
import com.segment.analytics.Analytics;
import com.segment.analytics.messages.TrackMessage;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import okhttp3.OkHttpClient;
import retrofit.client.Client;
import java.sql.Timestamp;
import java.util.Date;

public class Main {
  public static void main(String[] args) throws Exception {
    
    // final BlockingFlush blockingFlush = BlockingFlush.create(); //commented out please see comment on use under builder chain
    
    Date date = new Date();
    long time = date.getTime();
    
    System.out.println("Setting up analytics-java...");
    final Analytics analytics = Analytics.builder("3KiDlUvApV7FW9rmiULOFfPV4wzUwdXM")
    //.networkExecutor(Executors.newFixedThreadPool(10000000)) //configure number of threads
    //.flushQueueSize(100)   // if messages bigger than 2kb then tweak this to be lower than 250 (default value due to tracking api limit on message size)
    //.plugin(blockingFlush.plugin()) // this is not important but if you want to block app until all messages processed then please use this and look into BlockingFlush.java comments; there is a limit of 64k messages in counter
    // .plugin(new LoggingPlugin()) //disable logger from builder plugins; uncomment to see logs and change LoggingPlugin class as needed
    .client(createClient())
    .build();

    final String userId = System.getProperty("user.name");
    final String anonymousId = UUID.randomUUID().toString();
    final AtomicInteger count = new AtomicInteger();
    System.out.println("Setup analytics-java complete.");
    System.out.println("Starting to send 10k track() calls ...");
    System.out.println("Start Time: " + new Timestamp(time));
    for (int i = 0; i < 100; i++) {
      for (int j = 0; j < 100; j++) {
        Map<String, Object> properties = new LinkedHashMap<>();
        properties.put("count", count.incrementAndGet());
        analytics
            .enqueue(TrackMessage.builder("Java Test")
            .properties(properties)
            .anonymousId(anonymousId)
            .userId(userId));
      }
      // System.out.println("Message Count: " + count.get() + " Time is: " + new Timestamp(time));
    }
    System.out.println(count.get() + " track() calls sent.");
    System.out.println("End Time: " + new Timestamp(time));

    analytics.flush();
    // blockingFlush.block(); // comment out when not using blockingFlush above
    analytics.shutdown();
  }

  /**
   * By default, the analytics client uses an HTTP client with sane defaults.
   * However you can customize the client to your needs. For instance, this client
   * is configured to automatically gzip outgoing requests.
   */
  private static Client createClient() {
    return new Ok3Client(
        new OkHttpClient.Builder().connectTimeout(15, TimeUnit.SECONDS).readTimeout(15, TimeUnit.SECONDS)
            .writeTimeout(15, TimeUnit.SECONDS).addInterceptor(new GzipRequestInterceptor()).build());
  }
}
