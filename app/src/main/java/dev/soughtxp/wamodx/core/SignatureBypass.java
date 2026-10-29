package dev.soughtxp.wamodx.core;

import android.content.pm.Signature;
import android.util.Base64;

import java.security.SecureRandom;

import dev.soughtxp.wamodx.util.PrefsUtils;

public class SignatureBypass {

    private static final String OFFICIAL_SIGNATURE_BASE64 =
        "MIIFUzCCAzsCFFWNE/sV9MNgOdKRq10LzcvzVmeoMA0GCSqGSIb3DQEBCwUAMGUxCzAJBgNVBAYTAlVTMRMwEQYDVQQIDApDYWxpZm9ybmlhMRMwEQYDVQQHDApNZW5sbyBQYXJrMRUwEwYDVQQKDAxXaGF0c0FwcCBMTEMxFTATBgNVBAMMDFdoYXRzQXBwIExMQzAgFw0yNDA4MDIyMTQ0MjNaGA8yMDU0MDgwMjIxNDQyM1owZTELMAkGA1UEBhMCVVMxEzARBgNVBAgMCkNhbGlmb3JuaWExEzARBgNVBAcMCk1lbmxvIFBhcmsxFTATBgNVBAoMDFdoYXRzQXBwIExMQzEVMBMGA1UEAwwMV2hhdHNBcHAgTExDMIICIjANBgkqhkiG9w0BAQEFAAOCAg8AMIICCgKCAgEAsGJ6puy0UxckLJ0N0I3KEXaN1WTkJ63ksCvrMQPyLhjA0gviAx7FXRR05HpoFrRzFENaf1TloC4lQq8OP41quttaO3201pfZKllEE9HxZypb5vI635NtnncAECkaL1EWMLt7x4Il2pvGHZnpvhEGkRF2HimGZZL0uGqNJmYTJ/X/qePF/jdeqbZi4ULQNWDzSZEp8uVWJMk3K/BX9rLdcYOIJg3hnlIg7IRd4mUtprzr6EArtegdwiZaMHGwayPQF2MidnMXw0MoWsGFMYGnLTRScCXE7W99hYIiYuarJuDKE4ocTPCET63Csb2nJW1OI0MpEOTd8qBh89vSobR6YyvB7Hy+4l90D975ZvXSfGBlSmR3PloaGbqHppMjVn0Pzm7cZXu2F2aMY319bAa1plBxg1lJV7MATDtlHNGUkJkqFpG56DiUfw4wJNGrXpTRKgxnsAJenF5RNLBil+74ErpSOXZcqTLOwizGQTMmS85ttbGf3x/ZF8/yiQXtSAP/hQ6HuloX/2fef1CywMMeumjkVDKQNUGH9rL8WTb1Sf1EGFLwqYV4E+Io6xdufoqBAaqtSoI6pyomMXTNEHqsXo5Gs6dhz9UYCcIibTRsSbm6mV1lbw7d2pr4Hwhh/zeAiTqSIxc9sOcAw3DHaauHH+tlTYXbSCM2RKx5OWOW9j0CAwEAATANBgkqhkiG9w0BAQsFAAOCAgEAQzUkFbNh3w5+UG7fK1s+gAhkeJsBZdRgny0AThdb4AnRikfvQ2rVfAhGFRpzrhUml5Bs0ABoLK2F7NlIHUr3/oWIgVYQtKDZIvY46vAETWjw1tkBQ3f4+zdiQLCTQd/E7awehNV8oQaCtbPPLh8tne4ViG2nAE8K0fhGJoUPb2zbFiX9A9ShGtYQdx0zJc0821w9PQk/YinMRL9C2gB4/V43t9i8NEkAlQYykTYYt0N6DQLPQW0JZ14XZcBsafQo9bP9bsp3ZqACuRRXzi3bJVEvLnkAj3bbPiJQrMwx8onr1TIjGhBZfoVMI4FPwJq5gNvv9bvvA046W8bA/X9KQL/nOFzLyvnZ2xi5i9JHqVKZyAodDp2uaucoNNvSJKnfhCUlqn71itkL+6SNnTGzW55yUvpH1FeDcwyA33x6UhxxOQ2OT294sAYsfD1861VTghHBn2dHG58RWDv7dOwxt//uX1ZBHCmc6CzhjZK2oALaIYahITIIm/sYbVFas7cepfotFnalLrT34/zolxjFXxhqDUKmNE98KbNwMrxdncOyoqrGBGHqbaKNZ6wd47xe3LetMU9u7LSXvVPwYrHPP+Y363QaEu5YPCDsjRG2K59KCIjl9WJR6jHRme+PHQuYJvm0yxNcZL8QBL5oQYi72hNFAFDgSq9gNRsSsmqtnm4=";

