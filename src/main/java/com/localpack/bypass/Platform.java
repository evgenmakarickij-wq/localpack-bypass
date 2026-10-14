package com.localpack.bypass;

import java.util.Locale;

/**
 * Which system the game is running on, and the few decisions that actually depend on it.
 *
 * <p>No path is ever assembled here from a platform name: Fabric already points the game directory
 * at the right folder on Windows, macOS, Linux and the Android launchers, and every path is built
 * with {@link java.nio.file.Path#resolve} rather than a separator. What is left is the smaller set
 * of questions the mod does have to ask — whether the jar it was started from can be deleted
 * (Windows holds a lock on it, the others do not) and whether a shell helper is worth writing.
 *
 * <p>Android is decided before Linux on purpose. An Android launcher runs an ordinary OpenJDK, so
 * {@code os.name} is {@code "Linux"} and the JVM name gives nothing away; the kernel line under
 * {@code os.version} does — it reads {@code "6.12.38-android16-..."} on a phone and never contains
 * the word "android" on a desktop.
 */
public enum Platform {
    WINDOWS,
    MACOS,
    LINUX,
    ANDROID,
    UNKNOWN;

    private static final Platform CURRENT = detect();

    public static Platform current() {
        return CURRENT;
    }

    public static boolean isWindows() {
        return CURRENT == WINDOWS;
    }

    /**
     * Whether the game may delete the jar it was started from. Windows keeps a file lock on a jar
     * that is on the classpath; everywhere else the file is merely open, and unlinking it is
     * allowed.
     */
    public static boolean canReplaceRunningJar() {
        return CURRENT != WINDOWS && CURRENT != UNKNOWN;
    }

    private static Platform detect() {
        String os = System.getProperty("os.name", "").toLowerCase(Locale.ROOT);
        if (os.startsWith("windows")) return WINDOWS;
        if (looksLikeAndroid()) return ANDROID;
        if (os.contains("mac") || os.contains("darwin") || os.contains("os x")) return MACOS;
        if (os.contains("linux") || os.contains("nux") || os.contains("aix")) return LINUX;
        return UNKNOWN;
    }

    /** The kernel line is the reliable signal; the rest is there for launchers that set only some of it. */
    private static boolean looksLikeAndroid() {
        if (System.getProperty("os.version", "").toLowerCase(Locale.ROOT).contains("android")) return true;

        if (System.getProperty("java.vm.name", "").toLowerCase(Locale.ROOT).contains("dalvik")) return true;
        if (System.getProperty("java.runtime.name", "").toLowerCase(Locale.ROOT).contains("android")) return true;

        return System.getenv("ANDROID_ROOT") != null
                || System.getenv("ANDROID_DATA") != null
                || System.getenv("ANDROID_STORAGE") != null;
    }
}
