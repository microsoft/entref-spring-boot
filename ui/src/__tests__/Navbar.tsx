import {mount } from 'enzyme'
import * as React from 'react'
import * as renderer from 'react-test-renderer'

import { Navbar } from '../components/Navbar'

describe('<Navbar />', () => {
    it('renders correctly', () => {
        const tree = renderer
            .create(<Navbar />)
            .toJSON()
        expect(tree).toMatchSnapshot()
    })
    it('should activate Home link by default', () => {
        const navbarWrapper = mount(<Navbar />)
        // @reach/router sets the 'aria-current' property to 'page' when the Link element is active
        // The Navbar component adds the 'active' class to the className list too
        const linkElementWrapper = navbarWrapper.find({
            'aria-current': 'page',
            'className': 'nav-link active',
            'href': '/',
        })
        expect(linkElementWrapper.text()).toBe('Home')
    })
})
