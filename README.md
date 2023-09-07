## PoC project (integration of YAF testing framework with Gradle project)

#### Usage:

* Building the project and running tests:
  - Fill in the data for running tests **featureFiles** and **testTags** in the **gradle.properties** file
  - Execute the Gradle task **fullCycleST**
<br/><br/>
* Available Gradle tasks:
  - [ **rollUp** ]          downloads images and deploys the microservice environment locally
  - [ **runTests** ]        runs tests and validates the TM; results are saved in the Allure folder
  - [ **tearDown** ]        tears down the environment and removes containers (images remain)
  - [ **checkYmlStruct** ]  checks the correctness of the structure of the provided feature files
  - [ **fullCycleST** ]     initiates the entire testing cycle
<br/><br/>
* Test run reports do not overlap but get updated with each execution of the [ **runTests** ] command;\
  Open _src/test/resources/test_model/allure-report/index.html_ in a web browser to view the **_Allure_** report\
  Open build/reports/jacoco/test/html/index.html in a web browser to view the **_Jacoco_** report (code coverage for _Sonar_)
<br/><br/>
* The feature file is validated during preparation and transmission to the test executor\
  _test_model/features_ -  is your Test Model
<br/><br/>
> :warning: Fill the **testTags** variable with **_YOUR_TAGS_EXPRESSION_** or leave it empty
```yaml
    ...
  - name: Test singleton - without TD
    tags: ['beta_v3', 'alpha_v1.1']
    steps:
      ...
```
* **_YOUR_TAGS_EXPRESSION_** is an expression that filters the test collection (running only those tests for which the expression is true). For example:
  - beta_v3 - will run all tests whose tags: field contains the value beta_v3
  - not alpha_v1.1 - will NOT run only tests whose tags: field contains the value alpha_v1.1
  - not (beta_v3 or delta_v2) - will NOT run only tests whose tags: field contains the value delta_v2 or beta_v3
  - the absence of a value will run all tests for the selected features
  - you can assign several tags to a test
  - do NOT use spaces in tags
