/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jenkinsci.topj;

import hudson.EnvVars;
import hudson.model.AbstractBuild;
import hudson.model.EnvironmentContributingAction;
import hudson.model.InvisibleAction;

/**
 *
 * @author amdobre
 */
public class TopJShellAction extends InvisibleAction implements EnvironmentContributingAction {

    protected final EnvVars envToAdd;
    
    TopJShellAction(EnvVars envs) {
        this.envToAdd = envs;
    }

    public void buildEnvVars(AbstractBuild<?, ?> ab, EnvVars ev) {
        ev.putAll(envToAdd);
    }
    
}
