/*
 * Apache License
 * Version 2.0, January 2004
 * http://www.apache.org/licenses/
 *
 * Copyright 2008-2010 by chenillekit.org
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 */

package org.chenillekit.tapestry.core.components;

import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.annotations.AfterRender;
import org.apache.tapestry5.annotations.AfterRenderBody;
import org.apache.tapestry5.annotations.BeforeRenderBody;
import org.apache.tapestry5.annotations.Environmental;
import org.apache.tapestry5.json.JSONObject;
import org.apache.tapestry5.services.javascript.JavaScriptSupport;
import org.chenillekit.tapestry.core.base.AbstractWindow;

/**
 * creates a window based on jvascript <a href="http://prototype-window.xilinus.com/">window</a> library.
 *
 * @version $Id$
 */
public class Window extends AbstractWindow
{
    @Environmental
    private JavaScriptSupport javascriptSupport;

    private boolean hasBody = false;

    /**
     * Tapestry render phase method.
     * Called before component body is rendered.
     *
     * @param writer the markup writer
     */

    private String contentDivId;

    @BeforeRenderBody
    void beforeRenderBody(MarkupWriter writer)
    {
        contentDivId = javascriptSupport.allocateClientId(getClientId() + "Content");
        hasBody = true;
        writer.element("div",
                       "id", contentDivId,
                       "style", "display:none;");
    }

    /**
     * Tapestry render phase method.
     * Called after component body is rendered.
     * return false to render body again.
     *
     * @param writer the markup writer
     */
    @AfterRenderBody
    void afterRenderBody(MarkupWriter writer)
    {
        writer.end();
    }


    /**
     * Tapestry render phase method. End a tag here.
     *
     * @param writer the markup writer
     */
    @AfterRender
    void afterRender(MarkupWriter writer)
    {
        JSONObject options = new JSONObject();

        options.put("className", getClassName());
        options.put("width", getWidth());
        options.put("height", getHeight());
        options.put("id", getClientId());
        options.put("title", getTitle());

        //
        // Let subclasses do more.
        //
        configure(options);

        JSONObject ckOptions = new JSONObject();
        ckOptions.put("windowoptions", options);
        ckOptions.put("hasbody", hasBody);
        ckOptions.put("show", isShow());
        ckOptions.put("center", isCenter());
        ckOptions.put("modal", isModal());
        ckOptions.put("clientid", getClientId());
        ckOptions.put("contentid", contentDivId);

        javascriptSupport.addInitializerCall("ckwindow", ckOptions);
    }
}