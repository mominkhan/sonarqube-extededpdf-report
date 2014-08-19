/*
 * SonarQube Extended-PDF Report (Sonar Plugin)
 * Copyright (C) 2014 hCentive - Technology Solutions to Simplify Healthcare
 * dev@sonar.codehaus.org
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02
 */
package org.sonarqube.report.extendedpdf.batch;

import org.apache.commons.io.IOUtils;
import org.slf4j.LoggerFactory;
import org.sonar.api.batch.maven.MavenPlugin;
import org.sonar.api.batch.maven.MavenPluginHandler;
import org.sonar.api.resources.Project;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by Momin.Khan
 */
public class ExtendedPDFMavenPluginHandler implements MavenPluginHandler {
    public String getGroupId() {
        return "org.codehaus.sonar-plugins.extendedpdf-report";
    }

    public String getArtifactId() {
        return "sonarqube-extendedpdf-maven-plugin";
    }

    public String getVersion() {
        InputStream input = null;
        try {
            Properties props = new Properties();
            input = this.getClass().getResourceAsStream(
                    "/META-INF/maven/org.codehaus.sonar-plugins.extendedpdf-report/sonarqube-extendedpdf-report-plugin/pom.properties");
            props.load(input);
            return props.getProperty("version");

        } catch (IOException e) {
            LoggerFactory.getLogger(getClass()).error("can not load the plugin version from pom.properties", e);
            return null;

        } finally {
            IOUtils.closeQuietly(input);
        }
    }

    public boolean isFixedVersion() {
        return false;
    }

    public String[] getGoals() {
        return new String[] { "generate" };
    }

    public void configure(Project project, MavenPlugin plugin) {
        plugin.setParameter("reportType", project.getConfiguration().getString(ExtendedPDFPostJob.EXTENDED_REPORT_TYPE,
                ExtendedPDFPostJob.EXTENDED_REPORT_TYPE_DEFAULT_VALUE));
        plugin.setParameter("username", project.getConfiguration().getString(ExtendedPDFPostJob.USERNAME,
                ExtendedPDFPostJob.USERNAME_DEFAULT_VALUE));
        plugin.setParameter("password", project.getConfiguration().getString(ExtendedPDFPostJob.PASSWORD,
                ExtendedPDFPostJob.PASSWORD_DEFAULT_VALUE));
    }
}
