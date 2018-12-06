import * as React from 'react'

import { AuthContext } from './AuthContext'
import { PageForm } from './PageForm'

export interface IPersonProps { path: string }

export interface IPersonState { loading: boolean, personId: string, result: object }

export interface IPersonResponse extends Response {
  _embedded?: { persons: Array<{ nconst: string }> },
  error?: string,
  error_description?: string
}

export interface IPersonResult { nconst: string }

export class Person extends React.Component<IPersonProps, IPersonState> {
  public static contextType = AuthContext

  public state = {
    loading: false,
    personId: null,
    result: null,
  }

  public render() {
    return (
      <div className='page-container'>
        <div className='form-container'>
          <h1>Search People</h1>
          <p className='form-description'>Enter the ID for a person to get their information from the database.
            Leave the form empty for a random sample of people from the database.</p>
          <PageForm
            inputTitle='Person ID:'
            inputPlaceholder='Person ID'
            onInputChange={this.handleNameInputChange}
            onSubmitClick={this.handleFormSubmit}
          />
        </div>
        <div className='results-container'>
          <h2 className='results-title'>{`Results for PersonId: ${this.state.result ? this.state.personId : ''}`}</h2>
          <pre className='results-view'>{JSON.stringify(this.state.result, null, 2)}</pre>
          <h4>{this.state.loading ? 'Loading. . .' : null}</h4>
        </div>
      </div>
    )
  }

  private handleNameInputChange = (event) => this.setState({
    personId: event.target.value,
    result: null,
  })

  private handleFormSubmit = async (event) => {
    event.preventDefault()

    this.setState({ loading: true })

    // set up endpoint
    const id = this.state.personId && this.state.personId.replace(/\s+/g, '')
    const base = WEBPACK_PROP_PEOPLE_BASE_URL
    const endpoint = id ? base + id : base

    // set up request header with Bearer token
    const headers = new Headers()
    const bearer = `Bearer ${this.context.accessToken}`
    headers.append('Authorization', bearer)
    const options = {
      headers,
      method: 'GET',
    }

    try {
      const response = await fetch(endpoint, options)
      const resOut: IPersonResponse = await response.json()

      if (resOut.hasOwnProperty('error')) {
        throw new Error(`Error: ${resOut.error}, ${resOut.error_description}`)
      } else if (id) {
        this.setState({ result: resOut })
      } else {
        const persons = resOut._embedded.persons
        const result: IPersonResult = persons[Math.floor(Math.random() * persons.length)]
        this.setState({ result, personId: result.nconst })
      }
    } catch (err) {
      this.setState({ result: { error: err.message } })
    } finally {
      this.setState({ loading: false })
    }
  }
}