    private static final String LEGACY_SIGNATURE_BASE64 =
        "MIIDMjCCAvCgAwIBAgIETCU2pDALBgcqhkjOOAQDBQAwfDELMAkGA1UEBhMCVVMxEzARBgNVBAgTCkNhbGlmb3JuaWExFDASBgNVBAcTC1NhbnRhIENsYXJhMRYwFAYDVQQKEw1XaGF0c0FwcCBJbmMuMRQwEgYDVQQLEwtFbmdpbmVlcmluZzEUMBIGA1UEAxMLQnJpYW4gQWN0b24wHhcNMTAwNjI1MjMwNzE2WhcNNDQwMjE1MjMwNzE2WjB8MQswCQYDVQQGEwJVUzETMBEGA1UECBMKQ2FsaWZvcm5pYTEUMBIGA1UEBxMLU2FudGEgQ2xhcmExFjAUBgNVBAoTDVdoYXRzQXBwIEluYy4xFDASBgNVBAsTC0VuZ2luZWVyaW5nMRQwEgYDVQQDEwtCcmlhbiBBY3RvbjCCAbgwggEsBgcqhkjOOAQBMIIBHwKBgQD9f1OBHXUSKVLfSpwu7OTn9hG3UjzvRADDHj+AtlEmaUVdQCJR+1k9jVj6v8X1ujD2y5tVbNeBO4AdNG/yZmC3a5lQpaSfn+gEexAiwk+7qdf+t8Yb+DtX58aophUPBPuD9tPFHsMCNVQTWhaRMvZ1864rYdcq7/IiAxmd0UgBxwIVAJdgUI8VIwvMspK5gqLrhAvwWBz1AoGBAPfhoIXWmz3ey7yrXDa4V7l5lK+7+jrqgvlXTAs9B4JnUVlXjrrUWU/mcQcQgYC0SRZxI+hMKBYTt88JMozIpuE8FnqLVHyNKOCjrh4rs6Z1kW6jfwv6ITVi8ftiegEkO8yk8b6oUZCJqIPf4VrlnwaSi2ZegHtVJWQBTDv+z0kqA4GFAAKBgQDRGYtLgWh7zyRtQainJfCpiaUbzjJuhMgo4fVWZIvXHaSHBU1t5w//S0lDK2hiqkj8KpMWGywVov9eZxZy37V26dEqr/c2m5qZ0E+ynSu7sqUD7kGx/zeIcGT0H+KAVgkGNQCo5Uc0koLRWYHNtYoIvt5R3X6YZylbPftF/8ayWTALBgcqhkjOOAQDBQADLwAwLAIUAKYCp0d6z4QQdyN74JDfQ2WCyi8CFDUM4CaNB+ceVXdKtOrNTQcc0e+t";

    public static final String OFFICIAL_CLASSES_DEX_MD5_HEX = "c4180c7e1e26a24656838740e5467cda";

    public static String getAndroidId(String original) {
        String id = PrefsUtils.PREFERENCES.getString("android_id", "");
        if (!id.equals("")) {
            return id;
        }
        id = Long.toHexString(new SecureRandom().nextLong());
        PrefsUtils.PREFERENCES.setString("android_id", id);
        return id;
    }

    public static Signature[] getOfficialSignatures() {
        byte[] bytes = Base64.decode(OFFICIAL_SIGNATURE_BASE64, Base64.DEFAULT);
        return new Signature[]{new Signature(bytes)};
    }

    public static Signature[] getSignaturesHistory() {
        byte[] original = Base64.decode(LEGACY_SIGNATURE_BASE64, Base64.DEFAULT);
        byte[] current = Base64.decode(OFFICIAL_SIGNATURE_BASE64, Base64.DEFAULT);
        return new Signature[]{new Signature(original), new Signature(current)};
    }

    public static Signature[] getLegacySignatures() {
        byte[] bytes = Base64.decode(LEGACY_SIGNATURE_BASE64, Base64.DEFAULT);
        return new Signature[]{new Signature(bytes)};
    }

    public static String getPlayStoreInstaller() {
        return "com.android.vending";
    }

    public static String getPackageName() {
        return "com.whatsapp";
    }

    public static byte[] hexToBytes(String hex) {
        int len = hex.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(hex.charAt(i), 16) << 4)
                    + Character.digit(hex.charAt(i + 1), 16));
        }
        return data;
    }

    public static byte[] dexMd5(byte[] original) {
        byte[] official = hexToBytes(OFFICIAL_CLASSES_DEX_MD5_HEX);
        if (official != null && official.length == 16) {
            return official;
        }
        return original;
    }

    public static Signature[] getSignatures(Object packageInfo) {
        return getOfficialSignatures();
    }
}
