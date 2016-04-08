package net.sf.aidl2;

import com.google.testing.compile.JavaFileObjects;

import net.sf.aidl2.internal.AidlProcessor;
import net.sf.aidl2.internal.Config;

import org.junit.Test;

import javax.tools.JavaFileObject;

import static com.google.common.truth.Truth.assertAbout;
import static com.google.testing.compile.JavaSourceSubjectFactory.javaSource;

public class ErrorTests {
    @Test
    public void failsWhenAidlMethodDeclaresNoRemoteException() {
        JavaFileObject testSource = JavaFileObjects.forResource(IntTests.class.getResource("NoExceptionFailure.java"));

        assertAbout(javaSource()).that(testSource)
                .processedWith(new AidlProcessor())
                .failsToCompile()
                .withErrorCount(2)
                .withErrorContaining("RemoteException")
                .in(testSource)
                .onLine(7)
                .and()
                .withErrorContaining("bailing out");
    }

    @Test
    public void failsWhenAidlDoesNotImplementIInterface() {
        JavaFileObject testSource = JavaFileObjects.forResource(IntTests.class.getResource("NotIInterfaceFailure.java"));

        assertAbout(javaSource()).that(testSource)
                .processedWith(new AidlProcessor())
                .failsToCompile()
                .withErrorCount(2)
                .withErrorContaining("IInterface")
                .in(testSource)
                .onLine(3)
                .and()
                .withErrorContaining("bailing out");
    }
}