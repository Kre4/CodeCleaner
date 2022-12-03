package com.kre4.devtools5.state

import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage
import com.intellij.util.xmlb.XmlSerializerUtil


@State(
    name = "com.kre4.devtools5.state", storages = [Storage("codeCleaner.xml")]
)
object LocalConfiguration: PersistentStateComponent<LocalConfiguration> {

    var ratio: Float = 0.0f
    var isFileRowsMode = true
    var isFileCharsMode = false
    var isSelectedTextMode = false
    var undoAvailable = true

    override fun getState(): LocalConfiguration {
        return this
    }

    override fun loadState(state: LocalConfiguration) {
        XmlSerializerUtil.copyBean(state, this);
//        this.ratio = state.ratio
//        this.isFileRowsMode = state.isFileRowsMode
//        this.isFileCharsMode = state.isFileCharsMode
//        this.isProjectFilesMode = state.isProjectFilesMode
    }
}