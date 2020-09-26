package com.jiva.cloud.juniatime.utils;

import com.jiva.cloud.juniatime.model.ping.PingCount;

public class PingCountUtil {
  
  private PingCountUtil() {
    throw new IllegalStateException("Utility class");
  }
  
  /**
   * Incrementa os up no horario normal e comercial em um {@link PingCount}
   */
  public static void incrementUp(Long upCount, Long upComercialCount, PingCount pingCount) {
    pingCount.incrementUpCount(upCount);
    pingCount.incrementUpComercialCount(upComercialCount);
  }
  
  /**
   * Incrementa os down no horario normal e comercial em um {@link PingCount}
   */
  public static void incrementDown(Long downCount, Long downComercialCount, PingCount pingCount) {
    pingCount.incrementDownCount(downCount);
    pingCount.incrementDownComercialCount(downComercialCount);
  }

}
