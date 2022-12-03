package com.kre4.devtools5.ui
import com.intellij.openapi.wm.ToolWindow
import com.kre4.devtools5.state.LocalConfiguration
import javax.swing.*

class PluginConfigureToolWindow(toolWindow: ToolWindow?) {
    private var panel: JPanel? = null
    private var ratioLabel: JLabel? = null
    private var modeLabel: JLabel? = null
    private var fileRows: JCheckBox? = null
    private var selectedText: JCheckBox? = null
    private var fileChars: JCheckBox? = null
    private var undoAvailable: JCheckBox? = null
    private var ratioInput: JTextField? = null
    private var refreshButton: JButton? = null

    init {
        val localConfig = LocalConfiguration
//        val localConfiguration =  ServiceManager.getService(LocalConfiguration.javaClass)
//        print(localConfiguration.isFileCharsMode)
//        ComponentManager.getComponent(LocalConfiguration.javaClass)
        refreshButton?.addActionListener {
            try {
                localConfig.state.ratio = if (ratioInput?.text.isNullOrEmpty()) 0.5f else ratioInput?.text!!.toFloat()
            } catch (exception: NumberFormatException) {
                localConfig.state.ratio = 0.5f
            }
            localConfig.state.isSelectedTextMode = selectedText?.isSelected ?: false
            localConfig.state.isFileRowsMode = fileRows?.isSelected ?: false
            localConfig.state.isFileCharsMode = fileChars?.isSelected ?: false
            localConfig.state.undoAvailable = undoAvailable?.isSelected ?: true
            localConfig.loadState(localConfig)
        }
    }

    val content: JComponent?
        get() = panel
}