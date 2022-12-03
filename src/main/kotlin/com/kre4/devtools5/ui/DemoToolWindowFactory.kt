package com.kre4.devtools5.ui

import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowFactory
import com.intellij.ui.content.ContentFactory

class DemoToolWindowFactory : ToolWindowFactory {
    override fun createToolWindowContent(project: Project, toolWindow: ToolWindow) {
        val pluginConfigureToolWindow = PluginConfigureToolWindow(toolWindow)
        val contentFactory = ContentFactory.SERVICE.getInstance()
        val content = contentFactory.createContent(pluginConfigureToolWindow.content, "Demo Tool Window", false)
        toolWindow.contentManager.addContent(content)
    }
}