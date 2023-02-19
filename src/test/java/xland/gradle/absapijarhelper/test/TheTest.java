package xland.gradle.absapijarhelper.test;

import xland.gradle.absapijarhelper.ApiJarRun;

import java.io.File;

public class TheTest {
    public static void main(String[] args) {
        File in = new File("/tmp/abs-api-input.jar"), output = new File("/tmp/abs-api-output.jar");

        ApiJarRun program = new ApiJarRun();
        program.accept(in, output);
    }
}
