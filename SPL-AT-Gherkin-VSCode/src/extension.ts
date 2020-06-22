// The module 'vscode' contains the VS Code extensibility API
// Import the module and reference it with the alias vscode in your code below
import * as vscode from 'vscode';
import { FeatureProvider } from './featureProvider';
import { CodeProvider } from './codeProvider';
import {createNewFeatureFromInputBox} from './featureInput';

// this method is called when your extension is activated
// your extension is activated the very first time the command is executed
export function activate(context: vscode.ExtensionContext) {

	// Use the console to output diagnostic information (console.log) and errors (console.error)
	// This line of code will only be executed once when your extension is activated
	console.log('Congratulations, your extension "spl-at-gherkin-vscode" is now active!');

	// The command has been defined in the package.json file
	// Now provide the implementation of the command with registerCommand
	// The commandId parameter must match the command field in package.json
	let disposable = vscode.commands.registerCommand('spl-at-gherkin-vscode.createNewFeature', ()=> {
		// The code you place here will be executed every time your command is executed

		// Creates new feature file on Feature directory.
		//vscode.window.showInformationMessage('Feature file is created. Name is ' + file_name);
		createNewFeatureFromInputBox(vscode.workspace.getConfiguration('spl-at-gherkin-vscode'));
	});

	const featureProvider = new FeatureProvider(vscode.workspace.rootPath);
	const codeProvider = new CodeProvider(vscode.workspace.rootPath);

	vscode.window.registerTreeDataProvider("feature-explorer", featureProvider);
	vscode.commands.registerCommand("feature-explorer.refresh", () => featureProvider.refresh());
	vscode.commands.registerCommand("feature-explorer.generateCode", () => featureProvider.generateCodeFiles());

	vscode.window.registerTreeDataProvider("code-explorer", codeProvider);
	vscode.commands.registerCommand("code-explorer.refresh", () => codeProvider.refresh());

	vscode.commands.registerCommand("spl-at-gherkin-vscode.openEditor", filePath => {
		vscode.commands.executeCommand('vscode.open', 
		vscode.Uri.parse(filePath));
	});

	const config = vscode.workspace.getConfiguration('spl-at-gherkin-vscode');
	
	context.subscriptions.push(disposable);

}

// this method is called when your extension is deactivated
export function deactivate() {}
