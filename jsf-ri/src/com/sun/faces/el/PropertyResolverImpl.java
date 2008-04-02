/*
 * $Id: PropertyResolverImpl.java,v 1.18 2005/05/20 14:49:59 rlubke Exp $
 */

/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.faces.el;

import java.lang.reflect.Array;
import java.util.List;

import javax.el.ELException;
import javax.el.ELResolver;
import javax.faces.context.FacesContext;
import javax.faces.el.EvaluationException;
import javax.faces.el.PropertyNotFoundException;
import javax.faces.el.PropertyResolver;

import com.sun.faces.util.Util;

/**
 * <p>
 * Concrete implementation of <code>PropertyResolver</code>.
 * </p>
 */

public class PropertyResolverImpl extends PropertyResolver {
    
    private ELResolver elResolver = null;

    public PropertyResolverImpl(ELResolver resolver ) {
        this.elResolver = resolver;
    }


    // Specified by javax.faces.el.PropertyResolver.getType(Object,int)
    public Class getType(Object base, int index)  
        throws EvaluationException, PropertyNotFoundException{
        // validates base != null and index >= 0
        assertInput(base, index);

        Class type = base.getClass();
        try {
            if (type.isArray()) {
                Array.get(base, index);
                return type.getComponentType();
            } else if (base instanceof List) {
                Object value = ((List) base).get(index);
                return (value != null) ? value.getClass() : null;
            } else {
                throw new PropertyNotFoundException(Util.getExceptionMessageString(
                        Util.EL_PROPERTY_TYPE_ERROR_ID,
                        new Object[]{base}));
            }
        } catch (ArrayIndexOutOfBoundsException aioobe) {
            throw new PropertyNotFoundException(Util.getExceptionMessageString(
                        Util.EL_SIZE_OUT_OF_BOUNDS_ERROR_ID,
                        new Object[]{base,new Integer(index), 
                                new Integer(Array.getLength(base))}));
        } catch (IndexOutOfBoundsException ioobe) {
           throw new PropertyNotFoundException(Util.getExceptionMessageString(
                        Util.EL_SIZE_OUT_OF_BOUNDS_ERROR_ID,
                        new Object[]{base,new Integer(index), 
                                new Integer(((List)base).size())}));
        }
    }

    // Specified by javax.faces.el.PropertyResolver.getType(Object,String)
    public Class getType(Object base, Object property) {
        assertInput(base, property);
        Class result = null;
        try {
            FacesContext context = FacesContext.getCurrentInstance();
            result = elResolver.getType(context.getELContext(), base,property);    
        } catch (javax.el.PropertyNotFoundException pnfe) {
            throw new PropertyNotFoundException(pnfe);
        } catch (ELException elex) {
            throw new EvaluationException(elex);
        }
        return result;
    }

    // Specified by javax.faces.el.PropertyResolver.getValue(Object,int)
    public Object getValue(Object base, int index) {
        // validates base and index
        if (base == null) {
            return null;
        }

        if (base.getClass().isArray()) {
            try {
                return Array.get(base, index);
            } catch (ArrayIndexOutOfBoundsException aioobe) {
                return null;
            }
        } else if (base instanceof List) {
            try {
                return ((List) base).get(index);
            } catch (IndexOutOfBoundsException ioobe) {
                return null;
            }
        } else {
            throw new PropertyNotFoundException(Util.getExceptionMessageString(
                        Util.EL_PROPERTY_TYPE_ERROR_ID,
                        new Object[]{base}));
        }
        
    }

    // Specified by javax.faces.el.PropertyResolver.getValue(Object,String)
    public Object getValue(Object base, Object property) {
        Object result = null;
        try {
            FacesContext context = FacesContext.getCurrentInstance();
            result = elResolver.getValue(context.getELContext(), base,property);    
        } catch (javax.el.PropertyNotFoundException pnfe) {
            throw new PropertyNotFoundException(pnfe);
        } catch (ELException elex) {
            throw new EvaluationException(elex);
        }  
        return result;
    }

