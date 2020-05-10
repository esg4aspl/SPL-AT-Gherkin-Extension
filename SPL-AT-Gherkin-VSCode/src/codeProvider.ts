import * as vscode from 'vscode';
import * as path from 'path';

export class CodeProvider implements vscode.TreeDataProvider<Code>{
    
    onDidChangeTreeData?: vscode.Event<Code | null | undefined> | undefined;

    getTreeItem(element: Code): vscode.TreeItem | Thenable<vscode.TreeItem> {
        return element;
    }

    getChildren(element?: Code | undefined): vscode.ProviderResult<Code[]> {
        console.log("Get children for Codes.");
        let pages : PageCode[] = [
            new PageCode("page.java", vscode.TreeItemCollapsibleState.None)
        ];
        let tests : TestCode[] = [
            new TestCode("test.java", vscode.TreeItemCollapsibleState.None)
        ];
        let folders : Code[] = [
            new Code(CodeType.Page,"Page",vscode.TreeItemCollapsibleState.Collapsed),
            new Code(CodeType.Test,"Test",vscode.TreeItemCollapsibleState.Collapsed)
        ];
        if(!element){
            return folders;
        }
        if(element.codeType ===  CodeType.Page && element.contextValue === "code"){
            return pages;
        }
        if(element.codeType === CodeType.Test && element.contextValue === "code"){
            return tests;
        }
        return Promise.resolve([]);
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

enum CodeType {Test, Page}