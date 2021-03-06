/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 1997-2010 Oracle and/or its affiliates. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License.  You can
 * obtain a copy of the License at
 * https://glassfish.java.net/public/CDDL+GPL_1_1.html
 * or packager/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 *
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at packager/legal/LICENSE.txt.
 *
 * GPL Classpath Exception:
 * Oracle designates this particular file as subject to the "Classpath"
 * exception as provided by Oracle in the GPL Version 2 section of the License
 * file that accompanied this code.
 *
 * Modifications:
 * If applicable, add the following below the License Header, with the fields
 * enclosed by brackets [] replaced by your own identifying information:
 * "Portions Copyright [year] [name of copyright owner]"
 *
 * Contributor(s):
 * If you wish your version of this file to be governed by only the CDDL or
 * only the GPL Version 2, indicate your decision by adding "[Contributor]
 * elects to include this software in this distribution under the [CDDL or GPL
 * Version 2] license."  If you don't indicate a single choice of license, a
 * recipient has the option to distribute your version of this file under
 * either the CDDL, the GPL Version 2 or to extend the choice of license to
 * its licensees as provided above.  However, if you add GPL Version 2 code
 * and therefore, elected the GPL Version 2 license, then the option applies
 * only if the new code is made subject to such option by the copyright
 * holder.
 */

package com.sun.faces.application;

import com.sun.faces.cactus.ServletFacesTestCase;

import javax.faces.FactoryFinder;
import javax.faces.application.Application;
import javax.faces.application.ApplicationFactory;


/**
 * This class tests the <code>StateManagerImpl</code> class
 * functionality.
 */
public class TestHAStateManagerImpl extends ServletFacesTestCase {

     public static final String TEST_URI = "/test.jsp";
    //
    // Constructors/Initializers
    //
    public TestHAStateManagerImpl() {
        super("TestStateManagerImpl");
    }


    public TestHAStateManagerImpl(String name) {
        super(name);
    }
    
    private Application application = null;
    
    public void setUp() {
        super.setUp();
        ApplicationFactory aFactory =
            (ApplicationFactory) FactoryFinder.getFactory(
                FactoryFinder.APPLICATION_FACTORY);
        application = (Application) aFactory.getApplication();
        application.setViewHandler(new ViewHandlerImpl());
        application.setStateManager(new StateManagerImpl());
    }
    
    //
    // Test Methods
    //
    
    
    public void testHighAvailabilityStateSaving1() {
       
//        // precreate tree and set it in session and make sure the tree is
//        // restored from session.
//        UIViewRoot root = application.getViewHandler().createView(getFacesContext(), null);
//        root.setViewId(TEST_URI);
//
//        UIForm basicForm = new UIForm();
//        basicForm.setId("basicForm");
//        UIInput userName = new UIInput();
//
//        userName.setId("userName");
//        userName.setTransient(true);
//        root.getChildren().add(basicForm);
//        basicForm.getChildren().add(userName);
//
//        UIPanel panel1 = new UIPanel();
//        panel1.setId("panel1");
//        basicForm.getChildren().add(panel1);
//
//        UIInput userName1 = new UIInput();
//        userName1.setId("userName1");
//        panel1.getChildren().add(userName1);
//
//        getFacesContext().setViewRoot(root);
//
//        StateManager stateManager =
//            getFacesContext().getApplication().getStateManager();
//        
//        SerializedView state = stateManager.saveSerializedView(getFacesContext());
//        
//        // make sure that the value of viewId attribute in session is an
//        // instance of SerializedView.
//        Object result = session.getAttribute(TEST_URI);
//        assertTrue(result instanceof SerializedView);
//        
//        root = stateManager.restoreView(getFacesContext(), TEST_URI,
//                                 RenderKitFactory.HTML_BASIC_RENDER_KIT);
//       
//        assertTrue(root != null);
//        basicForm = (UIForm) root.findComponent("basicForm");
//        assertTrue(basicForm != null);
//
//        userName = (UIInput) basicForm.findComponent("userName");
//        assertTrue(userName == null);
//
//        panel1 = (UIPanel) basicForm.findComponent("panel1");
//        assertTrue(panel1 != null);
//
//        userName1 = (UIInput) panel1.findComponent("userName1");
//        assertTrue(userName1 != null);
    }

}
