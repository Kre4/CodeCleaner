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

/**
 * Activation via shift+ctrl+alt+DELETE
 */
class SuperCleanerAction : AnAction() {
    override fun actionPerformed(e: AnActionEvent) {
        val editor: Editor = e.getRequiredData(CommonDataKeys.EDITOR)
        val project: Project = e.getRequiredData(CommonDataKeys.PROJECT)
        val caret = editor.getCaretModel().getPrimaryCaret();
        val start= primaryCaret.getSelectionStart()
        val end = primaryCaret.getSelectionEnd()
        var originText: String =
            if (LocalConfiguration.isSelectedTextMode)
                editor.selectionModel.selectedText?:""
            else
                editor.document.text
        val ratio: Float = LocalConfiguration.ratio
        if (ratio > 1.0f || ratio < 0f) {
            Messages.showMessageDialog(
                project, "Ratio should be more than 0 and less then 1.0", "Incorrect input",
                Messages.getInformationIcon()
            );
            return
        }
        if (LocalConfiguration.isFileRowsMode)
            originText = randomFileRowsSelection(originText, ratio)
        if (LocalConfiguration.isFileCharsMode)
            originText = randomFileCharsSelection(originText, ratio)

        this.performTextChange(project, editor.document, originText, LocalConfiguration.undoAvailable)

        Messages.showMessageDialog(
            project, "your file has been murdered :(", "Sorry",
            Messages.getInformationIcon()
        );

    }

    /**
     * @param ratio percent of elements that will be deleted
     */
    private fun randomFileRowsSelection(fileContent: String, ratio: Float): String {
        if (ratio > 1.0f) {
            return fileContent
        }
        val codeRows = fileContent.split(Regex("\n"))
        val randomRange = (codeRows.indices).shuffled().take((codeRows.size * ratio).toInt()).sorted()
        val output = StringBuilder()
        for (i in randomRange) {
            output.append(codeRows[i]).append("\n")
        }
        return output.toString()
    }

    private fun randomFileCharsSelection(fileContent: String, ratio: Float): String {
        val randomRange = (fileContent.indices).shuffled().take((fileContent.length * ratio).toInt()).sorted()
        val output = StringBuilder()
        for (i in randomRange) {
            output.append(fileContent[i]).append("\n")
        }
        return output.toString()
    }


    private fun performTextChange(
        project: Project, document: Document,
        newText: CharSequence, withUndo: Boolean
    ) {
        if (withUndo)
            WriteCommandAction.runWriteCommandAction(project, Runnable {
                document.setText(newText)
                // document.replaceString(start, end, "editor_basics")
            })
        else
            document.setText(newText)
    }
}