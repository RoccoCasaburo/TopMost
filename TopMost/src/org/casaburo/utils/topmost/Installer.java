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
package org.casaburo.utils.topmost;

import java.awt.AWTException;
import java.awt.Frame;
import java.awt.Image;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.net.URL;
import java.util.ResourceBundle;
import javax.swing.ImageIcon;
import org.openide.DialogDisplayer;
import org.openide.NotifyDescriptor;
import org.openide.modules.ModuleInstall;
import org.openide.util.Exceptions;
import org.openide.windows.WindowManager;

/**
 * Manages a module's lifecycle. Remember that an installer is optional and
 * often not needed at all.
 */
public class Installer extends ModuleInstall  {

    private static final ResourceBundle bundle = ResourceBundle.getBundle("org/casaburo/utils/topmost/Bundle");
    private static Image trayIconImage;
    private static TrayIcon trayIcon;
    private String trayToolTip;

    @Override
    public void restored() {
        if (!Toolkit.getDefaultToolkit().isAlwaysOnTopSupported()) {
            informUnsupported();
        }
        if (SystemTray.isSupported()) {
            WindowManager.getDefault().invokeWhenUIReady(new Runnable() {
                @Override
                public void run() {
                    createTrayIcon();
                }
            });

        }


    }

    private void informUnsupported() {
        NotifyDescriptor.Message message = new NotifyDescriptor.Message(bundle.getString("UNSUPPORTED_INFO"));
        DialogDisplayer.getDefault().notify(message);
    }

    private void createTrayIcon() {


        trayIconImage= WindowManager.getDefault().getMainWindow().getIconImage();
        trayToolTip=WindowManager.getDefault().getMainWindow().getTitle();
        try {
            System.out.println("*************aggiungendo l'icona");
            trayIcon = new TrayIcon(trayIconImage);
            trayIcon.setImageAutoSize(true);
            trayIcon.setToolTip(trayToolTip);
            
  SystemTray.getSystemTray().add(trayIcon);
  trayIcon.addMouseListener(createMouseListener());

        } catch (AWTException ex) {
            Exceptions.printStackTrace(ex);
        }
    }

    protected static Image createImage(String path, String description) {
        URL imageURL = Installer.class.getResource(path);
        if (imageURL == null) {
            System.err.println("Resource not found: " + path);
            return null;
        } else {
            return (new ImageIcon(imageURL, description)).getImage();
        }
    }

    private MouseListener createMouseListener() {
        MouseListener ml = new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    WindowManager.getDefault().getMainWindow().setVisible(true);
                    WindowManager.getDefault().getMainWindow().setState(Frame.NORMAL);
                }
            }
        };
        return ml;
    }

    @Override
    public void uninstalled() {
        
        super.uninstalled();
        SystemTray.getSystemTray().remove(trayIcon);
        WindowManager.getDefault().getMainWindow().setVisible(true);
    }

   

//    @Override
//    public void propertyChange(PropertyChangeEvent evt) {
//        System.out.println("****************" + evt.getPropertyName());
////        if (HideToTray.getHideToTray()&& evt.getNewValue()==Frame.ICONIFIED) {
//          WindowManager.getDefault().getMainWindow().setVisible(false);
////        }
//    }

 
}
