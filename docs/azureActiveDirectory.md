# Azure Active Directory

> Note: This document roughly follows [this tutorial](https://docs.microsoft.com/en-us/azure/active-directory/develop/quickstart-v1-add-azure-ad-app).

To enable authentication, one may wish to use [Azure Active Directory (AAD)](https://azure.microsoft.com/en-us/services/active-directory/).
This document will describe how to configure and leverage AAD for this application.

## Setup AAD App

> TL;DR: After these steps you should have two values noted - an `Application ID` and an `App ID URI`.

1. Login to [Azure Portal](https://portal.azure.com)
2. Select "Azure Active Directory" from the portal left-hand sidebar
3. Select "App Registrations" from the left-most panel
4. Select "New Application" from the top panel
5. Name your application, and enter a valid url for sign-on (hint: we use the url of our hosted service)
6. When the application has been created, the newly visible panel will display its "Application ID" - please note its value
7. Select "Settings" from the top panel
8. Select "Properties" from the right-most panel
9. Find "App ID URI" in the panel, note its value

## Configure SpringDAL

> Note: Authentication is only enabled in the `production` profile.
> TL;DR: After these steps you should have two application environment variables set, `security.oauth2.resource.jwk.key-set-uri` and `security.oauth2.resource.id`.

Ensure that the `security.oauth2.resource.jwk.key-set-uri` environment variable is set to `https://login.microsoftonline.com/common/discovery/keys` - this is because a common key set is used for all Azure Active Directory applications. This will validate that your service only allows valid AAD tokens, but it won't yet verify that the token is for your application.

To ensure the token was granted for your application, you must set `security.oauth2.resource.id` to your `App ID URI` as noted [above](#setup-aad-app).

## Test Authentication

> Note: Authentication is only enabled in the `production` profile.

To ensure authentication is working properly, we need to issue ourselves a token and validate it works. To do so, we'll use [Postman](https://www.getpostman.com/). Please download and install it now.

### Configure AAD Test information

> TL;DR: After these steps you should have one value noted - a `Value`.

1. Login to [Azure Portal](https://portal.azure.com)
2. Select "Azure Active Directory" from the portal left-hand sidebar
3. Select "App Registrations" from the left-most panel
4. Enter your `Application ID` as noted above, into the search field
5. Click your application, to enter its panel
6. Select "Settings" from the top panel
7. Select "Reply URLs" from the right-most panel
8. Ensure `https://www.getpostman.com/oauth2/callback` is the only reply URL entry
9. Select "Save" from the top panel
10. Close the "Reply URLs" panel by selecting the "x" in the top right
11. Select "Settings" from the top panel
12. Select "Keys" from the right-most panel
13. Under "Passwords" type a new "Key Description" in the field, and choose an expiration time
14. Select "Save" from the top panel - please note the value that appears in the `Value` field

### Configure Postman Test information

> TL;DR: After these steps you should have one value noted - an `access_token`.

1. Open Postman
2. Navigate to the Body panel
3. Select "x-www-form-urlencoded"
4. Populate the table that appears with the following values
    + `grant_type`: `client_credentials`
    + `client_id`: `<yourApplicationId>` where `<yourApplicationId>` is `Application ID` from above
    + `client_secret`: `<yourKey>` where `<yourKey>` is the Key `Value` from above
    + `resource`: `<yourApplicationIdUrl>` where `<yourApplicationIdUrl>` is `App ID URI` from above
5. Change the method in the address bar to "POST"
6. Enter `https://login.microsoftonline.com/microsoft.onmicrosoft.com/oauth2/token` for the url
7. Select "Send"
8. The "Body" section of the bottom pane should now be populated
9. Select `access_token` from the "Body" section - please note its value

### Run test

> TL;DR: After these steps you should know if authentication is working properly!

1. Start the `SpringDAL` application with the `production` profile (see the Readme for more information)
2. Open Postman
3. Change the method in the address bar to "GET"
4. Enter `http://localhost:8080/` for the url
5. Navigate to the Headers panel
6. Add one header in the table - `Authorization`: `Bearer <yourAccessToken>` where `<yourAccessToken>` is `access_token` from [above](#configure-postman-test-information)
7. Issue the Postman request by clicking "Send"
8. Validate that Postman shows a successful response
9. Navigate to "Headers" in Postman
10. Toggle off the "Authorization" header by clicking the checkbox next to it
11. Issue the Postman request by clicking "Send"
12. Validate that Postman shows a failure response