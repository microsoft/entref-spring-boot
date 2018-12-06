import * as React from 'react'
import * as renderer from 'react-test-renderer'

import { Person } from '../components/Person'

describe('<Person />', () => {
    it('renders correctly', () => {
        const tree = renderer
            .create(<Person path='/people'/>)
            .toJSON()
        expect(tree).toMatchSnapshot()
    })
})
