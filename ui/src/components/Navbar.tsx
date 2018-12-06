import { Link } from '@reach/router'
import * as React from 'react'

import { AuthButton } from './AuthButton'

import './../styles/Navbar.css'

const isActive = ({ isCurrent }) => {
    return {
        className: isCurrent ? 'nav-link active' : 'nav-link',
    }
}

export class Navbar extends React.Component {
    public render() {
        return (
            <nav className='nav-container'>
                <span className='nav-title'>
                    Project Jackson
                </span>
                <div className='nav-link-container'>
                    <Link getProps={isActive} to='/'>Home</Link>
                    <Link getProps={isActive} to='/people'>People</Link>
                    <Link getProps={isActive} to='/titles'>Titles</Link>
                    <AuthButton />
                </div>
            </nav>
        )
    }
}
