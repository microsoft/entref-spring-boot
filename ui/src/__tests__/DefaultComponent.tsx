import {
    createHistory,
    createMemorySource,
    LocationProvider,
} from '@reach/router'
import { render } from 'enzyme'
import * as React from 'react'
import * as renderer from 'react-test-renderer'

import { App } from '../components/App'
import { DefaultComponent } from '../components/DefaultComponent'

describe('<DefaultComponent />', () => {
    it('renders correctly', () => {
        const tree = renderer
            .create(<DefaultComponent default />)
            .toJSON()
        expect(tree).toMatchSnapshot()
    })

    describe('It is the default 404 page', () => {
        test('It finds the page if user tries a nonsense path', () => {
            const badPath = '/awefwaef'
            const source = createMemorySource(badPath)
            const hist = createHistory(source)
            const r = render(
                <LocationProvider history={hist}>
                    <App />
                </LocationProvider>,
                )

            expect(r.find('.default-component')).toHaveLength(1)
            expect(r.find('.default-component1')).toHaveLength(0)
        })

        test('It doesn\'t find the page if user tries a valid path', () => {
            const goodPath = '/titles'
            const source = createMemorySource(goodPath)
            const hist = createHistory(source)
            const r = render(
                <LocationProvider history={hist}>
                    <App />
                </LocationProvider>,
                )

            expect(r.find('.default-component')).toHaveLength(0)
            })
        })
    })