    // Specified by javax.faces.el.PropertyResolver.isReadOnly(Object,int)
    public boolean isReadOnly(Object base, int index) {
        // validate input
        assertInput(base, index);
        
        if (base instanceof List || base.getClass().isArray()) {
            return false;
        } else {
            throw new PropertyNotFoundException(Util.getExceptionMessageString(
                        Util.EL_PROPERTY_TYPE_ERROR_ID,
                        new Object[]{base}));
        }  
    }

    // Specified by javax.faces.el.PropertyResolver.isReadOnly(Object,String)
    public boolean isReadOnly(Object base, Object property) {
        boolean result = false;
        try {
            FacesContext context = FacesContext.getCurrentInstance();
            result = elResolver.isReadOnly(context.getELContext(), base,property);    
        } catch (ELException elex) {
            throw new EvaluationException(elex);
        }   
        return result;
    }

    // Specified by javax.faces.el.PropertyResolver.setValue(Object,int,Object)
    public void setValue(Object base, int index, Object value) {
        
        // validate input
        assertInput(base, index);
        FacesContext context = FacesContext.getCurrentInstance();
        Class type = base.getClass();
        if (type.isArray()) {
            try {
                Array.set(base, index, (context.getApplication().
                    getExpressionFactory()).coerceToType(value, type
                        .getComponentType()));
            }
            catch (ArrayIndexOutOfBoundsException aioobe) {
                throw new PropertyNotFoundException(Util.getExceptionMessageString(
                        Util.EL_SIZE_OUT_OF_BOUNDS_ERROR_ID,
                        new Object[]{base,new Integer(index), 
                                new Integer(Array.getLength(base))}));
            }
        } else if (base instanceof List) {
            try {
                ((List) base).set(index, value);
            } catch (IndexOutOfBoundsException ioobe) {
                throw new PropertyNotFoundException(Util.getExceptionMessageString(
                        Util.EL_SIZE_OUT_OF_BOUNDS_ERROR_ID,
                        new Object[]{base,new Integer(index), 
                                new Integer(((List)base).size())}));
            }
        } else {
           throw new PropertyNotFoundException(Util.getExceptionMessageString(
                        Util.EL_PROPERTY_TYPE_ERROR_ID,
                        new Object[]{base}));
        }
    }

    // Specified by
    // javax.faces.el.PropertyResolver.setValue(Object,String,Object)
    public void setValue(Object base, Object property, Object value) {
        try {
            FacesContext context = FacesContext.getCurrentInstance();
            elResolver.setValue(context.getELContext(), base,property, value);    
        } catch (javax.el.PropertyNotFoundException pnfe) {
            throw new PropertyNotFoundException(pnfe);
        } catch (javax.el.PropertyNotWritableException pnwe) {
            throw new PropertyNotFoundException(pnwe);
        }
        catch (ELException elex) {
            throw new EvaluationException(elex);
        }  
    }
    
    protected static void assertInput(Object base, Object property)
            throws PropertyNotFoundException {
        if (base == null || property == null) {
            String message = Util.getExceptionMessageString
                (Util.NULL_PARAMETERS_ERROR_MESSAGE_ID);
            message = message + " base " + base + " property " + property;
            throw new PropertyNotFoundException(message);
        }
    }
    
    protected static void assertInput(Object base, int index)
            throws PropertyNotFoundException {
        if (base == null) {
            String message = Util.getExceptionMessageString
                (Util.NULL_PARAMETERS_ERROR_MESSAGE_ID);
            message = message + " base " + base;
            throw new PropertyNotFoundException(message);
        }
        if (index < 0) {
            throw new PropertyNotFoundException(Util.getExceptionMessageString(
                        Util.EL_OUT_OF_BOUNDS_ERROR_ID,
                        new Object[]{base, new Integer(index)}));
        }
    }
}
