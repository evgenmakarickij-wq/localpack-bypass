package com.localpack.bypass;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Comparison for versions like "0.0.2", "v0.0.2", "1.2.0-beta", "1.2.0-beta.1".
 *
 * <p>No dependency is pulled in for this: the release tags of this mod are plain dotted numbers
 * with an optional pre-release suffix, and a full semver library would be more code than the
 * comparison itself.
 */
public final class ModVersion implements Comparable<ModVersion> {

    private static final Pattern VERSION = Pattern.compile("^v?(\\d+)(?:\\.(\\d+))?(?:\\.(\\d+))?(?:[-+](.+))?$");

    private final int[] numbers;
    private final String preRelease;

    private ModVersion(int[] numbers, String preRelease) {
        this.numbers = numbers;
        this.preRelease = preRelease;
    }

    public static ModVersion parse(String raw) {
        if (raw == null) return null;
        Matcher matcher = VERSION.matcher(raw.trim());
        if (!matcher.matches()) return null;

        int[] numbers = new int[3];
        for (int i = 0; i < 3; i++) {
            String part = matcher.group(i + 1);
            numbers[i] = part == null ? 0 : Integer.parseInt(part);
        }
        return new ModVersion(numbers, matcher.group(4));
    }

    /** Null-safe: an unparseable version on either side counts as "not newer". */
    public static boolean isNewer(String candidate, String current) {
        ModVersion a = parse(candidate);
        ModVersion b = parse(current);
        return a != null && b != null && a.compareTo(b) > 0;
    }

    @Override
    public int compareTo(ModVersion other) {
        for (int i = 0; i < 3; i++) {
            int diff = Integer.compare(numbers[i], other.numbers[i]);
            if (diff != 0) return diff;
        }
        // No pre-release suffix means a final release, which is newer than any pre-release.
        if (preRelease == null && other.preRelease == null) return 0;
        if (preRelease == null) return 1;
        if (other.preRelease == null) return -1;
        return comparePreRelease(preRelease, other.preRelease);
    }

    /** "beta.10" must sort after "beta.9", so numeric identifiers are compared as numbers. */
    private static int comparePreRelease(String left, String right) {
        List<String> a = split(left);
        List<String> b = split(right);
        for (int i = 0; i < Math.max(a.size(), b.size()); i++) {
            if (i >= a.size()) return -1;
            if (i >= b.size()) return 1;
            String x = a.get(i);
            String y = b.get(i);
            boolean xNumber = x.chars().allMatch(Character::isDigit);
            boolean yNumber = y.chars().allMatch(Character::isDigit);
            int diff;
            if (xNumber && yNumber) {
                diff = Integer.compare(Integer.parseInt(x), Integer.parseInt(y));
            } else {
                diff = x.compareTo(y);
            }
            if (diff != 0) return diff;
        }
        return 0;
    }

    private static List<String> split(String value) {
        List<String> parts = new ArrayList<>();
        for (String part : value.split("[.\\-]")) {
            if (!part.isEmpty()) parts.add(part);
        }
        return parts;
    }

    @Override
    public String toString() {
        return numbers[0] + "." + numbers[1] + "." + numbers[2] + (preRelease == null ? "" : "-" + preRelease);
    }
}
