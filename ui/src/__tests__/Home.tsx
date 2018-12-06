import { mount } from 'enzyme'
import * as React from 'react'
import * as renderer from 'react-test-renderer'

import { Home } from '../components/Home'

describe('<Home />', () => {
    it('renders correctly', () => {
        const tree = renderer
            .create(<Home path='/'/>)
            .toJSON()
        expect(tree).toMatchSnapshot()
    })
})
