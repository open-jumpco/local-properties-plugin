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

import org.gradle.api.Plugin;
import org.gradle.api.Project;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 *
 */
public class LocalPropertiesPlugin implements Plugin<Project> {
    public void apply(Project project) {
        project.getLogger().info("local-properties:load");
        final Properties properties = new Properties();
        final Map<String, String> actions = new HashMap<>();

        File localGradleProperties = new File(project.getProjectDir(), "gradle.local.properties");
        if (localGradleProperties.exists()) {
            project.getLogger().lifecycle("local-properties:" + localGradleProperties.getName() + ":loading");
            try (FileInputStream fis = new FileInputStream(localGradleProperties)) {
                properties.load(fis);
                for (String key : properties.stringPropertyNames()) {
                    String property = properties.getProperty(key);
                    if (project.hasProperty(key)) {
                        project.getLogger().info("local-properties:set:" + key + "=" + property);
                        actions.put(key, "override");
                    } else {
                        project.getLogger().warn("local-properties:add:" + key + "=" + property);
                        actions.put(key, "added");
                    }
                    project.getExtensions().getExtraProperties().set(key, property);
                }
            } catch (IOException e) {
                project.getLogger().error("local-properties:exception:" + e, e);
            }
        } else {
            project.getLogger().info("local-properties:" + localGradleProperties.getName() + " not found");
        }
        project.getLogger().info("local-properties:loaded");
        project.getTasks().register("listProperties", task -> {
            task.doLast(t -> {
                t.getProject().getLogger().debug("listProperties:start");
                for (Map.Entry<String, String> entry : actions.entrySet()) {
                    t.getProject()
                        .getLogger()
                        .lifecycle("local-properties:list:" + entry.getValue() + ":" + entry.getKey());
                }
                t.getProject().getLogger().debug("listProperties:end");
            });
        });
    }
}
