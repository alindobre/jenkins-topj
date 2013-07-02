/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jenkinsci.topj;

import hudson.EnvVars;
import hudson.Extension;
import hudson.FilePath;
import hudson.Launcher;
import hudson.model.AbstractBuild;
import hudson.model.AbstractProject;
import hudson.model.BuildListener;
import hudson.model.Run;
import hudson.model.TaskListener;
import hudson.model.listeners.SCMPollListener;
import hudson.scm.SCM;
import hudson.tasks.BuildWrapper;
import hudson.tasks.BuildWrapperDescriptor;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.eclipse.jgit.transport.RefSpec;
import org.jenkinsci.plugins.gitclient.Git;
import org.jenkinsci.plugins.gitclient.GitClient;
import org.kohsuke.stapler.DataBoundConstructor;

/**
 *
 * @author amdobre
 */
public class TopJBuildWrapper extends BuildWrapper {

    private EnvVars envs = new EnvVars();
    
    @DataBoundConstructor
    public TopJBuildWrapper() {
    }

    @Override
    public Environment setUp(AbstractBuild build, Launcher launcher, BuildListener listener) throws IOException, InterruptedException {
        return new Environment() {};
    }

    @Override
    public Launcher decorateLauncher(AbstractBuild build, Launcher launcher, BuildListener listener) throws IOException, InterruptedException, Run.RunnerAbortedException {
        FilePath ws = build.getWorkspace();
        String s="";
        if (ws != null) s += ws.getRemote();
        listener.getLogger().println("------------------------------------- TopJBuildWrapper -> decorateLauncher(): "+s);
        return launcher;
    }

    @Override
    public void preCheckout(AbstractBuild build, Launcher launcher, BuildListener listener) throws IOException, InterruptedException {
        try {
            envs = build.getEnvironment(listener);
        } catch (IOException ex) {
            Logger.getLogger(TopJShell.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InterruptedException ex) {
            Logger.getLogger(TopJShell.class.getName()).log(Level.SEVERE, null, ex);
        }

        File jrt = new File(build.getWorkspace().getRemote(), ".jruntime-"+build.getNumber());
        jrt.mkdirs();
        File jdir = new File(jrt, "jenkins");
        File jscripts = new File(jdir, "scripts");
        
        envs.put("JRT", jrt.getAbsolutePath());
        envs.put("JDIR", jdir.getAbsolutePath());
        envs.put("JENKINS_SCRIPTS_PATH", jscripts.getAbsolutePath());
        envs.put("JS", jscripts.getAbsolutePath());
        envs.put("PATH+J", new File(jdir, "bin").getAbsolutePath());
        envs.put("J", new File(jscripts, "j").getAbsolutePath());
        build.getActions().add(new TopJShellAction(envs));

        SCM scm = build.getProject().getScm();
        if (scm != null) {
            scm.buildEnvVars(build, envs);
            listener.getLogger().println("------------------------------------- TopJBuildWrapper -> preCheckout(): INSIDE SCM");
            listener.getLogger().println("------------------------------------- TopJBuildWrapper -> preCheckout(): INSIDE SCM");
        }
        listener.getLogger().println("------------------------------------- TopJBuildWrapper -> preCheckout()");
    }

    /*
    @Override
    public void makeBuildVariables(AbstractBuild build, Map<String, String> variables) {
        variables.put("OOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO", new File(build.getWorkspace().getRemote(), ".jruntime-"+build.getNumber()).getAbsolutePath());
    }
    */
    
    @Extension
    public static class DescriptorImpl extends BuildWrapperDescriptor {

        @Override
        public boolean isApplicable(AbstractProject<?, ?> ap) {
            return true;
        }

        @Override
        public String getDisplayName() {
            return "J Script";
        }
    }
    
}
