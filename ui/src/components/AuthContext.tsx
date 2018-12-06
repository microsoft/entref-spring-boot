import { createContext } from 'react'

export interface IAuthContextValues {
  accessToken: string,
  authResponse: string,
  handleAuth: () => void,
  setAuthResponse: (msg?: string) => void,
}

// Default values and enforce use of interface
const defaultAuthContextValue: IAuthContextValues = {
  accessToken: null,
  authResponse: null,
  handleAuth: () => null,
  setAuthResponse: () => null,
}

export const AuthContext = createContext<IAuthContextValues>(defaultAuthContextValue)
// exporting the Provider and Consumer components for more specific imports
export const AuthProvider = AuthContext.Provider
export const AuthConsumer = AuthContext.Consumer
