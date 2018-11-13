// <editor-fold defaultstate="collapsed" desc="license">
/*
 *  Copyright 2010 Rocco Casaburo.
 *  mail address: rcp.nbm.casaburo (at) gmail.com
 *  Visit projects homepage at http://sites.google.com/site/nbmprojects
 *
 *  Licensed under the GNU General Public License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *       http://www.gnu.org/licenses/gpl-2.0.html
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *  under the License.
 */
// </editor-fold>
//$Id: TopMostAction.java 114 2010-04-28 11:33:56Z casaburo.rocco $
package org.casaburo.utils.topmost;

import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.openide.util.HelpCtx;
import org.openide.util.NbBundle;
import org.openide.util.NbPreferences;
import org.openide.util.actions.BooleanStateAction;
import org.openide.windows.WindowManager;

/**
 *
 * @author Rocco.Casaburo
 */
public final class TopMostAction extends BooleanStateAction implements PropertyChangeListener {

    private static final Logger logger = Logger.getLogger(TopMostAction.class.getName());
    private static final ResourceBundle bundle = java.util.ResourceBundle.getBundle("org/casaburo/utils/topmost/Bundle");
    private static final String PREF_ALWAYSONTOP = "alwaysOnTop";

    @Override
    protected void initialize() {
        super.initialize();
        if (!Toolkit.getDefaultToolkit().isAlwaysOnTopSupported()) {
            setEnabled(false);
        } else {
            WindowManager.getDefault().getMainWindow().addPropertyChangeListener(this);
            setBooleanState(NbPreferences.forModule(this.getClass()).getBoolean(PREF_ALWAYSONTOP, false));
            WindowManager.getDefault().getMainWindow().setAlwaysOnTop(getBooleanState());
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        boolean status = WindowManager.getDefault().getMainWindow().isAlwaysOnTop();
        WindowManager.getDefault().getMainWindow().setAlwaysOnTop(!status);
        NbPreferences.forModule(this.getClass()).putBoolean(PREF_ALWAYSONTOP, getBooleanState());
    }

    @Override
    public String getName() {
        return NbBundle.getMessage(getClass(), "CTL_TopMostAction");
    }

    @Override
    public HelpCtx getHelpCtx() {
        return new HelpCtx("org.casaburo.utils.topmost.about");
    }

    @Override
    protected String iconResource() {
        return bundle.getString("ICON_PATH");

    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals("alwaysOnTop")) {
            this.setBooleanState(WindowManager.getDefault().getMainWindow().isAlwaysOnTop());
            logger.log(Level.INFO, bundle.getString("TOPMOST_LOGMESSAGE"), new Object[]{getBooleanState(), new Date()});
        }
    }
}
