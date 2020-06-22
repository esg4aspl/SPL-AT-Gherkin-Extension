import * as vscode from 'vscode';
import {featureFileRegex} from './global';
import * as fs from 'fs';

/**
 * Shows an input box using window.showInputBox().
 */
export async function createNewFeatureFromInputBox(config : vscode.WorkspaceConfiguration) {
	const result = await vscode.window.showInputBox({
		value: '',
		placeHolder: 'Please enter feature',
		validateInput: text => {
            return text === null ? text : null;
		}
    });
    if(!result || result.match(featureFileRegex)){
        vscode.window.showErrorMessage("Please enter valid feature name.");
    }else{
		fs.writeFile(config.get("featurePath") + "/" + result + ".feature", Buffer.from(""), (err) => {
			// throws an error, you could also catch it here
			if (err) {
				vscode.window.showErrorMessage("Cannot create feature file. " + err);
			}
			vscode.window.showInformationMessage("Feature file is created successfully.");
			//refresh feature explorer.
			vscode.commands.executeCommand('feature-explorer.refresh');
		});
    }
}


