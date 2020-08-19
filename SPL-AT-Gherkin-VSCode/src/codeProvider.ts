import * as vscode from 'vscode';
import * as path from 'path';
import * as fs from 'fs';
import { setPageDirectoryPath, setTestDirectoryPath, setXMLDirectoryPath, pageDirectoryPath, testDirectoryPath, xmlDirectoryPath } from './global';

export class CodeProvider implements vscode.TreeDataProvider<Code>{
    
    private _onDidChangeTreeData: vscode.EventEmitter<Code | undefined> = new vscode.EventEmitter<Code | undefined>();

    readonly onDidChangeTreeData: vscode.Event<Code | undefined> = this._onDidChangeTreeData.event;

    private isConfigDone : boolean = false;

    refresh(): void {
        this._onDidChangeTreeData.fire(undefined);
        vscode.window.showInformationMessage("Code files are refreshed.");
    }
    
    execute():void{
        
    }

    constructor(private workspaceRoot: string | undefined){
        const config = vscode.workspace.getConfiguration('spl-at-gherkin-vscode');
        if(!config.get("pagePath")){
            vscode.window.showErrorMessage("Please enter Page directory path from settings.");
        }
        if(!config.get("testPath")){
            vscode.window.showErrorMessage("Please enter Test directory path from settings.");
        }
        if(!config.get("XMLPath")){
            vscode.window.showErrorMessage("Please enter XML directory path from settings.");
        }
        else {
            this.isConfigDone = true;
            setPageDirectoryPath(String(config.get("pagePath")));
            setTestDirectoryPath(String(config.get("testPath")));
            setXMLDirectoryPath(String(config.get("XMLPath")));
        }
        
    }

    getTreeItem(element: Code): vscode.TreeItem | Thenable<vscode.TreeItem> {
        return element;
    }

    getChildren(element?: Code | undefined): vscode.ProviderResult<Code[]> {
        if(!this.isConfigDone){
            return Promise.resolve([]);
        }
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

    /**
     * Get files from based on code type.
     * @param dirPath path of directory
     * @param codeType type of generated code file
     */
    getCodeFiles(dirPath: string, codeType:CodeType) : TestCode[] | PageCode[] | XMLCode[]{
        const absoluteDirectoryPath = dirPath;
        if(!fs.existsSync(absoluteDirectoryPath)){
            vscode.window.showInformationMessage("There is no folder for given path. Path is " + dirPath);
            return [];
        }

        let files = fs.readdirSync(absoluteDirectoryPath);

        if(codeType === CodeType.XML){
            return files
            .filter(file => path.extname(file) === ".xml")
            .map(file => new XMLCode(file, 
                vscode.TreeItemCollapsibleState.None,
                {
                    command: 'spl-at-gherkin-vscode.openEditor',
                    title: '',
                    arguments: [(dirPath + "/" + file)]
                }));
        }
        else {
            files = files.filter(file => path.extname(file) === ".java");
            if(codeType === CodeType.Page){
                return files.map(file => new PageCode(file, 
                    vscode.TreeItemCollapsibleState.None,
                    {
                        command: 'spl-at-gherkin-vscode.openEditor',
                        title: '',
                        arguments: [(dirPath + "/" + file)]
                    }));
            }
            else {
                return files.map(file => new TestCode(file, 
                    vscode.TreeItemCollapsibleState.None,
                    {
                        command: 'spl-at-gherkin-vscode.openEditor',
                        title: '',
                        arguments: [(dirPath + "/" + file)]
                    }));
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
            vscode.window.showInformationMessage("There is no Page folder from given path. Path is " + absolutePagePath);
        }
        if(!fs.existsSync(absoluteXMLPath)){
            vscode.window.showInformationMessage("There is no XML folder from given path. Path is " + absoluteXMLPath);
        }
        if(!fs.existsSync(absoluteTestPath)){
            vscode.window.showInformationMessage("There is no Test folder from given path. Path is " + absoluteTestPath);
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
        public readonly collapsibleState: vscode.TreeItemCollapsibleState,
        public readonly command?: vscode.Command) {
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
		public readonly collapsibleState: vscode.TreeItemCollapsibleState,
        public readonly command?: vscode.Command) {
		super(CodeType.Page,label, collapsibleState,command);
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
		public readonly collapsibleState: vscode.TreeItemCollapsibleState,
        public readonly command?: vscode.Command) {
		super(CodeType.Test,label, collapsibleState,command);
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
		public readonly collapsibleState: vscode.TreeItemCollapsibleState,
        public readonly command?: vscode.Command) {
		super(CodeType.Test,label, collapsibleState,command);
    }

    iconPath = {
		light: path.join(__filename, '..', '..', 'resources', 'light', 'file_type_xml.svg'),
		dark: path.join(__filename, '..', '..', 'resources', 'dark', 'file_type_xml.svg')
	};

	contextValue = 'xmlCode';
}

enum CodeType {Test, Page, XML}