package com.localpack.bypass;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class LocalPackBypassHelperTest {

    @TempDir
    Path tempDir;

    @Test
    void validatesAndNormalizesSha1Names() {
        String upper = "A9993E364706816ABA3E25717850C26C9CD0D89D";

        assertTrue(LocalPackBypassHelper.isValidSha1(upper));
        assertFalse(LocalPackBypassHelper.isValidSha1("not-a-hash"));
        assertEquals("localpack-a9993e364706816aba3e25717850c26c9cd0d89d.zip",
                LocalPackBypassHelper.fileNameFor(upper));
    }

    @Test
    void findsCanonicalAndRenamedPacksOnlyByTheirBytes() throws Exception {
        Path renamed = tempDir.resolve("server-pack.zip");
        Files.writeString(renamed, "abc");
        String hash = "a9993e364706816aba3e25717850c26c9cd0d89d";

        assertEquals(renamed, LocalPackBypassHelper.findLocalPackByHash(tempDir, hash));
        assertNull(LocalPackBypassHelper.findLocalPackByHash(tempDir,
                "0000000000000000000000000000000000000000"));
    }

    @Test
    void atomicCopyReplacesTheCompleteTarget() throws Exception {
        Path source = tempDir.resolve("source.zip");
        Path target = tempDir.resolve("nested/target.zip");
        Files.writeString(source, "new pack");
        Files.createDirectories(target.getParent());
        Files.writeString(target, "old pack");

        dev.mcchopik.modulib.io.AtomicFiles.copy(source, target);

        assertEquals("new pack", Files.readString(target));
    }
}
