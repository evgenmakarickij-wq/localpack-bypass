package com.localpack.bypass;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Comparison for versions like "0.0.2", "v0.0.2", "1.2.0-beta", "1.2.0-beta.1", "0.0.1.2026-alpha-build".
 *
 * <p>No dependency is pulled in for this: the release tags of this mod are plain dotted numbers
 * with an optional pre-release suffix, and a full semver library would be more code than the
 * comparison itself.
 *
 * <p>Any number of dotted numeric parts is accepted, not just three, because this mod's own tags
 * carry a fourth one naming the build. Comparison walks the parts in order and treats a part that
 * is missing on one side as zero, so "1.2" and "1.2.0.0" are the same version.
 */
public final class ModVersion implements Comparable<ModVersion> {

    private static final Pattern VERSION = Pattern.compile("^v?(\\d+(?:\\.\\d+)*)(?:[-+](.+))?$");

    /** Parsed numeric parts, most significant first. Never empty. */
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

        String[] parts = matcher.group(1).split("\\.", -1);
        int[] numbers = new int[parts.length];
        try {
            for (int i = 0; i < parts.length; i++) {
                numbers[i] = Integer.parseInt(parts[i]);
            }
        } catch (NumberFormatException e) {
            // A part with more digits than an int holds. No release of this mod will ever have one,
            // and "unreadable" is the same answer the old three-part parser gave, so say that.
            return null;
        }
        return new ModVersion(numbers, matcher.group(2));
    }

    /** Null-safe: an unparseable version on either side counts as "not newer". */
    public static boolean isNewer(String candidate, String current) {
        ModVersion a = parse(candidate);
        ModVersion b = parse(current);
        return a != null && b != null && a.compareTo(b) > 0;
    }

    @Override
    public int compareTo(ModVersion other) {
        for (int i = 0; i < Math.max(numbers.length, other.numbers.length); i++) {
            int diff = Integer.compare(numberAt(numbers, i), numberAt(other.numbers, i));
            if (diff != 0) return diff;
        }
        // No pre-release suffix means a final release, which is newer than any pre-release.
        if (preRelease == null && other.preRelease == null) return 0;
        if (preRelease == null) return 1;
        if (other.preRelease == null) return -1;
        return comparePreRelease(preRelease, other.preRelease);
    }

    private static int numberAt(int[] numbers, int index) {
        return index < numbers.length ? numbers[index] : 0;
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
            // Numeric identifiers compare as numbers ("beta.10" after "beta.9"); anything else
            // compares as text, including a digit run too long for an int.
            Long xNumber = asNumber(x);
            Long yNumber = asNumber(y);
            int diff = xNumber != null && yNumber != null
                    ? Long.compare(xNumber, yNumber)
                    : x.compareTo(y);
            if (diff != 0) return diff;
        }
        return 0;
    }

    private static Long asNumber(String value) {
        if (value.isEmpty() || !value.chars().allMatch(Character::isDigit)) return null;
        try {
            return Long.parseLong(value);
        } catch (NumberFormatException e) {
            return null;
        }
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
        StringBuilder sb = new StringBuilder();
        for (int number : numbers) {
            if (sb.length() > 0) sb.append('.');
            sb.append(number);
        }
        return preRelease == null ? sb.toString() : sb + "-" + preRelease;
    }
}
