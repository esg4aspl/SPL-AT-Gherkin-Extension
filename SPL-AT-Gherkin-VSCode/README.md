# tomato README <img width="60px" height="60px" src="media/tomato-color.png" />

tomato is a VSCode extension. You can create your maven test automation project for mobile applications based on feature files which are written in [cherry](https://github.com/esg4aspl/SPL-AT-Gherkin-Extension/tree/master/SPL-AT-Gherkin "cherry")  language.

## How to Configure Maven Project to Tomato
Firstly, create an empty [maven project](https://maven.apache.org/guides/getting-started/maven-in-five-minutes.html#creating-a-project), then create Page, Feature and Test folders and configure following paths
* Feature directory path that includes feature files which are written in cherry language.<br/>Example : **/Users/username/projectFolder/featureFolder/**
* Page directory path that includes auto-generated page classes.<br/>Example : **/Users/username/projectFolder/pageFolder/**
* Test directory path that includes auto-generated test classes.<br/>Example : **/Users/username/projectFolder/testFolder/**
* Directory of testng.xml path.<br/>Example : **/Users/username/projectFolder/testngXMLFolder/**

![](resources/gif/how_to_configure_tomato_2.gif)

> Tip: Do not forget set your project paths for Page, Test, Feature and XML files.

## Requirements

Your automation project should be created as maven project.

## Example Projects created with Tomato

* [Instagram Android Application](https://github.com/sercanparker/Cherry-Samples/tree/master/instagram-case-study)

## Known Issues

Calling out known issues can help limit users opening duplicate issues against your extension.

## Release Notes

You can create and generate your automation project code with cherry language. 

### 0.0.1

Initial release of tomato

-----------------------------------------------------------------------------------------------------------




