package org.casaburo.utils.topmost;

import java.awt.Frame;
import java.awt.SystemTray;
import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowStateListener;
import java.util.ResourceBundle;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionRegistration;
import org.openide.util.HelpCtx;
import org.openide.util.NbBundle;
import org.openide.util.NbBundle.Messages;
import org.openide.util.NbPreferences;
import org.openide.util.actions.BooleanStateAction;
import org.openide.windows.WindowManager;

@ActionID(
        category = "View",
        id = "org.casaburo.utils.topmost.HideToTray")
@ActionRegistration(
        lazy = false,
        iconBase = "org/casaburo/utils/topmost/hide.png",
        displayName = "#CTL_HideToTray")
@ActionReferences({
    @ActionReference(path = "Menu/View", position = 9990),
    @ActionReference(path = "Toolbars/View", position = 100)
})
@Messages("CTL_HideToTray=Hide when minimized")
public final class HideToTray extends BooleanStateAction implements WindowStateListener {

    private static final ResourceBundle bundle = java.util.ResourceBundle.getBundle("org/casaburo/utils/topmost/Bundle");
    private static boolean hide;
    private static final String PREF_HIDETOTRAY ="hideToTray";

    @Override
    public void actionPerformed(ActionEvent e) {
        setBooleanState(!getBooleanState());
        hide = getBooleanState();
        NbPreferences.forModule(this.getClass()).putBoolean(PREF_HIDETOTRAY, getBooleanState());

    }

    @Override
    public String getName() {
        return NbBundle.getMessage(getClass(), "CTL_HideToTry");
    }

    @Override
    protected void initialize() {
        super.initialize();
        if (!SystemTray.isSupported()) {
           setEnabled(false);
           setBooleanState(false);
        }else{
        setBooleanState(NbPreferences.forModule(this.getClass()).getBoolean(PREF_HIDETOTRAY, false));
        WindowManager.getDefault().getMainWindow().addWindowStateListener(this);
    }}

    @Override
    public HelpCtx getHelpCtx() {
        return new HelpCtx("org.casaburo.utils.topmost.about");
    }

    @Override
    protected String iconResource() {
        return bundle.getString("HIDE_ICON_PATH");

    }

    public static boolean isHideToTray() {
        return hide;
    }

    @Override
    public void windowStateChanged(WindowEvent e) {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.

        if (getBooleanState() && (e.getNewState() == Frame.ICONIFIED || e.getNewState() == (Frame.ICONIFIED + Frame.MAXIMIZED_BOTH))) {
            WindowManager.getDefault().getMainWindow().setVisible(false);
        }
    }
}
