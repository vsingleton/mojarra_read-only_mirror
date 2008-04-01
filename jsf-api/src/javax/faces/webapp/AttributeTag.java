/*
 * $Id: AttributeTag.java,v 1.2 2002/09/02 21:36:35 craigmcc Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.webapp;


import javax.faces.component.UIComponent;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.Tag;
import javax.servlet.jsp.tagext.TagSupport;


/**
 * <p>Tag implementation that adds an attribute with a specified name
 * and (String) value, if the component does not already contain such
 * an attribute.  This tag creates no output to the page currently
 * being created.</p>
 */

public class AttributeTag extends TagSupport {


    // ------------------------------------------------------------- Attributes


    /**
     * <p>The name of the attribute to be created, if not already present.
     */
    private String name = null;


    /**
     * <p>Set the attribute name.</p>
     *
     * @param name The new attribute name
     */
    public void setName(String name) {

        this.name = name;

    }


    /**
     * <p>The value to be associated with this attribute, if it is created.</p>
     */
    private String value = null;



    /**
     * <p>Set the attribute value.</p>
     *
     * @param value The new attribute value
     */
    public void setValue(String value) {

        this.value = value;

    }


    // --------------------------------------------------------- Public Methods


    /**
     * <p>Register the specified attribute name and value with the
     * {@link UIComponent} instance associated with our most immediately
     * surrounding {@link FacesTag} instance, if this {@link UIComponent}
     * does not already have a value for the specified attribute name.</p>
     *
     * @exception JspException if a JSP error occurs
     */
    public int doStartTag() throws JspException {

        // Locate the appropriate UIComponent instance
        Tag tag = getParent();
        while ((tag != null) && !(tag instanceof FacesTag)) {
            tag = tag.getParent();
        }
        if (tag == null) { // FIXME - i18n
            throw new JspException("Not nested in a FacesTag");
        }

        // Add this attribute if it is not already defined
        UIComponent component = ((FacesTag) tag).getComponent();
        if (component == null) { // FIXME - i18n
            throw new JspException("No component associated with FacesTag");
        }
        if (component.getAttribute(name) == null) {
            component.setAttribute(name, value);
        }
        return (SKIP_BODY);

    }


    /**
     * <p>Release references to any acquired resources.
     */
    public void release() {

        this.name = null;
        this.value = null;

    }


}
