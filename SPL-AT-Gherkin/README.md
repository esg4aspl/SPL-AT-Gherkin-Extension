# cherry README <img width="60px" height="60px" src="https://github.com/esg4aspl/SPL-AT-Gherkin-Extension/blob/master/SPL-AT-Gherkin-VSCode/resources/light/cherry.svg" />

cherry is a domain specific language which is typed Given-When-Then style like [Gherkin](https://cucumber.io/docs/gherkin/). 

## What is the purpose of cherry language? 💡
Implemented test automation project is generated automatically with [tomato](https://github.com/esg4aspl/SPL-AT-Gherkin-Extension/tree/master/SPL-AT-Gherkin-VSCode "tomato") extension which understand cherry language. Generated codes are also implemented with [PageObject design pattern](https://martinfowler.com/bliki/PageObject.html "PageObject") and you can also access these implementations.

## How to use cherry language?
Please check [wiki](https://github.com/esg4aspl/SPL-AT-Gherkin-Extension/wiki "cherry") page to understand language and its fatures in detail.

## Installation

You can clone the repository.

```bash
git clone https://github.com/esg4aspl/SPL-AT-Gherkin-Extension.git
```

## Usage
After cloning the repository, you can extract jar file from maven project. Then, you can generate Test classes, Page classes and testng.xml files automatically with it.
Feature files which are written in cherry language should be prepared before. Jar file which is located on out/artifacts/SPL_AT_Gherkin_jar also could be used for generation. 

:exclamation: Note : Please ensure that all bash scripts are located on same line. 

```bash
java -cp SPL-AT-Gherkin.jar GherkinParser.MainProgram
    /Users/XXX/Desktop/sample/Feature/
    /Users/XXX/Desktop/sample/Test/
    /Users/XXX/Desktop/sample/Page/
    /Users/XXX/Desktop/sample/XML/
```
When generation is done successfully, information message is going to be appeared on console.
```bash
Test classes, page classes and testng xml files are generated successfully.
Test Classes path is /Users/XXX/Desktop/sample/Test/
Page Classes path is /Users/XXX/Desktop/sample/Page/
testng xml file path is /Users/XXX/Desktop/sample/XML/
```
## Contributing
Pull requests are welcome. For major changes, please open an issue first to discuss what you would like to change.
