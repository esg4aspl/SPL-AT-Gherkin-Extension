export let featureDirectoryPath : string;
export let pageDirectoryPath : string;
export let xmlDirectoryPath : string;
export let testDirectoryPath : string;
export let featureFileRegex = /^.+\s.+$/g;;

export function setFeatureDirectoryPath(path: string) {
    featureDirectoryPath = path;
}
export function setPageDirectoryPath(path: string){
    pageDirectoryPath = path;
}
export function setXMLDirectoryPath(path: string){
    xmlDirectoryPath = path;
}
export function setTestDirectoryPath(path: string){
    testDirectoryPath = path;
}


