import * as vscode from 'vscode';

export class FeatureEditorProvider implements vscode.CustomTextEditorProvider {

    private static readonly viewType = "splgherkin.splGherkinFeaturePreview";

    constructor(
		private readonly context: vscode.ExtensionContext
	) { }

    resolveCustomTextEditor(document: vscode.TextDocument, webviewPanel: vscode.WebviewPanel, token: vscode.CancellationToken): void | Thenable<void> {
        console.log(document);
        console.log(webviewPanel);
        console.log(token);
        //throw new Error("Method not implemented.");
    }

    public static register(context: vscode.ExtensionContext): vscode.Disposable { 
		const provider = new FeatureEditorProvider(context);
		const providerRegistration = vscode.window.registerCustomEditorProvider(FeatureEditorProvider.viewType, provider);
		return providerRegistration;
	}
    
}