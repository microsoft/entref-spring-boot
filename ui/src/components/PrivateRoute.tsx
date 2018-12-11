import * as React from 'react'

import { AuthContext } from './AuthContext'
import { Home } from './Home'

export interface IPrivateRouteProps { as: React.ComponentType, path: string }

export class PrivateRoute extends React.Component<IPrivateRouteProps> {
  public static contextType = AuthContext

  public componentDidMount() {
    if (!this.context.accessToken) {
      this.context.setAuthResponse(`Please log in to access: ${this.props.path}`)
    }
  }

  public componentDidUpdate(prevProps, prevState, snapshot) {
    if (!this.context.accessToken && this.props.path !== prevProps.path) {
      this.context.setAuthResponse(`Please log in to access: ${this.props.path}`)
    }
  }

  public componentWillUnmount() {
    this.context.setAuthResponse(null)
  }

  public render() {
    const { as: Component, ...props } = this.props
    // this private route uses the existence of the accessToken to
    // lock/unlock the private routes. We don't pass down the values from
    // context as we would rather subscribe to them directly in the
    // components themselves.
    return this.context.accessToken !== null ? <Component {...props} /> : <Home {...props} />
  }
}
