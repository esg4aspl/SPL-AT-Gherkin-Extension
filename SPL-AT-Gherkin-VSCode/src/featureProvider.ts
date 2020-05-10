import * as vscode from 'vscode';
import * as path from 'path';

export class FeatureProvider implements vscode.TreeDataProvider<Feature>{
    
    onDidChangeTreeData?: vscode.Event<Feature | null | undefined> | undefined;

    getTreeItem(element: Feature): vscode.TreeItem | Thenable<vscode.TreeItem> {
        return element;
    }

    getChildren(element?: Feature | undefined): vscode.ProviderResult<Feature[]> {
        console.log("Get children for Feature.");
        if(!element){
            let array : Feature[] = new Array(10);
            for (let index = 0; index < array.length; index++) {
                array[index] = new Feature(index + "sample.feature", vscode.TreeItemCollapsibleState.None);
            }
            return array;
        }
        return Promise.resolve([]);
    }
    
}

export class Feature extends vscode.TreeItem{ 
    constructor(
		public readonly label: string,
		public readonly collapsibleState: vscode.TreeItemCollapsibleState) {
		super(label, collapsibleState);
    }

    iconPath = {
		light: path.join(__filename, '..', '..', 'resources', 'light', 'file_type_cucumber.svg'),
		dark: path.join(__filename, '..', '..', 'resources', 'dark', 'file_type_cucumber.svg')
	};

	contextValue = 'feature';
    
}