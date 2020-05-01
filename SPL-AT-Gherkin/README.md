# SPL-AT-Gherkin

SPL-AT-Gherkin is a domain specific language which is patched from [Gherkin](https://cucumber.io/docs/gherkin/). 

## Installation

You can clone the repository.

```bash
git clone https://github.com/esg4aspl/SPL-AT-Gherkin-Extension.git
```

## Usage
After cloning the repository, you can extract jar file from maven project. Then, you can generate Test classes, Page classes and testng.xml files automatically with it.
Feature files which are written in SPL-AT-Gherkin language should be prepared before.

:exclamation: Note : Please ensure that all bash scripts are located on same line. 

```bash
java -cp SPL-AT-Gherkin.jar GherkinParser.MainProgram
    PATH_OF_Feature_DIRECTORY
    PATH_OF_Test_DIRECTORY
    PATH_OF_Page_DIRECTORY
    PATH_OF_XML_DIRECTORY
```
When generation is done successfully, information message is going to be appeared on console.
```bash
Test classes, page classes and testng xml files are generated successfully.
Test Classes path is PATH_OF_Test_DIRECTORY
Page Classes path is PATH_OF_Page_DIRECTORY
testng xml file path is PATH_OF_XML_DIRECTORY
```
## Contributing
Pull requests are welcome. For major changes, please open an issue first to discuss what you would like to change.
