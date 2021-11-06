/*
    Copyright 2021 Open JumpCO

    Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated
    documentation files (the "Software"), to deal in the Software without restriction, including without limitation
    the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software,
    and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

    The above copyright notice and this permission notice shall be included in all copies or substantial
    portions of the Software.

    THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO
    THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
    AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF
    CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER
    DEALINGS IN THE SOFTWARE.
 */
package io.jumpco.open.gradle;

import static org.assertj.core.api.Assertions.*;
import org.gradle.testkit.runner.BuildResult;
import org.gradle.testkit.runner.GradleRunner;
import org.junit.jupiter.api.Test;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;

/**
 * A simple functional test for the 'io.jumpco.open.greeting' plugin.
 */
class LocalPropertiesPluginPluginFunctionalTest {
    @Test
    void overAndListPropertiesTask() throws IOException {
        // Setup the test build
        File projectDir = new File("build/functionalTest");
        Files.createDirectories(projectDir.toPath());
        writeString(new File(projectDir, "settings.gradle"), "");
        writeString(new File(projectDir, "build.gradle"),
            "plugins { id('io.jumpco.open.gradle.local-properties') }");
        writeString(new File(projectDir, "gradle.properties"), "abc=xyz\n");
        writeString(new File(projectDir, "gradle.local.properties"), "abc=def\nghi=123");

        // Run the build
        GradleRunner runner = GradleRunner.create();
        runner.forwardOutput();
        runner.withPluginClasspath();
        runner.withArguments("--info", "tasks", "listProperties");
        runner.withProjectDir(projectDir);
        BuildResult result = runner.build();
        String output = result.getOutput();
        assertThat(output).contains("listProperties");
        assertThat(output).contains("set:abc=def");
        assertThat(output).contains("add:ghi=123");
    }

    private void writeString(File file, String string) throws IOException {
        System.out.println("creating:" + file.getAbsolutePath());
        try (Writer writer = new FileWriter(file)) {
            writer.write(string);
        }
    }
}
