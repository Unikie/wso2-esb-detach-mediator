# WSO2 ESB Detach Mediator

## What is WSO2 ESB?
[WSO2 ESB](http://wso2.com/products/enterprise-service-bus/) is an open source Enterprise Service Bus that enables interoperability among various heterogeneous systems and business applications.

## Features
Detach Mediator is an helper mediator to detach element(s) or attribute(s) from payload. Mediator modifies original payload.

## Usage

### 1. Install the mediator to the ESB
Copy the `DetachMediator-x.y.jar` to `$WSO2_ESB_HOME/repository/components/dropins/`.

### 2. Use it in your proxies/sequences
Mediator can be used as follows:
```xml
<detach xpath="expression"/>
```

#### Example: detach by element(s)
```xml
<detach xpath="//element"/>
```

#### Example: detach by attribute
```xml
<detach xpath="//element/@attribute"/>
```

## Technical Requirements

#### Usage

* Oracle Java 6 or above
* WSO2 ESB
    * Wrapper Mediator has been tested with WSO2 ESB versions 4.9.0 & 5.0.0

#### Development

* Java 6 + Maven 3.0.X

### Contributors

- [Kreshnik Gunga](https://github.com/kgunga)
- [Jarkko Kallio](https://github.com/kallja)
- [Vesa Pohjola](https://github.com/vesapohjola)
- [Ville Harvala](https://github.com/vharvala)

## [License](LICENSE)

Copyright &copy; 2016 [Mystes Oy](http://www.mystes.fi). Licensed under the [Apache 2.0 License](LICENSE).
