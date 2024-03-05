package com.github.louislou2.jprotobuf.service;

import com.intellij.codeInsight.daemon.DaemonCodeAnalyzer;
import com.intellij.codeInsight.daemon.impl.HighlightInfo;
import com.intellij.codeInsight.daemon.impl.HighlightingSession;
import com.intellij.lang.annotation.HighlightSeverity;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiDocumentManager;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiErrorElement;
import com.intellij.psi.PsiFile;

import java.util.List;

public class CodeAnalyser {
    public static boolean hasError(PsiFile file){
        Project project = file.getProject();
        PsiDocumentManager documentManager = PsiDocumentManager.getInstance(project);
        Document document = documentManager.getDocument(file);
        DaemonCodeAnalyzer daemonCodeAnalyzer = DaemonCodeAnalyzer.getInstance(project);
        daemonCodeAnalyzer.restart(file);
        PsiElement[] children = file.getChildren();
        for (PsiElement child : children) {
            if (child instanceof PsiErrorElement) {
                return true;
            }
        }
        return false;
    }
}
