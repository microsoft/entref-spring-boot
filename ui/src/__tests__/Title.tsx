import * as React from 'react'
import * as renderer from 'react-test-renderer'

import { Title } from '../components/Title'

describe('<Title />', () => {
    it('renders correctly', () => {
        const tree = renderer
            .create(<Title path='/titles'/>)
            .toJSON()
        expect(tree).toMatchSnapshot()
    })
})
