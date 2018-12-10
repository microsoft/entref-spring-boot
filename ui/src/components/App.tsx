import { Router } from '@reach/router'
import * as Msal from 'msal'
import * as React from 'react'

import { AuthProvider } from './AuthContext'
import { AuthResponseBar } from './AuthResponseBar'
import { DefaultComponent } from './DefaultComponent'
import { Home } from './Home'
import { Navbar } from './Navbar'
import { Person } from './Person'
import { PrivateRoute } from './PrivateRoute'
import { Title } from './Title'

import './../styles/App.css'

const appConfig = {
    clientID: WEBPACK_PROP_AAD_CLIENT_ID,
}

// initialize the UserAgentApplication globally so popup and iframe can run in the background
const userAgentApp = appConfig.clientID && new Msal.UserAgentApplication(appConfig.clientID, null, null)
// short circuit userAgentApp. If clientID is an empty string so is userAgentApp

export class App extends React.Component {

    // App is still responsible for managing the auth state
    // it uses the AuthContext to share this with other aspects of the UI
    // including the navbar, private route component, and the pages themselves
    public state = {
        accessToken: null,
        authResponse: null,
    }

    public handleAuth = async () => {
        if (!appConfig.clientID) {
            if (this.state.accessToken !== null) {
                this.setState({ accessToken: null })
            } else {
                // tslint:disable-next-line no-console max-line-length
                console.warn('AAD Client ID has not been configured. If you are currently in production mode, see the \'deploy\' documentation for details on how to fix this.')
                this.setState({ accessToken: '' })
            }
        } else {
            let accessToken = null
            try {
                if (this.state.accessToken) {
                    // log out
                    await userAgentApp.logout()
                } else {
                    // log in
                    if (!appConfig.clientID) {
                        throw new Error(
                            'AAD Client ID has not been configured. See the \'deploy\' documentation for more details.')
                    }
                    const graphScopes = [appConfig.clientID]
                    await userAgentApp.loginPopup(graphScopes)
                    accessToken = await userAgentApp.acquireTokenSilent(graphScopes,
                        'https://login.microsoftonline.com/microsoft.onmicrosoft.com')
                }
                this.setState({ accessToken })
            } catch (err) {
                this.setAuthResponse(err.toString())
            }
        }
    }

    public setAuthResponse = (msg: string) => {
        this.setState({ authResponse: msg })
    }

    public render() {
        // Implementing the authprovider at app root to share the auth state
        // with all internal components (if they subscribe to it)
        // by linking the accessToken to the app state we can be certain the
        // context will always update and propogate the value to subscribed nodes
        return (
            <AuthProvider value={{
                accessToken: this.state.accessToken,
                authResponse: this.state.authResponse,
                handleAuth: this.handleAuth,
                setAuthResponse: this.setAuthResponse,
            }}>
                <div className='app-container'>
                    <Navbar />
                    <AuthResponseBar />
                    <Router>
                        <Home path='/' />
                        <PrivateRoute as={Person} path='/people' />
                        <PrivateRoute as={Title} path='/titles' />
                        <DefaultComponent default />
                    </Router>
                </div>
            </AuthProvider>
        )
    }
}
