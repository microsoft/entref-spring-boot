import * as React from 'react'

import { AuthContext } from './AuthContext'
import { PageForm } from './PageForm'

export interface ITitleProps { path: string }

export interface ITitleState { loading: boolean, titleId: string, result: object }

export interface ITitleResponse extends Response {
  _embedded?: { titles: Array<{ tconst: string }> },
  error?: string,
  error_description?: string
}

export interface ITitleResult { tconst: string }

export class Title extends React.Component<ITitleProps, ITitleState> {
  public static contextType = AuthContext

  public state = {
    loading: false,
    result: null,
    titleId: null,
  }

  public render() {
    return (
      <div className='page-container'>
        <div className='form-container'>
          <h1>Search Titles</h1>
          <p className='form-description'>Enter the ID for a title to get their information from the database.
            Leave the form empty for a random sample of people from the database.</p>
          <PageForm
            inputTitle='Title ID:'
            inputPlaceholder='Title ID'
            onInputChange={this.handleNameInputChange}
            onSubmitClick={this.handleFormSubmit}
          />
        </div>
        <div className='results-container'>
          <h2 className='results-title'>{`Results for TitleId: ${this.state.result ? this.state.titleId : ''}`}</h2>
          <pre className='results-view'>{JSON.stringify(this.state.result, null, 2)}</pre>
          <h4>{this.state.loading ? 'Loading. . .' : null}</h4>
        </div>
      </div>
    )
  }

  private handleNameInputChange = (event) => this.setState({
    result: null,
    titleId: event.target.value,
  })

  private handleFormSubmit = async (event) => {
    event.preventDefault()

    this.setState({ loading: true })

    // set up endpoint
    const id = this.state.titleId && this.state.titleId.replace(/\s+/g, '')
    const base = WEBPACK_PROP_TITLE_BASE_URL
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
      const resOut: ITitleResponse = await response.json()

      if (resOut.hasOwnProperty('error')) {
        throw new Error(`Error: ${resOut.error}, ${resOut.error_description}`)
      } else if (id) {
        this.setState({ result: resOut })
      } else {
        const titles = resOut._embedded.titles
        const result: ITitleResult = titles[Math.floor(Math.random() * titles.length)]
        this.setState({ result, titleId: result.tconst })
      }
    } catch (err) {
      this.setState({ result: { error: err.message } })
    } finally {
      this.setState({ loading: false })
    }
  }
}
