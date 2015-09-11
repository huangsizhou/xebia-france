# How To Generate a Self Signed X509 Certificate in Java #

Here is a demo to generate a self signed x509 certificate and associated ".pem" files in Java with [Bouncy Castle](http://www.bouncycastle.org/) library.

# Code Sample #

Source code :
[SelfSignedX509CertificateGeneratorDemo.java](http://code.google.com/p/xebia-france/source/browse/cloudcomputing/xebia-cloudcomputing-extras/trunk/src/test/java/fr/xebia/demo/amazon/aws/SelfSignedX509CertificateGeneratorDemo.java?spec=svn2041&r=2041)
```
import java.security.*;
import java.security.cert.*;
import javax.security.auth.x500.X500Principal;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openssl.PEMWriter;
import org.bouncycastle.x509.X509V1CertificateGenerator;
...

static {
    // adds the Bouncy castle provider to java security
    Security.addProvider(new BouncyCastleProvider());
}

/**
 * Generate a self signed X509 certificate with Bouncy Castle.
 */
static void generateSelfSignedX509Certificate() throws Exception {

    // yesterday
    Date validityBeginDate = new Date(System.currentTimeMillis() - 24 * 60 * 60 * 1000);
    // in 2 years
    Date validityEndDate = new Date(System.currentTimeMillis() + 2 * 365 * 24 * 60 * 60 * 1000);

    // GENERATE THE PUBLIC/PRIVATE RSA KEY PAIR
    KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA", "BC");
    keyPairGenerator.initialize(1024, new SecureRandom());

    KeyPair keyPair = keyPairGenerator.generateKeyPair();

    // GENERATE THE X509 CERTIFICATE
    X509V1CertificateGenerator certGen = new X509V1CertificateGenerator();
    X500Principal dnName = new X500Principal("CN=John Doe");

    certGen.setSerialNumber(BigInteger.valueOf(System.currentTimeMillis()));
    certGen.setSubjectDN(dnName);
    certGen.setIssuerDN(dnName); // use the same
    certGen.setNotBefore(validityBeginDate);
    certGen.setNotAfter(validityEndDate);
    certGen.setPublicKey(keyPair.getPublic());
    certGen.setSignatureAlgorithm("SHA256WithRSAEncryption");

    X509Certificate cert = certGen.generate(keyPair.getPrivate(), "BC");

    // DUMP CERTIFICATE AND KEY PAIR

    System.out.println(Strings.repeat("=", 80));
    System.out.println("CERTIFICATE TO_STRING");
    System.out.println(Strings.repeat("=", 80));
    System.out.println();
    System.out.println(cert);
    System.out.println();

    System.out.println(Strings.repeat("=", 80));
    System.out.println("CERTIFICATE PEM (to store in a cert-johndoe.pem file)");
    System.out.println(Strings.repeat("=", 80));
    System.out.println();
    PEMWriter pemWriter = new PEMWriter(new PrintWriter(System.out));
    pemWriter.writeObject(cert);
    pemWriter.flush();
    System.out.println();

    System.out.println(Strings.repeat("=", 80));
    System.out.println("PRIVATE KEY PEM (to store in a priv-johndoe.pem file)");
    System.out.println(Strings.repeat("=", 80));
    System.out.println();
    pemWriter.writeObject(keyPair.getPrivate());
    pemWriter.flush();
    System.out.println();
}
```