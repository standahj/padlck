package com.cleverthis.interview.padlock;

class Utils {
    /**
     * Check if the sleep is disabled.
     * User can use `-Dfast=true` in the jvm args,
     * or change it on the fly.
     * Might waste sometime on checking this flag, but the effect should be minor.
     * */
    private static boolean shouldSkipSleep() {
        return Boolean.parseBoolean(System.getProperty("fast"));
    }

    /**
     * Ensure we will wait a given amount of time even if there are interruptions.
     * Property `-Dfast=true` can disable the sleep.
     *
     * @param millis The time you want to sleep, measure in millisecond.
     */
    public static void ensureSleep(long millis) {
        if (shouldSkipSleep()) return;
        long endTime = System.currentTimeMillis() + millis;
        while (endTime > System.currentTimeMillis()) {
            try {
                //noinspection BusyWait
                Thread.sleep(endTime - System.currentTimeMillis());
            } catch (InterruptedException e) {
                // do nothing when interrupted, will re-sleep in next loop
            } catch (IllegalArgumentException e) {
                break;
            }
        }
    }
}
