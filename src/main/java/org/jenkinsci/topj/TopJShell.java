/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jenkinsci.topj;

import hudson.EnvVars;
import hudson.Extension;
import hudson.FilePath;
import hudson.model.Build;
import hudson.model.BuildListener;
import hudson.plugins.git.GitSCM;
import hudson.tasks.Builder;
import hudson.tasks.Shell;
import java.io.File;
import java.io.IOException;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.sf.json.JSONObject;
import org.eclipse.jgit.transport.RefSpec;
import org.jenkinsci.plugins.gitclient.Git;
import org.jenkinsci.plugins.gitclient.GitClient;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.StaplerRequest;

/**
 *
 * @author amdobre
 */
public class TopJShell extends Shell {
    @DataBoundConstructor
    public TopJShell(String command) {
        super(command);
    }

    public void print_env(EnvVars env, BuildListener listener) {
        listener.getLogger().println("=================================================");
        final Set<String> keys = env.keySet();
    	for (String key : keys) {
    		listener.getLogger().println("Key : " + key + " = " +env.get(key));
    	}
        listener.getLogger().println("=================================================");
    }
    
    @Override
    public boolean prebuild(Build build, BuildListener listener) {
        /*
        listener.getLogger().println("=================== Preparing J Script ===================");
        EnvVars envs = new EnvVars();
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
        
        GitClient git = Git.with(listener, envs).using("git").in(jdir).getClient();
        git.clone("ssh://git@github.com:alindobre/alindobre-scripts.git", "scripts-origin", true, null);
        RefSpec ref = new RefSpec("refs/heads/master:remotes/scripts-origin/master");
        git.fetch("scripts-origin", ref);
        git.checkout("remotes/scripts-origin/master", "local-scripts-master");
        
        /*GitSCM git = new GitSCM("ssh://git@github.com:alindobre/alindobre-scripts.git");
        try {
            git.checkout(build, null, new FilePath(jdir), listener, jdir);
        } catch (IOException ex) {
            Logger.getLogger(TopJShell.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InterruptedException ex) {
            Logger.getLogger(TopJShell.class.getName()).log(Level.SEVERE, null, ex);
        }
        */
        /*
        build.getActions().add(new TopJShellAction(envs));
        listener.getLogger().println("=================== Done J Script ===================");
        */
        return true;
    }

    @Extension
    public static class DescriptorImpl extends Shell.DescriptorImpl {
        @Override
        public String getDisplayName() {
            return "J Shell";
        }

        @Override
        public Builder newInstance(StaplerRequest sr, JSONObject jsono) {
            return new TopJShell(jsono.getString("command"));
        }
    }
}
