import * as vscode from 'vscode';
import * as path from 'path';
import { featureDirectoryPath , setFeatureDirectoryPath, testDirectoryPath, pageDirectoryPath, xmlDirectoryPath } from './global';
import * as fs from 'fs';

export class FeatureProvider implements vscode.TreeDataProvider<Feature>{
    
    private _onDidChangeTreeData: vscode.EventEmitter<Feature | undefined> = new vscode.EventEmitter<Feature | undefined>();

    readonly onDidChangeTreeData: vscode.Event<Feature | undefined> = this._onDidChangeTreeData.event;

    private isConfigDone : boolean = false;

    constructor(private workspaceRoot: string | undefined) {
        const config = vscode.workspace.getConfiguration('spl-at-gherkin-vscode');
        if(!config.get("featurePath")){
            vscode.window.showErrorMessage("Please enter Feature directory path from settings.");
            return;
        }else {
            this.isConfigDone = true;
            setFeatureDirectoryPath(String(config.get("featurePath")));
        }
    }
    

    refresh(): void {
        this._onDidChangeTreeData.fire(undefined);
        vscode.window.showInformationMessage("Feature files are refreshed.");
    } 

    getTreeItem(element: Feature): vscode.TreeItem | Thenable<vscode.TreeItem> {
        return element;
    }

    getChildren(element?: Feature | undefined): vscode.ProviderResult<Feature[]> {
        if(!this.isConfigDone){
            return Promise.resolve([]);
        }
        if(!element){
            return Promise.resolve(this.getFeaturesFromPath());
        }
        return Promise.resolve([]);
    }

    /**
     * Read all feature files that are located in Feature directory from root path.
     */
    private getFeaturesFromPath() : Feature[]{
        if(!fs.existsSync(featureDirectoryPath)){
            vscode.window.showErrorMessage("Feature folder does not exist from given path. Path is " + featureDirectoryPath);
            return [];
        }
        return fs.readdirSync(featureDirectoryPath)
        .filter(file => path.extname(file) === ".feature")
        .map(file => new Feature(file, 
            vscode.TreeItemCollapsibleState.None,
            {
                command: 'spl-at-gherkin-vscode.openEditor',
                title: '',
                arguments: [(featureDirectoryPath + "/" + file)]
            }));
    }

    generateCodeFiles(){
        try{
            var shell = require('shelljs');
            shell.config.execPath = __dirname;
            shell.cd(__dirname);
            shell.cd("..");
            shell.cd("resources/jar");
            let command = "java -cp SPL-AT-Gherkin.jar GherkinParser.MainProgram " + 
            featureDirectoryPath + "/ " + 
            testDirectoryPath + "/ " +
            pageDirectoryPath + "/ " +
            xmlDirectoryPath + "/";

            shell.exec(command, function(code:string, stdout:string, stderr:string) {
                if(stderr){
                    vscode.window.showErrorMessage("Code generation did not occured. " + code);
                    return;
                }
                vscode.window.showInformationMessage("Source code is generated successfullly. Please refresh Code field.");
              });

        }
        catch(error){
            vscode.window.showErrorMessage("Code does not created " + error);
            return;
        }
    }
    
}

export class Feature extends vscode.TreeItem{ 
    constructor(
		public readonly label: string,
        public readonly collapsibleState: vscode.TreeItemCollapsibleState,
        public readonly command?: vscode.Command) {
		super(label, collapsibleState);
    }

    iconPath = {
		light: path.join(__filename, '..', '..', 'resources', 'light', 'cherry.svg'),
		dark: path.join(__filename, '..', '..', 'resources', 'dark', 'cherry.svg')
	};

	contextValue = 'feature';
    
}