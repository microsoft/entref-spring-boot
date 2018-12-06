import * as React from 'react'

import { AuthContext } from './AuthContext'

export class AuthResponseBar extends React.Component {
  public static contextType = AuthContext
  public render() {
    return this.context.authResponse && (
      <div className='auth-message-container'>
        <button className='auth-message-close-button' onClick={() => this.context.setAuthResponse(null)}>X</button>
        <span className='auth-message'>{this.context.authResponse}</span>
      </div>
    )
  }
}
