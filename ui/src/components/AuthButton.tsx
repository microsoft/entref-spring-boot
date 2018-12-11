import * as React from 'react'

import { AuthContext } from './AuthContext'

export class AuthButton extends React.Component {
    public static contextType = AuthContext
    public render() {
        return (
            <button
                onClick={this.context.handleAuth}
                className='login-button'
            >
                {this.context.accessToken !== null ? 'Log Out' : 'Log In'}
            </button>
        )
    }
}
