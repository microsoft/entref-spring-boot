import { Link } from '@reach/router'
import * as React from 'react'

import { AuthButton } from './AuthButton'

import './../styles/Navbar.css'

const isActive = ({ isCurrent }) => {
    return {
        className: isCurrent ? 'nav-link active' : 'nav-link',
    }
}

export interface INavbarProps { basepath?: string }

export class Navbar extends React.Component<INavbarProps> {
    public render() {
        const basepath = this.props.basepath || ''

        return (
            <nav className='nav-container'>
                <span className='nav-title'>
                    Project Jackson
                </span>
                <div className='nav-link-container'>
                    <Link getProps={isActive} to={`${basepath}/`}>Home</Link>
                    <Link getProps={isActive} to={`${basepath}/people`}>People</Link>
                    <Link getProps={isActive} to={`${basepath}/titles`}>Titles</Link>
                    <AuthButton />
                </div>
            </nav>
        )
    }
}
