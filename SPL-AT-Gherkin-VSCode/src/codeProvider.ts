import * as vscode from 'vscode';
import * as path from 'path';
import { isMavenProject } from './utilities';
import * as fs from 'fs';
import { setPageDirectoryPath, setTestDirectoryPath, setXMLDirectoryPath, pageDirectoryPath, testDirectoryPath, xmlDirectoryPath } from './global';

export class CodeProvider implements vscode.TreeDataProvider<Code>{
    
    private _onDidChangeTreeData: vscode.EventEmitter<Code | undefined> = new vscode.EventEmitter<Code | undefined>();

    readonly onDidChangeTreeData: vscode.Event<Code | undefined> = this._onDidChangeTreeData.event;

    refresh(): void {
        this._onDidChangeTreeData.fire(undefined);
        vscode.window.showInformationMessage("Code files are refreshed.");
    } 

    constructor(private workspaceRoot: string | undefined){
        if(isMavenProject(workspaceRoot)){

        }
        else {
            setPageDirectoryPath(this.workspaceRoot + "/" + "Page");
            setTestDirectoryPath(this.workspaceRoot + "/" + "Test");
            setXMLDirectoryPath(this.workspaceRoot + "/" + "XML");
        }
        
    }

    getTreeItem(element: Code): vscode.TreeItem | Thenable<vscode.TreeItem> {
        return element;
    }

    getChildren(element?: Code | undefined): vscode.ProviderResult<Code[]> {
        if(!this.workspaceRoot){
            vscode.window.showErrorMessage('Cannot show generated code, a folder should be opened following File>Open path');
			return Promise.resolve([]);
        }
        if(isMavenProject(this.workspaceRoot)){

        }
        else{
            //read generated code from current root path.
            if(!element){
                return Promise.resolve(this.getCodeDirectoriesFromPath());
            }
            if(element.codeType ===  CodeType.Page && element.contextValue === "code"){
                return Promise.resolve(this.getCodeFiles(pageDirectoryPath,element.codeType));
            }
            if(element.codeType === CodeType.Test && element.contextValue === "code"){
                return Promise.resolve(this.getCodeFiles(testDirectoryPath, element.codeType));
            }
            if(element.codeType === CodeType.XML && element.contextValue === "code"){
                return Promise.resolve(this.getCodeFiles(xmlDirectoryPath, element.codeType));
            }
            return Promise.resolve([]);
        }
    }

    /**
     * Get files from based on code type.
     * @param dirPath path of directory
     * @param codeType type of generated code file
     */
    getCodeFiles(dirPath: string, codeType:CodeType) : TestCode[] | PageCode[] | XMLCode[]{
        const absoluteDirectoryPath = dirPath;
        if(!fs.existsSync(absoluteDirectoryPath)){
            vscode.window.showInformationMessage("There is no folder in current root path.");
            return [];
        }

        let files = fs.readdirSync(absoluteDirectoryPath);

        if(codeType === CodeType.XML){
            return files
            .filter(file => path.extname(file) === ".xml")
            .map(file => new XMLCode(file, vscode.TreeItemCollapsibleState.None));
        }
        else {
            files = files.filter(file => path.extname(file) === ".java");
            if(codeType === CodeType.Page){
                return files.map(file => new PageCode(file, vscode.TreeItemCollapsibleState.None));
            }
            else {
                return files.map(file => new TestCode(file, vscode.TreeItemCollapsibleState.None));
            }
        }
    }

    /**
     * Get directories from given paths.
     */
    getCodeDirectoriesFromPath() : Code[] {
        const absolutePagePath = pageDirectoryPath;
        const absoluteTestPath = testDirectoryPath;
        const absoluteXMLPath = xmlDirectoryPath;
        if(!fs.existsSync(absolutePagePath)){
            vscode.window.showInformationMessage("There is no Page folder in current root path.");
        }
        if(!fs.existsSync(absoluteXMLPath)){
            vscode.window.showInformationMessage("There is no XML folder in current root path.");
        }
        if(!fs.existsSync(absoluteTestPath)){
            vscode.window.showInformationMessage("There is no Test folder in current root path.");
            return [];
        }
        return [
            new Code(CodeType.Page,"Page",vscode.TreeItemCollapsibleState.Collapsed),
            new Code(CodeType.Test,"Test",vscode.TreeItemCollapsibleState.Collapsed),
            new Code(CodeType.XML, "XML",vscode.TreeItemCollapsibleState.Collapsed)
        ];
    }

    
    
}

export class Code extends vscode.TreeItem{ 
    private codeType_ : CodeType;

    constructor(
        codeType : CodeType,
		public readonly label: string,
        public readonly collapsibleState: vscode.TreeItemCollapsibleState) {
        super(label, collapsibleState);
        this.codeType_ = codeType;
    }

    iconPath = {
		light: path.join(__filename, '..', '..', 'resources', 'light', 'folder.svg'),
		dark: path.join(__filename, '..', '..', 'resources', 'dark', 'folder.svg')
	};

    contextValue = 'code';

    get codeType(): CodeType {
        return this.codeType_;
    }
}

export class PageCode extends Code{
    constructor(
		public readonly label: string,
		public readonly collapsibleState: vscode.TreeItemCollapsibleState) {
		super(CodeType.Page,label, collapsibleState);
    }

    iconPath = {
		light: path.join(__filename, '..', '..', 'resources', 'light', 'file_type_java.svg'),
		dark: path.join(__filename, '..', '..', 'resources', 'dark', 'file_type_java.svg')
	};

	contextValue = 'pageCode';
}

export class TestCode extends Code{
    constructor(
		public readonly label: string,
		public readonly collapsibleState: vscode.TreeItemCollapsibleState) {
		super(CodeType.Test,label, collapsibleState);
    }

    iconPath = {
		light: path.join(__filename, '..', '..', 'resources', 'light', 'file_type_java.svg'),
		dark: path.join(__filename, '..', '..', 'resources', 'dark', 'file_type_java.svg')
	};

	contextValue = 'testCode';
}

export class XMLCode extends Code{
    constructor(
		public readonly label: string,
		public readonly collapsibleState: vscode.TreeItemCollapsibleState) {
		super(CodeType.Test,label, collapsibleState);
    }

    iconPath = {
		light: path.join(__filename, '..', '..', 'resources', 'light', 'file_type_xml.svg'),
		dark: path.join(__filename, '..', '..', 'resources', 'dark', 'file_type_xml.svg')
	};

	contextValue = 'xmlCode';
}

enum CodeType {Test, Page, XML}