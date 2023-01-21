import org.junit.jupiter.api.Test;
import org.lwjgl.Version;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SetupTests {
    @Test
    public void testVersion() {
        String[] versionParts = Version.getVersion().split(" ");
        String[] semver = versionParts[0].split("\\.");
        int major = Integer.parseInt(semver[0]);
        int minor = Integer.parseInt(semver[1]);
        int patch = Integer.parseInt(semver[2]);
        int build = Integer.parseInt(versionParts[2]);

        assertEquals(3, major);
        assertEquals(3, minor);
        assertEquals(1, patch);
        assertEquals(7, build);
    }
}
