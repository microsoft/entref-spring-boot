import * as React from 'react'

export interface IDefaultComponentProps {
    default: boolean,
}

export class DefaultComponent extends React.Component<IDefaultComponentProps> {
    public render() {
        return (
            <div className='default-component cover'>
                <h1>Error 404</h1>
                <p className='lead'>
                    The requested resource could not be found but may be available again in the future.
                </p>
            </div>
        )
    }
}
