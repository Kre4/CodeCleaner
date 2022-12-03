package com.kre4.devtools5.actions

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.command.WriteCommandAction
import com.intellij.openapi.editor.Document
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.Messages
import com.kre4.devtools5.state.LocalConfiguration
import java.lang.AssertionError

/**
 * Activation via shift+ctrl+alt+DELETE
 */
class SuperCleanerAction : AnAction() {
    override fun actionPerformed(e: AnActionEvent) {
        val editor: Editor?
        try {
            editor = e.getRequiredData(CommonDataKeys.EDITOR_EVEN_IF_INACTIVE)
        } catch (exception: AssertionError) {
            return
        }

        val project: Project = e.getRequiredData(CommonDataKeys.PROJECT)

        var originText: String =
            if (LocalConfiguration.isSelectedTextMode)
                editor.selectionModel.selectedText?:""
            else
                editor.document.text
        val ratio: Float = LocalConfiguration.ratio
        if (ratio > 1.0f || ratio < 0f) {
            Messages.showMessageDialog(
                project, "Ratio should be more than 0 and less then 1.0", "Incorrect input",
                Messages.getErrorIcon())
            return
        }
        if (LocalConfiguration.isFileRowsMode)
            originText = randomFileRowsSelection(originText, ratio)
        if (LocalConfiguration.isFileCharsMode)
            originText = randomFileCharsSelection(originText, ratio)

        this.performTextChange(project, editor, originText, LocalConfiguration.undoAvailable)

        Messages.showMessageDialog(
            project, "Your file has been murdered :(", "Sorry",
            Messages.getInformationIcon()
        )

    }

    /**
     * @param ratio percent of elements that will be deleted
     */
    private fun randomFileRowsSelection(fileContent: String, ratio: Float): String {
        val codeRows = fileContent.split(Regex("\n"))
        val randomRange = (codeRows.indices).shuffled().take((codeRows.size * (1f - ratio)).toInt()).sorted()
        val output = StringBuilder()
        for (i in randomRange) {
            output.append(codeRows[i]).append("\n")
        }
        return output.toString()
    }

    /**
     * @param ratio percent of elements that will be deleted
     */
    private fun randomFileCharsSelection(fileContent: String, ratio: Float): String {
        val randomRange = (fileContent.indices).shuffled().take((fileContent.length * (1f - ratio)).toInt()).sorted()
        val output = StringBuilder()
        for (i in randomRange) {
            output.append(fileContent[i])
        }
        return output.toString()
    }


    private fun performTextChange(
        project: Project, editor: Editor,
        newText: CharSequence, withUndo: Boolean) {
        if (withUndo)
            WriteCommandAction.runWriteCommandAction(project) {
                editText(editor, newText)
            }
        else
            editText(editor, newText)
    }

    private fun editText(editor: Editor, newText: CharSequence) {
        if (LocalConfiguration.isSelectedTextMode) {
            val caret = editor.caretModel.primaryCaret
            val start= caret.selectionStart
            val end = caret.selectionEnd
            editor.document.replaceString(start, end, newText)
        } else {
            editor.document.setText(newText)
        }
    }
}