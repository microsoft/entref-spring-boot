import * as React from 'react'

export interface IDefaultComponentProps {
    default: boolean,
}

export interface IDefaultComponentState {
    deerSrc: string,
}

export class DefaultComponent extends React.Component<IDefaultComponentProps> {
    public state = {
        deerSrc: null,
    }

    public componentWillMount() {
        this.setState({ deerSrc: 'https://i.gifer.com/KG8.gif' })
    }

    public render() {
        return (
            <div className='default-component cover'>
                <h1>
                    Oh Deer :(
                <br />
                    <small>
                        Error 404
                    </small>
                </h1>
                <p className='lead'>
                    The requested resource could not be found but may be available again in the future.
            </p>
                <img className='default-page-image' src={this.state.deerSrc} />
            </div>
        )
    }
}
