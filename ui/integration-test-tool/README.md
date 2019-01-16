# Integration test tool

This tool runs integration tests against production instances

## How To Use

Follow the steps in the sections below to get up and running!

### Install dependencies

Run `npm i` from the project directory

### Configure Environment Variables

On Windows:

```
set TARGET_BACKEND_SITE=<backendBaseUrl>
set TARGET_FRONTEND_SITE=<frontendBaseUrl>
set TARGET_USERNAME=<aadUsername>
set TARGET_PASSWORD=<aadPassword>
```

On Linux:

```
export TARGET_BACKEND_SITE=<backendBaseUrl>
export TARGET_FRONTEND_SITE=<frontendBaseUrl>
export TARGET_USERNAME=<aadUsername>
export TARGET_PASSWORD=<aadPassword>
```

Where...

+ `backendBaseUrl` - The URL of the backend service that hosts `Title` and `Person` endpoints
+ `frontendBaseUrl` - The URL of the frontend service that hosts the UI
+ `aadUsername` - The Username of some Azure Active Directory Account
+ `aadPassword` - The password of some Azure Active Directory Account

### Run

Run `npm test` from the project directory

## FAQ

### What browser does this use to simulate user interaction?

We use [puppeteer](https://github.com/GoogleChrome/puppeteer) which, in turn uses Chrome.

### What values do I use for the base URL properties?

If you're running our service on Azure and are using Application Gateway to front the traffic (as is default) you can point `backendBaseUrl` at `http://<yourAppGatewayIp>` or `http://<yourConfiguredDnsName>`, and `frontendBaseUrl` at `http://<yourAppGatewayIp>/ui` or `http://yourConfiguredDnsName>/ui`.

### How can I generate an AAD account?

If you're using AAD internal user accounts, you can create a new one following along with [this article](https://docs.microsoft.com/en-us/azure/active-directory/fundamentals/add-users-azure-active-directory). If you're using AAD public Microsoft accounts, you can create a new a new outlook account [here](https://outlook.com).

### How can I determine which type of AAD account I need to use?

This is in reference to [the above question](#how-can-i-generate-an-aad-account) - To determine which account type you are using you can inspect your AAD application's manifest. If your manifest contains `signInAudience: AzureADAndPersonalMicrosoftAccount` then it supports public Microsoft accounts. If it does not, then you support only AAD internal user accounts. For reference, see [this document](https://docs.microsoft.com/en-us/azure/active-directory/develop/reference-app-manifest).