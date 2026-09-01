package com.localpack.bypass;

import net.minecraft.client.Minecraft;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public final class LocalPackBypassHelper {

    private LocalPackBypassHelper() {}

    public static File findLocalPackByHash(String targetHash) {
        if (targetHash == null || targetHash.isEmpty()) return null;

        Path dirPath = Minecraft.getInstance().getResourcePackDirectory();
        File dir = dirPath.toFile();
        if (!dir.exists()) return null;

        File[] files = dir.listFiles((d, name) -> name.endsWith(".zip"));
        if (files == null) return null;

        for (File file : files) {
            String sha1 = getSha1(file);
            if (!sha1.isEmpty() && sha1.equalsIgnoreCase(targetHash)) {
                return file;
            }
        }
        return null;
    }

    public static String getSha1(File file) {
        try (FileInputStream fis = new FileInputStream(file)) {
            MessageDigest digest = MessageDigest.getInstance("SHA-1");
            byte[] byteArray = new byte[8192];
            int bytesCount;
            while ((bytesCount = fis.read(byteArray)) != -1) {
                digest.update(byteArray, 0, bytesCount);
            }
            byte[] bytes = digest.digest();
            StringBuilder sb = new StringBuilder();
            for (byte b : bytes) {
                sb.append(Integer.toString((b & 0xff) + 0x100, 16).substring(1));
            }
            return sb.toString();
        } catch (IOException | NoSuchAlgorithmException e) {
            return "";
        }
    }
}
